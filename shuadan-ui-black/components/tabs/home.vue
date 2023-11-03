<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      :title="$t('home')"
      fixed
      bgColor="#000000"
      leftIconSize="0"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="price">
      <view class="price-box">
        <view class="flex">
          <view class="money">
            <view class="title">{{ $t("balance") }}（{{ $t("rmb") }}）</view>
            <view class="txt">{{ infos.balance }}</view>
          </view>
          <view class="btn">
            <view @click="goDeposit('/pages/recharge')">
              {{ $t("recharge") }}
            </view>
            <view class="active" @click="goDeposit('/pages/deposit')">
              {{ $t("deposit") }}
            </view>
          </view>
        </view>
        <view class="ulStyle">
          <view class="ul">
            <view class="li">
              <view class="name">{{ infos.todayIncome }}</view>
              <view class="txt">{{ $t("todayIncome") }}({{ $t("rmb") }})</view>
            </view>
            <view class="li">
              <view class="name">{{ infos.yesterdayIncome }}</view>
              <view class="txt"
                >{{ $t("yesterdayIncome") }}({{ $t("rmb") }})</view
              >
            </view>
            <view class="li">
              <view class="name">{{ infos.totalIncome }}</view>
              <view class="txt">{{ $t("totalIncome") }}({{ $t("rmb") }})</view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="notice">
      <view class="tips">
        <view class="image">
          <image class="img" src="@/static/img/bg-004.png" mode="widthFix" />
        </view>
        <!-- <marquee class="marquee">
          <div v-html="homeNotice"></div>
        </marquee> -->
        <div class="marquee">
          {{ $t("noticeTitle") }}
        </div>
        <view class="btn" @click="goDeposit('/pages/message')">
          {{ $t("message") }}
        </view>
      </view>
    </view>
    <view class="header py">{{ $t("task") }}</view>
    <view class="list">
      <view
        class="item"
        v-for="(item, index) in list"
        :key="index"
        @click="
          routechange2(item.unlock, '/pages/index?tabs=2&level=' + item.areaId)
        "
      >
        <view class="no" v-if="!item.unlock">
          <image class="img" src="@/static/img/suo.png" mode="widthFix" />
          <view class="txt">{{ $t("unlock") }}</view>
        </view>
        <view class="reactive">
          <image :src="item.levelImg" class="img" mode="widthFix" />
        </view>
        <!-- <view class="content">
          <view>{{ item.areaName }}</view>
          <view>{{ item.remark }}</view>
        </view> -->
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
        class="item flex items-center justify-between"
        @click="change(item.en)"
        v-for="(item, index) in nav"
        :key="index"
      >
        <view class="con">
          <view class="name">{{ item.name }}</view>
          <!-- <view class="txt">{{ item.txt }}</view> -->
        </view>
        <u-icon
          size="20"
          color="#fff"
          name="arrow-right"
          class="arrow-right"
        ></u-icon>
      </view>
    </view>
    <!-- <annunciate ref="annunciateRef" /> -->
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
        {
          img: img13,
          name: this.$t("introduce"),
          en: "ptjj",
          txt: "introduce",
        },
        {
          img: img14,
          name: this.$t("instructions"),
          en: "dlhz",
          txt: "instructions",
        },
        {
          img: img15,
          name: this.$t("cooperation"),
          en: "gzsm",
          txt: "cooperation",
        },
        {
          img: img16,
          name: this.$t("qualification"),
          en: "gszz",
          txt: "qualification",
        },
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
    open() {
      this.getInfo();
      this.$api.system_config().then(({ data }) => {
        if (data.code == 0) {
          this.homeNotice = data.data.homeNotice;
          this.$refs.annunciateRef.open(data.data);
        }
      });
      this.$api.area_list().then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    change(en) {
      if(en != "dlhz"){
        uni.navigateTo({
          url: `/pages/richtext?en=${en}`,
        });
      }else{
        uni.navigateTo({
          url: `/pages/zhengshu`,
        });
      }
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
        this.$base.show(this.$t("input_bind"));
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
.back {
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
  border: 1rpx solid;
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
}
.main {
  padding-top: 32rpx;
  background-color: #280c43;
  .header {
    width: 90%;
    font-weight: bold;
    margin: 0 auto 10rpx auto;
    font-size: 36rpx;
    color: $white;
    position: relative;
    padding: 30rpx 0 20rpx 40rpx;
    &::after {
      content: "";
      position: absolute;
      left: 0;
      border-radius: 8rpx;
      top: 30rpx;
      background: linear-gradient(130.74deg, #6c38ed 17.26%, #9167f2 91.1%);
      height: 48rpx;
      width: 16rpx;
    }
  }
  .price {
    width: 90%;
    margin: 0 auto;
    border-radius: 20rpx;
    background: url("@/static/img/bg-015.png") no-repeat;
    background-size: 100% 100%;
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
      align-items: center;
      gap: 8rpx;
      justify-content: space-between;
      flex-direction: column;
      view {
        display: flex;
        align-items: center;
        justify-content: center;
        // width: 104rpx;
        height: 64rpx;
        padding: 0 24rpx;
        border-radius: 16rpx;
        background: rgba($white, 0.12);
        color: $white;
      }
    }
  }
  .money {
    .title {
      font-size: 24rpx;
      font-weight: 500;
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
    margin-top: 48rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid rgba($white, 0.12);
  }
}
.ul {
  padding-top: 5rpx;
  display: flex;
  text-align: center;
  .li {
    width: calc(100% / 3);
    position: relative;
    &::after {
      content: "";
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      left: 0;
      height: 60rpx;
      background: rgba($white, 0.12);
      width: 1rpx;
    }
    &:nth-child(1) {
      &::after {
        display: none;
      }
    }
  }
  .name {
    color: $white;
    text-align: center;
    font-size: 36rpx;
    font-weight: 600;
    letter-spacing: 0.54px;
  }
  .txt {
    color: rgba($white, 0.45);
    text-align: center;
    font-size: 20rpx;
    font-weight: 500;
    padding-top: 8rpx;
  }
}
.rich {
  padding: 48rpx 0;
  width: 90%;
  gap: 8rpx;
  margin: 48rpx auto 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid rgba($white, 0.12);
  .item {
    background-color: $white;
    border-radius: 20rpx;
    width: calc(25% - 8rpx);
    padding: 20rpx 5rpx;
    position: relative;
    @extend .back;
  }
  .arrow-right {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
  }
  .con {
    padding: 0 10rpx;
    .name {
      padding-bottom: 10rpx;
      color: rgba($white, 0.85);
      font-size: 16rpx;
    }
    .txt {
      color: rgba($white, 0.65);
      font-size: 16rpx;
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
  gap: 16rpx;
  .reactive {
    position: relative;
    margin: auto;
  }
  .no {
    position: absolute;
    z-index: 10;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(#000, 0.5);
    border-radius: 20rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    overflow: hidden;

    .img {
      width: 36rpx;
      margin: 0 auto;
    }
    .txt {
      color: $white;
      font-size: 24rpx;
      padding-top: 10rpx;
    }
  }
  .item {
    @extend .back;
    padding: 20rpx;
    width: calc(50% - 16rpx);
    border-radius: 20rpx;
    position: relative;
    display: flex;
    align-items: center;
    min-height: 144rpx;
    .icon {
      width: 44rpx;
    }
  }
  .content {
    z-index: 2;
    position: relative;
    padding-bottom: 10rpx;
    view {
      color: #cac8d9;
      font-weight: 600;
      font-size: 28rpx;
    }
    view:nth-child(2) {
      font-size: 20rpx;
      font-weight: 400;
      color: rgba($white, 0.65);
      padding-top: 8rpx;
    }
  }
  .img {
    width: 240rpx;
    z-index: 2;
    position: relative;
    // margin-right: 16rpx;
  }
}
.notice {
  width: 90%;
  margin: 4rpx auto 0;
  padding-top: 28rpx;
  position: relative;
  .tips {
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 20rpx;
  }
  .image {
    padding: 0 20rpx;
    .img {
      width: 58rpx;
    }
  }
  .btn {
    color: $main-c;
    padding-left: 20rpx;
    width: 120rpx;
    margin-left: 20rpx;
    position: relative;
    &::after {
      content: "";
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      left: 0;
      height: 30rpx;
      background: rgba($white, 0.12);
      width: 1rpx;
    }
  }
}
.marquee {
  flex:1;
  align-items: center;
  padding: 20rpx 0 20rpx 34rpx;
  font-size: 28rpx;
  font-weight: normal;
  line-height: 40rpx;
  color: $white;
}
</style>
