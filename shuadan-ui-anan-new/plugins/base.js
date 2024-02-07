import Vue from "vue";
import * as store from "plugins/store.js";
Vue.prototype.$store = store;
import i18n from "./lang/index";
Vue.prototype._i18n = i18n;
let langType = 'en_us'
let that = i18n.vm.messages[uni.getStorageSync("lang") || langType];

//设置缓存内容
export const storage = (name, value) => {
  uni.setStorage({
    key: name,
    data: value,
  });
};
//通用提示
export const show = (title, icon = "none") => {
  uni.showToast({
    title: title,
    duration: 2000,
    icon: icon,
  });
};
//loading
export const loading = (title) => {
  uni.showLoading({
    title: title,
  });
};
const PATH_URL =
  process.env.NODE_ENV === "development"
    // ? "http://8.222.224.10:9544/" //本地环境
    ? "http://sdqt.juhai.xyz/sd-qtapi-test/" //测试环境
    : "http://sdqt.juhai.xyz/sd-qtapi-test/"; //app打包地址
    // : "/sdan1/"; //h5打包地址

export const upload = (params) => {
  let header = {
    token: uni.getStorageSync("token"),
  };
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: PATH_URL + "api_common/upload/uploading",
      filePath: params.filePath,
      name: params.name,
      header: header,
      formData: params.formData,
      success: (uploadFileRes) => {
        resolve(uploadFileRes);
      },
    });
  });
};
//请求
export const request = (params) => {
  let str = params.method.toUpperCase();
  if (str == "POST") {
    params.header = {
      lang: langType,
      "Content-Type":
        params["Content-Type"] === undefined
          ? "application/x-www-form-urlencoded;charset=UTF-8"
          : params["Content-Type"],
      token: uni.getStorageSync("token"),
    };
  } else {
    params.header = {
      lang: langType,
      token: uni.getStorageSync("token"),
    };
  }
  if (params.loading) {
  }
  return new Promise((resolve, reject) => {
    uni.request({
      url: PATH_URL + params.url,
      data: params.data,
      method: str,
      header: params.header,
      sslVerify: false,
      success: (res) => {
        if (res.data.code == -2) {
          uni.redirectTo({
            url: "/pages/login",
          });
          uni.removeStorageSync("token");
          show(res.data.msg);
        } else if (res.data.code != 0) {
          uni.showToast({
            title: res?.data?.msg || that.notNetwork,
            duration: 2000,
            icon: "none",
          });
        }
        resolve(res);
      },
      fail: (e) => {
        show(that.notNetwork);
      },
      complete: () => {
        uni.hideLoading();
      },
    });
  });
};
