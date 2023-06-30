import { RouterMount, createRouter } from "uni-simple-router";
import path from "../plugins/path.js";

const router = createRouter({
  platform: process.env.VUE_APP_PLATFORM,
  routes: [...path],
});
const WHILE_LIST = ["/pages/register", "/pages/login", "/pages/onlineService"];
//全局路由前置守卫
router.beforeEach((to, from, next) => {
  let token = uni.getStorageSync("token");
  if (!WHILE_LIST.includes(to.path) && !token) {
    next({
      path: "/pages/login",
      NAVTYPE: "replaceAll",
    });
  } else {
    next();
  }
});
// 全局路由后置守卫
router.afterEach((to, from) => {});

export { router, RouterMount };
