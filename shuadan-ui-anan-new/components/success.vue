<template>
  <view>
    <u-modal
      :show="show"
      width="95vw"
      :showConfirmButton="false"
      :showCancelButton="false"
    >
      <view class="flex2">
        <view class="message_block">
          <view class="message_info_content">
            <view class="boxstyle">
              <view class="box">
                <view class="goodsstyle">
                  <view class="goods">
                    <image class="img" :src="items.goodsImg" mode="widthFix" />
                    <view class="content">
                      <view class="name">
                        {{ items.goodsName }}
                      </view>
                    </view>
                  </view>
                </view>
                <view class="redb" v-if="items.balanceSub < 0">
                  {{ $t("not_sufficient_funds") }}{{ items.balanceSub }}
                </view>
                <view class="ul">
                  <view class="li">
                    <text>{{ $t("order_amount") }}</text>
                    <text>{{ $t("rmb_icon") }} {{ Number(items.orderAmount).toFixed(2) }}</text>
                  </view>
                  <view class="li">
                    <text
                      >{{ $t("commission")
                      }}
                      </text>
                    <text>{{ $t("rmb_icon") }} {{ Number(items.commission).toFixed(2) }}</text>
                  </view>
                  <view class="li">
                    <text>{{ $t("return_amount") }}</text>
                    <text class="moeny"
                      >{{ $t("rmb_icon") }} {{ Number(items.forecastReturn).toFixed(2) }}</text
                    >
                  </view>
                </view>
              </view>
            </view>
            <view class="btn"
            @click="change"
            >
              {{$t('submit')}}
              
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
        <p class="txt">{{ $t("long_distance") }}</p>
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
  padding: 0 20rpx;
}
/deep/ .u-line {
  border: 0 !important;
}
.message_block {
  box-shadow: 0 0 16rpx 0 #ffb400b2;
  border-radius: 20rpx;
  width: 100%;
  background-color: #fff;
  position: relative;
  border: 1px solid;

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
    padding-bottom: 40rpx;
  }
  .rich {
    height: 100%;
    width: 100%;
    display: block;
    overflow-y: auto;
  }
}
.flex2 {
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
    background-color: #fff;
    border-radius: 16rpx;
    position: relative;
    color: #343434;
    .uid,
    .time {
      font-size: 24rpx;
      padding-bottom: 10rpx;
      .num {
        padding: 1px;
        border-radius: 50%;
        background: #f56c6c;
        border: 1rpx solid $white;
        width: 34rpx;
        height: 34rpx;
        line-height: 30rpx;
        font-size: 20rpx;
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
      text{
        color: #fff;
      }
    }
  }
  .static {
    background: rgba($white, 0.3);
    border-radius: 4rpx;
    border: 1rpx solid rgba($white, 0.6);
    color: $white;
    font-size: 20rpx;
    padding: 4rpx 8rpx;
    &.success {
      background: rgba($green, 0.3);
      color: $green;
      border: 1rpx solid rgba($green, 0.6);
    }
    &.loading {
      background: rgba($orange, 0.3);
      color: $orange;
      border: 1rpx solid rgba($orange, 0.6);
    }
    &.dongjie {
      background: rgba($red, 0.3);
      color: $red;
      border: 1rpx solid rgba($red, 0.6);
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
    margin: 20rpx 0;
    display: flex;
    overflow: hidden;
    justify-content: space-between;
    align-items: center;
    .img {
      width: 128rpx;
      max-height: 128rpx;
      border-radius: 8rpx;
      padding: 8rpx;
      background-color: $white;
    }
    .content {
      padding-left: 20rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: calc(100% - 158rpx);
    }
    .name {
      display: -webkit-box;
      overflow: hidden;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      font-size: 24rpx;
      line-height: 2;
      margin-right: 20rpx;
      width: calc(100% - 120rpx);
    }
    .text {
      font-size: 20rpx;
      width: 120rpx;
      text-align: right;
      view:nth-child(2) {
        color: #ffffffa6;
      }
    }
  }
  .ul {
    background: #303030;
    padding: 24rpx;
    border-radius: 8rpx;
    .li {
      color: #ffffffa6;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 24rpx;
      align-items: center;
      padding-bottom: 24rpx;
      &:nth-child(3) {
        padding-bottom: 10rpx;
      }
      &:nth-child(4) {
        padding-bottom: 10rpx;
      }
    }
    text {
      font-size: 28rpx;
    }
    .moeny {
      color: #f56c6c;
      font-size: 28rpx;
      font-weight: 600;
    }
  }
}
.btn {
  font-size: 32rpx;
  text-align: center;
  background-color: #7d7d7d;
  border-radius: 10rpx;
  height: 72rpx;
  line-height: 72rpx;
  color: #fff;
  width: 92%;
  margin: auto;
}
.tip-bubble {
  position: relative;
  background-color: red;
  width: 88rpx;
  height: 40rpx;
  color: #303030;
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
.header {
  text-align: center;
  .text {
    padding-top: 32rpx;
    padding-bottom: 64rpx;
    color: $white;
    font-size: 32rpx;
    font-weight: 500;
  }
}
</style>
