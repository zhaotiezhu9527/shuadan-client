<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="修改名称"
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
      <view class="from-input">
        <label>名字：</label>
        <input
          type="text"
          v-model="nickName"
          class="input-text"
          placeholder="请输入您的名称"
        />
      </view>
      <view class="title">请修改您的名称</view>
      <view class="btn"> 确认修改 </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      nickName: "", //真实姓名
      loading: false, //
    };
  },
  onShow() {
    this.getInfo();
  },
  methods: {
    submit() {
      this.loading = true;
      const DATA_OBJ = {
        nickName: this.nickName,
      };
      this.$api
        .update_nickName(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
            this.loading = false;
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
          this.nickName = res.data.data.nickName;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.title {
  padding: 0 80rpx;
  color: #666;
  font-size: 27rpx;
  line-height: 75rpx;
}
.from-input {
  margin: 40rpx 40rpx 10rpx;
  display: flex;
  height: 90rpx;
  background-color: #fff;
  line-height: 90rpx;
  border-radius: 20rpx;
  label {
    width: 100rpx;
    margin-right: 20rpx;
    color: #999;
    margin-left: 40rpx;
  }
  .input-text {
    flex: 1;
    height: 90rpx;
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
  margin: 40rpx auto 0 auto;
}
</style>
