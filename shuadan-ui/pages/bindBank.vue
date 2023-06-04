<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="绑定银行卡"
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
      <view class="title">您的身份信息</view>
      <view class="from-input">
        <label>真实姓名</label>
        <input
          type="text"
          v-model="realName"
          class="input-text"
          placeholder="请输入您的真实姓名"
        />
      </view>
      <view class="from-input">
        <label>联系电话</label>
        <input
          type="text"
          v-model="phone"
          class="input-text"
          placeholder="请输入您的手机号码"
        />
      </view>
      <view class="title">您的银行卡信息</view>
      <view class="from-input">
        <label>银行卡号</label>
        <input
          type="text"
          v-model="card"
          class="input-text"
          placeholder="请输入您的真实姓名"
        />
      </view>
      <view class="from-input">
        <label>银行名称</label>
        <input
          type="text"
          v-model="bankName"
          class="input-text"
          placeholder="请输入您的手机号码"
        />
      </view>
      <view class="from-input">
        <label>开户行</label>
        <input
          type="text"
          v-model="detail"
          class="input-text"
          placeholder="请输入您的手机号码"
        />
      </view>
      <view class="from-input">
        <label>支行地址</label>
        <input
          type="text"
          v-model="addr"
          class="input-text"
          placeholder="请输入您的手机号码"
        />
      </view>
      <view class="btn">
        <u-button
              class="custom-style"
              color="#2f3848"
              block
              @click="submit"
              :loading="loading"
            >
              确认修改
            </u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      realName:'',//真实姓名
      loading: false,//
      phone:'',//手机
      card:'',//银行卡号
      bankName:'',//银行名称
      detail:'',//开户行
      addr:''//支行地址
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
.title{
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
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .input-text {
      flex: 1;
      height: 90rpx;
      line-height: 90rpx;
    }
  }
  .btn{
    width: 90%;
    margin: 40rpx auto;
  }
</style>
