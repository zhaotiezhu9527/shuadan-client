<template>
  <view class="main">
    <view class="register" @click="routePage">
      <view class="name">{{ $t("login") }}</view>
      <image mode="widthFix" class="image" src="@/static/img/10002.png" />
    </view>
    <!-- <lang /> -->
    <view class="postion">
      <view class="form">
        <view class="title"> {{ $t("register") }} </view>
        <view class="input">
          <u-input
            border="none"
            :placeholder="$t('enterNickName')"
            placeholderClass="placeholder"
            v-model="nickName"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10003.png" />
            </view>
          </u-input>
        </view>
        <view class="input">
          <u-input
            border="none"
            :placeholder="$t('enterUserName')"
            placeholderClass="placeholder"
            v-model="userName"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10003.png" />
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
              <image mode="widthFix" src="@/static/img/10004.png" />
            </view>
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye" color="#2E68F2" size="46rpx"></u-icon>
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
              <image mode="widthFix" src="@/static/img/10004.png" />
            </view>
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye-off" color="#2E68F2" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <u-input
            border="none"
            :placeholder="$t('enterPayPassword')"
            placeholderClass="placeholder"
            password
            v-show="passicon2"
            v-model="payPwd"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10005.png" />
            </view>
            <view slot="suffix" @click="payPwdChange">
              <u-icon name="eye" color="#2E68F2" size="46rpx"></u-icon>
            </view>
          </u-input>
          <u-input
            border="none"
            :placeholder="$t('enterPayPassword')"
            placeholderClass="placeholder"
            v-model="payPwd"
            v-show="!passicon2"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10005.png" />
            </view>
            <view slot="suffix" @click="payPwdChange">
              <u-icon name="eye-off" color="#2E68F2" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <u-input
            border="none"
            :placeholder="$t('enterReferralCode')"
            placeholderClass="placeholder"
            password
            v-show="passicon3"
            v-model="inviteCode"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10006.png" />
            </view>
            <view slot="suffix" @click="codeChange">
              <u-icon name="eye" color="#2E68F2" size="46rpx"></u-icon>
            </view>
          </u-input>
          <u-input
            border="none"
            :placeholder="$t('enterReferralCode')"
            placeholderClass="placeholder"
            v-model="inviteCode"
            v-show="!passicon3"
          >
            <view class="icon" slot="prefix">
              <image mode="widthFix" src="@/static/img/10006.png" />
            </view>
            <view slot="suffix" @click="codeChange">
              <u-icon name="eye-off" color="#2E68F2" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="btn">
          <u-button
            class="button"
            @click="login"
            :text="$t('register')"
            color="#2E68F2"
            :loading="loading"
            shape="circle"
          ></u-button>
        </view>
      </view>
      <view class="other">
        <u-icon
          class="icon"
          color="#ffffff"
          name="checkmark-circle-fill"
        ></u-icon>
        <view>
          {{ $t("iKnow") }}
          <text>{{ $t("accountOpening") }} </text>
          {{ $t("termsContract") }}
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import lang from "@/components/lang";
export default {
  data() {
    return {
      radio: "1",
      passicon1: true,
      passicon2: true,
      passicon3: true,
      nickName: "", //昵称
      userName: "", //账号
      loginPwd: "", // 密码
      loading: false,
      payPwd: "", //交易密码
      inviteCode: "", //推荐码id
    };
  },
  onLoad(e) {
    if (e.code) {
      this.inviteCode = e.code;
    }
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
    payPwdChange() {
      this.passicon2 = !this.passicon2;
    },
    codeChange() {
      this.passicon3 = !this.passicon3;
    },
    login() {
      let pay = /^(?:0|(?:-?[1-9]\d*))$/;
      let en = /^(?=.*[a-zA-Z])(?=.*\d).+$/;
      let user = /^\d+$/;
      let cn =
        /^(?:[\u3400-\u4DB5\u4E00-\u9FEA\uFA0E\uFA0F\uFA11\uFA13\uFA14\uFA1F\uFA21\uFA23\uFA24\uFA27-\uFA29]|[\uD840-\uD868\uD86A-\uD86C\uD86F-\uD872\uD874-\uD879][\uDC00-\uDFFF]|\uD869[\uDC00-\uDED6\uDF00-\uDFFF]|\uD86D[\uDC00-\uDF34\uDF40-\uDFFF]|\uD86E[\uDC00-\uDC1D\uDC20-\uDFFF]|\uD873[\uDC00-\uDEA1\uDEB0-\uDFFF]|\uD87A[\uDC00-\uDFE0])+$/;
      // if (!cn.test(this.nickName) || this.nickName.length < 2) {
      //   return this.$base.show("请输入中文昵称且长度大于2~");
      // } else
      if (!user.test(this.userName)) {
        return this.$base.show(this.$t("accountLength"));
      } else if (!this.loginPwd || this.loginPwd.length < 6) {
        return this.$base.show(this.$t("passwordLength"));
      } else if (!pay.test(this.payPwd) || this.payPwd.length !== 6) {
        return this.$base.show(this.$t("payPasswordLength"));
      } else if (!this.inviteCode || this.inviteCode.length < 6) {
        return this.$base.show(this.$t("referralCodeLength"));
      }
      const DATA_OBJ = {
        userName: this.userName,
        loginPwd: this.loginPwd,
        nickName: this.nickName,
        payPwd: this.payPwd,
        inviteCode: this.inviteCode,
      };
      this.loading = true;
      this.$api
        .user_register(DATA_OBJ)
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
    routePage() {
      uni.redirectTo({
        url: "/pages/login",
      });
    },
  },
  components: {
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
    color: #2e68f2;
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
.main {
  background-repeat: no-repeat;
  background: url("@/static/img/10009.png");
  background-size: 100% auto;
  padding-top: var(--status-bar-height);
  .postion {
    padding-top: 350rpx;
    .title {
      color: #2e68f2;
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
      color: #2e68f2 !important;
      font-size: 30rpx;
    }
  }
}
.button {
  height: 88rpx;
  line-height: 88rpx;
}
.other {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #2e68f2;
  .icon {
    margin-right: 10rpx;
  }
  text,
  view {
    font-size: 24rpx;
    color: #fff;
  }
}
</style>
