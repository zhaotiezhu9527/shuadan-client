<template>
  <view class="main">
    <view class="title">
      <view class="sub">全名任务</view>
      <view class="name">用户登录</view>
    </view>
    <view class="form">
      <view class="input">
        <u-input
          border="bottom"
          placeholder="请输入账户"
          placeholderClass="placeholder"
        >
          <view class="icon" slot="prefix">
            <image mode="widthFix" src="@/static/img/icon01.png" />
          </view>
        </u-input>
      </view>
      <view class="input">
        <u-input
          border="bottom"
          placeholder="请输入密码"
          placeholderClass="placeholder"
        >
          <view class="icon" slot="prefix">
            <image mode="widthFix" src="@/static/img/icon02.png" />
          </view>
          <view slot="suffix">
            <u-icon
              v-if="passicon1"
              name="eye"
              color="#666"
              size="46rpx"
            ></u-icon>
            <u-icon v-else name="eye-off" color="#666" size="46rpx"></u-icon>
          </view>
        </u-input>
      </view>
      <u-button class="button" text="登录" color="#2f3848"></u-button>
      <view class="other">
        <view>忘记密码</view>
        <view @click="roeute">免费注册</view>
      </view>
    </view>
    <service />
  </view>
</template>

<script>
import service from "@/components/service";
export default {
  data() {
    return {
      passicon1: false,
    };
  },
  async onLoad() {
    await this.$onLaunched;
  },
  onShow() {},
  methods: {
    roeute() {
      uni.navigateTo({
        url: "/pages/register",
      });
    },
  },
  components: {
    service,
  },
};
</script>

<style scoped lang="scss">
.main {
  position: fixed;
  height: 100%;
  width: 100%;
  background-repeat: no-repeat;
  background: url("@/static/img/login-bg1.png");
  background-size: 100% auto;
  padding-top: var(--status-bar-height);
  .title {
    padding: 154rpx 80rpx 140rpx;
    .sub {
      font-size: 34rpx;
    }
    .name {
      font-size: 50rpx;
    }
  }
  .form {
    padding: 0 80rpx;
    .input {
      padding-bottom: 60rpx;
      .icon {
        padding-right: 20rpx;
        border-right: 2rpx solid #ccc;
      }
      image {
        width: 42rpx;
      }
    }
    .placeholder {
      color: #666 !important;
      font-size: 30rpx;
    }
    .other {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
    }
  }
  /deep/.button .u-button__text {
    color: #f2d8be;
  }
}
</style>
