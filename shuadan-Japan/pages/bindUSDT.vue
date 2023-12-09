<template>
  <view>
    <u-navbar
      placeholder
      :border="false"
      autoBack
      :title="$t('USDTAddr')"
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
      <view class="title">{{ $t("personalInformation") }}</view>
      <view class="from-input">
        <label>{{ $t("realName") }}</label>
        <input
          type="text"
          v-model="userData.realName"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noRealName')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("phoneNo") }}</label>
        <input
          type="text"
          v-model="userData.phone"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('noPhone')"
        />
      </view>
      <!-- <view class="title">{{ $t("cardDetails") }}</view> -->
      <view class="from-input">
        <label>{{ $t("USDTaddress") }}</label>
        <input
          type="text"
          v-model="userData.addr"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('usdtAddress')"
        />
      </view>
      <view class="from-input">
        <label>{{ $t("idCard") }}</label>
        <input
          type="text"
          v-model="userData.idCard"
          :disabled="bindStatus"
          class="input-text"
          :placeholder="$t('enterIdCard')"
        />
      </view>
      <view class="from-img">
        <label> {{ $t("IDphoto") }} </label>
        <view class="id-img-box" @click="upImg">
          <view class="up-img">
            +
          </view>
          <img v-if="userData.idCardImg" :src="userData.idCardImg" class="id-img"/>
        </view>

      </view>
      <view class="title">ショッピング センターでは本物の情報が必要ですが、偽の情報を提供した場合、センターは給料を支払いません。</view>
      <view class="btn">
        <u-button v-if="bindStatus" class="custom-style" color="#9d9d9c" block>
          {{ $t("confirm") }}
        </u-button>
        
        <u-button
          v-else-if="!bindStatus"
          class="custom-style"
          color="#2f3848"
          block
          @click="submit"
          :loading="loading"
        >
          {{ $t("confirm") }}
        </u-button>
      </view>
      <view class="title" v-if="bindStatus">情報をカスタマーケアに交換していただいて欲しいですが</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false, //
      userData: {
        realName: "", //真实姓名
        phone: "", //手机
        idCard: "", //银行卡号
        addr: "", //USDt地址
        dataPath: "", //头像图片
        idCardImg: "", //展示的图片
      },
      bindStatus: false, //绑定状态
      imgUrl: '', //身份证图片
    };
  },
  onShow() {
    this.getInfo();
  },
  methods: {
    submit() {
      if (!this.userData.realName) {
        return this.$base.show(this.$t("noRealName"));
      } else if (!this.userData.phone) {
        return this.$base.show(this.$t("noPhone"));
      } else if (!this.userData.idCard) {
        return this.$base.show(this.$t("enterIdCard"));
      } else if (!this.userData.addr) {
        return this.$base.show(this.$t("usdtAddress"));
      } else if (!this.userData.dataPath) {
        return this.$base.show(this.$t("enterIDphoto"));
      }
      this.loading = true;
      const DATA_OBJ = {
        realName: this.userData.realName, //真实姓名
        phone: this.userData.phone, //手机
        idCard: this.userData.idCard, //银行卡号
        idCardImg: this.userData.dataPath, //银行名称
        addr: this.userData.addr, //支行地址
      };
      this.$api
        .bind_usdt(DATA_OBJ)
        .then((res) => {
          if (res.data.code == 0) {
            this.$base.show(res.data.msg);
            this.loading = false;
            this.getInfo();
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //用户列表数据
    getInfo() {
      uni.showLoading({
        title: this.$t("loading"),
      });
      this.$api.user_info().then((res) => {
        if (res.data.code == 0) {
          this.userData.realName = res.data.data.realName;
          this.userData.phone = res.data.data.phone;
          this.userData.idCard = res.data.data.idCard;
          this.userData.addr = res.data.data.walletAddr;
          this.userData.idCardImg = res.data.data.idCardImg;
          if (this.userData.addr === null || !this.userData.addr) {
            this.bindStatus = false;
          } else {
            this.bindStatus = true;
          }
        }
      });
    },
    //上传图片
    upImg(){
      let self = this
      let filePath
      if(self.bindStatus){
        return false
      }
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'],
        sourceType: ['album'],
        success: function(res) {
          filePath = res.tempFilePaths[0]
          console.log(res.tempFiles[0])
            self.$base.upload({
              filePath: filePath,
            }).then((res) => {
              console.log(JSON.parse(res.data))
              if (res.statusCode == 200) {
                self.userData.idCardImg = JSON.parse(res.data).fullPath
                self.userData.dataPath = JSON.parse(res.data).path
              }
            });
          }
        });
    }
  },
};
</script>

<style scoped lang="scss">
.main {
  .title {
    padding: 0 12rpx;
    background-color: #eee;
    font-size: 27rpx;
    line-height: 75rpx;
    color: #333;
  }
  .from-input {
    margin: 0 40rpx;
    display: flex;
    height: 90rpx;
    line-height: 90rpx;
    font-size: 28rpx;
    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .input-text {
      flex: 1;
      height: 90rpx;
      line-height: 90rpx;
      font-size: 28rpx;
    }
  }
  .btn {
    width: 90%;
    margin: 40rpx auto;
  }
}
.from-img{
  margin: 0 40rpx 20rpx 40rpx;
    display: flex;
    font-size: 28rpx;

    label {
      width: 200rpx;
      margin-right: 20rpx;
      color: #999;
    }
    .id-img-box{
      position: relative;
      width: 300rpx;
      height: 300rpx;
    }
    .up-img{
      position: absolute;
      width: 300rpx;
      height: 300rpx;
      border: 4rpx solid #ddd;
      border-radius: 10rpx;
      color: #ddd;
      text-align: center;
      line-height: 300rpx;
      font-size: 100rpx
    }
    .id-img{
      width: 300rpx;
      height: 300rpx;
      position: absolute;
      border-radius: 10rpx;
    }
}

</style>
