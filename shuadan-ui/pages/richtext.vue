<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="title"
      fixed
      safe-area-inset-top
      bgColor="#fff"
      leftIconColor="#666"
      height="50px"
      leftIconSize="32"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="helpstyle">
        <view class="helps">
          <rich-text :nodes="content" space="nbsp" image-menu-prevent />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import annunciate from "@/components/annunciate";
export default {
  data() {
    return {
      title: "平台简介",
      content: "123",
    };
  },
  onLoad(e) {
    this.title = e.title;
  },
  onShow() {},
  methods: {},
};
</script>

<style scoped lang="scss">
.main {
  background-color: #f2f2f2;
  height: calc(100vh - 100rpx);
  width: 100%;
  .helpstyle {
    padding: 30rpx 20rpx;
  }
  .helps {
    background: #fff;
    border-radius: 20rpx;
    padding: 40rpx 20rpx;
  }
}
</style>
