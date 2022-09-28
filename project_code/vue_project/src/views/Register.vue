<template>
  <div class = "register-container">
    <div class="register-form">
    <div>  <!--border-radius: 给边框一些圆角 -->
      <div style="margin: 20px 0; text-align: center; font-size: 28px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium"
                    style="margin: 10px 0; width: 420px; left: 30px"
                    prefix-icon="el-icon-user"
                    v-model="user.username"
                    @keyup.enter.native="register" placeholder="请输入用户名, 如名字缩写+随机数字"></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
        <el-input size="medium"
                  style="margin: 10px 0; width: 420px; left: 30px"
                  prefix-icon="el-icon-s-custom"
                  v-model="user.nickname"
                  @keyup.enter.native="register"
                  placeholder="请输入昵称, 如中文名"
        ></el-input>
      </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium"
                    style="margin: 10px 0; width: 420px; left: 30px"
                    prefix-icon="el-icon-lock"
                    show-password
                    v-model="user.password"
                    @keyup.enter.native="register"
                    placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input size="medium"
                    style="margin: 10px 0; width: 420px; left: 30px"
                    prefix-icon="el-icon-lock"
                    show-password
                    v-model="user.confirmPassword"
                    @keyup.enter.native="register"
                    placeholder="请确认密码"
          ></el-input>
        </el-form-item>

        <el-form-item prop="role">
          <el-select clearable v-model="user.role" placeholder="请选择角色" size="medium" style="margin: 10px 0; width: 220px; left: 30px">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag" v-if="item.flag !== 'ADMIN' && item.flag !== 'TEACHER'"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item style="margin: 10px 0; text-align: right; width: 420px; left: 30px">
          <el-button type="primary" size="small"  autocomplete="off" @click="register" style="width: 300px">注册</el-button>
          <el-button size="small"  autocomplete="off" @click="$router.push('/login')" style="background-color: inherit">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      roles:[
        {name: '用户', flag: 'NORMAL'},
        {name: '医学生', flag: 'STUDENT_CLINICAL'},
        {name: '汉语言学生', flag: 'STUDENT_CHINESE'},
      ],
      rules: {
        username: [ //username与prop的username相对应
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          {required: true, message: '请确认密码', trigger: 'blur'},
          { min: 1, max: 20, message: '密码不相符', trigger: 'blur' }
        ],
        nickname: [
          {required: true, message: '昵称不为空', trigger: 'blur'},
        ],
        role: [
          {required: true, message: '你什么身份什么地位', trigger: 'blur'},
        ]
      }
    }
  },
  created() {
    // this.request.get("/role").then(res => {
    //   this.roles = res.data
    // })
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => { //根据UserForm校验
        if (valid) {  // 表单校验合法
          if( this.user.password === this.user.confirmPassword) {
            this.request.post("/user/register", this.user).then(res => {
              if (res.code === "200") {
                localStorage.setItem("user",JSON.stringify(res.data))
                this.$message.success("注册成功!")
                this.$router.push("/login")
              } else {
                this.$message.error(res.msg)
              }
            })
          }else{
            this.$message.error("两次密码不一致")
            return false
          }
        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style>
.register-container{
  width: 100vw;
  height: 100vh;
  background: url("../assets/secenery.jpg");
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: cover;
}

.register-form {
  width: 50vh;
  height: 70vh;
  /*background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);*/
  overflow: hidden; /* 把超出部分隐藏掉 */
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 100;
  background: inherit;
  border-radius: 18px;
}

/*.register-form::before{*/
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

.register-form input,
.register-form button{
  margin: 6px 0;
  height: 36px;
  background-color: rgba(255, 255, 255, 0.3);;
  padding: 0 14px;
  color: #3d5245;
}

.register-form input::placeholder{
  color: #3d5245;
}

/*去除选中时高亮*/
.register-form button:focus,
.register-form input:focus{
  outline:0;
}

.register-form button{
  margin-top: 24px;
  background-color: rgba(2209, 179, 9, 0.4);
  color: white;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: 0.4s;
}

.register-form button:hover{
  background-color: rgba(209, 179,9 ,0.7);
}

.register-form button::before,
.register-form button::after{
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

.register-form button:after{
  width: 40px;
  background: rgba(179, 255, 210, 0.3);
  left: 60px;
  opacity: 0;
  filter: blur(5px);
}

.register-form button:hover::before{
  transition: 1s;
  transform: translateX(320px);
  opacity: 70%;
}

.register-form button:hover::after{
  transition: 1s;
  transform: translateX(320px);
  opacity: 1;
}

</style>