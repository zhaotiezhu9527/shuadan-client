<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('avatar')"
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
      <view class="head">
        <image class="head-img" :src="avatarUrl" />
        <view class="btn" @click="postHead">{{ $t("add_set") }}</view>
        <view class="bottom-line"></view>
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
.wapper{
  background-image: url("@/static/img/bg.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  padding: 0;
  width: 100%;
  height: 100%;
  .main{
    width: 90%;
    background-color: #fff;
    margin: 40rpx auto;
    padding-bottom: 40rpx;
  }
}
.head {
  width: 100%;
  height: 300rpx;
  overflow: hidden;
  .head-img {
    display: block;
    width: 115rpx;
    height: 115rpx;
    margin: 30rpx auto 20rpx auto;
  }
  .btn {
    font-size: 30rpx;
    color: #fff;
    border-radius: 10rpx;
    width: 152rpx;
    height: 70rpx;
    line-height: 70rpx;
    text-align: center;
    margin: auto;
    background-color: #7d7d7d;
  }
  .bottom-line{
    background-color: #7d7d7d;
    width: 95%;
    height: 2rpx;
    margin: 23px auto;
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
    width: 88rpx;
    height: 88rpx;
  }
}
</style>
