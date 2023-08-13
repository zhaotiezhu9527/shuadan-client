<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="团队报表"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#1e1e1e"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="scroll">
      <view class="rows">
        <view class="team flex items-center justify-between">
          <view class="tatol">
            <view class="txt">团队余额（元）</view>
            <view class="moeny">{{ items.teamBalance || 0 }}</view>
          </view>
          <view class="li">
            <view class="item">
              <view class="txt">团队流水（元）</view>
              <view class="moeny green">￥{{ items.teamwithdraw || 0 }}</view>
            </view>
            <view class="item">
              <view class="txt">团队总充值（元）</view>
              <view class="moeny">￥{{ items.teamBet || 0 }}</view>
            </view>
            <view class="item">
              <view class="txt">团队总提现（元）</view>
              <view class="moeny green">￥{{ items.teamDeposit || 0 }}</view>
            </view>
            <view class="item">
              <view class="txt">团队订单佣金（元）</view>
              <view class="moeny">￥{{ items.teamIncome || 0 }}</view>
            </view>
          </view>
        </view>
      </view>
      <view class="views flex items-center">
        <view class="item">
          <view class="moeny">{{ items.inviteCount || 0 }}人</view>
          <view class="txt">直推人数</view>
        </view>
        <view class="item">
          <view class="moeny">{{ items.teamMemberCount || 0 }}人</view>
          <view class="txt">团队人数</view>
        </view>
        <view class="item">
          <view class="moeny">{{ items.depositCount || 0 }}人</view>
          <view class="txt">首充人数</view>
        </view>
        <view class="item">
          <view class="moeny">{{ items.newRegisterCount || 0 }}人</view>
          <view class="txt">新增人数</view>
        </view>
        <view class="item">
          <view class="moeny">{{ items.activeCount || 0 }}人</view>
          <view class="txt">活跃人数</view>
        </view>
      </view>
      <view class="rowStyle">
        <view class="title">
          <view
            class="tab"
            :class="{ active: index === active }"
            v-for="(item, index) in nav"
            :key="index"
            @click="navChange(index)"
          >
            {{ item.name }}
          </view>
        </view>
        <template v-if="list.length">
          <view class="contentStyle" v-for="(item, index) in list" :key="index">
            <view class="content">
              <view class="time">注册时间：{{ item.registerTime }}</view>
              <view class="user flex items-center justify-between">
                <view class="flex items-center">
                  <image
                    class="image"
                    src="@/static/img/head.png"
                    mode="widthFix"
                  />
                  <view class="con">
                    <view class="name">{{ item.nickName }}</view>
                    <view class="phone">{{ item.phone }}</view>
                  </view>
                </view>
                <view class="num">
                  <view class="numSize">{{ item.inviteCount }}</view>
                  <view class="numText">推荐人数</view>
                </view>
              </view>
              <view class="moeny">
                <view class="topup">
                  <view>充值</view>
                  <view>￥{{ item.deposit }}</view>
                </view>
                <view class="withdraw">
                  <view>提现</view>
                  <view>￥{{ item.withdraw }}</view>
                </view>
              </view>
            </view>
          </view>
        </template>
        <u-empty class="nempty" text="暂无数据" v-else />
      </view>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      list: [], //列表数据
      loading: false,
      active: 0,
      datetime: Number(new Date()),
      items: {},
      nav: [{ name: "一级" }, { name: "二级" }, { name: "三级" }],
    };
  },
  onShow() {
    // 获取团队信息
    this.$api.user_teamReport().then(({ data }) => {
      if (data.code == 0) {
        this.items = data.data;
      } else {
        this.$base.show(data.msg);
      }
    });
    this.navChange(this.active);
  },
  methods: {
    navChange(index) {
      this.active = index;
      this.$api.user_team(index + 1).then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
  },
};
</script>
<style lang="scss">
.scroll {
  height: calc(100vh - var(--status-bar-height) - 100rpx) !important;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
.rowStyle {
  background: #1e1e1e;
  padding: 32rpx;
  border-radius: 32rpx 32rpx 0 0;
  margin-top: 32rpx;
}
.title {
  width: 100%;
  height: 80rpx;
  background: linear-gradient(
      0deg,
      rgba(255, 255, 255, 0.2),
      rgba(255, 255, 255, 0.2)
    ),
    linear-gradient(0deg, #1e1e1e, #1e1e1e);
  border: 1rpx solid #ffffff33;
  box-shadow: 0 8rpx 8rpx 0 #00000040 inset;
  overflow: hidden;
  border-radius: 100rpx;

  .tab {
    float: left;
    width: 33.3%;
    color: #ffffff;
    height: 76rpx;
    line-height: 76rpx;
    text-align: center;
    font-size: 28rpx;
    transition: all 0.5s;
    border-radius: 100rpx;
    &.active {
      box-shadow: 4rpx 4rpx 0 0 rgba(255, 255, 255, 0.18) inset;
      box-shadow: -4rpx -4rpx 0 0 rgba(0, 0, 0, 0.35) inset;
      background: linear-gradient(130.74deg, $main-c 17.26%, #9167f2 91.1%);
      border: 0 !important;
      color: $white !important;
    }
  }
}
.contentStyle {
  padding: 32rpx 0 0;
}
.content {
  padding: 28rpx 32rpx;
  background: #2c2c2c;
  border-radius: 16rpx;
  .time {
    padding-bottom: 28rpx;
    color: #ffffffd9;
    border-bottom: 1rpx solid #ffffff1f;
    font-size: 20rpx;
  }
  .user {
    padding: 36rpx 0 52rpx;
    .image {
      width: 80rpx;
    }
    .con {
      margin-left: 16rpx;
    }
    .name {
      font-size: 32rpx;
      color: #ffffffd9;
      font-weight: 500;
    }
    .phone {
      font-size: 24rpx;
      color: #ffffffa6;
      padding-top: 10rpx;
    }
  }
  .num {
    width: 130rpx;
    padding-left: 30rpx;
    text-align: center;
    position: relative;
    &::after {
      content: "";
      display: block;
      height: 48rpx;
      width: 1rpx;
      background: #ffffff1f;
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
    }
    .numSize {
      color: #ffffffd9;
      font-size: 36rpx;
      font-weight: 700;
    }
    .numText {
      color: #ffffffa6;
      font-size: 20rpx;
    }
  }
  .moeny {
    gap: 0 16rpx;
    display: flex;
    align-items: center;
    > view {
      height: 56rpx;
      width: 100%;
      display: flex;
      align-items: center;
      background: linear-gradient(
          0deg,
          rgba(255, 255, 255, 0.08),
          rgba(255, 255, 255, 0.08)
        ),
        linear-gradient(0deg, #252525, #252525);
      border: 1rpx solid #ffffff14;
      border-radius: 8rpx;
      overflow: hidden;
      view:nth-child(1) {
        color: #121212;
        font-size: 20rpx;
        width: 72rpx;
        height: 56rpx;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      view:nth-child(2) {
        font-size: 24rpx;
        color: #ffffffd9;
        margin-left: 16rpx;
      }
    }
    .topup view:nth-child(1) {
      background: linear-gradient(134.61deg, #8ed8ac 0.81%, #68c4a3 97.27%);
    }
    .withdraw view:nth-child(1) {
      background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
    }
  }
}
.rows {
  padding: 32rpx;
  background: #1e1e1e;
  .team > view {
    width: 50%;
  }
  .tatol {
    color: $white;
    .txt {
      font-size: 24rpx;
    }
    .moeny {
      font-size: 64rpx;
      padding-top: 16rpx;
    }
  }
  .item {
    color: #ffffffa6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 16rpx;
    &:nth-last-child(1) {
      padding-bottom: 0;
    }
    .moeny,
    .txt {
      font-size: 24rpx;
    }
  }
}
.views {
  background: #1e1e1e;
  border-top: 1rpx solid #ffffff1f;

  .item {
    width: 25%;
    text-align: center;
    height: 128rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    color: $white;
    .txt {
      font-size: 20rpx;
      color: #ffffffa6;
    }
    .moeny {
      font-size: 32rpx;
      padding-bottom: 14rpx;
    }
  }
}
</style>
