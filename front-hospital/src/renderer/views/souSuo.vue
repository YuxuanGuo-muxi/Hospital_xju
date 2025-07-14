<script lang='ts' setup>
import { onMounted, ref } from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';
import type { TagProps } from 'element-plus'
//挂载函数
onMounted(async () => {
  // alert('mounted');
  //获取标签
  let jsonLabelList = await dataLabelLoad();
  if (jsonLabelList !== null) {
    jsonDataLabel.value = jsonLabelList;
  }
  let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
  if (jsonPatientList !== null) {
    jsonDataPatient.value = jsonPatientList;
  }
  searchButtonListeners();
  filterSearchListeners();
});
//过滤
let jsonDataSearch = ref({
  "tags": [],
  "tagSearch": {isDeleted:'0'},
  "pageIndex": 1,
  "pageSize": 6,
  "sortMode": ""
});
//页码
let pageIndex = jsonDataSearch.value.pageIndex;
//标签列表
let jsonDataLabel = ref({});
//患者列表
let jsonDataPatient = ref({
  patientList: []
});
//翻页函数
const first = async () => {
  if (jsonDataSearch.value.pageIndex == 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    // alert("已经是第一页了！");
    return;
  }
  jsonDataSearch.value.pageIndex -= 1;
  pageIndex -= 1;
  console.log(jsonDataSearch.value);
  let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
  creatPatientList(jsonPatientList);
};
const third = async () => {
  jsonDataSearch.value.pageIndex += 1;
  pageIndex += 1;
  console.log(jsonDataSearch.value);
  let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
  if (jsonPatientList.patientList.length === 0) {
    jsonDataSearch.value.pageIndex -= 1;
    pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    // alert("已经是最后一页了！");
    return;
  }
  creatPatientList(jsonPatientList);
}
//请求标签列表
const dataLabelLoad = async () => {
  //向后台请求标签数量前五的数据
  try {
    const response = await axios.get('/api/tag/getTags');
    if (response.data.result === true) {
      let jsonLabelList = {
        "tags": []
      };
      response.data.data.forEach(function (label) {
        jsonLabelList.tags.push(label);
      });
      console.log(jsonLabelList);
      return jsonLabelList;

    } else {
      ElMessage({
        type: 'error',
        message: '标签列表请求失败！'
      });
      // alert("标签列表请求失败！");
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
    return null;
  }
};
//标签选择事件
const checkBoxChange = async (tagName, isChecked) => {
  if (isChecked) {
    if (jsonDataSearch.value.tags.includes(tagName) === false) {
      jsonDataSearch.value.tags.push(tagName);
    }
  } else {
    if (jsonDataSearch.value.tags.includes(tagName) === true) {
      jsonDataSearch.value.tags.splice(jsonDataSearch.value.tags.indexOf(tagName), 1);
    }
  }
  console.log('checkBoxChange:', jsonDataSearch.value);
  let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
  console.log(jsonDataPatient.value);
  creatPatientList(jsonPatientList);
  console.log(jsonDataPatient.value);
};
//显示患者列表
const creatPatientList = (jsonPatientList) => {
  jsonDataPatient.value = jsonPatientList;
  console.log('jsonDataPatient:', jsonDataPatient.value);
};
//请求患者列表
const dataPatientLoad = async (query) => {
  try {
    console.log('query:', query);
    const response = await axios.post('/api/report/search', query);
    if (response.data.result === true) {
      console.log("response:", response.data.data);
      let jsonPatientList = {
        "patientList": []
      }
      response.data.data.forEach(function (patient) {
        if (patient.tagSet !== '') {
          let tagList = patient.tagSet.split(",");
          // 选取第一个到倒数第二个标签
          patient.tagList = tagList.slice(0, tagList.length - 1);
        }
        jsonPatientList.patientList.push(patient);
        patient.noduleList.forEach(function (nodule) {
          nodule.imageList = nodule.image.split(";").slice(0,-1);
        });
      });
      console.log('jsonPatientList:', jsonPatientList);
      return jsonPatientList;
    } else {
      ElMessage({
        type: 'error',
        message: '患者列表请求失败！'
      });
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    return null;
  }
};
//搜索按钮事件绑定
const searchButtonListeners = () => {
  const searchButton = document.getElementById('searchButton');
  searchButton.addEventListener('click', async function () {
    // console.log('optionsType:', optionsType);
    // console.log('valueType:', valueType.value);
    // console.log('optionsContain:', optionsContain[valueType.value]);
    console.log('jsonDataSearch:', jsonDataSearch.value);
    if (Object.keys(jsonDataSearch.value.tagSearch).length === 1) {
      ElMessage({
        type: 'warning',
        message: '请输入搜索内容！'
      });
      // alert("请输入搜索内容！");
    } else {
      // const query = {
      //   tagList: jsonDataSearch.value.tagList,
      //   patientName: jsonDataSearch.value.patientName,
      //   pageIndex: jsonDataSearch.value.pageIndex,
      //   pageSize: jsonDataSearch.value.pageSize,
      //   sortMode: jsonDataSearch.value.sortMode
      // }
      console.log(jsonDataSearch.value);
      let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
      creatPatientList(jsonPatientList);
      console.log('jsonTagSearch:',jsonTagSearch.value);
    }
  });
};
//监听过滤器
const filterSearchListeners = () => {
  const sortModeForm = document.getElementById("sortModeForm");
  sortModeForm.querySelectorAll('input[type="radio"]').forEach(function (sortMode) {
    sortMode.addEventListener('change', async function () {
      if (this.checked) {
        // 单选按钮被选中时执行的操作
        console.log(this.value);
        if (this.value === 'asc') {
          jsonDataSearch.value.sortMode = 1;
        } else if (this.value === 'desc') {
          jsonDataSearch.value.sortMode = 2;
        } else {
          jsonDataSearch.value.sortMode = null;
        }
        console.log(jsonDataSearch.value);
        let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
        creatPatientList(jsonPatientList);
      }
    });
  });
};
// 点击空白处取消图片预览
const handleClickItem = () => {
  // 获取遮罩层dom
  setTimeout(() => {
    const domImageMask = document.querySelector(".el-image-viewer__wrapper");
    if (!domImageMask) {
      return;
    }

    // 添加点击事件监听器
    domImageMask.addEventListener("click", (e: MouseEvent) => {
      const target = e.target as HTMLElement;

      // 检查点击的是否是底部菜单或底部菜单的子元素
      if (target.closest('.el-image-viewer__actions__inner') || target.closest('.el-image-viewer__next') || target.closest('.el-image-viewer__prev')) {
        return;  // 如果点击的是底部菜单，不关闭
      }

      // 点击遮罩层时调用关闭按钮的 click 事件
      const closeButton = document.querySelector(".el-image-viewer__close") as HTMLElement;
      if (closeButton) {
        closeButton.click();
      }
    });
  }, 300);
}
//搜索  
const select = ref('');
let valueContain = ref('');
const valueType = ref('');
const optionsContain = {
  tcm: [{
    value: '肺脾气虚',
    label: '肺脾气虚'
  },
  {
    value: '肝郁气滞',
    label: '肝郁气滞'
  },
  {
    value: '痰浊壅肺',
    label: '痰浊壅肺'
  },
  {
    value: '寒凝阻络',
    label: '寒凝阻络'
  },
  {
    value: '肝肾亏虚',
    label: '肝肾亏虚'
  }],
  representation: [{
    value: '分叶',
    label: '分叶'
  },{
    value: '毛刺',
    label: '毛刺'
  },{
    value: '气管征',
    label: '气管征'
  },{
    value: '血管征',
    label: '血管征'
  },{
    value: '空泡征',
    label: '空泡征'
  },{
    value: '钙化',
    label: '钙化'
  },{
    value: '胸膜牵拉',
    label: '胸膜牵拉'
  },{
    value: '胸膜凹陷征',
    label: '胸膜凹陷征'
  },{
    value: '无表征',
    label: '无表征'
  }],
  patientBirthday: [{
    value: '请输入格式为yyyy-mm-dd的日期',
    label: '请输入格式为yyyy-mm-dd的日期',
    disabled: true,
  }],
  patientGender: [{
    value: '男',
    label: '男'
  },
  {
    value: '女',
    label: '女'
  }],
  patientAge: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  checkTime: [{
    value: '请输入格式为yyyy-mm-dd的日期',
    label: '请输入格式为yyyy-mm-dd的日期',
    disabled: true,
  }],
  result: [{
    value: '良性',
    label: '良性'
  },
  {
    value: '恶性',
    label: '恶性'
  }],
  riskLevel: [{
    value: '低危',
    label: '低危'
  },
  {
    value: '中危',
    label: '中危'
  },
  {
    value: '高危',
    label: '高危'
  }],
  ctr: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  malignancyRisk: [{
    value:  '实性',
    label: '实性'
  },
  {
    value: '磨玻璃',
    label: '磨玻璃'
  },{
    value: '混合性结节',
    label: '混合性结节'
  }],
  isNext: [{
    value: '是',
    label: '是'
  },
  {
    value: '否',
    label: '否'
  }],
  noduleProbability: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  diagnoseTime: [{
    value: '请输入格式为yyyy-mm-dd的日期',
    label: '请输入格式为yyyy-mm-dd的日期',
    disabled: true,
  }],
  centerDensity: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  densityAverage: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  densityMax: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  densityMin: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  malignancy: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  size: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  volume: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  diameter: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  wetDegree: [{
    value: '请输入数字',
    label: '请输入数字',
    disabled: true,
  }],
  nextTime: [{
    value: '请输入格式为yyyy-mm-dd的日期',
    label: '请输入格式为yyyy-mm-dd的日期',
    disabled: true,
  }]
}
const optionsType = [
  {
    label: 'CT基本信息表',
    options: [{
      value: 'patientId',
      label: '身份证号',
    },{
      value: 'patientName',
      label: '患者姓名',
    },{
      value: 'patientBirthday',
      label: '出生日期',
    },{
      value: 'patientAge',
      label: '患者年龄',
    },{
      value: 'checkTime',
      label: '随访检查时间',
    },{
      value: 'doctorName',
      label: '医生姓名',
    },{
      value: 'patientGender',
      label: '患者性别',
    },{
      value: 'diagnoseTime',
      label: '诊断时间',
    }]
  },
  {
    label: '结节图像信息表',
    options: [{
      value: 'result',
      label: '肺结节病理结果',
    },
    {
      value: 'riskLevel',
      label: '肺结节危险分类',
    },
    {
      value: 'ctr',
      label: '肺结节实性比例'
    },
    {
      value: 'malignancyRisk',
      label: '肺结节性质'
    },
    {
      value: 'representation',
      label: '肺结节表征'
    },
    {
      value: 'tcm',
      label: '中医辩证分型',
    },
    {
      value: 'calcification',
      label: '钙化情况',
    },
    {
      value: 'centerDensity',
      label: '中心密度',
    },
    {
      value: 'densityAverage',
      label: '平均密度',
    },
    {
      value: 'densityMax',
      label: '最大密度',
    },
    {
      value: 'densityMin',
      label: '最小密度',
    },{
      value: 'layer',
      label: '影像层'
    },{
      value: 'malignancy',
      label: '肺结节恶性概率'
    },{
      value: 'lung',
      label: '肺'
    },{
      value: 'lungLobe',
      label: '肺叶'
    },{
      value: 'lungSegment',
      label: '肺段'
    },{
      value: 'size',
      label: '肺结节大小'
    },{
      value: 'volume',
      label: '肺结节体积'
    },{
      value: 'diameter',
      label: '肺结节标准直径'
    },{
      value: 'shape',
      label: '肺结节形态类型'
    },{
      value: 'wetDegree',
      label: '肺结节浸润程度'
    },{
      value: 'noduleProbability',
      label: '肺结节概率'
    },{
      value: 'suggestion',
      label: '肺结节影像建议'
    },{
      value: 'postOpDiagnosis',
      label: '肺结节术后诊断建议'
    },{
      value: 'remark',
      label: '备注'
    }]
  },{
    label: '其他信息表',
    options: [{
      value: 'isNext',
      label: '是否需要复查'
    },{
      value: 'nextTime',
      label: '复查时间',
    }]
  }
]
//中英映射表
const en2zh = {
  'calcification': '钙化情况',
  'centerDensity': '中心密度',
  'densityAverage': '平均密度',
  'densityMax': '最大密度',
  'densityMin': '最小密度',
  'representation': '肺结节表征',
  'tcm': '中医辩证分型',
  'patientName': '患者姓名',
  'patientId': '身份证号',
  'patientBirthday': '出生日期',
  'patientAge': '患者年龄',
  'checkTime': '随访检查时间',
  'doctorName': '医生姓名',
  'patientGender': '患者性别',
  'diagnoseTime': '诊断时间',
  'nextTime': '复查时间',
  'result': '肺结节病理结果',
  'riskLevel': '肺结节危险分类',
  'ctr': '肺结节实性比例',
  'malignancyRisk': '肺结节性质',
  'layer': '影像层',
  'malignancy': '肺结节恶性概率',
  'lung': '肺',
  'lungLobe': '肺叶',
  'lungSegment': '肺段',
  'size': '肺结节大小',
  'volume': '肺结节体积',
  'diameter': '肺结节标准直径',
  'shape': '肺结节形态类型',
  'wetDegree': '肺结节浸润程度',
  'noduleProbability': '肺结节概率',
  'suggestion': '肺结节影像建议',
  'postOpDiagnosis': '肺结节术后诊断建议',
  'remark': '备注',
  'isNext': '是否需要复查'
}
//搜索标签
interface TagsItem {
  name: string
  type: TagProps['type']
}

const tagSearch = ref<TagsItem[]>([])
const jsonTagSearch = ref({})
const handleClose = async (tag) => {
  tagSearch.value.splice(tagSearch.value.indexOf(tag), 1);
  delete jsonTagSearch.value[tag.type];
  delete jsonDataSearch.value.tagSearch[tag.type];
  let jsonPatientList = await dataPatientLoad(jsonDataSearch.value);
  creatPatientList(jsonPatientList);
}
//增加搜索标签
const addTag = () => {
  let value = valueContain.value;
  if (valueContain.value === '') {
    ElMessage({
      type: 'warning',
      message: '请输入搜索内容！'
    });
    return;
  } else {
    let flag = true;
    console.log('tagSearch:',tagSearch.value);
    tagSearch.value.forEach(item => {
      if (item.type === valueType.value){
        ElMessage({
          type: 'warning',
          message: '搜索属性已存在！'
        });
        flag = false;
      }
    });
    if (valueType.value === 'patientBirthday' || valueType.value === 'checkTime' || valueType.value === 'nextTime' || valueType.value === 'diagnoseTime'){
      // alert('请输入格式为yyyy-mm-dd的日期！');
      if (/^\d{4}-\d{2}-\d{2}$/.test(valueContain.value) === false){
          ElMessage({
            type: 'warning',
            message: '请输入格式为yyyy-mm-dd的日期！'
          });
          flag = false;
      }
      value = new Date(value);
    }
    if (valueType.value === 'patientAge' || valueType.value === 'centerDensity' || valueType.value === 'densityAverage' || valueType.value === 'densityMax' || valueType.value === 'densityMin' || valueType.value === 'ctr' || valueType.value === 'noduleProbability' || valueType.value === 'volume' || valueType.value === 'diameter' || valueType.value === 'wetDegree' || valueType.value ==='size' || valueType.value ==='malignancy'){
      if (/^\d+$/.test(valueContain.value) === false && /^\d+\.\d+$/.test(valueContain.value) === false){
          ElMessage({
            type: 'warning',
            message: '请输入数字！'
          });
          flag = false;
      }
    }
    if (flag) {
      tagSearch.value.push({
        representation: en2zh[valueType.value],
        type: valueType.value,
        name: valueContain.value,
        status: 'success'
      });
      jsonTagSearch.value[valueType.value] = value;
      jsonDataSearch.value.tagSearch[valueType.value] = value;
    }else {
      return;
    }
  }
}
//检查是否可输入
const checkType = () => {
  alert('请输入搜索内容！');
  if (optionsContain[valueType.value] === undefined){
    ElMessage({
      type: 'warning',
      message: '该属性暂不支持搜索！'
   });
  }
}
</script>

<template>
  <div class="o-scroll">

    <!-- Back To Top Start -->
    <a href="#main-wrapper" id="backto-top" class="back-to-top"></a>
    <!-- Main Wrapper Start -->
    <div id="main-wrapper" class="main-wrapper overflow-hidden">

      <!-- Header Area Start -->
      <Header />
      <!-- Main Content Start -->
      <div class="page-content m-0">
        <h6 style="padding: 0px 110px;">>随访信息检索</h6>
        <!-- Booking Area Start -->
        <section class="booking mb-8">
          <div class="container-fluid">
            <div class="content" style="padding: 0px 92px;">
              <div class="row align-items-center">
                <div class="col-sm-2">
                  <button type="submit" class="cus-btn small-pad" id="searchButton">搜索
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewbox="0 0 32 32" fill="none">
                      <g clip-path="url(#clip0_502_1331)">
                        <circle cx="12" cy="12" r="10" fill="none" stroke="white"></circle>
                      </g>
                      <defs>
                        <clippath id="clip0_502_1351">
                          <rect width="32" height="32" fill="black"></rect>
                        </clippath>
                      </defs>
                    </svg>
                  </button>
                </div>
                <div class="col-sm-10 d-flex" style="padding: 10px">
                  <div>
                    <el-select
                      v-model="valueType"
                      collapse-tags
                      collapse-tags-tooltip
                      :max-collapse-tags="1"
                      placeholder="选择搜索属性"
                      style="width: 200px;
                      --el-select-input-focus-border-color: #09c41c;"
                    >
                      <el-option-group
                      v-for="group in optionsType"
                      :key="group.label"
                      :label="group.label"
                      >
                        <el-option
                          v-for="item in group.options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-option-group>
                    </el-select>
                  </div>
                  <div>
                    <el-select
                      v-model="valueContain"
                      filterable
                      allow-create
                      default-first-option
                      clearable
                      :reserve-keyword="false"
                      no-data-text="没有匹配的选项，请输入搜索内容"
                      placeholder="输入搜索内容"
                      style="width: 350px; margin-right: 5px;
                      --el-select-input-focus-border-color: #09c41c;
                      --el-menu-active-color: #09c41c;"
                    >
                      <el-option
                        v-for="item in optionsContain[valueType]"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                        :disabled="item.disabled" 
                      />
                    </el-select>
                  </div>
                  <div>
                    <el-button type="success" plain @click="addTag">+新增</el-button>
                  </div>
                  <p class="dark-gray" style="align-items: center; display: flex; font-size: 14px;">(请按回车确定搜索内容,并点击新增按钮后,再进行搜索!)</p>
                  <!-- <el-input v-model="jsonDataSearch.patientName" style="max-width: 600px" placeholder="请输入相关信息"
                    class="input-with-select">
                    <template #prepend>
                      <el-select v-model="select" placeholder="Select" style="width: 115px">
                        <el-option label="Restaurant" value="1" />
                        <el-option label="Order No." value="2" />
                        <el-option label="Tel" value="3" />
                      </el-select>
                    </template>
                  </el-input>
                  <div class="slector-wrapper"></div> -->
                  <!-- <div class="booking-info promo-code mb-sm-0 mb-16">
                    <div class="custom-sel-input-block m-0 col-sm-12">
                      <input type="text" class="sel-input p-0" id="searchText" placeholder="请输入相关信息">
                      <div class="slector-wrapper"></div>
                    </div>
                  </div> -->
                </div>
                <div class="col-sm-12" style="padding: 10px">
                  <el-tag v-for="tag in tagSearch" :key="tag.name" closable :type="tag.status" 
                  @close="handleClose(tag)">
                    {{ tag.representation + ':' + tag.name }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Flight Listing Start -->
        <section class="flight-listing-page mb-60">
          <div class="container-fluid">
            <div class="row">
              <div class="col-xl-3 col-lg-4 mb-xl-0 mb-32">
                <div class="sidebar bg-white br-10 light-shadow p-2">
                  <div class="sidebar-title">
                    <h4 class="lightest-black">过滤搜索</h4>
                  </div>
                  <div class="filter-block p-24">
                    <!-- <h4 class="color-black mb-12">分类筛选</h4>
                    <el-cascader style="width: 100%; margin-bottom: 10px;" placeholder="Try searchingL Guide"
                      :options="options" :props="props" clearable filterable /> -->
                    <h4 class="color-black mb-12">排序方式</h4>
                    <div class="content-block mb-24">
                      <form class="radio-tile-group sidebar" id="sortModeForm">
                        <div class="input-container">
                          <input id="asc" class="radio-button" type="radio" name="sortMode" value="asc">
                          <div class="radio-tile sidebar-radio">
                            <label for="asc" class="radio-tile-label sidebar-label">复查时间顺序</label>
                          </div>
                        </div>
                        <div class="input-container">
                          <input id="desc" class="radio-button" type="radio" name="sortMode" value="desc">
                          <div class="radio-tile sidebar-radio">
                            <label for="desc" class="radio-tile-label sidebar-label">复查时间倒序</label>
                          </div>
                        </div>
                      </form>
                    </div>
                    <hr class="custom-hr">
                  </div>
                  <div class="filter-block plr-24">
                    <h4 class="color-black fw-500 mb-12">标签</h4>
                    <div class="content-block">
                      <div class="custom-control" id="tagCheckbox">
                        <div v-for="tag in jsonDataLabel.tags" :key="tag.tagId" v-if="jsonDataLabel !== null"
                          class="d-flex justify-content-start align-items-center mb-24">
                          <div class="checkbox-button">
                            <input type="checkbox" :id="'tag' + tag.tagId" :name="tag.tagName"
                              class="custom-control-input" style="transform: scale(1.3); margin-right: 10px;"
                              @change="checkBoxChange(tag.tagName, $event.target.checked)">
                          </div>
                          <label :for="'tag' + tag.tagId" class="custom-control-label lightest-black col-sm-10"
                            style="font-size: 25px;">{{ tag.tagName }}</label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xl-9 col-lg-8">
                <div class="row" id="divPatientList">
                  <div v-for="patient in jsonDataPatient.patientList"
                    v-if="jsonDataPatient !== null" class="col-sm-12">
                    <div class="flight-block bg-white light-shadow p-24 br-10 mb-16">
                      <div class="flight-area">
                        <div class="airline-name col-sm-4">
                          <div>
                            <h5 class="lightest-black mb-8">{{ patient.patientName }}(姓名)
                            </h5>
                            <h6 class="dark-gray">{{ patient.patientId }}(身份证号)</h6>
                          </div>
                        </div>
                        <div class="flight-detail col-sm-5">
                          <div class="flight-departure d-flex">
                            <h5 class="dark-gray">标签:</h5>
                            <h5 class="dark-gray" v-for="tag in patient.tagList" :key="tag">
                              <{{ tag }}>
                            </h5>
                          </div>
                        </div>
                        <div class="flight-button col-sm-4">
                          <router-link :to="'/detailPatient?patientId=' + patient.patientId"
                            class="cus-btn btn-sec">查看病人其它具体信息</router-link>
                        </div>
                      </div>
                      <hr class="custom-hr">
                      <div class="d-flex justify-content-between align-items-center">
                        <h5 class="color-black" v-if="patient.nextTime !== null">{{ patient.nextTime }}(复查日期)</h5>
                        <h5 class="color-black" v-if="patient.nextTime === null">(不需要复查)</h5>
                        <div>
                          <a href="#" class="accordion-button color-primary h5 collapsed" data-bs-toggle="collapse"
                            :data-bs-target="'#Detail' + patient.reportId" aria-expanded="true">其它细节</a>
                        </div>
                      </div>
                    </div>
                    <div :id="'Detail' + patient.reportId" class="accordion-collapse collapse mb-12"
                    v-for="nodule in patient.noduleList" :key="nodule.noduleId">
                      <div class="row bg-white br-10 light-shadow p-24 m-0 align-items-center">
                        <div class="col-lg-12 col-sm-4">
                          <div class="time-detail">
                            <h6 class="flight-date mb-32">病理结果：{{ nodule.result }}</h6>
                          </div>
                        </div>
                        <div class="col-lg-12 mb-32">
                          <div class="detail-block" style="padding: 24px 24px;">
                            <div class="d-sm-flex d-block gap-24">
                              <div class="content row col-sm-12">
                                <div v-if="nodule.imageList.length > 0 && nodule.imageList[0] !== ''" class="col-sm-3">
                                  <h6>病理图片：（点击查看更多）</h6>
                                  <el-image style="width: 200px;" :src="nodule.imageList[0]" :zoom-rate="1.2"
                                    :max-scale="7" :min-scale="0.2" :preview-src-list="nodule.imageList"
                                    :initial-index="4" fit="cover" @click.stop="handleClickItem" />
                                </div>
                                <div v-if="nodule.imageList.length <= 0 || nodule.imageList[0] === ''" class="col-sm-3">
                                  <h4 class="dark-gray" style="margin-top: 50px;">无病理图片</h4>
                                </div>
                                <div class="col-sm-9">
                                  <h6>表征：{{ nodule.representation }}</h6>
                                  <h6>位置：{{ nodule.location }}</h6>
                                  <h6>性质：{{ nodule.nature }}</h6>
                                  <h6>中医分辨型：{{ nodule.tcm }}</h6>
                                  <h6>恶性概率：{{ nodule.malignancy }}</h6>
                                  <h6>影像建议：{{ nodule.suggestion }}</h6>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="paginations mt-8">
                  <ul class="unstyled" id="pageList">
                    <a class="h6 fw-600 mb-0" id="pageFirst" @click="first">
                      <li>&lt;</li>
                    </a>
                    <a class="h6 fw-600 mb-0" id="pageSecond">
                      <li>{{ pageIndex }}</li>
                    </a>
                    <a class="h6 fw-600 mb-0" id="pageThird" @click="third">
                      <li>&gt;</li>
                    </a>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </section>
        <!-- Flight Listing End -->
      </div>
      <!-- Main Content End -->

      <!-- Footer Start -->

      <Footer />
    </div>
  </div>
</template>

<style scoped>
@import "@/renderer/assets/css/replenish.css";
@import "@/renderer/assets/css/font-awesome.css";
@import "@/renderer/assets/css/style.css";
@import "@/renderer/assets/css/bootstrap.min.css";
@import "@/renderer/assets/css/slick.css";
@import "@/renderer/assets/css/slick-theme.css";
@import "@/renderer/assets/css/aksVideoPlayer.css";
@import "@/renderer/assets/css/datetime.css";
@import "@/renderer/assets/css/classic.css";
@import "@/renderer/assets/css/classic.date.css";
@import "@/renderer/assets/css/jquery.timepicker.min.css";
@import "@/renderer/assets/css/ui-autocomplete.css";
@import "@/renderer/assets/css/ionrangeslider.css";
@import "@/renderer/assets/css/sal.css";
@import "@/renderer/assets/css/app.css";
header {
  background: url(../assets/image/title-banner.png)
}
.input-with-select .el-input-group__prepend {
  background-color: var(--el-fill-color-blank);
}
.el-select-dropdown__item.is-selected {
  color: #09c41c;
  font-weight: bold;
}
.el-select__wrapper.is-focused {
  box-shadow: 0 0 0 1px #09c41c inset;
}
.accordion-collapse {
  transition: none !important; /* 移除过渡效果 */
  overflow: hidden; /* 确保内容不会溢出 */
}
</style>