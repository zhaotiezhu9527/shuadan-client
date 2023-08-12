<template>
  <view class="main">
    <view class="container">
      <view class="title">客服中心</view>
      <view class="sub_title">如遇到问题需要帮助请联系在线客服</view>
    </view>
    <view class="customer_type">
      <image
        mode="widthFix"
        class="customerServiceImg"
        src="@/static/img/bg-02.png"
      />
      <view class="online1">
        <view
          class="onlineservice"
          v-for="(item, index) in list"
          :key="index"
          @click="route(item)"
        >
          <view class="servicetime">
            <image mode="widthFix" class="image" src="@/static/img/zixun.png" />
            <view class="con">
              <view class="onlineTitle">{{ item.serviceName }}</view>
              <view class="onlineText">{{ item.workTime }}</view>
            </view>
          </view>
          <image mode="widthFix" class="icon" src="@/static/img/back.png" />
        </view>
      </view>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      list: [],
    };
  },
  methods: {
    open() {
      this.$api.system_customerService_list().then(({ data }) => {
        if (data.code == 0) {
          this.list = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    route(item) {
      uni.navigateTo({
        url: "/pages/onlineService?path=" + item.link,
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main {
  .container {
    height: 230rpx;
    padding: calc(var(--status-bar-height) + 40rpx) 30rpx 0;

    position: relative;
    z-index: 2;

    .title {
      font-size: 52rpx;
      font-weight: 600;
      color: #2f3848;
    }
    .sub_title {
      font-size: 28rpx;
      padding-top: 20rpx;
      color: #2f384880;
    }
  }
  .customerServiceImg {
    width: 100%;
  }
  .online1 {
    background-color: #fffbf9;
    padding-top: 20rpx;
    padding-bottom: calc(240rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(240rpx + env(safe-area-inset-bottom));
  }
  .onlineservice {
    box-shadow: 0rpx 8rpx 20rpx 0rpx rgba(0, 0, 0, 0.06);
    background: #eff6fa;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 20rpx;
    width: 90%;
    margin: 20rpx auto;
  }
  .servicetime {
    padding: 30rpx 30rpx 30rpx 38rpx;
    display: flex;
    align-items: center;
    .con {
      padding-left: 30rpx;
    }
    .image {
      width: 100rpx;
    }
  }
  .icon {
    width: 36rpx;
    margin-right: 30rpx;
  }

  .customer_type {
    border-radius: 30rpx;
    background-color: #fff;
    margin-top: -60rpx;
    position: relative;
    z-index: 3;
  }
}
</style>
