<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('notice')"
      fixed
      leftIconColor="#000"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="100rpx"
      titleStyle="color:#000;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="bg-box">
        <img class="bg-img" src="../static/img/msg.jpg" />
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
  .bg-box{
    width:100%;
    .bg-img{
      width: 100%;
    }
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
        width: 32rpx;
        height: 32rpx;
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
      .notice {
        border-bottom: 1px solid #393939;
        padding: 24rpx 0;
        text-indent: 24rpx;
      }
      .text-content {
        padding: 24rpx;
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
