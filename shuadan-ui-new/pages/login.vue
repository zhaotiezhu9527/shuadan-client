<template>
  <view class="main">
    <image class="login" src="@/static/img/login.png" />
    <view class="title">
      <view class="sub">全民任务</view>
      <view class="name">账号密码登录</view>
    </view>
    <view class="form">
      <view class="tabs">
        <view class="active">密码登录</view>
        <view @click="nopass('/pages/register')">账户注册</view>
      </view>
      <view class="input">
        <view class="label">账号</view>
        <u-input
          border="bottom"
          placeholder="请输入账户"
          placeholderClass="placeholder"
          v-model="userName"
        >
        </u-input>
      </view>
      <view class="input">
        <view class="label">密码</view>
        <u-input
          border="bottom"
          placeholder="请输入密码"
          placeholderClass="placeholder"
          password
          v-show="passicon1"
          v-model="loginPwd"
        >
          <view slot="suffix" @click="pwdChange">
            <u-icon name="eye" color="#666" size="46rpx"></u-icon>
          </view>
        </u-input>
        <u-input
          border="bottom"
          placeholder="请输入密码"
          placeholderClass="placeholder"
          v-model="loginPwd"
          v-show="!passicon1"
        >
          <view slot="suffix" @click="pwdChange">
            <u-icon name="eye-off" color="#666" size="46rpx"></u-icon>
          </view>
        </u-input>
      </view>
      <view class="btn">
        <u-button
          class="button"
          @click="login"
          text="立即登录"
          color="linear-gradient(180deg, #FFAD49 0%, #FFC861 99%)"
          :loading="loading"
          shape="circle"
        ></u-button>
      </view>
    </view>
    <service />
  </view>
</template>

<script>
import service from "@/components/service";
export default {
  data() {
    return {
      passicon1: true,
      loginPwd: "",
      userName: "",
      loading: false,
      config: {},
    };
  },
  methods: {
    pwdChange() {
      this.passicon1 = !this.passicon1;
    },
    login() {
      if (!this.userName) {
        return this.$base.show("请输入登录账号~");
      } else if (!this.loginPwd) {
        return this.$base.show("请输入登录密码~");
      }
      this.loading = true;
      this.$api
        .user_login({
          userName: this.userName,
          loginPwd: this.loginPwd,
        })
        .then((res) => {
          if (res.data.code == 0) {
            uni.setStorage({
              key: "token",
              data: res.data.token,
              success: function () {
                uni.reLaunch({ url: "/pages/index?tabs=4" });
              },
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    nopass(url) {
      uni.navigateTo({
        url,
      });
    },
  },
  components: {
    service,
  },
};
</script>

<style scoped lang="scss">
@import "@/static/login.scss";
</style>
