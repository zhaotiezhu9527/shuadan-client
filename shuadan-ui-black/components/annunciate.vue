<template>
  <u-modal
    :show="show"
    width="90vw"
    :showConfirmButton="false"
    :showCancelButton="false"
  >
    <view class="flex">
      <view class="message_block">
        <view class="title flex items-center">特别公告通知</view>
        <view class="message_info_content">
          <rich-text class="rich" :nodes="content"></rich-text>
          <u-button
            class="button"
            @click="show = false"
            text="确定"
            shape="circle"
          ></u-button>
        </view>
        <image src="/static/img/bg-004.png" class="img1" mode="widthFix" />
        <image src="/static/img/bg-005.png" class="img2" mode="widthFix" />
      </view>
    </view>
  </u-modal>
</template>

<script>
export default {
  data() {
    return {
      show: true,
      content: "",
    };
  },
  async mounted() {
    await this.$onLaunched;
    this.content = uni.getStorageSync("config").homeMsg;
  },
  methods: {},
};
</script>

<style scoped lang="scss">
/deep/ .u-popup__content {
  background: transparent;
  overflow: inherit !important;
}
/deep/.u-modal {
  overflow: inherit !important;
}
/deep/ .u-modal__content {
  padding: 0;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  height: 914rpx;
  border-radius: 20rpx;
  width: 100%;
  position: relative;
  background: url("@/static/img/bg-003.png") no-repeat;
  background-size: 100% 100%;
  .title {
    font-size: 40rpx;
    color: $white;
    font-weight: bold;
    padding-bottom: 32rpx;
    padding-top: 48rpx;
  }
  .message_info_content {
    position: relative;
    z-index: 2;
    border-radius: 20rpx;
    margin: 0 40rpx;
    color: $white;
  }
  .button {
    margin-top: 32rpx;
  }
  .rich {
    height: 100%;
    width: 100%;
    display: block;
    overflow-y: auto;
  }
  .img1 {
    position: absolute;
    top: -80rpx;
    width: 206rpx;
    left: 0;
  }
  .img2 {
    position: absolute;
    top: -200rpx;
    width: 330rpx;
    right: 30rpx;
  }
}
.flex {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
}
</style>
