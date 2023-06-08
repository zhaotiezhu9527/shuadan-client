<template>
  <u-modal
    :show="show"
    width="90vw"
    :showConfirmButton="false"
    :showCancelButton="false"
  >
    <view class="flex">
      <view class="message_block">
        <view class="message_info">
          <image
            mode="widthFix"
            class="left"
            src="@/static/img/notice_left.png"
          />
          <image
            mode="widthFix"
            class="center"
            src="@/static/img/notice_center.png"
          />
          <image
            mode="widthFix"
            class="right"
            src="@/static/img/notice_right.png"
          />
        </view>
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
      content: "测试内容",
    };
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
  height: 60vh;
  border-radius: 20rpx;
  width: 100%;
  background: linear-gradient(1turn, #fff, #ffcc2c);
  position: relative;
  &::after {
    content: "";
    top: 0;
    left: 0;
    background: url("@/static/img/bg_05.png") 0rpx no-repeat;
    background-size: 100%;
    position: absolute;
    height: 70%;
    width: 100%;
    z-index: 1;
  }
  .message_info {
    position: relative;
    z-index: 2;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 60rpx 0 40rpx;
    .left,
    .right {
      width: 68rpx;
      height: 56rpx;
    }
    .center {
      width: 340rpx;
      margin: 0 20rpx;
      height: 56rpx;
    }
  }
  .message_info_content {
    position: relative;
    z-index: 2;
    background-color: #fff;
    border-radius: 20rpx;
    height: calc(100% - 196rpx);
    margin: 0 40rpx 40rpx;
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
