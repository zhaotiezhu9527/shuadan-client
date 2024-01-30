<template>
  <view>
    <home v-show="active === 0" ref="main0" :userData="userData" />
    <order v-show="active === 1" ref="main1" />
    <task v-if="active === 2" ref="main2" />
    <service v-show="active === 3" ref="main3" />
    <user v-show="active === 4" ref="main4" :userData="userData" />
    <view class="tabs maxwidth">
      <template v-for="(item, index) in list">
        <view class="item" :key="index" @click="change(index)">
          <view :class="active === index ? 'active item-list' : 'item-list'">
            <image class="img" v-if="active === index" :src="item.image1" mode="heightFix" />
            <image class="img" v-else :src="item.image2" mode="heightFix" />
          </view>
          <view class="name" :class="{ active: active === index }">
            {{ item.name }}
          </view>
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
  import rw1 from "@/static/img/task1.png";
  import rw2 from "@/static/img/task1.png";
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
        list: [{
            image1: home1,
            image2: home2,
            name: this.$t("home")
          },
          {
            image1: jilv1,
            image2: jilv2,
            name: this.$t("log")
          },
          {
            image1: rw1,
            image2: rw2,
            name: this.$t("task_name")
          },
          {
            image1: kefu1,
            image2: kefu2,
            name: this.$t("service_name"),
          },
          {
            image1: user1,
            image2: user2,
            name: this.$t("my"),
          },
        ],
        userData: {},
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
      // #ifdef APP-PLUS
      this.$nextTick(() => {
        this.$refs[`main${this.active}`].open(this.route);
      });
      // #endif
    },
    onShow() {
      this.getInfo();
      // #ifdef H5
      this.$nextTick(() => {
        this.$refs[`main${this.active}`].open(this.route);
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
          this.$refs[`main${this.active}`].open({
            tabs: index
          });
        });
        // #endif
      },
      //用户列表数据
      getInfo() {
        this.$api.user_info().then((res) => {
          if (res.data.code == 0) {
            this.userData = res.data.data;
          }
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
    color: #575651;
    background: #fff;
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

        &.active {}
      }
    }

    .other {
      padding-top: 0;
      margin-top: -160rpx;
      width: 120rpx !important;
      height: 120rpx !important;
      display: flex;
    }

    .item-list {
      &.active {
        width: 100rpx;
        height: 100rpx;
        margin-top: -60rpx;
        background-color: #fff;
        border-radius: 100rpx;
        border: 1px solid #333;

        .img {
          width: 80rpx;
          height: 80rpx;
          display: block;
          margin: 10rpx;
        }
      }
    }

    .img {
      height: 40rpx;
      height: 40rpx;
    }

    .img2 {
      width: 40rpx;
      height: 40rpx;
    }
  }
</style>