<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('edit_name')"
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
      <view class="from-input">
        <label>{{ $t("name") }}：</label>
        <input
          type="text"
          v-model="nickName"
          class="input-text"
          :placeholder="$t('input_name')"
        />
      </view>
      <view class="title">{{ $t("edit_my_name") }}</view>
      <view class="btn" @click="submit"> {{ $t("condig_edit") }} </view>
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
  padding: 0 40rpx;
  background-color: #1e1e1e;
  color: #ddd;
  display: flex;
  height: 90rpx;
  line-height: 90rpx;
  margin-top: 20rpx;
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
  width: 90%;
  height: 80rpx;
  border-radius: 45rpx;
  opacity: 1;
  line-height: 78rpx;
  text-align: center;
  font-size: 36rpx;
  font-weight: 500;
  color: #ddd;
  margin: 40rpx auto 0 auto;
  border: 1px solid #666;
}
</style>
