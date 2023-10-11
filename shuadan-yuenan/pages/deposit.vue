<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('withdraw')"
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
          <span>{{$t('withdrawalAmount') }}</span>
          <label>{{$t('withdrawalRate') }}{{ withdrawFee }}%</label>
        </view>
        <view class="title-content">
          <view class="money">
            <!-- <label>¥</label> -->
            <input
              class="title-input"
              v-model="amount"
              type="number"
              :placeholder="$t('enterWithdrawalAmount')"
              @blur="update"
            />
          </view>
          <view class="text">
            <span>{{$t('balance') }}：{{ $u.priceFormat(balance) }}</span>
            <label @click="amount = balance">{{$t('withdrawAll') }}</label>
          </view>
        </view>
      </view>
      <view class="content">
        <view class="item">
          <!-- <view class="detail">
            <label>{{$t('phoneNo') }}</label>
            <span>{{ phone }}</span>
          </view> -->
          <view class="detail">
            <label>{{$t('bankNo') }}</label>
            <span>{{ bankNo }}</span>
          </view>
          <view class="detail">
            <label>{{$t('bankName') }}</label>
            <span>{{ bankName }}</span>
          </view>
          <view class="detail">
            <label>{{$t('realName') }}</label>
            <span>{{ realName }}</span>
          </view>
          <view class="detail">
            <label>{{$t('payPassword') }}</label>
            <input
              class="title-input"
              v-model="pwd"
              type="number"
              :placeholder="$t('enterPayPassword')"
            />
          </view>
        </view>
      </view>
      <view class="foot-text">
        *<br />
        {{$t('checkInfo') }}<br />
        {{$t('handlingFee') }} {{ withdrawFee }}%
      </view>
      <view class="btn">
        <u-button
          class="custom-style"
          color="#4350af"
          block
          @click="submit"
          :loading="loading"
        >
        {{$t('withdrawImmediately') }}
        </u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
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
    submit() {
      if (!this.amount) {
        return this.$base.show(this.$t('enterWithdrawalAmount'));
      } else if (!this.pwd) {
        return this.$base.show(this.$t('enterPayPassword'));
      } else if (this.amount > this.balance) {
        return this.$base.show(this.$t('NotExceed') + this.balance);
      }
      this.loading = true;
      const DATA_OBJ = {
        amount: this.amount, //提现金额
        pwd: this.pwd, //密码
      };
      this.$api
        .user_withdraw(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
            this.getInfo();
          }
        })
        .finally(() => {
          this.loading = false;
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
  background-color: #f2f2f2;
  height: calc(100vh - 52px + var(--status-bar-height));
  overflow: hidden;
  .title,
  .content {
    width: 92%;
    background-color: #fff;
    margin: 20rpx auto;
    border-radius: 20rpx;
    padding: 20rpx 0;
    .title-text {
      padding: 0 20rpx;
      span {
        font-size: 32rpx;
      }
      label {
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
        span {
          color: #999;
        }
        label {
          margin-left: 40rpx;
          color: #333;
          font-weight: 400;
        }
      }
    }
  }
  .detail {
    display: flex;
    height: 100rpx;
    margin: 0 30rpx;
    line-height: 100rpx;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #8799a3;
      font-size: 26rpx;
    }
    .title-input {
      flex: 1;
      text-align: right;
      height: 100rpx;
      line-height: 100rpx;
    }
    span {
      flex: 1;
      text-align: right;
      font-size: 30rpx;
    }
  }
  .foot-text {
    width: 92%;
    background-color: #fff;
    margin: 20rpx auto;
    border-radius: 20rpx;
    padding: 20rpx 20rpx;
    font-size: 24rpx;
  }
  .btn {
    width: 90%;
    margin: 40rpx auto;
  }
}
</style>
