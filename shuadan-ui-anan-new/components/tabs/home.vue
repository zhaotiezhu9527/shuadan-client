<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      :title="$t('home')"
      fixed
      safe-area-inset-top
      leftIconSize="0"
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="content">
      <!-- 规则抬头 -->
      <view class="title">
        <view v-for="(item, index) in nav"
        :key="index"
        @click="change(item.en)"
        :class="index == 2 ? 'width105': 'title-item'"
        >
          <view class="title-bg">
            <image class="title-img" :src="item.img" />
          </view>
          <view class="title-text">{{ item.name }}</view>
        </view>
      </view>
      <!-- 金额显示 -->
      <view class="price">
        <view class="price-content">
          <view class="price-title">
            {{ $t("balance") }}（{{ $t("rmb") }}）
          </view>
          <view class="price-box">
            <view class="flex">
              <view class="money">
                <view class="txt">{{ Number(infos.balance).toFixed(2) }}</view>
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
                  <view class="name">{{ Number(infos.todayIncome).toFixed(2) }}</view>
                  <view class="txt">{{ $t("todayIncome") }}({{ $t("rmb") }})</view>
                </view>
                <view class="li flex1">
                  <view class="name">{{ Number(infos.totalIncome).toFixed(2) }}</view>
                  <view class="txt">{{ $t("totalIncome") }}({{ $t("rmb") }})</view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <!-- vip等级 -->
      <view class="vip">
        <view class="header">{{ $t("vipName") }}</view>
      </view>
    </view>
    
    <!-- <view class="list">
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
      </view>
    </view> -->
  </view>
</template>

<script>
import img13 from "@/static/img/title-about.png";
import img14 from "@/static/img/title-yinzhang.png";
import img15 from "@/static/img/title-rul.png";
import img16 from "@/static/img/title-q.png";

export default {
  props:['userData'],
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
      bindStatus: false, //银行卡绑定状态
    };
  },
  methods: {
    open() {
      this.getInfo();
      this.$api.area_list().then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
      this.level =
        this.list.find((item) => item.remark.includes(this.userData.levelName))
          ?.level || 1;
      if (this.userData.walletAddr === null || !this.userData.walletAddr) {
        this.bindStatus = false;
      } else {
        this.bindStatus = true;
      }
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
      if (!this.bindStatus && url == "/pages/deposit") {
        this.$base.show(this.$t("bindUSDTAddr"));
      } else {
        uni.navigateTo({
          url,
        });
      }
    },
    //用户列表数据
    getInfo() {
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
  },
};
</script>

<style scoped lang="scss">
.main {
  padding-top: 32rpx;
  padding-bottom: 160rpx;
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
.content{
  width: 95%;
  margin: auto;
  .title{
    width: 100%;
    height: 228rpx;
    background-color: #fff;
    color: #343434;
    font-size: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    .title-item{
      flex: 1;
    }
    .width105{
      width: 210rpx;
    }
    .title-bg{
      width: 120rpx;
      height: 120rpx;
      border-radius: 60rpx;
      background-color: rgba(255, 255, 255, 1);
      box-shadow: 2px 2px 2px 0 #333;
      margin: auto;
      .title-img{
        width: 72rpx;
        height: 72rpx;
        display: block;
        margin: auto;
        padding-top: 24rpx;
      }
    }
    .title-text{
      margin-top: 16rpx;
    }
  }
  .price {
    width: 100%;
    margin: 0 auto;
    border-radius: 50rpx;
    background-color: #fff;
    color: #000;
    box-shadow: 0px 0px 1px 1px #333;
    margin-top: 46rpx;
    .price-content{
      .price-title{
        height: 70rpx;
        line-height: 70rpx;
        text-indent: 40rpx;
      }
    }
    .price-box {
      padding: 40rpx 20rpx 30rpx;
      border-radius: 50rpx;
      box-shadow: 0px 0px 1px 1px #333;
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
            width: 170rpx;
            border-radius: 16rpx;
            background: #7d7d7d;
            color: #fff;
          }
        }
      }
      .money {
        .title {
          font-size: 24rpx;
          font-weight: 500;
        }
        .txt {
          font-weight: 600;
          font-size: 64rpx;
          padding-top: 20rpx;
          font-weight: bold;
        }
      }
      .ulStyle {
        margin-top: 48rpx;
        padding-top: 16rpx;
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
          text-align: center;
          font-size: 36rpx;
          font-weight: 600;
          letter-spacing: 0.54px;
        }
        .txt {
          text-align: center;
          font-size: 20rpx;
          font-weight: 500;
          padding-top: 8rpx;
        }
      }
    }
  }
}
.header{
  margin: 58rpx 0 46rpx 0;
  color: #000;
  font-size: 36rpx;
  font-weight: 600;
}
</style>
