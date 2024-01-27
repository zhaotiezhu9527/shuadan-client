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
    background: linear-gradient(134.61deg, #333333 0.81%, #1e1e1e 97.27%),
      conic-gradient(
        from 180deg at 50% 50%,
        rgba(245, 211, 172, 0) 0deg,
        rgba(245, 211, 172, 0.38) 45deg,
        rgba(245, 211, 172, 0) 84.38deg,
        rgba(245, 211, 172, 0) 133.12deg,
        rgba(245, 211, 172, 0.37) 187.5deg,
        rgba(245, 211, 172, 0) 230.62deg,
        rgba(245, 211, 172, 0) 360deg
      );
    color: $white !important;
    border-radius: 20rpx;
    padding: 40rpx 30rpx;
  }
}
</style>
