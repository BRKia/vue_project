<template>
  <div class = "container">
    <div class="login-form">
    <div>  <!--border-radius: 给边框一些圆角 -->
      <div style="margin: 20px 0; text-align: center; font-size: 28px; color: #3d5245">
        <b>登 录</b>
      </div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium"
                    style="margin: 10px 0; width: 280px; left: 30px"
                    prefix-icon="el-icon-user"
                    v-model="user.username"
                    @keyup.enter.native="login" placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium"
                    style="margin: 10px 0; width: 280px; left: 30px"
                    prefix-icon="el-icon-lock"
                    show-password
                    v-model="user.password"
                    @keyup.enter.native="login"
                    placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: center">
          <el-button type="submit" size="mini"  autocomplete="off" @click="login" style="width: 240px">登录</el-button>
          <el-button size="mini"  autocomplete="off" @click="register" style="background-color: inherit; color: cadetblue">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
  </div>
</template>

<script>
import {setRouters} from "@/router";

export default {
  // created(){
  //   this.getUser()
  // },
  name: "Login",
  data() {
    return {
      user: {
        password:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).password:"",
        username:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username:""
      },
      rules: {
        username: [ //username与prop的username相对应
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 0 , message: '长度不为0', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    register(){
      this.$router.push('/register')
    },
    login() {
      console.log(this.user.username)
      this.$refs['userForm'].validate((valid) => { //根据UserForm校验
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if(res.code === "200") {
              localStorage.setItem("user",JSON.stringify(res.data)) //存储用户信息到浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus)) //存储用户信息到浏览器
              setRouters() //动态设置当前用户的路由
              this.$router.push("/")
              this.$message.success("登录成功!")
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style>
.container{
  width: 100vw;
  height: 100vh;
  background: url("../assets/scenery01.jpg");
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: cover;
}

.login-form {
  width: 40vh;
  height: 40vh;
  /*background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);*/
  overflow: hidden; /* 把超出部分隐藏掉 */
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 100;
  background: inherit;
  border-radius: 18px;
}

/*.login-form::before{*/
/*  content: '';*/
/*  width: 40vh;*/
/*  height: 30vh;*/
/*  background: inherit;*/
/*  box-shadow: inset 0 0 0 200px rgba(255, 255, 255, 0.25);*/
/*  top: -10px;*/
/*  left: -10px;*/
/*  overflow: hidden;*/
/*  filter: blur(5px);*/
/*  z-index: -1;*/
/*}*/

.login-form input,
.login-form button{
  margin: 6px 0;
  height: 36px;
  background-color: rgba(255, 255, 255, 0.3);;
  padding: 0 14px;
  color: #3d5245;
}

.login-form input::placeholder{
  color: #3d5245;
}

/*去除选中时高亮*/
.login-form button:focus,
.login-form input:focus{
  outline:0;
}

.login-form button{
  margin-top: 24px;
  background-color: rgba(2209, 179, 9, 0.4);
  color: white;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: 0.4s;
}

.login-form button:hover{
  background-color: rgba(209, 179,9 ,0.7);
}

.login-form button::before,
.login-form button::after{
  content: "";
  display: block;
  width: 80px;
  height: 100%;
  background: rgba(179,255,210,0.5);
  opacity: 0.5;
  position: absolute;
  top: 0;
  left: 0;
  transform: skewX(-0.06turn);
  filter: blur(30px);
  overflow: hidden;
  transform: translateX(-100px);
}

.login-form button:after{
  width: 40px;
  background: rgba(179, 255, 210, 0.3);
  left: 60px;
  opacity: 0;
  filter: blur(5px);
}

.login-form button:hover::before{
  transition: 1s;
  transform: translateX(320px);
  opacity: 0.7s;
}

.login-form button:hover::after{
  transition: 1s;
  transform: translateX(320px);
  opacity: 1;
}

</style>