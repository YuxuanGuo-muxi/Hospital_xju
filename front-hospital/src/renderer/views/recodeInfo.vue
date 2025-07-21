<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';
import type { UploadProps, UploadUserFile } from 'element-plus'
import { BASE_URL } from '@/renderer/utils/request'
import { ArrowDown } from '@element-plus/icons-vue'

const base_url = BASE_URL;

const handleRemove = (uploadFile) => {
  console.log(uploadFile.response);
  const imageList = CTImageForm.value.image.split(';').slice(0, -1);
  imageList.splice(uploadFile.response, 1);
  let image = '';
  imageList.forEach(function (item) {
    image += (item + ";")
  });
  console.log('image:',image);
  CTImageForm.value.image = image;
}

const handlePreview = (file) => {
  console.log(file);
}
const handleSuccess = (response) => {
  // 在上传成功后，response会返回上传成功的图片路径，将其赋值给CTImageForm.image
  if (noduleList.value[currentIndex.value] !== undefined){
    noduleList.value[currentIndex.value].image += (response + ";")
    console.log('CTImageList.value[currentIndex.value].image',noduleList.value[currentIndex.value].image);
  }else{
    CTImageForm.value.image += (response + ";")
    console.log('CTImageForm.value.image',CTImageForm.value.image);
  }
}
//当前时间
const currentTime = ref('');
//挂载函数
onMounted(async () => {
  jsonTagList.value = await dataTagList();
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  currentTime.value = `${year}-${month}-${day}`;
  console.log(currentTime.value);
  console.log(jsonTagList.value);
});
//提交表单
let recodeInfoForm = ref({});
//CTbaseMessageForm
let CTbaseMessageForm = ref({
  patientName: "",
  patientAge: "",
  patientBirthday: "",
  patientId: "",
  patientGender: "",
  checkTime: "",
  doctorName: "",
  diagnoseTime: "",
  checkItem: "CT",
  checkEquipment: "CT",
  noduleList: []
})
//CTImageForm
let CTImageForm = ref({
  result: "",
  riskLevel: "",
  ctr: "",
  malignancyRisk: "",
  representation: "",
  tcm: "",
  image: "",
  layer: "",
  lung: "",
  lungLobe: "",
  lungSegment: "",
  size: "",
  volume: "",
  diameter: "",
  shape: "",
  malignancy: "",
  wetDegree: "",
  noduleProbability: "",
  calcification: "",
  centerDensity: "",
  densityAverage: "",
  densityMax: "",
  densityMin: "",
  suggestion: "",
  postOpDiagnosis: "",
  remark: "",
});
//otherForm
let otherForm = ref({
  isNext: "是",
  nextTime: "",
  tagList: [],
  hospitalAddress: "乌鲁木齐市黄河路116号",
  postCode: "830000"
});
//标签列表
let jsonTagList = ref([]);
let imageList = ref([]);
//标签设定函数
const setTags = (tagName, checked) => {
  if (checked === true) {
    otherForm.value.tagList.push(tagName);
  } else {
    otherForm.value.tagList.splice(otherForm.value.tagList.indexOf(tagName), 1);
  }
  console.log(otherForm.value.tagList);
}
// 获取患者详情数据
const dataDetailPatient = async (patientId) => {
  try {
    const response = await axios.get("/api/patient/getById/" + patientId);
    if (response.data.result === true) {
      console.log(response.data.data);
      return response.data.data;
    } else {
      ElMessage({
        type: 'warning',
        message: '该患者没有录入基本信息！'
      });
      // alert("该患者没有录入基本信息！");
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("获取患者详情数据失败！");
    // 测试数据
    // let dataPatient = {
    //     "patientId": "000001",
    //     "doctorId": "000001",
    //     "drugName": "阿司匹林",
    //     "patientGender": "男",
    //     "isQuit": "是",
    //     "isSmoke": "有",
    //     "medicalHistory": "糖尿病",
    //     "patientAddress": "北京市海淀区",
    //     "patientAge": "30",
    //     "patientAllergy": "有",
    //     "patientEduLevel": "大专、本科及以上",
    //     "patientGroup": "汉族",
    //     "patientMalignant": "无",
    //     "patientMarriage": "未婚",
    //     "patientName": "张三",
    //     "patientPhone": "13812345678",
    //     "patientPregnancy": "无孕产",
    //     "patientWork": "矿工",
    //     "quitYears": "1",
    //     "source": "住院号",
    //     "smokingAmount": "4",
    //     "smokingType": "主动",
    //     "smokingYears": "10",
    //     "createTime": "2024-01-02",
    //     "tagList": [
    //         {
    //             "tagId": "000001",
    //             "tagName": "特别关注"
    //         },
    //         {
    //             "tagId": "000002",
    //             "tagName": "紧急"
    //         }
    //     ]
    // }
    // return dataPatient;
    return null;
  }
}
//获取设置的标签
const dataTagList = async () => {
  try {
    const response = await axios.post("/api/tag/getTags");
    if (response.data.result === true) {
      let tagList = [];
      response.data.data.forEach(function (tag) {
        tagList.push(tag)
      });
      return tagList;
    } else {
      ElMessage({
        type: 'error',
        message: '获取标签列表失败！'
      });
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
  }
}
// 避免二次提交flag
let submitFlag = false;
//提交
const submitFormMessage = async (event) => {
  event.preventDefault();
  if (submitFlag) {
    ElMessage({
      type: 'warning',
      message: '请勿重复提交！'
    });
    return;
  }
  saveCTImage(event);
  for (let key in CTbaseMessageForm.value) {
    recodeInfoForm.value[key] = CTbaseMessageForm.value[key];
  }
  if (noduleList.value.length === 0) {
    noduleList.value.push(CTImageForm.value);
  }
  recodeInfoForm.value.noduleList = noduleList.value;
    // for (let key in CTImageForm.value) {
  //   recodeInfoForm.value[key] = CTImageForm.value[key];
  // }
  for (let key in otherForm.value) {
    recodeInfoForm.value[key] = otherForm.value[key];
  }
  if (recodeInfoForm.value.nextTime === "" && otherForm.value.isNext === "是"){
    ElMessage({
      type: 'error',
      message: '提交失败，请填写复查时间！'
    });
    return;
  }
  console.log(recodeInfoForm.value);
  try {
    console.log(recodeInfoForm.value);
    const response = await axios.post("/api/report/add", recodeInfoForm.value);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '提交成功！'
      });
      // alert("提交成功！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '提交失败！'
    });
    return;
    // alert("提交失败！");
  }
  submitFlag = true;
  //在一定时间后
}
//展示form
let showForm = ref(false);
//patientId确定
const patientIdConfirm = async (event, patientId) => {
  event.preventDefault(); // 防止默认表单提交行为

  // 这里的逻辑保持不变
  if (patientId.trim() === "") {
    ElMessage({
      type: 'error',
      message: '请输入身份证号！'
    });
    // alert("请输入身份证号！");
    return;
  }
  try {
    const patientData = await dataDetailPatient(patientId);
    if (patientData === null) {
      return;
    }
    if (patientData.patientType !== "肺结节"){
      ElMessage({
        type: 'warning',
        message: '该患者不是肺结节患者，请修改患者类型！'
      });
      return;
    }
    if (patientData !== null) {
      CTbaseMessageForm.value.patientName = patientData.patientName;
      CTbaseMessageForm.value.patientAge = patientData.patientAge;
      CTbaseMessageForm.value.patientBirthday = patientData.patientBirthday;
      CTbaseMessageForm.value.patientGender = patientData.patientGender;
      CTbaseMessageForm.value.checkTime = currentTime.value;
      CTbaseMessageForm.value.diagnoseTime = currentTime.value;
      showForm.value = true;
    } else {
      console.log('patientData is null');
      showForm.value = false;
    }
    // 这里可以根据需要对患者数据进行处理或显示
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员1！'
    });
    // alert("请检查网络连接或联系管理员！");
  }
};

