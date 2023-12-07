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
      <view class="progress-bar-background">
        <view class="progress-bar" :style="{ width: progressBarWidth + '%' }"></view>
      </view>
      <web-view :webview-styles="webviewStyles" :src="config.onlineService" class="online">
      </web-view>
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
      },
      progressBarWidth: 0, // 控制进度条宽度的数据
    };
  },
  onLoad() {
    this.getConfig()
  },
  onReady() {
    // 页面准备好之后，开始加载
    let self = this;
    let interval = setInterval(() => {
      // 假设每个时间间隔进度条增加10%
      if (self.progressBarWidth >= 100) {
        clearInterval(interval);
      } else {
        self.progressBarWidth += 10;
      }
    }, 300);
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
    },
  },
};
</script>

<style scoped lang="scss">
.online {
  width: 100%;
  margin-top: 100rpx;
  height: calc(100vh - 100rpx + var(--status-bar-height));
  // margin-top: 100rpx;
  border: none;
}
.progress-bar-background {
  width: 100%;
  height: 5px;
  background-color: #e0e0e0;
}
.progress-bar {
  height: 5px;
  background-color: #03a9f4;
  width: 0; /* 初始化进度条宽度为0 */
}
</style>
