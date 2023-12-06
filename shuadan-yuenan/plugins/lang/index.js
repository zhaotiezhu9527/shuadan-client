import Vue from "vue";
// 引入 多语言包
import VueI18n from "vue-i18n";
// import zh from "./zh.js";
// import Thai from "./Thai.js";
import vi from "./vi.js";

Vue.use(VueI18n);

// 创建实例这里设置语言
const i18n = new VueI18n({
  locale: uni.getStorageSync("lang") ? uni.getStorageSync("lang") : "vi",
  messages: {
    // zh,
    // Thai,
    vi,
  },
});

export default i18n;

// 使用案例
// <button class="login-btn">{{ $t("login") }}</button>
// console.log(this.$t("login"));

// 切换
// this.$i18n.locale = e;
// uni.setStorageSync("lang", e);
