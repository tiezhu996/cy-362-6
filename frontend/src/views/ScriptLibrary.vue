<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import type { Script, Review } from "../types";
import { fetchScripts, fetchReviewsByScript } from "../api/client";
import ReviewList from "../components/ReviewList.vue";

const scripts = ref<Script[]>([]);
const loading = ref(false);
const activeScriptId = ref<number | null>(null);
const reviews = ref<Review[]>([]);
const avgRating = ref<number | null>(null);
const reviewCount = ref(0);
const loadingReviews = ref(false);

const activeScript = computed(() => {
  return scripts.value.find((s) => s.id === activeScriptId.value) || null;
});

async function loadScripts() {
  loading.value = true;
  try {
    scripts.value = await fetchScripts();
    if (scripts.value.length > 0 && !activeScriptId.value) {
      activeScriptId.value = scripts.value[0].id;
      loadReviews(scripts.value[0].id);
    }
  } catch (e) {
    ElMessage.error("加载剧本列表失败");
  } finally {
    loading.value = false;
  }
}

async function loadReviews(scriptId: number) {
  loadingReviews.value = true;
  try {
    const data = await fetchReviewsByScript(scriptId);
    reviews.value = data.reviews;
    avgRating.value = data.avgScriptRating;
    reviewCount.value = data.reviewCount;
  } catch (e) {
    ElMessage.error("加载评价失败");
  } finally {
    loadingReviews.value = false;
  }
}

function selectScript(script: Script) {
  activeScriptId.value = script.id;
  loadReviews(script.id);
}

function formatDuration(minutes: number) {
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  return mins > 0 ? `${hours}小时${mins}分钟` : `${hours}小时`;
}

function getDifficultyLabel(difficulty: string) {
  return {
    简单: "入门",
    中等: "进阶",
    困难: "硬核",
  }[difficulty] || difficulty;
}

function getDifficultyColor(difficulty: string) {
  return {
    简单: "background: color-mix(in srgb, #3268b8 18%, transparent);",
    中等: "background: color-mix(in srgb, #cf5c36 18%, transparent);",
    困难: "background: color-mix(in srgb, #19212e 18%, transparent);",
  }[difficulty] || "";
}

function renderStars(rating: number | null) {
  if (!rating) return "☆☆☆☆☆";
  const full = Math.round(rating);
  return "★".repeat(full) + "☆".repeat(5 - full);
}

onMounted(() => {
  loadScripts();
});
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div>
        <span class="brand-code">LDMURDERGAME</span>
        <h1 class="brand-title">剧本库</h1>
      </div>
    </header>

    <section class="workspace">
      <div class="hero-panel">
        <span class="pill">共 {{ scripts.length }} 个剧本</span>
        <h2>精选剧本，任你挑选</h2>
        <p>查看玩家真实评价，帮助你找到最适合的剧本。每一个评价都来自真实打本体验。</p>
      </div>

      <div v-loading="loading" class="script-layout">
        <aside class="script-sidebar">
          <div
            v-for="script in scripts"
            :key="script.id"
            class="script-card"
            :class="{ active: activeScriptId === script.id }"
            @click="selectScript(script)"
          >
            <div class="script-card-header">
              <span class="script-type">{{ script.type }}</span>
              <span class="script-difficulty" :style="getDifficultyColor(script.difficulty)">
                {{ getDifficultyLabel(script.difficulty) }}
              </span>
            </div>
            <h3 class="script-name">{{ script.name }}</h3>
            <div class="script-meta">
              <span>{{ formatDuration(script.duration) }}</span>
              <span>{{ script.playerCount }}人</span>
            </div>
            <div class="script-rating">
              <span class="rating-stars" v-if="script.avgScriptRating">
                {{ renderStars(script.avgScriptRating) }}
              </span>
              <span class="rating-score" v-if="script.avgScriptRating">
                {{ script.avgScriptRating.toFixed(1) }}
              </span>
              <span class="rating-empty" v-else>暂无评分</span>
              <span class="review-count">{{ script.reviewCount }} 条评价</span>
            </div>
          </div>
        </aside>

        <section class="script-detail" v-if="activeScript">
          <div class="detail-header">
            <div>
              <div class="detail-tags">
                <span class="pill">{{ activeScript.type }}</span>
                <span class="pill">{{ getDifficultyLabel(activeScript.difficulty) }}</span>
                <span class="pill">{{ formatDuration(activeScript.duration) }}</span>
                <span class="pill">{{ activeScript.playerCount }}人本</span>
              </div>
              <h2 class="detail-title">{{ activeScript.name }}</h2>
              <p class="detail-desc">{{ activeScript.description }}</p>
            </div>
            <div class="detail-rating-panel">
              <div class="rating-display">
                <span class="rating-number">{{ avgRating ? avgRating.toFixed(1) : '--' }}</span>
                <div class="rating-stars-lg">
                  {{ renderStars(avgRating) }}
                </div>
                <span class="rating-total">{{ reviewCount }} 人评价</span>
              </div>
            </div>
          </div>

          <div class="work-panel">
            <ReviewList
              v-loading="loadingReviews"
              :reviews="reviews"
              title="玩家评价"
              :show-dm-name="true"
            />
          </div>
        </section>
      </div>
    </section>
  </main>
