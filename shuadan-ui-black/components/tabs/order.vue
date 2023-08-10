<template>
  <view class="main">
    <u-list class="container" @scrolltolower="load">
      <view class="title">
        <view>任务记录</view>
        <view>{{ userData.balance }}</view>
      </view>
      <view class="sub_title">
        <view>本数据由全民任务官方提供</view>
        <view>剩余可用资产(元)</view>
      </view>
      <view class="tabs">
        <u-tabs
          :list="nav"
          lineColor="#ff9a2c"
          lineWidth="60rpx"
          lineHeight="6rpx"
          :scrollable="false"
          :current="current"
          activeStyle="color:#ff9a2c;font-weight:600"
          @change="navChange"
        ></u-tabs>
      </view>
      <view class="list" v-if="isArray">
        <view class="boxstyle" v-for="(item, index) in list" :key="index">
          <view class="box">
            <view class="time">抢单时间：{{ item.orderTime }}</view>
            <view class="uid"
              >抢单编号：{{ item.orderNo
              }}<text>{{ item.dayOrderCount }}</text></view
            >
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
              <view class="li" v-if="item.status === 0">
                <view class="submit" @click="change(item)">提交订单</view>
              </view>
            </view>
            <image
              v-if="item.status === 0"
              class="static"
              src="@/static/img/dai.png"
              mode="widthFix"
            />
            <image
              v-else-if="item.status === 1"
              class="static"
              src="@/static/img/succ.png"
              mode="widthFix"
            />
            <image
              v-else-if="item.status === 2"
              class="static"
              src="@/static/img/dongjie.png"
              mode="widthFix"
            />
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
    height: calc(100vh - var(--status-bar-height)) !important;
    &::before {
      content: "";
      position: absolute;
      width: 100%;
      height: 20%;
      background-image: url("@/static/img/bg-01.png");
      background-size: 100%;
      top: 0;
      left: 0;
      z-index: 2;
    }
    &::after {
      content: "";
      position: absolute;
      width: 100%;
      height: 50%;
      background: linear-gradient(1turn, #fff, #bac3d2);
      top: 0;
      left: 0;
      z-index: 1;
    }
    .title {
      font-size: 46rpx;
      font-weight: 600;
      display: flex;
      justify-content: space-between;
      position: relative;
      align-items: center;
      z-index: 3;
      padding: 40rpx 30rpx;
      view {
        color: #2f3848;
      }
      view:nth-child(2) {
        font-size: 40rpx;
      }
    }
    .sub_title {
      font-size: 24rpx;
      display: flex;
      justify-content: space-between;
      position: relative;
      z-index: 3;
      padding: 20rpx 30rpx 0;
      view {
        color: #7a7d82;
      }
      view:nth-child(2) {
        color: #434343;
      }
    }
  }
  .tabs {
    padding-top: 30rpx;
    padding-bottom: 10rpx;
    border-bottom: 1rpx solid #fff;
    position: relative;
    z-index: 3;
  }
}
.boxstyle {
  padding: 30rpx;
  position: relative;
  z-index: 3;
  border-bottom: 6rpx solid #eee;
  &:nth-last-child(1) {
    border-bottom: 0;
  }
  .box {
    padding: 30rpx;
    background-image: url("@/static/img/bg_04.png");
    background-size: 100% 100%;
    position: relative;
    .uid,
    .time {
      color: #9e9e9e;
      font-size: 24rpx;
      padding-bottom: 10rpx;
      text {
        padding: 1px;
        border-radius: 50%;
        background-color: red;
        width: 40rpx;
        height: 40rpx;
        line-height: 40rpx;
        color: #fff;
        font-size: 28rpx;
        display: inline-block;
        text-align: center;
      }
    }
    .uid {
      padding-bottom: 0;
    }
  }
  .static {
    position: absolute;
    right: 20rpx;
    top: 20rpx;
    width: 130rpx;
  }
  .goodsstyle {
    margin: 20rpx 0;
  }
  .goods {
    background: #f2f2f2;
    border-radius: 20rpx;
    margin: 20rpx 0;
    display: flex;
    overflow: hidden;
    justify-content: space-between;
    align-items: center;
    .img {
      width: 180rpx;
      max-height: 180rpx;
    }
    .content {
      padding: 20rpx;
      display: flex;
      flex: 1;
      justify-content: space-between;
      flex-direction: column;
      font-size: 26rpx;
      line-height: 1.5;
    }
    .text {
      display: flex;
      padding-top: 20rpx;
      font-size: 26rpx;
      justify-content: space-between;
      align-items: center;
    }
  }
  .ul {
    .li {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 30rpx;
      &:nth-child(3) {
        padding-bottom: 10rpx;
      }
      &:nth-child(4) {
        padding-bottom: 10rpx;
      }
    }
    .submit {
      background: #ff575c;
      color: #fff;
      font-size: 20rpx;
      text-align: center;
      padding: 6rpx 20rpx;
      border-radius: 10rpx;
    }
    text {
      font-size: 28rpx;
    }
    .moeny {
      color: #ff9a2c;
      font-size: 45rpx;
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
  color: #fff;
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
