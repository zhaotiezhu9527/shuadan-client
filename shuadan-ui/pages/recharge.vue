<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('recharge')"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="contentStyle">
      <view class="content" v-if="!active">
        <view class="title">{{$t('rechargeAmount') }}</view>
        <u-input
          :placeholder="$t('enterMoney')"
          border="bottom"
          v-model="value"
          clearable
          type="number"
        >
          <template #prefix>
            <!-- <view class="rmb">￥</view> -->
          </template>
        </u-input>
        <view class="txt">{{$t('rechargeIllustrate') }}</view>
        <view class="list">
          <view
            class="item"
            @click="value = item"
            v-for="(item, index) in list"
            :key="index"
          >
            {{ item }}
            <!-- 元 -->
          </view>
        </view>
      </view>
      <view class="content" v-else @click="submit">
        <view class="title">{{$t('selectPaymentChannel') }}</view>
        <view class="link">
          <view class="flex">
            <image
              class="image"
              src="@/static/img/unionpay.jpg"
              mode="widthFix"
            />
            <view class="box">
              <view>{{$t('rechargeService') }}</view>
              <view>{{$t('rechargeService') }}</view>
            </view>
          </view>
          <u-icon name="arrow-right" color="#cacaca" size="40rpx"></u-icon>
        </view>
      </view>
    </view>
    <view class="btn" v-if="!active">
      <u-button
        type="primary"
        @click="change"
        color="#2f3848"
        text="下一步"
      ></u-button>
    </view>
  </view>
</template>

<script>
export default {
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
        this.$base.show(this.$t('enterInteger'));
      } else {
        this.active = 1;
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
  min-height: 100vh;
  background-color: #f2f2f2;
  .contentStyle {
    padding: 20rpx 30rpx;
    .content {
      padding: 30rpx;
      border-radius: 5rpx;
      box-shadow: 0 0 10rpx 0 rgba(0, 0, 0, 0.15);
      background-color: #fff;
    }
    .title {
      font-size: 28rpx;
    }
    .txt {
      padding-top: 10rpx;
      color: #999;
      font-size: 24rpx;
    }
  }
  .u-input {
    padding: 20rpx 18rpx !important;
    /deep/.uni-input-input,
    /deep/.uni-input-placeholder {
      font-size: 38rpx;
      color: #666;
    }
  }
  .rmb {
    color: #d91d37;
    font-size: 24rpx;
  }
  .list {
    padding-top: 20rpx;
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-wrap: wrap;
    gap: 20rpx;
    .item {
      border: 1px solid #bbb;
      font-size: 24rpx;
      padding: 15rpx 0;
      width: 130rpx;
      color: #d91d37;
      text-align: center;
    }
  }
  .btn {
    padding: 20rpx 30rpx 0;
    /deep/.u-button__text {
      color: #fff;
    }
  }
  .link {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx 0;
    .image {
      width: 78rpx;
      margin-right: 25rpx;
    }
    .flex {
      display: flex;
      align-items: center;
    }
  }
  .box {
    view:nth-child(1) {
      font-size: 28rpx;
    }
    view:nth-child(2) {
      padding-top: 20rpx;
      font-size: 24rpx;
      color: #777777;
    }
  }
}
</style>
