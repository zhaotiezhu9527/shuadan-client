<template>
  <view>
    <view class="main">
      <image class="top-bg" src="@/static/img/topMask.png" />
      <!-- 头部 -->
      <view class="title">
        <view class="user">
          <view class="head">
            <image class="head-img" :src="userData.avatarUrl" />
          </view>
          <view class="information">
            <view class="level">
              <view class="name">{{ userData.nickName }}</view>
              <view class="code">
                邀请码: {{ userData.inviteCode }}
                <label class="copy-text" @click="copy(userData.inviteCode)"
                  >复制</label
                ></view
              >
              <image class="level-img" :src="userData.levelIcon" />
              <label class="credit">信用分:{{ userData.creditValue }}</label>
            </view>
          </view>
        </view>
        <view class="money">
          <view class="balance">
            <view>账户金额</view>
            <view class="money-color"
              >¥<label class="money-number">{{ userData.balance }}</label></view
            >
          </view>
          <view class="withdrawal" @click="goDeposit('/pages/deposit')"
            >提现</view
          >
          <view class="recharge" @click="goDeposit('/pages/recharge')"
            >充值</view
          >
          <image class="money-img" src="@/static/img/Vector.png" />
        </view>
      </view>
      <!-- 其他功能文字 -->
      <view class="other-text">其他功能</view>
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
      <view class="logout" @click="loginoutShow = true"> 退出登陆 </view>
      <view class="bottom-bg"> </view>
      <u-modal
        :show="loginoutShow"
        title="退出登陆"
        @confirm="loginOut"
        @cancel="loginoutShow = false"
        ref="uModal"
        :asyncClose="true"
        showCancelButton
        confirmColor="#4b80af"
      >
        <view class="content">你确定退出吗？</view>
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
          label: "抢单记录",
          icon: "rob.png",
          url: "/pages/index?tabs=1",
        },
        {
          label: "账户明细",
          icon: "list.png",
          url: "/pages/accountDetails",
        },
        {
          label: "充值记录",
          icon: "recharge.png",
          url: "/pages/rechargeRecord",
        },
        {
          label: "提现记录",
          icon: "withdrawal.png",
          url: "/pages/withdrawalRecords",
        },
        {
          label: "个人信息",
          icon: "user.png",
          url: "/pages/set",
        },
        {
          label: "邀请好友",
          icon: "friend.png",
          url: "/pages/promotion",
        },
        {
          label: "信息公告",
          icon: "message.png",
          url: "/pages/message",
        },
        {
          label: "团队报表",
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
      if (label === "抢单记录") {
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
        this.$base.show("请先绑定银行卡");
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
    // 复制功能
    copy(value) {
      uni.setClipboardData({
        data: value, //要被复制的内容
        success: () => {
          //复制成功的回调函数
          uni.showToast({
            //提示
            title: "复制成功",
          });
        },
      });
    },
  },
};
</script>

<style scoped lang="scss">
.title {
  width: 100%;
  height: calc(500rpx + var(--status-bar-height));
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
        width: 168rpx;
        height: 168rpx;
        display: block;
        border-radius: 50%;
        border: 2rpx solid #fff;
        box-sizing: border-box;
      }
    }
    .information {
      float: left;
      margin-left: 34rpx;
      .level {
        overflow: hidden;
        .name {
          font-size: 52rpx;
          color: #333;
          font-weight: bold;
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
        .copy-text {
          font-size: 24rpx;
          color: #ffb24e;
          padding-left: 8px;
        }
      }
    }
  }
  .money {
    width: 680rpx;
    height: 200rpx;
    background: linear-gradient(255deg, #ffd86f 1%, #ffa23f 97%);
    border-radius: 20rpx;
    position: relative;
    margin: 54rpx auto auto;
    padding: 0 32rpx;
    color: #fff;
    .balance {
      margin-top: 24rpx;
      display: block;
      float: left;
      .money-color {
        font-size: 24rpx;
        margin-top: 40rpx;
        .money-number {
          font-weight: 700;
          font-size: 44rpx;
          margin-left: 10rpx;
        }
      }
    }
    .recharge {
      float: right;
      width: 152rpx;
      height: 66rpx;
      text-align: center;
      line-height: 66rpx;
      background: #ffffff;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      font-size: 36rpx;
      font-weight: 500;
      color: #f78944;
      border-radius: 33rpx;
      margin: 20rpx 0 0 32rpx;
    }
    .withdrawal {
      float: right;
      width: 152rpx;
      height: 66rpx;
      text-align: center;
      line-height: 66rpx;
      background: #ffffff;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      font-size: 36rpx;
      font-weight: 500;
      color: #f78944;
      border-radius: 33rpx;
      margin: 20rpx 0 0 32rpx;
    }
    .money-img {
      width: 180rpx;
      height: 180rpx;
      position: absolute;
      right: 0;
      bottom: -46%;
      opacity: 0.3;
    }
  }
}
.content {
  background: #fff;
  overflow: hidden;
  width: 92%;
  margin: auto;
  border-radius: 20rpx;
  padding: 20rpx 0;
  .item {
    float: left;
    width: 25%;
    box-sizing: border-box;
    text-align: center;
    padding: 10rpx;
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
  width: 388rpx;
  height: 66rpx;
  border-radius: 45rpx;
  opacity: 1;
  background: #ffb550;
  line-height: 66rpx;
  text-align: center;
  font-size: 36rpx;
  font-weight: 500;
  color: #ffffff;
  margin: 120rpx auto 0 auto;
}
.top-bg {
  position: fixed;
  left: -20%;
  top: -12%;
  width: 150%;
  height: 320px;
  opacity: 1;
  z-index: -1;
}
.other-text {
  width: 680rpx;
  margin: auto;
  font-size: 32rpx;
}
.bottom-bg {
  position: fixed;
  border-radius: 100%;
  left: -40%;
  top: 50%;
  width: 1440rpx;
  height: 1440rpx;
  transform: rotate(151.42deg);
  opacity: 1;
  z-index: -1;
  background: linear-gradient(
    222deg,
    #fedfc9 8%,
    rgba(254, 224, 202, 0.21) 69%,
    rgba(254, 224, 202, 0.21) 75%
  );
}
</style>
