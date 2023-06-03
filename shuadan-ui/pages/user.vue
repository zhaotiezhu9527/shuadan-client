<template>
  <view>
    <view class="main">
      <!-- 头部 -->
      <view class="title">
        <view class="icon">
          <image class="icon-img" src="../static/img/notice.png" />
          <image class="icon-img box-right" src="../static/img/setting.png" />
        </view>
        <view class="user">
          <view class="head">
            <image class="head-img" src="../static/img/head.png" />
          </view>
          <view class="information">
            <view class="level">
              <label class="name">小鹿</label>
              <label class="grade">LV1</label>
              <label class="credit">信用分:100</label>
            </view>
            <view class="code">
              邀请码:017824
            </view>
          </view>
        </view>
        <view class="money">
          <view class="balance">
            <view>账户金额</view>
            <view class="money-color">¥<label class="money-number">10000.45</label></view>
          </view>
          <view class="withdrawal" @click="goDeposit">提现</view>
          <view class="recharge">充值</view>
        </view>
        <view class="money-bottom"></view>
      </view>
      <!-- 导航 -->
      <view class="content">
        <view class="item" @click="goSet">
          <image class="item-img" src="../static/img/user.png" />
          <view class="text">个人信息</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/rob.png" />
          <view class="text">抢单记录</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/list.png" />
          <view class="text">账户明细</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/recharge.png" />
          <view class="text">充值记录</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/withdrawal.png" />
          <view class="text">提现记录</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/friend.png" />
          <view class="text">邀请好友</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/message.png" />
          <view class="text">信息公告</view>
        </view>
        <view class="item">
          <image class="item-img" src="../static/img/team.png" />
          <view class="text">团队报表</view>
        </view>
      </view>
      <!-- 退出登陆 -->
      <view class="logout">
        <u-button
            class="custom-style"
            color="#2f3848"
            block
            @click="loginOut"
            :loading="loading"
          >
            退出登陆
          </u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false,//加载状态
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {
    loginOut(){

    },
    goSet() {
      uni.navigateTo({
        url: "/pages/set",
      });
    },
    goDeposit(){
      uni.navigateTo({
        url: "/pages/deposit",
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
        .grade{
          float: left;
          background: rgba(0, 0, 0, .2);
          color: #fff;
          height: 32rpx;
          border-radius: 4rpx;
          font-size: 24rpx;
          margin-left: 12rpx;
          padding: 0 4rpx;
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
