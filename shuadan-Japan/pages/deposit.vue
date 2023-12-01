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
      <!-- <view class="main-type">
        <view class="type-item type-item-left"
              :class="typeActive === 1 ? 'active' : ''" @click="typeActive = 1">{{ $t('bankWithdrawal') }}</view>
        <view class="type-item"
              :class="typeActive === 2 ? 'active' : ''"
              @click="typeActive = 2"
        >{{ $t('USDT') }}</view>
      </view> -->
      <view class="card-num" @click="show = true">
        <view class="flex">
          {{ title }}
          <u-icon class="icon" color="#000" size="14" name="arrow-down-fill" />
        </view>
      </view>
      <view class="title">
        <view class="title-text">
          <span>{{ $t("withdrawalAmount") }}</span>
          <label>{{ $t("withdrawalRate") }}{{ withdrawFee }}%</label>
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
            <span v-if="typeActive === 2"> ≈ {{ Number((Number(amount) * Number(huilv))).toFixed(2)}} USDT</span>
          </view>
          <view class="text">
            <span v-if="typeActive === 1">{{ $t("canAmount") }}：{{ FormatAmount(balance) }}</span>
            <span v-if="typeActive === 2">{{ $t("canAmount") }}：{{ FormatAmount(balance) }}</span>
            <!-- <span v-if="typeActive === 2">{{ $t("canAmount") }}：{{ FormatAmount(Number(balance) * Number(huilv))}} USDT</span> -->
            <!-- <label @click="amount = balance">{{ $t("withdrawAll") }}</label> -->
          </view>
        </view>
      </view>
      <view class="content">
        <view class="item">
          <!-- <view class="detail">
            <label>{{$t('phoneNo') }}</label>
            <span>{{ phone }}</span>
          </view> -->
          <view class="detail" v-if="typeActive === 1">
            <label>{{ $t("bankNo") }}</label>
            <span>{{ bankNo }}</span>
          </view>
          <view class="detail" v-if="typeActive === 1">
            <label>{{ $t("bankName") }}</label>
            <span>{{ bankName }}</span>
          </view>
          <view class="detail" v-if="typeActive === 2">
            <label>{{ $t("USDT") }}</label>
            <span>{{ walletAddr }}</span>
          </view>
          <view class="detail" v-if="typeActive === 1">
            <label>{{ $t("realName") }}</label>
            <span>{{ realName }}</span>
          </view>
          <view class="detail" v-if="typeActive === 2">
            <label>{{ $t("withdrawalNetwork") }}</label>
            <span> TRC-20 </span>
          </view>
          <view class="detail">
            <label>{{ $t("payPassword") }}</label>
            <input class="title-input" v-model="pwd" type="number" />
            <!-- :placeholder="$t('enterPayPassword')" -->
          </view>
        </view>
      </view>
      <view class="foot-text">
        *<br />
        {{ $t("checkInfo") }}<br />
        {{ $t("handlingFee") }} {{ withdrawFee }}%
      </view>
      <view class="btn">
        <u-button
          class="custom-style"
          color="#4350af"
          block
          @click="submit"
          :loading="loading"
        >
          {{ $t("withdrawImmediately") }}
        </u-button>
      </view>
    </view>
    <u-picker
      :title="$t('withdrawalMethod')"
      :show="show"
      show-toolbar
      :style="{ height: '50%' }"
      :columns="columns"
      itemHeight="90"
      confirmColor="#4b80af"
      @confirm="onConfirm"
      @cancel="show = false"
      :confirmText="$t('confirm')"
      :cancelText="$t('quxiao')"
    />
  </view>
</template>

<script>
import { FormatAmount } from "@/plugins/util";
export default {
  data() {
    return {
      FormatAmount,
      amount: "",
      loading: false,
      phone: "", //手机号
      bankNo: "", //银行卡号
      bankName: "", //银行名称
      realName: "", //真实姓名
      pwd: "", //支付密码
      balance: 0, //余额
      withdrawFee: 0, //提款手续费
      typeActive: 2,
      walletAddr: "",//钱包地址
      huilv: 0,//汇率
      title: this.$t('USDT'),
      show: false,
      columns: [
        [
          { text: this.$t('USDT'), value: 2 },
          { text: this.$t('bankWithdrawal'), value: 1 },
        ],
      ],
    };
  },
  onShow() {
    this.getInfo();
    this.getConfig();
  },
  methods: {
    submit() {
      if (!this.amount) {
        return this.$base.show(this.$t("enterWithdrawalAmount"));
      } else if (!this.pwd) {
        return this.$base.show(this.$t("enterPayPassword"));
      } else if (this.amount > this.balance) {
        return this.$base.show(this.$t("NotExceed") + this.balance);
      }
      this.loading = true;
      const DATA_OBJ = {
        amount: this.amount, //提现金额
        pwd: this.pwd, //密码
        type: this.typeActive,
      };
      this.$api
        .user_withdraw_jp(DATA_OBJ)
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
    getConfig() {
      this.$api.system_config().then(({ data }) => {
        if (data.code == 0) {
          this.huilv = data.data.huilv;
        }
      });
    },
    onConfirm(e) {
      this.title = e.value[0].text;
      this.typeActive = e.value[0].value;
      this.show = false;
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  background-color: #f2f2f2;
  height: calc(100vh - 52px + var(--status-bar-height));
  overflow: hidden;
  .main-type{
    display: flex;
    width: 92%;
    margin: 30rpx auto 0 auto;
    .type-item{
      width: 45%;
      height: 80rpx;
      text-align:center;
      line-height: 80rpx;
      background-color: #fff;
      border-radius: 20rpx;
      &.active{
        background-color: #443ca7;
        color: #fff;
      }
    }
    .type-item-left{
      margin-right: 10%;
    }
  }
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
      font-size: 24rpx;
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
      font-size: 24rpx;
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
.card-num {
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    padding-left: 15rpx;
    background-color: #fff;
    margin-top: 20rpx;
    box-sizing: border-box;
    label {
      margin-left: 20rpx;
      width: calc(100% - 180rpx);
    }
    .flex {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .icon {
      padding-right: 30rpx;
    }
  }
</style>
