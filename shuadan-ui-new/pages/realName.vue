<template>
  <view class="wapper">
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
      <view class="btn">
        <!-- <u-button
              class="custom-style"
              color="#2f3848"
              block
              @click="submit"
              :loading="loading"
            >
              
            </u-button> -->
            确认修改
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      nickName:'',//真实姓名
      loading: false,//
    };
  },
  onShow() {
    this.getInfo()
  },
  methods: {
    submit(){
      this.loading = true
      const DATA_OBJ = {
        nickName: this.nickName,
      };
      this.$api
        .update_nickName(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg)
            this.loading = false
          }
        })
        .finally(() => {
          this.loading = false
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
.title{
  padding: 0 80rpx;
  background-color: #eee;
  font-size: 27rpx;
  line-height: 75rpx;
  color: #333;
}
.from-input {
    margin: 40rpx;
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
  .btn{
    width: 388rpx;
    height: 66rpx;
    border-radius: 45rpx;
    opacity: 1;
    background: #FFB550;
    line-height: 66rpx;
    text-align: center;
    font-size: 36rpx;
    font-weight: 500;
    color: #FFFFFF;
    margin: 40rpx auto 0 auto; 
  }
</style>
