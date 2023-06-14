<template>
  <view class="main">
    <view class="price">
      <view class="price-box">
        <view class="title">我的总资产(元)</view>
        <view class="money">
          <view class="txt">{{ infos.balance }}</view>
          <view class="btn">
            <view @click="goDeposit('/pages/deposit')">提现</view>
            <view @click="goDeposit('/pages/recharge')">充值</view>
          </view>
        </view>
        <view class="ulStyle">
          <view class="ul">
            <view class="li">
              <view class="name">{{ infos.yesterdayIncome }}</view>
              <view class="txt">昨日收益(元)</view>
            </view>
            <view class="li">
              <view class="name">{{ infos.totalIncome }}</view>
              <view class="txt">累计收益(元)</view>
            </view>
            <view class="li">
              <view class="name">{{ infos.todayIncome }}</view>
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
      <marquee class="marquee">
        <div v-html="homeNotice"></div>
      </marquee>
      <view class="txt">了解</view>
    </view>
    <view class="task">任务大厅</view>
    <view class="list">
      <view
        class="item"
        v-for="(item, index) in list"
        :key="index"
        @click="
          routechange2(item.areaId, '/pages/index?tabs=2&level=' + item.areaId)
        "
      >
        <image :src="item.levelImg" class="icon" mode="widthFix" />
        <image src="@/static/img/icon07.png" class="bg" mode="widthFix" />
        <view class="content">
          <view>{{ item.areaName }}</view>
          <view>{{ item.remark }}</view>
        </view>
        <image :src="item.areaImg" class="img" mode="heightFix" />
        <view class="no" v-if="item.areaId > level">
          <image class="img" src="@/static/img/suo.png" mode="widthFix" />
          <view class="txt">待解锁</view>
        </view>
      </view>
    </view>
    <view class="rich">
      <image
        @click="change(item.en)"
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

export default {
  data() {
    return {
      nav: [
        { img: img13, name: "平台简介", en: "ptjj" },
        { img: img14, name: "规则说明", en: "gzsm" },
        { img: img15, name: "代理合作", en: "dlhz" },
        { img: img16, name: "公司资质", en: "gszz" },
      ],
      list: [],
      items: {},
      infos: {},
      level: 1,
      homeNotice: "",
      bindStatus: false, //银行卡绑定状态
    };
  },
  methods: {
    async open() {
      await this.$onLaunched;
      this.homeNotice = uni.getStorageSync("config").homeNotice;
      this.getInfo();
      this.$api.area_list().then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    change(en) {
      uni.navigateTo({
        url: `/pages/richtext?en=${en}`,
      });
    },
    routechange2(index, url) {
      if (index > this.level) return false;
      uni.reLaunch({
        url,
      });
    },
    routechange(url) {
      uni.navigateTo({
        url,
      });
    },
    goDeposit(url) {
      if (this.bindStatus) {
        uni.navigateTo({
          url,
        });
      } else {
        this.$base.show("请先绑定银行卡");
      }
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.items = res.data.data;
          this.level =
            this.list.find((item) => item.remark.includes(this.items.levelName))
              ?.level || 1;
          if (this.items.bankNo === null || !this.items.bankNo) {
            this.bindStatus = false;
          } else {
            this.bindStatus = true;
          }
        }
      });
      // 用户收益详情
      this.$api.user_income_detail().then(({ data }) => {
        if (data.code == 0) {
          this.infos = data.data;
        } else {
          this.$base.show(data.msg);
        }
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
  flex-wrap: wrap;
  gap: 40rpx;
  .no {
    position: absolute;
    z-index: 10;
    bottom: 30rpx;
    left: 20rpx;
    width: calc(100% - 40rpx);
    height: 140rpx;
    background-color: rgba(#000, 0.5);
    border-radius: 10rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    .img {
      width: 40rpx;
    }
    .txt {
      color: #fff;
      font-size: 24rpx;
      padding-top: 10rpx;
    }
  }
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
      z-index: 10;
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
    max-height: 140rpx !important;
    position: relative;
  }
}
.notice {
  position: relative;
  border: 2rpx solid #eee;
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
.marquee {
  display: flex;
  align-items: center;
  height: 84rpx;
}
</style>
