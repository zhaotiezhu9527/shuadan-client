<template>
  <view class="main">
    <u-navbar
      placeholder
      :border="false"
      title="客服中心"
      fixed
      bgColor="#000000"
      leftIconSize="0"
      safe-area-inset-top
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="container">
      <view class="title">
        <view class="sub_title">如遇到问题需要帮助</view>
        <view class="sub_title">请联系在线客服</view>
      </view>
      <image class="image" src="@/static/img/bg_02.png" mode="widthFix" />
    </view>
    <view class="customer_type">
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
    position: relative;
    display: flex;
    justify-content: flex-end;
    .title {
      position: absolute;
      left: 32rpx;
      top: 50%;
      transform: translateY(-60%);
    }
    .sub_title {
      font-size: 28rpx;
      color: #ffffffd9;
    }
    .image {
      width: 450rpx;
    }
  }
  .customer_type {
    position: relative;
    z-index: 3;
  }
  .list {
    display: flex;
    gap: 16rpx;
    padding: 0 30rpx;
    position: relative;
    z-index: 4;
    flex-wrap: wrap;
    padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
    .item {
      border-radius: 16rpx;
      width: calc(50% - 8rpx);
      padding: 32rpx;
      background: linear-gradient(134.61deg, #333333 0.81%, #1e1e1e 97.27%),
        conic-gradient(
          from 180deg at 50% 50%,
          rgba(245, 211, 172, 0) 0deg,
          rgba(245, 211, 172, 0.38) 45deg,
          rgba(245, 211, 172, 0) 84.38deg,
          rgba(245, 211, 172, 0) 133.12deg,
          rgba(245, 211, 172, 0.37) 187.5deg,
          rgba(245, 211, 172, 0) 230.62deg,
          rgba(245, 211, 172, 0) 360deg
        );
      border: 1px solid;

      border-image-source: conic-gradient(
        from 180deg at 50% 50%,
        rgba(245, 211, 172, 0) 0deg,
        rgba(245, 211, 172, 0.38) 45deg,
        rgba(245, 211, 172, 0) 84.38deg,
        rgba(245, 211, 172, 0) 133.12deg,
        rgba(245, 211, 172, 0.37) 187.5deg,
        rgba(245, 211, 172, 0) 230.62deg,
        rgba(245, 211, 172, 0) 360deg
      );
    }
    .name {
      font-size: 28rpx;
      font-weight: 600;
      color: #ffffffd9;
    }
    .img {
      margin: 16rpx 0;
      width: 100%;
    }
    .txt {
      font-size: 24rpx;
      color: #ffffffd9;
    }
    .time {
      display: flex;
      align-items: center;
      padding-top: 16rpx;
      .icon {
        width: 32rpx;
        margin-right: 8rpx;
      }
      text {
        font-size: 24rpx;
        color: #ffffffa6;
      }
    }
  }
}
</style>
