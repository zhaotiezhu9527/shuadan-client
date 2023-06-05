<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="注册"
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
          <text>昵称</text>
          <u-input
            shape="circle"
            placeholder="请输入您的中文昵称"
            border="surround"
            placeholderClass="placeholder"
          ></u-input>
        </view>
        <view class="input">
          <text>账号</text>
          <u-input
            shape="circle"
            placeholder="请输入字母加数字"
            border="surround"
            placeholderClass="placeholder"
          ></u-input>
        </view>
        <view class="input">
          <text>密码</text>
          <u-input
            shape="circle"
            placeholder="请输入密码"
            border="surround"
            placeholderClass="placeholder"
          >
            <view slot="suffix">
              <u-icon
                v-if="passicon1"
                name="eye"
                color="#666"
                size="46rpx"
              ></u-icon>
              <u-icon v-else name="eye-off" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <text>交易密码</text>
          <u-input
            shape="circle"
            placeholder="请输入交易密码"
            border="surround"
            placeholderClass="placeholder"
          >
            <view slot="suffix">
              <u-icon
                v-if="passicon2"
                name="eye"
                color="#666"
                size="46rpx"
              ></u-icon>
              <u-icon v-else name="eye-off" color="#666" size="46rpx"></u-icon>
            </view>
          </u-input>
        </view>
        <view class="input">
          <text>推荐码</text>
          <u-input
            shape="circle"
            placeholder="请输入推荐码"
            border="surround"
            placeholderClass="placeholder"
          ></u-input>
        </view>
        <view class="other">
          <u-radio-group v-model="radio">
            <u-radio name="1" activeColor="red" size="30rpx">
              <view>
                我已知晓并同意
                <text>“开户协议”</text>
                各项条约
              </view>
            </u-radio>
          </u-radio-group>
        </view>
        <view class="btn">
          <u-button
            shape="circle"
            type="primary"
            color="#ec0022"
            text="注册"
            hairline
          ></u-button>
          <u-button
            @click="roeute"
            hairline
            shape="circle"
            type="primary"
            color="#ffffff"
            text="已有帐号，马上下载"
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
      passicon1: false,
      passicon2: false,
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {
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
