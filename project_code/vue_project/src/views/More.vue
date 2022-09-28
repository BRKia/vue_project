<template>
  <div style="color: #666;font-size: 14px;">
    <div  style="padding-bottom: 20px; color: lightseagreen">
      <b>您好！{{ user.nickname }}</b>
    </div>
    <el-card>
      <b>致谢</b>
      <el-divider />
      本项目于22.6.7正式启动，至22.9.28结项<br><br>
      在网络部署即将调试完成之际，我的情绪无法平静。<br><br>
      <b>首先</b>，十分感谢岳文飞先生长期以来的指导，从使用第一个接口链接前后端，到一遍又一遍地引导本人解决项目中具体的问题，循循善诱<br><br>
      在此，我表示衷心感谢<br><br>
      <b>其次</b>，感谢父母在完成项目过程中给予的支持和鼓励。<br><br>
      <b>最后</b>，感谢在csdn上踊跃回答问题的学者们。使本人在摸索的过程中能够将问题迎刃而解<br><br>
      <el-divider content-position="left"><b>项目评价与推广</b></el-divider>
      考虑到个人能力有限，在本作品中并未涉及同步异步问题以及在数据量达到百万级别时应采用的分表查询等方法<br><br>
      实现了<b>权限控制，路由阻截，头像实时更新</b>等功能。
      未来可应用于<b>博客网站开发，教师作业发布与学生选课</b>等兼顾交互性与鲁棒性的高性能系统。
      <el-divider content-position="center"><b>技术参考</b></el-divider>
      <a>参考b站程序员青戈的SSM教程，基于该教程对其业务功能和底层逻辑等方面进行了修改和拓展</a>
    </el-card>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>

    <el-row :gutter="30">
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px">基于SSM框架的华侨大学后台管理系统</div>
        <div class="ml-5">
          <el-button type="primary"><i class="el-icon-link"></i> <a style="color: #fff" href="">github源码</a></el-button>
          <el-button type="primary"><i class="el-icon-link"></i> <a style="color: #fff" href="">B站视频指导</a></el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px">技术栈</div>
        <el-row>
          <el-col :span="12" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>SpringBoot2</div>
            <div>Hutool</div>
            <div>Poi</div>
            <div>Lombok</div>
            <div>Mybatis/Mybatis-plus</div>
            <div>linux</div>
          </el-col>
          <el-col :span="12" style="line-height: 30px">
            <b>前端</b>
            <div>Vue3</div>
            <div>Vue-Router</div>
            <div>VueX</div>
            <div>ElementUI</div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "More",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>
<style>
@font-face {
  font-family: '行书';
  src: url("font/方正字迹-宝庆行书 简.TTF");
  font-weight: normal;
  font-style: normal;
}
</style>
