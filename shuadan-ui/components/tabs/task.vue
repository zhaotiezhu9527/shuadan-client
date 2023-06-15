<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      title="抢单"
      fixed
      safe-area-inset-top
      bgColor="#fff"
      leftIconSize="0"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view class="item">
        <image :src="vim.levelImg" class="icon" mode="widthFix" />
        <view class="content">
          <view>{{ vim.areaName }}</view>
          <view>{{ vim.remark }} 佣金{{ vim.commission }}%</view>
        </view>
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
        <view class="txt">全力抢单中，抢单结果将在下方发放。</view>
      </view>
    </view>
    <view class="btn">
      <u-button
        color="#2f3848"
        type="primary"
        text="充值升级"
        @click="addVip"
      ></u-button>
      <u-button
        color="#ff6225"
        type="primary"
        @click="startCallBack"
        text="自动匹配"
      ></u-button>
    </view>
    <view class="task">今日战果</view>
    <view class="report">
      <view class="item">
        <view class="txt gray">总资产</view>
        <view class="moeny gray">
          <template v-if="infos.balance">+</template>{{ infos.balance }}
        </view>
      </view>
      <view class="item">
        <view class="txt gray">昨日收益</view>
        <view class="moeny">
          <template v-if="infos.yesterdayIncome">+</template>
          {{ infos.yesterdayIncome }}
        </view>
      </view>
      <view class="item">
        <view class="txt">今日已抢佣金</view>
        <view class="moeny">
          <template v-if="infos.todayIncome">+</template
          >{{ infos.todayIncome }}</view
        >
      </view>
      <view class="item">
        <view class="txt">账户冻结金额</view>
        <view class="moeny">
          <template v-if="infos.freezeBalance">+</template
          >{{ infos.freezeBalance }}</view
        >
      </view>
      <view class="item">
        <view class="txt">今天已抢单数</view>
        <view class="moeny">{{ infos.todayOrderCount }}单</view>
      </view>
      <view class="item">
        <view class="txt">昨日团队佣金</view>
        <view class="moeny">
          <template v-if="infos.yesterdayTeamIncome">+</template
          >{{ infos.yesterdayTeamIncome }}</view
        >
      </view>
    </view>
    <view class="passStyle">
      <view class="pass">
        <text>解锁</text>
        <view>下一等级专属通道获得更高佣金</view>
      </view>
    </view>
    <view class="order">订单说明</view>
    <view class="info">
      <view>尊敬的用户你好</view>
      <view>
        平台为了防止有人恶意进行洗黑钱或者套现一系列不法行为，会员需完成70单方可进行申请提现，提现审核成功后，到账时间为T+0到账（2小时内）具体到账时间以银行为准！
      </view>
      <view>为了保证商家成交量的真实性，所有任务订单都是垫付立返</view>
      <view>
        抢到任务订单后请及时提交订单，避免长时间未提交导致卡单卡顿异常降低信用分
      </view>
    </view>
    <success ref="sucRef" />
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
    };
  },
  methods: {
    open(e) {
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
      let height = 19 * 220;
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
  },
};
</script>
<style scoped lang="scss">
.main {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding: 0 30rpx;
  padding-top: var(--status-bar-height);
  padding-bottom: calc(160rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
}
.list {
  display: flex;
  padding: 60rpx 0 20rpx;
  justify-content: center;
  flex-wrap: wrap;
  .item {
    width: 100%;
    padding: 20rpx;
    border-radius: 10rpx;
    background: #fff;
    position: relative;
    .bg {
      position: absolute;
      top: 0;
      right: 0;
      z-index: 1;
      width: 210rpx;
    }
    .icon {
      width: 148rpx;
      position: absolute;
      left: 20rpx;
      top: -20rpx;
      z-index: 2;
    }
    .txt {
      padding: 20rpx 0;
      font-size: 24rpx;
      color: #434343;
    }
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
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30rpx 0;
  gap: 30rpx;
  /deep/.u-button:nth-child(1) .u-button__text {
    color: #f2d8be;
    font-size: 30rpx !important;
  }
  /deep/.u-button:nth-child(2) .u-button__text {
    color: #ffffff;
    font-size: 30rpx !important;
  }
}
.passStyle {
  padding: 30rpx 0;
  .pass {
    display: flex;
    align-items: center;
    background-color: #fff;
    border-radius: 10rpx;
    padding: 25rpx 20rpx;
    view {
      font-size: 28rpx;
      padding-left: 16rpx;
    }
    text {
      background: #2f3848;
      border-radius: 4rpx;
      color: #f2d8be;
      font-size: 24rpx;
      padding: 6rpx 10rpx;
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
  padding: 60rpx 0 20rpx;
  view {
    font-size: 28rpx;
    line-height: 1.5;
    color: #9c9c9c;
    padding-top: 6rpx;
  }
}
.report {
  background-color: #fff;
  border-radius: 20rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  padding: 30rpx 0;
  .item {
    width: calc(100% / 3);
    padding: 20rpx;
    border-right: 1rpx solid #f1f1f1;
    &:nth-child(-n + 3) {
      padding-bottom: 30rpx;
    }
    &:nth-child(3) {
      border-right: 0;
    }
  }
  .txt {
    font-size: 24rpx;
    padding-bottom: 10rpx;
    &.gray {
      color: #9c9c9c;
    }
  }
  .moeny {
    font-size: 40rpx;
    color: red;
    &.gray {
      color: #000;
    }
  }
}
.task_content {
  width: 100%;
  height: 490rpx;
  background: url("@/static/img/task.png") no-repeat;
  background-size: 100% 100%;
  .padding {
    padding: 250rpx 20rpx 0;
  }
}
.container {
  width: 100%;
  display: flex;
}
.scroll-container {
  width: calc(100% / 3);
  height: 220rpx;
  text-align: center;
  overflow: hidden;
  .wenhao {
    height: 220rpx;
    max-height: 220rpx;
  }
  .item-text {
    line-height: 220rpx;
    max-height: 220rpx;
  }
}
</style>
