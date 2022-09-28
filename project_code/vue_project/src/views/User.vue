<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-user-solid" class="ml-5" v-model="username" @keyup.enter.native="load_search"></el-input>
      <el-input style="width: 200px" placeholder="请输入昵称" suffix-icon="el-icon-user" class="ml-5" v-model="nickname" @keyup.enter.native="load_search"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email" @keyup.enter.native="load_search"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address" @keyup.enter.native="load_search"></el-input>
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
      <el-button type="warning" @click="reset">重置 <i class="el-icon-refresh"></i></el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd" v-if="user.role === 'ADMIN'">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" @keyup.enter.native="delBatch" v-if="user.role === 'ADMIN'">批量删除 <i class="el-icon-finished"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://124.70.63.245:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5" v-if="user.role === 'ADMIN'">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5" v-if="user.role === 'ADMIN'">导出 <i class="el-icon-top"></i></el-button>
      <el-button @click="clearFilter" type="success" class="ml-5">清空所有筛选 <i class="el-icon-toilet-paper"></i></el-button>
      <el-button @click="seekMyCourses()" type="info" class="ml-5" round v-if="user.role !== 'ADMIN' && user.role !== 'TEACHER' ">我的选课 <i class="el-icon-monitor"></i></el-button>
    </div>

    <el-table :data="tableData" height="680" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" ref="filterTable">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="username" label="用户名" width="140" align="center"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
      <el-table-column prop="phone" label="电话" v-if="user.role !== 'STUDENT_CLINICAL' && user.role !== 'STUDENT_CHINESE'" align="center"></el-table-column>
      <el-table-column prop="address" label="地址" v-if="user.role !== 'STUDENT_CLINICAL' && user.role !== 'STUDENT_CHINESE'" align="center"></el-table-column>
      <el-table-column prop="role" label="权限"  sortable
                       :filters="[{text: '管理员', value: 'ADMIN'},
                                  {text: '普通用户', value: 'NORMAL'},
                                  {text: '老师', value: 'TEACHER'},
                                  {text: '汉语言学生', value: 'STUDENT_CHINESE'},
                                  {text: '医学生', value: 'STUDENT_CLINICAL'},]"
                       :filter-method="filterHandler"
                       column-key="role"
                       align="center"
      >
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.role === 'ADMIN'">管理员</el-tag>
          <el-tag type="warning" v-if="scope.row.role === 'TEACHER'">老师</el-tag>
          <el-tag type="success" v-if="scope.row.role === 'STUDENT_CHINESE'">汉语言学生</el-tag>
          <el-tag type="success" v-if="scope.row.role === 'STUDENT_CLINICAL'">医学生</el-tag>
          <el-tag type="info" v-if="scope.row.role === 'NORMAL'">普通用户</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="360" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="user.role === 'ADMIN'">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="primary" @click="seekCourses(scope.row)"  v-if="scope.row.role === 'TEACHER'">查看教授课程 <i class="el-icon-collection"></i></el-button>
          <el-button type="warning" @click="seekStuCourses(scope.row)"  v-if="scope.row.role === 'STUDENT_CLINICAL' || scope.row.role === 'STUDENT_CHINESE'">查看已选课程 <i class="el-icon-document"></i></el-button>
<!--          || scope.row.role === 'STUDENT_CHINESE'-->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" @keyup.enter.native="del(scope.row.id)" v-if="user.role === 'ADMIN'">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
      <img :src="form.avatar" alt=""
           style="width: 130px; border-radius: 100%; position: relative; bottom: 15px; left: 200px">
      <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="userForm">
        <!--加了model和rules后，使v-model和model对应-->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="课程信息" :visible.sync="vis" width="30%" >
      <el-card style="color: chocolate"> {{ this.checkUser.nickname }} </el-card>
      <el-table :data="courses" border stripe>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="score" label="学分" sortable></el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog title="已选 选修课程信息" :visible.sync="stuVis" width="50%" center >
      <el-card style="color: navy">医学生 {{ this.checkUser.nickname }}</el-card>
      <el-table :data="stuCourses" border stripe ref="filterTable" height="500">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="term" label="学期" sortable
                         :filters="[{text: '2021.9 - 2022.1 大一', value: 1},
                                    {text: '2022.2 - 2022.6', value: 2},

                                    {text: '2022.9 - 2023.1 大二', value: 3},
                                    {text: '2023.2 - 2023.6', value: 4},

                                    {text: '2023.9 - 2024.1 大三', value: 5},
                                    {text: '2024.2 - 2024.6', value: 6},

                                    {text: '2024.9 - 2025.1 大四', value: 7},
                                    {text: '2025.2 - 2025.6', value: 8},
                                    ]"
                         :filter-method="filterHandler"></el-table-column>
        <el-table-column prop="score" label="学分" sortable></el-table-column>
