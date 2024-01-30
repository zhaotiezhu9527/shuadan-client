<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('pwd')"
      fixed
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="from-input">
        <label>{{ $t("former_pwd") }}：</label>
        <input
          type="text"
          v-model="oldPwd"
          class="input-text"
          :placeholder="$t('input_former_pwd')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("new_pwd") }}：</label>
        <input
          type="text"
          v-model="newPwd"
          class="input-text"
          :placeholder="$t('input_new_pwd')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("config_pwd") }}：</label>
        <input
          type="text"
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
        return this.$base.show(this.$t("former_pwd_no"));
      } else if (!this.newPwd) {
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
  min-height: calc(100vh - 100rpx);
}
.title {
  padding: 0 40rpx;
  font-size: 24rpx;
  line-height: 75rpx;
  color: #999;
}
.from-input {
  color: #ddd;
  display: flex;
  height: 100rpx;
  line-height: 100rpx;
  border-radius: 10rpx;
  border: 1px solid #575651;
  width: 95%;
  margin: 14rpx auto;
  background-color: #fff;
  label {
    width: 260rpx;
    margin-right: 20rpx;
    color: #353535;
    font-size: 24rpx;
    text-indent: 20rpx;
    font-weight: 600;
  }
  .input-text {
    flex: 1;
    height: 100rpx;
    line-height: 100rpx;
    font-size: 24rpx;
    color: #696969;
  }
}
</style>
