<template>
  <view class="page">
    <u-navbar
      placeholder
      :title="$t('service')"
      :border="false"
      autoBack
      fixed
      safe-area-inset-top
      bgColor="#000000"
      leftIconColor="#ffffff"
      leftIconSize="32"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="wrap">
      <view v-if="path">
        <iframe :src="path" class="online"> </iframe>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      config: {}, //配置
      path: "",
    };
  },
  async onLoad(e) {
    await this.$onLaunched;
    this.config = uni.getStorageSync("config");
    this.path = e.path || this.config.onlineService;
  },
  methods: {},
};
</script>

<style scoped lang="scss">
.online {
  width: 100%;
  height: calc(100vh - 100rpx + var(--status-bar-height));
  border: none;
}
</style>
