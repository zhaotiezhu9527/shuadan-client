import Vue from "vue";
import * as base from "plugins/base.js";
Vue.prototype.$base = base;
//统一API资源管理

//登录
export const user_login = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/login",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//注册
export const user_register = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/register",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//用户数据详情
export const user_info = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/info",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//获取头像
export const avatar_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "avatar/list",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//设置头像
export const avatar_set = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "avatar/set/" + data.id,
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//修改名称
export const update_nickName = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/update/nickName",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//绑定银行卡
export const bindBank = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "/user/bindBank",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};