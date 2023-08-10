<template>
  <view class="main">
    <image class="login" src="@/static/img/login.png" />
    <view class="title">
      <view class="sub">全民任务</view>
      <view class="name">账号密码登录</view>
    </view>
    <view class="form">
      <view class="tabs">
        <view @click="roeute">密码登录</view>
        <view class="active">账户注册</view>
      </view>
      <view class="input">
        <view class="label">昵称</view>
        <u-input
          shape="circle"
          placeholder="请输入您的中文昵称"
          border="none"
          placeholderClass="placeholder"
          v-model="nickName"
        ></u-input>
      </view>
      <view class="input">
        <view class="label">账号</view>
        <u-input
          shape="circle"
          placeholder="请输入字母加数字"
          border="none"
          placeholderClass="placeholder"
          v-model="userName"
        ></u-input>
      </view>
      <view class="input">
        <view class="label">密码</view>

        <u-input
          shape="circle"
          placeholder="请输入密码"
          border="none"
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
          placeholder="请输入密码"
          border="none"
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
        <view class="label">交易密码</view>
        <u-input
          shape="circle"
          placeholder="请输入交易密码"
          border="none"
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
          placeholder="请输入交易密码"
          border="none"
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
        <view class="label">推荐码</view>
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
          <u-radio name="1" activeColor="#FFAD49" size="30rpx">
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
          color="linear-gradient(180deg, #FFAD49 0%, #FFC861 99%)"
          :loading="loading"
          shape="circle"
        ></u-button>
      </view>
      <view class="registerStyle">
        <view class="register" @click="roeute">
          已有账号，马上下载
          <image class="img" src="@/static/img/loginRjiantou.png" />
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
<style lang="scss" scoped>
@import "@/static/login.scss";
.input {
  display: flex;
  align-items: center;
  .label {
    font-size: 32rpx !important;
    font-weight: 500;
    color: #333333;
    width: 144rpx;
    text-align: justify;
    text-align-last: justify;
    padding-bottom: 0 !important;
  }
}
.main .form .u-input {
  background-color: #f2f2f2;
  padding: 20rpx 40rpx !important;
  margin-left: 20rpx;
}
.other {
  margin-bottom: 40rpx;
  .agr {
    color: #999;
  }
}
.register {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffa23f;
  font-size: 36rpx;
  .img {
    width: 48rpx;
    height: 48rpx;
  }
}
.registerStyle {
  padding: 30rpx 0;
  padding-bottom: calc(constant(safe-area-inset-bottom) + 30rpx);
  padding-bottom: calc(env(safe-area-inset-bottom) + 30rpx);
}
</style>