</template>

<style scoped>
.script-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  margin-top: 26px;
}

.script-sidebar {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.script-card {
  padding: 18px;
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  border: 1px solid color-mix(in srgb, #19212e 10%, transparent);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.script-card:hover {
  border-color: color-mix(in srgb, #3268b8 30%, transparent);
  transform: translateY(-2px);
  box-shadow: 0 12px 32px color-mix(in srgb, #19212e 10%, transparent);
}

.script-card.active {
  border-color: #3268b8;
  background: color-mix(in srgb, #3268b8 8%, #f4f7fb);
  box-shadow: 0 8px 24px color-mix(in srgb, #3268b8 15%, transparent);
}

.script-card-header {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.script-type {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  background: color-mix(in srgb, #3268b8 14%, transparent);
  color: #19212e;
}

.script-difficulty {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  color: #19212e;
}

.script-name {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 700;
  line-height: 1.3;
}

.script-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
  font-size: 13px;
  color: color-mix(in srgb, #19212e 60%, transparent);
}

.script-rating {
  display: flex;
  align-items: center;
  gap: 8px;
  padding-top: 10px;
  border-top: 1px dashed color-mix(in srgb, #19212e 10%, transparent);
}

.rating-stars {
  color: #cf5c36;
  font-size: 14px;
  letter-spacing: 1px;
}

.rating-score {
  font-weight: 700;
  font-size: 14px;
  color: #cf5c36;
}

.rating-empty {
  font-size: 13px;
  color: color-mix(in srgb, #19212e 40%, transparent);
}

.review-count {
  margin-left: auto;
  font-size: 12px;
  color: color-mix(in srgb, #19212e 50%, transparent);
}

.script-detail {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.detail-header {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 24px;
  padding: clamp(22px, 4vw, 42px);
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  border: 1px solid color-mix(in srgb, #19212e 13%, transparent);
  border-radius: 8px;
  box-shadow: 0 18px 50px color-mix(in srgb, #19212e 10%, transparent);
}

.detail-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.detail-title {
  margin: 0 0 12px;
  font-size: clamp(28px, 4vw, 36px);
  font-weight: 800;
  line-height: 1.15;
}

.detail-desc {
  margin: 0;
  font-size: clamp(15px, 1.8vw, 17px);
  line-height: 1.8;
  color: color-mix(in srgb, #19212e 72%, #3268b8 28%);
}

.detail-rating-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 180px;
  padding: 20px;
  background: color-mix(in srgb, #3268b8 8%, transparent);
  border-radius: 12px;
}

.rating-display {
  text-align: center;
}

.rating-number {
  display: block;
  font-size: 48px;
  font-weight: 800;
  color: #cf5c36;
  line-height: 1;
}

.rating-stars-lg {
  display: block;
  font-size: 20px;
  color: #cf5c36;
  letter-spacing: 3px;
  margin: 8px 0;
}

.rating-total {
  font-size: 13px;
  color: color-mix(in srgb, #19212e 60%, transparent);
}

@media (max-width: 960px) {
  .script-layout {
    grid-template-columns: 1fr;
  }

  .script-sidebar {
    flex-direction: row;
    overflow-x: auto;
    padding-bottom: 8px;
  }

  .script-card {
    min-width: 260px;
    flex-shrink: 0;
  }

  .detail-header {
    grid-template-columns: 1fr;
  }

  .detail-rating-panel {
    order: -1;
  }
}
</style>
