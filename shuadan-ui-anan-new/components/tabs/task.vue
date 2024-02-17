<template>
  <view class="main">
    <u-navbar :title="$t('rob_order')" :border="true" leftIconSize="0" height="100rpx" titleStyle="font-weight:600;">
    </u-navbar>
    <!-- app 轮播图 -->
    <swiper class="app-swiper" :indicator-dots="false" :autoplay="true" :circular="true" :interval="3000"
      :duration="1000">
      <swiper-item v-for="(item, index) in taskAppList" :key="index">
        <view class="app-swiper-item">
          <image :src="item.url" class="img-fill"></image>
        </view>
      </swiper-item>
    </swiper>
    <!-- app logo -->
    <view class="task-logo">
      <image src="@/static/img/app-logo1.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo2.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo3.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo4.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo5.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo6.png" class="task-logo-img"></image>
    </view>
    <view class="schedule">
      {{ infos.todayOrderCount }} / {{ items.dayOrderCount }}
    </view>
    <!-- start btn -->
    <view class="summary-container-start-btn" @click="startCallBack">
      <text class="start-btn-text">{{$t('start_call_back')}}</text>
    </view>
    <!-- Today's results -->
    <view class="summary-container">
      <view class="summary-container-panels">
        <view class="summary-container-panel-item">
          <view class="icon-box">
            <image src="@/static/img/task-1.png" mode="aspectFill" class="img-fill"></image>
          </view>
          <view class="summary-container-panel-item-content">
            <view class="summary-container-panel-item-content-num"> ${{ Number(infos.balance).toFixed(2) }}</view>
            <view class="summary-container-panel-item-content-text">{{ $t("my_balance") }}</view>
          </view>
        </view>
        <view class="summary-container-panel-item">
          <view class="icon-box">
            <image src="@/static/img/task-2.png" mode="aspectFill" class="img-fill"></image>
          </view>
          <view class="summary-container-panel-item-content">
            <view class="summary-container-panel-item-content-num">
              ${{ Number(infos.todayIncome).toFixed(2)  }}
            </view>
            <view class="summary-container-panel-item-content-text">{{ $t("todayIncome") }}</view>
          </view>
        </view>
      </view>

    </view>
    <!-- user info -->
    <view class="user-info-container">
      <view class="user-info-container-left">
        <image :src="items.avatarUrl" mode="aspectFill" class="user-info-container-left-avatar" />
        <image :src="items.levelIcon" mode="widthFix" class="user-info-container-left-level" />
      </view>
      <view class="user-info-container-right" @click="addVip">
        <image class="user-info-container-right-icon" src="../../static/img/task-5.png" mode="aspectFill"></image>
        <text class="user-info-container-right-text">{{$t('add_vip')}}</text>
      </view>
    </view>

    <success ref="sucRef" @ok="getInfo(that)" />
    <view class="maskLoading" v-if="loading">
      <view class="content">
        <image class="img" src="@/static/img/10001.gif" mode="widthFix" />
        <p class="txt">{{ $t("matching") }}</p>
      </view>
    </view>
  </view>
</template>

<script>
  import success from "@/components/success.vue";
  import taskAppList from '@/static/js/taskApp.js'

  export default {
    components: {
      success
    },
    data() {
      return {
        lastTm: null,
        lastCollisions: [],
        items: {},
        infos: {},
        animation: [{
            type: 0,
            animationData: null,
            time: 0,
          },
          {
            type: 1,
            animationData: null,
            time: 100,
          },
          {
            type: 0,
            animationData: null,
            time: 200,
          },
        ],
        loading: false,
        vim: {},
        that: {},
        taskAppList
      };
    },
    methods: {
      open(e) {
        this.that = e;
        this.getInfo(e);
      },
      addVip() {
        uni.navigateTo({
          url: "/pages/vip",
        });
      },

      //用户列表数据,用户收益详情
      getInfo(e) {
        let index = e.level || 1;
        Promise.all([
          this.$api.user_info(),
          this.$api.user_income_detail(),
          this.$api.area_detail(index),
        ]).then((res) => {
          if (res[0].data.code == 0) {
            this.items = res[0].data.data;
          } else {
            this.$base.show(res[0].data.msg);
          }
          if (res[1].data.code == 0) {
            this.infos = res[1].data.data;
          } else {
            this.$base.show(res[1].data.msg);
          }
          if (res[2].data.code == 0) {
            this.vim = res[2].data.data;
          } else {
            this.$base.show(res[2].data.msg);
          }
        });
      },
      // 开始滚动
      startCallBack() {
        if (this.loading) return false;
        this.loading = true;
        setTimeout(() => {
          this.endCallBack();
        }, 6500);
      },
      // 重置
      endCallBack() {
        this.$api.order_match(this.vim.areaId).then(({
          data
        }) => {
          this.loading = false;
          if (data.code == 0) {
            this.getInfo(this.vim.areaId);
            this.$refs.sucRef.open(data.data.orderNo);
          }
        });
      },
      moenyFn(val) {
        if (!val) {
          return 0;
        } else if (val >= 100000) {
          return parseInt(val / 100000) + this.$t("myriad");
        } else {
          return "" + val;
        }
      }
    }
  };
