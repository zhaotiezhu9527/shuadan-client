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
/deep/.u-navbar__content {
  z-index: 1024;
}
</style>
