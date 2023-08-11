<script>
export default {
  onLaunch: function () {
    this.$api.system_config().then(({ data }) => {
      if (data.code == 0) {
        this.$base.storage("config", data.data);
        this.$isResolve();
      } else {
        this.$isResolve();
      }
    });
  },
  methods: {},
  onShow: function () {},
};
</script>
<style lang="scss">
page {
  background-image: linear-gradient(
    180deg,
    rgba(49, 19, 81, 0) 0%,
    rgba(44, 17, 72, 0.94) 0%,
    #311351 100%
  );
  background-repeat: no-repeat;
  background-attachment: fixed;
  min-height: 100%;
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
  box-shadow: 2px 2px 0px 0px rgba(255, 255, 255, 0.18) inset;
  box-shadow: -2px -2px 0px 0px rgba(0, 0, 0, 0.35) inset;
  background: linear-gradient(130.74deg, $main-c 17.26%, #9167f2 91.1%);
  border: 0 !important;
  color: $white !important;
  border-radius: 50%;
}
.button {
  box-shadow: 2px 2px 0px 0px rgba(255, 255, 255, 0.18) inset;
  box-shadow: -2px -2px 0px 0px rgba(0, 0, 0, 0.35) inset;
  background: linear-gradient(130.74deg, $main-c 17.26%, #9167f2 91.1%);
  border: 0 !important;
  color: $white !important;
}
.button-plain {
  border: 1rpx solid $main-c !important;
  background: transparent !important;
  color: $main-c !important;
}
// #ffad49
</style>
