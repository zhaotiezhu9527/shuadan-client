<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('deposit')"
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
      <!-- 银行卡信息 -->
      <!-- <view class="link">
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
      </view> -->
      <!-- 钱包地址 -->
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
          <!-- <template #prefix>
            <view class="rmb">{{ $t("rmb_icon") }}</view>
          </template> -->
        </u-input>
        <view class="text">
          <text>{{ $t("usable") }}：
            <!-- {{ $t("rmb_icon") }} -->
            {{ balance }}</text>
          <text @click="amount = balance">{{ $t("all_deposit") }}</text>
        </view>
      </view>
      <view class="foot-text">
        {{ $t("withdrawFee_info") }}<br />
        {{ $t("withdrawFee") }} {{ withdrawFee }}%
      </view>
      <view class="btn">
        <u-button class="button" block @click="submit" :loading="loading">
          {{ $t("config_deposit") }}
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
        height: 80rpx !important;
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
