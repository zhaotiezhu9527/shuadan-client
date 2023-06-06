<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="充值"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="contentStyle">
      <view class="content">
        <view class="title">充值金额</view>
        <view class="txt">充值说明</view>
      </view>
    </view>
    <view class="btn">
      <u-button type="primary" color="#2f3848" text="下一步"></u-button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      list: [100, 300, 500, 1000, 3000, 5000, 10000, 50000],
      active: 0,
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {
    submit() {},
  },
};
</script>

<style scoped lang="scss">
.main {
  width: 100%;
  min-height: 100vh;
  background-color: #f2f2f2;
  .contentStyle {
    padding: 20rpx 30rpx;
    .content {
      padding: 40rpx 30rpx;
      border-radius: 5rpx;
      box-shadow: 0 0 10rpx 0 rgba(0, 0, 0, 0.15);
      background-color: #fff;
    }
  }
  .btn {
    padding: 20rpx 30rpx 0;
    /deep/.u-button__text {
      color: #fff;
    }
  }
}
</style>
