<template>
  <view class="main">
    <u-list class="container">
      <view class="title">
        <view>任务记录</view>
        <view>2532.36</view>
      </view>
      <view class="sub_title">
        <view>本数据由全民任务官方提供</view>
        <view>剩余可用资产(元)</view>
      </view>
      <view class="tabs">
        <u-tabs
          :list="list"
          lineColor="#ff9a2c"
          lineWidth="60rpx"
          lineHeight="6rpx"
          :scrollable="false"
          :current="current"
          activeStyle="color:#ff9a2c;font-weight:600"
        ></u-tabs>
      </view>
      <view class="list">
        <view class="boxstyle" v-for="(item, index) in 10" :key="index">
          <view class="box">
            <view class="time">抢单时间：2023年06月05日 19:48:35</view>
            <view class="uid">抢单编号：UB2306051948357664<text>1</text></view>
            <view class="goodsstyle">
              <view class="goods">
                <image
                  class="img"
                  src="@/static/img/goods.jpg"
                  mode="widthFix"
                />
                <view class="content">
                  <view class="name">
                    汤河店
                    户外冲锋裤男女可脱卸秋冬季加绒加厚保暖软壳防风防水登山滑雪裤
                  </view>
                  <view class="text">
                    <view>¥ 179.00</view>
                    <view>x 11</view>
                  </view>
                </view>
              </view>
            </view>
            <view class="ul">
              <view class="li">
                <text>订单总额</text>
                <text>¥ 1969.00</text>
              </view>
              <view class="li">
                <text>佣金</text>
                <text>¥ 1969.00</text>
              </view>
              <view class="li">
                <text>预计返还</text>
                <text class="moeny">¥ 1969.00</text>
              </view>
              <view class="li">
                <view class="submit">提交订单</view>
              </view>
            </view>
            <image
              class="static"
              src="@/static/img/dongjie.png"
              mode="widthFix"
            />
          </view>
        </view>
      </view>
    </u-list>
    <success />
  </view>
</template>
<script>
import success from "@/components/success.vue";
export default {
  data() {
    return {
      current: 0,
      list: [
        { name: "全部" },
        { name: "待处理" },
        { name: "已完成" },
        { name: "冻结中" },
      ],
    };
  },
  components: { success },
};
</script>
<style lang="scss" scoped>
.main {
  padding-top: var(--status-bar-height);
  .container {
    height: calc(100vh - var(--status-bar-height)) !important;
    &::before {
      content: "";
      position: absolute;
      width: 100%;
      height: 20%;
      background-image: url("@/static/img/bg-01.png");
      background-size: 100%;
      top: 0;
      left: 0;
      z-index: 2;
    }
    &::after {
      content: "";
      position: absolute;
      width: 100%;
      height: 50%;
      background: linear-gradient(1turn, #fff, #bac3d2);
      top: 0;
      left: 0;
      z-index: 1;
    }
    .title {
      font-size: 46rpx;
      font-weight: 600;
      display: flex;
      justify-content: space-between;
      position: relative;
      z-index: 2;
      padding: 40rpx 30rpx;
      view {
        color: #2f3848;
      }
      view:nth-child(2) {
        font-size: 40rpx;
      }
    }
    .sub_title {
      font-size: 24rpx;
      display: flex;
      justify-content: space-between;
      position: relative;
      z-index: 2;
      padding: 20rpx 30rpx 0;
      view {
        color: #7a7d82;
      }
      view:nth-child(2) {
        color: #434343;
      }
    }
  }
  .tabs {
    padding-top: 30rpx;
    padding-bottom: 10rpx;
    border-bottom: 1rpx solid #fff;
    position: relative;
    z-index: 3;
  }
}
.boxstyle {
  padding: 30rpx;
  position: relative;
  z-index: 3;
  border-bottom: 6rpx solid #eee;
  &:nth-last-child(1) {
    border-bottom: 0;
  }
  .box {
    padding: 30rpx;
    background-image: url("@/static/img/bg_04.png");
    background-size: 100% 100%;
    position: relative;
    .uid,
    .time {
      color: #9e9e9e;
      font-size: 24rpx;
      padding-bottom: 10rpx;
      text {
        padding: 1px;
        border-radius: 50%;
        background-color: red;
        width: 40rpx;
        height: 40rpx;
        line-height: 40rpx;
        color: #fff;
        font-size: 28rpx;
        display: inline-block;
        text-align: center;
      }
    }
    .uid {
      padding-bottom: 0;
    }
  }
  .static {
    position: absolute;
    right: 20rpx;
    top: 20rpx;
    width: 130rpx;
  }
  .goodsstyle {
    margin: 20rpx 0;
  }
  .goods {
    background: #f2f2f2;
    border-radius: 20rpx;
    margin: 20rpx 0;
    display: flex;
    overflow: hidden;
    justify-content: space-between;
    align-items: center;
    .img {
      width: 180rpx;
    }
    .content {
      padding: 20rpx;
      display: flex;
      flex: 1;
      justify-content: space-between;
      flex-direction: column;
      font-size: 26rpx;
      line-height: 1.5;
    }
    .text {
      display: flex;
      padding-top: 20rpx;
      font-size: 26rpx;
      justify-content: space-between;
      align-items: center;
    }
  }
  .ul {
    .li {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 30rpx;
      &:nth-child(3) {
        padding-bottom: 10rpx;
      }
      &:nth-child(4) {
        padding-bottom: 10rpx;
      }
    }
    .submit {
      background: #ff575c;
      color: #fff;
      font-size: 20rpx;
      text-align: center;
      padding: 6rpx 20rpx;
      border-radius: 10rpx;
    }
    text {
      font-size: 28rpx;
    }
    .moeny {
      color: #ff9a2c;
      font-size: 45rpx;
    }
  }
}
.list {
  padding-bottom: calc(constant(safe-area-inset-bottom) + 160rpx);
  padding-bottom: calc(env(safe-area-inset-bottom) + 160rpx);
}
</style>
