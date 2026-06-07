import type { Component } from "vue";

export interface RouteConfig {
  path: string;
  label: string;
  component?: Component;
}

export const routes: RouteConfig[] = [
  { path: "/", label: "运营总览" },
  { path: "/scripts", label: "剧本库" },
  { path: "/sessions", label: "我的组局" },
];
