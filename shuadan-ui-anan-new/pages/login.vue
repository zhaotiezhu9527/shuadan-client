<template>
  <view class="main">
    <image
      src="/static/img/logo.png"
      class="logo flex items-center"
      mode="widthFix"
    />
    <view class="form">
      <view class="input">
        <u-input
          border="none"
          :placeholder="$t('account')"
          placeholderClass="placeholder"
          v-model="userName"
        >
        </u-input>
      </view>
      <view class="input">
        <u-input
          border="none"
          :placeholder="$t('password')"
          placeholderClass="placeholder"
          password
          v-model="loginPwd"
        >
        </u-input>
      </view>
    </view>
    <u-button
      class="button-50"
      @click="login"
      :loading="loading"
      shape="circle"
    >
      <u-icon class="rightward" name="arrow-rightward"></u-icon>
    </u-button>
    <view class="flex items-center ul justify-center">
      <!-- <view class="li" @click="nopass('/pages/onlineService')">
        {{ $t("forget_pwd") }}
      </view> -->
      <view class="li" @click="$refs.registerRef.open(inviteCode)">
        {{ $t("free_reg") }}
      </view>
      <view class="li" @click="nopass('/pages/onlineService')">
        {{ $t("service") }}
      </view>
    </view>
    <register ref="registerRef" />
  </view>
</template>

<script>
import register from "@/components/register.vue";
export default {
  components: {
    register,
  },
  data() {
    return {
      loginPwd: "",
      userName: "",
      loading: false,
      config: {},
      inviteCode: "",
    };
  },
  onLoad(e) {
    if (e.code) {
      this.$nextTick(() => {
        this.inviteCode = e.code;
        this.$refs.registerRef.open(e.code);
      });
    }
    this.getConfig()
  },
  methods: {
    login() {
      if (!this.userName) {
        return this.$base.show(this.$t("input_act"));
      } else if (!this.loginPwd) {
        return this.$base.show(this.$t("input_pwd"));
      }
      this.loading = true;
      this.$api
        .user_login({
          userName: this.userName.replace(/\s*/g,""),
          loginPwd: this.loginPwd.replace(/\s*/g,""),
        })
        .then((res) => {
          if (res.data.code == 0) {
            uni.setStorage({
              key: "token",
              data: res.data.token,
              success: function () {
                uni.reLaunch({ url: "/pages/index?tabs=4" });
              },
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    nopass(url) {
      if(window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))
      {
        uni.navigateTo({
          url,
        });
      }else{
        // #ifdef APP-PLUS
        plus.runtime.openURL(this.config.onlineService);
        // #endif
        // #ifdef H5
        window.open(this.config.onlineService);
        // #endif
      }
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
@import "@/static/login.scss";
</style>
