<template>
  <div>
    <div style="padding-bottom: 20px; color: teal" >
      <b>欢迎你！{{ user.nickname }}</b>
    </div>
<!--    <div>-->
<!--&lt;!&ndash;      <el-button @click="enterGame">进入游戏</el-button>&ndash;&gt;-->
<!--      <a href="https://www.4399.com/" v-if="user.role === 'STUDENT_CLINICAL' || user.role === 'STUDENT_CHINESE'"><input type="button" value='进入游戏'></a>-->
<!--    </div>-->
    <el-row :gutter="10">
      <el-col :span="6" style="margin-bottom: 40px">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-s-custom"></i>用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold;font-size: 25px">
            <el-tag type="primary" style="font-size: 25px">{{ sum_per }}</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div><i class="el-icon-bank-card"></i>销售总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 25px">
            {{ sum_per*20+5 }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-money"></i>收益额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 25px">
            ${{ sum_per*500+600 }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div><i class="el-icon-s-shop"></i>门店总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 25px">
            3
          </div>
        </el-card>
      </el-col>

    </el-row>
    <el-row>
      <!--      页面总横距离为24     -->
      <el-col :span="10">
        <div id="main" style="width: 400px; height: 500px"></div>
      </el-col>
      <el-col :span="14">
        <div id="pie" style="width: 800px; height: 450px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: "Home",
  data(){
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      date:new Date(),
      // items: [
      //   {
      //     name: '用户总数',
      //     icon: 'el-icon-s-custom'
      //   }
      //     ,'销售总量','收益额'
      // ]
      sum_per: 0
    }
  },
  methods:{
    enterGame(){
      this.$router.replace({path:'https://www.4399.com/'})
    }
  },
  mounted(){ //等页面元素渲染后再触发 -> created为直接触发
    var option = {
      title: {
        text: '各季度新增用户',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '实际用户数',
          data: [],
          type: 'line'
        },
        {
          name: '实际用户数',
          data: [],
          type: 'bar'
        },
        {
          name: '预期目标',
          data: [],
          type: 'bar'
        },
        {
          name: '预期目标',
          data: [],
          type: 'line'
        }
      ]
    };

// 饼图

    var pieOption = {
      // backgroundColor: '#2c343c',
      title: {
        text: '各季度会员数量统计',
        subtext: '比例图',
        left: 'center',
        top: 20,
        textStyle: {
          color: '#334455'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a}<br/>{b}: {c} {{d}%}'
      },
      visualMap: {
        show: false,
        min: 80,
        max: 600,
        inRange: {
          colorLightness: [0.3, 0.1]
        }
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      toolbox: {
        show: true,
        feature: {
          mark: {show: true},
          dataView: {show: true, readOnly: false},
          restore: {show: true},
          saveAsImage: {show: true}
        }
      },
      series: [
        {
          name: '实际用户数',
          center: ['25%', '50%'],
          type: 'pie',
          radius: [40, 140],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'outer', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#334455"
              },
              formatter:'{d}%'
            }
          },
          selectedMode: 'single',
          selectedOffset: 30,
          data: [].sort(function (a, b) { return a.value - b.value; }),
          // roseType: 'radius',  // 填空
          roseType: 'area',
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: '#D8BFD8'
            },
            label: {
              show: true
            }
          }
        },
        {
          name: '预期目标',
          type: 'pie',
          center: ['75%', '50%'],
          radius: [20, 110],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{c}/ {d}%'
            }
          },
          selectedMode: 'single',
          selectedOffset: 30,
          roseType: 'radius',
          data: [
            {name: "第一季度", value: 15}, //, itemStyle:{color: '#91cc75', shadowColor: '#91cc75', borderType: 'dashed', opacity: 0.5}
            {name: "第二季度", value: 16},
            {name: "第三季度", value: 27},
            {name: "第四季度", value: 36},
          ].sort(function (a, b) { return a.value - b.value; }),  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: '#D8BFD8'
            },
          }
        }
      ]
    };

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res => {

      pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]},
      ]
      pieChart.setOption(pieOption)
      this.sum_per=res.data[0]+res.data[1]+res.data[2]+res.data[3];
    })

    //以下为柱状图数据
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    this.request.get("/echarts/members").then(res => {
      // 填空


      option.series[0].data = res.data //line
      option.series[1].data = res.data //bar

      option.series[2].data = [15,16,27,36] //bar
      option.series[3].data = [15,16,27,36] //line

      // 数据准备完毕之后再set
      myChart.setOption(option);
    })
  },
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>