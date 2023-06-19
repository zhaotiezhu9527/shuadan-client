<template>
  <view class="main">
    <view class="title">
      <view class="sub">全民任务</view>
      <view class="name">用户登录</view>
    </view>
    <view class="form">
      <view class="input">
        <u-input
          border="bottom"
          placeholder="请输入账户"
          placeholderClass="placeholder"
          v-model="userName"
        >
          <view class="icon" slot="prefix">
            <image mode="widthFix" src="@/static/img/icon01.png" />
          </view>
        </u-input>
      </view>
      <view class="input">
        <u-input
          border="bottom"
          placeholder="请输入密码"
          placeholderClass="placeholder"
          password
          v-if="passicon1"
          v-model="loginPwd"
        >
          <view class="icon" slot="prefix">
            <image mode="widthFix" src="@/static/img/icon02.png" />
          </view>
          <view slot="suffix" @click="pwdChange">
            <u-icon name="eye" color="#666" size="46rpx"></u-icon>
          </view>
        </u-input>
        <u-input
          border="bottom"
          placeholder="请输入密码"
          placeholderClass="placeholder"
          v-model="loginPwd"
          v-else
        >
          <view class="icon" slot="prefix">
            <image mode="widthFix" src="@/static/img/icon02.png" />
          </view>
          <view slot="suffix" @click="pwdChange">
            <u-icon name="eye-off" color="#666" size="46rpx"></u-icon>
          </view>
        </u-input>
      </view>
      <u-button
        class="button"
        @click="login"
        text="登录"
        color="#2f3848"
        :loading="loading"
      ></u-button>
      <view class="other">
        <view @click="nopass('/pages/onlineService')">忘记密码</view>
        <view @click="nopass('/pages/register')">免费注册</view>
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
.main {
  position: fixed;
  height: 100%;
  width: 100%;
  background-repeat: no-repeat;
  background: url("@/static/img/login-bg1.png");
  background-size: 100% auto;
  padding-top: var(--status-bar-height);
  .title {
    padding: 154rpx 80rpx 140rpx;
    .sub {
      font-size: 34rpx;
    }
    .name {
      font-size: 50rpx;
    }
  }
  .form {
    padding: 0 80rpx;
    .input {
      padding-bottom: 60rpx;
      .icon {
        padding-right: 20rpx;
        border-right: 2rpx solid #ccc;
      }
      image {
        width: 42rpx;
      }
    }
    .placeholder {
      color: #666 !important;
      font-size: 30rpx;
    }
    .other {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
    }
  }
  /deep/.button .u-button__text {
    color: #f2d8be;
  }
}
</style>
