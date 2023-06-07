<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="我要分享"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      height="50px"
      class="nav-custom"
    >
    </u-navbar>
    <view class="main">
      <view class="head">
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {},
};
</script>

<style scoped lang="scss">
.main{
  .head{
    width: 100%;
    height: 372rpx;
    background: url(../static/img/msg.png) no-repeat;
    background-size: 100% 100%;
    overflow: hidden;
  }
}
.nav-custom{
  color:#fff;
  font-weight:500;
  font-size:32rpx;
  background-image: linear-gradient(135deg, #F9B821 10%, #FBE750 100%)!important;
}
</style>
