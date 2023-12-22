<template>
  <view class="main">
    <u-navbar
      :border="false"
      :fixed="false"
      :title="$t('companyName')"
      leftIconSize="0"
      rightIconSize="32"
      bgColor="transparent"
      @rightClick="routechange('/pages/message')"
      height="50px"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
      <!-- <template #left>
        <lang inherit />
      </template> -->
      <template #right>
        <view>
          <image
            mode="widthFix"
            class="customer"
            src="@/static/img/customer.png"
          />
        </view>
      </template>
    </u-navbar>
    <u-swiper :list="swiper" height="400"></u-swiper>
    <view class="nav">
      <view
        class="item"
        @click="routechange(item.url)"
        v-for="(item, index) in nav"
        :key="index"
      >
        <image class="img" :src="item.img" mode="widthFix" />
        <text class="txt">{{ item.name }}</text>
      </view>
    </view>
    <view class="notice">
      <view class="black">
        <image class="image" src="@/static/img/100020.png" mode="widthFix" />
      </view>
      <marquee class="marquee">
        <div v-html="homeNotice"></div>
      </marquee>
    </view>
    <view class="sub_title">
      <view class="txt">{{ $t("vipLevel") }}</view>
    </view>
    <view class="list">
      <view
        class="item"
        v-for="(item, index) in list"
        :key="index"
        @click="
          routechange2(item.unlock, '/pages/index?tabs=2&level=' + item.areaId)
        "
      >
        <view class="flex">
          <image :src="item.levelImg" class="icon" mode="widthFix" />
          <view class="txt">
            <view>{{ item.areaName }}</view>
            <view>{{ item.remark }}</view>
          </view>
        </view>
        <view class="status">
          <image
            mode="widthFix"
            v-if="!item.unlock"
            src="@/static/img/10004.png"
          />
          <view v-else>{{ $t("unlocked") }}</view>
        </view>
      </view>
    </view>
    <view class="sub_title">
      <view class="txt white">{{ $t("revenueList") }}</view>
    </view>
    <view class="ranking">
      <view class="notice__inner">
        <view
          class="notice__box"
          :style="{ animation: `roll ${ranking.length / 2}s linear infinite` }"
        >
          <view
            class="notice__item"
            v-for="(item, index) in ranking"
            :key="index"
          >
            <view class="txt">
              <image
                class="img"
                src="@/static/img/100026.png"
                mode="widthFix"
              />
              <text>{{ item.phone }}</text>
            </view>
            <view class="txt">
              <text class="blue-text">{{ FormatAmount(item.price) }}</text>
            </view>
            <view class="time">{{ item.date }}</view>
          </view>
        </view>
      </view>
    </view>
    <view class="sub_title">
      <view class="txt white">{{ $t("partner") }}</view>
    </view>
    <view class="partners">
      <image src="@/static/img/shop1.png" />
      <image src="@/static/img/shop2.png" />
      <image src="@/static/img/shop3.jpg" />
      <image src="@/static/img/shop4.jpg" />
    </view>
  </view>
</template>

