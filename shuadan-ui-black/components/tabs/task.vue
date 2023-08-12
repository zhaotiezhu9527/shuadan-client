<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      title="抢单"
      fixed
      safe-area-inset-top
      bgColor="#000000"
      leftIconSize="0"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view class="item">
        <view class="flex items-center justify-between">
          <view class="content">
            <view>{{ vim.areaName }}</view>
            <view>{{ vim.remark }} 佣金{{ vim.commission }}%</view>
          </view>
          <image :src="vim.levelImg" class="icon" mode="widthFix" />
        </view>
        <view class="task_content">
          <view class="padding">
            <image
              class="bg012"
              src="/static/img/bg-012.png"
              mode="heightFix"
            />
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
      <view class="btn">
        <u-button
          type="primary"
          text="充值升级"
          @click="addVip"
          class="button-golden"
        ></u-button>
        <u-button
          type="primary"
          @click="startCallBack"
          text="自动匹配"
          class="button"
        ></u-button>
      </view>
    </view>
    <view class="task">今日战果</view>
    <view class="report">
      <view class="item">
        <view class="moeny gray">
          {{ moenyFn(infos.balance) }}
        </view>
        <view class="txt">总资产</view>
      </view>
      <view class="item">
        <view class="moeny">
          {{ moenyFn(infos.yesterdayIncome) }}
        </view>
        <view class="txt">昨日收益</view>
      </view>
      <view class="item">
        <view class="moeny"> {{ moenyFn(infos.todayIncome) }}</view>
        <view class="txt">今日已抢佣金</view>
      </view>
      <view class="item">
        <view class="moeny">
          <template v-if="infos.freezeBalance"></template>
          {{ infos.freezeBalance }}
        </view>
        <view class="txt">账户冻结金额</view>
      </view>
      <view class="item">
        <view class="moeny">{{ infos.todayOrderCount }} 单</view>
        <view class="txt">今天已抢单数</view>
      </view>
      <view class="item">
        <view class="moeny"> {{ moenyFn(infos.yesterdayTeamIncome) }}</view>
        <view class="txt">昨日团队佣金</view>
      </view>
    </view>
    <view class="passStyle">
      <view class="pass">
        <text>解锁</text>
        <view>下一等级专属通道获得更高佣金</view>
      </view>
    </view>
    <view class="task">订单说明</view>
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
      let height = 19 * 120;
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
      } else if (val >= 100000) {
        return parseInt(val / 100000) + "万+";
      } else {
        return "" + val;
      }
    },
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
  background-color: #111111;
  min-height: 100vh;
  padding: 0 30rpx;
  padding-top: var(--status-bar-height);
  padding-bottom: calc(240rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(240rpx + env(safe-area-inset-bottom));
}
.list {
  display: flex;
  margin-top: 24rpx;
  padding: 0 0 20rpx;
  justify-content: center;
  @extend .back;
  flex-wrap: wrap;
  border-radius: 32rpx;
  .item {
    width: 100%;
    padding: 20rpx;
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
      z-index: 2;
    }
    .txt {
      padding: 16rpx 0;
      width: 80%;
      text-align: center;
      margin: 0 auto;
      font-size: 24rpx;
      color: #ffffffa6;
      background: #ffffff1f;
      border-radius: 16px;
    }
  }
  .content {
    z-index: 2;
    position: relative;
    view:nth-child(1) {
      font-weight: 600;
      font-size: 32rpx;
      padding-top: 40rpx;
      color: $white;
    }
    view:nth-child(2) {
      color: #ffffffa6;
      font-size: 20rpx;
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
.btn {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30rpx 0;
  width: 90%;
  gap: 30rpx;
  /deep/.u-button {
    border-radius: 100rpx;
  }
  /deep/.u-button:nth-child(1) .u-button__text {
    color: $white;
    font-size: 32rpx !important;
  }
  /deep/.u-button:nth-child(2) .u-button__text {
    color: $white;
    font-size: 32rpx !important;
  }
}
.passStyle {
  padding: 30rpx 0;
  .pass {
    @extend .back;
    display: flex;
    align-items: center;
    border-radius: 10rpx;
    padding: 25rpx 20rpx;
    view {
      font-size: 28rpx;
      padding-left: 16rpx;
      color: $white;
    }
    text {
      padding: 4rpx 8rpx;
      border-radius: 8rpx;
      color: #121212;
      display: flex;
      align-items: center;
      font-weight: 400;
      justify-content: center;
      margin-left: 10rpx;
      font-size: 20rpx;
      background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
    }
  }
}
.info {
  @extend .back;
  padding: 32rpx 24rpx;
  border-radius: 16rpx;
  view {
    font-size: 20rpx;
    line-height: 1.5;
    color: #ffffffd9;
    padding-top: 10rpx;
  }
}
.report {
  border-radius: 20rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 16rpx;
  padding: 30rpx 0;
  .item {
    @extend .back;
    border-radius: 16rpx;
    width: calc(100% / 3 - 16rpx);
    padding: 20rpx;
    &:nth-child(-n + 3) {
      padding-bottom: 30rpx;
    }
  }
  .txt {
    text-align: center;
    color: #ffffffa6;
    font-size: 20rpx;
  }
  .moeny {
    font-weight: 600;
    font-size: 32rpx;
    padding-bottom: 10rpx;
    color: #ffffffd9;
    text-align: center;
  }
}
.task_content {
  width: 100%;
  height: 652rpx;
  background: url("@/static/img/task.png") no-repeat;
  background-size: 100% 100%;
  .padding {
    padding: 280rpx 20rpx 0;
    width: 65%;
    transform: translateX(-20rpx);
    margin: 0 auto;
    position: relative;
  }
  .bg012 {
    height: 64rpx;
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translateX(-50%);
  }
}
.container {
  width: 100%;
  display: flex;
}
.scroll-container {
  width: calc(100% / 3);
  height: 120rpx;
  text-align: center;
  overflow: hidden;
  .wenhao {
    height: 60rpx;
  }
  .item-text {
    display: flex;
    justify-content: center;
    align-items: center;
    line-height: 120rpx;
    min-height: 120rpx;
  }
}
</style>
