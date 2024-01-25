<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('bind')"
      fixed
      leftIconColor="#ffffff"
      bgColor="#1e1e1e"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="title">{{ $t("realName_info") }}</view>
      <view class="from-input border-bottom393939">
        <label>{{ $t("realName") }}</label>
        <input
          type="text"
          v-model="userData.realName"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('input_realName')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("phone") }}</label>
        <input
          type="text"
          v-model="userData.phone"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('input_phone')"
        />
      </view>
      <view class="title">{{ $t("bind_info") }}</view>
      <view class="from-input border-bottom393939">
        <label>{{ $t("bank_code") }}</label>
        <input
          type="text"
          v-model="userData.bankNo"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('input_bink_code')"
        />
      </view>
      <view class="from-input border-bottom393939">
        <label>{{ $t("bink_name") }}</label>
        <input
          type="text"
          v-model="userData.bankName"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('input_bink_name')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("bink_address") }}</label>
        <input
          type="text"
          v-model="userData.bankAddr"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('input_bink_address')"
        />
      </view>
      <view class="btn">
        <view v-if="bindStatus" class="bank-btn"> {{ $t("edit_info") }} </view>
        <view v-else-if="!bindStatus" @click="submit">
          {{ $t("set_info") }}
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false, //
      userData: {
        realName: "", //真实姓名
        phone: "", //手机
        bankNo: "", //银行卡号
        bankName: "", //银行名称
        bankAddr: "", //支行地址
      },
      bindStatus: false, //绑定状态
    };
  },
  onLoad() {},
  onShow() {
    this.getInfo();
  },
  methods: {
    submit() {
      if (!this.userData.realName) {
        return this.$base.show(this.$t("input_realName_edit"));
      } else if (!this.userData.phone) {
        return this.$base.show(this.$t("input_phone_edit"));
      } else if (!this.userData.bankNo) {
        return this.$base.show(this.$t("input_bink_code_edit"));
      } else if (!this.userData.bankName) {
        return this.$base.show(this.$t("input_bink_name_edit"));
      } else if (!this.userData.bankAddr) {
        return this.$base.show(this.$t("input_bink_address_edit"));
      }
      this.loading = true;
      const DATA_OBJ = {
        realName: this.userData.realName, //真实姓名
        phone: this.userData.phone, //手机
        cardNo: this.userData.bankNo, //银行卡号
        bankName: this.userData.bankName, //银行名称
        addr: this.userData.bankAddr, //支行地址
      };
      this.$api
        .bindBank(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
            this.loading = false;
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
          this.userData = res.data.data;
          if (this.userData.bankNo === null || !this.userData.bankNo) {
            this.bindStatus = false;
          } else {
            this.bindStatus = true;
          }
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  min-height: calc(100vh - 100rpx);
  .title {
    padding: 0 40rpx;
    font-size: 27rpx;
    line-height: 75rpx;
    color: #999;
  }
  .from-input {
    padding: 0 40rpx;
    display: flex;
    height: 112rpx;
    line-height: 112rpx;
    font-size: 32rpx;
    background-color: #1e1e1e;
    color: #ddd;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #ddd;
    }
    .input-text {
      flex: 1;
      height: 112rpx;
      line-height: 112rpx;
      font-size: 32rpx;
      text-align: right;
    }
  }
  .btn {
    width: 90%;
    height: 80rpx;
    border-radius: 45rpx;
    opacity: 1;
    line-height: 78rpx;
    text-align: center;
    font-size: 36rpx;
    font-weight: 500;
    color: #ddd;
    margin: 40rpx auto 0 auto;
    border: 1px solid #666;
  }
}
</style>
