<template>
  <u-popup :show="show" @close="close">
    <view class="main">
      <view class="form form2">
        <view class="flex items-center justify-center title">
          <u-icon
            @click="close"
            color="#ffffff"
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
            password
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
        <!-- <view class="other">
          <u-radio-group v-model="radio">
            <u-radio name="1" activeColor="#6c38ed" size="30rpx">
              <view class="agr">
                {{ $t("consent_left") }}
                <text>“{{ $t("consent_middle") }}”</text>
                {{ $t("consent_right") }}
              </view>
            </u-radio>
          </u-radio-group>
        </view> -->
        <view class="btn">
          <u-button
            class="button"
            @click="login"
            :text="$t('register')"
            :loading="loading"
            shape="circle"
          ></u-button>
          <!-- <u-button
            class="button-plain mt-20"
            @click="close"
            :text="$t('download')"
            :loading="loading"
            shape="circle"
          ></u-button> -->
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
      let pay = /^(?:0|(?:-?[1-9]\d*))$/;
      let en = /^(?=.*[a-zA-Z])(?=.*\d).+$/;
      let cn =
        /^(?:[\u3400-\u4DB5\u4E00-\u9FEA\uFA0E\uFA0F\uFA11\uFA13\uFA14\uFA1F\uFA21\uFA23\uFA24\uFA27-\uFA29]|[\uD840-\uD868\uD86A-\uD86C\uD86F-\uD872\uD874-\uD879][\uDC00-\uDFFF]|\uD869[\uDC00-\uDED6\uDF00-\uDFFF]|\uD86D[\uDC00-\uDF34\uDF40-\uDFFF]|\uD86E[\uDC00-\uDC1D\uDC20-\uDFFF]|\uD873[\uDC00-\uDEA1\uDEB0-\uDFFF]|\uD87A[\uDC00-\uDFE0])+$/;
      if (!en.test(this.userName) || this.userName.length < 6) {
        return this.$base.show(this.$t("account_len"));
      } else if (!this.loginPwd || this.loginPwd.length < 6) {
        return this.$base.show(this.$t("pwd_len"));
      } else if (!this.phone) {
        return this.$base.show(this.$t("input_phone"));
      } else if (!pay.test(this.payPwd) || this.payPwd.length !== 6) {
        return this.$base.show(this.$t("deal_pwd_len"));
      } else if (!this.inviteCode || this.inviteCode.length < 6) {
        return this.$base.show(this.$t("invite_code_len"));
      }
      const DATA_OBJ = {
        userName: this.userName,
        phone: this.phone,
        loginPwd: this.loginPwd,
        nickName: this.userName,
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
  background: #161617;
  box-shadow: 8rpx 8rpx 8rpx 0 rgba(20, 20, 22, 1);
  box-shadow: -8rpx -8rpx 8rpx 0 rgba(50, 51, 54, 0.3);
  box-shadow: -8rpx -8rpx 8rpx 0 rgba(32, 32, 34, 0.6) inset;
  box-shadow: 8rpx 8rpx 8rpx 0 rgba(18, 18, 20, 1) inset;
  margin-bottom: 64rpx;
  border-radius: 100rpx;
  height: 96rpx;
  line-height: 96rpx;
  position: relative;
  .name {
    font-size: 36rpx;
    color: $white;
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
</style>
