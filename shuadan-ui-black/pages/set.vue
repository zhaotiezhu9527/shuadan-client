<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('userset')"
      fixed
      safe-area-inset-top
      bgColor="#000000"
      leftIconColor="#ffffff"
      leftIconSize="32"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <view class="list-item" @click="goHeadimg">
        <label>{{ $t("avatar_name") }}</label>
        <image class="user-img" :src="userData.avatarUrl" />
        <view class="icon"></view>
      </view>
      <view
        class="list-item margin-top10 border-bottom393939"
        @click="goRealName"
      >
        <label>{{ $t("nickName_all") }}</label>
        <span class="item-text">{{ userData.nickName }}</span>
        <view class="icon"></view>
      </view>
      <view class="list-item">
        <label>{{ $t("userName_all") }}</label>
        <span class="item-text">{{ userData.userName }}</span>
        <view class="icon"></view>
      </view>
    </view>
    <!-- <view class="list margin-top10 margin-bottom10">
      <view class="list-item" @click="goBindBank">
        <label>{{ $t("bindBank_all") }}</label>
        <view class="icon"></view>
      </view>
    </view> -->
    <view class="list margin-top10 margin-bottom10">
      <view class="list-item" @click="goUSDT">
        <label>{{ $t("USDTAddr") }}</label>
        <view class="icon"></view>
      </view>
    </view>
    <view class="list">
      <view class="list-item border-bottom393939" @click="goPassword">
        <label>{{ $t("pwd") }}</label>
        <view class="icon"></view>
      </view>
      <view class="list-item" @click="goPayPassword">
        <label>{{ $t("pwd_acc_all") }}</label>
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
    goUSDT(){
      uni.navigateTo({
        url: "/pages/bindUSDT",
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
  .list-item {
    display: flex;
    align-items: center;
    vertical-align: middle;
    font-size: 33rpx;
    padding: 18rpx 44rpx;
    background-color: #1e1e1e;
    color: #dedede;
    // &:last-child {
    //   border: none;
    // }
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
