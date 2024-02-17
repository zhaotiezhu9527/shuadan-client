# mosowe-move

## 相比mosowe-text-scroll-row组件

1. 参数减少，使用slot插槽全自定义每一项结构

2. 可以多行展示，mosowe-text-scroll-row组件多行展示需要引入多次

3. 可以Y轴滚动

4. 滚动效果比mosowe-text-scroll-row组件流畅

## 属性

| props        | 类型      | 说明                                      | 默认    |
| ------------ | ------- | --------------------------------------- | ----- |
| list         | Array   | 以为数组列表                                  | []    |
| row          | number  | 滚动行数，即将list数据平均几行展示，每行数据不会重复            | 1     |
| speed        | number  | 滚动速度，建议10-40，毫秒，                        | 30    |
| delay        | number  | 组件延时，单行的错位延时效果可以使用slot插槽处理              | 0     |
| initPosition | string  | 初始位置，可选[''left','right','top','bottom'] | left  |
| y            | boolean | Y轴滚动，此时row失效，Y轴滚动时只展示一列，从下向上滚动          | false |

## slot

| slot    | 说明                                                                            |
| ------- | ----------------------------------------------------------------------------- |
| default | 作用域参数：{row，rowIndex,colIndex}，row：当前项数据，rowIndex：当前项所在行数索引，colIndex：当前项所在列数索引 |

## 示例

```html
<template>
  <view>
    <mosowe-move
      :list="list"
      :row="3"
      :speed="10"
      initPosition="right">
      <template #default="{ row, rowIndex, colIndex }">
        <view class="item">{{ rowIndex }}-{{ colIndex }}:{{ row }}</view>
      </template>
    </mosowe-move>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const list = ref(
  Array.from({ length: 30 }).map((item, index) => '哈'.repeat(1 + Math.floor(Math.random() * 10)) + index)
);
</script>

<style lang="scss">
.item {
  margin-right: 20rpx;
  font-size: 20rpx;
  line-height: 40rpx;
}
</style>


```