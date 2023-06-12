<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="账户明细"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#464646;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <view class="title">
          <view class="tab active">所有类型</view>
          <view class="tab">提现记录</view>
          <view class="tab">充值记录</view>
        </view>
        <u-list-item v-for="(item, index) in list" :key="index">
          <view class="content">
            <view class="table-money">
              {{ item.orderNo }}
            </view>
            <view class="table-foot">
              <view class="table-title">
                <label v-if="item.status == 0" class="yellow-text"
                  >待审核</label
                >
                <label v-else-if="item.status == 1" class="blue-text"
                  >审核通过</label
                >
                <label v-else-if="item.status == 2" class="red-text"
                  >审核失败</label
                >
              </view>
              <view class="table-number">
                {{ item.orderNo }}
              </view>
            </view>
          </view>
        </u-list-item>
        <view class="loading" v-if="loading">加载中...</view>
        <view class="nomore" v-if="finished">没有更多了</view>
      </u-list>
      <u-empty class="empty" text="暂无数据" v-else />
      <u-datetime-picker
        :show="startShow"
        v-model="datetime"
        mode="date"
        itemHeight="100"
        @confirm="changeStart"
        @cancel="startShow = false"
      ></u-datetime-picker>
    </view>
  </view>
</template>

<script>
import { dateFormat } from "../plugins/util";
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
      datetime: Number(new Date()),
      startTimeText: "", //开始日期显示文字
    };
  },
  onLoad() {
    // this.list = [];
    // uni.showLoading();
    // this.page = 1;
    // this.dataFn();
  },
  onShow() {},
  methods: {
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      this.dataFn(this.page);
    },
    dataFn(page = 1, limit = 20) {
      this.loading = true;
      // this.$api
      //   .user_withdraw_list({ page, limit })
      //   .then((res) => {
      //     if (res.data.code == 0) {
      //       const vim = res.data.page;
      //       this.list = this.list.concat(vim.list);
      //       this.isArray = vim.totalCount ? true : false;
      //       if (this.page >= vim.totalPage) {
      //         this.finished = true;
      //       }
      //     }
      //   })
      //   .finally(() => {
      //     this.loading = false;
      //   });
    },
    changeStart(e) {
      console.log(dateFormat("YYYY-mm-dd", new Date(e.value)));
      this.startTimeText = dateFormat("YYYY-mm-dd", new Date(e.value));
      this.startShow = false;
    },
  },
};
</script>

<style scoped lang="scss">
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
      border-bottom: 4rpx solid #be1204;
    }
  }
}
.content {
  padding: 20rpx;
  font-size: 24rpx;
  line-height: 40rpx;
  border-bottom: 1px solid #ccc;
  .table-money {
    font-size: 28rpx;
  }
  .yellow-text {
    color: #f0ad4e;
  }
  .blue-text {
    color: #007aff;
  }
  .red-text {
    color: #dd524d;
  }
  .table-title {
    float: left;
  }
  .table-number {
    float: left;
    background-color: #f44336;
    color: #fff;
    border-radius: 12rpx;
    font-size: 24rpx;
    padding: 2rpx 8rpx;
    margin-left: 100rpx;
  }
}
.scroll {
  height: calc(100% - 100rpx) !important;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
