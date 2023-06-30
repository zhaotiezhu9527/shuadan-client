<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="个人信息"
      fixed
      safe-area-inset-top
      class="linear"
      bgColor="transparent"
      :background="navBackground"
      leftIconColor="#000"
      leftIconSize="32"
      height="100rpx"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view class="list-item" @click="goHeadimg">
        <label>头像</label>
        <image class="user-img" :src="userData.avatarUrl" />
        <view class="icon"></view>
      </view>
      <view class="list-item" @click="goRealName">
        <label>全民中文名</label>
        <span class="item-text">{{ userData.nickName }}</span>
        <view class="icon"></view>
      </view>
      <view class="list-item">
        <label>全民用户名</label>
        <span class="item-text">{{ userData.userName }}</span>
        <view class="icon"></view>
      </view>
    </view>
    <view class="list margin20">
      <view class="list-item" @click="goBindBank">
        <label>我的银行卡</label>
        <view class="icon"></view>
      </view>
    </view>
    <view class="list">
      <view class="list-item" @click="goPassword">
        <label>密码管理</label>
        <view class="icon"></view>
      </view>
      <view class="list-item" @click="goPayPassword">
        <label>交易密码</label>
        <view class="icon"></view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userData: {
        nickName: "", //昵称
        avatarUrl: "", //头像
        balance: "", //余额
        creditValue: "", //信用分
        inviteCode: "", //邀请码
        userName: "", //用户名
        levelName: "", //会员名称
        levelIcon: "", //
      },
      navBackground: {
        backgroundColor: "#001f3f",
        backgroundImage: "linear-gradient(127deg, #F8A057 -1%, #FFC860 59%);",
      },
    };
  },
  onShow() {
    this.getInfo();
  },
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
    goBindBank() {
      uni.navigateTo({
        url: "/pages/bindBank",
      });
    },
    goPassword() {
      uni.navigateTo({
        url: "/pages/password",
      });
    },
    goPayPassword() {
      uni.navigateTo({
        url: "/pages/payPassword",
      });
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData = res.data.data;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.list {
  width: 100%;
  border-radius: 20rpx;
  color: #333;
  .list-item {
    display: flex;
    align-items: center;
    vertical-align: middle;
    font-size: 33rpx;
    padding: 44rpx;
    border-bottom: 1px solid #eee;
    background-color: #fff;
    border-radius: 20rpx;
    // &:last-child {
    //   border: none;
    // }
    .icon-img {
      width: 40rpx;
      height: 40rpx;
      margin-right: 20rpx;
    }
    .user-img {
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
    .item-text {
      margin-right: 20rpx;
    }
  }
}
</style>
