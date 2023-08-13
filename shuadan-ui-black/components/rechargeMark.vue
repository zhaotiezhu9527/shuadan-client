<template>
  <view>
    <u-modal
      :show="show"
      width="95vw"
      :showConfirmButton="false"
      :showCancelButton="false"
    >
      <view class="flex2">
        <view class="message_block">
          <view class="message_info_content">
            <image
              class="image"
              src="@/static/img/unionpay.png"
              mode="widthFix"
            />
            <view class="box"> 充值请咨询在线客服 </view>
            <view class="btn" @click="submit">立即咨询</view>
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
  </view>
</template>

<script>
export default {
  data() {
    return {
      show: false,
    };
  },
  methods: {
    open(e) {
      this.show = true;
    },
    submit() {
      this.show = false;
      this.$emit("ok");
      uni.navigateTo({
        url: "/pages/onlineService",
      });
    },
  },
};
</script>

<style scoped lang="scss">
/deep/ .u-popup__content {
  background: transparent;
}
/deep/ .u-modal__content {
  padding: 0 20rpx;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  box-shadow: 0 0 16rpx 0 #ffb400b2;
  border-radius: 40rpx;
  width: 100%;
  background-color: #1e1e1e;
  position: relative;
  border: 1px solid;
  border-image-source: conic-gradient(
    from 180deg at 50% 50%,
    rgba(245, 211, 172, 0) 0deg,
    rgba(245, 211, 172, 0.38) 45deg,
    rgba(245, 211, 172, 0) 84.38deg,
    rgba(245, 211, 172, 0) 133.12deg,
    rgba(245, 211, 172, 0.37) 187.5deg,
    rgba(245, 211, 172, 0) 230.62deg,
    rgba(245, 211, 172, 0) 360deg
  );
  .message_info_content {
    border-radius: 20rpx;
    position: relative;
    padding: 32rpx 48rpx 64rpx;
    z-index: 2;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    .image {
      width: 96rpx;
    }
    .box {
      padding: 32rpx 0;
      color: $white;
      font-size: 32rpx;
    }
    .btn {
      padding: 14rpx 32rpx;
      border-radius: 100rpx;
      color: $white;
      border: 1rpx solid #ffffffa6;
    }
  }
}
.flex2 {
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
