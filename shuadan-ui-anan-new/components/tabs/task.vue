<template>
  <view class="main">
    <u-navbar :title="$t('rob_order')" :border="true" leftIconSize="0" height="100rpx" titleStyle="font-weight:600;">
    </u-navbar>
    <!-- app 气泡图 -->
    <view class="task-title">
      <view class="banner">
          <u-swiper 
            previousMargin="175"
            nextMargin="175"
            circular
            :autoplay="true"
            displayMultipleItems="1"
            :list="list2" class="my-swipe" indicator> </u-swiper>
        </view>
      <!-- <image src="@/static/img/task-bg2.png" class="task-title-img"></image> -->
      <!-- <image src="@/static/img/task-bg1.png" class="task-title-img"></image> -->
    </view>
    <!-- app logo -->
    <view class="task-logo">
      <image src="@/static/img/app-logo1.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo2.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo3.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo4.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo5.png" class="task-logo-img"></image>
      <image src="@/static/img/app-logo6.png" class="task-logo-img"></image>
    </view>
    <view class="schedule">
      {{ infos.todayOrderCount }} / {{ items.dayOrderCount }}
    </view>
    <!-- start btn -->
    <view class="summary-container-start-btn" @click="startCallBack">
      <text class="start-btn-text">{{$t('start_call_back')}}</text>
    </view>
    <!-- Today's results -->
    <view class="summary-container">
      <view class="summary-container-panels">
        <view class="summary-container-panel-item">
          <view class="icon-box">
            <image src="@/static/img/task-1.png" mode="aspectFill" class="img-fill"></image>
          </view>
          <view class="summary-container-panel-item-content">
            <view class="summary-container-panel-item-content-num"> ${{ Number(infos.balance).toFixed(2) }}</view>
            <view class="summary-container-panel-item-content-text">{{ $t("my_balance") }}</view>
          </view>
        </view>
        <view class="summary-container-panel-item">
          <view class="icon-box">
            <image src="@/static/img/task-2.png" mode="aspectFill" class="img-fill"></image>
          </view>
          <view class="summary-container-panel-item-content">
            <view class="summary-container-panel-item-content-num">
              ${{ Number(infos.todayIncome).toFixed(2)  }}
            </view>
            <view class="summary-container-panel-item-content-text">{{ $t("todayIncome") }}</view>
          </view>
        </view>
      </view>
      
    </view>
    <!-- user info -->
    <view class="user-info-container">
      <view class="user-info-container-left">
        <image :src="items.avatarUrl" mode="aspectFill" class="user-info-container-left-avatar" />
        <image :src="items.levelIcon" mode="widthFix" class="user-info-container-left-level" />
      </view>
      <view class="user-info-container-right" @click="addVip">
        <image class="user-info-container-right-icon" src="../../static/img/task-5.png" mode="aspectFill"></image>
        <text class="user-info-container-right-text">{{$t('add_vip')}}</text>
      </view>
    </view>

    <success ref="sucRef" @ok="getInfo(that)" />
    <view class="maskLoading" v-if="loading">
      <view class="content">
        <image class="img" src="@/static/img/10001.gif" mode="widthFix" />
        <p class="txt">{{ $t("matching") }}</p>
      </view>
    </view>
  </view>
</template>

