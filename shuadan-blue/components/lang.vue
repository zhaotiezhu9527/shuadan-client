<template>
  <view>
    <view class="cn" :class="{ inherit }" @click="show = true">
      <view class="txt">简体中文</view>
      <image src="@/static/img/10001.png" mode="widthFix" class="image" />
    </view>
    <u-action-sheet
      :actions="list"
      round="50"
      title="设置语言"
      :show="show"
      @close="show = false"
    ></u-action-sheet>
  </view>
</template>

<script>
export default {
  props: ["inherit"],
  data() {
    return {
      show: false,
      list: [
        {
          name: "简体中文",
          fontSize: "20",
        },
        {
          name: "繁体中文",
          fontSize: "20",
        },
        {
          name: "英文",
          fontSize: "20",
        },
      ],
    };
  },
  mounted() {},
  methods: {},
  components: {},
};
</script>

<style scoped lang="scss">
.cn {
  display: flex;
  align-items: center;
  position: absolute;
  top: 50rpx;
  left: 30rpx;
  &.inherit {
    position: inherit !important;
  }
  .image {
    width: 36rpx;
  }

  .txt {
    color: #fff;
    font-size: 24rpx;
  }
}
</style>
