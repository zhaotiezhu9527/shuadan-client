<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('teamReports')"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="scroll">
      <view class="rows">
        <view class="item">
          <view class="txt">{{$t('teamBalance') }}({{ $t('currency') }})</view>
          <view class="moeny">{{ $u.priceFormat(items.teamBalance) || 0 }}</view>
        </view>
        <view class="item right">
          <view class="txt">{{$t('teamFlow') }}({{ $t('currency') }})</view>
          <view class="moeny green">{{ $u.priceFormat(items.teamwithdraw) || 0 }}</view>
        </view>
        <view class="item">
          <view class="txt">{{$t('totalTeamRecharge') }}({{ $t('currency') }})</view>
          <view class="moeny">{{ $u.priceFormat(items.teamBet) || 0 }}</view>
        </view>
        <view class="item right">
          <view class="txt">{{$t('totalTeamWithdraw') }}({{ $t('currency') }})</view>
          <view class="moeny green">{{ $u.priceFormat(items.teamDeposit) || 0 }}</view>
        </view>
        <view class="item">
          <view class="txt">{{$t('teamOrderCommission') }}({{ $t('currency') }})</view>
          <view class="moeny">{{ $u.priceFormat(items.teamIncome) || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">{{$t('pushNo') }}</view>
          <view class="moeny">{{ items.inviteCount || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">{{$t('teamNo') }}</view>
          <view class="moeny">{{ items.teamMemberCount || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">{{$t('firstNo') }}</view>
          <view class="moeny">{{ items.depositCount || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">{{$t('addNo') }}</view>
          <view class="moeny">{{ items.newRegisterCount || 0 }}</view>
        </view>
        <view class="item red">
          <view class="txt">{{$t('activeNo') }}</view>
          <view class="moeny">{{ items.activeCount || 0 }}</view>
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
      <template v-if="list.length">
        <view class="contentStyle" v-for="(item, index) in list" :key="index">
          <view class="content">
            <image class="image" src="@/static/img/head.png" mode="widthFix" />
            <view class="text">
              <view class="li">
                <text>{{$t('nickName') }}:{{ item.nickName }}</text>
                <text class="blur">{{$t('phoneNo') }}:{{ item.phone || 'xxxx'}}</text>
              </view>
              <view class="li">
                <text clas="blur">{{$t('recharge') }}:{{ $u.priceFormat(item.deposit) }}</text>
                <text class="green">{{$t('agentNo') }}: {{ item.inviteCount }}</text>
              </view>
              <view class="li">
                <text class="blur">{{$t('withdraw') }}:{{ $u.priceFormat(item.withdraw) }}</text>
                <text>{{$t('register')}}{{$t('time')}}:{{ item.registerTime }}</text>
              </view>
            </view>
          </view>
        </view>
      </template>
      <u-empty class="nempty" :text="$t('noData')" v-else />
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
      nav: [{ name: this.$t('level1')}, { name: this.$t('level2')}, { name: this.$t('level3') }],
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
    transition: all 0.5s;
    &.active {
      color: #be1204;
      border-color: #be1204;
      transition: all 0.5s;
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
