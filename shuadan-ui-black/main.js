import Vue from "vue";
import App from "./App";
import uView from "@/uni_modules/uview-ui";
import { router, RouterMount } from "./plugins/router";

// 引入 多语言包
import VueI18n from "vue-i18n";
import i18n from "./plugins/lang/index.js";
Vue.use(router);
Vue.use(uView);
// 如此配置即可
uni.$u.config.unit = "rpx";
import * as base from "plugins/base.js";
import * as api from "plugins/api.js";
import * as store from "plugins/store.js";

Vue.prototype.$onLaunched = new Promise((resolve) => {
  Vue.prototype.$isResolve = resolve;
});

Vue.prototype.$api = api;
Vue.prototype.$base = base;
Vue.prototype.$store = store;

Vue.config.productionTip = false;

Vue.prototype._i18n = i18n;
App.mpType = "app";

const app = new Vue({
  ...App,
});

// 设置全局标题
Vue.mixin({
  onShow() {
    uni.setNavigationBarTitle({
      title: "全民任务",
    });
    // #ifdef H5
    document.title = "全民任务";
    // #endif
  },
});

// #ifdef H5
RouterMount(app, router, "#app");
// #endif

// #ifdef APP-PLUS
app.$mount();
// #endif
