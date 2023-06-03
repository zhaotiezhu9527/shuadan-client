import { RouterMount, createRouter } from "uni-simple-router";
import path from "../plugins/path.js";

const router = createRouter({
  platform: process.env.VUE_APP_PLATFORM,
  routes: [...path],
});
// const WHILE_LIST = ["/pages/register", "/pages/login"];
//全局路由前置守卫
router.beforeEach((to, from, next) => {
  // let token = uni.getStorageSync("token");
  next();
});
// 全局路由后置守卫
router.afterEach((to, from) => {});

export { router, RouterMount };
