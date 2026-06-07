<script setup lang="ts">
import { onMounted, ref, watch, computed } from "vue";
import { fetchOverview } from "./api/client";
import { APP_CODE, APP_NAME } from "./constants/app";
import { REQUEST_MESSAGES } from "./constants/messages";
import { createFallbackOverview } from "./state/dashboard";
import type { OverviewResponse } from "./types";
import FeatureStrip from "./components/FeatureStrip.vue";
import MetricGrid from "./components/MetricGrid.vue";
import OperationsTable from "./components/OperationsTable.vue";
import ScriptLibrary from "./views/ScriptLibrary.vue";
import GroupSessions from "./views/GroupSessions.vue";
import { routes } from "./routes";

const overview = ref<OverviewResponse>(createFallbackOverview());
const notice = ref(REQUEST_MESSAGES.overviewFallback);
const currentPath = ref(window.location.hash.slice(1) || "/");

const currentRoute = computed(() => {
  return routes.find((r) => r.path === currentPath.value) || routes[0];
});

const isHomePage = computed(() => currentPath.value === "/");

function navigate(path: string) {
  window.location.hash = path;
}

function goHealth() {
  window.location.href = REQUEST_MESSAGES.healthPath;
}

function onHashChange() {
  currentPath.value = window.location.hash.slice(1) || "/";
}

watch(currentPath, async () => {
  if (currentPath.value === "/" && overview.value.kpis.length === 0) {
    try {
      overview.value = await fetchOverview();
      notice.value = "后端服务已联通，当前展示实时接口数据。";
    } catch {
      notice.value = REQUEST_MESSAGES.overviewFallback;
    }
  }
});

onMounted(async () => {
  window.addEventListener("hashchange", onHashChange);
  if (currentPath.value === "/") {
    try {
      overview.value = await fetchOverview();
      notice.value = "后端服务已联通，当前展示实时接口数据。";
    } catch {
      notice.value = REQUEST_MESSAGES.overviewFallback;
    }
  }
});
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div>
        <span class="brand-code">{{ APP_CODE }}</span>
        <h1 class="brand-title">{{ APP_NAME }}</h1>
      </div>
      <nav class="nav-tabs">
        <button
          v-for="route in routes"
          :key="route.path"
          class="nav-tab"
          :class="{ active: currentPath === route.path }"
          @click="navigate(route.path)"
        >
          {{ route.label }}
        </button>
      </nav>
      <el-button type="primary" @click="goHealth">API Health</el-button>
    </header>

    <component :is="ScriptLibrary" v-if="currentPath === '/scripts'" />
    <component :is="GroupSessions" v-else-if="currentPath === '/sessions'" />
    <section v-else class="workspace">
      <div class="lead-grid">
        <article class="hero-panel">
          <span class="pill">{{ notice }}</span>
          <h2>{{ overview.appName }}</h2>
          <p>{{ overview.description }}</p>
        </article>
        <MetricGrid :items="overview.kpis" />
      </div>
      <FeatureStrip :items="overview.features" />
      <section class="work-panel">
        <h2>运营任务流</h2>
        <OperationsTable :records="overview.records" />
      </section>
    </section>
  </main>
</template>

<style scoped>
.nav-tabs {
  display: flex;
  gap: 4px;
  background: color-mix(in srgb, #19212e 6%, transparent);
  padding: 4px;
  border-radius: 8px;
}

.nav-tab {
  padding: 8px 16px;
  border: none;
  background: transparent;
  color: color-mix(in srgb, #19212e 70%, transparent);
  font-size: 14px;
  font-weight: 600;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
}

.nav-tab:hover {
  background: color-mix(in srgb, #19212e 6%, transparent);
}

.nav-tab.active {
  background: white;
  color: #3268b8;
  box-shadow: 0 2px 8px color-mix(in srgb, #19212e 10%, transparent);
}

@media (max-width: 860px) {
  .topbar {
    gap: 16px;
  }

  .nav-tabs {
    order: 3;
    width: 100%;
    justify-content: stretch;
  }

  .nav-tab {
    flex: 1;
  }
}
</style>
