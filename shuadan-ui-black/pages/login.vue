<template>
  <view class="main">
    <image
      src="/static/img/logo.png"
      class="logo flex items-center"
      mode="widthFix"
    />
    <view class="form">
      <view class="input">
        <u-input
          border="none"
          placeholder="账户"
          placeholderClass="placeholder"
          v-model="userName"
        >
        </u-input>
      </view>
      <view class="input">
        <u-input
          border="none"
          placeholder="密码"
          placeholderClass="placeholder"
          password
          v-model="loginPwd"
        >
        </u-input>
      </view>
    </view>
    <u-button
      class="button-50"
      @click="login"
      :loading="loading"
      shape="circle"
    >
      <u-icon class="rightward" name="arrow-rightward"></u-icon>
    </u-button>
    <image class="back" src="/static/img/bg-001.png" mode="widthFix" />
    <view class="flex items-center ul justify-center">
      <view class="li" @click="nopass('/pages/onlineService')">忘记密码</view>
      <view class="li" @click="$refs.registerRef.open()">免费注册</view>
      <view class="li" @click="nopass('/pages/onlineService')">在线客服</view>
    </view>
    <register ref="registerRef" />
  </view>
</template>

<script>
import register from "@/components/register.vue";
export default {
  components: {
    register,
  },
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
};
</script>

<style scoped lang="scss">
@import "@/static/login.scss";
</style>
