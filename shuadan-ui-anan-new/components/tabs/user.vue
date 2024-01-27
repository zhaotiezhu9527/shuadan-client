<template>
  <view>
    <view class="main">
      <u-navbar
        placeholder
        :border="false"
        :title="$t('centre')"
        fixed
        safe-area-inset-top
        leftIconSize="32"
        bgColor="#fff"
        height="100rpx"
        titleStyle="color:#000;font-weight:600;font-size:32rpx;"
      >
      </u-navbar>
      <view class="title">
        <view class="user">
          <image class="head-img" mode="whiteFix" :src="userData.avatarUrl" />
          <view class="name">{{ userData.userName }}</view>
          <view class="information">
            <view class="flex items-center justify-center">
              <image class="level-img" :src="userData.levelIcon" />
              <label class="credit"
                >{{ $t("creditValue") }}:{{ userData.creditValue }}</label
              >
            </view>
            <view class="code">
              {{ $t("inviteCode") }}: {{ userData.inviteCode }}
              <label class="copy-text" @click="copy(userData.inviteCode)">
                {{ $t("copy") }}
              </label>
            </view>
          </view>
        </view>
        <view class="price">
          <view class="price-box">
            <view class="flex">
              <view class="money2">
                <view class="title">
                  {{ $t("account_balance") }}（{{ $t("rmb") }}）
                </view>
                <view class="txt">{{ Number(userData.balance).toFixed(2) }}</view>
              </view>
              <view class="btn">
                <view @click="goDeposit('/pages/recharge')">
                  {{ $t("recharge") }}
                </view>
                <view class="active" @click="goDeposit('/pages/deposit')">
                  {{ $t("deposit") }}
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <!-- 导航 -->
      <view class="content">
        <view
          class="item"
          v-for="(item, index) in list"
          :key="index"
          @click="goPage(item)"
          :class="{ mb: item.className }"
        >
          <view class="item-img flex items-center justify-center">
            <image
              :style="{
                width: item.width + 'rpx',
              }"
              :src="'../static/img/' + item.icon"
              mode="widthFix"
            />
          </view>
          <view class="text">{{ item.label }}</view>
          <image
              class="user-jiaobiao"
              src="@/static/img/jiaobiao.png"
              mode="widthFix"
            />
        </view>
      </view>
      <u-modal
        :show="loginoutShow"
        :title="$t('renturn_login')"
        @confirm="loginOut"
        @cancel="loginoutShow = false"
        ref="uModal"
        :asyncClose="true"
        showCancelButton
        confirmColor="#6c38ed"
        cancelColor="#ffffff"
        :confirmText="$t('confirmText')"
        :cancelText="$t('cancelText')"
      >
        <view class="btn">{{ $t("loginout") }}</view>
      </u-modal>
    </view>
  </view>
</template>

<script>
export default {
  props:['userData'],
  data() {
    return {
      loading: false, //加载状态
      list: [
        {
          label: this.$t("userset"),
          icon: "user-icon1.png",
          url: "/pages/set",
          width: "36",
        },
        {
          label: this.$t("USDTAddr"),
          icon: "user-icon2.png",
          url: "/pages/bindUSDT",
          className: "mb",
          width: "40",
        },
        {
          label: this.$t("recharge_log"),
          icon: "user-icon3.png",
          url: "/pages/rechargeRecord",
          width: "40",
        },
        {
          label: this.$t("deposit_log"),
          icon: "user-icon4.png",
          url: "/pages/withdrawalRecords",
          width: "40",
        },

        {
          label: this.$t("order_log"),
          icon: "user-icon5.png",
          url: "/pages/accountDetails",
          className: "mb",
          width: "36",
        },
        {
          label: this.$t("service"),
          icon: "user-icon6.png",
          url: "/pages/onlineService",
          width: "36",
        },
        {
          label: this.$t("notice"),
          icon: "user-icon7.png",
          url: "/pages/message",
          width: "40",
        },
        {
          label: this.$t("renturn_login"),
          icon: "user-icon9.png",
          url: "",
          width: "32",
          className: "mb",
        },
      ],
      loginoutShow: false,
      config: {},
    };
  },
  onShow() {
    this.getConfig();
  },
  methods: {
    open() {
      this.getConfig()
    },
    loginOut() {
      this.$api.user_logout().then((res) => {
        if (res.data.code == 0) {
          uni.removeStorage({
            key: "token",
            success: function (res) {
              uni.redirectTo({ url: "/pages/login" });
            },
          });
        }
      });
    },
    goPage({ label, url }) {
      if (label === this.$t("renturn_login")) {
        this.loginoutShow = true;
      } else if (label === this.$t("service")) {
        if(window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))
        {
          uni.navigateTo({
            url: "/pages/onlineService"
          });
        }else{
          // #ifdef APP-PLUS
          plus.runtime.openURL(this.config.onlineService);
          // #endif
          // #ifdef H5
          window.open(this.config.onlineService);
          // #endif
        }
      }
      else {
        uni.navigateTo({
          url: url,
        });
      }
    },
    goDeposit(url) {
      if (!this.userData.walletAddr && url == "/pages/deposit") {
        this.$base.show(this.$t("bindUSDTAddr"));
      } else {
        uni.navigateTo({
          url,
        });
      }
    },
    // 复制功能
    copy(value) {
      uni.setClipboardData({
        data: value,
        success: () => {
          uni.showToast({
            title: this.$t("copy_success"),
          });
        },
      });
    },
    getConfig(){
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
        } 
      });
    },
  },
};
</script>

