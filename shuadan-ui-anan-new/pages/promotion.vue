<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('promotion')"
      fixed
      leftIconColor="#ffffff"
      bgColor="#1e1e1e"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="boxStyle">
      <view class="box">
        <image class="image" src="/static/img/bg-017.png" mode="widthFix" />
        <view class="rwmTxt">
          <view class="rwm">
            <uqrcode
              ref="uqrcode"
              canvas-id="qrcode"
              :value="`${api}/#/pages/login?code=${infos.inviteCode}`"
              size="62"
            ></uqrcode>
          </view>
          <view class="txt">{{ $t("inviteCode") }}:{{ infos.inviteCode }}</view>
        </view>
      </view>
      <view class="btn">
        <u-button
          :text="$t('invitation')"
          @click="change"
          class="button"
        ></u-button>
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
  onShow() {
    this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
        this.api = data.data.webDomain;
      }
    });
    this.getInfo();
  },
  methods: {
    //用户列表数据
    getInfo() {
      uni.showLoading({
        title: this.$t("load_more"),
      });
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.infos = res.data.data;
        }
      });
    },
    change() {
      uni.setClipboardData({
        data: `${this.api}/#/pages/login?code=${this.infos.inviteCode}`,
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main {
  background-color: #280c43;
  .boxStyle {
    padding: 48rpx 0;
  }
  .box {
    padding: 0 48rpx;
    position: relative;
    .image {
      width: 100%;
    }
    .rwmTxt {
      position: absolute;
      bottom: 88rpx;
      left: 50%;
      transform: translateX(-50%);
    }
    .rwm {
      background-color: $white;
      padding: 10rpx;
      width: 72px;
      height: 72px;
      margin: 0 auto;
      box-sizing: border-box;
    }
    .txt {
      text-align: center;
      font-size: 32rpx;
      padding-top: 32rpx;
      color: $white;
    }
  }
}
.btn {
  padding: 48rpx 48rpx 0;
  .button {
    border-radius: 100rpx;
  }
}
</style>
