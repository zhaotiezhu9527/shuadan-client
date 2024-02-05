<template>
  <view class="main3">
    <u-navbar
      placeholder
      :border="false"
      :title="$t('service')"
      fixed
      safe-area-inset-top
      leftIconSize="0"
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="container">
      <view class="main">
        <view class="container">
          <view class="title">{{$t('help') }}</view>
          <view class="sub_title">{{$t('relation_service') }}</view>
        </view>
        <view class="customer_type">
          <image class="image" src="@/static/img/kefu-service.png" mode="widthFix" />
        </view>
        <!-- <view class="support-hours">
          {{$t('supportHours') }}
        </view> -->
        <view class="service-btn" @click="goService">
          {{$t('service') }}
        </view>
      </view>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      list: [],
      config: {},
    };
  },
  onShow() {
  },
  methods: {
    goService() {
      if(window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))
      {
        uni.navigateTo({
          url: "/pages/onlineService"
        });
      }else{
        // #ifdef APP-PLUS
        plus.runtime.openURL(this.config.onlineService);
        // #endif
        // #ifdef H5
        window.open(this.config.onlineService);
        // #endif
      }
    },
    getConfig(){
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
        } 
      });
    },
    open(){
      this.getConfig()
    }
  },
};
</script>
<style lang="scss" scoped>
.main {
  padding: 0;
  width: 100%;
  color: #585858;
  text-align: center;
  .container {
    height: 280rpx;
    padding: 80rpx 30rpx 0;
    // background: #bac3d2;
    position: relative;
    z-index: 2;
    .title {
      font-size: 34rpx;
      font-weight: 600;
    }
    .sub_title {
      font-size: 24rpx;
    }
  }
  .customer_type {
    .image {
      width: 524rpx;
      margin: 60rpx auto 0 auto;
      display: block;
    }
    .list {
      padding-top: 120rpx;
    }
  }
  .support-hours{
    width: 100%;
    text-align: center;
    color: #fff;
    font-size: 24rpx;
    margin-top: -60rpx;
  }
  .service-btn{
    width: 500rpx;
    height: 84rpx;
    text-align: center;
    color: #fff;
    background-color: #575651;
    border-radius: 10rpx;
    line-height: 84rpx;
    font-size: 28rpx;
    margin: 0rpx auto 0 auto;
  }
}
</style>
