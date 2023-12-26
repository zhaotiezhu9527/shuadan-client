<template>
  <view>
    <!-- <view :is="list[active].route" ref="main"></view> -->
    <home v-if="active === 0" ref="main0"/>
    <order v-else-if="active === 1" ref="main1"/>
    <task v-else-if="active === 2" ref="main2" />
    <service v-else-if="active === 3" ref="main3" />
    <user v-else-if="active === 4" ref="main4" />
    <view class="tabs">
      <template v-for="(item, index) in list">
        <view
          class="item"
          :key="index"
          v-if="index !== 2"
          @click="change(index)"
        >
          <image class="img" :src="item.image" mode="widthFix" />
          <view class="name" :class="{ active: active === index }">
            {{ item.name }}
          </view>
        </view>
        <view class="item other" v-else @click="change(index)">
          <image class="img2" :src="item.image" mode="widthFix" />
          <view class="name name2" :class="{ active: active === index }">
            {{ item.name }}
          </view>
        </view>
      </template>
    </view>
  </view>
</template>
<script>
import homeimg from "@/static/img/100034.png";
import jilv from "@/static/img/100035.png";
import kefu from "@/static/img/100037.png";
import userimg from "@/static/img/100038.png";
import rw from "@/static/img/100036.png";

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
        { image: homeimg, route: home, name: this.$t("frontPage") },
        { image: jilv, route: order, name: this.$t("record")},
        { image: rw, route: task, name: this.$t("task") },
        { image: kefu, route: service, name: this.$t("service")},
        { image: userimg, route: user, name: this.$t("personal")},
      ],
      userData: {},
      infos: {},
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
      this.$refs[`main${this.active}`].open(this.route);
    });
    // #endif
    // this.getInfo();
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
        this.$refs[`main${this.active}`].open({ tabs: index });
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
  background-color: #fff;
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
  .img {
    width: 60rpx;
  }
  .img2 {
    width: 110rpx;
    background-color: #fff;
    padding: 20rpx;
    border-radius: 50%;
    box-sizing: border-box;
  }
  .name {
    font-size: 28rpx;
    padding: 4rpx 0;
    &.active {
      color: #2e68f2;
    }
  }
  .name2 {
    position: relative;
    top: -16rpx;
  }
}
</style>
