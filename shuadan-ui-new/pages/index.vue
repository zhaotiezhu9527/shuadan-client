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
            mode="widthFix"
          />
          <image class="img" v-else :src="item.image2" mode="widthFix" />
          <view class="name" :class="{ active: active === index }">{{
            item.name
          }}</view>
        </view>
        <view class="item other" v-else @click="change(index)">
          <image class="img2" :src="item.image1" mode="widthFix" />
        </view>
      </template>
    </view>
  </view>
</template>
<script>
import home1 from "@/static/img/home1.png";
import home2 from "@/static/img/home2.png";
import jilv1 from "@/static/img/jilv1.png";
import jilv2 from "@/static/img/jilv2.png";
import kefu1 from "@/static/img/kefu1.png";
import kefu2 from "@/static/img/kefu2.png";
import user1 from "@/static/img/user1.png";
import user2 from "@/static/img/user2.png";
import rw1 from "@/static/img/rw1.png";
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
        { image1: home1, image2: home2, route: home, name: "首页" },
        { image1: jilv1, image2: jilv2, route: order, name: "记录" },
        { image1: rw1, route: "task", route: task },
        { image1: kefu1, image2: kefu2, route: service, name: "客服" },
        { image1: user1, image2: user2, route: user, name: "我的" },
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
  background-size: 100% auto;
  -moz-background-size: 100% auto;
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
    padding-top: 30rpx;
    .name {
      padding-top: 6rpx;
      font-size: 24rpx;
      color: #333;
      &.active {
        color: #ffad49;
      }
    }
  }
  .other {
    padding-top: 0;
    margin-top: -80rpx;
    width: 120rpx;
    height: 120rpx;
    display: flex;
    background: linear-gradient(180deg, #ffad49 0%, #ffc861 100%);
    border-radius: 50%;
  }
  .img {
    width: 48rpx;
  }
  .img2 {
    width: 60rpx;
    height: 60rpx;
  }
}
</style>
