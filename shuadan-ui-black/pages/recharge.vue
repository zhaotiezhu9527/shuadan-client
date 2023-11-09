<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('recharge')"
      fixed
      leftIconColor="#ffffff"
      bgColor="#1E1E1E"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
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
        <!-- <template #prefix>
          <view class="rmb">{{ $t("rmb_icon") }}</view>
        </template> -->
      </u-input>
      <view class="list">
        <view
          class="item"
          @click="value = item"
          v-for="(item, index) in list"
          :key="index"
        >
          {{ item }}
          <!-- {{ $t("rmb") }} -->
        </view>
      </view>
    </view>
    <view class="btn" v-if="!active">
      <u-button
        type="primary"
        @click="change"
        class="button"
        :text="$t('next_step')"
      ></u-button>
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
        // this.active = 1;
        this.$refs.rechargeMarkRef.open();
      }
    },
    submit() {
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
    background-color: #1e1e1e;
    .title {
      font-size: 32rpx;
      color: #ffffffd9;
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
        color: #ffffffd9;
      }
      view:nth-child(2) {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #ffffffa6;
      }
    }
  }
  .u-input {
    padding: 20rpx 0rpx 44rpx !important;
    /deep/.uni-input-input {
      height: 80rpx !important;
      font-size: 64rpx;
      color: $white;
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
    color: #ffffffd9;
  }
  .placeholder {
    font-size: 64rpx;
    font-weight: 500;
    color: #ffffff4d;
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
    .item {
      border: 1rpx solid #ffffff4d;
      font-size: 28rpx;
      padding: 15rpx 0;
      width: calc(25% - 16rpx);
      color: #ffffffd9;
      text-align: center;
    }
  }
  .btn {
    padding: 48rpx 42rpx 0;
    .button {
      border-radius: 100rpx;
    }
  }
}
</style>
