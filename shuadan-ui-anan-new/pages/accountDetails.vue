<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('order_log')"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#1e1e1e"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <u-list @scrolltolower="load" v-if="isArray" class="scroll">
        <u-list-item v-for="(item, index) in list" :key="index">
          <view class="content">
            <view class="table-content">
              <view class="table-money">
                {{ item.optTypeStr }}
              </view>
              <view class="table-foot">
                <view class="table-title">
                  <label>
                    {{ item.optTime }}
                  </label>
                </view>
                <view class="table-number">
                  {{ Number(item.amount).toFixed(2) }}
                </view>
              </view>
            </view>
          </view>
        </u-list-item>
        <view class="loading" v-if="loading">{{ $t("load_more") }}</view>
        <view class="nomore" v-if="finished">{{ $t("no_more") }}</view>
      </u-list>
      <u-empty class="empty" :text="$t('nodata')" v-else />
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
    dataFn(page = 1, limit = 100) {
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
.main {
  margin-top: 20rpx;
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
  }
}
.content {
  padding: 20rpx 20rpx 0 20rpx;
  font-size: 24rpx;
  line-height: 40rpx;
  background-color: #1e1e1e;
  color: #b0b0b0;
  overflow: hidden;
  .table-content {
    width: 96%;
    margin: auto;
    border-bottom: 1px solid #393939;
    .table-foot {
      padding-bottom: 20rpx;
      overflow: hidden;
    }
  }
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
    float: right;
    background: linear-gradient(#d8b88e, #c49a68);
    color: #000;
    border-radius: 8rpx;
    font-size: 24rpx;
    padding: 4rpx 10rpx;
    margin-left: 120rpx;
  }
}
.scroll {
  height: calc(100% - 100rpx) !important;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
