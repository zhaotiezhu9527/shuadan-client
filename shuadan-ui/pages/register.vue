<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('enterPassword')"
      fixed
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
      <template #left>
        <u-icon name="close" size="40rpx"></u-icon>
      </template>
    </u-navbar>
    <view class="mainstyle">
      <view class="main">
        <view class="input">
          <text>{{$t('nickName') }}</text>
          <u-input
            shape="circle"
            :placeholder="$t('enterNickName')"
            border="surround"
            placeholderClass="placeholder"
            v-model="nickName"
          ></u-input>
        </view>
        <view class="input">
          <text>{{$t('userName') }}</text>
          <u-input
            shape="circle"
            :placeholder="$t('enterUserName')"
            border="surround"
            placeholderClass="placeholder"
            v-model="userName"
          ></u-input>
        </view>
        <view class="input">
          <text>{{$t('password') }}</text>
          <u-input
            shape="circle"
            :placeholder="$t('enterPassword')"
            border="surround"
            placeholderClass="placeholder"
            v-model="loginPwd"
            v-show="passicon1"
            password
          >
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
          <u-input
            shape="circle"
            :placeholder="$t('enterPassword')"
            border="surround"
            placeholderClass="placeholder"
            v-model="loginPwd"
            v-show="!passicon1"
          >
            <view slot="suffix" @click="pwdChange">
              <u-icon name="eye-off" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <text>{{$t('payPassword') }}</text>
          <u-input
            shape="circle"
            :placeholder="$t('enterPayPassword')"
            border="surround"
            placeholderClass="placeholder"
            v-model="payPwd"
            password
            v-show="passicon2"
          >
            <view slot="suffix" @click="payPwdChange">
              <u-icon name="eye" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
          <u-input
            shape="circle"
            :placeholder="$t('enterPayPassword')"
            border="surround"
            placeholderClass="placeholder"
            v-model="payPwd"
            v-show="!passicon2"
          >
            <view slot="suffix" @click="payPwdChange">
              <u-icon name="eye-off" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <text>{{$t('referralCode') }}</text>
          <u-input
            shape="circle"
            :placeholder="$t('enterReferralCode')"
            border="surround"
            placeholderClass="placeholder"
            v-model="inviteCode"
          ></u-input>
        </view>
        <view class="other">
          <u-radio-group v-model="radio">
            <u-radio name="1" activeColor="red" size="30rpx">
              <view>
                {{$t('iKnow') }} 
                <text>{{$t('accountOpening') }} </text>
                {{$t('termsContract') }} 
              </view>
            </u-radio>
          </u-radio-group>
        </view>
        <view class="btn">
          <u-button
            shape="circle"
            type="primary"
            color="#ec0022"
            :text="$t('register')"
            hairline
            :loading="loading"
            @click="login"
          ></u-button>
          <u-button
            @click="roeute"
            hairline
            shape="circle"
            type="primary"
            color="#ffffff"
            :text="$t('haveAccount')"
            plain
          ></u-button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      radio: "1",
      passicon1: true,
      passicon2: true,
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
    pwdChange() {
      this.passicon1 = !this.passicon1;
    },
    payPwdChange() {
      this.passicon2 = !this.passicon2;
    },
    login() {
      let pay = /^\d{6}$/;
      let en = /^(?=.*[a-zA-Z])(?=.*\d).+$/;
      let cn =
        /^(?:[\u3400-\u4DB5\u4E00-\u9FEA\uFA0E\uFA0F\uFA11\uFA13\uFA14\uFA1F\uFA21\uFA23\uFA24\uFA27-\uFA29]|[\uD840-\uD868\uD86A-\uD86C\uD86F-\uD872\uD874-\uD879][\uDC00-\uDFFF]|\uD869[\uDC00-\uDED6\uDF00-\uDFFF]|\uD86D[\uDC00-\uDF34\uDF40-\uDFFF]|\uD86E[\uDC00-\uDC1D\uDC20-\uDFFF]|\uD873[\uDC00-\uDEA1\uDEB0-\uDFFF]|\uD87A[\uDC00-\uDFE0])+$/;
      // if (!cn.test(this.nickName) || this.nickName.length < 2) {
      //   return this.$base.show("请输入中文昵称且长度大于2~");
      // } else 
      if (!en.test(this.userName) || this.userName.length < 6) {
        return this.$base.show(this.$t('accountLength'));
      } else if (!this.loginPwd || this.loginPwd.length < 6) {
        return this.$base.show(this.$t('passwordLength'));
      } else if (!pay.test(this.payPwd.replace(/\s*/g,""))) {
        return this.$base.show(this.$t('payPasswordLength'));
      } else if (!this.inviteCode || this.inviteCode.length < 6) {
        return this.$base.show(this.$t('referralCodeLength'));
      }
      const DATA_OBJ = {
        userName: this.userName.replace(/\s*/g,""),
        loginPwd: this.loginPwd.replace(/\s*/g,""),
        nickName: this.nickName.replace(/\s*/g,""),
        payPwd: this.payPwd.replace(/\s*/g,""),
        inviteCode: this.inviteCode.replace(/\s*/g,""),
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
    roeute() {
      uni.redirectTo({
        url: "/pages/login",
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.mainstyle {
  padding-top: 40rpx;
  .main {
    width: 90%;
    margin: 0 auto;
    box-shadow: 0 0 20rpx 10rpx #dfdfdf;
    border-radius: 20rpx;
    padding: 30rpx;
  }
  .input {
    padding-bottom: 30rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    text {
      width: 140rpx;
      text-align: center;
    }
    .placeholder {
      color: #666 !important;
      font-size: 28rpx;
    }
    .u-input {
      padding-left: 30rpx !important;
      border-color: #eee !important;
    }
  }
  .other {
    display: flex;
    align-items: center;
    text,
    view {
      font-size: 24rpx;
    }
    text {
      color: red;
    }
  }
}
.u-button {
  margin-top: 40rpx;
  &:nth-child(1) /deep/.u-button__text {
    color: #fff;
  }
  &:nth-child(2) {
    border-color: #ec0022 !important;
    /deep/.u-button__text {
      color: #ec0022;
    }
  }
}
</style>
