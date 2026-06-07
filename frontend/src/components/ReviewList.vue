<script setup lang="ts">
import type { Review } from "../types";

defineProps<{
  reviews: Review[];
  title?: string;
  showScriptName?: boolean;
  showDmName?: boolean;
}>();

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function renderStars(rating: number) {
  return "★".repeat(rating) + "☆".repeat(5 - rating);
}

function getRatingColor(rating: number) {
  if (rating >= 5) return "color: #cf5c36;";
  if (rating >= 4) return "color: #3268b8;";
  if (rating >= 3) return "color: #19212e;";
  return "color: color-mix(in srgb, #19212e 50%, transparent);";
}
</script>

<template>
  <div class="review-list">
    <h3 v-if="title" class="review-list-title">{{ title }}</h3>

    <div v-if="reviews.length === 0" class="empty-state">
      <p>暂无评价</p>
      <small>成为第一个评价的人吧～</small>
    </div>

    <div v-for="review in reviews" :key="review.id" class="review-item">
      <div class="review-header">
        <div class="reviewer-info">
          <span class="reviewer-name">{{ review.playerName }}</span>
          <span class="review-date">{{ formatDate(review.createdAt) }}</span>
        </div>
        <div class="review-tags">
          <span v-if="showScriptName" class="pill">{{ review.scriptName }}</span>
          <span v-if="showDmName" class="pill">DM：{{ review.dmName }}</span>
        </div>
      </div>

      <div class="review-ratings">
        <div class="rating-row">
          <span class="rating-label">剧本</span>
          <span class="rating-stars" :style="getRatingColor(review.scriptRating)">
            {{ renderStars(review.scriptRating) }}
          </span>
          <span class="rating-score">{{ review.scriptRating }}.0</span>
        </div>
        <p v-if="review.scriptComment" class="review-comment">
          {{ review.scriptComment }}
        </p>

        <div class="rating-row">
          <span class="rating-label">DM</span>
          <span class="rating-stars" :style="getRatingColor(review.dmRating)">
            {{ renderStars(review.dmRating) }}
          </span>
          <span class="rating-score">{{ review.dmRating }}.0</span>
        </div>
        <p v-if="review.dmComment" class="review-comment">
          {{ review.dmComment }}
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.review-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-list-title {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 700;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: color-mix(in srgb, #19212e 50%, transparent);
}

.empty-state p {
  margin: 0 0 4px;
  font-size: 16px;
  font-weight: 600;
}

.empty-state small {
  font-size: 14px;
}

.review-item {
  padding: 20px;
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  border: 1px solid color-mix(in srgb, #19212e 10%, transparent);
  border-radius: 8px;
  transition: all 0.2s ease;
}

.review-item:hover {
  border-color: color-mix(in srgb, #3268b8 30%, transparent);
  box-shadow: 0 8px 24px color-mix(in srgb, #19212e 8%, transparent);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px dashed color-mix(in srgb, #19212e 10%, transparent);
}

.reviewer-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.reviewer-name {
  font-weight: 700;
  font-size: 15px;
}

.review-date {
  font-size: 12px;
  color: color-mix(in srgb, #19212e 50%, transparent);
}

.review-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.review-ratings {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.rating-label {
  width: 40px;
  font-size: 14px;
  color: color-mix(in srgb, #19212e 70%, transparent);
  font-weight: 600;
}

.rating-stars {
  font-size: 16px;
  letter-spacing: 2px;
}

.rating-score {
  font-size: 14px;
  font-weight: 700;
  color: color-mix(in srgb, #19212e 70%, transparent);
}

.review-comment {
  margin: 4px 0 0 52px;
  padding: 10px 12px;
  background: color-mix(in srgb, #3268b8 5%, transparent);
  border-radius: 6px;
  font-size: 14px;
  line-height: 1.6;
  color: color-mix(in srgb, #19212e 80%, transparent);
}
</style>
