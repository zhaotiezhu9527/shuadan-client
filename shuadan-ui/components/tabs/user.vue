<template>
  <view>
    <view class="main">
      <!-- 头部 -->
      <view class="title">
        <view class="icon">
          <image
            class="icon-img"
            src="@/static/img/notice.png"
            @click="goPage({ label: '', url: list[6].url })"
          />
          <image
            class="icon-img box-right"
            src="@/static/img/setting.png"
            @click="goPage({ label: '', url: list[0].url })"
          />
        </view>
        <view class="user">
          <view class="head">
            <image class="head-img" :src="userData.avatarUrl" />
          </view>
          <view class="information">
            <view class="level">
              <label class="name">{{ userData.nickName }}</label>
              <!-- <label class="grade">{{ userData.levelName }}</label> -->
              <image class="level-img" :src="userData.levelIcon" />
              <label class="credit">{{$t('credit') }}:{{ userData.creditValue }}</label>
            </view>
            <view class="code"> {{$t('referralCode') }}: {{ userData.inviteCode }} </view>
          </view>
        </view>
        <view class="money">
          <view class="balance">
            <view>{{$t('accountBalance') }}</view>
            <view class="money-color"
              >
              <!-- ¥ -->
              <label class="money-number">{{ userData.balance }}</label></view
            >
          </view>
          <view class="withdrawal" @click="goDeposit('/pages/deposit')"
            >{{$t('withdraw') }}</view
          >
          <view class="recharge" @click="goDeposit('/pages/recharge')"
            >{{$t('recharge') }}</view
          >
        </view>
        <view class="money-bottom"></view>
      </view>
      <!-- 导航 -->
      <view class="content">
        <view
          class="item"
          v-for="(item, index) in list"
          :key="index"
          @click="goPage(item)"
        >
          <image class="item-img" :src="'../static/img/' + item.icon" />
          <view class="text">{{ item.label }}</view>
        </view>
      </view>
      <!-- 退出登陆 -->
      <view class="logout">
        <u-button
          class="custom-style"
          color="#2f3848"
          block
          @click="loginoutShow = true"
          :loading="loading"
        >
        {{$t('logout') }}
        </u-button>
      </view>
      <u-modal
        :show="loginoutShow"
        :title="$t('logout')"
        @confirm="loginOut"
        @cancel="loginoutShow = false"
        ref="uModal"
        :asyncClose="true"
        showCancelButton
        confirmColor="#4b80af"
      >
        <view class="content">{{$t('confirmLogout') }}</view>
      </u-modal>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false, //加载状态
      list: [
        {
          label: this.$t('personalInformation'),
          icon: "user.png",
          url: "/pages/set",
        },
        {
          label: this.$t('financialHistory'),
          icon: "rob.png",
          url: "/pages/index?tabs=1",
        },
        {
          label: this.$t('accountDetails'),
          icon: "list.png",
          url: "/pages/accountDetails",
        },
        {
          label: this.$t('rechargeRecord'),
          icon: "recharge.png",
          url: "/pages/rechargeRecord",
        },
        {
          label: this.$t('withdrawalsRecord'),
          icon: "withdrawal.png",
          url: "/pages/withdrawalRecords",
        },
        {
          label: this.$t('inviteFriends'),
          icon: "friend.png",
          url: "/pages/promotion",
        },
        {
          label: this.$t('systemInformation'),
          icon: "message.png",
          url: "/pages/message",
        },
        {
          label: this.$t('teamReports'),
          icon: "team.png",
          url: "/pages/team",
        },
      ],
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
      loginoutShow: false,
      bindStatus: false, //银行卡绑定状态
    };
  },
  methods: {
    open() {
      this.getInfo();
    },
    loginOut() {
      this.$api.user_logout().then((res) => {
        if (res.data.code == 0) {
          uni.removeStorage({
            key: "token",
            success: function (res) {
              uni.redirectTo({ url: "/pages/login" });
            },
          });
        }
      });
    },
    goPage({ label, url }) {
      if (label === this.$t('financialHistory')) {
        uni.reLaunch({
          url: url,
        });
      } else {
        uni.navigateTo({
          url: url,
        });
      }
    },
    goDeposit(url) {
      if (this.bindStatus) {
        uni.navigateTo({
          url,
        });
      } else {
        this.$base.show(this.$t('cardMsg'));
      }
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData = res.data.data;
          if (this.userData.bankNo === null || !this.userData.bankNo) {
            this.bindStatus = false;
          } else {
            this.bindStatus = true;
          }
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.title {
  width: 100%;
  height: calc(440rpx + var(--status-bar-height));
  background: url(@/static/img/title.png) no-repeat;
  background-size: 100% 100%;
  overflow: hidden;
  padding-top: var(--status-bar-height);
  position: relative;
  .icon {
    margin-top: 20rpx;
    height: 40rpx;
    padding: 0 40rpx;
    display: flex;
    .icon-img {
      width: 44rpx;
      height: 44rpx;
    }
  }
  .user {
    margin-top: 50rpx;
    padding-left: 40rpx;
    overflow: hidden;
    .head {
      float: left;
      .head-img {
        width: 100rpx;
        height: 100rpx;
        display: block;
        border-radius: 50%;
        border: 2rpx solid #fff;
        box-sizing: border-box;
      }
    }
    .information {
      float: left;
      margin-left: 24rpx;
      .level {
        overflow: hidden;
        .name {
          font-size: 36rpx;
          color: #171717;
          font-weight: bold;
          float: left;
        }
        // .grade{
        //   float: left;
        //   background: rgba(0, 0, 0, .2);
        //   color: #fff;
        //   height: 32rpx;
        //   border-radius: 4rpx;
        //   font-size: 24rpx;
        //   margin-left: 12rpx;
        //   padding: 0 4rpx;
        //   margin-top: 8rpx;
        // }
        .level-img {
          float: left;
          color: #fff;
          height: 36rpx;
          width: 96rpx;
          margin-left: 12rpx;
          margin-top: 8rpx;
        }
        .credit {
          float: left;
          background-color: #00ced1;
          height: 37.5rpx;
          line-height: 37.5rpx;
          margin-left: 12rpx;
          margin-top: 8rpx;
          color: #fff;
          font-size: 24rpx;
          padding: 0rpx 8rpx;
          border-radius: 4rpx;
        }
      }
      .code {
        margin-top: 14rpx;
        font-size: 26rpx;
        color: #434343;
      }
    }
  }
  .money {
    width: 660rpx;
    height: 160rpx;
    background: url("@/static/img/money.png") no-repeat;
    background-size: 100% 100%;
    margin: 54rpx auto auto;
    padding: 0 32rpx;
    .balance {
      color: hsla(0, 0%, 100%, 0.6);
      margin-top: 24rpx;
      display: block;
      float: left;
      .money-color {
        color: #f2d8be;
        font-size: 24rpx;
        margin-top: 15rpx;
        .money-number {
          font-weight: 700;
          font-size: 44rpx;
          margin-left: 10rpx;
        }
      }
    }
    .recharge {
      float: right;
      width: 116rpx;
      height: 56rpx;
      text-align: center;
      line-height: 56rpx;
      background: hsla(0, 0%, 100%, 0.2);
      color: #fff;
      border-radius: 28rpx;
      margin: 54rpx 0 0 32rpx;
      font-size: 28rpx;
    }
    .withdrawal {
      float: right;
      width: 116rpx;
      height: 56rpx;
      text-align: center;
      line-height: 56rpx;
      background: hsla(0, 0%, 100%, 0.2);
      color: #fff;
      border-radius: 28rpx;
      margin: 54rpx 0 0 32rpx;
      font-size: 28rpx;
    }
  }
  .money-bottom {
    width: 100%;
    height: 69rpx;
    background: url("@/static/img/money_bottom.png") no-repeat;
    background-size: 100%;
    position: absolute;
    bottom: 0;
  }
}
.content {
  border-top: 2rpx solid #f2f2f2;
  background: #fff;
  overflow: hidden;
  .item {
    float: left;
    width: 33.33%;
    box-sizing: border-box;
    border-right: 2rpx solid #eee;
    border-bottom: 2rpx solid #eee;
    text-align: center;
    padding: 42rpx 0 50rpx;
    .item-img {
      width: 78rpx;
      height: 78rpx;
    }
    .text {
      color: #333;
      margin-top: 10rpx;
      font-size: 28rpx;
    }
  }
}
.logout {
  padding: 0 26rpx;
  padding-bottom: calc(160rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
  .custom-style {
    border-radius: 10rpx;
    margin-bottom: 20rpx;
    font-size: 32rpx;
    height: 88rpx;
    margin-top: 40rpx;
  }
}
</style>
