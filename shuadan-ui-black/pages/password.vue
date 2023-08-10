<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="密码管理"
      fixed
      leftIconColor="#ffffff"
      bgColor="#000000"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="title">请输入您的新密码</view>
      <view class="from-input">
        <label>旧密码：</label>
        <input
          type="text"
          v-model="oldPwd"
          class="input-text"
          placeholder="请输入旧密码"
        />
      </view>
      <view class="from-input">
        <label>新密码：</label>
        <input
          type="text"
          v-model="newPwd"
          class="input-text"
          placeholder="请输入新密码"
        />
      </view>
      <view class="from-input">
        <label>确认密码：</label>
        <input
          type="text"
          v-model="password"
          class="input-text"
          placeholder="请再次输入密码"
        />
      </view>
      <view class="pour">请牢记密码,如忘记密码,请联系客服。</view>
      <view class="btn" @click="submit"> 确认修改 </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      oldPwd: "", //旧密码
      loading: false, //
      newPwd: "", //新密码
      password: "", //确认密码
    };
  },
  onShow() {},
  methods: {
    submit() {
      if (!this.oldPwd) {
        return this.$base.show("旧密码不能为空~");
      } else if (!this.newPwd) {
        return this.$base.show("请填写新密码~");
      } else if (this.newPwd != this.password) {
        return this.$base.show("两次输入的密码不一致~");
      }
      this.loading = true;
      const DATA_OBJ = {
        newPwd: this.newPwd, //新密码
        oldPwd: this.oldPwd, //旧密码
      };
      this.$api
        .updatePwd(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
            uni.removeStorage({
              key: "token",
              success: function (res) {
                uni.redirectTo({ url: "/pages/login" });
              },
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  background-color: #f2f2f2;
  min-height: calc(100vh - 100rpx);
}
.title {
  padding: 0 40rpx;
  background-color: #eee;
  font-size: 24rpx;
  line-height: 75rpx;
  color: #666;
}
.from-input {
  padding: 0 40rpx;
  background-color: #fff;
  display: flex;
  height: 90rpx;
  line-height: 90rpx;
  label {
    width: 160rpx;
    margin-right: 20rpx;
    color: #666;
    text-align: right;
  }
  .input-text {
    flex: 1;
    height: 90rpx;
    line-height: 90rpx;
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
.pour {
  margin: 30rpx 50rpx 60rpx;
  color: #f78944;
  font-size: 24rpx;
}
</style>
