<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="会员升级"
      fixed
      safe-area-inset-top
      bgColor="#ec6b00"
      leftIconColor="#fff"
      leftIconSize="32"
      height="50px"
      titleStyle="color:#fff;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="carstyle">
      <view class="car">
        <image class="user" src="@/static/img/head.png" mode="widthFix" />
        <view class="txt">
          <view>会员等级：{{ items.currLevelName }}</view>
          <view>每天可接单：{{ items.dayOrderCount }}单</view>
        </view>
        <view class="link" @click="change">会员详情</view>
      </view>
    </view>
    <view class="list">
      <view class="item">
        <image class="img" src="@/static/img/v1.png" mode="widthFix" />
        <view class="txt">佣金加成</view>
      </view>
      <view class="item">
        <image class="img" src="@/static/img/v2.png" mode="widthFix" />
        <view class="txt">任务增多</view>
      </view>
      <view class="item">
        <image class="img" src="@/static/img/v3.png" mode="widthFix" />
        <view class="txt">专属客服</view>
      </view>
    </view>
    <view class="more">左右滑动查看更多</view>
    <u-scroll-list>
      <view class="row">
        <view class="item" v-for="(item, index) in items.levels" :key="index">
          <view class="title">{{ item.levelName }}</view>
          <view class="money">{{ item.levelPrice }}元</view>
          <view class="txt">提现次数:{{ item.dayWithdrawCount }}/天</view>
          <view class="txt">提现额度:{{ item.maxWithdrawAmount }}</view>
          <view class="txt">接单数量:{{ item.dayOrderCount }}/天</view>
          <view class="txt">佣金比例:{{ item.commissionRate }}</view>
          <view class="txt">会员永久有效</view>
        </view>
      </view>
    </u-scroll-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      items: {},
    };
  },
  onShow() {
    this.$api.user_vipiv_info().then(({ data }) => {
      if (data.code == 0) {
        this.items = data.data;
      } else {
        this.$base.show(data, msg);
      }
    });
  },
  methods: {
    change() {
      uni.reLaunch({ url: "/pages/index?tabs=4" });
    },
  },
};
</script>

<style scoped lang="scss">
.carstyle {
  width: 100%;
  height: 240rpx;
  padding: 30rpx 30rpx 0;
  background: -webkit-linear-gradient(left, #f58020, #f99c4f);
  overflow: hidden;
  .car {
    background: url(/static/img/vip_bg.png) no-repeat;
    background-size: 100% 100%;
    height: 220rpx;
    border-top-left-radius: 20rpx;
    border-top-right-radius: 20rpx;
    display: flex;
    align-items: center;
    position: relative;
    .user {
      width: 120rpx;
      height: 120rpx;
      margin-left: 30rpx;
    }
    .txt {
      margin-left: 20rpx;
      view {
        color: #fff;
      }
      view:nth-child(1) {
        font-size: 36rpx;
        font-weight: bold;
        padding-bottom: 20rpx;
      }
      view:nth-child(2) {
        font-size: 28rpx;
      }
    }
    .link {
      background: #cfa55d;
      height: 60rpx;
      display: inline-block;
      width: 176rpx;
      border-radius: 30rpx 0 0 30rpx;
      color: #997030;
      font-size: 22rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
    }
  }
}
.list {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 80rpx;
  padding-top: 40rpx;
  .item {
    text-align: center;
    &:nth-child(1) .txt {
      color: #febb35;
    }
    &:nth-child(2) .txt {
      color: #e65a69;
    }
    &:nth-child(3) .txt {
      color: #33cdf8;
    }
  }

  .img {
    width: 120rpx;
  }
  .txt {
    font-size: 24rpx;
  }
}
.more {
  text-align: center;
  color: #d4d2d2;
  font-size: 24rpx;
  padding-top: 60rpx;
}
.row {
  display: flex;
  flex-wrap: nowrap;
  gap: 20rpx;
  padding: 10rpx 30rpx;
  .item {
    border: 2rpx #f5f5f5 solid;
    border-radius: 10rpx;
    width: 240rpx;
    text-align: center;
    padding: 20rpx 0;
  }
  view {
    line-height: 1.4;
  }
  .title {
    color: #a29d9d;
    font-size: 32rpx;
  }
  .money {
    color: #228aff;
    font-size: 24rpx;
  }
  .txt {
    font-size: 24rpx;
  }
}
/deep/.u-scroll-list__indicator {
  display: none;
}
</style>
