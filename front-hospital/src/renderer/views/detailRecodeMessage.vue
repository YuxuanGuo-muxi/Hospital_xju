<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import { useRoute } from "vue-router";
import type { UploadProps, UploadUserFile } from 'element-plus'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';
import { BASE_URL } from '@/renderer/utils/request'
import { ArrowDown } from '@element-plus/icons-vue'

const base_url = BASE_URL;

// const uploadImageList = ref([])

const handleRemove = (uploadFile) => {
  console.log(uploadFile.response);
  recodeInfoForm.value.noduleList[currentIndex.value].imageList.splice(uploadFile.response, 1);
  console.log(recodeInfoForm.value.noduleList[currentIndex.value].imageList);
}

const handlePreview = (file) => {
  console.log(file);
}

const handleSuccess = (response) => {
  // 在上传成功后，response会返回上传成功的图片路径，将其赋值给CTImageForm.image
  if (recodeInfoForm.value.noduleList[currentIndex.value] !== undefined){
    recodeInfoForm.value.noduleList[currentIndex.value].image += (response + ";");
    recodeInfoForm.value.noduleList[currentIndex.value].imageList.push(response);
    CTImageForm.value.image = recodeInfoForm.value.noduleList[currentIndex.value].image;
    CTImageForm.value.imageList = recodeInfoForm.value.noduleList[currentIndex.value].imageList;
    console.log('CTImageForm.value.imageList:',CTImageForm.value.imageList);
    console.log('recodeInfoForm.value.noduleList[currentIndex.value].imageList:',recodeInfoForm.value.noduleList[currentIndex.value].imageList);
  }else{
    CTImageForm.value.image += (response + ";")
    CTImageForm.value.imageList.push(response);
    console.log('CTImageForm.value.image',CTImageForm.value.image);
  }
}

const route = useRoute();