<!--        <el-empty v-if="stuCourses === []" description="暂无选修课"></el-empty>-->
      </el-table>
    </el-dialog>
    <el-dialog title="已选 选修课程信息" :visible.sync="stuVis02" width="50%" center >
      <el-card style="color: navy">汉语言学生 {{ this.checkUser.nickname }}</el-card>
      <el-table :data="stuCourses02" border stripe ref="filterTable" height="500">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="term" label="学期" sortable
                         :filters="[{text: '2021.9 - 2022.1 大一', value: 1},
                                    {text: '2022.2 - 2022.6', value: 2},

                                    {text: '2022.9 - 2023.1 大二', value: 3},
                                    {text: '2023.2 - 2023.6', value: 4},

                                    {text: '2023.9 - 2024.1 大三', value: 5},
                                    {text: '2024.2 - 2024.6', value: 6},

                                    {text: '2024.9 - 2025.1 大四', value: 7},
                                    {text: '2025.2 - 2025.6', value: 8},

                                    {text: '2025.9 - 2026.1 大五', value: 9},
                                    {text: '2026.2 - 2026.6', value: 10},
                                    ]"
                         :filter-method="filterHandler"></el-table-column>
        <el-table-column prop="score" label="学分" sortable></el-table-column>
<!--        <el-empty v-if="stuCourses02 === []" description="暂无选修课"></el-empty>-->
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      rules: {
        username: [ //username与prop的username相对应
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 3000,
      username: "",
      email: "",
      address: "",
      nickname: "",
      id: "",
      password: "",
      form: {
      },
      dialogFormVisible: false,
      multipleSelection: [],
      roles:[],
      menus:[],
      courses: [],
      stuCourses: [],
      stuCourses02: [],
      vis: false,
      stuVis: false,
      stuVis02: false,
      user : localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {},
      checkUser: this.user
    }
  },
  created() {
    this.load()
  },
  methods: {
    seekChinese(nickname){
      this.request.get("/user/page02", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: nickname,
        }
      }).then(res => {
        this.stuCourses02 = res.data.records[0].stuCourses02
      })
    },
    seekClinical(nickname){
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: nickname,
        }
      }).then(res => {
        this.stuCourses = res.data.records[0].stuCourses
      })
    },
    seekMyCourses(){
      console.log(this.courses)
      this.checkUser = this.user
      if(this.user.role === 'STUDENT_CLINICAL'){
        this.seekClinical(this.user.nickname)
        this.stuVis = true
      }else if(this.user.role === 'STUDENT_CHINESE'){
        this.seekChinese(this.user.nickname)
        this.stuVis02 = true
      }

    },
    seekCourses(row) {
      this.checkUser = row
      this.courses = row.courses
      this.vis = true
    },
    seekStuCourses(row) {
      this.checkUser = row
      if(row.role === 'STUDENT_CLINICAL'){
        this.seekClinical(row.nickname)
        this.stuVis = true
      }else if(row.role === 'STUDENT_CHINESE'){
        this.seekChinese(row.nickname)
        this.stuVis02 = true
      }
    },
    clearFilter () {
      this.$refs.filterTable.clearFilter('role');
      this.$message.success('清空成功')
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    exp() {
      window.open("http://124.70.63.245:9090/user/export")
    },
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: this.nickname,
          email: this.email,
          address: this.address,
        }
      }).then(res => {
        console.log(res.data)
        // this.$message.success("查询到"+res.data.total+"个人员信息")
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
      this.checkUser = this.user
    },
    load_search(){
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: this.nickname,
          email: this.email,
          address: this.address,
          username: this.username
        }
      }).then(res => {
        console.log(res.data)
        if(res.data.total === 1){
          this.$message.warning("仅查询到"+res.data.total+"条人员信息")
        } else if(res.data.total === 0){
          this.$message.error("未查询到相关人员信息")
        } else{
          this.$message.success("查询到"+res.data.total+"条人员信息")
        }
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.$refs['userForm'].validate((valid) => { //根据UserForm校验
        if(valid){
          this.request.post("/user", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            } else {
              // res.records = ""
              this.$message.error("保存失败")
            }
          })
        }else{
          this.$message.error("填写错误")
        }
      });
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      // this.form.password.visible(true)
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.data) {
          // res.records = ""
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.data) {
          // res.records = ""
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.nickname = ""
      this.email = ""
      this.address = ""
      this.username = ""
      this.$message.success("已重置")
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      // this.load_search()
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>