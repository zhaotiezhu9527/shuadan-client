<template>
  <view class="main">
    <image
      src="/static/img/logo.png"
      class="logo flex items-center"
      mode="widthFix"
    />
    <view class="form">
      <view class="input">
        <image
          src="/static/img/icon_phone.png"
          mode="widthFix"
          class="input_icon"
        />
        <u-input
          border="none"
          :placeholder="$t('account')"
          placeholderClass="placeholder"
          v-model="userName"
        >
        </u-input>
      </view>
      <view class="input">
        <image
          src="/static/img/icon_passward.png"
          mode="widthFix"
          class="input_icon"
        />
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
    <view
      class="forget-password"
      @click="nopass('/pages/onlineService')"  
    >
      {{ $t("forget_pwd") }}
    </view>
    <view
      class="login-button"
      @click="login"
      >
      {{ $t("login") }}
    </view>
    <view
      class="free_reg"
      @click="$refs.registerRef.open(inviteCode)"
      >
      {{ $t("free_reg") }}
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
      // 客服修改
      // if(window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))
      // {
      //   uni.navigateTo({
      //     url,
      //   });
      // }else{
        // #ifdef APP-PLUS
        plus.runtime.openURL(this.config.onlineService);
        // #endif
        // #ifdef H5
        window.open(this.config.onlineService);
        // #endif
      // }
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
