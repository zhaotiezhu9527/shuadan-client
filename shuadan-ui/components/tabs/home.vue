<template>
  <view class="main">
    <view class="price">
      <view class="price-box">
        <view class="title">我的总资产(元)</view>
        <view class="money">
          <view class="txt">2520.55</view>
          <view class="btn">
            <view @click="routechange('/pages/deposit')">提现</view>
            <view @click="routechange('/pages/recharge')">充值</view>
          </view>
        </view>
        <view class="ulStyle">
          <view class="ul">
            <view class="li">
              <view class="name">3316.91</view>
              <view class="txt">昨日收益(元)</view>
            </view>
            <view class="li">
              <view class="name">3316.91</view>
              <view class="txt">累计收益(元)</view>
            </view>
            <view class="li">
              <view class="name">3316.91</view>
              <view class="txt">今日收益(元)</view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="notice">
      <view class="image">
        <image class="img" src="@/static/img/icon12.png" mode="widthFix" />
      </view>
      <u-notice-bar
        bgColor="#fff"
        color="#c0392b"
        fontSize="38rpx"
        text="温馨提示：每天做任务时间09:00"
      ></u-notice-bar>
      <view class="txt">了解</view>
    </view>
    <view class="task">任务大厅</view>
    <view class="list">
      <view
        class="item"
        v-for="(item, index) in list"
        :key="index"
        @click="routechange2('/pages/task?tabs=2&infos=' + item)"
      >
        <image :src="item.icon" class="icon" mode="widthFix" />
        <image src="@/static/img/icon07.png" class="bg" mode="widthFix" />
        <view class="content">
          <view>{{ item.name }}</view>
          <view>{{ item.content }}</view>
        </view>
        <image :src="item.img" class="img" mode="widthFix" />
      </view>
    </view>
    <view class="rich">
      <image
        @click="change(item.name)"
        mode="widthFix"
        v-for="(item, index) in nav"
        :key="index"
        :src="item.img"
      />
    </view>
    <annunciate />
  </view>
</template>

<script>
import annunciate from "@/components/annunciate";
import img13 from "@/static/img/icon13.png";
import img14 from "@/static/img/icon14.png";
import img15 from "@/static/img/icon15.png";
import img16 from "@/static/img/icon16.png";

import img03 from "@/static/img/icon03.png";
import img04 from "@/static/img/icon04.png";
import img05 from "@/static/img/icon05.png";
import img06 from "@/static/img/icon06.png";
import img08 from "@/static/img/icon08.png";
import img09 from "@/static/img/icon09.png";
import img10 from "@/static/img/icon10.png";
import img11 from "@/static/img/icon11.png";
export default {
  data() {
    return {
      nav: [
        { img: img13, name: "平台简介" },
        { img: img14, name: "规则说明" },
        { img: img15, name: "代理合作" },
        { img: img16, name: "公司资质" },
      ],
      list: [
        {
          name: "拼多多",
          content: "白银会员专属通道",
          img: img08,
          icon: img03,
        },
        { name: "淘宝", content: "黄金会员专属通道", img: img09, icon: img04 },
        { name: "天猫", content: "铂金会员专属通道", img: img10, icon: img05 },
        { name: "京东", content: "钻石会员专属通道", img: img11, icon: img06 },
      ],
    };
  },
  methods: {
    open(e) {
      console.log(e);
    },
    change(title) {
      uni.navigateTo({
        url: `/pages/richtext?title=${title}`,
      });
    },
    routechange2(url) {
      uni.reLaunch({
        url,
      });
    },
    routechange(url) {
      uni.navigateTo({
        url,
      });
    },
  },
  components: {
    annunciate,
  },
};
</script>

<style scoped lang="scss">
.main {
  padding-top: calc(var(--status-bar-height) + 40rpx);
  .price {
    background: url("@/static/img/info_bg.png");
    background-size: 100%;
    height: 368rpx;
    width: auto;
    position: relative;
    padding: 0 30rpx;
  }
}
.price-box {
  padding: 40rpx 20rpx;
  .title {
    color: #434343;
    font-size: 28rpx;
  }
  .money {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .txt {
      font-weight: 600;
      font-size: 58rpx;
    }
    .btn {
      display: flex;
      view {
        width: 100rpx;
        padding: 6rpx 0;
        text-align: center;
        background: #a2b0c8;
        border-radius: 25rpx;
        color: #fff;
        font-weight: 600;
        margin-left: 20rpx;
        font-size: 26rpx;
      }
    }
  }
  .ulStyle {
    padding-top: 40rpx;
  }
}
.ul {
  padding-top: 5rpx;
  display: flex;
  text-align: center;
  border-top: 1rpx solid rgba(#434343, 0.1);
  .li {
    width: calc(100% / 3);
    border-right: 1rpx solid rgba(#434343, 0.1);
    &:nth-child(3) {
      border: 0;
    }
  }
  .name {
    color: #434343;
    font-weight: 600;
    font-size: 32rpx;
  }
  .txt {
    color: #434343;
    opacity: 0.7;
    font-size: 24rpx;
    padding-top: 4rpx;
  }
}
.rich {
  padding: 40rpx 30rpx;
  width: 100%;
  gap: 8rpx;
  display: flex;
  justify-content: space-between;
  padding-bottom: calc(160rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
}
.task {
  font-weight: 600;
  color: #434343;
  font-size: 36rpx;
  padding: 50rpx 30rpx 50rpx;
}
.list {
  display: flex;
  padding: 0 30rpx;
  justify-content: center;
  flex-wrap: wrap;
  gap: 40rpx;
  .item {
    width: calc(50% - 22rpx);
    padding: 20rpx;

    border-radius: 10rpx;
    background: #f2f3f399;
    position: relative;
    .bg {
      position: absolute;
      top: 0;
      right: 0;
      z-index: 1;
      width: 210rpx;
    }
    .icon {
      width: 96rpx;
      position: absolute;
      left: 20rpx;
      top: -20rpx;
      z-index: 2;
    }
  }
  .content {
    z-index: 2;
    position: relative;
    view:nth-child(1) {
      color: #434343;
      font-weight: 600;
      font-size: 26rpx;
      padding-top: 4rpx;
    }
    view:nth-child(2) {
      color: #969696;
      font-size: 24rpx;
      padding: 4rpx 0 8rpx;
    }
  }
  .img {
    width: 100%;
    z-index: 2;
    position: relative;
  }
}
.notice {
  position: relative;
  /deep/.u-notice-bar {
    border: 2rpx solid #eee;
    border-left: 0;
    border-right: 0;
    font-weight: 600;
    position: relative;
    z-index: 1;
  }
  .image {
    background-color: #fff;
    position: absolute;
    left: 0;
    top: 50%;
    z-index: 2;
    transform: translateY(-50%);
    padding: 0 40rpx 0 20rpx;
    .img {
      width: 80rpx;
    }
  }
  .txt {
    position: absolute;
    z-index: 2;
    padding: 0 20rpx 0 40rpx;
    top: 50%;
    right: 0;
    height: 86rpx;
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    transform: translateY(-50%);
    color: #ffa94b;
    font-size: 24rpx;
    &::before {
      content: "";
      height: 40rpx;
      left: 20rpx;
      top: 50%;
      transform: translateY(-50%);
      position: absolute;
      background-color: #e2e2e2;
      width: 2rpx;
    }
  }
}
</style>
