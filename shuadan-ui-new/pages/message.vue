<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="信息公告"
      fixed
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#F2F2F2"
      height="100rpx"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="head"> </view>
      <view class="content">
        <view class="list-item">
          <image class="icon-img" src="../static/img/msg_notice.png" />
          <label>系统公告</label>
          <view class="icon"></view>
        </view>
        <view class="text" v-for="(item, index) in list" :key="index">
          <view class="notice">{{ item.title }}</view>
          <view class="prompt">温馨提示：</view>
          <view class="details" v-html="item.content"> </view>
          <view class="time">{{ item.pushTime }}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      list: [], //信息公告列表
    };
  },
  onShow() {
    this.getList();
  },
  methods: {
    //用户列表数据
    getList() {
      this.$api.notice_list().then((res) => {
        if (res.data.code == 0) {
          this.list = res.data.data;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.main {
  min-height: calc(100vh - 100rpx);
  background-color: #f0f0f0;
  .head {
    width: 100%;
    height: 372rpx;
    background: url(../static/img/msg.png) no-repeat;
    background-size: 100% 100%;
    overflow: hidden;
  }
  .notice {
    padding-bottom: 10rpx;
  }
  .content {
    position: relative;
    padding: 56rpx 30rpx;
    margin: -40rpx auto 0 auto;

    .list-item {
      display: flex;
      align-items: center;
      vertical-align: middle;
      font-size: 28rpx;
      border-bottom: 1rpx solid #eee;
      background-color: #fff;
      padding: 24rpx;
      border-radius: 8rpx;
      box-shadow: 0 3rpx 5rpx rgba(0, 0, 0, 0.1);
      margin-bottom: 18rpx;
      .icon-img {
        width: 4rpx;
        height: 40rpx;
        margin-right: 20rpx;
      }
      label {
        flex: 1;
      }
      .icon {
        width: 20rpx;
        height: 20rpx;
        position: relative;
        &:after {
          content: "";
          width: 16rpx;
          height: 16rpx;
          position: absolute;
          right: 10rpx;
          bottom: 10rpx;
          border-left: 2rpx solid #aaa;
          border-bottom: 2rpx solid #aaa;
          transform: translate(0, 50%) rotate(-135deg);
          -webkit-transform: translate(0, 50%) rotate(-135deg);
        }
      }
    }
    .text {
      font-size: 28rpx;
      border-bottom: 1rpx solid #eee;
      background-color: #fff;
      padding: 24rpx;
      border-radius: 8rpx;
      margin-top: 20rpx;
      .details {
        font-size: 24rpx;
      }
      .time {
        text-align: right;
        color: #999;
      }
    }
  }
}
</style>
