<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>

      </el-breadcrumb>

    </div>
    <div style="width: 300px; color: navy">北京时间 {{dateFormat(date)}}
      <i class="el-icon-sunrise-1" v-if="date.getHours() > 3 && date.getHours() < 9"></i> <!-- 4 - 8     -->
      <i class="el-icon-sunny" v-if="date.getHours() > 8 && date.getHours() < 17"></i> <!-- 9 - 16     -->
      <i class="el-icon-sunset" v-if="date.getHours() > 16 && date.getHours() < 19"></i> <!-- 17 - 18     -->
      <i class="el-icon-moon" v-if="date.getHours() > 18 && date.getHours() < 22"></i> <!-- 19 - 21     -->
      <i class="el-icon-moon-night" v-if="date.getHours() > 21 || date.getHours() < 4"></i> <!-- 22 - 24 或 0 - 5     -->
    </div>
    <el-dropdown style="width: 150px; cursor: pointer" text-align: right>
      <div>
<!--        <img src="../assets/logo.png" alt="" style="width: 20px;border-radius: 50%; position: relative; top: 10px; right: 5px">-->
        <img :src="user.avatar" alt=""
             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0; ">
          <span style="text-decoration: none" @click="personIndex">个人信息</span>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出 <i class="el-icon-switch-button"></i></span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据
    },
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data(){
    return {
      date:new Date(),
    }
  },
  mounted() {
    //显示当前日期时间
    let _this = this// 声明一个变量指向Vue实例this，保证作用域一致
    this.timer = setInterval(() => {
      _this.date = new Date(); // 修改数据date
    }, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  },
  methods: {
    dateFormat(time) {
      var date=new Date(time);
      var year=date.getFullYear();
      /* 在日期格式中，月份是从0开始的，因此要加0
      * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
      * */
      var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
      var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
      var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
      var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
      var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
      // 拼接
      return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
    },
    collapse() {
      // this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
      this.$emit("asideCollapse")
    },
    logout() {
      this.$router.push("/login")
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      this.$message.success("退出成功")
    },
    personIndex(){
      this.$router.push("/person")
      // this.$message.success("加载成功")
    }
  }
}
</script>

<style scoped>

</style>
