<template>
  <view class="main">
    <view class="register" @click="routePage('/pages/register')">
      <view class="name">{{ $t("register") }}</view>
      <image mode="widthFix" class="image" src="@/static/img/10002.png" />
    </view>
    <!-- <lang /> -->
    <view class="postion">
      <view class="form">
        <view class="title"> {{ $t("login") }} </view>
        <view class="input">
          <u-input
            border="none"
            :placeholder="$t('enterAccount')"
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
            border="none"
            :placeholder="$t('enterPassword')"
            placeholderClass="placeholder"
            password
            v-show="passicon1"
            v-model="loginPwd"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/icon02.png" />
            </view>
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye" color="#4e995c" size="46rpx"></u-icon>
            </view>
          </u-input>
          <u-input
            border="none"
            :placeholder="$t('enterPassword')"
            placeholderClass="placeholder"
            v-model="loginPwd"
            v-show="!passicon1"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/icon02.png" />
            </view>
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye-off" color="#4e995c" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="btn">
          <u-checkbox-group v-model="checked">
            <u-checkbox
              activeColor="#4e995c"
              :label="$t('rememberPassword')"
              name="1"
            ></u-checkbox>
          </u-checkbox-group>
          <u-button
            class="button"
            @click="login"
            :text="$t('login')"
            color="#4e995c"
            :loading="loading"
            shape="circle"
          ></u-button>
          <view class="nopass" @click="routePage('/pages/password')">
            {{ $t("forgetPassword") }}
          </view>
        </view>
      </view>
      <view class="other">
        <view class="link" @click="routePage('/pages/onlineService')">
          {{ $t("contactService") }} 
        </view>
        <view class="link">{{ $t("appDownload") }}</view>
      </view>
    </view>
    <service />
    <view class="display-hidden">
      <web-view v-show="config.onlineService" :webview-styles="webviewStyles"  :src="config.onlineService" class="online"></web-view>
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
      webviewStyles: {
        progress: {
          color: '#00FF00'
        }
      }
    };
  },
  onShow() {
    if (uni.getStorageSync("userName")) {
      this.userName = uni.getStorageSync("userName");
      this.loginPwd = uni.getStorageSync("loginPwd");
      this.checked["1"];
    }
    this.getConfig()
  },
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
          userName: this.userName.replace(/\s*/g,""),
          loginPwd: this.loginPwd.replace(/\s*/g,""),
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
    getConfig(){
      // uni.showLoading();
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
          // uni.hideLoading();
        } 
      });
    }
  },
  components: {
    service,
    lang,
  },
};
</script>

<style scoped lang="scss">
.btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .nopass {
    font-size: 26rpx;
    color: #4e995c;
  }
  /deep/.u-checkbox__icon-wrap {
    width: 30rpx !important;
    height: 30rpx !important;
  }
}
.register {
  position: absolute;
  right: 40rpx;
  top: 100rpx;
  .image {
    width: 50rpx;
  }
  .name {
    padding-bottom: 16rpx;
    color: #fff;
  }
}
.other {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 40rpx;
  .link {
    color: #ffffff;
    font-size: 26rpx;
    padding: 0 30rpx;
  }
}
.main {
  position: fixed;
  height: 100%;
  width: 100%;
  background-repeat: no-repeat;
  background: url("@/static/img/login-bg-green.png");
  background-size: 100% auto;
  padding-top: var(--status-bar-height);
  .postion {
    position: absolute;
    top: 50%;
    left: 0;
    width: 100%;
    transform: translateY(-40%);
    .title {
      color: #4e995c;
      font-size: 55rpx;
      font-weight: 600;
      padding: 0 40rpx 60rpx;
    }
  }
  .form {
    padding: 80rpx 30rpx;
    border-radius: 50rpx;
    background-color: #ffffff;
    /deep/.u-input {
      background-color: #f4f4f4;
      padding: 32rpx 40rpx !important;
      border-radius: 100rpx;
    }
    .input {
      padding-bottom: 60rpx;
      .icon {
        padding-right: 20rpx;
      }
      image {
        width: 42rpx;
        max-height: 48rpx;
      }
    }
    .placeholder {
      color: #4e995c !important;
      font-size: 30rpx;
    }
  }
}
.button {
  width: 300rpx;
  height: 88rpx;
  line-height: 88rpx;
}
</style>
