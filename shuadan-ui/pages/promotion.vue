<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="我要分享"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="boxStyle">
      <view class="box">
        <view class="rwm"></view>
        <view class="txt">ID:12312312</view>
        <view style="padding-top: 20rpx">
          <u-button color="#89bfdb" text="立即使用" @click="submit"></u-button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    submit() {
      uni.setClipboardData({
        data: "123",
        success() {
          console.log("123");
        },
        fail() {},
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main {
  height: 100vh;
  background: linear-gradient(1turn, #89bfdb, #a4e0d8);
  .boxStyle {
    padding: 30rpx;
  }
  .box {
    padding: 100rpx 20rpx;
    background: #fff;
    border-radius: 20rpx;
    box-shadow: 0 0 10rpx 0 #eee;
    .rwm {
      border: 1rpx solid #eee;
      width: 300rpx;
      height: 300rpx;
      margin: 0 auto;
    }
    .txt {
      text-align: center;
      font-size: 26rpx;
      padding: 20rpx 0;
    }
  }
}
</style>