//标签列表
let jsonTagList = ref([]);
//挂载函数
onMounted(async () => {
  // 获取查询参数
  const queryParams = route.query;
  const reportId = queryParams.reportId;
  console.log(reportId);
  let recodeInfo = await dataRecodeMessage(reportId);
  console.log('recodeInfo:',recodeInfo);
  if (recodeInfo !== null) {
    recodeInfoForm.value = recodeInfo;
  }
  CTImageMenu.value = recodeInfoForm.value.noduleList.length;
  jsonTagList.value = await dataTagList();
  console.log('CTImageMenu:',CTImageMenu.value);
  console.log("标签列表", jsonTagList.value);
  console.log('recodeInfoForm:',recodeInfoForm.value);
  console.log('recodeInfoForm.noduleList',recodeInfoForm.value.noduleList);
  noduleList.value = recodeInfoForm.value.noduleList;
});
//表单数据
let recodeInfoForm = ref({
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
  malignancyRisk: "",
  isNext: "否",
  nextTime: "",
  tagList: [],
  hospitalAddress: "乌鲁木齐市黄河路116号",
  postCode: "830000",
  noduleList: []
});
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
    }else{
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
//保存编辑信息函数
const saveEditMessage = (event) => {
  event.preventDefault();
  console.log(recodeInfoForm.value);
  axios.post("/api/report/update/", recodeInfoForm.value).then(function (response) {
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '保存成功！'
      });
      // alert("保存成功！");
    } else {
      ElMessage({
        type: 'error',
        message: '保存失败！'
      });
      // alert("保存失败！");
    }
  }).catch(function (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("保存失败！");
  });
}
//标签设定函数
const setTags = (tagName, checked) => {
  if (checked === true) {
    recodeInfoForm.value.tagList.push(tagName);
  } else {
    recodeInfoForm.value.tagList.splice(recodeInfoForm.value.tagList.indexOf(tagName), 1);
  }
  console.log(recodeInfoForm.value.tagList);
}
//进行编辑按钮
let editFlag = ref(true);
const editButton = (event) => {
  event.preventDefault();
  editFlag.value = false;
}
//修正时间
const correctTime = (dateStr) => {
  const date = new Date(dateStr);
  date.setDate(date.getDate() + 1);
  return date.toISOString().split('T')[0];
}
//加载数据
const dataRecodeMessage = async (reportId) => {
  try {
    console.log(reportId);
    const response = await axios.get("/api/report/getReport/" + reportId);
    console.log('response.data:',response.data);
    if (response.data.result === true) {
      // console.log(response.data.data.imageList);
      // response.data.data.noduleList.forEach(function (nodule) {
      //   let imgList = [];
      //   nodule.imageList.forEach(function (img) {
      //     const parts = img.split("/");
      //     const lastPart = parts[parts.length - 1];
      //     console.log(lastPart);
      //     imgList.push({
      //       name: lastPart,
      //       url: img
      //     });
      //   });
      //   uploadImageList.value.push(imgList);
      // });
      // console.log('uploadImageList.value:',uploadImageList.value);
      //手动修正时间
      if (response.data.data.patientBirthday !== ''){
        response.data.data.patientBirthday = correctTime(response.data.data.patientBirthday);
      }
      if (response.data.data.checkTime !== ''){
        response.data.data.checkTime = correctTime(response.data.data.checkTime);
      }
      if (response.data.data.diagnoseTime !== ''){
        response.data.data.diagnoseTime = correctTime(response.data.data.diagnoseTime);
      }
      response.data.data.nextTime = correctTime(response.data.data.nextTime);
      response.data.data.tagList = response.data.data.tagSet.split(",").slice(0,-1);
      // uploadImageList.value.push({
      // })
      return response.data.data;
    } else {
      ElMessage({
        type: 'error',
        message: '该随访记录未导入！'
      });
      // alert("该随访记录未导入！")
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // let recodeInfo = {
    //   patientName: "",
    //   patientAge: "",
    //   patientBirthday: "",
    //   patientId: "",
    //   patientGender: "",
    //   checkTime: "",
    //   doctorName: "",
    //   diagnoseTime: "",
    //   checkItem: "CT",
    //   checkEquipment: "CT",
    //   result: "",
    //   riskLevel: "",
    //   ctr: "",
    //   location: "",
    //   malignancyRisk: "",
    //   representation: "",
    //   tcm: "",
    //   image: [],
    //   layer: "",
    //   lung: "",
    //   lungLobe: "",
    //   lungSegment: "",
    //   size: "",
    //   volume: "",
    //   diameter: "",
    //   type: "",
    //   malignancy: "",
    //   wetDegree: "",
    //   noduleProbability: "",
    //   calcification: "",
    //   centerDensity: "",
    //   densityAverage: "",
    //   densityMax: "",
    //   densityMin: "",
    //   imageInfo: "",
    //   suggestion: "",
    //   isNext: "否",
    //   nextTime: "",
    //   tagList: [],
    //   imageList: [],
    //   hospitalAddress: "乌鲁木齐市黄河路116号",
    //   postCode: "830000"
    // };
    // return recodeInfo;
    return null;
  }
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
//结节图像菜单
let CTImageMenu = ref(1);
//结节图像列表
let noduleList = ref([]);
//当前结节图像
let currentIndex = ref(0);
let CTImageForm = ref({
  result: "",
  riskLevel: "",
  ctr: "",
  malignancyRisk: "",
  representation: "",
  tcm: "",
  image: "",
  imageList: [],
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
});
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
  if (recodeInfoForm.value.noduleList[currentIndex.value] !== undefined) {
    console.log(1);
  } else {
    console.log(2);
    recodeInfoForm.value.noduleList.push(JSON.parse(JSON.stringify(CTImageForm.value)));
    console.log('recodeInfoForm.value.noduleList:',recodeInfoForm.value.noduleList);
  }
  console.log('noduleList:', recodeInfoForm.value.noduleList);
  CTImageForm.value = {
    result: "",
    riskLevel: "",
    ctr: "",
    malignancyRisk: "",
    representation: "",
    tcm: "",
    image: "",
    imageList: [],
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
//重置已上传图片
const reSetUpLoadImg = (event) => {
  event.preventDefault();
  ElMessage({
    type: "success",
    message: "已清空已上传图片"
  })
  recodeInfoForm.value.noduleList[currentIndex.value].image = '';
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
        <h6 style="padding: 0px 110px;">><router-link :to="{name: 'souSuo'}">信息检索（点击可返回）</router-link>><router-link
            :to="'/detailPatient?patientId=' + recodeInfoForm.patientId">患者具体信息（点击可返回）</router-link>>随访信息记录</h6>
        <section class="flight-booking">
          <div class="container-fluid">
            <div class="row">
              <div class="col-xl-12">
                <div class="booking-form">
                  <form action="#" id="recodeInfoForm" method="post" role="form" class="contact-form form-wizard">
                    <div class="wizard-content overflow-visible mb-24">
                      <fieldset id="CTBaseForm" class="tab-pane show wizard-fieldset p-0">
                        <div class="detail-form mb-32">
                          <div class="d-flex justify-content-end">
                            <button class="cus-btn small-pad" @click="editButton">进行编辑
                            </button>
                            <button class="cus-btn small-pad" id="saveButton" style="margin-left: 10px;"
                              @click="saveEditMessage">保存编辑
                            </button>
                          </div>
                          <h4 class="black p-0 mb-12">CT基本信息表：</h4>
                          <div class="row ">
                            <div class="col-sm-3 mb-12">
                              <h5>姓名：</h5>
                              <input type="text" class="form-control wizard-required" id="patientName"
                                name="patientName" placeholder="请输入……" :value="recodeInfoForm.patientName"
                                :readonly="editFlag" @change="recodeInfoForm.patientName = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-3 mb-12">
                              <h5>年龄(岁)：</h5>
                              <input type="text" class="form-control wizard-required" id="patientAge" name="patientAge"
                                placeholder="请输入……" :value="recodeInfoForm.patientAge" :readonly="editFlag"
                                @change="recodeInfoForm.patientAge = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-3 mb-12">
                              <h5>出生日期：</h5>
                              <input type="date" class="form-control wizard-required" id="patientBirthday"
                                name="patientBirthday" :value="recodeInfoForm.patientBirthday" :readonly="editFlag"
                                @change="recodeInfoForm.patientBirthday = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-3 mb-12">
                              <h5>PatientId：</h5>
                              <input type="text" class="form-control wizard-required" id="patientId" name="patientId"
                                placeholder="请输入……" :value="recodeInfoForm.patientId" :readonly="editFlag"
                                @change="recodeInfoForm.patientId = $event.target.value">
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
                                      value="男" :checked="recodeInfoForm.patientGender === '男'" :disabled="editFlag"
                                      @change="recodeInfoForm.patientGender = '男'">
                                    <label class="custom-control-label" for="man">男</label>
                                  </div>
                                  <div class=" radio-button" style="flex: 1;">
                                    <input type="radio" name="patientGender" class="custom-control-input" id="women"
                                      value="女" :checked="recodeInfoForm.patientGender === '女'" :disabled="editFlag"
                                      @change="recodeInfoForm.patientGender = '女'">
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
                                :value="recodeInfoForm.checkTime" :readonly="editFlag"
                                @change="recodeInfoForm.checkTime = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-3 mb-12">
                              <h5>医生姓名：</h5>
                              <input type="text" class="form-control wizard-required" id="doctorName" name="doctorName"
                                placeholder="请输入……" :value="recodeInfoForm.doctorName" :readonly="editFlag"
                                @change="recodeInfoForm.doctorName = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-3 mb-12">
                              <h5>诊断日期：</h5>
                              <input type="date" class="form-control wizard-required" id="diagnosis" name="diagnosis"
                                :value="recodeInfoForm.diagnoseTime" :readonly="editFlag"
                                @change="recodeInfoForm.diagnoseTime = $event.target.value">
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
                      <fieldset id="recodeInfoForm" class="tab-pane show wizard-fieldset p-0">
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
                                <!-- <el-dropdown-item command="新增" v-if="editFlag === false">+新增</el-dropdown-item> -->
                              </el-dropdown-menu>
                            </template>
                          </el-dropdown>
                        </div>
                        <div class="detail-form mb-32" v-if="recodeInfoForm.noduleList[currentIndex] !== undefined">
                          <div class="d-flex justify-content-end">
                            <button class="cus-btn small-pad" id="saveButton" style="margin-right: 20px;"
                              @click="saveCTImage" v-if="editFlag === false">保存结节信息编辑</button>
                          </div>
                          <h4 class="black p-0 mb-12">第{{ currentIndex + 1 }}张CT结节图像信息表：</h4>
                          <div class="row ">
                            <div class="col-sm-3">
                              <div class="tab-pane active" id="result">
                                <h5>病理结果：</h5>
                                <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                  <div class=" radio-button" style="flex: 1;">
                                    <input type="radio" name="result" class="custom-control-input" id="good"
                                      value="recodeInfoForm.result" :checked="recodeInfoForm.noduleList[currentIndex].result === '良性'"
                                      :disabled="editFlag" @change="recodeInfoForm.noduleList[currentIndex].result = '良性'">
                                    <label class="custom-control-label" for="good">良性</label>
                                  </div>
                                  <div class=" radio-button" style="flex: 1;">
                                    <input type="radio" name="result" class="custom-control-input" id="bad" value="恶性"
                                      :checked="recodeInfoForm.noduleList[currentIndex].result === '恶性'" :disabled="editFlag"
                                      @change="recodeInfoForm.noduleList[currentIndex].result = '恶性'">
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
                                      value="低危" :checked="recodeInfoForm.noduleList[currentIndex].riskLevel === '低危'" :disabled="editFlag"
                                      @change="recodeInfoForm.noduleList[currentIndex].riskLevel = '低危'">
                                    <label class="custom-control-label" for="lower">低危</label>
                                  </div>
                                  <div class=" radio-button" style="flex: 1;">
                                    <input type="radio" name="riskLevel" class="custom-control-input" id="middle"
                                      value="中危" :checked="recodeInfoForm.noduleList[currentIndex].riskLevel === '中危'" :disabled="editFlag"
                                      @change="recodeInfoForm.noduleList[currentIndex].riskLevel = '中危'">
                                    <label class="custom-control-label" for="middle">中危</label>
                                  </div>
                                  <div class=" radio-button" style="flex: 1;">
                                    <input type="radio" name="riskLevel" class="custom-control-input" id="high"
                                      value="高危" :checked="recodeInfoForm.noduleList[currentIndex].riskLevel === '高危'" :disabled="editFlag"
                                      @change="recodeInfoForm.noduleList[currentIndex].riskLevel = '高危'">
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
                                placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].ctr" :readonly="editFlag"
                                @change="recodeInfoForm.noduleList[currentIndex].ctr = $event.target.value">
                              <div class="wizard-form-error"></div>
                            </div>
                          </div>
                          <div class="col-sm-12">
                            <h5>性质：</h5>
                            <div class="custom-control mb-12 col-sm-6" style="display: flex;margin-top: 5px;">
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="malignancyRisk" class="custom-control-input" id="shiXing" value="实性"
                                  :checked="recodeInfoForm.noduleList[currentIndex].malignancyRisk === '实性'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].malignancyRisk = '实性'">
                                <label class="custom-control-label" for="shiXing">实性</label>
                              </div>
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="malignancyRisk" class="custom-control-input" id="moBoLi" value="磨玻璃"
                                  :checked="recodeInfoForm.noduleList[currentIndex].malignancyRisk === '磨玻璃'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].malignancyRisk = '磨玻璃'">
                                <label class="custom-control-label" for="moBoLi">磨玻璃</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="malignancyRisk" class="custom-control-input" id="hunHeXingJieJie"
                                  value="混合性结节" :checked="recodeInfoForm.noduleList[currentIndex].malignancyRisk === '混合性结节'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].malignancyRisk = '混合性结节'">
                                <label class="custom-control-label" for="hunHeXingJieJie">混合性结节</label>
                              </div>
                            </div>
                          </div>
                          <div class="col-sm-12">
                            <h5>表征：</h5>
                            <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="fenYe"
                                  value="分叶" :checked="recodeInfoForm.noduleList[currentIndex].representation === '分叶'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '分叶'">
                                <label class="custom-control-label" for="fenYe">分叶</label>
                              </div>
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="maoCi"
                                  value="毛刺" :checked="recodeInfoForm.noduleList[currentIndex].representation === '毛刺'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '毛刺'">
                                <label class="custom-control-label" for="maoCi">毛刺</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="qiGuanZheng"
                                  value="气管征" :checked="recodeInfoForm.noduleList[currentIndex].representation === '气管征'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '气管征'">
                                <label class="custom-control-label" for="qiGuanZheng">气管征</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="xueGuanZheng"
                                  value="血管征" :checked="recodeInfoForm.noduleList[currentIndex].representation === '血管征'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '血管征'">
                                <label class="custom-control-label" for="xueGuanZheng">血管征</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="kongPaoZheng"
                                  value="空泡征" :checked="recodeInfoForm.noduleList[currentIndex].representation === '空泡征'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '空泡征'">
                                <label class="custom-control-label" for="kongPaoZheng">空泡征</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input" id="gaiHua"
                                  value="钙化" :checked="recodeInfoForm.noduleList[currentIndex].representation === '钙化'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '钙化'">
                                <label class="custom-control-label" for="gaiHua">钙化</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input"
                                  id="xiongMoQianLa" value="胸膜牵拉" :checked="recodeInfoForm.noduleList[currentIndex].representation === '胸膜牵拉'"
                                  :disabled="editFlag" @change="recodeInfoForm.noduleList[currentIndex].representation = '胸膜牵拉'">
                                <label class="custom-control-label" for="xiongMoQianLa">胸膜牵拉</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input"
                                  id="xiongMoOuXianZheng" value="胸膜凹陷征"
                                  :checked="recodeInfoForm.noduleList[currentIndex].representation === '胸膜凹陷征'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '胸膜凹陷征'">
                                <label class="custom-control-label" for="xiongMoOuXianZheng">胸膜凹陷征</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="representation" class="custom-control-input"
                                  id="wubiaozheng" value="无表征"
                                  :checked="recodeInfoForm.noduleList[currentIndex].representation === '无表征'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].representation = '无表征'">
                                <label class="custom-control-label" for="wubiaozheng">无表征</label>
                              </div>
                            </div>
                          </div>
                          <div class="col-sm-12 mb-32">
                            <h5>中医辩证分型：</h5>
                            <div class="custom-control col-sm-9" style="display: flex; margin-top: 5px;">
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="tcm" class="custom-control-input" id="feiPiQiXu" value="肺脾气虚"
                                  :checked="recodeInfoForm.noduleList[currentIndex].tcm === '肺脾气虚'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].tcm = '肺脾气虚'">
                                <label class="custom-control-label" for="feiPiQiXu">肺脾气虚</label>
                              </div>
                              <div class=" radio-button" style="flex: 1;">
                                <input type="radio" name="tcm" class="custom-control-input" id="ganYuQiXie" value="肝郁气滞"
                                  :checked="recodeInfoForm.noduleList[currentIndex].tcm === '肝郁气滞'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].tcm = '肝郁气滞'">
                                <label class="custom-control-label" for="ganYuQiXie">肝郁气滞</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="tcm" class="custom-control-input" id="tanZuoYongFei"
                                  value="痰浊壅肺" :checked="recodeInfoForm.noduleList[currentIndex].tcm === '痰浊壅肺'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].tcm = '痰浊壅肺'">
                                <label class="custom-control-label" for="tanZuoYongFei">痰浊壅肺</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="tcm" class="custom-control-input" id="hanNingZuLuo"
                                  value="寒凝阻络" :checked="recodeInfoForm.noduleList[currentIndex].tcm === '寒凝阻络'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].tcm = '寒凝阻络'">
                                <label class="custom-control-label" for="hanNingZuLuo">寒凝阻络</label>
                              </div>
                              <div class="radio-button" style="flex: 1;">
                                <input type="radio" name="tcm" class="custom-control-input" id="ganShenKuiXu"
                                  value="肝肾亏虚" :checked="recodeInfoForm.noduleList[currentIndex].tcm === '肝肾亏虚'" :disabled="editFlag"
                                  @change="recodeInfoForm.noduleList[currentIndex].tcm = '肝肾亏虚'">
                                <label class="custom-control-label" for="ganShenKuiXu">肝肾亏虚</label>
                              </div>
                            </div>
                          </div>
                          <h4 class="black p-0 mb-12">AI影像表现：</h4>
                          <div class="d-flex mb-12" v-if="recodeInfoForm.noduleList[currentIndex].image !== '' ">
                            <button class="cus-btn small-pad" id="saveButton" style="margin-right: 20px;"
                              @click="reSetUpLoadImg">清空已上传图片</button>
                          </div>
                          <div class="row">
                            <div class="col-sm-7 mb-12 ">
                              <div class="image-box" style="height: 641px; overflow-y: auto;">
                                <h5 class="mb-12" v-if="recodeInfoForm.noduleList[currentIndex].image !== '' ">已上传的图片：</h5>
                                <el-image  v-for="img in recodeInfoForm.noduleList[currentIndex].image.split(';').slice(0, -1)"
                                    style="width: 100px; height: 100px; margin-bottom: 10px;"
                                    :src="img" :zoom-rate="1.2" :max-scale="7"
                                    :min-scale="0.2" :preview-src-list="[img]"
                                    :initial-index="4" fit="cover" 
                                    v-if="recodeInfoForm.noduleList[currentIndex].image !== ''"
                                    @click.stop="handleClickItem"/>
                                <h5 class="mb-12">上传图片：</h5>
                                <el-upload v-if="editFlag === false"
                                    class="upload-demo"
                                    action="/api/report/upload"
                                    :on-preview="handlePreview"
                                    :on-remove="handleRemove"
                                    :on-success="handleSuccess"
                                    list-type="picture"
                                    multiple>
                                    <el-button type="primary"
                                        class="cus-btn">点击上传图片</el-button>
                                    <template #tip>
                                        <div class="el-upload__tip">
                                            图片大小不超过1Mb，支持JPG、PNG、JPEG格式
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
                                    name="imageLayer" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].layer"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].layer = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>肺：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="locationLung"
                                    name="locationLung" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].lung"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].lung = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>肺叶：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="lungLobes"
                                    name="lungLobes" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].lungLobe"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].lungLobe = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>肺段：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="lungSegment"
                                    name="lungSegment" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].lungSegment"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].lungSegment = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>大小(mm*mm)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="size" name="size"
                                    placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].size" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].size = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>体积(mm³)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="volume" name="volume"
                                    placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].volume" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].volume = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>标准直径(mm)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="diameter" name="diameter"
                                    placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].diameter" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].diameter = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>形态类型：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="shape" name="shape"
                                    placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].shape" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].shape = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-2 mb-12">
                              <div class="row">
                                <h5>恶性概率(%)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="maligancy"
                                    name="maligancy" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].malignancy"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].malignancy = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>浸润程度：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="wetting" name="wetting"
                                    placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].wetDegree" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].wetDegree = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>结节概率(%)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="noduleProbability"
                                    name="noduleProbability" placeholder="请输入……"
                                    :value="recodeInfoForm.noduleList[currentIndex].noduleProbability" :readonly="editFlag"
                                    @change="recodeInfoForm.noduleList[currentIndex].noduleProbability = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>钙化情况：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="calcification"
                                    name="calcification" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].calcification"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].calcification = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>中心密度(HU)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="centerDensity"
                                    name="centerDensity" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].centerDensity"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].centerDensity = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>平均密度(HU)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="averageDensity"
                                    name="averageDensity" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].densityAverage"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].densityAverage = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>最大密度(HU)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="maxDensity"
                                    name="maxDensity" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].densityMax"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].densityMax = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                                <h5>最小密度(HU)：</h5>
                                <div>
                                  <input type="text" class="form-control wizard-required" id="minDensity"
                                    name="minDensity" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].densityMin"
                                    :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].densityMin = $event.target.value">
                                  <div class="wizard-form-error"></div>
                                </div>
                              </div>
                            </div>
                            <div class="col-sm-10 mb-12">
                              <h5>影像建议（字数限制250字）：</h5>
                              <textarea class="form-control wizard-required" id="imageSuggestion"
                                name="imageSuggestion" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].suggestion"
                                :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].suggestion = $event.target.value" style="border: none;"></textarea>
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-10 mb-12">
                              <h5>术后诊断建议（字数限制250字）：</h5>
                              <textarea class="form-control wizard-required" id="postOpDiagnosis"
                                name="postOpDiagnosis" placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].postOpDiagnosis"
                                :readonly="editFlag" @change="recodeInfoForm.noduleList[currentIndex].postOpDiagnosis = $event.target.value" style="border: none;"></textarea>
                              <div class="wizard-form-error"></div>
                            </div>
                            <div class="col-sm-10 mb-12">
                              <h5>备注（字数限制250字）：</h5>
                              <textarea class="form-control wizard-required" id="remark" name="remark"
                                placeholder="请输入……" :value="recodeInfoForm.noduleList[currentIndex].remark" :readonly="editFlag"
                                @change="recodeInfoForm.noduleList[currentIndex].remark = $event.target.value" style="border: none;"></textarea>
                              <div class="wizard-form-error"></div>
                            </div>
                          </div>
                        </div>
                        <div class="detail-form mb-32" v-if="recodeInfoForm.noduleList[currentIndex] === undefined">
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
                      <fieldset id="recodeInfoForm" class="tab-pane show wizard-fieldset p-0">
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
                                      <a href="#need" :class="{
                                        'cus-btn': true,
                                        'primary-light': true,
                                        'primary': true,
                                        'active': recodeInfoForm.isNext === '是'
                                      }" :style="{
                                        'pointer-events': editFlag ? 'none' : 'auto'
                                      }" aria-current="true"
                                        data-bs-toggle="tab" @click="recodeInfoForm.isNext = '是'">
                                        需要
                                      </a>
                                    </li>
                                    <li class="nav-item">
                                      <a href="#noNeed" :class="{
                                        'cus-btn': true,
                                        'primary-light': true,
                                        'primary': true,
                                        'active': recodeInfoForm.isNext === '否'
                                      }" :style="{
                                        'pointer-events': editFlag ? 'none' : 'auto'
                                      }" aria-current="false"
                                        data-bs-toggle="tab"
                                        @click="recodeInfoForm.isNext = '否', recodeInfoForm.nextTime = ''">
                                        不需要
                                      </a>
                                    </li>
                                  </ul>
                                </div>
                                <div class="card-body tab-content mb-32">
                                  <div class="tab-pane active" id="need">
                                    <form action="#" method="post">
                                      <div class="col-sm-3">
                                        <h5>复查日期：</h5>
                                        <input type="date" class="form-control wizard-required" id="nextTime"
                                          name="nextTime" :value="recodeInfoForm.nextTime" :readonly="editFlag"
                                          @change="recodeInfoForm.nextTime = $event.target.value">
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
                                           @change="setTags(tag.tagName,$event.target.checked)" 
                                            :checked="recodeInfoForm.tagList.includes(tag.tagName)" :disabled="editFlag">
                                    <label :for="tag.tagId"
                                           class="color-black">{{ tag.tagName }}</label>
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
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
        <!-- Flight-Booking End -->
      </div>
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

.demo-image__error .image-slot {
  font-size: 30px;
}

.demo-image__error .image-slot .el-icon {
  font-size: 30px;
}

.demo-image__error .el-image {
  width: 100%;
  height: 200px;
}

header {
  background: url(../assets/image/title-banner.png);
}
</style>