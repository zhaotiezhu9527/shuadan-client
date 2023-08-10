<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="充值记录"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#000000"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
      :background="background"
    >
    </u-navbar>
    <view class="main">
      <!-- <view class="title">
        <view class="tab active">线下充值</view>
        <view class="tab">线上充值</view>
      </view> -->
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <u-list-item
          v-for="(item, index) in list"
          :key="index"
          class="content-item"
        >
          <view class="content">
            <text class="color666">{{ item.time }}</text>
            <view class="item-list">
              <image class="list-img" src="@/static/img/congzhiList.png" />
              <view>
                <view class="table-money">
                  充值金额
                  <text class="money-right">{{ item.amount }}</text>
                </view>
                <view class="table-title color666">
                  订单号：{{ item.orderNo }}
                </view>
              </view>
            </view>
            <text class="status">
              <label v-if="item.status == 0" class="yellow-text">
                {{ item.statusStr }}
              </label>
              <label v-else-if="item.status == 1" class="blue-text">
                {{ item.statusStr }}
              </label>
              <label v-else-if="item.status == 2" class="red-text">
                {{ item.statusStr }}
              </label>
            </text>
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
      page: 0,
      background: {
        // backgroundColor: '#001f3f',

        // 导航栏背景图
        // background: 'url(https://cdn.uviewui.com/uview/swiper/1.jpg) no-repeat',
        // 还可以设置背景图size属性
        // backgroundSize: 'cover',

        // 渐变色
        backgroundImage: "linear-gradient(47deg, #F8A057 0%, #FFC860 100%);",
      },
    };
  },
  onLoad() {
    this.list = [];
    uni.showLoading();
    this.page = 1;
    this.dataFn();
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
      this.$api
        .user_deposit_list({ page, limit })
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
  width: 92%;
  margin: auto;
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
    color: #333;
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
  border-radius: 10px;
  opacity: 1;
  background: #ffffff;
  padding: 14px 20px;
  position: relative;
  margin: 20rpx 0;
}
.content {
  font-size: 28rpx;
  .status {
    position: absolute;
    right: 10%;
    top: 10%;
    font-size: 36rpx;
    font-weight: 500;
  }
  .item-list {
    display: flex;
    padding-top: 40rpx;
    .list-img {
      width: 90rpx;
      height: 90rpx;
      margin-right: 20rpx;
    }
  }
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
  .money-right {
    position: absolute;
    right: 10%;
    top: 43%;
    font-size: 36rpx;
    font-weight: 500;
    color: #de2511;
  }
}
</style>
