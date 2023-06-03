<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="头像设置"
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
        <image class="head-img" src="../static/img/head1.png"/>
        <view class="btn">立即设置</view>
      </view>
      <view class="content">
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/head1.png"/>
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
.head{
  width: 100%;
  height: 252rpx;
  background: url(../static/img/head_title.png) no-repeat;
  background-size: 100% 100%;
  overflow: hidden;
  .head-img{
    display: block;
    width: 120rpx;
    height: 120rpx;
    margin: 30rpx auto 20rpx auto;
  }
  .btn{
    font-size: 30rpx;
    color: #fff;
    border: 1px solid #fff;
    border-radius: 10rpx;
    width: 160rpx;
    text-align: center;
    margin: auto;
  }
}
.content{
  width: 100%;
  .item{
    width: 20%;
    padding: 18rpx;
    float: left;
  }
  .item-img{
    display: block;
    width: 120rpx;
    height: 120rpx;
  }
}
</style>
