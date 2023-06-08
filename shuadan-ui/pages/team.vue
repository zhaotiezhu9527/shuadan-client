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
    <u-list @scrolltolower="load" v-if="isArray" class="scroll">
      <view class="head">
        <view class="pull-left">
          <u-datetime-picker
            :show="startShow"
            v-model="startTime"
            mode="date"
            @confirm="changeStart"
          ></u-datetime-picker>
          <view class="date-input" @click="startShow = true">{{
            startTimeText
          }}</view>
        </view>
        <view class="pull-left data-text">至</view>
        <view class="pull-left">
          <u-datetime-picker
            :show="startShow"
            v-model="startTime"
            mode="date"
            @confirm="changeStart"
          ></u-datetime-picker>
          <view class="date-input" @click="startShow = true">{{
            startTimeText
          }}</view>
        </view>
        <view class="pull-left">
          <u-button
            class="btn-style"
            color="#ff7614"
            shape="circle"
            icon="search"
            size="small"
            text="搜索"
          ></u-button>
        </view>
      </view>
      <view class="rows">
        <view class="item">
          <view class="txt">团队余额（元）</view>
          <view class="moeny">￥0</view>
        </view>
        <view class="item right">
          <view class="txt">团队流水（元）</view>
          <view class="moeny green">￥0</view>
        </view>
        <view class="item">
          <view class="txt">团队总充值（元）</view>
          <view class="moeny">￥0</view>
        </view>
        <view class="item right">
          <view class="txt">团队总提现（元）</view>
          <view class="moeny green">￥0</view>
        </view>
        <view class="item">
          <view class="txt">团队订单佣金（元）</view>
          <view class="moeny">￥0</view>
        </view>
        <view class="item red">
          <view class="txt">直推人数</view>
          <view class="moeny">2人</view>
        </view>
        <view class="item red">
          <view class="txt">团队人数</view>
          <view class="moeny">2人</view>
        </view>
        <view class="item red">
          <view class="txt">首充人数</view>
          <view class="moeny">2人</view>
        </view>
        <view class="item red">
          <view class="txt">新增人数</view>
          <view class="moeny">2人</view>
        </view>
        <view class="item red">
          <view class="txt">活跃人数</view>
          <view class="moeny">2人</view>
        </view>
      </view>
      <view class="title">
        <view class="tab active">一级</view>
        <view class="tab">二级</view>
        <view class="tab">三级</view>
      </view>
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
    </u-list>
    <u-empty class="empty" text="暂无数据" v-else />
  </view>
</template>
<script>
export default {
  data() {
    return {
      list: [
        {
          amount: "10000",
          orderNo: "213123213213123213",
          status: "1",
        },
        {
          amount: "10000",
          orderNo: "213123213213123213",
          status: "0",
        },
        {
          amount: "10000",
          orderNo: "213123213213123213",
          status: "2",
        },
      ], //列表数据
      loading: false,
      finished: false,
      isArray: true,
      page: 0,
      startShow: false, //开始日期显示
      startTime: Number(new Date()),
      startTimeText: "", //开始日期显示文字
    };
  },
  methods: {
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      // this.dataFn(this.page);
    },
    changeStart() {
      console.log(this.startTime);
      console.log(new Date(this.startTime));
      console.log(dateFormat("YYYY-mm-dd", new Date(this.startTime)));
      this.startTimeText = dateFormat("YYYY-mm-dd", new Date(this.startTime));
      this.startShow = false;
    },
  },
};
</script>
<style lang="scss">
.scroll {
  height: calc(100% - 100rpx) !important;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
.head {
  overflow: hidden;
  padding: 20rpx 30rpx;
  border-bottom: 1px solid #eee;
  .data-text {
    height: 60rpx;
    line-height: 60rpx;
    padding: 0 20rpx;
  }
  .date-input {
    width: 240rpx;
    height: 60rpx;
    background-color: #f2f2f2;
    border-radius: 30rpx;
    line-height: 60rpx;
    text-align: center;
    font-size: 24rpx;
  }
  .btn-style {
    color: #fff;
    margin-left: 10rpx;
    height: 60rpx;
    line-height: 60rpx;
    display: flex;
    justify-content: center;
    /deep/.uicon-search {
      color: #fff !important;
      font-size: 30rpx !important;
    }
  }
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
