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
          <view class="message_info">{{ $t("matchSuccessful") }}</view>
          <view class="message_info_content">
            <view class="boxstyle">
              <view class="box">
                <view class="uid">
                  {{ $t("number") }} {{ $t("grabOrder") }}：{{ items.orderNo
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
                        <view> {{ FormatAmount(items.goodsPrice) }}</view>
                        <view>x {{ items.goodsCount }}</view>
                      </view>
                      <view class="text yj">
                        {{ $t("commission") }}

                        {{ FormatAmount(items.commission) }}
                      </view>
                    </view>
                  </view>
                </view>
                <view class="time">
                  {{ $t("time") }} 
                  <!-- {{ $t("grabOrder") }} -->
                   {{ items.orderTime }}
                </view>
                <view class="redb" v-if="items.balanceSub < 0">
                  {{ $t("insufficientBalance") }}：
                  {{ FormatAmount(items.balanceSub) }}
                  ≈
                  {{ Number(Number(items.balanceSub) * Number(config.huilv)).toFixed(2) }} USDT
                </view>
                <view class="ul">
                  <view class="li">
                    <text class="label">{{ $t("orderTotal") }}</text>
                    <text class="value">
                      {{ FormatAmount(items.orderAmount) }}</text
                    >
                  </view>
                  <view class="li">
                    <text class="mr-10 label">{{ $t("expectedReturn") }}</text>
                    <text class="moeny">
                      {{ FormatAmount(items.forecastReturn) }}
                    </text>
                  </view>
                </view>
              </view>
            </view>
            <view class="btn">
              <u-button
                shape="circle"
                color="#2E68F2"
                plain
                @click="show = false"
                :text="$t('notSubmitted')"
              ></u-button>
              <u-button
                shape="circle"
                color="#2E68F2"
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
        <p class="txt">{{ $t("remoteHost") }}</p>
      </view>
    </view>
  </view>
</template>

<script>
import { FormatAmount } from "@/plugins/util";
export default {
  data() {
    return {
      show: false,
      loading: false,
      FormatAmount,
      items: {},
      config: {},
    };
  },
  mounted (){
    this.getConfig()
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
    getConfig(){
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
        } 
      });
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
  background: url("@/static/img/order-frame.png");
  background-repeat: round;
  background-size: 100% auto;
  position: relative;
  padding: 50rpx 0;
  .message_info {
    position: relative;
    z-index: 2;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 30rpx 0;
    color: #2e68f2;
    font-weight: 600;
    font-size: 36rpx;
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
    padding: 0 30rpx;
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
        width: 36rpx;
        height: 36rpx;
        line-height: 34rpx;
        color: #fff;
        font-size: 24rpx;
        margin-left: 10rpx;
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
      padding-top: 5rpx;
      font-size: 30rpx;
      justify-content: space-between;
      align-items: center;
      color: #7e869f;
      font-weight: 600;
      &.yj {
        font-weight: 400;
      }
    }
  }
  .ul {
    .li {
      display: flex;
      align-items: center;
      padding-bottom: 10rpx;
      .mr-10 {
        margin-right: 10rpx;
      }
      &:nth-child(1) {
        justify-content: space-between;
      }
      .label {
        color: #969696;
        margin-right: 40rpx;
      }
      .value {
        color: #7e869f;
        font-weight: 600;
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
      color: #ff9a2c !important;
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
