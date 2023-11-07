<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('acc_pwd')"
      fixed
      leftIconColor="#ffffff"
      bgColor="#1e1e1e"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="title">{{ $t("input_my_new_pwd") }}</view>
      <view class="from-input">
        <label>{{ $t("former_pwd") }}：</label>
        <input
          type="number"
          v-model="oldPwd"
          class="input-text"
          :placeholder="$t('input_former_pwd')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("new_pwd") }}：</label>
        <input
          type="number"
          v-model="newPwd"
          class="input-text"
          :placeholder="$t('input_new_pwd')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("config_pwd") }}：</label>
        <input
          type="number"
          v-model="password"
          class="input-text"
          :placeholder="$t('input_config_pwd')"
        />
      </view>
      <view class="pour">{{ $t("pwd_info") }}</view>
      <view class="password-btn" @click="submit">
        {{ $t("condig_edit") }}
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
        return this.$base.show(this.$t("sex_pwd"));
      } else if (!pay.test(this.newPwd) || this.newPwd.length !== 6) {
        return this.$base.show(this.$t("input_new_pwd_edit"));
      } else if (this.newPwd != this.password) {
        return this.$base.show(this.$t("input_tow_pwd_edit"));
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
.main {
  min-height: calc(100vh - 100rpx);
}
.title {
  padding: 0 40rpx;
  font-size: 24rpx;
  line-height: 75rpx;
  color: #999;
}
.from-input {
  padding: 0 40rpx;
  background-color: #1e1e1e;
  color: #ddd;
  display: flex;
  height: 90rpx;
  line-height: 90rpx;
  label {
    width: 250rpx;
    margin-right: 20rpx;
    color: #ddd;
    text-align: right;
    font-size: 24rpx;
  }
  .input-text {
    flex: 1;
    height: 90rpx;
    line-height: 90rpx;
    font-size: 28rpx;
  }
}
</style>
