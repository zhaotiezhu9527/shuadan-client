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
        <view class="payment-item" onclick="_MEIQIA('showPanel')">
            <image class="payment-img" src="../static/img/tiktok.png" />
            <view class="payment-text">
              <view class="payment-text-box">
                <text class="payment-text-black">{{$t('douBinRecharge') }}</text>
                <text class="payment-text-red">{{$t('recommend') }}</text>
              </view>
              <view class="payment-text-grey">{{$t('douBinRecharge') }}</view>
            </view>
        </view>
        <view class="payment-item" onclick="_MEIQIA('showPanel')">
            <image class="payment-img" src="../static/img/unionpay.png" />
            <view class="payment-text">
              <view class="payment-text-box">
                <text class="payment-text-black">{{$t('bankCardTransfer') }}</text>
                <text class="payment-text-red">{{$t('recommend') }}</text>
              </view>
              <view class="payment-text-grey">{{$t('bankCardTransfer') }}</view>
            </view>
        </view>
        <view class="payment-item" onclick="_MEIQIA('showPanel')">
            <image class="payment-img" src="../static/img/icon_usdt.jpeg" />
            <view class="payment-text">
              <view class="payment-text-box">
                <text class="payment-text-black">{{$t('USDTdeposit') }}</text>
                <text class="payment-text-red">{{$t('recommend') }}</text>
              </view>
              <view class="payment-text-grey">{{$t('USDTdeposit') }}</view>
            </view>
        </view>
      </view>
    </view>
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
      config:{},
    };
  },
  mounted (){
    this.getConfig()
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
    readFn(kfid,mate){
      (function(m, ei, q, i, a, j, s) {
        m[i] = m[i] || function() {
          (m[i].a = m[i].a || []).push(arguments)
        };
        j = ei.createElement(q),
                s = ei.getElementsByTagName(q)[0];
        j.async = true;
        j.charset = 'UTF-8';
        j.src = 'https://static.meiqia.com/dist/meiqia.js';
        s.parentNode.insertBefore(j, s);
      })(window, document, 'script', '_MEIQIA');
      _MEIQIA('entId', kfid);
      // 在这里开启手动模式（必须紧跟美洽的嵌入代码）
      _MEIQIA('init');
      _MEIQIA('withoutBtn');
      if(mate != null){
        _MEIQIA('metadata', {
            comment: mate.account,
            name: mate.name, // 美洽默认字段
            tel: mate.tel, // 美洽默认字段
        });
      }
    },
    getConfig(){
      // uni.showLoading();
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
          // uni.hideLoading();
          this.readFn(this.config.kfid,this.config.mate)
        } 
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
