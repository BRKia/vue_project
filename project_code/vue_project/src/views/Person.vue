<template>
  <el-card style="width: 500px; text-align: center; padding: 20px; border: 1px solid #ccc">
    <el-form label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          action="http://124.70.63.245:9090/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :data={nickname:user.nickname}
      >
        <img v-if="form.avatar" :src="form.avatar" class="avatar" alt="">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <!--加了model和rules后，使v-model和model对应-->
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="权限">
        <el-input v-model="form.role" disabled autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input type="textarea" v-model="form.address" autocomplete="off" @keyup.enter.native="save"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      roles: [],
    }
  },
  created() {
    this.initialUser()
  },
  methods: {
    initialUser(){
      this.request.get("/user/username/" + this.user.username).then(res => {
        this.form = res.data
      })
      // this.form = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {}
    },
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === "200") {
          this.$router.push("/home")
          this.$message.success("修改成功")
          //触发父级的refreshUser方法
          this.$emit("refreshUser")
          // // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
          })
          // this.load_user()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatar = res
    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}

.avatar {
  width: 200px;
  height: 138px;
  display: block;
}
</style>