<script>
import banner1 from "@/static/img/task-bg2.png";
import banner2 from "@/static/img/task-bg1.png";
import banner3 from "@/static/img/taskall-bg1.jpg";
  import success from "@/components/success.vue";
  import {
    bubbles
  } from '@/static/js/taskBubble.js'
  import {
    flatten
  } from '../../plugins/util.js'
  const rotate = (x, y, sin, cos, reverse) => {
    return reverse ? {
      x: cos * x + sin * y,
      y: cos * y - sin * x
    } : {
      x: cos * x - sin * y,
      y: cos * y + sin * x
    }
  }

  export default {
    components: {
      success
    },
    data() {
      return {
        animationFrameId: null,
        lastTm: null,
        lastCollisions: [],
        bubbles: bubbles.slice(0, 20),
        // bubbles,
        bubbleBox: {
          top: 0,
          left: 0,
          width: 0,
          height: 0
        },
        items: {},
        infos: {},
        animation: [{
            type: 0,
            animationData: null,
            time: 0,
          },
          {
            type: 1,
            animationData: null,
            time: 100,
          },
          {
            type: 0,
            animationData: null,
            time: 200,
          },
        ],
        loading: false,
        vim: {},
        that: {},
        // list2: [banner3,banner3,banner3,banner3,banner3,banner3,banner3],
        list2: [banner1,banner2,banner1,banner2,banner1,banner2,banner1],
      };
    },
    methods: {
      open(e) {
        this.that = e;
        this.getInfo(e);
      },
      addVip() {
        uni.navigateTo({
          url: "/pages/vip",
        });
      },

      //用户列表数据,用户收益详情
      getInfo(e) {
        let index = e.level || 1;
        Promise.all([
          this.$api.user_info(),
          this.$api.user_income_detail(),
          this.$api.area_detail(index),
        ]).then((res) => {
          if (res[0].data.code == 0) {
            this.items = res[0].data.data;
          } else {
            this.$base.show(res[0].data.msg);
          }
          if (res[1].data.code == 0) {
            this.infos = res[1].data.data;
          } else {
            this.$base.show(res[1].data.msg);
          }
          if (res[2].data.code == 0) {
            this.vim = res[2].data.data;
          } else {
            this.$base.show(res[2].data.msg);
          }
        });
      },
      // 开始滚动
      startCallBack() {
        if (this.loading) return false;
        this.loading = true;
        setTimeout(() => {
          this.endCallBack();
        }, 6500);
      },
      // 重置
      endCallBack() {
        this.$api.order_match(this.vim.areaId).then(({
          data
        }) => {
          this.loading = false;
          if (data.code == 0) {
            this.getInfo(this.vim.areaId);
            this.$refs.sucRef.open(data.data.orderNo);
          }
        });
      },
      moenyFn(val) {
        if (!val) {
          return 0;
        } else if (val >= 100000) {
          return parseInt(val / 100000) + this.$t("myriad");
        } else {
          return "" + val;
        }
      },
      bubbleUpdate(tm) {
        if (this.lastTm && this.bubbles.length) {
          const diff = tm - this.lastTm
          const couples = []
          this.bubbles.forEach(b1 => {
            this.bubbles.forEach(b2 => {
              if (b1.id !== b2.id) {
                couples.push([b1, b2])
              }
            })
          })
          const collisions = couples.filter(couple => {
            const dist = Math.sqrt(Math.pow(couple[0].top - couple[1].top, 2) + Math.pow(couple[0].left - couple[1]
              .left, 2))
            return dist < couple[0].size / 2 + couple[1].size / 2
          })
          const newCollisions = collisions.filter(couple => {
            const key = couple[0].id + '__bubble__' + couple[1].id
            return this.lastCollisions.indexOf(key) < 0
          })
          newCollisions.forEach(couple => {
            const a = couple[0]
            const b = couple[1]
            a.mass = 1
            b.mass = 1
            if (a.collisionFree && b.collisionFree) {
              var dx = b.left - a.left
              var dy = b.top - a.top
              var collisionAngle = Math.atan2(dy, dx)
              var sin = Math.sin(collisionAngle)
              var cos = Math.cos(collisionAngle)
              var pos_a = {
                x: 0,
                y: 0
              }
              var pos_b = rotate(dx, dy, sin, cos, true)
              var vel_a = rotate(a.vx, a.vy, sin, cos, true)
              var vel_b = rotate(b.vx, b.vy, sin, cos, true)
              var vxTotal = vel_a.x - vel_b.x
              vel_a.x = ((a.mass - b.mass) * vel_a.x + 2 * b.mass * vel_b.x) / (a.mass + b.mass)
              vel_b.x = vxTotal + vel_a.x
              pos_a.x += vel_a.x
              pos_b.x += vel_b.x

              var pos_a_final = rotate(pos_a.x, pos_a.y, sin, cos, false)
              var vel_a_final = rotate(vel_a.x, vel_a.y, sin, cos, false)
              a.new_x = a.left + pos_a_final.x
              a.new_y = a.top + pos_a_final.y
              a.new_vx = vel_a_final.x
              a.new_vy = vel_a_final.y
            }
          })
          newCollisions.forEach(couple => {
            const a = couple[0]
            const b = couple[1]
            if (a.new_vy) {
              a.vx = a.new_vx
              a.vy = a.new_vy
              a.left = a.new_x
              a.top = a.new_y
            }
          })
          this.lastCollisions = collisions.map(couple => {
            return couple[0].id + '__bubble__' + couple[1].id
          })

          const collided = [...new Set(flatten(newCollisions))]
          const collidedKeys = collided.map(c => c.key)
          this.bubbles.forEach(bubble => {
            bubble.collisionFree = bubble.collisionFree || collidedKeys.indexOf(bubble.key) < 0
            const {
              top,
              left,
              size,
              vx,
              vy
            } = bubble
            if (top < 0) {
              bubble.vy = Math.abs(vy)
            } else if (top > this.bubbleBox.height - size) {
              bubble.vy = -Math.abs(vy)
            }
            if (left < 0) {
              bubble.vx = Math.abs(vx)
            } else if (left > this.bubbleBox.width - size) {
              bubble.vx = -Math.abs(vx)
            }
            bubble.top += bubble.vy * diff
            bubble.left += bubble.vx * diff
          })
        }
        this.lastTm = tm
        this.animationFrameId = requestAnimationFrame(this.bubbleUpdate)
      }
    },
    mounted() {
      uni.createSelectorQuery().select('.bubble-inner').boundingClientRect(data => {
        this.bubbleBox = {
          top: data.top,
          left: data.left,
          width: data.width,
          height: data.height,
        }
      }).exec()
      const bubbleNodes = uni.createSelectorQuery().selectAll('.bubble-inner .bubble').fields({
        rect: true,
        size: true
      }, data => {
        this.bubbles.forEach(b => {
          const top = Math.random() * this.bubbleBox.height
          const left = Math.random() * this.bubbleBox.width
          b.top = top > this.bubbleBox.height - b.size ? this.bubbleBox.height - b.size : top
          b.left = left > this.bubbleBox.width - b.size ? this.bubbleBox.width - b.size : left
        })
        this.$nextTick(() => {
          this.animationFrameId = requestAnimationFrame(this.bubbleUpdate)
        })
      }).exec()

    },
    beforeDestroy() {
      cancelAnimationFrame(this.animationFrameId)
    }
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

  .back {
    background: linear-gradient(134.61deg, #333333 0.81%, #1e1e1e 97.27%),
      conic-gradient(from 180deg at 50% 50%,
        rgba(245, 211, 172, 0) 0deg,
        rgba(245, 211, 172, 0.38) 45deg,
        rgba(245, 211, 172, 0) 84.38deg,
        rgba(245, 211, 172, 0) 133.12deg,
        rgba(245, 211, 172, 0.37) 187.5deg,
        rgba(245, 211, 172, 0) 230.62deg,
        rgba(245, 211, 172, 0) 360deg);
    border: 1rpx solid;
    border-image-source: conic-gradient(from 180deg at 50% 50%,
        rgba(245, 211, 172, 0) 0deg,
        rgba(245, 211, 172, 0.38) 45deg,
        rgba(245, 211, 172, 0) 84.38deg,
        rgba(245, 211, 172, 0) 133.12deg,
        rgba(245, 211, 172, 0.37) 187.5deg,
        rgba(245, 211, 172, 0) 230.62deg,
        rgba(245, 211, 172, 0) 360deg);
  }

  .main {
    padding-top: 100rpx;
    padding-left: 32rpx;
    padding-right: 32rpx;
    padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
  }

  .bubble-container {
    position: relative;
    margin: 32rpx 0;
    aspect-ratio: 1.6;
    border-radius: 20rpx;
    overflow: hidden;

    .bubble-inner {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      z-index: 2;
      overflow: hidden;

      .bubble {
        position: absolute;
      }
    }
  }

  // .today {
  //   position: relative;
  //   text-align: center;
  //   font-size: 32rpx;
  //   color: #373737;
  //   line-height: 40rpx;

  //   &::after {
  //     content: '';
  //     position: absolute;
  //     bottom: -10rpx;
  //     left: 0;
  //     right: 0;
  //     width: 24rpx;
  //     height: 4rpx;
  //     margin: auto;
  //     background-color: #7D7D7D;
  //   }
  // }
  .schedule{
    position: relative;
    text-align: center;
    font-size: 48rpx;
    color: #3A3A3A;
    line-height: 48rpx;
    font-weight: 500;
    margin-top: 62rpx;
  }
  .summary-container {
    margin: 56rpx 0 40rpx;
    padding: 40rpx 30rpx 30rpx;
    border-radius: 20rpx;
    background-color: #C1D0F1;

    &-balance {
      text-align: center;
      color: #333;

      &-num {
        font-size: 88rpx;
      }

      &-text {
        font-size: 24rpx;
      }
    }

    &-panels {
      display: flex;
      justify-content: space-between;
      column-gap: 24rpx;
    }

    &-panel-item {
      flex: 1;
      padding: 24rpx;
      background-color: #fff;
      border-radius: 20rpx;

      .icon-box {
        width: 100rpx;
        height: 100rpx;
        flex-shrink: 0;
        overflow: hidden;
        display: block;
        margin: 20rpx auto;
      }

      &-content {
        flex: 1;
        text-align: center;

        &-num {
          color: #3A3A3A;
          font-size: 40rpx;
        }

        &-text {
          margin-top: 8rpx;
          color: #373737;
          font-size: 18rpx;
        }
      }
    }

    &-start-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100rpx;
      margin-top: 32rpx;
      background-color: #333333;
      border-radius: 20rpx;
      cursor: pointer;

      .start-btn-text {
        margin: 0 16rpx;
        line-height: 30px;
        font-size: 52rpx;
        color: #fff;
        font-family: Arial;
      }

      .start-btn-icon {
        width: 48rpx;
        height: 48rpx;
      }
    }
  }

  .user-info-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 32rpx;
    padding: 30rpx;
    background-color: rgba(239, 244, 253, 0.74);
    border-radius: 20rpx;

    &-left {
      display: flex;
      align-items: center;

      &-avatar {
        width: 120rpx;
        height: 120rpx;
        margin-right: 12rpx;
      }

      &-level {
        width: 120rpx;
      }
    }

    &-right {
      display: flex;
      align-items: center;
      padding: 16rpx 10rpx;
      background-color: #7D7D7D;
      border: 1rpx solid #575651;
      border-radius: 10rpx;

      &-icon {
        width: 40rpx;
        height: 40rpx;
        margin-right: 10rpx;
      }

      &-text {
        color: #fff;
        font-size: 36rpx;
        font-family: Arial;
      }
    }
  }

  .list {
    position: relative;

    .bg {
      width: 100%;
    }
  }

  .box {
    @extend .back;
    border-radius: 32rpx;
    // position: absolute;
    // left: 50%;
    // bottom: 70rpx;
    // transform: translateX(-50%);
    // width: calc(100% - 64rpx);
    padding: 32rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .con {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-direction: column;
    }

    .img {
      width: 180rpx;
    }

    .title {
      padding: 16rpx 0;
      color: #e3e6f3;
      font-size: 32rpx;
    }

    .text {
      font-size: 28rpx;
      color: #ffffffa6;
      margin-top: 20rpx;
    }
  }

  .task {
    font-weight: bold;
    margin: 0 auto 10rpx auto;
    font-size: 36rpx;
    color: $white;
    position: relative;
    padding: 30rpx 0 20rpx 40rpx;

    &::after {
      content: "";
      position: absolute;
      left: 0;
      border-radius: 8rpx;
      top: 30rpx;
      background: linear-gradient(130.74deg, #6c38ed 17.26%, #9167f2 91.1%);
      height: 48rpx;
      width: 16rpx;
    }
  }

  .btn {
    padding: 30rpx 0 30rpx 30rpx;
    width: 276rpx;
    position: relative;

    &::after {
      content: "";
      display: block;
      height: 75%;
      width: 1rpx;
      background: #ffffff1f;
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
    }

    /deep/.u-button {
      border-radius: 100rpx;

      &:nth-child(1) {
        margin-bottom: 32rpx;
      }
    }

    /deep/.u-button:nth-child(1) .u-button__text {
      color: $white;
      font-size: 32rpx !important;
    }

    /deep/.u-button:nth-child(2) .u-button__text {
      color: $white;
      font-size: 32rpx !important;
    }
  }

  .passStyle {
    padding: 30rpx 0;

    .pass {
      @extend .back;
      display: flex;
      align-items: center;
      border-radius: 10rpx;
      padding: 25rpx 20rpx;

      view {
        font-size: 28rpx;
        padding-left: 16rpx;
        color: $white;
      }

      text {
        padding: 4rpx 8rpx;
        border-radius: 8rpx;
        color: #121212;
        display: flex;
        align-items: center;
        font-weight: 400;
        justify-content: center;
        margin-left: 10rpx;
        font-size: 20rpx;
        background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
      }
    }
  }

  .info {
    @extend .back;
    padding: 32rpx 24rpx;
    border-radius: 16rpx;

    view {
      font-size: 20rpx;
      line-height: 1.5;
      color: #ffffffd9;
      padding-top: 10rpx;
    }
  }

  .report {
    border-radius: 20rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    gap: 16rpx;
    padding: 30rpx 0;

    .item {
      @extend .back;
      border-radius: 16rpx;
      width: calc(100% / 3 - 16rpx);
      padding: 20rpx;

      &:nth-child(-n + 3) {
        padding-bottom: 30rpx;
      }
    }

    .txt {
      text-align: center;
      color: #ffffffa6;
      font-size: 20rpx;
    }

    .moeny {
      font-weight: 600;
      font-size: 32rpx;
      padding-bottom: 10rpx;
      color: #ffffffd9;
      text-align: center;
    }
  }
  .task-title{
    display: flex;
    .task-title-img{
      width: 340rpx;
      height: 400rpx;
      &:nth-child(2){
        margin-left: 26rpx;
      }
    }
  }
  .task-logo{
    display: flex;
    margin-top: 35rpx;
    .task-logo-img{
      width: 120rpx;
      height: 120rpx;
    }
  }
  .banner {
  height: 400rpx;
  width: 100vw;
  box-sizing: border-box;
}
/deep/.u-swiper,
/deep/.u-swiper__wrapper,
.my-swipe {
  // width: 600rpx !important;
  height: 400rpx !important;
  .u-swiper__wrapper__item__wrapper__image {
    height: 100% !important;
  }
}
</style>