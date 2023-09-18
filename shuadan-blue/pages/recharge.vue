<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('recharge')"
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
      <view class="content" v-if="!active">
        <view class="title">{{$t('paymentChannel') }}</view>
        <view class="payment-item" @click="submit">
          <image class="payment-img" src="../static/img/tiktok.png" />
          <view class="payment-text">
            <view class="payment-text-box">
              <text class="payment-text-black">{{$t('抖币充值') }}</text>
              <text class="payment-text-red">{{$t('推荐') }}</text>
            </view>
            <view class="payment-text-grey">{{$t('抖币充值') }}</view>
          </view>
        </view>
        <view class="payment-item" @click="submit">
          <image class="payment-img" src="../static/img/unionpay.png" />
          <view class="payment-text">
            <view class="payment-text-box">
              <text class="payment-text-black">{{$t('银行卡转账') }}</text>
              <text class="payment-text-red">{{$t('推荐') }}</text>
            </view>
            <view class="payment-text-grey">{{$t('银行卡转账') }}</view>
          </view>
        </view>
        <view class="payment-item" @click="submit">
          <image class="payment-img" src="../static/img/icon_usdt.jpeg" />
          <view class="payment-text">
            <view class="payment-text-box">
              <text class="payment-text-black">{{$t('USDT充值') }}</text>
              <text class="payment-text-red">{{$t('推荐') }}</text>
            </view>
            <view class="payment-text-grey">{{$t('USDT充值') }}</view>
          </view>
        </view>
      </view>
    </view>
    <service />
  </view>
</template>

<script>
import service from "@/components/service";
export default {
  data() {
    return {
      list: [100, 300, 500, 1000, 3000, 5000, 10000, 50000],
      active: 0,
      value: "",
    };
  },
  methods: {
    change() {
      let num = /^\+?[1-9]\d*$/;
      if (!num.test(this.value)) {
        this.$base.show(this.$t('enterInteger'));
      } else {
        this.active = 1;
      }
    },
    submit() {
      uni.navigateTo({
        url: "/pages/onlineService",
      });
    },
  },
  components: {
    service,
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
      .payment-item{
        padding: 16rpx 10rpx;
        border-bottom: 2rpx solid #f2f2f2;
        background: url(../static/img/jiaobiao.png) no-repeat 100%;
        background-size: auto 30rpx;
        background-origin: content-box;
        background-color: #fff;
        overflow: hidden;
        .payment-img{
          width: 80rpx;
          height: 80rpx;
          float: left;
          margin-right: 30rpx;
        }
        .payment-text{
          float: left;
          font-size: 28rpx;
          .payment-text-red{
            display: block;
            background: red;
            padding: 1px 4px;
            color: #fff;
            border-radius: 3px;
            float: left;
            margin-left: 20rpx;
          }
          .payment-text-black{
            float: left;
          }
          .payment-text-box{
            overflow: hidden;
          }
          .payment-text-grey{
            font-size: 24rpx;
            color: #777;
            margin-top: 10rpx;
          }
        }
      }
    }
    .title {
      font-size: 28rpx;
      color: #333;
      margin-bottom: 10rpx;
    }
  }
  .u-input {
    padding: 20rpx 18rpx !important;
    /deep/.uni-input-input,
    /deep/.uni-input-placeholder {
      font-size: 38rpx;
      color: #666;
    }
  }
  .rmb {
    color: #d91d37;
    font-size: 24rpx;
  }
  .link {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx 0;
    .image {
      width: 78rpx;
      margin-right: 25rpx;
    }
    .flex {
      display: flex;
      align-items: center;
    }
  }
  .box {
    view:nth-child(1) {
      font-size: 28rpx;
    }
    view:nth-child(2) {
      padding-top: 20rpx;
      font-size: 24rpx;
      color: #777777;
    }
  }
}
</style>
