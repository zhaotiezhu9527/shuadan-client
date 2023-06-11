<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="密码管理"
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
      <view class="title">请输入您的新密码</view>
      <view class="from-input">
        <label>旧密码</label>
        <input
          type="text"
          v-model="oldPwd"
          class="input-text"
          placeholder="请输入旧密码"
        />
      </view>
      <view class="from-input">
        <label>新密码</label>
        <input
          type="text"
          v-model="newPwd"
          class="input-text"
          placeholder="请输入新密码"
        />
      </view>
      <view class="from-input">
        <label>确认密码</label>
        <input
          type="text"
          v-model="password"
          class="input-text"
          placeholder="请再次输入密码"
        />
      </view>
      <view class="pour">请牢记密码,如忘记密码,请联系客服。</view>
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
.btn {
  width: 90%;
  margin: 40rpx auto;
}
.pour {
  margin: 50rpx 50rpx;
  color: #e83323;
}
</style>
