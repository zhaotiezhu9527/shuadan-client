<script>
export default {
  onLaunch: function () {},
  methods: {},
  onShow: function () {},
};
</script>
<style lang="scss">
/*每个页面公共css */
@import "@/uni_modules/uview-ui/index.scss";
/deep/.u-navbar__content {
  z-index: 1024;
}
@keyframes roll {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(calc(-100% + 345rpx));
  }
}
</style>
