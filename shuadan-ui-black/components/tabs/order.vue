<template>
  <view class="main">
    <u-list class="container" @scrolltolower="load">
      <u-navbar
        placeholder
        :border="false"
        autoBack
        title="任务记录"
        fixed
        bgColor="#1e1e1e"
        leftIconSize="0"
        safe-area-inset-top
        height="100rpx"
        titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
      >
      </u-navbar>
      <view class="title">
        <view>任务记录</view>
        <view>{{ userData.balance }}</view>
      </view>
      <view class="tabs">
        <u-tabs
          :list="nav"
          lineColor="#fff"
          lineWidth="180rpx"
          lineHeight="1rpx"
          :scrollable="false"
          :current="current"
          activeStyle="color:#fff;font-size:28rpx;"
          @change="navChange"
        ></u-tabs>
      </view>
      <view class="list" v-if="isArray">
        <view class="boxstyle" v-for="(item, index) in list" :key="index">
          <view class="box">
            <view class="time flex items-center">
              <image
                src="/static/img/bg-007.png"
                mode="widthFix"
                style="width: 26rpx"
                class="mr-4"
              />
              抢单时间：{{ item.orderTime }}</view
            >
            <view class="flex items-center justify-between">
              <view class="uid">
                抢单编号：{{ item.orderNo }}
                <text>{{ item.dayOrderCount }}</text>
              </view>
              <view
                :class="{
                  loading: item.status === 0,
                  success: item.status === 1,
                  dongjie: item.status === 2,
                }"
                class="static"
              >
                {{ item.statusStr }}
              </view>
            </view>

            <view class="goodsstyle">
              <view class="goods">
                <image class="img" :src="item.goodsImg" mode="widthFix" />
                <view class="content">
                  <view class="name">
                    {{ item.goodsName }}
                  </view>
                  <view class="text">
                    <view>¥ {{ item.goodsPrice }}</view>
                    <view>x {{ item.goodsCount }}</view>
                  </view>
                </view>
              </view>
            </view>
            <view class="ul">
              <view class="li">
                <text>订单总额</text>
                <text>¥ {{ item.orderAmount }}</text>
              </view>
              <view class="li">
                <text
                  >佣金<text
                    v-if="item.commissionMul >= 2"
                    class="tip-bubble tip-bubble-left"
                    >x{{ item.commissionMul }}</text
                  ></text
                >
                <text>¥ {{ item.commission }}</text>
              </view>
              <view class="li">
                <text>预计返还</text>
                <text class="moeny">¥ {{ item.returnAmount }}</text>
              </view>
              <view class="li right" v-if="item.status === 0">
                <view class="submit" @click="change(item)">提交订单</view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <u-empty class="empty" text="暂无数据" v-else />
    </u-list>
    <success ref="sucRef" @ok="ok" />
  </view>
