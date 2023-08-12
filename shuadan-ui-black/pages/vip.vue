<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="会员升级"
      fixed
      safe-area-inset-top
      bgColor="#1e1e1e"
      leftIconColor="#fff"
      leftIconSize="32"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="carstyle">
      <view class="car">
        <image class="user" :src="infos.avatarUrl" mode="widthFix" />
        <view class="txt">
          <view>会员等级：{{ items.currLevelName }}</view>
          <view>每天可接单：{{ items.dayOrderCount }}单</view>
        </view>
        <view class="link" @click="change">会员详情</view>
      </view>
    </view>
    <view class="list">
      <view class="item">
        <image class="img" src="@/static/img/bg-009.png" mode="widthFix" />
        <view class="txt">佣金加成</view>
      </view>
      <view class="item">
        <image class="img" src="@/static/img/bg-010.png" mode="widthFix" />
        <view class="txt">任务增多</view>
      </view>
      <view class="item">
        <image class="img" src="@/static/img/bg-011.png" mode="widthFix" />
        <view class="txt">专属客服</view>
      </view>
    </view>
    <view class="row">
      <view class="item" v-for="(item, index) in items.levels" :key="index">
        <view class="header">
          <view class="title"
            >{{ item.levelName }}
            <text class="tips">永久</text>
          </view>
          <view class="money">{{ item.levelPrice }}元</view>
        </view>
        <view class="view">
          <view class="txt">
            <text>提现次数:</text>
            <text>{{ item.dayWithdrawCount }}/天</text>
          </view>
          <view class="txt">
            <text>提现额度:</text>
            <text>{{ item.maxWithdrawAmount }}/天</text>
          </view>
          <view class="txt">
            <text>接单数量:</text>
            <text>{{ item.dayOrderCount }}/天</text>
          </view>
          <view class="txt">
            <text>佣金比例:</text>
            <text>{{ item.commissionRate }}/天</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      items: {},
      infos: {},
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
    this.getInfo();
  },
  methods: {
    change() {
      uni.reLaunch({ url: "/pages/index?tabs=4" });
    },
    getInfo() {
      uni.showLoading({
        title: "加载中",
      });
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.infos = res.data.data;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.carstyle {
  width: 100%;
  height: 240rpx;
  padding: 30rpx 30rpx 0;
  overflow: hidden;
  background: url("/static/img/bg-008.png") no-repeat;
  background-size: 100% 100%;
  background-position: 0px 100rpx;
  .car {
    background: url(/static/img/vip_bg.png) no-repeat;
    background-size: 100% 100%;
    height: 250rpx;
    border-top-left-radius: 20rpx;
    border-top-right-radius: 20rpx;
    display: flex;
    align-items: center;
    position: relative;
    z-index: 1;
    .user {
      width: 80rpx;
      height: 80rpx;
      margin-left: 30rpx;
      margin-top: -30rpx;
    }
    .txt {
      margin-top: -30rpx;
      margin-left: 20rpx;
      view {
        color: $white;
      }
      view:nth-child(1) {
        font-size: 32rpx;
        font-weight: bold;
        padding-bottom: 20rpx;
      }
      view:nth-child(2) {
        font-size: 24rpx;
      }
    }
    .link {
      height: 48rpx;
      background: #252525;
      display: inline-block;
      width: 144rpx;
      border-radius: 8rpx;
      color: $white;
      font-size: 22rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      position: absolute;
      right: 30rpx;
      top: 50%;
      transform: translateY(calc(-50% - 15rpx));
    }
  }
}
.list {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 40rpx 0;
  background-color: #1e1e1e;
  margin-bottom: 32rpx;
  position: relative;
  z-index: 2;
  .item {
    text-align: center;
  }

  .img {
    width: 40rpx;
  }
  .txt {
    padding-top: 12rpx;
    font-size: 24rpx;
    color: $white;
  }
}
.row {
  gap: 20rpx;
  padding: 10rpx 30rpx;
  .item {
    border-radius: 32rpx;
    text-align: center;
    width: 100%;
    margin-bottom: 16rpx;
    padding: 32rpx 24rpx;
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

    background: linear-gradient(134.61deg, #333333 0.81%, #1e1e1e 97.27%),
      conic-gradient(
        from 180deg at 50% 50%,
        rgba(245, 211, 172, 0) 0deg,
        rgba(245, 211, 172, 0.38) 45deg,
        rgba(245, 211, 172, 0) 84.38deg,
        rgba(245, 211, 172, 0) 133.12deg,
        rgba(245, 211, 172, 0.37) 187.5deg,
        rgba(245, 211, 172, 0) 230.62deg,
        rgba(245, 211, 172, 0) 360deg
      );
  }
  .view {
    border-top: 1rpx solid #ffffff1f;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
  }
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx 0;
    .title {
      color: #e3e6f3;
      font-size: 28rpx;
      font-weight: 600;
      display: flex;
      align-items: center;
    }
    .money {
      font-weight: 600;
      color: $white;
      font-size: 28rpx;
    }
    .tips {
      padding: 0 8rpx;
      border-radius: 8rpx;
      color: #121212;
      display: flex;
      align-items: center;
      font-weight: 400;
      justify-content: center;
      margin-left: 10rpx;
      font-size: 20rpx;
      background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
    }
  }
  .txt {
    color: #ffffffa6;
    padding-top: 32rpx;
    width: calc(50% - 16rpx);
    display: flex;
    justify-content: space-between;
    align-items: center;
    text {
      font-size: 24rpx;
      &:nth-child(2) {
        color: #ffffffd9;
      }
    }
  }
}
/deep/.u-scroll-list__indicator {
  display: none;
}
</style>
