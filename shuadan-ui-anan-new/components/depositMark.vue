<template>
  <view>
    <u-modal
      :show="show"
      width="95vw"
      :showConfirmButton="false"
      :showCancelButton="false"
    >
      <view class="flex2">
        <view class="message_block">
          <view class="message_info_content">
            <image class="image" src="@/static/img/pass.png" mode="widthFix" />
            <view class="box"> {{ $t("input_code_pwd") }} </view>
            <u-input
              placeholder=" "
              border="none"
              v-model="pwd"
              @confirm="submit"
            >
            </u-input>
            <view class="btn" @click="submit">
              {{ $t("confirmText") }}
            </view>
          </view>
        </view>
        <u-icon
          @click="show = false"
          class="close"
          size="70rpx"
          color="#fff"
          name="close-circle"
        ></u-icon>
      </view>
    </u-modal>
    <view class="maskLoading" v-if="loading">
      <view class="content">
        <image class="img" src="@/static/img/10001.gif" mode="widthFix" />
        <p class="txt">{{ $t("load_more") }}</p>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      show: false,
      amount: "",
      balance: "",
      pwd: "",
      loading: false,
    };
  },
  methods: {
    open(e) {
      this.amount = e.amount;
      this.balance = e.balance;
      this.show = true;
    },
    submit() {
      if (!this.amount) {
        return this.$base.show(this.$t("input_balance"));
      } else if (!this.pwd) {
        return this.$base.show(this.$t("input_capital_pwd"));
      } else if (this.amount > this.balance) {
        return this.$base.show(this.$t("input_exceed_tatol") + this.balance);
      }
      this.loading = true;
      setTimeout(() => {
        const DATA_OBJ = {
          amount: this.amount, //提现金额
          pwd: this.pwd, //密码
        };
        this.$api
          .user_withdraw(DATA_OBJ)
          .then((res) => {
            if (res.data.code == 0) {
              this.$base.show(res.data.msg);
              this.$emit("ok");
              this.clear();
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }, 2000);
    },
    clear() {
      this.amount = "";
      this.pwd = "";
      this.balance = "";
      this.loading = false;
      this.show = false;
    },
  },
};
</script>

<style scoped lang="scss">
.maskLoading {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 99999;
  .content {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }
  .img {
    width: 150rpx;
    height: 150rpx;
  }
  .txt {
    color: #999;
    font-size: 24rpx;
  }
}
/deep/ .u-popup__content {
  background: transparent;
}
/deep/ .u-modal__content {
  padding: 0 20rpx;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  box-shadow: 0 0 16rpx 0 #ffb400b2;
  border-radius: 40rpx;
  width: 100%;
  background-color: #fff;
  position: relative;
  color: #343434;
  .u-input {
    padding: 20rpx 0rpx 44rpx !important;
    width: 100%;
    /deep/.uni-input-input {
      box-shadow: 0px 4px 4px 0px #00000040 inset;
      color: #343434;
      text-align: center;
    }
    /deep/.u-input__content__field-wrapper__field {
      height: 80rpx !important;
    }
  }
  .message_info_content {
    border-radius: 20rpx;
    position: relative;
    padding: 32rpx 48rpx 64rpx;
    z-index: 2;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    .image {
      width: 96rpx;
    }
    .box {
      padding: 32rpx 0;
      font-size: 32rpx;
    }
    .button {
      width: 80%;
      border-radius: 100rpx;
    }
  }
}
.flex2 {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
}
.close {
  margin-top: 80rpx;
}
.btn{
  font-size: 32rpx;
  text-align: center;
  background-color: #7d7d7d;
  border-radius: 10rpx;
  height: 72rpx;
  line-height: 72rpx;
  color: #fff;
  width: 100%;
}
</style>
