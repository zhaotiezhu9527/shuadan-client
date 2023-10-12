<template>
  <view>
    <view class="main">
      <!-- 头部 -->
      <view class="title">
        <view class="user">
          <view class="head">
            <image class="head-img" :src="userData.avatarUrl" />
          </view>
          <view class="information">
            <view class="level">
              <label class="name">{{ userData.nickName }}</label>
              <!-- <label class="grade">{{ userData.levelName }}</label> -->
              <image class="level-img" :src="userData.levelIcon" />
             
            </view>
            <view class="code"> 
              <text>{{$t('referralCode') }}: {{ userData.inviteCode }}</text>
              <label class="credit">{{$t('credit') }}:{{ userData.creditValue }}</label>
            </view>
          </view>
          <!-- <view class="customer-box">
            <image class="customer-img" src="@/static/img/customer.png" />
          </view> -->
        </view>
        <view class="money">
          <view class="balance">
            <view>{{$t('accountBalance') }}</view>
            <view class="money-color"
              >
              <!-- ¥ -->
              <label class="money-number">{{ FormatAmount(userData.balance) }}</label>
            </view>
          </view>
          <view class="withdrawal" @click="goDeposit('/pages/deposit')"
            >{{$t('withdraw') }}</view
          >
          <view class="recharge" @click="goDeposit('/pages/recharge')"
            >{{$t('recharge') }}</view
          >
          <view class="clear-float"></view>
          <view class="income">
            <view class="income-item">
              <view class="income-item-text">{{ $t("userYesterday") }}</view>
              <view class="income-item-number">{{ FormatAmount(infos.yesterdayIncome) }}
              </view>
            </view>
            <view class="income-item">
              <view class="income-item-text">{{ $t("CumulativeIncome") }}</view>
              <view class="income-item-number">{{ FormatAmount(infos.totalIncome) }}
              </view>
            </view>
            <view class="income-item">
              <view class="income-item-text">{{ $t("todayEarnings") }}</view>
              <view class="income-item-number">{{ FormatAmount(infos.todayIncome) }}
              </view>
            </view>
          </view>
        </view>
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
          color="#2E68F2"
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
        :confirmText="$t('logout')"
        :cancelText="$t('quxiao')"
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
        // {
        //   label: this.$t('financialHistory'),
        //   icon: "rob.png",
        //   url: "/pages/index?tabs=1",
        // },
        {
          label: this.$t('accountDetails'),
          icon: "list.png",
          url: "/pages/accountDetails",
        },
        {
          label: this.$t('teamReports'),
          icon: "team.png",
          url: "/pages/team",
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
        // {
        //   label: this.$t('inviteFriends'),
        //   icon: "friend.png",
        //   url: "/pages/promotion",
        // },
        // {
        //   label: this.$t('systemInformation'),
        //   icon: "message.png",
        //   url: "/pages/message",
        // },
        
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
      infos: {},//收益数据
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
      // 用户收益详情
      this.$api.user_income_detail().then(({ data }) => {
        if (data.code == 0) {
          this.infos = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    FormatAmount(price){
      return String(Math.floor(price))
      .split('')
      .reverse()
      .reduce((prev,next,index) => {
          return ( index % 3 ? next : next + '.') + prev
      })
    }
  },
};
</script>

<style scoped lang="scss">
.main {
  background-image: url("@/static/img/userbg.png");
  background-repeat: no-repeat;
  background-size: 100% auto;
  padding: 0;
  width: 100%;
  height: 100vh;
}
.title {
  width: 100%;
  overflow: hidden;
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
    margin-top: 60rpx;
    padding-left: 40rpx;
    overflow: hidden;
    color: #fff;
    .head {
      float: left;
      .head-img {
        width: 135rpx;
        height: 135rpx;
        display: block;
        border-radius: 50%;
        // border: 2rpx solid #fff;
        box-sizing: border-box;
      }
    }
    .information {
      float: left;
      margin-left: 22rpx;
      margin-top: 22rpx;
      .level {
        overflow: hidden;
        .name {
          font-size: 36rpx;
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
       
      }
      .code {
        margin-top: 14rpx;
        font-size: 26rpx;
        .credit {
          background: #D8E4F9;
          height: 37.5rpx;
          line-height: 35.5rpx;
          margin-left: 12rpx;
          margin-top: 8rpx;
          color: #546592;
          font-size: 24rpx;
          padding: 0rpx 8rpx;
          border-radius: 4rpx;
          border-radius: 6rpx;
        }
      }
    }
    .customer-box{
      float: right;
      .customer-img{
        width: 52rpx;
        height: 52rpx;
        margin-right: 44rpx;
        margin-top: 20rpx;
      }
    }
  }
  .money {
    width: 90%;
    height: 336rpx;
    margin: 20rpx auto auto;
    background-color: rgba(98,176,233,0.5);
    backdrop-filter: blur(5px);
    padding: 0 32rpx;
    border-radius: 10rpx;
    color: #fff;
    .balance {
      margin-top: 24rpx;
      display: block;
      float: left;
      font-size: 28rpx;
      .money-color {
        font-size: 24rpx;
        margin-top: 15rpx;
        .money-number {
          font-weight: 700;
          font-size: 44rpx;
        }
      }
    }
    .recharge {
      float: right;
      width: 116rpx;
      height: 56rpx;
      text-align: center;
      line-height: 56rpx;
      background-color: #D8E4F9;
      color: #2E68F2;
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
      background-color: #D8E4F9;
      color: #2E68F2;
      border-radius: 28rpx;
      margin: 54rpx 0 0 20rpx;
      font-size: 28rpx;
    }
    .income{
      display: flex;
      margin-top: 40rpx;
      .income-item{
        width: 33.33%;
        text-align: center;
        .income-item-text{
          font-size: 24rpx;
        }
        .income-item-number{
          text-shadow: 0 0 6rpx #2E68F2;
          font-size: 24rpx;
          margin-top: 30rpx
        }
      }
    }
  }
}
.content {
  overflow: hidden;
  .item {
    float: left;
    width: 33.33%;
    box-sizing: border-box;
    text-align: center;
    padding: 42rpx 0 0rpx 0rpx;
    .item-img {
      width: 150rpx;
      height: 150rpx;
    }
    .text {
      color: #fff;
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
