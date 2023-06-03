<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="个人信息"
      fixed
      safe-area-inset-top
      bgColor="#fff"
      leftIconColor="#666"
      leftIconSize="32"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view
        class="list-item"
        @click="goHeadimg"
      >
        <image class="icon-img" src="../static/img/touxiang.png" />
        <label>头像</label>
        <image class="user-img" src="../static/img/head.png" />
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goRealName"
      >
        <image class="icon-img" src="../static/img/real_name.png" />
        <label>全民中文名</label>
        <span class="item-text">小路</span>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
      >
        <image class="icon-img" src="../static/img/user_name.png" />
        <label>全民用户名</label>
        <span class="item-text">qwewewew</span>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goBindBank"
      >
        <image class="icon-img" src="../static/img/my_card.png" />
        <label>我的银行卡</label>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goPassword"
      >
        <image class="icon-img" src="../static/img/my_password.png" />
        <label>密码管理</label>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goPayPassword"
      >
        <image class="icon-img" src="../static/img/pay_password.png" />
        <label>交易密码</label>
        <view class="icon"></view>
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
  methods: {
    goHeadimg() {
      uni.navigateTo({
        url: "/pages/headimg",
      });
    },
    goRealName() {
      uni.navigateTo({
        url: "/pages/realName",
      });
    },
    goBindBank(){
      uni.navigateTo({
        url: "/pages/bindBank",
      });
    },
    goPassword(){
      uni.navigateTo({
        url: "/pages/password",
      });
    },
    goPayPassword(){
      uni.navigateTo({
        url: "/pages/payPassword",
      });
    },
  },
};
</script>

<style scoped lang="scss">
.list {
    width: 98%;
    margin: auto;
    background-color: #fff;
    border-radius: 20rpx;
    color: #333;
    .list-item {
      display: flex;
      align-items: center;
      vertical-align: middle;
      font-size: 33rpx;
      height: 116rpx;
      margin: 0 30rpx;
      border-bottom: 1px solid #eee;
      // &:last-child {
      //   border: none;
      // }
      .icon-img {
        width: 40rpx;
        height: 40rpx;
        margin-right: 20rpx;
      }
      .user-img{
        width: 90rpx;
        height: 90rpx;
        margin-right: 36rpx;
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
      .item-text{
        font-size: 24rpx;
        color: #999;
        margin-right: 20rpx;
      }
    }
  }
</style>
