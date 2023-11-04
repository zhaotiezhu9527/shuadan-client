<template>
  <view class="page">
    <u-navbar
      placeholder
      :title="$t('onlineService')"
      :border="false"
      autoBack
      fixed
      safe-area-inset-top
      bgColor="#fff"
      leftIconColor="#666"
      leftIconSize="32"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="wrap">
      <!-- <view v-if="config.onlineService">
        <iframe :src="config.onlineService" class="online"> </iframe>
      </view> -->
      <web-view v-show="config.onlineService" :webview-styles="webviewStyles"  :src="config.onlineService" class="online"></web-view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      config: {}, //配置
      webviewStyles: {
        progress: {
          color: '#00FF00'
        }
      }
    };
  },
  onLoad() {
    this.getConfig()
  },
  methods: {
    getConfig(){
      // uni.showLoading();
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
          // uni.hideLoading();
        } 
      });
    }
  },
};
</script>

<style scoped lang="scss">
.online {
  width: 100%;
  height: calc(100vh - 100rpx + var(--status-bar-height));
  // margin-top: 100rpx;
  border: none;
}
</style>
