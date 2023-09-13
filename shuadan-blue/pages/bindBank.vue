<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('bindBank')"
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
      <view class="title">{{$t('personalInformation') }}</view>
      <view class="from-input">
        <label>{{$t('realName') }}</label>
        <input
          type="text"
          v-model="userData.realName"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noRealName')"
        />
      </view>
      <view class="from-input">
        <label>{{$t('phoneNo') }}</label>
        <input
          type="text"
          v-model="userData.phone"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noPhone')"
        />
      </view>
      <view class="title">{{$t('cardDetails') }}</view>
      <view class="from-input">
        <label>{{$t('bankNo') }}</label>
        <input
          type="text"
          v-model="userData.bankNo"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noBankNo')"
        />
      </view>
      <view class="from-input">
        <label>{{$t('bankName') }}</label>
        <input
          type="text"
          v-model="userData.bankName"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noBankName')"
        />
      </view>
      <view class="from-input">
        <label>{{$t('addr') }}</label>
        <input
          type="text"
          v-model="userData.bankAddr"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noAddr')"
        />
      </view>
      <view class="btn">
        <u-button v-if="bindStatus" class="custom-style" color="#9d9d9c" block>
          {{$t('modifyService') }}
        </u-button>
        <u-button
          v-else-if="!bindStatus"
          class="custom-style"
          color="#2f3848"
          block
          @click="submit"
          :loading="loading"
        >
        {{$t('confirm') }}
        </u-button>
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
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {
    this.getInfo();
  },
  methods: {
    submit() {
      if (!this.userData.realName) {
        return this.$base.show(this.$t('noRealName'));
      } else if (!this.userData.phone) {
        return this.$base.show(this.$t('noPhone'));
      } else if (!this.userData.bankNo) {
        return this.$base.show(this.$t('noBankNo'));
      } else if (!this.userData.bankName) {
        return this.$base.show(this.$t('noBankName'));
      } else if (!this.userData.bankAddr) {
        return this.$base.show(this.$t('noAddr'));
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
      uni.showLoading({
        title: this.$t('loading'),
      });
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
  .title {
    padding: 0 12rpx;
    background-color: #eee;
    font-size: 27rpx;
    line-height: 75rpx;
    color: #333;
  }
  .from-input {
    margin: 0 40rpx;
    display: flex;
    height: 90rpx;
    line-height: 90rpx;
    font-size: 28rpx;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .input-text {
      flex: 1;
      height: 90rpx;
      line-height: 90rpx;
      font-size: 28rpx;
    }
  }
  .btn {
    width: 90%;
    margin: 40rpx auto;
  }
}
</style>
