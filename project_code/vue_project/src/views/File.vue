<template>
  <div>
    <div style="margin: 15px 0">
      <el-input style="width: 200px" placeholder="请输入文件名称" suffix-icon="el-icon-search" v-model="name" @keyup.enter.native="load_search"></el-input>
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
<!--      <i class="el-icon-thumb"></i>-->
      <el-button type="warning" @click="reset">重置   <i class="el-icon-refresh"></i></el-button>
    </div>
    <div style="margin: 10px 0">
      <el-upload action="http://124.70.63.245:9090/file/upload" :data={nickname:user.nickname} :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5" v-if="user.role === 'ADMIN' || user.role === 'TEACHER'">上传文件 <i class="el-icon-cloudy"></i></el-button>
<!--        <el-button v-else type="primary"></el-button>-->
      </el-upload>
      <el-button type="info" class="ml-5" slot="reference" @click="recycleFile" v-if="user.role === 'ADMIN' || user.role === 'TEACHER'" plain>回收站 <i class="el-icon-delete-solid"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" @keyup.enter.native="delBatch" v-if="user.role === 'ADMIN'">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center"></el-table-column>
      <el-table-column prop="id" label="ID" align="center"></el-table-column>
      <el-table-column prop="name" label="文件名称" align="center"></el-table-column>
      <el-table-column prop="type" label="文件类型" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.type === 'png' || scope.row.type === 'jpg'"><i class="el-icon-picture-outline"></i></el-tag>
          <el-tag type="success" v-else-if="scope.row.type === 'txt' || scope.row.type === 'xlsx'"><i class="el-icon-document"></i></el-tag>
          <el-tag type="success" v-else><i class="el-icon-tickets"></i></el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="size" label="文件大小(KB)" align="center" >
<!--        <template  slot-scope="scope"></template>-->
      </el-table-column>
      <el-table-column prop="uploadUser" label="上传作者" align="center"></el-table-column>
      <el-table-column prop="uploadTime" label="上传时间" align="center" width="200px"></el-table-column>
<!--      <el-table-column label="预览">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="primary" @click="preview(scope.row.url)">预览</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="下载" align="center" v-if="user.role !== 'TEACHER'">
        <template slot-scope="scope" >
          <el-button type="primary" @click="download(scope.row)" >下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="文件下载记录" align="center" v-if="user.role === 'TEACHER' || user.role === 'ADMIN'">
        <template slot-scope="scope" >
          <el-button type="text" @click="checkDownloader(scope.row)" >展开</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用" align="center" v-if="user.role === 'TEACHER' || user.role === 'ADMIN'"
                       :filters="[{text: '可下载', value: '1'},
                                  {text: '禁止下载', value: '0'}]"
                       :filter-method="filterHandler"
                       column-key="enable"
      >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="200" align="center" v-if="user.role === 'TEACHER' || user.role === 'ADMIN'">
        <template slot-scope="scope">
          <el-button type="info" @click="handleEdit(scope.row)">属性 <i class="el-icon-edit"></i></el-button>
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
    <el-dialog title="回收站" :visible.sync="recycleVis" width="50%" center >
      <el-button type="success" @click="recoverBatch" round >一键还原</el-button>
      <el-button type="danger" @click="delBatchEver" round >清空回收站</el-button>
      <el-table :data="deleteFile" border stripe ref="filterTable" height="400">
        <el-table-column prop="name" label="文件名"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="info" @click="handleEdit(scope.row)">属性 <i class="el-icon-edit"></i></el-button>
            <el-button type="success" @click="recover(scope.row)">还原 <i class="el-icon-edit"></i></el-button>
            <el-button type="danger" @click="delEver(scope.row)">永久删除 <i class="el-icon-remove-outline"></i></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="文件信息" :visible.sync="dialogFormVisible" width="30%" >
<!--      <img :src="form.avatar" alt=""-->
<!--           style="width: 130px; border-radius: 100%; position: relative; bottom: 15px; left: 200px">-->
      <el-form label-width="120px" size="small" :model="form" ref="userForm">
        <!--加了model和rules后，使v-model和model对应-->
        <el-form-item label="文件名" prop="name">
          <el-input v-model="form.name"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="文件类型" prop="type">
          <el-input v-model="form.type"  autocomplete="off" @keyup.enter.native="save" ></el-input>
        </el-form-item>
        <el-form-item label="文件大小(KB)" prop="size">
          <el-input v-model="form.size"  autocomplete="off" @keyup.enter.native="save" disabled></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="下载详情页" :visible.sync="downloaderVis" width="30%" >
      <el-card style="color: chocolate"> 文件: {{ this.currentFile.name }} </el-card>
      <el-table :data="downloader" border stripe>
        <el-table-column prop="nickname" label="下载用户" align="center"></el-table-column>
        <el-table-column prop="downloadTime" label="下载时间" sortable></el-table-column>
      </el-table>
    </el-dialog>

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
  </div>
