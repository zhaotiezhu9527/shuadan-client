<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="修改名称"
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
      <view class="title">请修改您的名称</view>
      <view class="from-input">
        <label>姓名</label>
        <input
          type="text"
          v-model="realName"
          class="input-text"
          placeholder="请输入您的名称"
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
    margin: 40rpx;
    display: flex;
    height: 90rpx;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .input-text {
      flex: 1;
    }
  }
  .btn{
    width: 90%;
    margin: auto;
  }
</style>
