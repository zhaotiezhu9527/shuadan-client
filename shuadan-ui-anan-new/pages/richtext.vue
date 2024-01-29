<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="title"
      fixed
      safe-area-inset-top
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
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
  width: 100%;
  .helpstyle {
    padding: 30rpx 20rpx;
  }
  .helps {
    background: #fff;
    color: #343434;
    border-radius: 20rpx;
    padding: 40rpx 30rpx;
  }
}
</style>
