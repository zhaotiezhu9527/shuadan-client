<template>
  <u-modal
    :show="show"
    width="90vw"
    :showConfirmButton="false"
    :showCancelButton="false"
  >
    <view class="flex">
      <view class="message_block">
        <view class="message_info_content">
          <rich-text class="rich" :nodes="content"></rich-text>
        </view>
      </view>
      <u-icon
        @click="show = false"
        class="close"
        size="70rpx"
        color="#fff"
        name="close-circle"
      ></u-icon>
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
}
/deep/ .u-modal__content {
  padding: 0;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  height: 65vh;
  border-radius: 20rpx;
  width: 100%;
  position: relative;
  background: url("@/static/img/message_bg.png") no-repeat;
  background-size: 100% 100%;
  .message_info_content {
    position: relative;
    z-index: 2;
    background-color: #fff;
    border-radius: 20rpx;
    height: calc(100% - 196rpx);
    margin: 150rpx 40rpx 40rpx;
    padding: 20rpx;
  }
  .rich {
    height: 100%;
    width: 100%;
    display: block;
    overflow-y: auto;
  }
}
.flex {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
}
.close {
  margin-top: 80rpx;
}
</style>