</template>
<script>
import success from "@/components/success.vue";
export default {
  data() {
    return {
      current: 0,
      nav: [
        { name: "全部", status: undefined },
        { name: "待处理", status: 0 },
        { name: "已完成", status: 1 },
        { name: "冻结中", status: 2 },
      ],
      list: [],
      userData: {},
      loading: false,
      finished: false,
      isArray: true,
      page: 0,
    };
  },
  methods: {
    open() {
      this.getInfo();
      this.list = [];
      uni.showLoading();
      this.page = 1;
      this.dataFn();
    },
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      this.dataFn(this.page);
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData = res.data.data;
        }
      });
    },
    dataFn(page = 1, type = 1) {
      this.loading = true;
      this.$api
        .user_order_list({
          page,
          status: this.nav[this.current].status,
        })
        .then(({ data }) => {
          if (data.code == 0) {
            const vim = data.page;
            if (type === 1) {
              this.list = this.list.concat(vim.list);
            } else {
              this.list = vim.list;
            }
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
    ok() {
      this.getInfo();
      this.dataFn(1, 0);
      this.page = 1;
      this.finished = false;
    },
    change(item) {
      this.$refs.sucRef.open(item.orderNo);
    },
    navChange(e) {
      this.current = e.index;
      this.ok();
    },
  },
  components: { success },
};
</script>
<style lang="scss" scoped>
.main {
  padding-top: var(--status-bar-height);
  .container {
    .title {
      font-size: 46rpx;
      font-weight: 600;
      display: flex;
      justify-content: space-between;
      position: relative;
      align-items: center;
      z-index: 3;
      padding: 40rpx 30rpx;
      background: #1e1e1e;

      view {
        color: $white;
        font-size: 32rpx;
      }
      view:nth-child(2) {
        font-size: 64rpx;
        font-weight: 500;
      }
    }
  }
  .tabs {
    padding-bottom: 10rpx;
    border-top: 1rpx solid rgba($white, 0.2);
    position: relative;
    background: #1e1e1e;
    z-index: 3;
  }
}
/deep/.u-tabs__wrapper__nav__line {
  bottom: 2rpx !important;
}
.boxstyle {
  padding: 30rpx;
  position: relative;
  z-index: 3;

  .box {
    padding: 30rpx;
    background-color: #1e1e1e;
    border-radius: 16rpx;
    background-size: 100% 100%;
    position: relative;
    .uid,
    .time {
      color: $white;
      font-size: 24rpx;
      padding-bottom: 10rpx;
      text {
        padding: 1px;
        border-radius: 50%;
        background: #f56c6c;
        border: 1rpx solid $white;
        width: 34rpx;
        height: 34rpx;
        display: inline-block;
        line-height: 30rpx;
        color: $white;
        font-size: 20rpx;
        text-align: center;
        margin-left: 10rpx;
      }
    }
    .uid {
      padding-bottom: 0;
    }
  }
  .static {
    background: rgba($white, 0.3);
    border-radius: 4rpx;
    border: 1rpx solid rgba($white, 0.6);
    color: $white;
    font-size: 20rpx;
    padding: 4rpx 8rpx;
    &.success {
      background: rgba($green, 0.3);
      color: $green;
      border: 1rpx solid rgba($green, 0.6);
    }
    &.loading {
      background: rgba($orange, 0.3);
      color: $orange;
      border: 1rpx solid rgba($orange, 0.6);
    }
    &.dongjie {
      background: rgba($red, 0.3);
      color: $red;
      border: 1rpx solid rgba($red, 0.6);
    }
  }
  .goodsstyle {
    margin: 20rpx 0;
  }
  .goods {
    margin: 20rpx 0;
    display: flex;
    overflow: hidden;
    justify-content: space-between;
    align-items: center;
    .img {
      width: 128rpx;
      max-height: 128rpx;
      border-radius: 8rpx;
      padding: 8rpx;
      background-color: $white;
    }
    .content {
      padding-left: 20rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: calc(100% - 158rpx);
      color: $white;
    }
    .name {
      display: -webkit-box;
      overflow: hidden;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      font-size: 24rpx;
      line-height: 2;
      margin-right: 20rpx;
      width: calc(100% - 100rpx);
    }
    .text {
      font-size: 20rpx;
      width: 100rpx;
      text-align: right;
      view:nth-child(2) {
        color: #ffffffa6;
      }
    }
  }
  .ul {
    background: #303030;
    padding: 24rpx;
    border-radius: 8rpx;
    .li {
      color: #ffffffa6;
      display: flex;
      justify-content: space-between;
      font-size: 24rpx;
      align-items: center;
      padding-bottom: 24rpx;
      &:nth-child(3) {
        padding-bottom: 10rpx;
      }
      &:nth-child(4) {
        padding-bottom: 10rpx;
      }
      &.right {
        justify-content: flex-end;
      }
    }
    .submit {
      margin-top: 30rpx;
      color: $white;
      font-size: 30rpx;
      height: 64rpx;
      width: 176rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 100rpx;
      border: 1rpx solid $white;
    }
    text {
      font-size: 28rpx;
    }
    .moeny {
      color: #f56c6c;
      font-size: 28rpx;
      font-weight: 600;
    }
  }
}
.list {
  padding-bottom: calc(constant(safe-area-inset-bottom) + 180rpx);
  padding-bottom: calc(env(safe-area-inset-bottom) + 180rpx);
  position: relative;
  z-index: 3;
}
.tip-bubble {
  position: relative;
  background-color: red;
  width: 88rpx;
  height: 40rpx;
  color: #313131;
  text-align: center;
  border-radius: 0 8rpx 8rpx 0;
  display: inline-block;
  margin-left: 50rpx;
  font-weight: bold;

  &::after {
    content: "";
    position: absolute;
    width: 0;
    height: 0;
    border: 20rpx solid;
  }
  &.tip-bubble-left::after {
    border-right-color: red;
    top: 0;
    right: 100%;
  }
}
/deep/.uni-scroll-view {
  position: relative;
  z-index: 5;
}
</style>
