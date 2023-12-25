<template>
  <view class="main">
    <u-list class="container" @scrolltolower="load">
      <view class="title">
        <view>{{ $t("taskRecord") }}</view>
        <view class="sub_title">
          <view>{{ $t("remainingAssets") }}：</view>
          <view>{{ $t("currencySymbol") }} {{ keepTwoDecimalFull(userData.balance) }}</view>
        </view>
      </view>
      <view class="tabstyle">
        <view class="tabs">
          <u-tabs
            :list="nav"
            lineColor="#2E68F2"
            lineWidth="60rpx"
            lineHeight="6rpx"
            :scrollable="false"
            :current="current"
            activeStyle="color:#2E68F2;"
            @change="navChange"
          ></u-tabs>
        </view>
      </view>
      <view class="list" v-if="isArray">
        <view class="boxstyle" v-for="(item, index) in list" :key="index">
          <view class="box">
            <view class="uid">
              <!-- {{ $t("grabOrder") }} -->
              {{ $t("number") }}：{{ item.orderNo }}
              <text class="ml-10">{{ item.dayOrderCount }}</text>
              <text v-if="item.status === 0" class="static loading">{{
                $t("pending")
              }}</text>
              <text v-else-if="item.status === 1" class="static success">{{
                $t("completed")
              }}</text>
              <text v-else-if="item.status === 2" class="static error">{{
                $t("freezing")
              }}</text>
            </view>
            <view class="goodsstyle">
              <view class="goods">
                <image class="img" :src="item.goodsImg" mode="widthFix" />
                <view class="content">
                  <view class="name">
                    {{ item.goodsName }}
                  </view>
                  <view class="text">
                    <view>
                      {{ $t("currencySymbol") }} {{ keepTwoDecimalFull(item.goodsPrice) }}
                    </view>
                    <view>x {{ item.goodsCount }}</view>
                  </view>
                  <view class="text yj">
                    <view>
                      {{ $t("commission") }}：
                      {{ $t("currencySymbol") }} {{ keepTwoDecimalFull(item.commission) }}
                    </view>
                    <view class="num">x {{ item.commissionMul }}</view>
                  </view>
                </view>
              </view>
            </view>

            <view class="time">
              {{ $t("time") }} 
              <!-- {{ $t("grabOrder") }} -->
              ：{{ item.orderTime }}
              <text class="txt" v-if="item.orderType == 0">
                [{{ item.promptText }}]
              </text>
            </view>
            <view class="ul">
              <!-- <view class="li">
                <text>{{ $t("orderTotal") }}</text>
                <text>{{ $t("currencySymbol") }} {{ item.orderAmount }}</text>
              </view> -->
              <view class="li">
                <text>{{ $t("expectedReturn") }}：</text>
                <text class="moeny">
                  {{ $t("currencySymbol") }} {{ keepTwoDecimalFull(item.returnAmount) }}</text
                >
              </view>
              <view class="li flex" v-if="item.status === 0">
                <view class="submit" @click="change(item)">{{
                  $t("submitOrder")
                }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <u-empty class="empty" :text="$t('noData')" v-else />
    </u-list>
    <success ref="sucRef" @ok="ok" />
  </view>
</template>
<script>
import success from "@/components/success.vue";
import { keepTwoDecimalFull } from "@/plugins/util";
export default {
  props:['userData'],
  data() {
    return {
      keepTwoDecimalFull,
      current: 0,
      nav: [
        { name: this.$t("all"), status: undefined },
        { name: this.$t("pending"), status: 0 },
        { name: this.$t("completed"), status: 1 },
        { name: this.$t("freezing"), status: 2 },
      ],
      list: [],
      loading: false,
      finished: false,
      isArray: true,
      page: 0,
    };
  },
  methods: {
    open() {
      // this.getInfo();
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
    background: linear-gradient(#dbe9fa, #f3f7fa);
    .title {
      font-size: 26rpx;
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
        font-size: 26rpx;
        font-weight: 400;
      }
    }
    .sub_title {
      display: flex;
      view {
        color: red;
        padding-left: 10rpx;
      }
    }
  }
  .tabstyle {
    padding: 0 30rpx;
    .tabs {
      border-radius: 20rpx;
      padding: 20rpx 0;
      background: #fff;
      position: relative;
      z-index: 3;
      text-align: center;
      font-size: 24rpx;
    }
  }
}
.boxstyle {
  padding: 30rpx 30rpx 0;
  position: relative;
  z-index: 3;
  &:nth-last-child(1) {
    border-bottom: 0;
  }
  .box {
    padding: 30rpx;
    background: #fff;
    background-size: 100% 100%;
    position: relative;
    border-radius: 16rpx;
    .uid,
    .time {
      font-size: 24rpx;
      padding-bottom: 10rpx;
      position: relative;
      .ml-10 {
        margin-left: 10rpx;
        padding: 1px;
        border-radius: 50%;
        background-color: red;
        width: 36rpx;
        height: 36rpx;
        line-height: 34rpx;
        color: #fff;
        font-size: 24rpx;
        display: inline-block;
        text-align: center;
      }
    }
    .uid {
      padding-bottom: 0;
    }
    .time {
      text {
        position: absolute;
        top: 0;
        right: 0;
        color: #ff0000;
      }
    }
  }
  .static {
    position: absolute;
    right: 0;
    top: 0;
    font-size: 24rpx;
    text-align: center;
    &.error {
      color: red;
    }
    &.success {
      color: #2e68f2;
    }
    &.loading {
      color: #ff6b21;
    }
  }
  .goodsstyle {
    margin: 20rpx 0;
  }
  .goods {
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
      font-size: 26rpx;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
    }
    .yj {
      font-weight: 400;
      font-size: 24rpx;
      .num {
        color: #ff6b21;
      }
    }
  }
  .ul {
    .li {
      display: flex;
      align-items: center;
      padding-bottom: 10rpx;
    }
    .flex {
      display: flex;
      justify-content: flex-end;
    }
    .submit {
      background: #2e68f2;
      color: #fff;
      font-size: 28rpx;
      text-align: center;
      padding: 15rpx 30rpx;
      border-radius: 50rpx;
    }
    text {
      font-size: 24rpx;
    }
    .moeny {
      color: #ff9a2c;
      font-size: 34rpx;
    }
  }
}
.list {
  padding-bottom: calc(constant(safe-area-inset-bottom) + 160rpx);
  padding-bottom: calc(env(safe-area-inset-bottom) + 160rpx);
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
