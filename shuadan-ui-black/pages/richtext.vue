<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="title"
      fixed
      safe-area-inset-top
      leftIconColor="#ffffff"
      bgColor="#000000"
      height="100rpx"
      leftIconSize="32"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
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
      title: "",
      content: "",
    };
  },
  onLoad(e) {
    this.$api.message(e.en).then(({ data }) => {
      if (data.code == 0) {
        this.title = data.data.title;
        this.content = data.data.content;
      } else {
        this.$base.show(data.msg);
      }
    });
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
