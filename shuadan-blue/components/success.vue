<template>
  <view>
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
                <view class="time">{{$t('grabOrder') }}{{$t('time') }}：{{ items.orderTime }}</view>
                <view class="uid">
                  {{ items.orderNo
                  }}<text class="num">{{ items.countNum }}</text
                  ><text class="txt" v-if="items.orderType == 0">
                    {{ items.promptText }}
                  </text>
                </view>
                <view class="goodsstyle">
                  <view class="goods">
                    <image class="img" :src="items.goodsImg" mode="widthFix" />
                    <view class="content">
                      <view class="name">
                        {{ items.goodsName }}
                      </view>
                      <view class="text">
                        <view>{{$t('currencySymbol') }} {{ items.goodsPrice }}</view>
                        <view>x {{ items.goodsCount }}</view>
                      </view>
                    </view>
                  </view>
                </view>
                <view class="redb" v-if="items.balanceSub < 0">
                  {{$t('insufficientBalance') }}{{ items.balanceSub }}
                </view>
                <view class="ul">
                  <view class="li">
                    <text>{{$t('orderTotal') }}</text>
                    <text>{{$t('currencySymbol') }} {{ items.orderAmount }}</text>
                  </view>
                  <view class="li">
                    <text
                      >{{$t('commission') }}<text
                        v-if="items.commissionMul >= 2"
                        class="tip-bubble tip-bubble-left"
                        >x{{ items.commissionMul }}</text
                      ></text
                    >
                    <text>{{$t('currencySymbol') }} {{ items.commission }}</text>
                  </view>
                  <view class="li">
                    <text>{{$t('expectedReturn') }}</text>
                    <text class="moeny">{{$t('currencySymbol') }} {{ items.forecastReturn }}</text>
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
                @click="show = false"
                :text="$t('notSubmitted')"
              ></u-button>
              <u-button
                shape="circle"
                color="#2f3848"
                :text="$t('submitNow')"
                :loading="loading"
                @click="change"
              ></u-button>
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
    <view class="maskLoading" v-if="loading">
      <view class="content">
        <image class="img" src="@/static/img/10001.gif" mode="widthFix" />
        <p class="txt">{{$t('remoteHost') }}</p>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      show: false,
      loading: false,
      items: {},
    };
  },
  methods: {
    open(e) {
      this.show = true;
      this.dataFn(e);
    },
    dataFn(e) {
      this.$api.order_detail(e).then(({ data }) => {
        if (data.code == 0) {
          this.items = data.data;
        } else {
          this.$base.show(data.msg);
        }
      });
    },
    change() {
      this.loading = true;
      setTimeout(() => {
        this.$api.order_pay(this.items.orderNo).then(({ data }) => {
          if (data.code == 0) {
            this.$emit("ok");
            this.$base.show(data.msg);
            this.loading = false;
            if (data.data.orderNo) {
              this.dataFn(data.data.orderNo);
            } else {
              this.show = false;
            }
          } else {
            this.loading = false;
          }
        });
      }, 4000);
    },
  },
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
