<template>
  <div style="overflow: hidden; height: 100vh">
<!--    <el-button type="text" @click="turnBack" style="position: absolute" circle>文字按钮</el-button>-->
    <img src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg-blog.csdnimg.cn%2F20190227135746478.gif&refer=http%3A%2F%2Fimg-blog.csdnimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275429&t=d10a7ae54b78d53b29d3aa059d0f3960"
         alt=""
         style="width: 100%; height: 100%"
         @click="turnBack">
  </div>
</template>

<script>
export default {
  name: "NotFound",
  methods:{
    turnBack(){
      this.$router.push('/home')
    }
  }
}
</script>

<style>
.bgImg{
  /*background: url("../assets/img_404.png");*/
  background-size: 100% 100vh;
}
</style>