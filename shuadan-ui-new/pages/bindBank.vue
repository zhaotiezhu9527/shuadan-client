<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="绑定银行卡"
      fixed
      leftIconColor="#000"
      class="linear"
      bgColor="transparent"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="title">您的身份信息</view>
      <view class="from-input">
        <label>真实姓名</label>
        <input
          type="text"
          v-model="userData.realName"
          :disabled="bindStatus"
          class="input-text"
          placeholder="未输入真实姓名，请设置"
        />
      </view>
      <view class="from-input">
        <label>联系电话</label>
        <input
          type="text"
          v-model="userData.phone"
          :disabled="bindStatus"
          class="input-text"
          placeholder="未输入电话，请设置"
        />
      </view>
      <view class="title">您的银行卡信息</view>
      <view class="from-input">
        <label>银行卡号</label>
        <input
          type="text"
          v-model="userData.bankNo"
          :disabled="bindStatus"
          class="input-text"
          placeholder="未输入银行卡号，请设置"
        />
      </view>
      <view class="from-input">
        <label>银行名称</label>
        <input
          type="text"
          v-model="userData.bankName"
          :disabled="bindStatus"
          class="input-text"
          placeholder="未输入银行名称，请设置"
        />
      </view>
      <view class="from-input">
        <label>支行地址</label>
        <input
          type="text"
          v-model="userData.bankAddr"
          :disabled="bindStatus"
          class="input-text"
          placeholder="未输入支行地址，请设置"
        />
      </view>
      <view class="btn">
        <u-button
          v-if="bindStatus"
          shape="circle"
          class="custom-style"
          color="#9d9d9c"
          block
        >
          修改信息联系客服
        </u-button>
        <view v-else-if="!bindStatus" @click="submit"> 设置信息 </view>
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
        return this.$base.show("真实姓名不能为空~");
      } else if (!this.userData.phone) {
        return this.$base.show("请填写联系电话~");
      } else if (!this.userData.bankNo) {
        return this.$base.show("请填写银行卡号~");
      } else if (!this.userData.bankName) {
        return this.$base.show("请填写银行名称~");
      } else if (!this.userData.bankAddr) {
        return this.$base.show("请填写支行地址~");
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
        title: "加载中",
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
  background-color: #f2f2f2;
  min-height: calc(100vh - 100rpx);
  .title {
    padding: 0 40rpx;
    background-color: #eee;
    font-size: 27rpx;
    line-height: 75rpx;
    color: #333;
  }
  .from-input {
    padding: 0 40rpx;
    display: flex;
    height: 112rpx;
    line-height: 112rpx;
    font-size: 36rpx;
    background-color: #fff;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .input-text {
      flex: 1;
      height: 112rpx;
      line-height: 112rpx;
      font-size: 36rpx;
      text-align: right;
    }
  }
  .btn {
    width: 388rpx;
    height: 66rpx;
    border-radius: 45rpx;
    opacity: 1;
    background: #ffb550;
    line-height: 66rpx;
    text-align: center;
    font-size: 36rpx;
    font-weight: 500;
    color: #ffffff;
    margin: 140rpx auto 0 auto;
  }
}
</style>
