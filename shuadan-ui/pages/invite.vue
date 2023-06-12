<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="我要分享"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      height="50px"
      class="nav-custom"
    >
    </u-navbar>
    <view class="main">
      <view class="head"> </view>
      <view class="ewmstyle">
        <view class="ewm">
          <uqrcode
            ref="uqrcode"
            canvas-id="qrcode"
            :value="`${api}/#/pages/register?code=${infos.inviteCode}`"
          ></uqrcode>
        </view>
      </view>
      <view class="btnstyle">
        <u-button color="#2f3848" text="复制链接" @click="change"></u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      ewm: "",
      infos: {},
      api: "",
    };
  },
  async onLoad() {
    await this.$onLaunched;
    this.api = uni.getStorageSync("config").webDomain;
  },
  onShow() {
    this.getInfo();
  },
  methods: {
    //用户列表数据
    getInfo() {
      uni.showLoading({
        title: "加载中",
      });
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.infos = res.data.data;
        }
      });
    },
    change() {
      uni.setClipboardData({
        data: `${this.api}/#/pages/register?code=${this.infos.inviteCode}`,
      });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  .head {
    width: 100%;
    height: 372rpx;
    background: url(../static/img/msg.png) no-repeat;
    background-size: 100% 100%;
    overflow: hidden;
  }
}
.nav-custom {
  color: #fff;
  font-weight: 500;
  font-size: 32rpx;
  background-image: linear-gradient(
    135deg,
    #f9b821 10%,
    #fbe750 100%
  ) !important;
}
.ewmstyle {
  padding-top: 50rpx;
  .ewm {
    width: 400rpx;
    height: 400rpx;
    border-radius: 10rpx;
    margin: 0 auto;
    border: 2rpx solid #eee;
  }
}
.btnstyle {
  width: 80%;
  padding-top: 50rpx;
  margin: 0 auto;
}
</style>
