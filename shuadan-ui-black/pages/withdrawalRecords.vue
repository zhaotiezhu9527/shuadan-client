<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="提现记录"
      bgColor="#1e1e1e"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <!-- <view class="title">
        <view class="tab active">银行卡提现</view>
        <view class="tab"></view>
      </view> -->
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <u-list-item
          v-for="(item, index) in list"
          :key="index"
          class="content-item"
        >
          <view class="content">
            <view class="table-content">
              <view class="table-left">
                <view>
                  {{ item.statusStr }}
                </view>
                <view class="table-money">
                  {{ item.orderNo}}
                </view>
              </view>
              <view class="table-foot">
                <view class="table-title">
                  <label
                  >
                    {{ item.amount }}
                  </label>
                </view>
                <view class="table-number">
                  {{ item.time}}
                </view>
              </view>
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
      list: [], //列表数据
      loading: false,
      finished: false,
      isArray: true,
      page: 1,
    };
  },
  onLoad() {
    this.list = [];
    uni.showLoading();
    this.page = 1;
    this.dataFn();
  },
  methods: {
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      this.dataFn(this.page);
    },
    dataFn(page = 1, limit = 20) {
      this.loading = true;
      this.$api
        .user_withdraw_list({ page, limit })
        .then((res) => {
          if (res.data.code == 0) {
            const vim = res.data.page;
            this.list = this.list.concat(vim.list);
            this.isArray = vim.totalCount ? true : false;
            if (this.page >= vim.totalPage) {
              this.finished = true;
            }
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  width: 100%;
  margin: auto;
  margin-top: 20rpx;
}
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
    width: 50%;
    color: #ccc;
    border-bottom: 4rpx solid #ccc;
    height: 76rpx;
    line-height: 76rpx;
    text-align: center;
    border-radius: 4rpx;
    box-shadow: 0 0 1rpx #b8bbbf;
    &.active {
      color: #be1204;
      border-bottom: 4rpx solid #be1204;
    }
  }
}
.content-item {
  background-color: #1e1e1e;
  color: #b0b0b0;
  position: relative;
}
.content {
  padding: 20rpx 20rpx 0 20rpx;
  font-size: 24rpx;
  line-height: 40rpx;
  background-color: #1e1e1e;
  color: #b0b0b0;
  overflow: hidden;
  .table-content{
    width: 96%;
    margin: auto;
    border-bottom: 1px solid #393939;
    overflow: hidden;
    .table-left{
      float: left;
    }
    .table-foot{
      float: right;
      text-align: right;
      padding-bottom: 20rpx;
      overflow: hidden;
      .table-title{
        color: #ddd;
        font-size: 32rpx;
      }
    }
  }
  .table-money {
    font-size: 28rpx;
  }
}
</style>
