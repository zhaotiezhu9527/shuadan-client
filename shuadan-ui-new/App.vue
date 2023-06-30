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
/*每个页面公共css */
@import "@/uni_modules/uview-ui/index.scss";
.linear {
  /deep/.u-navbar__content {
    background: linear-gradient(127deg, #f8a057 -1%, #ffc860 59%) !important;
  }
}
.linear2 {
  /deep/.u-navbar__content {
    background: linear-gradient(
      180deg,
      rgba(254, 233, 218, 0.8) -4%,
      rgba(254, 233, 218, 0.8) 52%,
      rgba(255, 255, 255, 0) 130%
    ) !important;
  }
}

/deep/.u-navbar__content {
  z-index: 1024;
}
// #ffad49
</style>
