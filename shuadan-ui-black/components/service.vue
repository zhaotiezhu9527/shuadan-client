<template>
  <view class="sevice" @click="submit">
    <view class="bg"></view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      config: {},
    };
  },
  onShow() {
    this.getConfig();
  },
  methods: {
    submit() {
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
  },
};
</script>
<style lang="scss" scoped>
.sevice {
  position: fixed;
  bottom: 40rpx;
  left: 0rpx;
  border-radius: 50%;
  width: 120rpx;
  height: 120rpx;
  z-index: 99;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #007aff;
  .bg {
    background: url(@/static/img/icon-mq-round@2x.png) 0 0 / 128rpx no-repeat;
    width: 64rpx;
    height: 64rpx;
  }
}
</style>
