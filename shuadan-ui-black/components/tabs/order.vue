<template>
  <view class="main">
    <u-list class="container" @scrolltolower="load">
      <u-navbar
        placeholder
        :border="false"
        :title="$t('task_log')"
        fixed
        bgColor="#1e1e1e"
        leftIconSize="0"
        safe-area-inset-top
        height="100rpx"
        titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
      >
      </u-navbar>
      <!-- <view class="title">
        <view>{{ $t("residue_balance") }}（{{ $t("rmb") }}）</view>
        <view>{{ userData.balance }}</view>
      </view> -->
      <view class="tabs"
        v-if="showTap"
        >
        <u-tabs
          :list="navList"
          lineColor="#fff"
          lineWidth="80rpx"
          lineHeight="1rpx"
          :scrollable="false"
          :current="current"
          activeStyle="color:#fff"
          @click="navChange"
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
              <!-- {{ $t("ordertime") }}：{{ item.orderTime }} -->
              <label class="red-text" v-if="item.promptText">{{item.promptText}}</label>
              </view
            >
            <view class="flex items-center justify-between">
              <view class="uid">
                
                {{ $t("order_no") }}：{{ item.orderNo }}
                <text>{{ item.dayOrderCount }}</text>
              </view>
              <view
                :class="{
                  loading: item.status === 0,
                  success: item.status === 1,
                  dongjie: item.status === 2,
                  loading: item.status === 4,
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
                  <!-- <view class="text">
                    <view>{{ $t("rmb_icon") }} {{ item.goodsPrice }}</view>
                    <view>x {{ item.goodsCount }}</view>
                  </view> -->
                </view>
              </view>
            </view>
            <view class="ul">
              <view class="li">
                <text>{{ $t("order_amount") }}</text>
                <text> {{ Number(item.orderAmount).toFixed(2) }} {{ $t("rmb_icon") }}</text>
              </view>
              <view class="li">
                <text
                  >{{ $t("commission")
                  }}
                  <!-- <text
                    v-if="item.commissionMul >= 2"
                    class="tip-bubble tip-bubble-left"
                    >x{{ item.commissionMul }}</text
                  > -->
                  </text>
                <text> {{ Math.floor(item.commission * 100 ) / 100 }} {{ $t("rmb_icon") }}</text>
              </view>
              <!-- <view class="li">
                <text>
                  {{ $t("return_amount") }}
                </text>
                <text class="moeny">
                  {{ $t("rmb_icon") }} {{ Math.floor(item.returnAmount * 100 ) / 100 }}
                </text>
              </view> -->
              <view class="li right" v-if="item.status === 0">
                <view class="submit" @click="change(item)">
                  {{ $t("oredr_submit") }}
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <u-empty class="empty" :text="$t('nodata')" v-else />
    </u-list>
    <success ref="sucRef" @ok="ok" />
  </view>
</template>
<script>
import success from "@/components/success.vue";
export default {
  props:['userData'],
  data() {
    return {
      current: 0,
      navList: [
        { name: this.$t("all"), status: "" },
        { name: this.$t("loading"), status: 0 },
        { name: this.$t("success"), status: 1 },
        { name: this.$t("error"), status: 2 },
      ],
      list: [],
      // userData: {},
      loading: false,
      finished: false,
      isArray: true,
      page: 0,
      showTap: false,
    };
  },
  methods: {
    open() {
      uni.showLoading();
      // this.getInfo();
      this.list = [];
      this.page = 1;
      this.dataFn();
      this.$nextTick(() => {
        this.showTap = true
      })
    },
    load() {
      if (this.loading || this.finished) return false;
      this.page++;
      this.dataFn(this.page);
    },
    //用户列表数据
    // getInfo() {
    //   this.$api.user_info().then((res) => {
    //     if (res.data.code == 0) {
    //       this.userData = res.data.data;
    //     }
    //   });
    // },
    dataFn(page = 1, type = 1) {
      this.loading = true;
      this.$api
        .user_order_list({
          page,
          status: this.navList[this.current].status,
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
      // this.getInfo();
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
  .container {
    .title {
      font-size: 40rpx;
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
  padding: 30rpx 30rpx 0;
  position: relative;
  z-index: 3;

  .box {
    padding: 20rpx 30rpx;
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
      .red-text{
        margin-left: 20rpx;
        color: #f56c6c;
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
      width: calc(100% - 120rpx);
    }
    .text {
      font-size: 20rpx;
      width: 120rpx;
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
      padding-bottom: 10rpx;
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
      background-color: #7000ed;
      margin-top: 10rpx;
      color: $white;
      font-size: 28rpx;
      height: 50rpx;
      width: 176rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 100rpx;
      // border: 1rpx solid $white;
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
