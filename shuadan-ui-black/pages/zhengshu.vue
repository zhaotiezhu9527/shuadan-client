<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('certificate')"
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
      <image class="img" src="@/static/img/zhengshu.jpg" mode="widthFix" />
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
  .img{
    width:100%;
  }
}
</style>