</script>

<style scoped lang="scss">
  .maskLoading {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 99999;

    .content {
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      text-align: center;
    }

    .img {
      width: 150rpx;
      height: 150rpx;
    }

    .txt {
      color: #999;
      font-size: 24rpx;
    }
  }

  .main {
    height: 100vh;
    padding-top: 100rpx;
    padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
    overflow-y: auto;
  }

  .schedule {
    position: relative;
    text-align: center;
    font-size: 48rpx;
    color: #3A3A3A;
    line-height: 48rpx;
    font-weight: 500;
    margin-top: 62rpx;
  }

  .summary-container-start-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100rpx;
    margin: 32rpx;
    background-color: #333333;
    border-radius: 20rpx;
    cursor: pointer;

    .start-btn-text {
      margin: 0 16rpx;
      line-height: 30px;
      font-size: 52rpx;
      color: #fff;
      font-family: Arial;
    }

    .start-btn-icon {
      width: 48rpx;
      height: 48rpx;
    }
  }

  .summary-container {
    margin: 56rpx 32rpx 40rpx;
    padding: 40rpx 30rpx 30rpx;
    border-radius: 20rpx;
    background-color: #C1D0F1;

    &-balance {
      text-align: center;
      color: #333;

      &-num {
        font-size: 88rpx;
      }

      &-text {
        font-size: 24rpx;
      }
    }

    &-panels {
      display: flex;
      justify-content: space-between;
      column-gap: 24rpx;
    }

    &-panel-item {
      flex: 1;
      padding: 24rpx;
      background-color: #fff;
      border-radius: 20rpx;

      .icon-box {
        width: 100rpx;
        height: 100rpx;
        flex-shrink: 0;
        overflow: hidden;
        display: block;
        margin: 20rpx auto;
      }

      &-content {
        flex: 1;
        text-align: center;

        &-num {
          color: #3A3A3A;
          font-size: 40rpx;
        }

        &-text {
          margin-top: 8rpx;
          color: #373737;
          font-size: 18rpx;
        }
      }
    }
  }

  .user-info-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 32rpx;
    padding: 30rpx;
    background-color: rgba(239, 244, 253, 0.74);
    border-radius: 20rpx;

    &-left {
      display: flex;
      align-items: center;

      &-avatar {
        width: 120rpx;
        height: 120rpx;
        margin-right: 12rpx;
      }

      &-level {
        width: 120rpx;
      }
    }

    &-right {
      display: flex;
      align-items: center;
      padding: 16rpx 10rpx;
      background-color: #7D7D7D;
      border: 1rpx solid #575651;
      border-radius: 10rpx;

      &-icon {
        width: 40rpx;
        height: 40rpx;
        margin-right: 10rpx;
      }

      &-text {
        color: #fff;
        font-size: 36rpx;
        font-family: Arial;
      }
    }
  }

  .task-title {
    display: flex;

    .task-title-img {
      width: 340rpx;
      height: 400rpx;

      &:nth-child(2) {
        margin-left: 26rpx;
      }
    }
  }

  .task-logo {
    display: flex;
    margin: 32rpx;

    .task-logo-img {
      width: 120rpx;
      height: 120rpx;
    }
  }

  .app-swiper {
    width: 100%;
    height: auto;
    aspect-ratio: 1.794;

    .app-swiper-item {
      height: 100%;
    }
  }
</style>