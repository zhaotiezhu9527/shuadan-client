<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="提现"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#1E1E1E"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="link">
        <view class="flex">
          <image
            class="image"
            src="@/static/img/unionpay.png"
            mode="widthFix"
          />
          <view class="box">
            <view>{{ bankName }}</view>
            <view>{{ bankNo }}</view>
          </view>
        </view>
        <view class="name">
          <view>{{ realName }}</view>
          <view>{{ phone }}</view>
        </view>
      </view>
      <view class="content">
        <view class="title">
          <view class="name">充值金额</view>
          <view class="rate" @click="submit">提现费率 {{ withdrawFee }} %</view>
        </view>
        <u-input
          placeholder="请输入提现金额"
          border="bottom"
          placeholderClass="placeholder"
          v-model="amount"
          clearable
          type="number"
          @blur="update"
        >
          <template #prefix>
            <view class="rmb">￥</view>
          </template>
        </u-input>
        <view class="text">
          <text>可用余额：¥{{ balance }}</text>
          <text @click="amount = balance">全部提现</text>
        </view>
      </view>
      <view class="foot-text">
        请仔细核对收款信息<br />
        本次提现扣除手续费 {{ withdrawFee }}%
      </view>
      <view class="btn">
        <u-button class="button" block @click="submit" :loading="loading">
          确认提现
        </u-button>
      </view>
    </view>
    <depositMark ref="depositMarkRef" @ok="clear" />
  </view>
</template>

<script>
import depositMark from "../components/depositMark.vue";
export default {
  components: {
    depositMark,
  },
  data() {
    return {
      amount: "",
      loading: false,
      phone: "", //手机号
      bankNo: "", //银行卡号
      bankName: "", //银行名称
      realName: "", //真实姓名
      pwd: "", //支付密码
      balance: 0, //余额
      withdrawFee: 0, //提款手续费
    };
  },
  onShow() {
    this.getInfo();
  },
  methods: {
    clear() {
      this.amount = "";
      this.getInfo();
    },
    submit() {
      if (!this.amount) {
        return this.$base.show("请输入提现金额~");
      } else if (this.amount > this.balance) {
        return this.$base.show("提现金额不能超过" + this.balance);
      }
      this.$refs.depositMarkRef.open({
        amount: this.amount,
        balance: this.balance,
      });
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.phone = res.data.data.phone;
          this.bankNo = res.data.data.bankNo;
          this.bankName = res.data.data.bankName;
          this.realName = res.data.data.realName;
          this.balance = res.data.data.balance;
          this.withdrawFee = res.data.data.withdrawFee;
        }
      });
    },
    // 金额文本框点击事件
    update() {
      if (this.amount > this.balance) {
        this.amount = this.balance;
      } else if (this.amount <= 0) {
        this.amount = 0;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  height: calc(100vh - 52px + var(--status-bar-height));
  overflow: hidden;
  .link {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24rpx 32rpx;
    margin-top: 16rpx;
    background-color: #1e1e1e;
    .image {
      width: 96rpx;
      margin-right: 16rpx;
    }
    .flex {
      display: flex;
      align-items: center;
    }
    .name {
      text-align: right;
      view:nth-child(1) {
        font-size: 24rpx;
        color: #ffffffd9;
      }
      view:nth-child(2) {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #ffffffa6;
      }
    }
  }
  .box {
    view:nth-child(1) {
      font-size: 32rpx;
      color: #ffffffd9;
    }
    view:nth-child(2) {
      padding-top: 10rpx;
      font-size: 24rpx;
      color: #ffffffa6;
    }
  }
  .content {
    margin-top: 16rpx;
    padding: 24rpx 32rpx;
    background-color: #1e1e1e;
    .title {
      padding-bottom: 24rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .name {
        font-size: 32rpx;
        color: #ffffffd9;
        font-weight: 600;
      }
      .rate {
        color: $white;
        border: 1rpx solid #ffffffa6;
        height: 48rpx;
        display: flex;
        align-items: center;
        padding: 0 8rpx;
        justify-content: center;
        border-radius: 8rpx;
        font-size: 24rpx;
      }
    }
    .u-input {
      padding: 20rpx 0rpx 44rpx !important;
      /deep/.uni-input-input {
        height: 100rpx !important;
        font-size: 64rpx;
        color: $white;
      }
      /deep/.u-input__content__field-wrapper__field {
        height: 80rpx !important;
      }
      &.u-border-bottom {
        border-color: rgba($white, 0.12) !important;
      }
    }
    .rmb {
      font-size: 48rpx;
      color: #ffffffd9;
    }
    .placeholder {
      font-size: 64rpx;
      font-weight: 500;
      color: #ffffff4d;
      height: 60rpx !important;
      display: flex;
      align-items: flex-end;
    }
    .text {
      padding: 26rpx 0 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      text {
        font-size: 28rpx;
        color: #ffffffa6;
      }
    }
  }

  .foot-text {
    margin: 20rpx auto;
    background-color: #1e1e1e;
    padding: 32rpx;
    color: #ffffff73;
    font-size: 24rpx;
    line-height: 2;
  }
  .btn {
    padding: 48rpx 42rpx 0;
    .button {
      border-radius: 100rpx;
    }
  }
}
</style>
