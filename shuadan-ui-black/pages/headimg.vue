<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="头像设置"
      fixed
      leftIconColor="#ffffff"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#1e1e1e"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="head">
        <image class="head-img" :src="avatarUrl" />
        <view class="btn" @click="postHead">立即设置</view>
      </view>
      <view class="content">
        <view
          class="item"
          v-for="(item, index) in list"
          :key="index"
          @click="changeAvatar(item)"
        >
          <image class="item-img" :src="item.url" />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      avatarUrl: "", //用户头像
      avatarId: "", //头像id
      list: [], //系统头像列表
    };
  },
  onShow() {
    this.getInfo();
    this.getAvatarList();
  },
  methods: {
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.avatarUrl = res.data.data.avatarUrl;
        }
      });
    },
    //获取头像列表
    getAvatarList() {
      this.$api.avatar_list().then((res) => {
        if (res.data.code == 0) {
          this.list = res.data.data;
        }
      });
    },
    //选取头像
    changeAvatar(data) {
      this.avatarUrl = data.url;
      this.avatarId = data.id;
    },
    //设置头像
    postHead() {
      const DATA_OBJ = {
        id: this.avatarId,
      };
      this.$api
        .avatar_set(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
          }
        })
        .finally(() => {});
    },
  },
};
</script>

<style scoped lang="scss">
.head {
  width: 100%;
  height: 336rpx;
  background: #1e1e1e;
  overflow: hidden;
  .head-img {
    display: block;
    width: 176rpx;
    height: 176rpx;
    margin: 30rpx auto 20rpx auto;
  }
  .btn {
    font-size: 30rpx;
    color: #fff;
    border-radius: 8rpx;
    width: 176rpx;
    height: 64rpx;
    line-height: 60rpx;
    text-align: center;
    margin: auto;
    border: 1px solid #666;
  }
}
.content {
  width: 100%;
  overflow: hidden;
  .item {
    width: 20%;
    padding: 18rpx;
    float: left;
  }
  .item-img {
    display: block;
    width: 120rpx;
    height: 120rpx;
  }
}
</style>
