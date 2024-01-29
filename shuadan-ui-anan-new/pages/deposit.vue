<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('deposit')"
      fixed
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="link">
        <view class="flex">
          <image
            class="image"
            src="@/static/img/usdt.png"
            mode="widthFix"
          />
          <view class="box">
            <view>{{ $t("USDTAddr") }}</view>
            <view>{{ walletAddr }}</view>
          </view>
        </view>
      </view>
      <view class="content">
        <view class="title">
          <view class="name">{{ $t("recharge_acc") }}</view>
          <view class="rate" @click="submit"
            >{{ $t("deposit_rate") }} {{ withdrawFee }} %</view
          >
        </view>
        <u-input
          placeholder="USDT"
          border="bottom"
          placeholderClass="placeholder"
          v-model="amount"
          clearable
          type="number"
          @blur="update"
        >
        </u-input>
        <view class="text">
          <text>{{ $t("usable") }}：
            {{ Number(balance).toFixed(2) }}</text>
          <text @click="amount = balance">{{ $t("all_deposit") }}</text>
        </view>
      </view>
      <view class="foot-text">
        {{ $t("withdrawFee_info") }}<br />
        {{ $t("withdrawFee") }} {{ withdrawFee }}%
      </view>
      <view class="tixian-btn" @click="submit">
        {{ $t("config_deposit") }}
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
      walletAddr: "",//usdt钱包地址
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
        return this.$base.show(this.$t("input_balance"));
      } else if (this.amount > this.balance) {
        return this.$base.show(this.$t("input_exceed_tatol") + this.balance);
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
          this.walletAddr = res.data.data.walletAddr;
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
  width: 95%;
  margin: auto;
  .link {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24rpx 24rpx;
    margin-top: 16rpx;
    background-color: #fff;
    color: #696969;
    border: 1px solid #575651;
    border-radius: 10rpx;
    .image {
      width: 58rpx;
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
      }
      view:nth-child(2) {
        font-size: 24rpx;
      }
    }
  }
  .box {
    view:nth-child(1) {
      font-size: 32rpx;
    }
    view:nth-child(2) {
      font-size: 24rpx;
    }
  }
  .content {
    margin-top: 16rpx;
    padding: 24rpx 32rpx;
    background-color: #fff;
    color: #696969;
    border: 1px solid #575651;
    border-radius: 10rpx;
    .title {
      padding-bottom: 24rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .name {
        font-size: 32rpx;
        font-weight: 600;
      }
      .rate {
        border: 1rpx solid #575651;
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
      /deep/.uni-input-input {
        height: 80rpx !important;
        font-size: 64rpx;
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
    }
    .placeholder {
      font-size: 64rpx;
      font-weight: 500;
      height: 80rpx !important;
      display: flex;
      align-items: center;
    }
    .text {
      padding: 26rpx 0 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      text {
        font-size: 28rpx;
      }
    }
  }

  .foot-text {
    margin: 20rpx auto;
    background-color: #fff;
    color: #696969;
    border: 1px solid #575651;
    border-radius: 10rpx;
    padding: 24rpx;
    font-size: 24rpx;
    line-height: 2;
  }
  .tixian-btn {
    height: 104rpx;
    line-height: 104rpx;
    background-color: #7d7d7d;
    border-radius: 10rpx;
    text-align: center;
    color: #fff;
  }
}
</style>
