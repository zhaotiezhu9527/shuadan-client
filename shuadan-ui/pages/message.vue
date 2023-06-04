<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="信息公告"
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
      <view class="head">
      </view>
      <view class="content">
        <view
          class="list-item"
        >
          <image class="icon-img" src="../static/img/msg_notice.png" />
          <label>系统公告</label>
          <view class="icon"></view>
        </view>
        <view class="text">
          <view class="notice">公告1</view>
          <view class="prompt">温馨提示：</view>
          <view class="details">
            您好！欢迎来到全民任务APP，
            本平台是一个第三方智能阿里云匹配订单平台，
            平台与各大网购平台商家合作，（
            对接淘宝、天猫、京东、唯品会、拼多多、等等一系列网购平台商家）
            通过智能系统将商家订单匹配给平台用户，
            平台每天都会将需要提高曝光度的商家产品放到平台上，
            提供给平台用户进行抢单。为了规避网购平台的检测，
            提高订单的真实性，新用户在抢单前必须先完善个人真实信息，
            并填写真实的收货地址。 
            为了有效的帮助商家提升订单成功率借此提高各大商家在各大平台的销售量和信誉度，
            传入智能阿里云主机，即可由智能系统匹配订单！
            （温馨提示：注：本平台不会以任何形式向平台内所有会员收取任何费用，
             提现由系统审核通过后2小时内到账。感谢您对本平台的支持与厚爱，祝您抢单愉快！）
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {},
};
</script>

<style scoped lang="scss">
.main{
  .head{
    width: 100%;
    height: 372rpx;
    background: url(../static/img/msg.png) no-repeat;
    background-size: 100% 100%;
    overflow: hidden;
  }
  .content{
    position: relative;
    background-color: #f8f4f4;
    width: 90%;
    border-radius: 20rpx;
    padding: 56rpx 30rpx;
    left: 5%;
    top: -40rpx;
    .list-item {
      display: flex;
      align-items: center;
      vertical-align: middle;
      font-size: 28rpx;
      border-bottom: 1px solid #eee;
      background-color: #fff;
      padding: 24rpx;
      border-radius: 8rpx;
      box-shadow: 0 3rpx 5rpx rgba(0, 0, 0, 0.1);
      margin-bottom: 18rpx;
      .icon-img {
        width: 40rpx;
        height: 40rpx;
        margin-right: 20rpx;
      }
      label {
        flex: 1;
      }
      .icon {
        width: 10px;
        height: 10px;
        position: relative;
        &:after {
          content: "";
          width: 8px;
          height: 8px;
          position: absolute;
          right: 5px;
          bottom: 5px;
          border-left: 1px solid #aaa;
          border-bottom: 1px solid #aaa;
          transform: translate(0, 50%) rotate(-135deg);
          -webkit-transform: translate(0, 50%) rotate(-135deg);
        }
      }
    }
    .text{
      font-size: 28rpx;
      border-bottom: 1px solid #eee;
      background-color: #fff;
      padding: 24rpx;
      border-radius: 8rpx;
      .details{
        font-size: 24rpx;
      }
    }
  }
}

</style>
