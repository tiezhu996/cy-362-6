<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import type { GroupSession } from "../types";
import { fetchGroupSessions } from "../api/client";
import ReviewDialog from "../components/ReviewDialog.vue";
import ReviewList from "../components/ReviewList.vue";
import { fetchReviewsByGroupSession } from "../api/client";
import type { Review } from "../types";

const sessions = ref<GroupSession[]>([]);
const loading = ref(false);
const playerName = ref("");
const reviewDialogVisible = ref(false);
const selectedSession = ref<GroupSession | null>(null);
const reviewDetailVisible = ref(false);
const detailSession = ref<GroupSession | null>(null);
const sessionReviews = ref<Review[]>([]);
const loadingReviews = ref(false);

async function loadSessions() {
  loading.value = true;
  try {
    sessions.value = await fetchGroupSessions(playerName.value);
  } catch (e) {
    ElMessage.error("加载组局记录失败");
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  loadSessions();
}

function openReviewDialog(session: GroupSession) {
  if (session.status !== "completed") {
    ElMessage.warning("该局尚未结束，暂不能评价");
    return;
  }
  if (session.hasReviewed) {
    ElMessage.info("您已经对该局进行过评价了");
    return;
  }
  selectedSession.value = session;
  reviewDialogVisible.value = true;
}

function onReviewSubmitted() {
  loadSessions();
}

async function openReviewDetail(session: GroupSession) {
  detailSession.value = session;
  reviewDetailVisible.value = true;
  loadingReviews.value = true;
  try {
    sessionReviews.value = await fetchReviewsByGroupSession(session.id);
  } catch (e) {
    ElMessage.error("加载评价详情失败");
  } finally {
    loadingReviews.value = false;
  }
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function getStatusLabel(status: string) {
  return {
    scheduled: "待开场",
    in_progress: "进行中",
    completed: "已结束",
  }[status] || status;
}

function getStatusType(status: string) {
  return {
    scheduled: "info",
    in_progress: "warning",
    completed: "success",
  }[status] || "";
}

function getPlayersText(names: string[]) {
  if (names.length <= 4) return names.join("、");
  return names.slice(0, 4).join("、") + ` 等${names.length}人`;
}

onMounted(() => {
  loadSessions();
});
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div>
        <span class="brand-code">LDMURDERGAME</span>
        <h1 class="brand-title">我的组局</h1>
      </div>
    </header>

    <section class="workspace">
      <div class="hero-panel">
        <span class="pill">共 {{ sessions.length }} 条记录</span>
        <h2>打完本，留个评价吧</h2>
        <p>您的每一条评价都能帮助其他玩家选择合适的剧本和DM，让剧本杀社区更美好。</p>
      </div>

      <section class="work-panel">
        <div class="search-bar">
          <el-input
            v-model="playerName"
            placeholder="输入玩家名称筛选我的场次"
            style="width: 300px"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <span>🔍</span>
            </template>
          </el-input>
          <el-button type="primary" @click="handleSearch">筛选</el-button>
          <span class="search-hint">
            试试输入「小明」「阿杰」「大伟」等示例名称查看评价状态
          </span>
        </div>

        <div v-loading="loading" class="session-list">
          <div v-if="sessions.length === 0" class="empty-state">
            <p>暂无组局记录</p>
          </div>

          <div
            v-for="session in sessions"
            :key="session.id"
            class="session-card"
          >
            <div class="session-main">
              <div class="session-header">
                <span class="pill">{{ session.scriptName }}</span>
                <el-tag :type="getStatusType(session.status)" effect="light">
                  {{ getStatusLabel(session.status) }}
                </el-tag>
              </div>
              <div class="session-info">
                <div class="info-row">
                  <span class="info-label">DM</span>
                  <span class="info-value">{{ session.dmName }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">时间</span>
                  <span class="info-value">{{ formatDate(session.sessionTime) }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">玩家</span>
                  <span class="info-value">{{ getPlayersText(session.playerNames) }}</span>
                </div>
              </div>
            </div>

            <div class="session-actions">
              <el-button
                v-if="session.status === 'completed' && !session.hasReviewed"
                type="primary"
                @click="openReviewDialog(session)"
              >
                去评价
              </el-button>
              <el-button
                v-else-if="session.status === 'completed' && session.hasReviewed"
                type="success"
                plain
                @click="openReviewDetail(session)"
              >
                已评价 ✓
              </el-button>
              <el-button
                v-if="session.status === 'completed'"
                @click="openReviewDetail(session)"
              >
                查看评价
              </el-button>
              <el-button
                v-else
                disabled
              >
                {{ session.status === 'scheduled' ? '待开场' : '进行中' }}
              </el-button>
            </div>
          </div>
        </div>
      </section>
    </section>

    <ReviewDialog
      v-model="reviewDialogVisible"
      :session="selectedSession"
      @submitted="onReviewSubmitted"
    />

    <el-dialog
      v-model="reviewDetailVisible"
      title="评价详情"
      width="700px"
      destroy-on-close
    >
      <div v-if="detailSession" class="detail-header">
        <div class="detail-info">
          <span class="pill">{{ detailSession.scriptName }}</span>
          <span>DM：{{ detailSession.dmName }}</span>
          <span>{{ formatDate(detailSession.sessionTime) }}</span>
        </div>
      </div>
      <div v-loading="loadingReviews">
        <ReviewList
          :reviews="sessionReviews"
          :show-script-name="false"
          :show-dm-name="false"
        />
      </div>
    </el-dialog>
  </main>
</template>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid color-mix(in srgb, #19212e 10%, transparent);
}

.search-hint {
  font-size: 13px;
  color: color-mix(in srgb, #19212e 50%, transparent);
}

.session-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: color-mix(in srgb, #19212e 50%, transparent);
}

.session-card {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 20px;
  align-items: center;
  padding: 20px;
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  border: 1px solid color-mix(in srgb, #19212e 10%, transparent);
  border-radius: 8px;
  transition: all 0.2s ease;
}

.session-card:hover {
  border-color: color-mix(in srgb, #3268b8 25%, transparent);
  box-shadow: 0 8px 24px color-mix(in srgb, #19212e 8%, transparent);
}

.session-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.session-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 14px;
}

.info-label {
  width: 40px;
  color: color-mix(in srgb, #19212e 50%, transparent);
  flex-shrink: 0;
}

.info-value {
  color: #19212e;
  line-height: 1.5;
}

.session-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex-shrink: 0;
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid color-mix(in srgb, #19212e 10%, transparent);
}

.detail-info {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
  color: color-mix(in srgb, #19212e 72%, #3268b8 28%);
  font-size: 14px;
}

@media (max-width: 760px) {
  .session-card {
    grid-template-columns: 1fr;
  }

  .session-actions {
    flex-direction: row;
  }

  .search-bar {
    flex-wrap: wrap;
  }

  .search-bar .el-input {
    flex: 1;
    min-width: 200px;
  }
}
</style>
