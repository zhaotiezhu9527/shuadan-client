<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('USDTAddr')"
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
      <view class="title">{{ $t("enterUsdt") }}</view>
      <view class="input-box">
        <view class="from-input border-bottom393939">
          <!-- <label>{{ $t("realName") }}</label> -->
          <input
            type="text"
            v-model="userData.realName"
            :disabled="bindStatus"
            class="input-text"
            :placeholder="$t('realName')"
          />
        </view>
        <view class="from-input">
          <!-- <label>{{ $t("realName") }}</label> -->
          <input
            type="text"
            v-model="userData.addr"
            :disabled="bindStatus"
            class="input-text"
            :placeholder="$t('enterUsdt')"
          />
        </view>
      </view>
      <view class="btn">
        <view v-if="bindStatus" class="bank-btn"> {{ $t("edit") }} </view>
        <view v-else-if="!bindStatus" @click="submit">
          {{ $t("confirmText") }}
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
        addr: "", //usdt地址
        realName: "",//真实姓名
      },
      bindStatus: true, //绑定状态
    };
  },
  onLoad() {},
  onShow() {
    this.getInfo();
  },
  methods: {
    submit() {
      if (!this.userData.addr) {
        return this.$base.show(this.$t("enterUsdt"));
      } else if(!this.userData.realName) {
        return this.$base.show(this.$t("input_realName"));
      } 
      this.loading = true;
      const DATA_OBJ = {
        addr: this.userData.addr, //真实姓名
        realName: this.userData.realName, //真实姓名
      };
      this.$api
        .bindUsdt(DATA_OBJ)
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
          this.userData.addr = res.data.data.walletAddr;
          this.userData.realName = res.data.data.realName;
          if (this.userData.addr === null || !this.userData.addr) {
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
  .input-box{
    width: 92%;
    margin: auto;
    border-radius: 10rpx;
    background-color: #fff;
  }
  .from-input {
    margin: 0 40rpx;
    display: flex;
    height: 112rpx;
    line-height: 112rpx;
    font-size: 32rpx;
    color: #353535;
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
      text-align: left;
    }
  }
  .btn {
    width: 92%;
    margin: auto;
    height: 104rpx;
    line-height: 104rpx;
    background-color: #575651;
    border-radius: 10rpx;
    text-align: center;
    color: #fff;
    margin-top: 36rpx;
  }
}
</style>
