<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      :title="$t('service')"
      fixed
      bgColor="#000000"
      leftIconSize="0"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
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
    };
  },
  methods: {
    goService() {
      uni.navigateTo({
        url: "/pages/onlineService",
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main {
  padding: 0;
  width: 100%;
  color: #fff;
  .container {
    height: 280rpx;
    padding: calc(var(--status-bar-height) + 40rpx) 30rpx 0;
    // background: #bac3d2;
    position: relative;
    z-index: 2;
    .title {
      font-size: 34rpx;
      font-weight: 600;
    }
    .sub_title {
      font-size: 24rpx;
      padding-top: 40rpx;
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
    width: 260rpx;
    height: 84rpx;
    text-align: center;
    color: #fff;
    background-color: #2E68F2;
    border-radius: 42rpx;
    line-height: 84rpx;
    font-size: 28rpx;
    margin: 0rpx auto 0 auto;
  }
}
</style>