//结节图像菜单
let CTImageMenu = ref(1);
//结节图像列表
let noduleList = ref([]);
//当前结节图像
let currentIndex = ref(0);
//保存结节图像
const saveCTImage = (event) => {
  event.preventDefault();
  ElMessage({
    type: 'success',
    message: '已保存结节图像'
  })
  // console.log('index:',index);
  // console.log('CTImageList.value.length:',CTImageList.value.length);
  // console.log(CTImageList.value[index]);
  if (noduleList.value[currentIndex.value] !== undefined) {
    console.log(1);
  } else {
    console.log(2);
    noduleList.value.push(JSON.parse(JSON.stringify(CTImageForm.value)));
  }
  console.log('noduleList:', noduleList.value);
  CTImageForm.value = {
    result: "",
    riskLevel: "",
    ctr: "",
    malignancyRisk: "",
    representation: "",
    tcm: "",
    image: "",
    layer: "",
    lung: "",
    lungLobe: "",
    lungSegment: "",
    size: "",
    volume: "",
    diameter: "",
    shape: "",
    malignancy: "",
    wetDegree: "",
    noduleProbability: "",
    calcification: "",
    centerDensity: "",
    densityAverage: "",
    densityMax: "",
    densityMin: "",
    suggestion: "",
    postOpDiagnosis: "",
    remark: ""
  }
}
//下拉菜单选择事件
const handleCommand = (command: string) => {
  if (command === "新增") {
    saveCTImage(event);
    CTImageMenu.value += 1;
    currentIndex.value += 1;
    ElMessage({
      type: 'success',
      message: `${command}结节图像成功`
    })
  } else {
    currentIndex.value = Number(command);
    ElMessage({
      type: 'success',
      message: `已转到第${command + 1}张结节图像`
    })
  }
  // console.log('index:',CTImageMenu.value)
  console.log('currentIndex:', currentIndex.value)
  console.log('CTImageList:', noduleList.value);
}
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
      if (target.closest('.el-image-viewer__actions__inner')) {
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
//重置已上传图片
const reSetUpLoadImg = (event) => {
  event.preventDefault();
  ElMessage({
    type: "success",
    message: "已清空已上传图片"
  })
  noduleList.value[currentIndex.value].image = '';
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
      <!-- Header Area end -->
      <!-- Main Content Start -->
      <div class="page-content m-0">
        <h6 style="padding: 0px 110px;">>随访信息录入 (本人承诺患者以上资料仅用作研究，不传播、不泄露)</h6>
        <section class="flight-booking">
          <div class="container-fluid">
            <div class="row">
              <div class="col-xl-12">
                <div class="booking-form">
                  <form action="#" id="recodeInfoForm" method="post" role="form" class="contact-form form-wizard">
                    <div class="wizard-content overflow-visible mb-24">

                      <div class="col-sm-4 mb-12 d-flex align-items-center"
                        style="margin-top: 20px; margin-left: 40px;">
                        <h5 class="col-sm-3">身份证号：</h5>
                        <input type="text" class="form-control wizard-required col-sm-4" id="patientId" name="patientId"
                          placeholder="请输入身份证号并点击确定，以继续进行填写表格" v-model="CTbaseMessageForm.patientId">
                        <div class="wizard-form-error"></div>
                        <button class="cus-btn col-sm-2" style="margin-left: 10px;"
                          @click="patientIdConfirm($event, CTbaseMessageForm.patientId)">确定</button>
                      </div>
                      <div v-if="showForm">
                        <fieldset id="CTBaseForm" class="tab-pane show wizard-fieldset p-0">
                          <div class="detail-form mb-32">
                            <h4 class="black p-0 mb-12">CT基本信息表：</h4>
                            <div class="row ">
                              <div class="col-sm-3 mb-12">
                                <h5>姓名：</h5>
                                <input type="text" class="form-control wizard-required" id="patientName"
                                  name="patientName" placeholder="请输入……" v-model="CTbaseMessageForm.patientName">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>年龄(岁)：</h5>
                                <input type="text" class="form-control wizard-required" id="patientAge"
                                  name="patientAge" placeholder="请输入……" v-model="CTbaseMessageForm.patientAge">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>出生日期：</h5>
                                <input type="date" class="form-control wizard-required" id="patientBirthday"
                                  name="patientBirthday" v-model="CTbaseMessageForm.patientBirthday">
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-3">
                                <div class="tab-pane active" id="patientGender">
                                  <h5>性别：</h5>
                                  <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="patientGender" class="custom-control-input" id="man"
                                        value="男" v-model="CTbaseMessageForm.patientGender">
                                      <label class="custom-control-label" for="man">男</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="patientGender" class="custom-control-input" id="women"
                                        value="女" v-model="CTbaseMessageForm.patientGender">
                                      <label class="custom-control-label" for="women">女</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-3 mb-12">
                                <h5>检查日期：</h5>
                                <input type="date" class="form-control wizard-required" id="checkDate" name="checkDate"
                                  v-model="CTbaseMessageForm.checkTime">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>医生姓名：</h5>
                                <input type="text" class="form-control wizard-required" id="doctorName"
                                  name="doctorName" placeholder="请输入……" v-model="CTbaseMessageForm.doctorName">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>诊断日期：</h5>
                                <input type="date" class="form-control wizard-required" id="diagnosis" name="diagnosis"
                                  v-model="CTbaseMessageForm.diagnoseTime">
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-3 ">
                                <div class="tab-pane active" id="checkProject">
                                  <h5>检查项目：</h5>
                                  <div class="custom-control" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="checkProject" class="custom-control-input"
                                        id="CTcheckProject" value="CT" checked disabled>
                                      <label class="custom-control-label" for="CTcheckProject">CT</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-3">
                                <div class="tab-pane active" id="checkEquipment">
                                  <h5>检查设备：</h5>
                                  <div class="custom-control" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="checkEquipment" class="custom-control-input"
                                        id="CTcheckEquipment" value="CT" checked disabled>
                                      <label class="custom-control-label" for="CTcheckEquipment">CT</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                        <fieldset id="CTImageForm" class="tab-pane show wizard-fieldset p-0">
                          <div class="flex flex-wrap items-center mb-12">
                            <el-dropdown @command="handleCommand">
                              <el-button type="primary" class="cus-btn small-pad">
                                结节图像列表<el-icon class="el-icon--right"><arrow-down /></el-icon>
                              </el-button>
                              <template #dropdown>
                                <el-dropdown-menu style="padding: 10px; margin-bottom: 0px;
                                  --el-dropdown-menuItem-hover-fill: #dfede7;
                                  --el-dropdown-menuItem-hover-color: #09c41c;">
                                  <el-dropdown-item :command="index" v-for="(n, index) in CTImageMenu" :key="index">
                                    第{{ index + 1 }}张</el-dropdown-item>
                                  <el-dropdown-item command="新增">+新增</el-dropdown-item>
                                </el-dropdown-menu>
                              </template>
                            </el-dropdown>
                          </div>
                          <div class="detail-form mb-32" v-if="noduleList[currentIndex] !== undefined">
                            <div class="d-flex justify-content-end">
                              <button class="cus-btn small-pad" id="saveButton" style="margin-right: 20px;"
                                @click="saveCTImage">保存结节信息编辑</button>
                            </div>
                            <h4 class="black p-0 mb-12">第{{ currentIndex + 1 }}张CT结节图像信息表：</h4>
                            <div class="row ">
                              <div class="col-sm-3">
                                <div class="tab-pane active" id="result">
                                  <h5>病理结果：</h5>
                                  <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="result" class="custom-control-input" id="good"
                                        value="良性" v-model="noduleList[currentIndex].result">
                                      <label class="custom-control-label" for="good">良性</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="result" class="custom-control-input" id="bad" value="恶性"
                                        v-model="noduleList[currentIndex].result">
                                      <label class="custom-control-label" for="bad">恶性</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-6">
                                <div class="tab-pane active" id="riskLevel">
                                  <h5>危险分类：</h5>
                                  <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="lower"
                                        value="低危" v-model="noduleList[currentIndex].riskLevel">
                                      <label class="custom-control-label" for="lower">低危</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="middle"
                                        value="中危" v-model="noduleList[currentIndex].riskLevel">
                                      <label class="custom-control-label" for="middle">中危</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="high"
                                        value="高危" v-model="noduleList[currentIndex].riskLevel">
                                      <label class="custom-control-label" for="high">高危</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <div class="col-sm-3 mb-12">
                                <h5>实性比例（CTR）：</h5>
                                <input type="text" class="form-control wizard-required" id="ctr" name="ctr"
                                  placeholder="请输入……" v-model="noduleList[currentIndex].ctr">
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <h5>性质：</h5>
                              <div class="custom-control mb-12 col-sm-6" style="display: flex;margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="shiXing" value="实性"
                                    v-model="noduleList[currentIndex].malignancyRisk">
                                  <label class="custom-control-label" for="shiXing">实性</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="moBoLi" value="磨玻璃"
                                    v-model="noduleList[currentIndex].malignancyRisk">
                                  <label class="custom-control-label" for="moBoLi">磨玻璃</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="hunHeXingJieJie"
                                    value="混合性结节" v-model="noduleList[currentIndex].malignancyRisk">
                                  <label class="custom-control-label" for="hunHeXingJieJie">混合性结节</label>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <h5>表征：</h5>
                              <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="fenYe"
                                    value="分叶" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="fenYe">分叶</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="maoCi"
                                    value="毛刺" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="maoCi">毛刺</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="qiGuanZheng" value="气管征" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="qiGuanZheng">气管征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xueGuanZheng" value="血管征" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="xueGuanZheng">血管征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="kongPaoZheng" value="空泡征" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="kongPaoZheng">空泡征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="gaiHua"
                                    value="钙化" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="gaiHua">钙化</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xiongMoQianLa" value="胸膜牵拉" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="xiongMoQianLa">胸膜牵拉</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xiongMoOuXianZheng" value="胸膜凹陷征" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="xiongMoOuXianZheng">胸膜凹陷征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="wubiaozheng" value="无表征" v-model="noduleList[currentIndex].representation">
                                  <label class="custom-control-label" for="wubiaozheng">无表征</label>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12 mb-32">
                              <h5>中医辩证分型：</h5>
                              <div class="custom-control col-sm-9" style="display: flex; margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="feiPiQiXu"
                                    value="肺脾气虚" v-model="noduleList[currentIndex].tcm">
                                  <label class="custom-control-label" for="feiPiQiXu">肺脾气虚</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="ganYuQiXie"
                                    value="肝郁气滞" v-model="noduleList[currentIndex].tcm">
                                  <label class="custom-control-label" for="ganYuQiXie">肝郁气滞</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="tanZuoYongFei"
                                    value="痰浊壅肺" v-model="noduleList[currentIndex].tcm">
                                  <label class="custom-control-label" for="tanZuoYongFei">痰浊壅肺</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="hanNingZuLuo"
                                    value="寒凝阻络" v-model="noduleList[currentIndex].tcm">
                                  <label class="custom-control-label" for="hanNingZuLuo">寒凝阻络</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="ganShenKuiXu"
                                    value="肝肾亏虚" v-model="noduleList[currentIndex].tcm">
                                  <label class="custom-control-label" for="ganShenKuiXu">肝肾亏虚</label>
                                </div>
                              </div>
                            </div>
                            <h4 class="black p-0 mb-12">AI影像表现：</h4>
                            <div class="d-flex mb-12" v-if="noduleList[currentIndex].image !== '' ">
                              <button class="cus-btn small-pad" id="saveButton" style="margin-right: 20px;"
                                @click="reSetUpLoadImg">清空已上传图片</button>
                            </div>
                            <div class="row">
                              <div class="col-sm-7 mb-12 ">
                                <div class="image-box" style="height: 641px; overflow-y: auto;">
                                  <h5 class="mb-12" v-if="noduleList[currentIndex].image !== '' ">已上传的图片：</h5>
                                  <el-image  v-for="img in noduleList[currentIndex].image.split(';').slice(0, -1)"
                                      style="width: 100px; height: 100px; margin-bottom: 10px;"
                                      :src="img" :zoom-rate="1.2" :max-scale="7"
                                      :min-scale="0.2" :preview-src-list="[img]"
                                      :initial-index="4" fit="cover" 
                                      v-if="noduleList[currentIndex].image !== ''"
                                      @click.stop="handleClickItem"/>
                                  <h5 class="mb-12">上传图片：</h5>
                                  <el-upload class="upload-demo"
                                             :action="base_url + '/api/report/upload'" multiple accept="image/*"
                                             :on-preview="handlePreview" :on-remove="handleRemove" list-type="picture"
                                             :on-success="handleSuccess">
                                    <el-button type="primary" class="cus-btn">点击上传图片
                                    </el-button>
                                    <template #tip>
                                      <div class="el-upload__tip">
                                        图片大小不超过500kb
                                      </div>
                                    </template>
                                  </el-upload>
                                </div>
                              </div>
                              <div class="col-sm-2 mb-12">
                                <div class="row">
                                  <h5>影像层：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="imageLayer"
                                      name="imageLayer" placeholder="请输入……" v-model="noduleList[currentIndex].layer">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="locationLung"
                                      name="locationLung" placeholder="请输入……" v-model="noduleList[currentIndex].locationLung">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺叶：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="lungLobes"
                                      name="lungLobes" placeholder="请输入……" v-model="noduleList[currentIndex].lungLobes">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺段：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="lungSegment"
                                      name="lungSegment" placeholder="请输入……" v-model="noduleList[currentIndex].lungSegment">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>大小(mm*mm)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="size" name="size"
                                      placeholder="请输入……" v-model="noduleList[currentIndex].size">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>体积(mm³)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="volume" name="volume"
                                      placeholder="请输入……" v-model="noduleList[currentIndex].volume">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>标准直径(mm)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="diameter"
                                      name="diameter" placeholder="请输入……" v-model="noduleList[currentIndex].diameter">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>形态类型：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="shape" name="shape"
                                      placeholder="请输入……" v-model="noduleList[currentIndex].shape">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-2 mb-12">
                                <div class="row">
                                  <h5>恶性概率(%)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="maligancy"
                                      name="maligancy" placeholder="请输入……" v-model="noduleList[currentIndex].maligancy">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>浸润程度：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="wetting" name="wetting"
                                      placeholder="请输入……" v-model="noduleList[currentIndex].wetting">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>结节概率(%)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="noduleProbability"
                                      name="noduleProbability" placeholder="请输入……"
                                      v-model="noduleList[currentIndex].noduleProbability">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>钙化情况：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="calcification"
                                      name="calcification" placeholder="请输入……" v-model="noduleList[currentIndex].calcification">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>中心密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="centerDensity"
                                      name="centerDensity" placeholder="请输入……" v-model="noduleList[currentIndex].centerDensity">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>平均密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="averageDensity"
                                      name="averageDensity" placeholder="请输入……" v-model="noduleList[currentIndex].averageDensity">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>最大密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="maxDensity"
                                      name="maxDensity" placeholder="请输入……" v-model="noduleList[currentIndex].maxDensity">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>最小密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="minDensity"
                                      name="minDensity" placeholder="请输入……" v-model="noduleList[currentIndex].minDensity">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>影像建议（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="imageSuggestion" name="imageSuggestion"
                                placeholder="请输入……" v-model="noduleList[currentIndex].suggestion" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                                <!-- <input type="text" class="form-control wizard-required" id="imageSuggestion"
                                  name="imageSuggestion" placeholder="请输入……" v-model="noduleList[currentIndex].suggestion">
                                <div class="wizard-form-error"></div> -->
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>术后诊断建议（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="postOpDiagnosis" name="postOpDiagnosis"
                                placeholder="请输入……" v-model="noduleList[currentIndex].postOpDiagnosis" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>备注（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="remark" name="remark"
                                placeholder="请输入……" v-model="noduleList[currentIndex].remark" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                          </div>
                          <div class="detail-form mb-32" v-if="noduleList[currentIndex] === undefined">
                            <div class="d-flex justify-content-end">
                              <button class="cus-btn small-pad" id="saveButton" style="margin-right: 20px;"
                                @click="saveCTImage">保存结节信息编辑</button>
                            </div>
                            <h4 class="black p-0 mb-12">第{{ currentIndex + 1 }}张CT结节图像信息表：</h4>
                            <div class="row ">
                              <div class="col-sm-3">
                                <div class="tab-pane active" id="result">
                                  <h5>病理结果：</h5>
                                  <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="result" class="custom-control-input" id="good"
                                        value="良性" v-model="CTImageForm.result">
                                      <label class="custom-control-label" for="good">良性</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="result" class="custom-control-input" id="bad" value="恶性"
                                        v-model="CTImageForm.result">
                                      <label class="custom-control-label" for="bad">恶性</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-6">
                                <div class="tab-pane active" id="riskLevel">
                                  <h5>危险分类：</h5>
                                  <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="lower"
                                        value="低危" v-model="CTImageForm.riskLevel">
                                      <label class="custom-control-label" for="lower">低危</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="middle"
                                        value="中危" v-model="CTImageForm.riskLevel">
                                      <label class="custom-control-label" for="middle">中危</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="riskLevel" class="custom-control-input" id="high"
                                        value="高危" v-model="CTImageForm.riskLevel">
                                      <label class="custom-control-label" for="high">高危</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <div class="col-sm-3 mb-12">
                                <h5>实性比例（CTR）：</h5>
                                <input type="text" class="form-control wizard-required" id="ctr" name="ctr"
                                  placeholder="请输入……" v-model="CTImageForm.ctr">
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <h5>性质：</h5>
                              <div class="custom-control mb-12 col-sm-6" style="display: flex;margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="shiXing" value="实性"
                                    v-model="CTImageForm.malignancyRisk">
                                  <label class="custom-control-label" for="shiXing">实性</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="moBoLi" value="磨玻璃"
                                    v-model="CTImageForm.malignancyRisk">
                                  <label class="custom-control-label" for="moBoLi">磨玻璃</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="malignancyRisk" class="custom-control-input" id="hunHeXingJieJie"
                                    value="混合性结节" v-model="CTImageForm.malignancyRisk">
                                  <label class="custom-control-label" for="hunHeXingJieJie">混合性结节</label>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12">
                              <h5>表征：</h5>
                              <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="fenYe"
                                    value="分叶" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="fenYe">分叶</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="maoCi"
                                    value="毛刺" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="maoCi">毛刺</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="qiGuanZheng" value="气管征" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="qiGuanZheng">气管征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xueGuanZheng" value="血管征" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="xueGuanZheng">血管征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="kongPaoZheng" value="空泡征" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="kongPaoZheng">空泡征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input" id="gaiHua"
                                    value="钙化" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="gaiHua">钙化</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xiongMoQianLa" value="胸膜牵拉" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="xiongMoQianLa">胸膜牵拉</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="xiongMoOuXianZheng" value="胸膜凹陷征" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="xiongMoOuXianZheng">胸膜凹陷征</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="representation" class="custom-control-input"
                                    id="wubiaozheng" value="无表征" v-model="CTImageForm.representation">
                                  <label class="custom-control-label" for="wubiaozheng">无表征</label>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-12 mb-32">
                              <h5>中医辩证分型：</h5>
                              <div class="custom-control col-sm-9" style="display: flex; margin-top: 5px;">
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="feiPiQiXu"
                                    value="肺脾气虚" v-model="CTImageForm.tcm">
                                  <label class="custom-control-label" for="feiPiQiXu">肺脾气虚</label>
                                </div>
                                <div class=" radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="ganYuQiXie"
                                    value="肝郁气滞" v-model="CTImageForm.tcm">
                                  <label class="custom-control-label" for="ganYuQiXie">肝郁气滞</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="tanZuoYongFei"
                                    value="痰浊壅肺" v-model="CTImageForm.tcm">
                                  <label class="custom-control-label" for="tanZuoYongFei">痰浊壅肺</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="hanNingZuLuo"
                                    value="寒凝阻络" v-model="CTImageForm.tcm">
                                  <label class="custom-control-label" for="hanNingZuLuo">寒凝阻络</label>
                                </div>
                                <div class="radio-button" style="flex: 1;">
                                  <input type="radio" name="tcm" class="custom-control-input" id="ganShenKuiXu"
                                    value="肝肾亏虚" v-model="CTImageForm.tcm">
                                  <label class="custom-control-label" for="ganShenKuiXu">肝肾亏虚</label>
                                </div>
                              </div>
                            </div>
                            <h4 class="black p-0 mb-12">AI影像表现：</h4>
                            <div class="row">
                              <div class="col-sm-7 mb-12 ">
                                <h5 class="mb-12">图片上传：</h5>
                                <div class="image-box" style="height: 641px; overflow-y: auto;">
                                  <el-upload v-model="CTImageForm.image" class="upload-demo"
                                    :action="base_url + '/api/report/upload'" multiple accept="image/*"
                                    :on-preview="handlePreview" :on-remove="handleRemove" list-type="picture"
                                    :on-success="handleSuccess">
                                    <el-button type="primary" class="cus-btn">点击上传图片
                                    </el-button>
                                    <template #tip>
                                      <div class="el-upload__tip">
                                        图片大小不超过500kb
                                      </div>
                                    </template>
                                  </el-upload>
                                </div>
                              </div>
                              <div class="col-sm-2 mb-12">
                                <div class="row">
                                  <h5>影像层：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="imageLayer"
                                      name="imageLayer" placeholder="请输入……" v-model="CTImageForm.layer">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="locationLung"
                                      name="locationLung" placeholder="请输入……" v-model="CTImageForm.lung">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺叶：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="lungLobes"
                                      name="lungLobes" placeholder="请输入……" v-model="CTImageForm.lungLobe">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>肺段：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="lungSegment"
                                      name="lungSegment" placeholder="请输入……" v-model="CTImageForm.lungSegment">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>大小(mm*mm)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="size" name="size"
                                      placeholder="请输入……" v-model="CTImageForm.size">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>体积(mm³)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="volume" name="volume"
                                      placeholder="请输入……" v-model="CTImageForm.volume">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>标准直径(mm)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="diameter"
                                      name="diameter" placeholder="请输入……" v-model="CTImageForm.diameter">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>形态类型：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="shape" name="shape"
                                      placeholder="请输入……" v-model="CTImageForm.shape">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-2 mb-12">
                                <div class="row">
                                  <h5>恶性概率(%)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="maligancy"
                                      name="maligancy" placeholder="请输入……" v-model="CTImageForm.malignancy">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>浸润程度：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="wetting" name="wetting"
                                      placeholder="请输入……" v-model="CTImageForm.wetDegree">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>结节概率(%)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="noduleProbability"
                                      name="noduleProbability" placeholder="请输入……"
                                      v-model="CTImageForm.noduleProbability">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>钙化情况：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="calcification"
                                      name="calcification" placeholder="请输入……" v-model="CTImageForm.calcification">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>中心密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="centerDensity"
                                      name="centerDensity" placeholder="请输入……" v-model="CTImageForm.centerDensity">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>平均密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="averageDensity"
                                      name="averageDensity" placeholder="请输入……" v-model="CTImageForm.densityAverage">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>最大密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="maxDensity"
                                      name="maxDensity" placeholder="请输入……" v-model="CTImageForm.densityMax">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <h5>最小密度(HU)：</h5>
                                  <div>
                                    <input type="text" class="form-control wizard-required" id="minDensity"
                                      name="minDensity" placeholder="请输入……" v-model="CTImageForm.densityMin">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>影像建议（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="imageSuggestion" name="imageSuggestion"
                                  placeholder="请输入……" v-model="CTImageForm.suggestion" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>术后诊断建议（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="postOpDiagnosis" name="postOpDiagnosis"
                                  placeholder="请输入……" v-model="CTImageForm.postOpDiagnosis" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>备注（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required" id="remark" name="remark"
                                  placeholder="请输入……" v-model="CTImageForm.remark" style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                        <fieldset id="otherForm" class="tab-pane show wizard-fieldset p-0">
                          <div class="detail-form mb-32">
                            <div class="row ">
                              <div class="tab-pane active" id="flight">
                                <div class="card">
                                  <h4 class="black p-0 mb-24">其它信息表：</h4>
                                  <div class="card-header">
                                    <h5>是否需要下次复查：</h5>
                                    <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs"
                                      style="margin-top: 10px;">
                                      <li class="nav-item">
                                        <a href="#need" class="cus-btn primary-light primary active" aria-current="true"
                                          data-bs-toggle="tab" @click="otherForm.isNext = '是'">
                                          需要
                                        </a>
                                      </li>
                                      <li class="nav-item">
                                        <a href="#noNeed" class="cus-btn primary-light primary " aria-current="false"
                                          data-bs-toggle="tab" @click="otherForm.isNext = '否', otherForm.nextTime = ''">
                                          不需要
                                        </a>
                                      </li>
                                    </ul>
                                  </div>
                                  <div class="card-body tab-content mb-32">
                                    <div class="tab-pane active" id="need">
                                      <form action="#" method="post">
                                        <div class="col-sm-3">
                                          <h5>下次复查时间：</h5>
                                          <input type="date" class="form-control wizard-required" id="nextTime"
                                            name="nextTime" v-model="otherForm.nextTime">
                                          <div class="wizard-form-error">
                                          </div>
                                        </div>
                                      </form>
                                    </div>
                                    <div class="tab-pane" id="noNeed"></div>
                                  </div>
                                  <h5>设置标签：</h5>
                                  <div style="margin-top: 10px;" class="row">
                                    <div class="filter-checkbox col-sm-2 mb-32" v-for="tag in jsonTagList"
                                      :key="tag.tagId">
                                      <input type="checkbox" :id="tag.tagId"
                                        @change="setTags(tag.tagName, $event.target.checked)">
                                      <label :for="tag.tagId" class="color-black">{{ tag.tagName }}</label>
                                    </div>
                                  </div>
                                  <div class="row ">
                                    <div class="col-sm-3 mb-12">
                                      <h5>医院地址：乌鲁木齐市黄河路116号</h5>
                                    </div>
                                    <div class="col-sm-3 mb-12">
                                      <h5>邮政编码：830000</h5>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                        <div class="col-sm-2" style="margin: 0 auto;">
                          <input style="width: 200px;" type="submit" class="cus-btn" @click="submitFormMessage"
                            value="提交"></input>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
        <!-- Flight-Booking End -->
      </div>
      <!-- Main Content End -->
      <Footer />
    </div>
  </div>
</template>
<style scoped>
@import "@/renderer/assets/css/replenish.css";
@import "@/renderer/assets/css/font-awesome.css";
@import "@/renderer/assets/css/bootstrap.min.css";
@import "@/renderer/assets/css/smart_wizard_all.min.css";
@import "@/renderer/assets/css/datetime.css";
@import "@/renderer/assets/css/classic.css";
@import "@/renderer/assets/css/classic.date.css";
@import "@/renderer/assets/css/sal.css";
@import "@/renderer/assets/css/app.css";
</style>