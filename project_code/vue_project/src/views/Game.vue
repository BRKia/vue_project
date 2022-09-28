<template>
  <div>
    <div style="margin: 15px 0">
      <el-input style="width: 200px" placeholder="请输入游戏名称" suffix-icon="el-icon-search" v-model="name" @keyup.enter.native="load_search"></el-input>
      <el-button class="ml-5" type="primary" @click="load_search">搜索</el-button>
<!--      <i class="el-icon-thumb"></i>-->
      <el-button type="warning" @click="reset">重置   <i class="el-icon-refresh"></i></el-button>
    </div>
    <div style="margin: 10px 0">
      <el-upload action="http://124.70.63.245:9090/game/upload" :show-game-list="true" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传游戏 <i class="el-icon-cloudy"></i></el-button>
      </el-upload>
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
      <el-table-column prop="name" label="游戏名称" align="center"></el-table-column>
      <el-table-column label="开启" align="center">
        <template slot-scope="scope" >
          <el-button type="primary" @click="goGame(scope.row.url)" >开启</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#ccc" inactive-color="#13ce66" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
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
      <el-table-column label="预览">
        <div class="wrap">
          <template slot-scope="scope" v-if="scope.row.type === 'png'">
            <div class="content" @click="showImgViewer"></div>
            <el-image-viewer
                v-if="imgViewerVisible"
                :on-close="closeImgViewer"
                :url-list="imgList" />
          </template>
        </div>
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
      enableDownload: localStorage.getItem("enableDownload")?JSON.parse(localStorage.getItem("enableDownload")) : {},
      user : localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {},
      imgViewerVisible:false,
      imgList:[
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg',
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg',
      ]
    }
  },
  created() {
    this.load()
    this.enableDownload = true
  },
  components:{
    'el-image-viewer': () => import('element-ui/packages/image/src/image-viewer')
  },
  methods:{
    goGame(url){
      this.$router.push({name: url})
    },
    showImgViewer(){
      this.imgViewerVisible = true;
      const m = (e) => { e.preventDefault() };
      document.body.style.overflow = 'hidden';
      document.addEventListener("touchmove", m, false); // 禁止页面滑动
    },
    closeImgViewer(){
      this.imgViewerVisible = false;
      const m = (e) => { e.preventDefault() };
      document.body.style.overflow = 'auto';
      document.removeEventListener("touchmove", m, true);
    },
    load() {
      console.log(this.enableDownload)
      this.request.get("/game/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        console.log(res.data)
        // this.$message.success("查询到"+res.data.total+"个人员信息")
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
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/game/" + id).then(res => {
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
      this.request.post("/game/del/batch", ids).then(res => {
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
    download(url){
      if(this.enableDownload){
        window.open(url)
      }else{
        this.$message.error('禁止下载')
      }
    },
    changeEnable(row){
      this.enableDownload = !this.enableDownload
      console.log(this.enableDownload)
      this.request.post("game/update", row).then(res =>{
        if(res.code === "200"){
          localStorage.setItem("enableDownload", JSON.stringify(res.data))
          this.$message.success("更新成功")
        }
      })
    },
    load_search(){
      this.request.get("/game/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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