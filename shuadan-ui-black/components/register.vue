<template>
  <u-popup :show="show" @close="close" @open="open">
    <view class="main">
      <view class="form">
        <view class="input">
          <u-input
            shape="circle"
            placeholder="昵称"
            border="none"
            placeholderClass="placeholder"
            v-model="nickName"
          ></u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            placeholder="账号"
            border="none"
            placeholderClass="placeholder"
            v-model="userName"
          ></u-input>
        </view>
        <view class="input">
          <u-input
            shape="circle"
            placeholder="密码"
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
            placeholder="请输入交易密码"
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
            placeholder="请输入推荐码"
            border="none"
            placeholderClass="placeholder"
            v-model="inviteCode"
          ></u-input>
        </view>
        <view class="other">
          <u-radio-group v-model="radio">
            <u-radio name="1" activeColor="#6c38ed" size="30rpx">
              <view class="agr">
                我已知晓并同意
                <text>“开户协议”</text>
                各项条约
              </view>
            </u-radio>
          </u-radio-group>
        </view>
        <view class="btn">
          <u-button
            class="button"
            @click="login"
            text="注册"
            :loading="loading"
            shape="circle"
          ></u-button>
          <u-button
            class="button-plain mt-20"
            @click="close"
            text="已有账号，马上下载"
            :loading="loading"
            shape="circle"
          ></u-button>
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
    open() {
      this.show = true;
    },
    close() {
      this.show = false;
    },
    pwdChange() {
      this.passicon1 = !this.passicon1;
    },
    payPwdChange() {
      this.passicon2 = !this.passicon2;
    },
    login() {
      let pay = /^(?:0|(?:-?[1-9]\d*))$/;
      let en = /^(?=.*[a-zA-Z])(?=.*\d).+$/;
      let cn =
        /^(?:[\u3400-\u4DB5\u4E00-\u9FEA\uFA0E\uFA0F\uFA11\uFA13\uFA14\uFA1F\uFA21\uFA23\uFA24\uFA27-\uFA29]|[\uD840-\uD868\uD86A-\uD86C\uD86F-\uD872\uD874-\uD879][\uDC00-\uDFFF]|\uD869[\uDC00-\uDED6\uDF00-\uDFFF]|\uD86D[\uDC00-\uDF34\uDF40-\uDFFF]|\uD86E[\uDC00-\uDC1D\uDC20-\uDFFF]|\uD873[\uDC00-\uDEA1\uDEB0-\uDFFF]|\uD87A[\uDC00-\uDFE0])+$/;
      if (!cn.test(this.nickName) || this.nickName.length < 2) {
        return this.$base.show("请输入中文昵称且长度大于2~");
      } else if (!en.test(this.userName) || this.userName.length < 6) {
        return this.$base.show("请输入账号且长度大于6~");
      } else if (!this.loginPwd || this.loginPwd.length < 6) {
        return this.$base.show("请输入密码且长度大于6~");
      } else if (!pay.test(this.payPwd) || this.payPwd.length !== 6) {
        return this.$base.show("请输入6位数字支付密码~");
      } else if (!this.inviteCode || this.inviteCode.length < 6) {
        return this.$base.show("请输入推荐码ID且长度大于6~");
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
