<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('recharge')"
      fixed
      leftIconColor="#000"
      leftIconSize="32"
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="content" v-if="!active">
      <view class="title">{{ $t("recharge_acc") }}</view>
      <u-input
        placeholder="USDT"
        border="bottom"
        placeholderClass="placeholder"
        v-model="value"
        clearable
        type="number"
        >
      </u-input>
      <view class="list">
        <view
          class="item"
          @click="value = item"
          v-for="(item, index) in list"
          :key="index"
        >
          {{ item }}
        </view>
      </view>
    </view>
    <view
      class="recharge-button"
      @click="change"
      v-if="!active"
      >
      {{ $t("next_step") }}
    </view>
    <rechargeMark ref="rechargeMarkRef" @ok="value = ''" />
  </view>
</template>

<script>
import rechargeMark from "../components/rechargeMark.vue";
export default {
  components: {
    rechargeMark,
  },
  data() {
    return {
      list: [100, 300, 500, 1000, 3000, 5000, 10000, 50000],
      active: 0,
      value: "",
      config: {},
    };
  },
  onShow() {
    this.getConfig();
  },
  methods: {
    change() {
      let num = /^\+?[1-9]\d*$/;
      if (!num.test(this.value)) {
        this.$base.show(this.$t("input_integer"));
      } else {
        this.$refs.rechargeMarkRef.open();
      }
    },
    submit() {
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
.main {
  width: 100%;
  .content {
    padding: 24rpx 32rpx;
    background-color: #fff;
    color: #999;
    .title {
      font-size: 32rpx;
      padding-bottom: 24rpx;
    }
    .link {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx 0;
      .image {
        width: 96rpx;
        margin-right: 16rpx;
      }
      .flex {
        display: flex;
        align-items: center;
      }
    }
    .box {
      view:nth-child(1) {
        font-size: 32rpx;
      }
      view:nth-child(2) {
        padding-top: 10rpx;
        font-size: 24rpx;
      }
    }
  }
  .u-input {
    padding: 20rpx 0rpx 20rpx !important;
    /deep/.uni-input-input {
      height: 80rpx !important;
      font-size: 64rpx;
      color: #999;
    }
    /deep/.u-input__content__field-wrapper__field {
      height: 80rpx !important;
    }
    &.u-border-bottom {
      border-color: rgba($white, 0.12) !important;
    }
  }
  .rmb {
    font-size: 48rpx;
  }
  .placeholder {
    font-size: 64rpx;
    font-weight: 500;
    height: 80rpx !important;
    display: flex;
    align-items: center;
  }
  .list {
    padding: 24rpx 0 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16rpx;
    border-top: 1px solid #999;
    .item {
      border: 1rpx solid #ddd;
      font-size: 28rpx;
      padding: 15rpx 0;
      width: calc(25% - 16rpx);
      text-align: center;
      border-radius: 10rpx;
    }
  }
  .btn {
    padding: 48rpx 42rpx 0;
    .button {
      border-radius: 100rpx;
    }
  }
}
.recharge-button{
  width: 92%;
  margin: auto;
  height: 104rpx;
  line-height: 104rpx;
  background-color: #7d7d7d;
  border-radius: 10rpx;
  text-align: center;
  color: #fff;
}
</style>
