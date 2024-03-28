<template>
  <u-popup :show="show" @close="close">
    <view class="main">
      <image
        src="/static/img/logo.png"
        class="logo flex items-center"
        mode="widthFix"
      />
      <view class="form form2">
        <view class="flex items-center justify-center title">
          <u-icon
            @click="close"
            color="#5c5c5c"
            size="30"
            class="close"
            name="close"
          ></u-icon>
          <text class="name">{{ $t("register") }}</text>
        </view>
        <!-- <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('nickname')"
            border="none"
            placeholderClass="placeholder"
            v-model="nickName"
          ></u-input>
        </view> -->
        <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('account')"
            border="none"
            placeholderClass="placeholder"
            v-model="userName"
          ></u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('phoneNumber')"
            border="none"
            placeholderClass="placeholder"
            v-model="phone"
          ></u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('password')"
            border="none"
            placeholderClass="placeholder"
            v-model="loginPwd"
            password
          >
          </u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('acc_pwd')"
            border="none"
            placeholderClass="placeholder"
            v-model="payPwd"
          >
          </u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            :placeholder="$t('inviteCode')"
            border="none"
            placeholderClass="placeholder"
            v-model="inviteCode"
          ></u-input>
        </view>
       
        <view class="register-btn"
          @click="login"
          >
     
          {{$t('register')}}
        </view>
      </view>
    </view>
  </u-popup>
</template>
<script>
export default {
  data() {
    return {
      show: false,
      radio: "1",
      nickName: "", //昵称
      phone: "",//手机
      userName: "", //账号
      loginPwd: "", // 密码
      loading: false,
      payPwd: "", //交易密码
      inviteCode: "", //推荐码id
    };
  },
  methods: {
    open(e) {
      this.show = true;
      this.$nextTick(() => {
        this.inviteCode = e;
      });
    },
    close() {
      this.show = false;
    },
    login() {
      let pay = /^[a-zA-Z0-9]{6,15}$/;
      let en = /^(?=.*[a-zA-Z])(?=.*\d).+$/;
      let cn =
        /^(?:[\u3400-\u4DB5\u4E00-\u9FEA\uFA0E\uFA0F\uFA11\uFA13\uFA14\uFA1F\uFA21\uFA23\uFA24\uFA27-\uFA29]|[\uD840-\uD868\uD86A-\uD86C\uD86F-\uD872\uD874-\uD879][\uDC00-\uDFFF]|\uD869[\uDC00-\uDED6\uDF00-\uDFFF]|\uD86D[\uDC00-\uDF34\uDF40-\uDFFF]|\uD86E[\uDC00-\uDC1D\uDC20-\uDFFF]|\uD873[\uDC00-\uDEA1\uDEB0-\uDFFF]|\uD87A[\uDC00-\uDFE0])+$/;
      if (this.userName.length < 4) {
        return this.$base.show(this.$t("account_len"));
      } else if (!this.loginPwd || this.loginPwd.length < 6) {
        return this.$base.show(this.$t("pwd_len"));
      } else if (!this.phone) {
        return this.$base.show(this.$t("input_phone"));
      } else if (!pay.test(this.payPwd.replace(/\s*/g,""))) {
        return this.$base.show(this.$t("deal_pwd_len"));
      } else if (!this.inviteCode || this.inviteCode.length < 6) {
        return this.$base.show(this.$t("invite_code_len"));
      }
      const DATA_OBJ = {
        userName: this.userName.replace(/\s*/g,""),
        phone: this.phone.replace(/\s*/g,""),
        loginPwd: this.loginPwd.replace(/\s*/g,""),
        nickName: this.userName.replace(/\s*/g,""),
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

<style scoped lang="scss">
@import "@/static/login.scss";
.main{
  width: 100%;
}
.form2 {
  width: 90% !important;
}
.title {
  border: 1px solid #4b4b4b;
  border-radius: 10rpx;
  margin-bottom: 26rpx;
  height: 96rpx;
  line-height: 96rpx;
  position: relative;
  .name {
    font-size: 36rpx;
    color: #5c5c5c;
    font-weight: 600;
  }
  .close {
    position: absolute;
    left: 20rpx;
    top: 50%;
    transform: translateY(-50%);
    /deep/.u-icon__icon {
      font-weight: 600 !important;
    }
  }
}
@media screen and(min-width: 1100px) {
  /deep/ .u-popup__content {
    width: 818rpx;
    margin: auto !important;
  }
}
/deep/ .u-popup__content {
  background-color: #111 !important;
  background: linear-gradient(180deg, #242527 0%, #000000 100%),
    linear-gradient(180deg, rgba($white, 0.16) 0%, rgba($white, 0) 100%);
  border-style: solid;
  border-image-source: linear-gradient(
    180deg,
    rgba($white, 0.16) 0%,
    rgba($white, 0) 100%
  );
  box-shadow: 0 -16rpx 16rpx 0 rgba($white, 0.12);
  padding: 48rpx 0 0 0;
  border-radius: 72rpx 72rpx 0 0;
}
.other {
  color: $white;
  padding: 48rpx 0;
}
.form {
  padding-bottom: calc(64rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(64rpx + env(safe-area-inset-bottom));
}
.register-btn{
  height: 104rpx;
  line-height: 104rpx;
  background-color: #575651;
  border-radius: 10rpx;
  text-align: center;
  color: #fff;
}
</style>
