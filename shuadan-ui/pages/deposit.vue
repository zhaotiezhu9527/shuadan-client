<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="提现"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="title">
        <view class="title-text">
          <span>提现金额</span>
          <label>提现费率0%</label>
        </view>
        <view class="title-content">
          <view class="money">
            <label>¥</label>
            <input
              class="title-input"
              v-model="amount"
              type="number"
              placeholder="输入资金密码"
              @input="update"
            />
          </view>
          <view class="text">
            <span>余额：¥1000.22元</span>
            <label>全部提现</label>
          </view>
        </view>
      </view>
      <view class="content">
        <view class="item">
          <view class="detail">
            <label>手机号</label>
            <span>13333333333</span>
          </view>
          <view class="detail">
            <label>银行卡号</label>
            <span>13333333333</span>
          </view>
          <view class="detail">
            <label>所属银行</label>
            <span>13333333333</span>
          </view>
          <view class="detail">
            <label>开户名</label>
            <span>13333333333</span>
          </view>
          <view class="detail">
            <label>资金密码</label>
            <input
              class="title-input"
              v-model="amount"
              type="number"
              placeholder="输入提现金额"
            />
          </view>
        </view>
      </view>
      <view class="foot-text">
        *<br>
        请仔细核对收款信息<br>
        本次提现扣除手续费 0%
      </view>
      <view class="btn">
        <u-button
              class="custom-style"
              color="#4350af"
              block
              @click="submit"
              :loading="loading"
            >
              立即提现
            </u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      amount:"",
      loading: false,
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {
    submit(){

    }
  },
};
</script>

<style scoped lang="scss">
.main{
  background-color: #f2f2f2;
  height: calc(100vh - 52px + var(--status-bar-height));
  overflow: hidden;
  .title ,.content{
    width: 92%;
    background-color: #fff;
    margin: 20rpx auto;
    border-radius: 20rpx;
    padding: 20rpx 0;
    .title-text{
      padding: 0 20rpx;
      span{
        font-size: 32rpx;
      }
      label{
        border: 1px solid #fbbd08;
        font-size: 24rpx;
        padding: 2rpx 12rpx;
        border-radius: 25rpx;
        color: #fbbd08;
        margin-left: 10rpx;
      }
    }
    .title-content {
      background-color: #fff;
      padding: 0 20rpx;
      font-size: 24rpx;
      .money {
        height: 80rpx;
        display: flex;
        align-items: center;
        margin-top: 30rpx;
        label {
          width: 80rpx;
          font-size: 40rpx;
          font-weight: 500;
          margin-left: 20rpx;
          text-align: center;
        }
        .title-input {
          font-size: 56rpx;
        }
      }
      .text {
        font-size: 24rpx;
        font-weight: 500;
        margin-top: 50rpx;
        span{
          color: #999;
        }
        label{
          margin-left: 40rpx;
          color: #333;
          font-weight: 400;
        }
      }
    }
  }
  .detail{
    display: flex;
    height: 100rpx;
    margin: 0 30rpx;
    line-height: 100rpx;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #8799a3;
      font-size: 30rpx;
    }
    .title-input {
      flex: 1;
      text-align: right;
      height: 100rpx;
      line-height: 100rpx;
    }
    span{
      flex: 1;
      text-align: right;
      font-size: 30rpx;
    }
  }
  .foot-text{
    width: 92%;
    background-color: #fff;
    margin: 20rpx auto;
    border-radius: 20rpx;
    padding: 20rpx 20rpx;
  }
  .btn{
    width: 90%;
    margin: 40rpx auto;
  }
}
</style>
