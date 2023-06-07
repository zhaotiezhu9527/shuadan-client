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
        <u-input
          placeholder="请输入金额"
          border="bottom"
          v-model="value"
          clearable
          @change="change"
          type="number"
        >
          <template #prefix>
            <view class="rmb">￥</view>
          </template>
        </u-input>
        <view class="txt">充值说明</view>
        <view class="list">
          <view class="item" v-for="(item, index) in list" :key="index">
            {{ item }}元
          </view>
        </view>
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
      value: "",
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
      padding: 30rpx;
      border-radius: 5rpx;
      box-shadow: 0 0 10rpx 0 rgba(0, 0, 0, 0.15);
      background-color: #fff;
    }
    .txt,
    .title {
      font-size: 24rpx;
    }
    .txt {
      color: #999;
    }
  }
  .u-input {
    padding: 20rpx 18rpx !important;
  }
  .rmb {
    color: red;
    font-size: 24rpx;
  }
  .list {
    padding-top: 20rpx;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 20rpx;
    .item {
      border: 1px solid #666;
      font-size: 24rpx;
      padding: 10rpx 0;
      width: 130rpx;
      color: red;
      text-align: center;
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
