<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="账户明细"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#000000"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <!-- <view class="title">
          <view
            class="tab"
            :class="{ active: active === index }"
            @click="change(index)"
            v-for="(item, index) in nav"
            :key="index"
          >
            {{ item.name }}
          </view>
        </view> -->
        <u-list-item v-for="(item, index) in list" :key="index">
          <view class="content">
            <view class="table-money">
              {{ item.optTime }}
            </view>
            <view class="table-foot">
              <view class="table-title">
                <label
                  :class="{
                    'red-text': item.optType === 4,
                    'blue-text': item.optType === 3,
                    'yellow-text': item.optType === 5,
                  }"
                >
                  {{ item.optTypeStr }}
                </label>
              </view>
              <view class="table-number">
                {{ item.amount }}
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
      active: 0,
      page: 0,
      nav: [
        {
          name: "所有类型",
        },
        {
          name: "提现记录",
        },
        {
          name: "充值记录",
        },
      ],
    };
  },
  onLoad() {
    this.list = [];
    uni.showLoading();
    this.page = 1;
    this.dataFn();
  },
  methods: {
    change(index) {
      this.active = index;
    },
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      this.dataFn(this.page);
    },
    dataFn(page = 1, limit = 20) {
      this.loading = true;
      this.$api
        .user_account_list({ page, limit })
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
      border-bottom: 4rpx solid #be1204;
      transition: all 0.5s;
    }
  }
}
.content {
  padding: 20rpx;
  font-size: 24rpx;
  line-height: 40rpx;
  border-bottom: 1px solid #ccc;
  .table-money {
    font-size: 26rpx;
  }
  .yellow-text {
    color: #f0ad4e;
  }
  .blue-text {
    color: #007aff;
  }
  .pink-text {
    color: #d814c9;
  }
  .red-text {
    color: red;
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
    margin-left: 120rpx;
  }
}
.scroll {
  height: calc(100% - 100rpx) !important;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
