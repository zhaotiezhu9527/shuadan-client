<template>
  <view class="main">
    <u-navbar
      placeholder
      title="Đăng nhập"
      fixed
      safe-area-inset-top
      bgColor="#fff"
      leftIconColor="#666"
      height="50px"
      leftIconSize="0"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="logo">
      <image class="img" mode="widthFix" src="@/static/img/picodi.png"></image>
    </view>
    <view class="input">
      <u-input
        :placeholder="$t('enterAccount')"
        border="bottom"
        v-model="userName"
      >
        <view class="icon" slot="prefix">
          <image mode="widthFix" src="@/static/img/10003.png" />
        </view>
      </u-input>
    </view>
    <view class="input">
      <u-input
        placeholder="Mật khẩu"
        password
        border="bottom"
        v-model="loginPwd"
        v-show="passicon1"
      >
        <view class="icon" slot="prefix">
          <image mode="widthFix" src="@/static/img/10004.png" />
        </view>
        <view class="pas" slot="suffix">
          <u-icon
            name="eye"
            color="#467AF6"
            size="46rpx"
            @click="pwdChange"
          ></u-icon>
          <view class="link-con" @click="routePage('/pages/onlineService')">
            <view class="link">Quên tài khoản</view>
            <view class="link">và mật khẩu?</view>
          </view>
        </view>
      </u-input>
      <u-input
        placeholder="Mật khẩu"
        border="bottom"
        v-model="loginPwd"
        v-show="!passicon1"
      >
        <view class="icon" slot="prefix">
          <image mode="widthFix" src="@/static/img/10004.png" />
        </view>
        <view class="pas" slot="suffix">
          <u-icon
            name="eye-off"
            color="#467AF6"
            size="46rpx"
            @click="pwdChange"
          ></u-icon>
          <view class="link-con" @click="routePage('/pages/onlineService')">
            <view class="link">Quên tài khoản</view>
            <view class="link">và mật khẩu?</view>
          </view>
        </view>
      </u-input>
    </view>
    <view class="btn">
      <u-button
        v-show="loginPwd && userName"
        class="button"
        @click="login"
        :text="$t('login')"
        color="#ee4d2d"
        :loading="loading"
      ></u-button>
      <u-button
        v-show="!loginPwd || !userName"
        class="button"
        :text="$t('login')"
        color="#E8E8E8"
        :loading="loading"
      ></u-button>
    </view>
    <view class="other">
      <view @click="routePage('/pages/register')">Đăng ký</view>
      <!-- <view @click="routePage('/pages/onlineService')">Đăng nhập bằng SMS</view> -->
    </view>
  </view>
</template>

<script>
import service from "@/components/service";
import lang from "@/components/lang";
export default {
  data() {
    return {
      checked: [],
      passicon1: true,
      loginPwd: "",
      userName: "",
      loading: false,
      config: {},
    };
  },
  onShow() {},
  methods: {
    routePage(url) {
      uni.navigateTo({
        url,
      });
    },
    pwdChange() {
      this.passicon1 = !this.passicon1;
    },
    login() {
      if (!this.userName) {
        return this.$base.show(this.$t("accountEmpty"));
      } else if (!this.loginPwd) {
        return this.$base.show(this.$t("passwordEmpty"));
      }
      this.loading = true;
      this.$api
        .user_login({
          userName: this.userName,
          loginPwd: this.loginPwd,
        })
        .then((res) => {
          if (res.data.code == 0) {
            // 是否存储记住密码
            if (this.checked.length) {
              uni.setStorageSync("userName", this.userName);
              uni.setStorageSync("loginPwd", this.loginPwd);
            } else {
              uni.removeStorageSync("userName");
              uni.removeStorageSync("loginPwd");
            }
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
    lang,
  },
};
</script>

<style scoped lang="scss">
.main {
  padding: 0 30rpx;
  .logo {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 100rpx 0;
    .img {
      width: 120rpx;
    }
  }
}
.input {
  padding-bottom: 20rpx;
  .icon {
    filter: grayscale(1);
    padding-right: 20rpx;
  }
  image {
    width: 42rpx;
    max-height: 48rpx;
  }
  .pas {
    display: flex;
    align-items: center;
  }
  .link-con {
    margin-left: 50rpx;
    position: relative;
    color: #467cf6;
    &::before {
      content: "";
      height: 30rpx;
      width: 1rpx;
      background-color: #999;
      position: absolute;
      left: -35rpx;
      top: 50%;
      transform: translateY(-50%);
    }
  }
  .link {
    font-size: 24rpx;
  }
}
.btn {
  padding-top: 30rpx;
}
.other {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 10rpx;
  view {
    font-size: 28rpx;
    color: #467cf6;
  }
}
</style>
