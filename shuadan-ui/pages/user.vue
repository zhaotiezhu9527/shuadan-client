<template>
  <view>
    <view class="main">
      <!-- 头部 -->
      <view class="title">
        <view class="icon">
          <image class="icon-img" src="../static/img/notice.png" />
          <image class="icon-img box-right" src="../static/img/setting.png" @click="goPage(list[0].url)"/>
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
              <label class="credit">信用分:{{ userData.creditValue }}</label>
            </view>
            <view class="code">
              邀请码: {{ userData.inviteCode }}
            </view>
          </view>
        </view>
        <view class="money">
          <view class="balance">
            <view>账户金额</view>
            <view class="money-color">¥<label class="money-number">{{ userData.balance }}</label></view>
          </view>
          <view class="withdrawal" @click="goDeposit">提现</view>
          <view class="recharge">充值</view>
        </view>
        <view class="money-bottom"></view>
      </view>
      <!-- 导航 -->
      <view class="content">
        <view class="item" v-for="(item,index) in list" :key="index" @click="goPage(item.url)">
          <image class="item-img" :src="'../static/img/' + item.icon" />
          <view class="text">{{item.label}}</view>
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
            退出登陆
          </u-button>
      </view>
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
      loading: false,//加载状态
      list:[
        {
          label:'个人信息',
          icon: 'user.png',
          url:'/pages/set'
        },
        {
          label:'抢单记录',
          icon: 'rob.png',
          url:'/pages/set'
        },
        {
          label:'账户明细',
          icon: 'list.png',
          url:'/pages/accountDetails'
        },
        {
          label:'充值记录',
          icon: 'recharge.png',
          url:'/pages/rechargeRecord'
        },
        {
          label:'提现记录',
          icon: 'withdrawal.png',
          url:'/pages/withdrawalRecords'
        },
        {
          label:'邀请好友',
          icon: 'friend.png',
          url:'/pages/invite'
        },
        {
          label:'信息公告',
          icon: 'message.png',
          url:'/pages/message'
        },
        {
          label:'团队报表',
          icon: 'team.png',
          url:'/pages/set'
        }
      ],
      userData:{
        nickName: "",//昵称
        avatarUrl: "",//头像
        balance: "",//余额
        creditValue: "",//信用分
        inviteCode: "",//邀请码
        userName: "",//用户名
        levelName: "",//会员名称
        levelIcon: "",//
      },
      loginoutShow: false,
      bindStatus: false,//银行卡绑定状态
    };
  },
  onShow() {
    this.getInfo()
  },
  methods: {
    loginOut(){
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
    goPage(url) {
      uni.navigateTo({
        url: url,
      });
    },
    goDeposit(){
      if(this.bindStatus){
        uni.navigateTo({
          url: "/pages/deposit",
        });
      }else{
        this.$base.show('请先绑定银行卡')
      }
      
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData = res.data.data;
          if(this.userData.bankNo === null || !this.userData.bankNo){
            this.bindStatus = false
          }else{
            this.bindStatus = true
          }
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.title{
  width: 100%;
  height: 440rpx;
  background: url(../static/img/title.png) no-repeat;
  background-size: 100% 100%;
  overflow: hidden;
  position: relative;
  .icon{
    margin-top: 20rpx;
    height: 40rpx;
    padding: 0 40rpx;
    display: flex;
    .icon-img{
      width: 44rpx;
      height: 44rpx;
    }
  }
  .user{
    margin-top: 50rpx;
    padding-left: 40rpx;
    overflow: hidden;
    .head{
      float: left;
      .head-img{
        width: 100rpx;
        height: 100rpx;
        display: block;
        border-radius: 50%;
        border: 2rpx solid #fff;
        box-sizing: border-box;
      }
    }
    .information{
      float: left;
      margin-left: 24rpx;
      .level{
        overflow: hidden;
        .name{
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
        .level-img{
          float: left;
          color: #fff;
          height: 36rpx;
          width: 96rpx;
          margin-left: 12rpx;
          margin-top: 8rpx;
        }
        .credit{
          float: left;
          background-color: #00CED1;
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
      .code{
        margin-top: 14rpx;
        font-size: 26rpx;
        color: #434343;
      }
    }
  }
  .money{
    width: 660rpx;
    height: 160rpx;
    background: url(../static/img/money.png) no-repeat;
    background-size: 100% 100%;
    margin: 54rpx auto auto;
    padding: 0 32rpx;
    .balance{
      color: hsla(0, 0%, 100%, .6);
      margin-top: 24rpx;
      display: block;
      float: left;
      .money-color{
        color: #f2d8be;
        font-size: 24rpx;
        margin-top: 15rpx;
        .money-number{
          font-weight: 700;
          font-size: 44rpx;
          margin-left: 10rpx;
        }
      }
    }
    .recharge{
      float: right;
      width: 116rpx;
      height: 56rpx;
      text-align: center;
      line-height: 56rpx;
      background: hsla(0, 0%, 100%, .2);
      color: #fff;
      border-radius: 28rpx;
      margin: 54rpx 0 0 32rpx;
      font-size: 28rpx;
    }
    .withdrawal{
      float: right;
      width: 116rpx;
      height: 56rpx;
      text-align: center;
      line-height: 56rpx;
      background: hsla(0, 0%, 100%, .2);
      color: #fff;
      border-radius: 28rpx;
      margin: 54rpx 0 0 32rpx;
      font-size: 28rpx;
    }
  }
  .money-bottom{
    width: 100%;
    height: 69rpx;
    background: url(../static/img/money_bottom.png) no-repeat;
    background-size: 100%;
    position: absolute;
    bottom: 0;
  }
}
.content{
  border-top: 2rpx solid #f2f2f2;
  background: #fff;
  overflow: hidden;
  .item{
    float: left;
    width: 33.33%;
    box-sizing: border-box;
    border-right: 2rpx solid #eee;
    border-bottom: 2rpx solid #eee;
    text-align: center;
    padding: 42rpx 0 50rpx;
    .item-img{
      width: 78rpx;
      height: 78rpx;
    }
    .text{
      color: #333;
      margin-top: 10rpx;
      font-size: 28rpx;
    }
  }
}
.logout{
  padding: 0 26rpx 50rpx;
  .custom-style {
    border-radius: 10rpx;
    margin-bottom: 20rpx;
    font-size: 32rpx;
    height: 88rpx;
    margin-top: 40rpx;
  }
}
</style>
