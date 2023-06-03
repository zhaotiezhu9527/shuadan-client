import Vue from "vue";
import * as base from "plugins/base.js";
Vue.prototype.$base = base;
//统一API资源管理

//产品详情
export const project_info = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "project/detail/" + data,
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//购买产品
export const order_execute = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "order/execute",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};
