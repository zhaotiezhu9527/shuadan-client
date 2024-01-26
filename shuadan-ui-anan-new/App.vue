<script>
export default {
  onLaunch: function () {},
  methods: {},
  onShow: function () {},
};
</script>
<style lang="scss">
/*  #ifdef  H5  */
html,
body,
.u-modal,
.width818 {
  max-width: 818rpx; //最大宽度自己可以调整
  margin: auto !important;
}
.u-navbar--fixed {
  max-width: 818rpx;
  transform: translateX(-50%);
  left: 50% !important;
  width: 100%;
}
@media screen and(min-width: 1100px) {
  .maxwidth {
    max-width: 818rpx;
    margin: auto !important;
    transform: translateX(-50%);
    left: 50% !important;
  }
}
/*  #endif  */
.uni-app--maxwidth {
  background-image: url("@/static/img/bg.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  padding: 0;
  width: 100%;
  height: 100%;
}
page {
  background-repeat: no-repeat;
  background-attachment: fixed;
}
/*每个页面公共css */
@import "./static/utils.scss";
@import "@/uni_modules/uview-ui/index.scss";

/deep/.u-navbar__content {
  z-index: 1024;
}
.button-50 {
  height: 144rpx !important;
  width: 144rpx !important;
  box-shadow: 4rpx 4rpx 0 0 rgba(255, 255, 255, 0.18) inset;
  box-shadow: -4rpx -4rpx 0 0 rgba(0, 0, 0, 0.35) inset;
  background: linear-gradient(130.74deg, $main-c 17.26%, #9167f2 91.1%);
  border: 0 !important;
  color: $white !important;
  border-radius: 50%;
}
.button {
  box-shadow: 4rpx 4rpx 0 0 rgba(255, 255, 255, 0.18) inset;
  box-shadow: -4rpx -4rpx 0 0 rgba(0, 0, 0, 0.35) inset;
  background: linear-gradient(130.74deg, $main-c 17.26%, #9167f2 91.1%);
  border: 0 !important;
  color: $white !important;
}
.button-golden {
  box-shadow: 4rpx 4rpx 0 0 rgba(255, 255, 255, 0.18) inset;
  box-shadow: -4rpx -4rpx 0 0 rgba(0, 0, 0, 0.35) inset;
  border: 0 !important;
  background: linear-gradient(134.61deg, #d8b88e 0.81%, #c49a68 97.27%);
  color: $white !important;
}
.button-plain {
  border: 1rpx solid $main-c !important;
  background: transparent !important;
  color: $main-c !important;
}
// #ffad49
</style>
