<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="团队报表"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <u-list @scrolltolower="load" class="scroll">
      <view class="rows">
        <view class="item">
          <view class="txt">团队余额（元）</view>
          <view class="moeny">￥{{ items.teamBalance || 0 }}</view>
        </view>
        <view class="item right">
          <view class="txt">团队流水（元）</view>
          <view class="moeny green">￥{{ items.teamwithdraw || 0 }}</view>
        </view>
        <view class="item">
          <view class="txt">团队总充值（元）</view>
          <view class="moeny">￥{{ items.teamBet || 0 }}</view>
        </view>
        <view class="item right">
          <view class="txt">团队总提现（元）</view>
          <view class="moeny green">￥{{ items.teamDeposit || 0 }}</view>
        </view>
        <view class="item">
          <view class="txt">团队订单佣金（元）</view>
          <view class="moeny">￥{{ items.teamIncome || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">直推人数</view>
          <view class="moeny">{{ items.inviteCount || 0 }}人</view>
        </view>
        <view class="item red">
          <view class="txt">团队人数</view>
          <view class="moeny">{{ items.teamMemberCount || 0 }}人</view>
        </view>
        <view class="item red">
          <view class="txt">首充人数</view>
          <view class="moeny">{{ items.depositCount || 0 }}人</view>
        </view>
        <view class="item red">
          <view class="txt">新增人数</view>
          <view class="moeny">{{ items.newRegisterCount || 0 }}人</view>
        </view>
        <view class="item red">
          <view class="txt">活跃人数</view>
          <view class="moeny">{{ items.activeCount || 0 }}人</view>
        </view>
      </view>
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
      <template v-if="isArray">
        <u-list-item
          class="contentStyle"
          v-for="(item, index) in list"
          :key="index"
        >
          <view class="content">
            <image class="image" src="@/static/img/head.png" mode="widthFix" />
            <view class="text">
              <view class="li">
                <text>姓名:赵铁柱</text>
                <text class="blur">电话:dun****n</text>
              </view>
              <view class="li">
                <text clas="blur">充值:0</text>
                <text class="green">推荐人数: 0</text>
              </view>
              <view class="li">
                <text class="blur">提现:0</text>
                <text>注册时间:2023年06月04日 12:06:14</text>
              </view>
            </view>
          </view>
        </u-list-item>
        <view class="loading" v-if="loading">加载中...</view>
        <view class="nomore" v-if="finished">没有更多了</view>
      </template>
      <u-empty class="nempty" text="暂无数据" v-else />
    </u-list>
  </view>
</template>
<script>
export default {
  data() {
    return {
      list: [], //列表数据
      loading: false,
      finished: false,
      isArray: false,
      page: 0,
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
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
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
.title {
  width: 100%;
  height: 80rpx;
  .tab {
    float: left;
    width: 33.3%;
    color: #333;
    border-bottom: 4rpx solid #ccc;
    height: 76rpx;
    line-height: 76rpx;
    text-align: center;
    border-radius: 4rpx;
    box-shadow: 0 0 1rpx #b8bbbf;
    font-size: 28rpx;
    &.active {
      color: #be1204;
      border-color: #be1204;
    }
  }
}
.contentStyle {
  padding-top: 20rpx;
}
.content {
  display: flex;
  align-items: center;
  border-bottom: 2rpx solid #f8f2f2;
  padding: 20rpx;
  .image {
    width: 120rpx;
    height: 120rpx;
  }
  .text {
    flex: 1;
    font-size: 24rpx;
    margin-left: 30rpx;
  }
  .li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 10rpx;
    .blur {
      color: #00bcd4;
    }
    .green {
      color: #00d44b;
    }
  }
}
.rows {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .item {
    width: 50%;
    border-right: 2rpx solid #eee;
    padding: 20rpx;
    border-bottom: 1rpx solid #eee;
    &:nth-child(2n) {
      border-right: 0;
    }
    &.right {
      text-align: right;
    }
    .green {
      color: green;
    }
    .txt {
      font-size: 24rpx;
      color: #9c9c9c;
    }
    .moeny {
      font-size: 38rpx;
    }
    &.red {
      view:nth-child(1) {
        font-size: 32rpx;
        color: #000;
      }
      view:nth-child(2) {
        color: red;
      }
    }
  }
}
</style>
