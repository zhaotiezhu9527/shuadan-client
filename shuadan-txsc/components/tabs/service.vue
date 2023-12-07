<template>
  <view>
    <view class="main">
      <view class="container">
        <view class="title">{{$t('customerService') }}</view>
        <view class="sub_title">{{$t('askService') }}</view>
      </view>
      <view class="customer_type">
        <image class="image" src="@/static/img/kefu-service.png" mode="widthFix" />
      </view>
      <view class="support-hours">
        {{$t('supportHours') }}
      </view>
      <view class="service-btn">
        <div onclick="_MEIQIA('showPanel')">
        {{$t('connect') }}{{$t('onlineService') }}
        </div>
      </view>
    </view>
  </view>
</template>
<script>

export default {
  data() {
    return {
      list: [],
      config:{},
    };
  },
  onShow() {
   
  },
  mounted (){
    this.getConfig()
  },
  methods: {
    open() {
      
    },
    goService() {
      uni.navigateTo({
        url: "/pages/onlineService",
      });
    },
    getConfig(){
      // uni.showLoading();
      this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
          this.config = data.data;
          // uni.hideLoading();
          this.readFn(this.config.kfid,this.config.mate)
        } 
      });
    },
    readFn(kfid,mate){
      (function(m, ei, q, i, a, j, s) {
        m[i] = m[i] || function() {
          (m[i].a = m[i].a || []).push(arguments)
        };
        j = ei.createElement(q),
                s = ei.getElementsByTagName(q)[0];
        j.async = true;
        j.charset = 'UTF-8';
        j.src = 'https://static.meiqia.com/dist/meiqia.js';
        s.parentNode.insertBefore(j, s);
      })(window, document, 'script', '_MEIQIA');
      _MEIQIA('entId', kfid);
      // 在这里开启手动模式（必须紧跟美洽的嵌入代码）
      _MEIQIA('init');
      _MEIQIA('withoutBtn');
      if(mate != null){
        _MEIQIA('metadata', {
            comment: mate.account,
            name: mate.name, // 美洽默认字段
            tel: mate.tel, // 美洽默认字段
        });
      }
    }
  },
};
</script>
<style lang="scss" scoped>
.main {
  background-image: url("@/static/img/userbg.png");
  background-repeat: no-repeat;
  background-size: 100% auto;
  padding: 0;
  width: 100%;
  height: 100vh;
  .container {
    height: 280rpx;
    padding: calc(var(--status-bar-height) + 40rpx) 30rpx 0;
    // background: #bac3d2;
    position: relative;
    z-index: 2;
    .title {
      font-size: 34rpx;
      font-weight: 600;
      color: #333;
    }
    .sub_title {
      font-size: 24rpx;
      padding-top: 40rpx;
      color: #000;
    }
  }
  .customer_type {
    .image {
      width: 524rpx;
      margin: 60rpx auto 0 auto;
      display: block;
    }
    .list {
      padding-top: 120rpx;
    }
  }
  .support-hours{
    width: 100%;
    text-align: center;
    color: #fff;
    font-size: 24rpx;
    margin-top: -60rpx;
  }
  .service-btn{
    width: 260rpx;
    height: 84rpx;
    text-align: center;
    color: #fff;
    background-color: #2E68F2;
    border-radius: 42rpx;
    line-height: 84rpx;
    font-size: 28rpx;
    margin: 40rpx auto 0 auto;
  }
}
</style>
