<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('userset')"
      fixed
      safe-area-inset-top
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view class="list-box">
        <view class="list-item" @click="goHeadimg">
          <label>{{ $t("avatar_name") }}</label>
          <image class="user-img" :src="userData.avatarUrl" />
          <view class="icon"></view>
        </view>
      </view>
      <view class="list-box margin-top30">
        <view class="list-item border-bottom393939">
          <label>{{ $t("userName_all") }}</label>
          <span class="item-text">{{ userData.userName }}</span>
        </view>
        <view class="list-item border-bottom393939">
          <label>{{ $t("realName") }}</label>
          <span class="item-text">{{ userData.realName }}</span>
        </view>
        <view class="list-item">
          <label>{{ $t("phoneNumber") }}</label>
          <span class="item-text">{{ userData.phone }}</span>
        </view>
      </view>
    </view>
    <view class="list">
      <view class="list-box">
        <view class="list-item margin-top30 border-bottom393939" @click="goPassword">
          <label>{{ $t("pwd") }}</label>
          <view class="icon"></view>
        </view>
        <view class="list-item" @click="goPayPassword">
          <label>{{ $t("pwd_acc_all") }}</label>
          <view class="icon"></view>
        </view>
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
        realName: "",//全名
        phone: "",//手机号码
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
  width: 92%;
  margin: auto;
  border-radius: 20rpx;
  margin-top: 30rpx;
  .list-box{
    border-radius: 10rpx;
    border: 1px solid #575651;
    background-color: #fff;
  }
  .list-item {
    display: flex;
    align-items: center;
    vertical-align: middle;
    font-size: 33rpx;
    margin: 0 44rpx;
    color: #353535;
    padding: 18rpx 0;
    .icon-img {
      width: 40rpx;
      height: 40rpx;
      margin-right: 20rpx;
    }
    .user-img {
      width: 60rpx;
      height: 60rpx;
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
