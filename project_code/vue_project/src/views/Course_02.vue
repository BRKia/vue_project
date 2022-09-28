<template>
  <div>
    <div style="margin: 15px 0">
      <el-input style="width: 200px" placeholder="请输入课程名称" suffix-icon="el-icon-search" v-model="name" @keyup.enter.native="load_search"></el-input>
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
      <!--      <i class="el-icon-thumb"></i>-->
      <el-button type="warning" @click="reset">重置   <i class="el-icon-refresh"></i></el-button>
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
        <el-button type="danger" slot="reference" @keyup.enter.native="delBatch" class="ml-5">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-button @click="clearFilter" type="success" class="ml-5">清空所有筛选 <i class="el-icon-toilet-paper"></i></el-button>
    </div>
    <el-table :data="tableData" height="680" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" ref="filterTable">
      <el-table-column type="selection" align="center" fixed></el-table-column>
      <el-table-column prop="id" label="ID" align="center" sortable></el-table-column>
      <el-table-column prop="property" label="课程性质" align="center"
                       :filters="[{text: '专业基础课', value: '专业基础课'},
                                  {text: '专业选修课', value: '专业选修课'},
                                  {text: '专业核心课', value: '专业核心课'},
                                  {text: '通识教育选修课', value: '通识教育选修课'},
                                  {text: '通识教育必修课', value: '通识教育必修课'},
                                  {text: '社会实践', value: '社会实践'},
                                  {text: '专业实践', value: '专业实践'}]"
                       :filter-method="filterHandler"
                       column-key="property"
      ></el-table-column>
      <el-table-column prop="name" label="课程名称" align="center" style="width: 200px"></el-table-column>
      <el-table-column prop="score" label="学分" align="center" sortable></el-table-column>
      <el-table-column prop="term" label="学期" align="center" sortable
                       :filters="[{text: '2021.9 - 2022.1 大一', value: 1},
                                  {text: '2022.2 - 2022.6', value: 2},

                                  {text: '2022.9 - 2023.1 大二', value: 3},
                                  {text: '2023.2 - 2023.6', value: 4},

                                  {text: '2023.9 - 2024.1 大三', value: 5},
                                  {text: '2024.2 - 2024.6', value: 6},

                                  {text: '2024.9 - 2025.1 大四', value: 7},
                                  {text: '2025.2 - 2025.6', value: 8},
                                  ]"
                       :filter-method="filterHandler"
                       column-key="term"
      ></el-table-column>
      <el-table-column prop="state" label="课程状态" align="center"
                       :filters="[{text: '未修', value: '-'},
                                  {text: '已修', value: '-√'}]"
                       :filter-method="filterState"
                       column-key="state"
      >
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.state === '-'">未修</el-tag>
          <el-tag type="success" v-if="scope.row.state !== '-'">已修</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="teacher" label="授课老师" align="center"></el-table-column>
      <el-table-column label="操作"  width="400" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="selectCourse(scope.row)" plain
                     v-if="user.role === 'STUDENT_CHINESE' && (scope.row.property === '通识教育选修课' ||scope.row.property === '专业选修课')">选课 <i class="el-icon-edit"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)" v-if="user.role === 'ADMIN' || user.role === 'TEACHER'">编辑 <i class="el-icon-edit"></i></el-button>
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
      <!--      <img :src="form.avatar" alt=""-->
      <!--           style="width: 130px; border-radius: 90%; position: relative; bottom: 15px; left: 150px">-->
      <el-form label-width="80px" size="small" :model="form" ref="userForm">
        <!--加了model和rules后，使v-model和model对应-->
        <el-form-item label="课程性质">
          <el-input v-model="form.property"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="form.name"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="form.score"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="学期" prop="term">
          <el-input v-model="form.term"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="课程状态" prop="state">
          <el-select clearable v-model="form.state" placeholder="请选择" style="width: 340px;">
            <el-option v-for="item in accomplish" :value="item.value" :label="item.label" :key="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="授课老师">
          <el-select clearable v-model="form.teacherId" placeholder="请选择" style="width: 340px;">
            <el-option v-for="item in teacherIds" :key="item.id" :label="item.nickname" :value="item.id"></el-option>
          </el-select>
          <!--          <el-input v-model="form.teacherId"  autocomplete="off" @keyup.enter.native="save" ></el-input>-->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Course_02",
  data(){
    return{
      // 课表参数

      // 其他参数
      tableData: [],
      name: '',
      size: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 150,
      total: 0,
      form:{},
      dialogFormVisible: false,
      property: '',
      teacherIds: [],
      user : localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {},
      selectedCourses: [],
      accomplish: [
        {value:'已修', label:'已修'},
        {value:'-', label:'未修'}
      ]
    }
  },
  created() {
    this.load()
  },
  methods:{
    selectCourse(row){
      this.request.post('/course02/studentCourse/' + this.user.id + '/' + row.id).then(res => {
        if(res.code === '200'){
          this.$message.success('《' + row.name + "》   选课成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    save() {
      this.request.post("/course02", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          // res.records = ""
          this.$message.error("保存失败")
        }
      })
    },
    load() {
      this.request.get("/course02/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name:this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      // 获取授课老师id
      this.request.get("/user/role/TEACHER").then(res => {
        this.teacherIds = res.data
      })
      // 拿到已选课程的id
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: this.user.nickname,
        }
      }).then(res => {
        this.selectedCourses = res.data.records.stuCourses
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/course02/" + id).then(res => {
        if (res.code === "200") {
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
      this.request.post("/course02/del/batch", ids).then(res => {
        if (res.code === "200") {
          // res.records = ""
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    clearFilter () {
      this.$refs.filterTable.clearFilter('term');
      this.$refs.filterTable.clearFilter('property');
      this.$refs.filterTable.clearFilter('state');
      this.$message.success('清空成功')

    },filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    filterState(value, row, column) {
      const property = column['property'];
      if (value.length !== 1) {
        return row[property] !== '-'
      }
      return row[property] === value;
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    load_search(){
      this.request.get("/course02/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res.data)
        if(res.data.total === 1){
          this.$message.warning("仅查询到"+res.data.total+"个相关课程")
        } else if(res.data.total === 0){
          this.$message.error("未查询到相关课程")
        } else{
          this.$message.success("查询到"+res.data.total+"个相关课程")
        }
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.name = ""
      this.load()
      this.$message.success("已重置")
    },
  }
}
</script>

<style scoped>

</style>