<!--
 
 @Author: mosowe
 @Date:2023-10-27 11:34:09
-->
<template>
  <view class="mosowe-move-component">
    <view class="mosowe-move-item" v-for="(list, index) in showList" :key="index">
      <view class="mosowe-move-item_content" :style="{
          transform: (y ? 'translateY' : 'translateX') + '(' + xArr[index] + 'px)',
          display: y ? 'block' : 'inline-block',
          'white-space': y ? 'normal' : 'nowrap'
        }">
        <view class="mosowe-move-item_list" v-for="column in 2" :key="column" :style="{
            display: y ? 'block' : 'inline-block'
          }">
          <view class="mosowe-move-item_item" :style="{
              display: y ? 'block' : 'inline-block'
            }" v-for="(item, i) in list" :key="i">
            <slot :row="item" :rowIndex="index" :colIndex="i">
              {{ item }}
            </slot>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
  export default {
    props: {
      list: {
        // 总数据，一维
        type: Array,
        default: () => []
      },
      row: {
        // 行数
        type: Number,
        default: 1
      },
      speed: {
        // 速度
        type: Number,
        default: 30
      },
      delay: {
        // 延时
        type: Number,
        default: 0
      },
      y: {
        // y轴
        type: Boolean,
        default: false
      },
      initPosition: {
        // 开始位置，left/right
        type: String,
        default: 'left',
        validator: (value) => {
          const arr = ['left', 'right', 'top', 'bottom'];
          const check = arr.includes(value);
          if (!check) {
            console.error(`initPosition props  optional parameters : ${arr}. error value: ` + value);
          }
          return check;
        }
      }
    },
    data() {
      return {
        componentWidth: 0,
        contentWidthArr: [],
        xArr: []
      };
    },
    computed: {
      showList() {
        // 多维数组
        if (this.y) {
          this.xArr = [2000];
          return [this.list, this.list];
        } else {
          this.xArr = Array.from({
            length: this.row
          }).map((_) => 2000);
          const l = Array.from({
            length: this.row
          }).map((_, index) => {
            const arr = [];
            this.list.forEach((item, i) => {
              if (i % this.row === index) {
                arr.push(item);
              }
            });
            return arr;
          });
          return l;
        }
      }
    },
    watch: {
      showList: {
        deep: true,
        immediate: true,
        handler() {
          this.$nextTick(() => {
            let tt = setTimeout(() => {
              clearTimeout(tt);
              tt = null;
              let query = uni.createSelectorQuery().in(this);
              query.select('.mosowe-move-component').boundingClientRect((data) => {
                this.componentWidth = Math.floor(this.y ? data.height : data.width);
              });
              query
                .selectAll('.mosowe-move-item_content')
                .boundingClientRect((data) => {
                  this.contentWidthArr = data.map((item) => Math.floor(this.y ? item.height : item.width));
                  this.xArr = this.contentWidthArr.map((_, index) => {
                    return ['left', 'top'].includes(this.initPosition) ? 0 : this.componentWidth;
                  });
                })
                .exec();
              let t = setTimeout(
                () => {
                  clearTimeout(t);
                  t = null;
                  this.scrollContent();
                },
                this.delay < 0 ? 0 : this.delay
              );
            }, 1);
          });
        }
      }
    },
    methods: {
      scrollContent() {
        this.xArr.forEach((num, index) => {
          let n = num;
          if (n <= -(this.contentWidthArr[index] / 2)) {
            n = 0;
          } else {
            n = num - 1;
          }

          this.$set(this.xArr, index, n);
        });
        let t = setTimeout(() => {
          clearTimeout(t);
          t = null;
          this.scrollContent();
        }, this.speed);
      }
    }
  };
</script>

<style scoped>
  .mosowe-move-component {
    overflow: hidden;
    width: 100%;
    height: 100%;
  }

  .mosowe-move-item {
    width: 100%;
    overflow: hidden;
    line-height: 0;
  }

  .mosowe-move-item_content,
  .mosowe-move-item_list,
  .mosowe-move-item_item {
    white-space: nowrap;
    display: inline-block;
  }

  .mosowe-move-item_content {
    min-width: 200%;
  }

  .mosowe-move-item_list {
    min-width: 50%;
  }
</style>