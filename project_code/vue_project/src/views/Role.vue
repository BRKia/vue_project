<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入角色名称" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="load_search"></el-input>
      <!--      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email" @keyup.enter.native="load_search"></el-input>-->
      <!--      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address" @keyup.enter.native="load_search"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
      <el-button type="warning" @click="reset">重置 <i class="el-icon-refresh"></i></el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
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

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
      <el-table-column prop="name" label="名称" align="center"></el-table-column>
      <el-table-column prop="description" label="描述" align="center"></el-table-column>
      <el-table-column prop="flag" label="标识" align="center"></el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="info" slot="reference" @click="distribute(scope.row.id)">分配菜单 <i class="el-icon-menu"></i></el-button>
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
            <el-button type="danger" slot="reference" @keyup.enter.native="del(scope.row.id)">删除 <i
                class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="form" ref="userForm">
        <!--加了model和rules后，使v-model和model对应-->
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
        <el-form-item label="标识" prop="flag">
          <el-input v-model="form.flag" autocomplete="off" @keyup.enter.native="save"></el-input>
<!--          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>-->
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" autocomplete="off" @keyup.enter.native="save"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单" :visible.sync="MenuDialogVisible" width="30%">
      <el-tree
          :data="menuData"
          show-checkbox
          :default-expanded-keys="expends"
          :default-checked-keys="checks"
          :props="props"
          node-key="id"
          ref="tree"
          :check-strictly="true"
      >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span> <!--菜单图标和菜单名称-->
          </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="MenuDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="配置需要重新登陆，要现在重启吗？" :visible.sync="logDialogVisible" width="30%">
      <div slot="footer" class="dialog-footer">
        <el-button @click="logDialogVisible = false">稍后再说</el-button>
        <el-button type="primary" @click="$store.commit('logout')">重 启 <i class="el-icon-s-home"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      // rules: {
      //   username: [
      //     { required: true, message: '请输入用户名', trigger: 'blur' },
      //   ],
      //   password: [
      //     { required: true, message: '请输入密码', trigger: 'blur' },
      //   ]
      // },
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      name: "",
      description: "",
      id: "",
      password: "",
      form: {},
      dialogFormVisible: false, //新增角色的界面
      MenuDialogVisible: false, //分配菜单的界面
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: '',
      ids: [],
      logDialogVisible: false
    }
  },
  created() {
    this.load()
  },
  methods: {
    distribute(roleId) {
      console.log("roleId:"+ roleId)
      this.MenuDialogVisible = true
      this.roleId = roleId
      console.log("this.roleId:"+ this.roleId)
      this.request.get("/menu", {
        params: {
          name: ''
        }
      }).then(res => {
        this.menuData = res.data
        this.expends = this.menuData.map(v => v.id) //把后台的id返回给expends数组，有几个id就展开几个
      })
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data //设置已选数据被选中
        console.log(res.data)
      })
    },
    load() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          description: this.description
        }
      }).then(res => {
        console.log(res.data)
        // this.$message.success("查询到"+res.data.total+"个人员信息")
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    load_search() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res.data)
        if (res.data.total === 1) {
          this.$message.warning("仅查询到" + res.data.total + "条权限信息")
        } else if (res.data.total === 0) {
          this.$message.error("未查询到相关权限信息")
        } else {
          this.$message.success("查询到" + res.data.total + "条权限信息")
        }
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/role", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
        } else {
          // res.records = ""
          this.$message.error("保存失败")
        }
        this.dialogFormVisible = false
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      // this.form.password.visible(true)
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
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
      this.request.post("/role/del/batch", ids).then(res => {
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
    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate)
    },
    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.flag
      // 请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data
        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })
          }
        })
        this.MenuDialogVisible = true
      })
    },
    saveRoleMenu(){
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if(res.code === "200"){
          this.$message.success('绑定成功')
          // if(this.user.role === 'ADMIN'){}
          this.logDialogVisible = true
        }else{
          this.$message.error(res.msg)
        }
        this.MenuDialogVisible = false
      })
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee !important;
}
</style>