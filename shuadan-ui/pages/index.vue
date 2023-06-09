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
            :class="'img0' + index"
            v-if="active === index"
            :src="item.image1"
            mode="widthFix"
          />
          <image
            :class="'img' + index"
            v-else
            :src="item.image2"
            mode="widthFix"
          />
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
      list: [
        { image1: home1, image2: home2, route: home },
        { image1: jilv1, image2: jilv2, route: order },
        { image1: rw1, route: "task", route: task },
        { image1: kefu1, image2: kefu2, route: service },
        { image1: user1, image2: user2, route: user },
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
    this.$nextTick(() => {
      this.$refs.main.open(e);
    });
  },
  methods: {
    change(index) {
      this.active = index;
      this.$store.tabs = index;
      // #ifdef H5
      this.$router.push(`/pages/index?tabs=${index}`);
      // #endif
      this.$nextTick(() => {
        this.$refs.main.open({ tabs: index });
      });
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
  }
  .other {
    padding-top: 0;
    margin-top: -60rpx;
  }
  .img0 {
    width: 40rpx;
  }
  .img00 {
    width: 44rpx;
  }
  .img1 {
    width: 40rpx;
  }
  .img01 {
    width: 48rpx;
  }
  .img2 {
    width: 120rpx;
  }
  .img3 {
    width: 40rpx;
  }
  .img03 {
    width: 52rpx;
  }
  .img4,
  .img04 {
    width: 70rpx;
  }
}
</style>
