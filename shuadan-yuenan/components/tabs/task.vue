<template>
  <view class="main">
    <view class="sub_title">
      <view class="txt noborder">{{ $t("missionHall") }}</view>
    </view>
    <view class="items">
      <view class="li">
        <view class="flex">
          <image :src="vim.levelImg" class="icon" mode="widthFix" />
          <view class="txt">{{ vim.areaName }}</view>
        </view>
        <view class="gray">{{ vim.commission }}%</view>
      </view>
      <view class="li">
        <view class="flex">
          <image src="@/static/img/100042.png" class="icon" mode="widthFix" />
          <view class="con">
            <view class="gray">{{ $t("commissionToday") }}</view>
            <view class="blue">
              {{ $t("currencySymbol") }}{{ $u.priceFormat(infos.todayIncome) }}
            </view
            >
          </view>
        </view>
        <view style="width: 30%" class="gray">{{ $t("updateCommissionToday") }}</view>
      </view>
      <view class="li">
        <view class="flex">
          <image src="@/static/img/100043.png" class="icon" mode="widthFix" />
          <view class="con">
            <view class="gray">{{ $t("myAssets") }}</view>
            <view class="blue">
              {{ $t("currencySymbol") }}{{ $u.priceFormat(infos.balance) }}
              </view
            >
          </view>
        </view>
        <view style="width: 30%" class="gray">{{ $t("addedTotalBalance") }}</view>
      </view>
    </view>
    <view class="list">
      <view class="item">
        <view class="name">{{ vim.areaName }}</view>
        <view class="task_content">
          <view class="padding">
            <view class="container">
              <view
                class="scroll-container"
                v-for="(item, index) in animation"
                :key="index"
              >
                <view
                  class="transform-container"
                  :animation="item.animationData"
                  :class="{ type: !item.type }"
                >
                  <view v-for="(vim, ix) in 20" class="item-text">
                    <image
                      :key="ix"
                      class="wenhao"
                      src="@/static/img/wenhao.png"
                      mode="heightFix"
                    />
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
        <view class="btn">
          <u-button
            type="primary"
            @click="startCallBack"
            :text="$t('automaticMatching')"
          ></u-button>
        </view>
        <view class="txt">{{ $t("workingOrders") }}</view>
      </view>
    </view>
    <view class="sub_title">
      <view class="txt white">{{ $t("todayResults") }}</view>
    </view>
    <view class="report">
      <view class="item">
        <view class="txt">{{ $t("yesterdayTeamCommission") }}</view>
        <view class="moeny">
          {{ $t("currencySymbol")
          }}{{ $u.priceFormat(infos.yesterdayTeamIncome) }}</view
        >
      </view>
      <view class="item">
        <view class="txt">{{ $t("yesterdayEarnings") }}</view>
        <view class="moeny">
          {{ $t("currencySymbol") }}{{ $u.priceFormat(infos.yesterdayIncome) }}
        </view>
      </view>
      <view class="item">
        <view class="txt">{{ $t("todayNo") }}</view>
        <view class="moeny">{{ infos.todayOrderCount || 0 }}</view>
      </view>
      <view class="item">
        <view class="txt">{{ $t("AccountFrozenAmount") }}</view>
        <view class="moeny">
          <template v-if="infos.freezeBalance"></template>
          {{ $t("currencySymbol") }}{{ $u.priceFormat(infos.freezeBalance) }}
        </view>
      </view>
    </view>
    <view class="sub_title">
      <view class="txt white">{{ $t("orderInfo") }}</view>
    </view>
    <view class="info">
      <view>{{ $t("respectUsers") }}</view>
      <view>
        {{ $t("rule1") }}
      </view>
      <view>{{ $t("rule2") }}</view>
      <view>
        {{ $t("rule3") }}
      </view>
    </view>
    <success ref="sucRef" @ok="getInfo(that)" />
  </view>
