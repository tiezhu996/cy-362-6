<script setup lang="ts">
import { ref, computed, watch } from "vue";
import { ElMessage } from "element-plus";
import type { GroupSession, ReviewRequest } from "../types";
import { submitReview } from "../api/client";

const props = defineProps<{
  modelValue: boolean;
  session: GroupSession | null;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "submitted"): void;
}>();

const visible = computed({
  get: () => props.modelValue,
  set: (v) => emit("update:modelValue", v),
});

const playerName = ref("");
const scriptRating = ref(0);
const scriptComment = ref("");
const dmRating = ref(0);
const dmComment = ref("");
const submitting = ref(false);

watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      playerName.value = "";
      scriptRating.value = 0;
      scriptComment.value = "";
      dmRating.value = 0;
      dmComment.value = "";
    }
  }
);

function getPlayerOptions() {
  if (!props.session) return [];
  return props.session.playerNames.map((name) => ({
    label: name,
    value: name,
  }));
}

async function handleSubmit() {
  if (!props.session) return;

  if (!playerName.value) {
    ElMessage.warning("请选择您的角色名称");
    return;
  }
  if (scriptRating.value === 0) {
    ElMessage.warning("请为剧本打分");
    return;
  }
  if (dmRating.value === 0) {
    ElMessage.warning("请为DM打分");
    return;
  }

  submitting.value = true;
  try {
    const request: ReviewRequest = {
      groupSessionId: props.session.id,
      scriptId: props.session.scriptId,
      dmId: props.session.dmId,
      playerName: playerName.value,
      scriptRating: scriptRating.value,
      scriptComment: scriptComment.value || undefined,
      dmRating: dmRating.value,
      dmComment: dmComment.value || undefined,
    };
    await submitReview(request);
    ElMessage.success("评价提交成功！");
    visible.value = false;
    emit("submitted");
  } catch (e) {
    ElMessage.error((e as Error).message || "提交失败，请重试");
  } finally {
    submitting.value = false;
  }
}
</script>

<template>
  <el-dialog
    v-model="visible"
    title="填写评价"
    width="600px"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <div v-if="session" class="review-header">
      <div class="review-info">
        <span class="pill">{{ session.scriptName }}</span>
        <span>DM：{{ session.dmName }}</span>
        <span>场次：{{ new Date(session.sessionTime).toLocaleString("zh-CN") }}</span>
      </div>
    </div>

    <el-form label-width="80px" class="review-form">
      <el-form-item label="我是">
        <el-select v-model="playerName" placeholder="请选择您在本局的角色名称">
          <el-option
            v-for="opt in getPlayerOptions()"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>
      </el-form-item>

      <div class="rating-section">
        <h4 class="section-title">
          <span class="section-icon">📖</span>
          剧本评分
        </h4>
        <el-rate
          v-model="scriptRating"
          :max="5"
          size="large"
          show-text
          :texts="['差评', '一般', '还行', '推荐', '神作']"
        />
        <el-input
          v-model="scriptComment"
          type="textarea"
          :rows="3"
          maxlength="500"
          show-word-limit
          placeholder="说说您对剧本的看法...（剧情、逻辑、节奏等）"
          class="comment-input"
        />
      </div>

      <div class="rating-section">
        <h4 class="section-title">
          <span class="section-icon">🎭</span>
          DM 评分
        </h4>
        <el-rate
          v-model="dmRating"
          :max="5"
          size="large"
          show-text
          :texts="['差评', '一般', '还行', '专业', '完美']"
        />
        <el-input
          v-model="dmComment"
          type="textarea"
          :rows="3"
          maxlength="500"
          show-word-limit
          placeholder="说说您对DM的评价...（控场、演绎、节奏把控等）"
          class="comment-input"
        />
      </div>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        提交评价
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.review-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid color-mix(in srgb, #19212e 10%, transparent);
}

.review-info {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
  color: color-mix(in srgb, #19212e 72%, #3268b8 28%);
  font-size: 14px;
}

.review-form {
  margin-top: 10px;
}

.rating-section {
  margin-top: 24px;
  padding: 16px;
  background: color-mix(in srgb, #3268b8 6%, transparent);
  border-radius: 8px;
}

.section-title {
  margin: 0 0 12px;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-icon {
  font-size: 18px;
}

.comment-input {
  margin-top: 12px;
}
</style>
