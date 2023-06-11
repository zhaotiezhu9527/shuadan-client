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

//退出登陆
export const user_logout = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/logout",
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
        url: "user/bindBank",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//修改密码
export const updatePwd = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/updatePwd",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//修改支付密码
export const updatePayPwd = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/updatePayPwd",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//用户提现
export const user_withdraw = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/withdraw",
        method: "post",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//用户提现记录列表
export const user_withdraw_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/withdraw/list",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//用户充值记录列表
export const user_deposit_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/deposit/list",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//信息公告列表
export const notice_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "notice/list",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//信息公告列表
export const system_config = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "system/config",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

//VIP等级详情
export const user_vipiv_info = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/viplv/info",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 文本消息相关
export const message = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "message/" + data,
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 用户收益详情
export const user_income_detail = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/income/detail",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 团队报表
export const user_teamReport = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/teamReport",
        method: "get",
        data: data,
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 根据层级获取用户团队成员
export const user_team = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "user/team/" + data,
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 获取客服列表
export const system_customerService_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "system/customerService/list",
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};

// 获取专区
export const area_list = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "area/list",
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};
// 获取专区详情
export const area_detail = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "area/detail/" + data,
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};
// 匹配
export const order_match = (data) => {
  return new Promise((resolve, reject) => {
    base
      .request({
        url: "order/match/" + data,
        method: "get",
      })
      .then((res) => {
        resolve(res);
      });
  });
};
