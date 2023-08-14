<template>
  <view class="main">
    <u-navbar
      :border="false"
      :title="$t('rob_order')"
      fixed
      safe-area-inset-top
      bgColor="transparent"
      leftIconSize="0"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="list">
      <image class="bg" mode="widthFix" src="/static/img/bg-016.png" />
      <view class="box">
        <view class="con">
          <image class="img" :src="vim.levelImg" mode="widthFix" />
          <view class="title">{{ vim.areaName }}</view>
          <view class="text">
            {{ vim.remark }} {{ $t("commission") }}{{ vim.commission }}%
          </view>
        </view>
        <view class="btn">
          <u-button
            type="primary"
            :text="$t('add_vip')"
            @click="addVip"
            class="button-golden"
          ></u-button>
          <u-button
            type="primary"
            @click="startCallBack"
            :text="$t('start_call_back')"
            class="button"
          ></u-button>
        </view>
      </view>
    </view>
    <view class="px-16">
      <view class="task">{{ $t("today") }}</view>
      <view class="report">
        <view class="item">
          <view class="moeny gray">
            {{ moenyFn(infos.balance) }}
          </view>
          <view class="txt">{{ $t("my_balance") }}</view>
        </view>
        <view class="item">
          <view class="moeny">
            {{ moenyFn(infos.yesterdayIncome) }}
          </view>
          <view class="txt">{{ $t("yesterday") }}</view>
        </view>
        <view class="item">
          <view class="moeny"> {{ moenyFn(infos.todayIncome) }}</view>
          <view class="txt">{{ $t("todayIncome") }}</view>
        </view>
        <view class="item">
          <view class="moeny">
            <template v-if="infos.freezeBalance"></template>
            {{ infos.freezeBalance }}
          </view>
          <view class="txt">{{ $t("freezeBalance") }}</view>
        </view>
        <view class="item">
          <view class="moeny">
            {{ infos.todayOrderCount }} {{ $t("unit") }}
          </view>
          <view class="txt">{{ $t("todayOrderCount") }}</view>
        </view>
        <view class="item">
          <view class="moeny"> {{ moenyFn(infos.yesterdayTeamIncome) }}</view>
          <view class="txt">{{ $t("yesterdayTeamIncome") }}</view>
        </view>
      </view>
      <view class="passStyle">
        <view class="pass">
          <text>{{ $t("unlocking") }}</text>
          <view>{{ $t("next") }}</view>
        </view>
      </view>
      <view class="task">{{ $t("order_tips") }}</view>
      <view class="info">
        <view>{{ $t("user") }}</view>
        <view> {{ $t("tips") }} </view>
        <view>{{ $t("real") }}</view>
        <view>
          {{ $t("reputation") }}
        </view>
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
import success from "@/components/success.vue";
export default {
  components: { success },
  data() {
    return {
      items: {},
      infos: {},
      animation: [
        {
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
      this.$api.order_match(this.vim.areaId).then(({ data }) => {
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
.back {
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
  border: 1rpx solid;
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

.main {
  padding-bottom: calc(220rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(220rpx + env(safe-area-inset-bottom));
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
  position: absolute;
  left: 50%;
  bottom: 70rpx;
  transform: translateX(-50%);
  width: calc(100% - 64rpx);
  padding: 32rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .con {
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
    font-size: 20rpx;
    color: #ffffffa6;
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
</style>
