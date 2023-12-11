<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('share')"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="boxStyle">
      <view class="box">
        <view class="rwm">
          <uqrcode
            ref="uqrcode"
            canvas-id="qrcode"
            :value="`${api}/#/pages/register?code=${infos.inviteCode}`"
            size="150"
          ></uqrcode>
        </view>
        <view class="txt">{{$t('referralCode') }}:{{ infos.inviteCode }}</view>
      </view>
      <view style="padding-top: 40rpx">
        <u-button color="#89bfdb" :text="$t('inviteFriends')" @click="change"></u-button>
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
<style lang="scss" scoped>
.main {
  height: 100vh;
  background: linear-gradient(1turn, #d5dde8, #c9d1dd);
  .boxStyle {
    padding: 100rpx 30rpx 0;
  }
  .box {
    padding: 100rpx 20rpx;
    background: #fff;
    border-radius: 20rpx;
    box-shadow: 0 0 10rpx 0 #eee;
    .rwm {
      border: 1rpx solid #eee;
      width: 300rpx;
      height: 300rpx;
      margin: 0 auto;
    }
    .txt {
      text-align: center;
      font-size: 26rpx;
      padding-top: 40rpx;
    }
  }
}
</style>
