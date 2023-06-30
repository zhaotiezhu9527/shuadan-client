<template>
  <view class="wapper">
    <u-navbar
      placeholder
      :border="false"
      autoBack
      title="头像设置"
      fixed
      leftIconColor="#666"
      leftIconSize="32"
      safe-area-inset-top
      bgColor="#fff"
      height="50px"
      titleStyle="color:#000;font-weight:500;font-size:32rpx;"
    >
    </u-navbar>
    <view class="main">
      <view class="head">
        <image class="head-img" :src="avatarUrl"/>
        <view class="btn" @click="postHead">保存</view>
      </view>
      <view class="content">
        <view class="item" v-for="(item,index) in list" :key="index" @click="changeAvatar(item)">
          <image class="item-img" :src="item.url"/>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      avatarUrl: "",//用户头像
      avatarId:'',//头像id
      list: [],//系统头像列表
    };
  },
  onShow() {
    this.getInfo()
    this.getAvatarList()
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
      this.avatarUrl = data.url
      this.avatarId = data.id
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
            this.$base.show(res.data.msg)
          }
        })
        .finally(() => {
        });
    },
  },
};
</script>

<style scoped lang="scss">
.head{
  width: 100%;
  height: 252rpx;
  background: linear-gradient(180deg, rgba(254,233,218,0.30) -4%, rgba(254,233,218,0.30) 52%,
  rgba(255,255,255,0.00) 130%);
  overflow: hidden;
  border-bottom: 20rpx solid #eee;
  .head-img{
    display: block;
    width: 120rpx;
    height: 120rpx;
    margin: 30rpx auto 20rpx auto;
  }
  .btn{
    font-size: 30rpx;
    color: #fff;
    background: #FFB550;
    border-radius: 90rpx;
    width: 160rpx;
    text-align: center;
    margin: auto;
  }
}
.content{
  width: 100%;
  background-color: #fff;
  overflow: hidden;
  .item{
    width: 20%;
    padding: 18rpx;
    float: left;
  }
  .item-img{
    display: block;
    width: 120rpx;
    height: 120rpx;
  }
}
</style>