</template>
<script>
import success from "@/components/success.vue";
export default {
  components: { success },
  data() {
    return {
      items: {},
      infos: {},
      animation: [
        {
          type: 0,
          animationData: null,
          time: 0,
        },
        {
          type: 1,
          animationData: null,
          time: 100,
        },
        {
          type: 0,
          animationData: null,
          time: 200,
        },
      ],
      loading: false,
      vim: {},
      that: {},
    };
  },
  methods: {
    open(e) {
      this.that = e;
      this.getInfo(e);
    },
    addVip() {
      uni.navigateTo({
        url: "/pages/vip",
      });
    },

    //用户列表数据,用户收益详情
    getInfo(e) {
      let index = e.level || 1;
      Promise.all([
        this.$api.user_info(),
        this.$api.user_income_detail(),
        this.$api.area_detail(index),
      ]).then((res) => {
        if (res[0].data.code == 0) {
          this.items = res[0].data.data;
        } else {
          this.$base.show(res[0].data.msg);
        }
        if (res[1].data.code == 0) {
          this.infos = res[1].data.data;
        } else {
          this.$base.show(res[1].data.msg);
        }
        if (res[2].data.code == 0) {
          this.vim = res[2].data.data;
        } else {
          this.$base.show(res[2].data.msg);
        }
      });
    },
    // 开始滚动
    startCallBack() {
      if (this.loading) return false;
      this.loading = true;
      let height = 19 * 180;
      this.animation.forEach((item) => {
        let animation = uni.createAnimation({
          duration: 6500,
          timingFunction: "ease",
          delay: item.time,
        });
        animation.translateY(-height + "rpx").step();
        item.animationData = animation.export();
      });
      setTimeout(() => {
        this.endCallBack();
      }, 6500);
    },
    // 重置
    endCallBack() {
      this.$api.order_match(this.vim.areaId).then(({ data }) => {
        this.loading = false;
        if (data.code == 0) {
          this.getInfo(this.vim.areaId);
          this.$refs.sucRef.open(data.data.orderNo);
        }
      });
      let animation = uni.createAnimation({
        duration: 0,
      });
      this.animation.forEach((item) => {
        item.animationData = animation.translateY(0).step().export();
      });
    },
    moenyFn(val) {
      if (!val) {
        return 0;
      } else if (val >= 10000) {
        return parseInt(val / 10000) + "万+";
      } else {
        return "" + val;
      }
    },
  },
};
</script>
<style scoped lang="scss">
.main {
  background: url("@/static/img/userbg.png") no-repeat;
  background-size: 100% 100%;
  min-height: 100vh;
  padding: 0 30rpx;
  padding-top: var(--status-bar-height);
  padding-bottom: calc(160rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
}
.items {
  background-color: rgba(#fff, 0.5);
  border-radius: 16rpx;
  margin-bottom: 40rpx;
  .li {
    padding: 20rpx 30rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1rpx solid #c0c7d8;
    &:nth-last-child(1) {
      border-bottom: 0;
    }
    .con {
      padding-left: 10rpx;
    }
    .flex {
      display: flex;
      align-items: center;
    }
    .txt {
      font-size: 26rpx;
      padding-left: 20rpx;
    }
    .gray {
      font-size: 26rpx;
      color: #7e869f;
    }
    .blue {
      color: #2e68f2;
      font-weight: 600;
      font-size: 26rpx;
      margin-top: 6rpx;
    }
  }
  .icon {
    width: 110rpx;
  }
}
.list {
  display: flex;
  padding: 0 0 20rpx;
  justify-content: center;
  flex-wrap: wrap;
  .item {
    width: 100%;
    padding: 20rpx;
    border-radius: 18rpx;
    background: url("@/static/img/100041.jpg") no-repeat;
    background-size: 100% 100%;
  }
  .content {
    z-index: 2;
    position: relative;
    view:nth-child(1) {
      color: #434343;
      font-weight: 600;
      font-size: 46rpx;
      padding-top: 40rpx;
    }
    view:nth-child(2) {
      color: #969696;
      font-size: 24rpx;
      padding: 16rpx 0;
    }
  }
  .img {
    width: 100%;
    z-index: 2;
    position: relative;
  }
}
.task {
  font-weight: 600;
  color: #434343;
  font-size: 46rpx;
  padding: 30rpx 0 10rpx;
}
.btn {
  padding: 50rpx 0 30rpx;
  width: 320rpx;
  margin: 0 auto;
  /deep/.u-button {
    background: url("@/static/img/100040.png") no-repeat;
    background-size: 100% 100%;
    border: 0;
    height: 100rpx;
    .u-button__text {
      color: red;
      font-size: 30rpx !important;
    }
  }
}
.order {
  background: linear-gradient(209deg, #ffcc2c, #ff9a2c);
  width: 190rpx;
  border-radius: 10rpx;
  color: #fff;
  text-align: center;
  padding: 10rpx 0;
  font-size: 24rpx;
}
.info {
  padding: 0 0 20rpx;
  view {
    font-size: 26rpx;
    line-height: 1.5;
    color: #fff;
    padding-top: 6rpx;
    font-weight: 600;
  }
}
.report {
  border-radius: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  .item {
    background-color: rgba(#fff, 0.8);
    width: calc(96% / 2);
    padding: 30rpx;
    margin-bottom: 20rpx;
    border-radius: 16rpx;
  }
  .txt {
    text-align: center;
    font-size: 26rpx;
    padding-bottom: 10rpx;
  }
  .moeny {
    text-align: center;
    font-size: 26rpx;
    color: #ff6b21;
    font-weight: 600;
  }
}
.list {
  .name {
    font-size: 28rpx;
    text-align: center;
    padding: 40rpx 0 20rpx;
  }
  .txt {
    color: #7e869f;
    font-size: 24rpx;
    padding-top: 30rpx;
  }
}
.task_content {
  width: 100%;
}
.container {
  width: 100%;
  display: flex;
  padding: 20rpx 0;
}
.scroll-container {
  width: calc(100% / 3);
  height: 180rpx;
  text-align: center;
  overflow: hidden;
  .wenhao {
    height: 180rpx;
    max-height: 180rpx;
  }
  .item-text {
    line-height: 180rpx;
    max-height: 180rpx;
  }
}

.sub_title {
  padding: 40rpx 0;
  .txt {
    border-left: 6rpx solid #2e68f2;
    &.noborder {
      border-width: 0;
    }
    font-weight: 600;
    font-size: 36rpx;
    padding-left: 10rpx;
    &.white {
      color: #fff;
    }
  }
}
</style>
