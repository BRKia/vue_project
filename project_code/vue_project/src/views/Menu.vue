<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name" @keyup.enter.native="load_search"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入路径" suffix-icon="el-icon-position" class="ml-5" v-model="path" @keyup.enter.native="load_search"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入图标" class="ml-5" v-model="icon" @keyup.enter.native="load_search"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
      <el-button type="warning" @click="reset">重置 <i class="el-icon-refresh"></i></el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd('')">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" @keyup.enter.native="delBatch">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" row-key="id" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <!--        让本字段以icon的形式展示-->
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="pagePath" label="页面路径"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作"  width="380" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)"  v-if="!scope.row.pid && !scope.row.path">新增子菜单 <i class="el-icon-plus"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" @keyup.enter.native="del(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small" :model="form" ref="userForm" :rules="rules">
<!--        <el-form-item label="ID" prop="id">-->
<!--          <el-input v-model="form.id"  autocomplete="off" @keyup.enter.native="save" ></el-input>-->
<!--        </el-form-item>-->
        <!--加了model和rules后，使v-model和model对应-->
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="页面路径" prop="PagePath">
          <el-input v-model="form.pagePath"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="图标">
<!--          <el-input v-model="form.icon"  autocomplete="off" @keyup.enter.native="save" ></el-input>-->
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description"  autocomplete="off" @keyup.enter.native="save" ></el-input>
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
  name: "Menu",
  data() {
    return {
      rules: {
        // id: [
        //   { required: true, message: '请输入菜单ID', trigger: 'blur' },
        // ],
      //   password: [
      //     { required: true, message: '请输入密码', trigger: 'blur' },
      //   ]
      },
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      path: "",
      icon: "",
      description: "",
      id: "",
      password: "",
      pagePath: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      options: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/menu", {
        params: {
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data
      })
    },
    load_search() {
      this.request.get("/menu/find/"+this.name, {

      }).then(res => {
        console.log(res.data.length)
        if (res.data.length === 1) {
          this.$message.warning("仅查询到" + res.data.length + "条权限信息")
        } else if (res.data.length === 0) {
          this.$message.error("未查询到相关权限信息")
        } else {
          this.$message.success("查询到" + res.data.length + "条权限信息")
        }
        this.tableData = res.data
        this.total = res.data.length
      })
    },
    save() {
      this.$refs['userForm'].validate((valid) => { //根据UserForm校验
        if(valid){
          this.request.post("/menu", this.form).then(res => {
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
    handleAdd(pid) {
      this.handleEdit({}) //在新增按钮时，显示可增添的图标
      // 传空对象表示form:{},null和""达不到此效果
      if(pid){
        this.form.pid = pid
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      //请求图标数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    del(id) {
      this.request.delete("/menu/" + id).then(res => {
        if (res.code === '200') {
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
      this.request.post("/menu/del/batch", ids).then(res => {
        if (res.code === '200') {
          // res.records = ""
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.icon = ""
      this.path = ""
      this.$message.success("已重置")
      this.load()
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
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
/*字体大小*/
.fontSize18{
  font-size: 18px;
}
/*头部字体*/
.fontSize12{
  font-size: 12px;
}
</style>