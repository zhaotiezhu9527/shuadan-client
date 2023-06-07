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
        <image :src="items.icon" class="icon" mode="widthFix" />
        <view class="content">
          <view>{{ items.name }}</view>
          <view>{{ items.content }}</view>
        </view>
        <view class="task_content">
          <view class="padding">
            <SlotMachine
              ref="myLucky"
              width="610rpx"
              height="220rpx"
              :prizes="prizes"
              :defaultConfig="defaultConfig"
              :slots="slots"
              @start="startCallBack"
              @end="endCallBack"
            />
          </view>
        </view>
        <view class="txt">全力抢单中，抢单结果将在下方发放。</view>
      </view>
    </view>
    <view class="btn">
      <u-button color="#2f3848" type="primary" text="充值升级"></u-button>
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
        <view class="moeny gray">+11.81</view>
      </view>
      <view class="item">
        <view class="txt gray">昨日收益</view>
        <view class="moeny">+11.81</view>
      </view>
      <view class="item">
        <view class="txt">今日已抢佣金</view>
        <view class="moeny">+11.81</view>
      </view>
      <view class="item">
        <view class="txt">账户冻结金额</view>
        <view class="moeny">+11.81</view>
      </view>
      <view class="item">
        <view class="txt">今天已抢单数</view>
        <view class="moeny">7单</view>
      </view>
      <view class="item">
        <view class="txt">昨日团队佣金</view>
        <view class="moeny">+11.81</view>
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
  </view>
</template>
<script>
import img03 from "@/static/img/icon03.png";
import img04 from "@/static/img/icon04.png";
import img05 from "@/static/img/icon05.png";
import img06 from "@/static/img/icon06.png";
import wenhao from "@/static/img/wenhao.png";
import SlotMachine from "@/components/@lucky-canvas/uni/slot-machine";
export default {
  data() {
    return {
      items: {
        name: "拼多多",
        content: "白银会员专属通道 佣金0.3%",
        icon: img03,
      },
      slots: [
        { speed: 8, direction: 1 },
        { speed: 9, direction: -1 },
        { speed: 10, direction: 1 },
      ],
      prizes: [
        {
          borderRadius: "10px",
          imgs: [
            {
              width: "100%",
              top: "0%",
              src: wenhao,
            },
          ],
        },
      ],
      defaultConfig: {
        rowSpacing: "10px",
        colSpacing: "10px",
      },
    };
  },
  onLoad() {},
  onShow() {},
  methods: {
    startCallBack() {
      // 先开始旋转
      this.$refs.myLucky.play();
      setTimeout(() => {
        // 假设后端返回的中奖索引是0
        const index = 0;
        this.$refs.myLucky.stop(index);
      }, 3000);
    },
    endCallBack(e) {
      console.log(e);
    },
  },
  components: { SlotMachine },
};
</script>
<style scoped lang="scss">
.main {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding: 0 30rpx;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
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
</style>
