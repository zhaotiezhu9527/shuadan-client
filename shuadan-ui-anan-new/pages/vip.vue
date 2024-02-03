<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('vip')"
      fixed
      safe-area-inset-top
      leftIconColor="#000"
      leftIconSize="32"
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="carstyle">
        <view class="car">
          <view class="vip-title">{{ $t("vip") }}</view>
          <image class="user" :src="infos.avatarUrl" mode="widthFix" />
          <view class="txt">
            <view>{{ $t("vip") }}：{{ items.currLevelName }}</view>
          </view>
        </view>
      </view>
      <view class="row">
        <view class="item" v-for="(item, index) in items.levels" :key="index">
          <view class="header">
            
            <view>
              <image class="level-img" :src="item.levelImg" />
            </view>
            <view class="title"
              >
              <text class="tips">{{ $t("perpetual") }}</text>
            </view>
            <view class="money">
            </view>
          </view>
          <view class="view">
            <view class="text-item">
              · Receive a set of 
              {{ item.dayOrderCount}}
               apps data tasks
            </view>
            <view class="text-item">
              · Profit of {{ item.commissionRate}}% per application 
            </view>
            <view class="text-item">
              · Activate with a 
              {{ item.levelPrice}}
              USDT
            </view>
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
        title: this.$t("load_more"),
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
.main{
}
.carstyle {
  width: 100%;
  height: 240rpx;
  padding: 30rpx 30rpx 0;
  overflow: hidden;
  .car {
    background-color: #fff;
    height: 250rpx;
    border-top-left-radius: 20rpx;
    border-top-right-radius: 20rpx;
    z-index: 1;
    .vip-title{
      width: 100%;
      text-align: center;
      height: 40rpx;
      line-height: 40rpx;
      font-size: 28rpx;
      color: #4b4b4b;
    }
    .user {
      width: 80rpx;
      height: 80rpx;
      display: block;
      margin: 10rpx auto;
    }
    .txt {
      width: 100%;
      text-align: center;
      view {
        color: #4b4b4b;
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
    border-radius: 10rpx;
    text-align: center;
    width: 100%;
    margin-bottom: 16rpx;
    padding: 32rpx 24rpx;
    border: 1px solid #575651;
    background: #fff;
  }
  .view {
    border-top: 1rpx solid #ffffff1f;
    text-align: left;
    color: #4b4b4b;
    .text-item{
      margin-top:20rpx;
    }
  }
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 0 32rpx 0;
    border-bottom: 1px solid #eee;
    .level-img {
      float: left;
      color: #fff;
      height: 36rpx;
      width: 40rpx;
      margin: 0 16rpx;
    }
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
      flex: 1;
      text-align: right;
    }
    .tips {
      padding: 4rpx 8rpx;
      border-radius: 8rpx;
      color: #4b4b4b;
      display: flex;
      align-items: center;
      font-weight: 400;
      justify-content: center;
      margin-left: 10rpx;
      font-size: 28rpx;
      // background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
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
