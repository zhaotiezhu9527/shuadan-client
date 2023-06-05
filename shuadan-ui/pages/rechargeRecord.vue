<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="充值记录"
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
      <view class="title">
        <view class="tab active">线下充值</view>
        <view class="tab">线上充值</view>
      </view>
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <u-list-item v-for="(item, index) in list" :key="index">
          <view class="content">
            <view class="table-money">
              充值金额（¥）{{ item.amount }}
            </view>
            <view class="table-title">
              订单号：{{ item.orderNo }}
            </view>
            <view>
              充值状态：
              <label v-if="item.status == 0" class="yellow-text">待审核</label>
              <label v-else-if="item.status == 1" class="blue-text">审核通过</label>
              <label v-else-if="item.status == 2" class="red-text">审核失败</label>
            </view>
          </view>
        </u-list-item>
        <view class="loading" v-if="loading">加载中...</view>
        <view class="nomore" v-if="finished">没有更多了</view>
      </u-list>
      <u-empty class="empty" text="暂无数据" v-else />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      list: [
        { 
          amount: '10000',
          orderNo: '213123213213123213',
          status: '1'
        },
        { 
          amount: '10000',
          orderNo: '213123213213123213',
          status: '0'
        },
        { 
          amount: '10000',
          orderNo: '213123213213123213',
          status: '2'
        },
      ], //列表数据
      loading: false,
      finished: false,
      isArray: true,
      page: 0,
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
  },
};
</script>

<style scoped lang="scss">
.title{
  width: 100%;
  height: 80rpx;
  .tab{
    float: left;
    width: 50%;
    color: #333;
    border-bottom: 4rpx solid #ccc;
    height: 76rpx;
    line-height: 76rpx;
    text-align: center;
    border-radius: 4rpx;
    box-shadow: 0 0 1rpx #b8bbbf;
    &.active{
      color: #be1204;
      border-bottom: 4rpx solid #be1204;
    }
  }
}
.content{
  padding: 20rpx;
  font-size: 24rpx;
  line-height: 40rpx;
  border-bottom: 1px solid #ccc;
  .table-money{
    font-size: 28rpx;
  }
  .yellow-text{
    color:#f0ad4e;
  }
  .blue-text{
    color:#007aff;
  }
  .red-text{
    color:#dd524d;
  }
}
</style>
