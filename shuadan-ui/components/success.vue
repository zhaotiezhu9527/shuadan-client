<template>
  <u-modal
    :show="show"
    width="95vw"
    :showConfirmButton="false"
    :showCancelButton="false"
  >
    <view class="flex">
      <view class="message_block">
        <view class="message_info">
          <image mode="widthFix" class="center" src="@/static/img/qd.png" />
        </view>
        <view class="message_info_content">
          <view class="boxstyle">
            <view class="box">
              <view class="time">抢单时间：2023年06月05日 19:48:35</view>
              <view class="uid">
                UB2306051948357664<text class="num">1</text
                ><text class="txt">加急单</text>
              </view>
              <view class="goodsstyle">
                <view class="goods">
                  <image
                    class="img"
                    src="@/static/img/goods.jpg"
                    mode="widthFix"
                  />
                  <view class="content">
                    <view class="name">
                      汤河店
                      户外冲锋裤男女可脱卸秋冬季加绒加厚保暖软壳防风防水登山滑雪裤
                    </view>
                    <view class="text">
                      <view>¥ 179.00</view>
                      <view>x 11</view>
                    </view>
                  </view>
                </view>
              </view>
              <view class="redb">可用余额不足，还需充值7929.03</view>
              <view class="ul">
                <view class="li">
                  <text>订单总额</text>
                  <text>¥ 1969.00</text>
                </view>
                <view class="li">
                  <text
                    >佣金<text class="tip-bubble tip-bubble-left"
                      >x2</text
                    ></text
                  >
                  <text>¥ 1969.00</text>
                </view>
                <view class="li">
                  <text>预计返还</text>
                  <text class="moeny">¥ 1969.00</text>
                </view>
              </view>
              <image
                class="static"
                src="@/static/img/succ.png"
                mode="widthFix"
              />
            </view>
          </view>
          <view class="btn">
            <u-button
              shape="circle"
              color="#2f3848"
              plain
              text="暂不提交"
            ></u-button>
            <u-button shape="circle" color="#2f3848" text="立即提交"></u-button>
          </view>
        </view>
      </view>
      <u-icon
        @click="show = false"
        class="close"
        size="70rpx"
        color="#fff"
        name="close-circle"
      ></u-icon>
    </view>
  </u-modal>
</template>

<script>
export default {
  data() {
    return {
      show: true,
      content: "测试内容",
    };
  },
  methods: {},
};
</script>

<style scoped lang="scss">
/deep/ .u-popup__content {
  background: transparent;
}
/deep/ .u-modal__content {
  padding: 0;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  border-radius: 20rpx;
  width: 100%;
  background: linear-gradient(1turn, #fff, #ffcc2c);
  position: relative;
  &::after {
    content: "";
    top: 0;
    left: 0;
    background: url("@/static/img/bg_05.png") 0rpx no-repeat;
    background-size: 100%;
    position: absolute;
    height: 70%;
    width: 100%;
    z-index: 1;
  }
  .message_info {
    position: relative;
    z-index: 2;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 30rpx 0;
    .center {
      width: 340rpx;
      margin: 0 20rpx;
      height: 56rpx;
    }
  }
  .message_info_content {
    border-radius: 20rpx;
    margin: 0 10rpx 0;
    position: relative;
    z-index: 2;
  }
  .rich {
    height: 100%;
    width: 100%;
    display: block;
    overflow-y: auto;
  }
}
.flex {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
}
.close {
  margin-top: 80rpx;
}
.boxstyle {
  position: relative;
  z-index: 3;
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
      .num {
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
      .txt {
        font-weight: 600;
        color: red;
        font-size: 35rpx;
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
  .redb {
    background: #ff575c;
    color: #fff;
    width: 100%;
    text-align: center;
    font-size: 24rpx;
    padding: 5rpx 0;
    margin-bottom: 20rpx;
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
.btn {
  padding: 40rpx 60rpx;
  display: flex;
  gap: 50rpx;
  /deep/.u-button {
    background-color: transparent;
  }
  /deep/.u-button__text {
    font-size: 32rpx !important;
  }
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
</style>
