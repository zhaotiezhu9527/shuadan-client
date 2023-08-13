<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="充值"
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
      <view class="title">充值金额</view>
      <u-input
        placeholder="请输入充值金额"
        border="bottom"
        placeholderClass="placeholder"
        v-model="value"
        clearable
        type="number"
      >
        <template #prefix>
          <view class="rmb">￥</view>
        </template>
      </u-input>
      <view class="list">
        <view
          class="item"
          @click="value = item"
          v-for="(item, index) in list"
          :key="index"
        >
          {{ item }}元
        </view>
      </view>
    </view>
    <view class="btn" v-if="!active">
      <u-button
        type="primary"
        @click="change"
        class="button"
        text="下一步"
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
    };
  },
  methods: {
    change() {
      let num = /^\+?[1-9]\d*$/;
      if (!num.test(this.value)) {
        this.$base.show("请输入整数");
      } else {
        // this.active = 1;
        this.$refs.rechargeMarkRef.open();
      }
    },
    submit() {
      uni.navigateTo({
        url: "/pages/onlineService",
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
      height: 100rpx !important;
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
    height: 60rpx !important;
    display: flex;
    align-items: flex-end;
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