<style scoped lang="scss">
.main{
  width: 90%;
  margin: auto;
}
.title {
  .user {
    padding: 58rpx 32rpx 0 32rpx;
    color: #000;
    .head-img {
      width: 120rpx;
      height: 120rpx;
      display: block;
      border-radius: 50%;
      border: 1rpx solid #fff;
      box-sizing: border-box;
      margin: auto;
    }
    .name {
      font-size: 36rpx;
      font-weight: 500;
      text-align: center;
    }
    .information {
      margin-left: 16rpx;
      
      .level-img {
        float: left;
        height: 36rpx;
        width: 96rpx;
        margin: 0 16rpx;
      }
      .credit {
        background: #7d7d7d;
        height: 37.5rpx;
        line-height: 37.5rpx;
        font-size: 24rpx;
        padding: 0rpx 8rpx;
        border-radius: 4rpx;
        color: #fff;
      }
      .code {
        margin-top: 14rpx;
        font-size: 26rpx;
        text-align: center;
        .copy-text {
          font-size: 24rpx;
          padding-left: 16rpx;
        }
      }
    }
  }
  .money {
    width: 680rpx;
    height: 200rpx;
    background: linear-gradient(255deg, #ffd86f 1%, #ffa23f 97%);
    border-radius: 20rpx;
    position: relative;
    margin: 54rpx auto auto;
    padding: 0 32rpx;
    color: #fff;
    .balance {
      margin-top: 24rpx;
      display: block;
      float: left;
      .money-color {
        font-size: 24rpx;
        margin-top: 40rpx;
        .money-number {
          font-weight: 700;
          font-size: 44rpx;
          margin-left: 10rpx;
        }
      }
    }
    .recharge {
      float: right;
      width: 152rpx;
      height: 66rpx;
      text-align: center;
      line-height: 66rpx;
      background: #ffffff;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      font-size: 36rpx;
      font-weight: 500;
      color: #f78944;
      border-radius: 33rpx;
      margin: 20rpx 0 0 32rpx;
    }
    .withdrawal {
      float: right;
      width: 152rpx;
      height: 66rpx;
      text-align: center;
      line-height: 66rpx;
      background: #ffffff;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      font-size: 36rpx;
      font-weight: 500;
      color: #f78944;
      border-radius: 33rpx;
      margin: 20rpx 0 0 32rpx;
    }
    .money-img {
      width: 180rpx;
      height: 180rpx;
      position: absolute;
      right: 0;
      bottom: -46%;
      opacity: 0.3;
    }
  }
}
.price {
  margin: 16rpx 0;
  border-bottom: 4rpx solid #eee;
  color: #000;
  .money2 {
    .title {
      font-size: 24rpx;
      font-weight: 500;
    }
    .txt {
      font-weight: 600;
      font-size: 58rpx;
      padding-top: 20rpx;
      font-weight: bold;
    }
  }
  .price-box {
    padding: 40rpx 32rpx;
    .flex {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .btn {
        display: flex;
        align-items: center;
        gap: 8rpx;
        justify-content: space-between;
        view {
          display: flex;
          align-items: center;
          justify-content: center;
          // width: 104rpx;
          height: 64rpx;
          padding: 0 20rpx;
          border-radius: 16rpx;
          background: #7d7d7d;
          color: $white;
          font-size: 24rpx;
        }
      }
    }
  }
}
.content {
  padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
  .item {
    display: flex;
    align-items: center;
    padding: 20rpx 28rpx;
    background-color: #fff;
    position: relative;
    margin-bottom: 0;
    color: #000;
    border: 2rpx solid #575651;
    border-radius: 10rpx;
    margin: 16rpx 0;
    &::before {
      content: "";
      background: rgba($white, 0.12);
      height: 1rpx;
      width: 90%;
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      bottom: 0;
      z-index: 1;
    }
    &.mb {
      margin-bottom: 16rpx;
      &::before {
        display: none;
      }
    }
    // &:after {
    //   content: "";
    //   width: 18rpx;
    //   height: 18rpx;
    //   position: absolute;
    //   right: 30rpx;
    //   top: 50%;

    //   border-left: 2rpx solid #aaa;
    //   border-bottom: 2rpx solid #aaa;
    //   transform: translateY(-50%) rotate(-135deg);
    // }
    .item-img {
      width: 40rpx;
      .image {
        width: 100%;
      }
    }
  }
  .text {
    font-size: 32rpx;
    margin-left: 16rpx;
    flex: 1;
  }
  .user-jiaobiao{
    width: 28rpx;
    margin-right: 2rpx;
  }
}
/deep/.u-popup__content {
  background: #1e1e1e;
  box-shadow: 0 0 16rpx 0 #ffb400b2;
  .u-modal__title {
    color: $white;
  }
}
.btn {
  padding: 0 20rpx;
  color: $white;
}
</style>