</template>

<script>
export default {
  name: "File",
  data(){
    return{
      tableData: [],
      name: '',
      type: '',
      size: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      // enableDownload: localStorage.getItem("enableDownload")?JSON.parse(localStorage.getItem("enableDownload")) : {},
      user : localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {},
      imgView: 'https://view.officeapps.live.com/op/view.aspx?src=',
      // form: {},
      deleteFile: [],
      recycleVis: false,
      dialogFormVisible: false,
      downloaderVis: false,
      form: {},
      deleteFileIds: [],
      currentFile: {},
      downloader: []
    }
  },
  created() {
    this.getUser()
    this.load()
  },
  methods:{
    // 永久删除
    delEver(row){
      // console.log("进入")
      this.request.post("/file/delFor/" + row.id).then(res => {
        if (res.code === "200") {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
      this.recycleFile()
    },
    // 一键清空
    delBatchEver(){
      this.request.post("/file/delFor/batch", this.deleteFileIds).then(res => {
        if (res.code === "200") {
          this.$message.success("删除成功")
          this.load()
        } else if(res.code === '600'){
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
      this.recycleFile()
    },
    save() {
      this.$refs['userForm'].validate((valid) => { //根据UserForm校验
        if(valid){
          this.request.post("/file/update", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
              // 如果是在回收站里面保存，则刷新回收站页面
              if(this.recycleVis === true){
                this.recycleFile()
              }
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
    // 一键还原
    recoverBatch() {
      this.request.post("/file/recoverFile/batch", this.deleteFileIds).then(res => {
        if (res.code === "200") {
          this.$message.success("一键还原成功")
          this.load()
        } else {
          this.$message.error("一键还原失败")
        }
      })
      this.recycleFile()
    },
    // 文件还原
    recover(row){
      this.request.post("/file/" + row.id).then(res => {
        if (res.code === "200") {
          this.$message.success("文件" + row.name + "已还原")
          this.load()
        } else {
          this.$message.error("文件" + row.name + "还原失败")
        }
      })
      this.recycleFile()
    },
    getUser(){
      this.user = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {}
    },
    recycleFile(){
      this.request.get("/file/recycle", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        this.deleteFile = res.data.records
        this.deleteFileIds = res.data.records.map(v => v.id)
        if(res.data.total === 0){
          this.$message.success('回收站为空')
        } else{
          this.$message.warning("查询到"+res.data.total+"个回收站信息")
        }
        console.log(res.data)
        // console.log('删除的文件id:' + this.deleteFileIds)
      })
      this.recycleVis = true
    },
    load() {
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name:this.name,
          role: this.user.role
        }
      }).then(res => {
        console.log(res.data.records)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleFileUploadSuccess(){
      this.$message.success("文件上传成功")
      console.log("文件上传成功")
      this.load()
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/file/" + id).then(res => {
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
      this.request.post("/file/del/batch", ids).then(res => {
        if (res.code === "200") {
          // res.records = ""
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
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
    download(row){
      if(row.enable){
        this.request.post("/userFile/postDownload/" + this.user.id, row.id).then(res => {
          if (res.code === '200') {
            console.log('开始下载')
            window.open(row.url)
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.$message.error("文件禁止下载")
      }
    },
    // preview(url) {
    //   window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
    // },
    checkDownloader(row){
      this.currentFile = row
      this.request.get("/userFile/checkDownload/" + row.id).then(res => {
        this.downloader = res.data
        console.log(res.data)
        this.downloaderVis = true
      })
    },
    changeEnable(row){
      this.request.post("file/update", row).then(res =>{
        if(res.code === "200"){
          if(row.enable === false){
            this.$message.error("文件不可下载更新成功")
          } else {
            this.$message.success("文件可下载更新成功")
          }
        }
      })
    },
    load_search(){
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          role: this.user.role
        }
      }).then(res => {
        console.log(res.data)
        if(res.data.total === 1){
          this.$message.warning("仅查询到"+res.data.total+"个相关文件")
        } else if(res.data.total === 0){
          this.$message.error("未查询到相关文件")
        } else{
          this.$message.success("查询到"+res.data.total+"个相关文件")
        }
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.name = ""
      this.$message.success("已重置")
      this.load()
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    }
  }
}
</script>

<style scoped>

</style>
<style lang="scss" scoped>
.content{
  width: 100%;
  height: 15px;
  background: #eeeeee;
}

</style>