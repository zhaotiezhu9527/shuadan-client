<template>
  <view class="page">
    <u-navbar
      placeholder
      :title="$t('service')"
      :border="false"
      autoBack
      fixed
      safe-area-inset-top
      bgColor="#000000"
      leftIconColor="#ffffff"
      leftIconSize="32"
      height="100rpx"
      titleStyle="color:#fff;font-weight:600;font-size:32rpx;"
    >
    </u-navbar>
    <view class="wrap">
      <view v-if="config.onlineService">
        <iframe :src="config.onlineService + '&metadata=' + JSON.stringify(userInfo)" class="online"> </iframe>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      config: {}, //配置
      userData: {}, //用户信息
    };
  },
  onLoad() {
    this.getConfig()
    if(uni.getStorageSync("token")){
      this.getInfo()
    }
  },
  methods: {
    getConfig(){
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
        } 
      });
    },
    //用户列表数据
    getInfo() {
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData = res.data.data;
          this.userInfo = {
            name: this.userData.realName,
            tel: this.userData.userName,
          }
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.online {
  width: 100%;
  height: calc(100vh - 100rpx + var(--status-bar-height));
  border: none;
}
</style>