<script>
import img014 from "@/static/img/banner1.jpg";
import img015 from "@/static/img/100015.png";
import img017 from "@/static/img/100017.png";
import img018 from "@/static/img/100018.png";
import img019 from "@/static/img/100019.png";
import lang from "@/components/lang.vue";
import { dateFormat, FormatAmount } from "@/plugins/util";
export default {
  components: {
    lang,
  },
  props:['userData'],
  data() {
    return {
      FormatAmount,
      swiper: [img014],
      nav: [
        {
          name: this.$t("recharge"),
          img: img015,
          url: "/pages/recharge",
        },
        {
          name: this.$t("withdraw"),
          img: img017,
          url: "/pages/deposit",
        },
        {
          name: this.$t("invite"),
          img: img018,
          url: "/pages/promotion",
        },
        {
          name: this.$t("announcement"),
          img: img019,
          url: "/pages/message",
        },
      ],
      ranking: [
        // { name: "186****1590", money: Math.ceil(Math.random()*(300000-10000) + 10000), time: dateFormat("YYYY-mm-dd",new Date()) },
      ],
      list: [],
      homeNotice: "",
    };
  },
  methods: {
    open() {
      this.getConfig();
      this.getList();
      this.$api.area_list().then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    getConfig() {
      this.$api.system_config().then(({ data }) => {
        if (data.code == 0) {
          this.homeNotice = data.data.homeNotice;
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
      console.log(url)
      if(url === '/pages/deposit'){
        if(this.userData.bankNo || this.userData.walletAddr){
          uni.navigateTo({
            url,
          });
        }else{
          this.$base.show(this.$t("cardMsg"));
        }
      }else{
        uni.navigateTo({
          url,
        });
      }
    },
    //  获取滚动列表数据
    getList() {
      this.$api.system_bounslist().then((res) => {
        if (res.data.code == 0) {
          this.ranking = res.data.data;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  background: url("@/static/img/100016.jpg") no-repeat;
  background-size: cover;
  width: 100vw;
  min-height: 100vh;
  padding-top: calc(var(--status-bar-height));
  padding-bottom: calc(160rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
  .customer {
    width: 52rpx;
  }
  .nav {
    display: flex;
    padding: 20rpx 0;
    .item {
      width: 25%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
    .img {
      width: 108rpx;
    }
    .txt {
      font-size: 28rpx;
    }
  }
}
.notice {
  display: flex;
  margin: 0 30rpx;
  padding: 10rpx 20rpx;
  background-color: rgba(#fff, 0.5);
  border-radius: 8rpx;
  align-items: center;
  .black {
    align-items: center;
    display: flex;
    padding-right: 40rpx;
    position: relative;
    &::before {
      content: "";
      position: absolute;
      right: 20rpx;
      height: 100%;
      width: 1rpx;
      background-color: #afafaf;
    }
  }
  .image {
    width: 36rpx;
  }
}
.sub_title {
  padding: 30rpx;
  .txt {
    border-left: 6rpx solid #2e68f2;
    font-weight: 600;
    font-size: 36rpx;
    padding-left: 10rpx;
    &.white {
      color: #fff;
    }
  }
}
.list {
  .item {
    padding: 20rpx 30rpx;
    background-color: rgba(#fff, 0.5);
    margin: 0 30rpx 20rpx;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .flex {
      display: flex;
      align-items: center;
    }
    .icon {
      width: 110rpx;
    }
  }
  .txt {
    padding-left: 20rpx;
    view:nth-child(1) {
      color: #566a98;
      font-weight: 700;
      font-size: 30rpx;
    }
    view:nth-child(2) {
      font-size: 20rpx;
      padding-top: 6rpx;
    }
  }
  .status {
    width: 80rpx;
    color: #2e68f2;
    font-size: 26rpx;
    display: flex;
    justify-content: center;
    image {
      width: 36rpx;
    }
  }
}
.partners {
  padding: 10rpx 30rpx;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  image {
    width: 300rpx;
    height: 200rpx;
    margin: 10rpx 0;
    border-radius: 30rpx;
  }
}
.ranking {
  margin: 30rpx 30rpx 0;
  overflow: hidden;
  height: 345rpx;
  border-radius: 16rpx;
  background-color: rgba(#fff, 0.5);
  .notice__inner {
    width: 100%;
    height: 100%;
    overflow: hidden;
    font-size: 28rpx;
    padding: 0 30rpx;
  }
  .notice__box {
    animation: roll 10s linear infinite;
  }
  .notice__item {
    width: 100%;
    height: 60rpx;
    background-color: #f2f2f2;
    line-height: 60rpx;
    padding: 0 10rpx;
    margin: 20rpx 0;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .img {
    width: 38rpx;
    margin-right: 10rpx;
  }
  .txt {
    display: flex;
    font-size: 28rpx;
    align-items: center;
  }
  .time {
    font-size: 26rpx;
    color: #999999;
  }
}
.blue-text {
  color: #2e68f2;
}
</style>
