<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="首页"
      fixed
      leftIconSize="0"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view>首页</view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {},
};
</script>

<style scoped lang="scss"></style>
