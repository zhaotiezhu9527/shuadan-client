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
              src="@/static/img/usdt.png"
              mode="widthFix"
            />
            <view class="box"> {{ $t("input_consult") }} </view>
            <view class="btn" @click="submit">{{ $t("consult") }}</view>
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
      config: {},
    };
  },
  methods: {
    open(e) {
      this.show = true;
      this.getConfig();
    },
    submit() {
      this.show = false;
      this.$emit("ok");
      // 客服修改
      if(window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))
      {
        uni.navigateTo({
          url: "/pages/onlineService"
        });
      }else{
        // #ifdef APP-PLUS
        plus.runtime.openURL(this.config.onlineService);
        // #endif
        // #ifdef H5
        window.open(this.config.onlineService);
        // #endif
      }
    },
    getConfig(){
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
        } 
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
  background-color: #fff;
  position: relative;
  border: 1px solid;
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
      color: #343434;
      font-size: 32rpx;
    }
    .btn {
      padding: 14rpx 32rpx;
      border-radius: 100rpx;
      color: #fff;
      background-color: #7d7d7d;
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
