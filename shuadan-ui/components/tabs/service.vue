<template>
  <view class="main">
    <view class="container">
      <view class="title">客服中心</view>
      <view class="sub_title">如遇到问题需要帮助请联系在线客服</view>
    </view>
    <view class="customer_type">
      <image class="image" src="@/static/img/bg_02.png" mode="widthFix" />
      <view class="list">
        <view
          class="item"
          v-for="(item, index) in list"
          :key="index"
          @click="route(item)"
        >
          <view class="name">{{ item.serviceName }}</view>
          <image class="img" src="@/static/img/bg_03.png" mode="widthFix" />
          <view class="txt">{{ item.remark || "客服" }}</view>
          <view class="time">
            <image class="icon" src="@/static/img/kefu.png" mode="widthFix" />
            <text>{{ item.workTime }}</text>
          </view>
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
    open(e) {
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
    height: 280rpx;
    padding: calc(var(--status-bar-height) + 40rpx) 30rpx 0;
    background: #bac3d2;
    position: relative;
    z-index: 2;
    &::before {
      content: "";
      position: absolute;
      width: 100%;
      height: 100%;
      background-image: url("@/static/img/bg-01.png");
      background-size: 100%;
      top: 0;
      left: 0;
    }
    .title {
      font-size: 46rpx;
      font-weight: 600;
      color: #2f3848;
    }
    .sub_title {
      font-size: 24rpx;
      padding-top: 20rpx;
      color: #2f384880;
    }
  }
  .customer_type {
    border-radius: 30rpx;
    background-color: #fff;
    margin-top: -60rpx;
    position: relative;
    z-index: 3;
    .image {
      width: 450rpx;
      position: absolute;
      right: 0;
      top: calc(var(--status-bar-height) + -180rpx);
    }
    .list {
      padding-top: 120rpx;
    }
  }
  .list {
    display: flex;
    gap: 25rpx;
    padding: 0 30rpx;
    position: relative;
    z-index: 4;
    flex-wrap: wrap;
    .item {
      background-color: #f6f7f7;
      border-radius: 20rpx;
      padding: 20rpx;
    }
    .name {
      font-size: 28rpx;
      font-weight: 600;
      color: #434343;
    }
    .img {
      margin-top: 10rpx;
      width: 280rpx;
    }
    .txt {
      font-size: 24rpx;
    }
    .time {
      display: flex;
      align-items: center;
      padding-top: 40rpx;
      .icon {
        width: 40rpx;
      }
      text {
        font-size: 20rpx;
      }
    }
  }
}
</style>
