<template>
  <view>
    <component :is="list[active].route" ref="main" />
    <view class="tabs">
      <template v-for="(item, index) in list">
        <view
          class="item"
          :key="index"
          v-if="index !== 2"
          @click="change(index)"
        >
          <image
            class="img"
            v-if="active === index"
            :src="item.image1"
            mode="heightFix"
          />
          <image class="img" v-else :src="item.image2" mode="heightFix" />
          <view class="name" :class="{ active: active === index }">
            {{ item.name }}
          </view>
        </view>
        <view class="item other button-50" v-else @click="change(index)">
          <image class="img2" :src="item.image1" mode="widthFix" />
          <view class="txt">{{ $t("task_name") }}</view>
        </view>
      </template>
    </view>
  </view>
</template>
<script>
import home1 from "@/static/img/home1.png";
import home2 from "@/static/img/home1.png";
import jilv1 from "@/static/img/jilv1.png";
import jilv2 from "@/static/img/jilv1.png";
import kefu1 from "@/static/img/kefu1.png";
import kefu2 from "@/static/img/kefu1.png";
import user1 from "@/static/img/user1.png";
import user2 from "@/static/img/user1.png";
import rw1 from "@/static/img/bg-013.png";
import home from "@/components/tabs/home.vue";
import order from "@/components/tabs/order.vue";
import service from "@/components/tabs/service.vue";
import task from "@/components/tabs/task.vue";
import user from "@/components/tabs/user.vue";
export default {
  data() {
    return {
      active: 0,
      route: {},
      list: [
        { image1: home1, image2: home2, route: home, name: this.$t("home") },
        { image1: jilv1, image2: jilv2, route: order, name: this.$t("log") },
        { image1: rw1, route: "task", route: task },
        {
          image1: kefu1,
          image2: kefu2,
          route: service,
          name: this.$t("service_name"),
        },
        {
          image1: user1,
          image2: user2,
          route: user,
          name: this.$t("my"),
        },
      ],
    };
  },
  onLoad(e) {
    if (e.tabs || Number(e.tabs) === 0) {
      this.active = Number(e.tabs);
      this.$store.tabs = Number(e.tabs);
    } else {
      this.active = this.$store.tabs;
    }
    this.route = e;
  },
  onShow() {
    // #ifdef H5
    this.$nextTick(() => {
      this.$refs.main.open(this.route);
    });
    // #endif
  },
  methods: {
    change(index) {
      if (this.active === index) return false;
      this.active = index;
      this.$store.tabs = index;
      // #ifdef H5
      this.$router.push(`/pages/index?tabs=${index}`);
      // #endif

      // #ifdef APP-PLUS
      this.$nextTick(() => {
        this.$refs.main.open({ tabs: index });
      });
      // #endif
    },
  },
  components: {
    home,
    order,
    service,
    user,
    task,
  },
};
</script>
<style lang="scss" scoped>
.tabs {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  color: #757575;
  background: url("@/static/img/footer.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-position: bottom;
  z-index: 999;
  height: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  .item {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: calc(100% / 5);
    padding: 15rpx 30rpx;
    .name {
      padding-top: 6rpx;
      font-size: 20rpx;
      color: $white;
      &.active {
        color: $white;
      }
    }
  }
  .other {
    padding-top: 0;
    margin-top: -160rpx;
    width: 120rpx !important;
    height: 120rpx !important;
    display: flex;
  }
  .img {
    height: 40rpx;
    height: 40rpx;
  }
  .img2 {
    width: 60rpx;
    height: 60rpx;
  }
}
</style>
