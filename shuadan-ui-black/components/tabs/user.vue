<template>
  <view>
    <view class="main">
      <u-navbar
        placeholder
        :border="false"
        title="个人中心"
        fixed
        bgColor="#1e1e1e"
        leftIconSize="0"
        safe-area-inset-top
        height="100rpx"
        titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
      >
        <template #left>
          <image
            style="width: 36rpx"
            @click="goPage({ label: '信息公告', url: '/pages/message' })"
            src="/static/img/msg_notice.png"
            mode="widthFix"
          />
        </template>
        <template #right>
          <image
            style="width: 48rpx"
            @click="goPage({ label: '个人信息', url: '/pages/set' })"
            src="/static/img/bg-020.png"
            mode="widthFix"
          />
        </template>
      </u-navbar>
      <view class="title">
        <view class="user">
          <image class="head-img" mode="whiteFix" :src="userData.avatarUrl" />
          <view class="information">
            <view class="flex items-center">
              <view class="name">{{ userData.nickName }}</view>
              <image class="level-img" :src="userData.levelIcon" />
              <label class="credit">信用分:{{ userData.creditValue }}</label>
            </view>
            <view class="code">
              邀请码: {{ userData.inviteCode }}
              <label class="copy-text" @click="copy(userData.inviteCode)"
                >复制</label
              ></view
            >
          </view>
        </view>
        <view class="price">
          <view class="price-box">
            <view class="flex">
              <view class="money2">
                <view class="title">账户金额（元）</view>
                <view class="txt">{{ userData.balance }}</view>
              </view>
              <view class="btn">
                <view @click="goDeposit('/pages/recharge')"> 充值 </view>
                <view class="active" @click="goDeposit('/pages/deposit')">
                  提现
                </view>
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
          :class="{ mb: item.className }"
        >
          <view class="item-img flex items-center justify-center">
            <image
              :style="{
                width: item.width + 'rpx',
              }"
              :src="'../static/img/' + item.icon"
              mode="widthFix"
            />
          </view>
          <view class="text">{{ item.label }}</view>
        </view>
      </view>
      <u-modal
        :show="loginoutShow"
        title="退出登录"
        @confirm="loginOut"
        @cancel="loginoutShow = false"
        ref="uModal"
        :asyncClose="true"
        showCancelButton
        confirmColor="#6c38ed"
        cancelColor="#ffffff"
      >
        <view class="btn">你确定退出吗？</view>
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
          label: "个人信息",
          icon: "bg-021.png",
          url: "/pages/set",
          width: "36",
        },
        {
          label: "抢单记录",
          icon: "bg-022.png",
          url: "/pages/index?tabs=1",
          className: "mb",
          width: "36",
        },
        {
          label: "账户明细",
          icon: "bg-023.png",
          url: "/pages/accountDetails",
          width: "36",
        },
        {
          label: "充值记录",
          icon: "bg-024.png",
          url: "/pages/rechargeRecord",
          width: "40",
        },
        {
          label: "提现记录",
          icon: "bg-025.png",
          url: "/pages/withdrawalRecords",
          className: "mb",
          width: "40",
        },
        {
          label: "邀请好友",
          icon: "bg-026.png",
          url: "/pages/promotion",
          className: "mb",
          width: "40",
        },
        {
          label: "信息公告",
          icon: "bg-027.png",
          url: "/pages/message",
          width: "40",
        },
        {
          label: "团队报表",
          icon: "bg-028.png",
          url: "/pages/team",
          className: "mb",
          width: "42",
        },
        {
          label: "退出登录",
          icon: "bg-029.png",
          url: "",
          width: "32",
          className: "mb",
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
      } else if (label === "退出登录") {
        this.loginoutShow = true;
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
  .user {
    display: flex;
    align-items: center;
    padding: 44rpx 32rpx;
    background: #1e1e1e;
    .head-img {
      width: 80rpx;
      height: 80rpx;
      display: block;
      border-radius: 50%;
      border: 1rpx solid #fff;
      box-sizing: border-box;
    }
    .information {
      margin-left: 16rpx;
      .name {
        font-size: 36rpx;
        color: #ffffffd9;
        font-weight: 500;
      }
      .level-img {
        float: left;
        color: #fff;
        height: 36rpx;
        width: 96rpx;
        margin: 0 16rpx;
      }
      .credit {
        background: linear-gradient(130.74deg, #6c38ed 17.26%, #9167f2 91.1%);
        height: 37.5rpx;
        line-height: 37.5rpx;
        color: #fff;
        font-size: 24rpx;
        padding: 0rpx 8rpx;
        border-radius: 4rpx;
      }
      .code {
        margin-top: 14rpx;
        font-size: 26rpx;
        color: #ffffffa6;
        .copy-text {
          font-size: 24rpx;
          color: #ffb24e;
          padding-left: 16rpx;
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
.price {
  margin: 16rpx 0;
  background: linear-gradient(134.61deg, #333333 0.81%, #1e1e1e 97.27%),
    conic-gradient(
      from 180deg at 50% 50%,
      rgba(245, 211, 172, 0) 0deg,
      rgba(245, 211, 172, 0.38) 45deg,
      rgba(245, 211, 172, 0) 84.38deg,
      rgba(245, 211, 172, 0) 133.12deg,
      rgba(245, 211, 172, 0.37) 187.5deg,
      rgba(245, 211, 172, 0) 230.62deg,
      rgba(245, 211, 172, 0) 360deg
    );
  border: 1px solid;

  border-image-source: conic-gradient(
    from 180deg at 50% 50%,
    rgba(245, 211, 172, 0) 0deg,
    rgba(245, 211, 172, 0.38) 45deg,
    rgba(245, 211, 172, 0) 84.38deg,
    rgba(245, 211, 172, 0) 133.12deg,
    rgba(245, 211, 172, 0.37) 187.5deg,
    rgba(245, 211, 172, 0) 230.62deg,
    rgba(245, 211, 172, 0) 360deg
  );

  .money2 {
    .title {
      font-size: 24rpx;
      font-weight: 500;
      color: #ffffff;
    }
    .txt {
      font-weight: 600;
      font-size: 64rpx;
      padding-top: 20rpx;
      font-weight: bold;
      color: #ffffff;
    }
  }
  .price-box {
    padding: 48rpx 32rpx;
    .flex {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .btn {
        display: flex;
        align-items: center;
        gap: 8rpx;
        justify-content: space-between;
        view {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 104rpx;
          height: 64rpx;
          padding: 0 24rpx;
          border-radius: 16rpx;
          background: rgba($white, 0.12);
          color: $white;
        }
      }
    }
  }
}
.content {
  padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
  .item {
    display: flex;
    align-items: center;
    padding: 24rpx 32rpx;
    background-color: #1e1e1e;
    position: relative;
    margin-bottom: 0;
    &::before {
      content: "";
      background: rgba($white, 0.12);
      height: 1rpx;
      width: 90%;
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      bottom: 0;
      z-index: 1;
    }
    &.mb {
      margin-bottom: 16rpx;
      &::before {
        display: none;
      }
    }
    &:after {
      content: "";
      width: 18rpx;
      height: 18rpx;
      position: absolute;
      right: 30rpx;
      top: 50%;

      border-left: 2rpx solid #aaa;
      border-bottom: 2rpx solid #aaa;
      transform: translateY(-50%) rotate(-135deg);
    }
    .item-img {
      width: 40rpx;
      .image {
        width: 100%;
      }
    }
  }
  .text {
    font-size: 32rpx;
    color: #ffffffd9;
    margin-left: 16rpx;
  }
}
/deep/.u-popup__content {
  background: #1e1e1e;
  box-shadow: 0 0 16rpx 0 #ffb400b2;
  .u-modal__title {
    color: $white;
  }
}
.btn {
  padding: 0 20rpx;
  color: $white;
}
</style>
