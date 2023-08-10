<template>
  <view class="sevice" @click="submit">
    <view class="bg"></view>
  </view>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    submit() {
      uni.navigateTo({
        url: "/pages/onlineService",
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
