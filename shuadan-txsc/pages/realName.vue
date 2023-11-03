<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('nickName')"
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
      <view class="title">{{$t('modifyNickname') }}</view>
      <view class="from-input">
        <label>{{$t('nickName') }}</label>
        <input
          type="text"
          v-model="nickName"
          class="input-text"
          :placeholder="$t('enterNickName')"
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
