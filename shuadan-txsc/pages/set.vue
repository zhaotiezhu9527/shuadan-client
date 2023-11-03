<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('personalInformation')"
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
        <label>{{$t('avatar') }}</label>
        <image class="user-img" :src="userData.avatarUrl" />
        <view class="icon"></view>
      </view>
      <!-- <view
        class="list-item"
        @click="goRealName"
      >
        <image class="icon-img" src="../static/img/real_name.png" />
        <label>{{$t('nickName') }}</label>
        <span class="item-text">{{ userData.nickName }}</span>
        <view class="icon"></view>
      </view> -->
      <view
        class="list-item"
      >
        <image class="icon-img" src="../static/img/user_name.png" />
        <label>{{$t('mine') }}{{$t('userName') }}</label>
        <span class="item-text">{{ userData.userName }}</span>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goBindBank"
      >
        <image class="icon-img" src="../static/img/my_card.png" />
        <label>{{$t('mine') }}{{$t('bankCard') }}</label>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goPassword"
      >
        <image class="icon-img" src="../static/img/my_password.png" />
        <label>{{$t('passwordManagement') }}</label>
        <view class="icon"></view>
      </view>
      <view
        class="list-item"
        @click="goPayPassword"
      >
        <image class="icon-img" src="../static/img/pay_password.png" />
        <label>{{$t('payPasswordManagement') }}</label>
        <view class="icon"></view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userData:{
        nickName: "",//昵称
        avatarUrl: "",//头像
        balance: "",//余额
        creditValue: "",//信用分
        inviteCode: "",//邀请码
        userName: "",//用户名
        levelName: "",//会员名称
        levelIcon: "",//
      }
    };
  },
  onShow() {
    this.getInfo()
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
