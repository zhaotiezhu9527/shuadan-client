<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="信息公告"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#000000"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
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
          <view class="text-content">
            <view class="prompt">温馨提示：</view>
            <view class="details" v-html="item.content"> </view>
          </view>
          <!-- <view class="time">{{ item.pushTime }}</view> -->
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
  background: linear-gradient(#000, #2d154d);
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
      background-color: #1e1f1e;
      padding: 24rpx;
      border-radius: 8rpx;
      margin-bottom: 18rpx;
      color: #dedede;
      .icon-img {
        width: 40rpx;
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
      background-color: #1e1f1e;
      border-radius: 8rpx;
      margin-top: 20rpx;
      color: #dedede;
      .notice{
        border-bottom: 1px solid #393939;
        padding: 24rpx 0;
        text-indent: 24rpx;
      }
      .text-content{
        padding: 24rpx ;
      }
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
