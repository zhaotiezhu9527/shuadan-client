<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('payPasswordManagement')"
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
<!--      <view class="title">{{$t('enterNewPassword') }}</view>-->
      <view class="from-input">
        <label>{{$t('oldPassword') }}</label>
        <input
          type="number"
          v-model="oldPwd"
          class="input-text"
          :placeholder="$t('enterOldPassword')"
        />
      </view>
      <view class="from-input">
        <label>{{$t('newPassword') }}</label>
        <input
          type="number"
          v-model="newPwd"
          class="input-text"
          :placeholder="$t('enterNewPassword')"
        />
      </view>
      <view class="from-input">
        <label>{{$t('confirmPassword') }}</label>
        <input
          type="number"
          v-model="password"
          class="input-text"
          :placeholder="$t('enterConfirmPassword')"
        />
      </view>
      <view class="pour">{{$t('passwordService') }}</view>
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
      oldPwd: "", //真实姓名
      loading: false, //
      newPwd: "", //手机
      password: "", //银行卡号
    };
  },
  methods: {
    submit() {
      let pay = /^(?:0|(?:-?[1-9]\d*))$/;
      if (!pay.test(this.oldPwd) || this.oldPwd.length !== 6) {
        return this.$base.show(this.$t('payPasswordLength'));
      } else if (!pay.test(this.newPwd) || this.newPwd.length !== 6) {
        return this.$base.show(this.$t('enterNewPassword'));
      } else if (this.newPwd != this.password) {
        return this.$base.show(this.$t('passwordDifferent'));
      }
      this.loading = true;
      const DATA_OBJ = {
        newPwd: this.newPwd, //新密码
        oldPwd: this.oldPwd, //旧密码
      };
      this.$api
        .updatePayPwd(DATA_OBJ)
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
  font-size: 24rpx;
  label {
    width: 220rpx;
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
.pour {
  margin: 50rpx 50rpx;
  color: #e83323;
}
</style>
