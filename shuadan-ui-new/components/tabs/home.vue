<template>
  <view class="main">
    <view class="header">云任务</view>
    <view class="price">
      <view class="price-box">
        <view class="flex">
          <view class="money">
            <view class="title">我的总资产（元）</view>
            <view class="txt">{{ infos.balance }}</view>
          </view>
          <view class="btn">
            <view @click="goDeposit('/pages/recharge')"> 充值 </view>
            <view class="active" @click="goDeposit('/pages/deposit')">
              提现
            </view>
          </view>
        </view>
        <view class="ulStyle">
          <view class="ul">
            <view class="li">
              <view class="name">{{ infos.todayIncome }}</view>
              <view class="txt">今日收益（元）</view>
            </view>
            <view class="li">
              <view class="name">{{ infos.yesterdayIncome }}</view>
              <view class="txt">昨日收益（元）</view>
            </view>
            <view class="li">
              <view class="name">{{ infos.totalIncome }}</view>
              <view class="txt">累计收益（元）</view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="notice">
      <view class="tips">
        <view class="image">
          <image class="img" src="@/static/img/tips.png" mode="widthFix" />
        </view>
        <marquee class="marquee">
          <div v-html="homeNotice"></div>
        </marquee>
      </view>
    </view>
    <view class="header py">任务大厅</view>
    <view class="list">
      <view
        class="item"
        v-for="(item, index) in list"
        :key="index"
        @click="
          routechange2(item.unlock, '/pages/index?tabs=2&level=' + item.areaId)
        "
      >
        <view class="content">
          <image :src="item.levelImg" class="icon" mode="widthFix" />
          <!-- <view>{{ item.areaName }}</view> -->
          <view>{{ item.remark }}</view>
        </view>
        <image :src="item.areaImg" class="img" mode="heightFix" />
        <view class="no" v-if="!item.unlock">
          <image class="img" src="@/static/img/suo.png" mode="widthFix" />
          <view class="txt">待解锁</view>
        </view>
      </view>
    </view>
    <view class="rich">
      <!-- <image
        @click="change(item.en)"
        mode="widthFix"
        v-for="(item, index) in nav"
        :key="index"
        :src="item.img"
      /> -->
      <!-- <image src=""/> -->
      <view
        class="item"
        @click="change(item.en)"
        v-for="(item, index) in nav"
        :key="index"
      >
        <view class="con">
          <view class="name">{{ item.name }}</view>
          <view class="txt">{{ item.txt }}</view>
        </view>
      </view>
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
        { img: img13, name: "平台简介", en: "ptjj", txt: "introduce" },
        { img: img14, name: "规则说明", en: "gzsm", txt: "instructions" },
        { img: img15, name: "代理合作", en: "dlhz", txt: "cooperation" },
        { img: img16, name: "公司资质", en: "gszz", txt: "qualification" },
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
    routechange2(unlock, url) {
      if (!unlock) return false;
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
  padding-top: calc(var(--status-bar-height) + 10rpx);
  background: linear-gradient(
    180deg,
    rgba(254, 233, 218, 0.3) -4%,
    rgba(254, 233, 218, 0.3) 52%,
    rgba(255, 255, 255, 0) 130%
  );
  .header {
    text-align: center;
    font-size: 52rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 10rpx;
    &.py {
      padding: 30rpx 0 20rpx;
    }
  }
  .price {
    background: linear-gradient(242deg, #ffd86f 1%, #ffa23f 100%);
    width: 90%;
    margin: 0 auto;
    border-radius: 20rpx;
    position: relative;
    overflow: hidden;
  }
}
.price-box {
  padding: 40rpx 20rpx 30rpx;
  .flex {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .btn {
      display: flex;
      width: 280rpx;
      height: 86rpx;
      border-radius: 200rpx;
      background: #ffffff;
      color: #f78944;
      font-size: 36rpx;
      font-weight: 500;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-sizing: border-box;
      overflow: hidden;
      view {
        width: 50%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 200rpx;
      }
      .active {
        background: #ffd86f;
        color: #8a4e0d;
      }
    }
  }
  .money {
    .title {
      font-size: 36rpx;
      font-weight: normal;
      color: #ffffff;
    }
    .txt {
      font-weight: 600;
      font-size: 64rpx;
      padding-top: 20rpx;
      font-weight: bold;
      color: #ffffff;
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
  .li {
    width: calc(100% / 3);
    border-right: 1rpx solid rgba(#ff8080, 1);
    &:nth-child(3) {
      border: 0;
    }
  }
  .name {
    color: #fff;
    font-weight: 600;
    font-size: 48rpx;
  }
  .txt {
    color: #fff;
    text-align: center;
    font-size: 28rpx;
    padding-top: 4rpx;
  }
}
.rich {
  padding: 40rpx 30rp0x;
  width: 90%;
  gap: 8rpx;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding-bottom: calc(180rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(180rpx + env(safe-area-inset-bottom));
  .item {
    background-color: #fff;
    border-radius: 10rpx;
    width: calc(50% - 8rpx);
    padding: 20rpx;
  }
  .con {
    .name {
      padding-bottom: 10rpx;
    }
    .txt {
      color: #999;
      font-size: 24rpx;
    }
  }
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
    background: #fff;
    position: relative;
    .icon {
      width: 44rpx;
    }
  }
  .content {
    z-index: 2;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    padding-bottom: 10rpx;
    view {
      color: #434343;
      font-weight: 600;
      font-size: 28rpx;
      margin-left: 6rpx;
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
  width: 90%;
  margin: 4rpx auto 0;
  background: linear-gradient(
    180deg,
    rgba(231, 56, 56, 0.34) 0%,
    rgba(255, 162, 63, 0) 100%
  );
  padding-top: 28rpx;
  position: relative;
  .tips {
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 179, 37, 0.49);
    border-radius: 20rpx;
  }
  .image {
    padding: 0 20rpx;
    .img {
      width: 40rpx;
    }
  }
}
.marquee {
  display: flex;
  align-items: center;
  padding: 20rpx 0 20rpx 34rpx;
  font-size: 28rpx;
  font-weight: normal;
  line-height: 40rpx;
  color: #c58e49;
  opacity: 0.8;
}
</style>
