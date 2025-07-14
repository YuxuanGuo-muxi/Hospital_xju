<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import userBk from '@/renderer/assets/picture/user-bk.png'
import calender from '@/renderer/assets/picture/calender.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import axios from '@/renderer/utils/request';
import { ElMessageBox, ElMessage } from 'element-plus';
import { data } from "jquery";

// 获取当前路由信息
const route = useRoute();
let id = ref("")
// 定义数据
let jsonDataPatient = ref({
  studyUnit: "新疆医科大学第四临床自治区中医医院",
  createTime: "",
  doctorId: "",
  source: "",
  admissionNumber: "",
  // clinicNo: "",
  patientGender: "",
  patientType: "",
  patientGroup: "",
  patientAge: "",
  patientName: "",
  patientWork: "",
  patientEduLevel: "",
  patientMarriage: "",
  patientPregnancy: "",
  patientMalignant: "",
  patientAddress: "",
  patientPhone: "",
  patientAllergy: "是",
  drugName: "",
  isSmoke: "是",
  smokingType: "主动",
  smokingYears: "",
  smokingAmount: "",
  isQuit: "是",
  quitYears: "",
  medicalHistory: "",
  tagList: []
});
//切换界面
let changeView = ref("baseMessage");
//编辑信息
let editMessage = ref(true);
const changeViewFunc = (viewName) => {
  changeView.value = viewName;
  // console.log(changeView.value);
}
//患者随访记录信息
let jsonPatientRecodeInfo = ref({});
let jsonSearchRecodeInfo = ref({
  "patientId": "",
  "pageIndex": 1,
  "pageSize": 4
});
//患者肺癌信息
let jsonPatientLangCancer = ref({});
let jsonSearchLangCancer = ref({
  "patientId": "",
  "pageIndex": 1,
  "pageSize": 4
});
//work选项
let special_work = ['矿工', '水泥工', '纺织工', '其它']
let unspecial_work = ['工人', '农民', '知识分子', '管理人员', '服务行业', '离退休', '无业', '下岗']
// 在挂载时执行的逻辑
onMounted(async () => {
  // 获取查询参数
  const queryParams = route.query;
  const patientId = queryParams.patientId;
  jsonPatientRecodeInfo.value.patientId = patientId;
  jsonPatientLangCancer.value.patientId = patientId;
  jsonSearchRecodeInfo.value.patientId = patientId;
  jsonSearchLangCancer.value.patientId = patientId;
  let dataPatient = await dataDetailPatient(patientId);
  if (dataPatient !== null) {
    jsonDataPatient.value = dataPatient;
  }
  // console.log(dataDetailPatient(patientId));
  // console.log(jsonSearchRecodeInfo.value);
  let patientRecodeInfo = await dataRecodeList(jsonSearchRecodeInfo.value);
  if (patientRecodeInfo !== null) {
    jsonPatientRecodeInfo.value = patientRecodeInfo;
  }
  console.log('jsonPatientRecodeInfo:',jsonPatientRecodeInfo.value);
  let patientLangCancer = await dataLangCancerList(jsonSearchLangCancer.value);
  if (patientLangCancer !== null) {
    jsonPatientLangCancer.value = patientLangCancer;
  }
  console.log('jsonPatientLangCancer:',jsonPatientLangCancer.value);
  // console.log(jsonDataPatient.value);
});
//创造标签
// let createTagName = ref("");
// const createTag = () => {
//   let flag = true;
//   if (createTagName.value == "") {
//     alert("标签名称不能为空！");
//     flag = false;
//   }
//   jsonDataPatient.value.tagList.forEach(function (item) {
//     if (item.tagName == createTagName.value) {
//       alert("标签名称已存在！");
//       flag = false;
//       return;
//     }
//   })
//   if (flag == true) {
//     jsonDataPatient.value.tagList.push({
//       "tagId": "",
//       "tagName": createTagName.value
//     });
//   }
// }
// //删除标签函数
// const removeTag = (tagId) => {
//   jsonDataPatient.value.tagList.forEach(function (item, index) {
//     if (item.tagId == tagId) {
//       jsonDataPatient.value.tagList.splice(index, 1);
//     }
//   });
// }
//编辑信息函数
const editMessageFunc = () => {
  editMessage.value = false;
  console.log(editMessage.value);
}
//保存编辑信息函数
const saveEditMessage = () => {
  console.log(jsonDataPatient.value)
  axios.post("/api/patient/update", jsonDataPatient.value).then(function (response) {
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
//随访翻页函数
const recodePageFront = async () => {
  if (jsonSearchRecodeInfo.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    // alert("已经是第一页了！")
    return;
  }
  jsonSearchRecodeInfo.value.pageIndex -= 1;
  // console.log(jsonSearchRecodeInfo.value);
  jsonPatientRecodeInfo.value = await dataRecodeList(jsonSearchRecodeInfo.value);
};
const recodePageBack = async () => {
  jsonSearchRecodeInfo.value.pageIndex += 1;
  // console.log(jsonSearchRecodeInfo.value);
  let patientRecodeInfo = await dataRecodeList(jsonSearchRecodeInfo.value);
  if (patientRecodeInfo.recodeMessageList.length === 0) {
    jsonSearchRecodeInfo.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    // alert("已经是最后一页了！")
    return;
  }
  jsonPatientRecodeInfo.value = patientRecodeInfo;
}
//肺癌翻页函数
const langCancerPageFront = async () => {
  if (jsonSearchLangCancer.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    // alert("已经是第一页了！")
    return;
  }
  jsonSearchLangCancer.value.pageIndex -= 1;
  // console.log(jsonSearchRecodeInfo.value);
  jsonPatientLangCancer.value = await dataLangCancerList(jsonSearchLangCancer.value);
};
const langCancerPageBack = async () => {
  jsonSearchLangCancer.value.pageIndex += 1;
  // console.log(jsonSearchRecodeInfo.value);
  let patientLangCancer = await dataLangCancerList(jsonSearchLangCancer.value);
  if (patientLangCancer.langCancerMessageList.length === 0) {
    jsonSearchLangCancer.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    // alert("已经是最后一页了！")
    return;
  }
  jsonPatientLangCancer.value = patientLangCancer;
}
//获取患者肺癌信息
const dataLangCancerList = async (jsonSearchLangCancerValue) => {
  try {
    console.log('jsonSearchLanCancerValue:', jsonSearchLangCancerValue);
    const response = await axios.post("/api/report/getLangCancerByPatientId", jsonSearchLangCancerValue);
    console.log(response.data.result);
    if (response.data.result === true) {
      // console.log(response.data.data);
      console.log(response.data.data);
      // alert("获取患者随访记录成功！");
      let patientLangCancer = {
        "langCancerMessageList": []
      };
      response.data.data.forEach(function (item) {
        patientLangCancer.langCancerMessageList.push(item);
      });
      return patientLangCancer;
    } else {
      // ElMessage({
      //   type: 'error',
      //   message: '获取患者肺癌记录失败！'
      // });
      // // alert("获取患者随访记录失败！")
      // return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    //测试案例
    let patientLangCancer = {
      "langCancerMessageList": [
        {
          reportId: "000001",
          doctorId: "123",
          diagnoseTime: "2021-06-01",
          diagnosticLogout: "无fqwfqw qfwqw fqwf qwf qwf qwfgqwgqg qwg qg asfqwfqw qwf qw qw fqwfgqw  gqg qw",//诊断建议
          curativeEffect: "无",//疗效
          seconds: "1",
          therapyMethod: "靶向治疗"
        }
      ]
    };
    return patientLangCancer;
  }
}
// 获取患者随访记录
const dataRecodeList = async (jsonSearchRecodeInfoValue) => {
  try {
    console.log('jsonSearchRecodeInfoValue:', jsonSearchRecodeInfoValue);
    const response = await axios.post("/api/report/getReportByPatientId", jsonSearchRecodeInfoValue);
    if (response.data.result === true) {
      // console.log(response.data.data);
      console.log(response.data.data);
      // alert("获取患者随访记录成功！");
      let patientRecodeInfo = {
        "recodeMessageList": []
      };
      response.data.data.forEach(function (item) {
        patientRecodeInfo.recodeMessageList.push(item);
      });
      return patientRecodeInfo;
    } else {
      ElMessage({
        type: 'error',
        message: '获取患者随访记录失败！'
      });
      // alert("获取患者随访记录失败！")
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // let patientRecodeInfo = {
    //     "recodeMessageList": [
    //         {
    //             "doctorId": "000005",
    //             "nextTime": "2021-06-01",
    //             "message": "患者的情况属于正常，无明显异常。",
    //             "reportId": "000001",
    //             "reportTimes": 1
    //         },
    //         {
    //             "doctorId": "000006",
    //             "nextTime": "2021-06-02",
    //             "message": "患者的情况异常，需要重点关注。",
    //             "reportId": "000002",
    //             "reportTimes": 2
    //         },
    //         {
    //             "doctorId": "000007",
    //             "nextTime": "2021-06-03",
    //             "message": "患者的情况属于正常，无明显异常。",
    //             "reportId": "000003",
    //             "reportTimes": 3
    //         },
    //         {
    //             "doctorId": "000008",
    //             "nextTime": "2021-06-04",
    //             "message": "患者的情况属于正常，无明显异常。",
    //             "reportId": "000004",
    //             "reportTimes": 4
    //         }
    //     ]
    // }
    // return patientRecodeInfo;
    return null;
  }
}
//既往病史改变
const updateMedicalHistory = (event) => {
  jsonDataPatient.value.medicalHistory = event.target.value;
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
        type: 'error',
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
    //     "nextTime": "2024-01-02",
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
//删除随访记录
const removeRecodeMessage = async (reportId) => {
  try {
    const confirm = await ElMessageBox.confirm('确定要删除该记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).catch(()=>{
      ElMessage({
        type: 'info',
        message: '取消删除！'
      });
    });
    if (confirm !== 'confirm') {
      return;
    };
    const url = "/api/report/delete/"+reportId
    const response = await axios.get(url);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '删除成功！'
      });
      let patientRecodeInfo = await dataRecodeList(jsonSearchRecodeInfo.value);
      if (patientRecodeInfo !== null) {
        jsonPatientRecodeInfo.value = patientRecodeInfo;
      }
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
  }
}
//删除肺癌记录
const removeLangCancerMessage = async (reportId) => {
  try {
    const confirm = await ElMessageBox.confirm('确定要删除该记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).catch(()=>{
      ElMessage({
        type: 'info',
        message: '取消删除！'
      });
    });
    if (confirm !== 'confirm') {
      return;
    };
    const url = "/api/report/deleteLangCancer/"+reportId;
    const response = await axios.get(url);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '删除成功！'
      });
      let patientLangCancer = await dataLangCancerList(jsonSearchLangCancer.value);
      if (patientLangCancer !== null) {
        jsonPatientLangCancer.value = patientLangCancer;
      }
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
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
      <!-- Header Area end -->


      <!-- Main Content Start -->
      <div class="page-content m-0">
        <h6 style="padding: 0px 110px;">><router-link :to="{ name: 'souSuo' }">信息检索（点击可返回）</router-link>>患者具体信息</h6>
        <div class="page-content m-0">

          <!-- Blog Area Start -->
          <section class="news-blog mb-24">
            <div class="container-fluid">

              <ul class="nav nav-tabs mb-32 sal-animate" id="myTab" role="tablist" data-sal="slide-left"
                data-sal-duration="800" data-sal-delay="100" data-sal-easing="ease-in-out" style="margin: 0;">
                <li class="nav-item" role="presentation">
                  <button :class="{
                    'nav-link': true,
                    'active': changeView == 'baseMessage'
                  }" id="baseForm-tab" data-bs-toggle="tab" data-bs-target="#baseMessage" type="button" role="tab"
                    aria-controls="all" aria-selected="true" @click="changeViewFunc('baseMessage')">基本信息</button>
                </li>
                <!-- <li class="vr-line"></li> -->
                <li class="nav-item" role="presentation">
                  <button :class="{
                    'nav-link': true,
                    'active': changeView == 'recodeMessage'
                  }" id="recodeForm-tab" data-bs-toggle="tab" data-bs-target="#recodeMessage" type="button" role="tab"
                    aria-controls="today" aria-selected="true" @click="changeViewFunc('recodeMessage')">随访信息</button>
                </li>
                <li class="nav-item" role="presentation">
                  <button :class="{
                    'nav-link': true,
                    'active': changeView == 'langCancerMessage'
                  }" id="recodeForm-tab" data-bs-toggle="tab" data-bs-target="#langCancerMessage" type="button"
                    role="tab" aria-controls="today" aria-selected="true"
                    @click="changeViewFunc('langCancerMessage')">肺癌信息</button>
                </li>
              </ul>
              <div class="tab-content mt-32 mb-24" id="myTabContent">
                <div :class="{
                  'tab-pane': true,
                  'fade': true,
                  'show': changeView == 'baseMessage',
                  'active': changeView == 'baseMessage'
                }" v-if="changeView == 'baseMessage'" id="baseMessage" role="tabpanel" aria-labelledby="all-tab">
                  <div class="row sal-animate">
                    <div class="col-xl-12 col-lg-12 col-md-6 col-sm-6 mb-24">
                      <div class="blog-box bg-white light-shadow p-24 br-20">
                        <div class="row align-items-center">
                          <div class="col-lg-12">
                            <div class="content-box">
                              <div class="d-flex justify-content-end">
                                <button class="cus-btn small-pad" id="changeButton"
                                  @click="editMessageFunc">进行编辑</button>
                                <button class="cus-btn small-pad" id="saveButton" style="margin-left: 10px;"
                                  @click="saveEditMessage">保存编辑</button>
                              </div>
                              <div class="detail-form mb-32" style="margin-top: 10px;">
                                <h4 class="black p-0 mb-40">基本资料表：</h4>
                                <div class="row ">
                                  <div class="col-sm-3 mb-12">
                                    <h5>研究单位：</h5>
                                    <input type="text" class="form-control wizard-required" id="studyUnit"
                                      name="studyUnit" readonly value="新疆医科大学第四临床自治区中医医院">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-3 mb-12">
                                    <h5>填表日期：</h5>
                                    <input type="date" class="form-control wizard-required" id="createTime"
                                      name="createTime" :value="jsonDataPatient.createTime" :readonly="editMessage"
                                      @change="jsonDataPatient.createTime = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-3 mb-12">
                                    <h5>调查人员ID：</h5>
                                    <input type="text" class="form-control wizard-required" id="doctorId"
                                      name="doctorId" placeholder="请输入……" :value="jsonDataPatient.doctorId"
                                      :readonly="editMessage" @change="jsonDataPatient.doctorId = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-3">
                                    <h5>病例来源：</h5>
                                    <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="source" class="custom-control-input"
                                          id="admissionNumber" value="住院号" :checked="jsonDataPatient.source == '住院号'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.source = $event.target.value">
                                        <label class="custom-control-label" for="admissionNumber">住院号</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="resource" class="custom-control-input" id="clinicNo"
                                          value="门诊号" :checked="jsonDataPatient.source == '门诊号'" :disabled="editMessage"
                                          @change="jsonDataPatient.source = $event.target.value">
                                        <label class="custom-control-label" for="clinicNo">门诊号</label>
                                      </div>
                                    </div>
                                  </div>
                                  <div class="col-sm-3">
                                    <h5>性别：</h5>
                                    <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientGender" class="custom-control-input" id="man"
                                          value="男" :checked="jsonDataPatient.patientGender == '男'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientGender = $event.target.value">
                                        <label class="custom-control-label" for="man">男</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientGender" class="custom-control-input" id="women"
                                          value="女" :checked="jsonDataPatient.patientGender == '女'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientGender = $event.target.value">
                                        <label class="custom-control-label" for="women">女</label>
                                      </div>
                                    </div>
                                  </div>
                                  <div class="col-sm-5">
                                    <h5>患者类型：</h5>
                                    <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientType" class="custom-control-input" id="肺结节"
                                          value="肺结节" :checked="jsonDataPatient.patientType == '肺结节'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientType = $event.target.value">
                                        <label class="custom-control-label" for="肺结节">肺结节</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1; margin-left: -50px;">
                                        <input type="radio" name="patientType" class="custom-control-input" id="肺结节转肺癌"
                                          value="肺结节转肺癌" :checked="jsonDataPatient.patientType == '肺结节转肺癌'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientType = $event.target.value">
                                        <label class="custom-control-label" for="肺结节转肺癌">肺结节转肺癌</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientType" class="custom-control-input" id="肺癌"
                                          value="肺癌" :checked="jsonDataPatient.patientType == '肺癌'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientType = $event.target.value">
                                        <label class="custom-control-label" for="肺癌">肺癌</label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-4 mb-12" v-if="jsonDataPatient.source === '住院号'">
                                    <h5>身份证号：</h5>
                                    <input type="text" class="form-control wizard-required" id="admissionNumber"
                                      name="admissionNumber" placeholder="请输入……"
                                      :value="jsonDataPatient.admissionNumber" :readonly="editMessage"
                                      @change="jsonDataPatient.admissionNumber = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-4 mb-12" v-if="jsonDataPatient.source === '门诊号'">
                                    <h5>身份证号：</h5>
                                    <input type="text" class="form-control wizard-required" id="clinicNo"
                                      name="admissionNumber" placeholder="请输入……" :value="jsonDataPatient.admissionNumber"
                                      :readonly="editMessage" @change="jsonDataPatient.admissionNumber = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-2 mb-12">
                                    <h5>民族：</h5>
                                    <input type="text" name="patientGroup" class="form-control wizard-required"
                                      id="patientGroup" :value="jsonDataPatient.patientGroup" placeholder="请输入……"
                                      :readonly="editMessage"
                                      @change="jsonDataPatient.patientGroup = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-2 mb-12">
                                    <h5>年龄(岁)：</h5>
                                    <input type="text" class="form-control wizard-required" id="patientAge"
                                      name="patientAge" placeholder="请输入……" :value="jsonDataPatient.patientAge"
                                      :readonly="editMessage"
                                      @change="jsonDataPatient.patientAge = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-3 mb-12">
                                    <h5>姓名：</h5>
                                    <input type="text" class="form-control wizard-required" id="patientName"
                                      name="patientName" placeholder="请输入……" :value="jsonDataPatient.patientName"
                                      :readonly="editMessage"
                                      @change="jsonDataPatient.patientName = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="card">
                                    <div class="card-header">
                                      <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                                        <li class="nav-item">
                                          <a href="#teshu" :class="{
                                            'active': special_work.includes(jsonDataPatient.patientWork),
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true
                                          }" aria-current="true" data-bs-toggle="tab" id="specialWork" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }">
                                            特殊职业：
                                          </a>
                                        </li>
                                        <li class="nav-item">
                                          <a href="#feiteshu" :class="{
                                            'active': unspecial_work.includes(jsonDataPatient.patientWork),
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true
                                          }" aria-current="false" data-bs-toggle="tab" id="unspecialWork" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }">
                                            非特殊职业：
                                          </a>
                                        </li>
                                      </ul>
                                    </div>
                                    <div class="card-body tab-content">
                                      <div :class="{
                                        'tab-pane': true,
                                        'active': special_work.includes(jsonDataPatient.patientWork)
                                      }" id="teshu">
                                        <div class="custom-control mb-12" style="display: flex;">
                                          <div class=" radio-button" style="flex: 1;">
                                            <input type="radio" name="patientWork" class="custom-control-input"
                                              id="miner" value="矿工" :disabled="editMessage"
                                              :checked="jsonDataPatient.patientWork == '矿工'"
                                              @change="jsonDataPatient.patientWork = $event.target.value">
                                            <label class="custom-control-label" for="miner">矿工</label>
                                          </div>
                                          <div class=" radio-button" style="flex: 1;">
                                            <input type="radio" name="patientWork" class="custom-control-input"
                                              id="cementer" value="水泥工" :disabled="editMessage"
                                              :checked="jsonDataPatient.patientWork == '水泥工'"
                                              @change="jsonDataPatient.patientWork = $event.target.value">
                                            <label class="custom-control-label" for="cementer">水泥工</label>
                                          </div>
                                          <div class="radio-button" style="flex: 1;">
                                            <input type="radio" name="patientWork" class="custom-control-input"
                                              id="spinner" value="纺织工" :disabled="editMessage"
                                              :checked="jsonDataPatient.patientWork == '纺织工'"
                                              @change="jsonDataPatient.patientWork = $event.target.value">
                                            <label class="custom-control-label" for="spinner">纺织工</label>
                                          </div>
                                          <div class="radio-button" style="flex: 1;">
                                            <input type="radio" name="patientWork" class="custom-control-input"
                                              id="other" value="其它" :disabled="editMessage"
                                              :checked="jsonDataPatient.patientWork == '其它'"
                                              @change="jsonDataPatient.patientWork = $event.target.value">
                                            <label class="custom-control-label" for="other">其它</label>
                                          </div>
                                        </div>
                                      </div>
                                      <div :class="{
                                        'tab-pane': true,
                                        'active': unspecial_work.includes(jsonDataPatient.patientWork)
                                      }" id="feiteshu">
                                        <form action="#" method="post">
                                          <div class="custom-control mb-12" style="display: flex;">
                                            <div class=" radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="woker" value="工人" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '工人'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="woker">工人</label>
                                            </div>
                                            <div class=" radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="farmer" value="农民" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '农民'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="farmer">农民</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="intellectual" value="知识分子" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '知识分子'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="intellectual">知识分子</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="custodian" value="管理人员" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '管理人员'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="custodian">管理人员</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="service" value="服务行业" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '服务行业'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="service">服务行业</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="retirement" value="离退休" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '离退休'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="retirement">离退休</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="unemployed" value="无业" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '无业'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="unemployed">无业</label>
                                            </div>
                                            <div class="radio-button" style="flex: 1;">
                                              <input type="radio" name="patientWork" class="custom-control-input"
                                                id="laid_off" value="下岗" :disabled="editMessage"
                                                :checked="jsonDataPatient.patientWork == '下岗'"
                                                @change="jsonDataPatient.patientWork = $event.target.value">
                                              <label class="custom-control-label" for="laid_off">下岗</label>
                                            </div>
                                          </div>
                                        </form>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-6">
                                    <h5>教育程度：</h5>
                                    <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientEduLevel" class="custom-control-input"
                                          id="elementary" value="小学及以下"
                                          :checked="jsonDataPatient.patientEduLevel == '小学及以下'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientEduLevel = $event.target.value">
                                        <label class="custom-control-label" for="elementary">小学及以下</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientEduLevel" class="custom-control-input"
                                          id="secondary" value="初中、高中及中专"
                                          :checked="jsonDataPatient.patientEduLevel == '初中、高中及中专'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientEduLevel = $event.target.value">
                                        <label class="custom-control-label" for="secondary">初中、高中及中专</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientEduLevel" class="custom-control-input"
                                          id="college+" value="大专、本科及以上"
                                          :checked="jsonDataPatient.patientEduLevel == '大专、本科及以上'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientEduLevel = $event.target.value">
                                        <label class="custom-control-label" for="college+">大专、本科及以上</label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-10">
                                    <h5>婚姻状况：</h5>
                                    <div class="custom-control mb-12" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMarriage" class="custom-control-input"
                                          id="unmarried" value="未婚" :checked="jsonDataPatient.patientMarriage == '未婚'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientMarriage = $event.target.value">
                                        <label class="custom-control-label" for="unmarried">未婚</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMarriage" class="custom-control-input"
                                          id="beMarried" value="已婚" :checked="jsonDataPatient.patientMarriage == '已婚'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientMarriage = $event.target.value">
                                        <label class="custom-control-label" for="beMarried">已婚</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMarriage" class="custom-control-input"
                                          id="separate" value="分居" :checked="jsonDataPatient.patientMarriage == '分居'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientMarriage = $event.target.value">
                                        <label class="custom-control-label" for="separate">分居</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMarriage" class="custom-control-input"
                                          id="divorce" value="离异" :checked="jsonDataPatient.patientMarriage == '离异'"
                                          :disabled="editMessage"
                                          @change="jsonDataPatient.patientMarriage = $event.target.value">
                                        <label class="custom-control-label" for="divorce">离异</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMarriage" class="custom-control-input"
                                          id="spouseBereaved" value="丧偶"
                                          :checked="jsonDataPatient.patientMarriage == '丧偶'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientMarriage = $event.target.value">
                                        <label class="custom-control-label" for="spouseBereaved">丧偶</label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-3 mb-12">
                                    <h5>妊娠史：</h5>
                                    <div class="custom-control" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientPregnancy" class="custom-control-input"
                                          id="havePregnancy" value="有孕产"
                                          :checked="jsonDataPatient.patientPregnancy == '有孕产'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientPregnancy = $event.target.value">
                                        <label class="custom-control-label" for="havePregnancy">有孕产</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientPregnancy" class="custom-control-input"
                                          id="noHavePregnancy" value="无孕产"
                                          :checked="jsonDataPatient.patientPregnancy == '无孕产'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientPregnancy = $event.target.value">
                                        <label class="custom-control-label" for="noHavePregnancy">无孕产</label>
                                      </div>
                                    </div>
                                  </div>
                                  <div class="col-sm-3 mb-12">
                                    <h5>恶性肿瘤家族史：</h5>
                                    <div class="custom-control" style="display: flex; margin-top: 5px;">
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMalignant" class="custom-control-input"
                                          id="noHaveMalignancy" value="无"
                                          :checked="jsonDataPatient.patientMalignant == '无'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientMalignant = $event.target.value">
                                        <label class="custom-control-label" for="noHaveMalignancy">无</label>
                                      </div>
                                      <div class=" radio-button" style="flex: 1;">
                                        <input type="radio" name="patientMalignant" class="custom-control-input"
                                          id="haveMalignancy" value="有"
                                          :checked="jsonDataPatient.patientMalignant == '有'" :disabled="editMessage"
                                          @change="jsonDataPatient.patientMalignant = $event.target.value">
                                        <label class="custom-control-label" for="haveMalignancy">有</label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-sm-6 mb-24">
                                    <h5>家庭住址：</h5>
                                    <input type="text" class="form-control wizard-required" id="patientAddress"
                                      name="patientAddress" placeholder="请输入……" :value="jsonDataPatient.patientAddress"
                                      :readonly="editMessage"
                                      @change="jsonDataPatient.patientAddress = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-5 mb-24">
                                    <h5>联系电话：</h5>
                                    <input type="text" class="form-control wizard-required" id="patientPhone"
                                      name="patientPhone" placeholder="请输入……" :value="jsonDataPatient.patientPhone"
                                      :readonly="editMessage"
                                      @change="jsonDataPatient.patientPhone = $event.target.value">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                </div>
                                <h4 class="black p-0 mb-12">药物过敏史：</h4>
                                <div class="row ">

                                  <div class="card">
                                    <div class="card-header">
                                      <h5>有无过敏史：</h5>
                                      <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                                        <li class="nav-item">
                                          <a href="#haveAllergy" :class="{
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true,
                                            'active': jsonDataPatient.patientAllergy == '是'
                                          }" aria-current="true" data-bs-toggle="tab" id="haveAllergyA" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }" @click="jsonDataPatient.patientAllergy = '是'">
                                            有
                                          </a>
                                        </li>
                                        <li class="nav-item">
                                          <a href="#noHaveAllergy" :class="{
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true,
                                            'active': jsonDataPatient.patientAllergy == '否'
                                          }" aria-current="false" data-bs-toggle="tab" id="noHaveAllergyA" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }"
                                            @click="jsonDataPatient.patientAllergy = '否', jsonDataPatient.drugName = ''">
                                            无
                                          </a>
                                        </li>
                                      </ul>
                                    </div>
                                    <div class="card-body tab-content">
                                      <div :class="{
                                        'tab-pane': true,
                                        'active': jsonDataPatient.patientAllergy == '是'
                                      }" id="haveAllergy">
                                        <div class="col-sm-5 mb-24">
                                          <h5>过敏药物名称：</h5>
                                          <input type="text" class="form-control wizard-required" id="drugName"
                                            name="drugName" placeholder="请输入……" :value="jsonDataPatient.drugName"
                                            :readonly="editMessage"
                                            @change="jsonDataPatient.drugName = $event.target.value">
                                          <div class="wizard-form-error">
                                          </div>
                                        </div>
                                      </div>
                                      <div class="tab-pane" id="noHaveAllergy">
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <h4 class="black p-0 mb-12">吸烟史：</h4>
                                <div class="row">
                                  <div class="card">
                                    <div class="card-header">
                                      <h5>有无吸烟史：</h5>
                                      <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                                        <li class="nav-item">
                                          <a href="#smoking" :class="{
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true,
                                            'active': jsonDataPatient.isSmoke == '是'
                                          }" aria-current="true" data-bs-toggle="tab" id="smokingA" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }" @click="jsonDataPatient.isSmoke = '是'">
                                            有
                                          </a>
                                        </li>
                                        <li class="nav-item">
                                          <a href="#noSmoking" :class="{
                                            'cus-btn': true,
                                            'primary-light': true,
                                            'primary': true,
                                            'active': jsonDataPatient.isSmoke == '否'
                                          }" aria-current="false" data-bs-toggle="tab" id="noSmokingA" :style="{
                                            'pointer-events': editMessage ? 'none' : 'auto'
                                          }"
                                            @click="jsonDataPatient.isSmoke = '否', jsonDataPatient.smokingType = '', jsonDataPatient.smokingYears = '', jsonDataPatient.smokingAmount = '', jsonDataPatient.isQuit = '', jsonDataPatient.quitYears = ''">
                                            无
                                          </a>
                                        </li>
                                      </ul>
                                    </div>
                                    <div class="card-body tab-content">
                                      <div :class="{
                                        'tab-pane': true,
                                        'active': jsonDataPatient.isSmoke == '是'
                                      }" id="smoking">
                                        <div class="card-header">
                                          <h5>吸烟方式：</h5>
                                          <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                                            <li class="nav-item">
                                              <a href="#initative" :class="{
                                                'cus-btn': true,
                                                'primary-light': true,
                                                'primary': true,
                                                'active': jsonDataPatient.smokingType == '主动'
                                              }" aria-current="true" data-bs-toggle="tab" id="initativeWay" :style="{
                                                'pointer-events': editMessage ? 'none' : 'auto'
                                              }" @click="jsonDataPatient.smokingType = '主动'">
                                                主动
                                              </a>
                                            </li>
                                            <li class="nav-item">
                                              <a href="#passivity" :class="{
                                                'cus-btn': true,
                                                'primary-light': true,
                                                'primary': true,
                                                'active': jsonDataPatient.smokingType == '被动'
                                              }" aria-current="false" data-bs-toggle="tab" id="passivityWay" :style="{
                                                'pointer-events': editMessage ? 'none' : 'auto'
                                              }"
                                                @click="jsonDataPatient.smokingType = '被动', jsonDataPatient.smokingAmount = '', jsonDataPatient.isQuit = '', jsonDataPatient.quitYears = ''">
                                                被动
                                              </a>
                                            </li>
                                          </ul>
                                        </div>
                                        <div class="card-body tab-content">
                                          <div :class="{
                                            'tab-pane': true,
                                            'active': jsonDataPatient.smokingType == '主动'
                                          }" id="initative">
                                            <div class="col-sm-5 mb-12">
                                              <div class="col-sm-5">
                                                <h5>吸烟年限(年)：</h5>
                                                <input type="text" class="form-control wizard-required"
                                                  id="initativeSmokingYears" name="smokingYears" placeholder="请输入……"
                                                  :value="jsonDataPatient.smokingYears" :readonly="editMessage"
                                                  @change="jsonDataPatient.smokingYears = $event.target.value">
                                                <div class="wizard-form-error">
                                                </div>
                                              </div>
                                            </div>
                                            <div class="col-sm-5 mb-12">
                                              <h5>吸烟量(支/天)：</h5>
                                              <input type="text" class="form-control wizard-required" id="smokingAmount"
                                                name="smokingAmount" placeholder="请输入……"
                                                :value="jsonDataPatient.smokingAmount" :readonly="editMessage"
                                                @change="jsonDataPatient.smokingAmount = $event.target.value">
                                              <div class="wizard-form-error">
                                              </div>
                                            </div>
                                            <div class="card-header">
                                              <h5>是否戒烟：</h5>
                                              <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                                                <li class="nav-item">
                                                  <a href="#true" :class="{
                                                    'cus-btn': true,
                                                    'primary-light': true,
                                                    'primary': true,
                                                    'active': jsonDataPatient.isQuit == '是'
                                                  }" aria-current="true" data-bs-toggle="tab" id="quitSmoking" :style="{
                                                    'pointer-events': editMessage ? 'none' : 'auto'
                                                  }" @click="jsonDataPatient.isQuit = '是'">
                                                    是
                                                  </a>
                                                </li>
                                                <li class="nav-item">
                                                  <a href="#false" :class="{
                                                    'cus-btn': true,
                                                    'primary-light': true,
                                                    'primary': true,
                                                    'active': jsonDataPatient.isQuit == '否'
                                                  }" aria-current="false" data-bs-toggle="tab" id="noQuitSmoking"
                                                    :style="{
                                                      'pointer-events': editMessage ? 'none' : 'auto'
                                                    }"
                                                    @click="jsonDataPatient.isQuit = '否', jsonDataPatient.quitYears = ''">
                                                    否
                                                  </a>
                                                </li>
                                              </ul>
                                            </div>
                                            <div class="card-body tab-content">
                                              <div :class="{
                                                'tab-pane': true,
                                                'active': jsonDataPatient.isQuit == '是'
                                              }" id="true">
                                                <div class="col-sm-5 mb-24">
                                                  <h5>戒烟年限(年)：</h5>
                                                  <input type="text" class="form-control wizard-required" id="quitYears"
                                                    name="quitYears" placeholder="请输入……"
                                                    :value="jsonDataPatient.quitYears" :readonly="editMessage"
                                                    @change="jsonDataPatient.quitYears = $event.target.value">
                                                  <div class="wizard-form-error">
                                                  </div>
                                                </div>
                                              </div>
                                              <div class="tab-pane" id="false">
                                                <div class="col-sm-5">
                                                </div>
                                              </div>
                                            </div>
                                          </div>

                                          <div :class="{
                                            'tab-pane': true,
                                            'active': jsonDataPatient.smokingType == '被动'
                                          }" id="passivity">
                                            <div class="col-sm-5 mb-24">
                                              <h5>吸烟年限(年)：</h5>
                                              <input type="text" class="form-control wizard-required"
                                                id="passivitySmokingYears" name="smokingYears" placeholder="请输入……"
                                                :value="jsonDataPatient.smokingYears" :readonly="editMessage"
                                                @change="jsonDataPatient.smokingYears = $event.target.value">
                                              <div class="wizard-form-error">
                                              </div>
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                      <div class="tab-pane" id="noSmoking">
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <h4 class="black p-0 mb-12">既往病史：</h4>
                                <div class="selectForm col-sm-3 mb-24">
                                  <div class="select">
                                    <select name="medicalHistory" id="medicalHistory" class="wizard-required"
                                      v-model="jsonDataPatient.medicalHistory" @change="updateMedicalHistory">
                                      <option selected="" disabled="" value="">
                                        点击选择
                                      </option>
                                      <option value="慢支" :selected="jsonDataPatient.medicalHistory == '慢支'"
                                        :disabled="editMessage">
                                        慢支
                                      </option>
                                      <option value="支气管哮喘" :selected="jsonDataPatient.medicalHistory == '支气管哮喘'"
                                        :disabled="editMessage">
                                        支气管哮喘
                                      </option>
                                      <option value="慢阻肺" :selected="jsonDataPatient.medicalHistory == '慢阻肺'"
                                        :disabled="editMessage">
                                        慢阻肺</option>
                                      <option value="肺结核" :selected="jsonDataPatient.medicalHistory == '肺结核'"
                                        :disabled="editMessage">
                                        肺结核</option>
                                      <option value="高血压" :selected="jsonDataPatient.medicalHistory == '高血压'"
                                        :disabled="editMessage">
                                        高血压</option>
                                      <option value="冠心病" :selected="jsonDataPatient.medicalHistory == '冠心病'"
                                        :disabled="editMessage">
                                        冠心病</option>
                                      <option value="糖尿病" :selected="jsonDataPatient.medicalHistory == '糖尿病'"
                                        :disabled="editMessage">
                                        糖尿病
                                      </option>
                                      <option value="肺心病" class="" :selected="jsonDataPatient.medicalHistory == '肺心病'"
                                        :disabled="editMessage">
                                        肺心病</option>
                                      <option value="肺间质性疾病" :selected="jsonDataPatient.medicalHistory == '肺间质性疾病'"
                                        :disabled="editMessage">
                                        肺间质性疾病
                                      </option>
                                      <option value="其他" :selected="jsonDataPatient.medicalHistory == '其他'"
                                        :disabled="editMessage">
                                        其他</option>
                                    </select>
                                  </div>
                                  <div class="wizard-form-error"></div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div :class="{
                'tab-pane': true,
                'fade': true,
                'show': changeView == 'recodeMessage',
                'active': changeView == 'recodeMessage'
              }" v-if="changeView == 'recodeMessage'" id="recodeMessage" role="tabpanel" aria-labelledby="today-tab">
                <div v-if="jsonPatientRecodeInfo.recodeMessageList !== undefined && jsonPatientRecodeInfo.recodeMessageList.length > 0">
                  <div class="row" id="recodeMessageList">
                    <div class="col-xl-5 blog-box bg-white light-shadow p-24 br-20 align-items-center col-lg-12 mb-12"
                      v-for="recodeMessage in jsonPatientRecodeInfo.recodeMessageList" :key="recodeMessage.reportId"
                      style="margin-left: 20px;">
                      <h4>第{{ recodeMessage.seconds }}次随访（肺结节）</h4>
                      <div class="content-box" style="margin-top: 10px;">
                        <div class="d-flex gap-16 mb-24">
                          <div class="d-flex align-items-center gap-8"><img :src="userBk">
                            <h5>医生ID：</h5>
                            <p>{{ recodeMessage.doctorName }}</p>
                          </div>
                          <div class="vr-line"></div>
                          <div class="d-flex align-items-center gap-8"><img :src="calender">
                            <h5>复查日期：</h5>
                            <p class="h-16 dark-gray">{{ recodeMessage.nextTime }}</p>
                          </div>
                        </div>
                        <!-- <p class="mb-24 dark-gray">随访建议：{{ recodeMessage.suggestion }}</p> -->
                        <div class="d-flex justify-content-center">
                          <button class="small-pad cus-btn" style="margin-right: 20px;" @click="removeRecodeMessage(recodeMessage.reportId)">删除</button>
                          <router-link :to="'/detailRecodeMessage?reportId=' + recodeMessage.reportId"
                            class="small-pad cus-btn ">查看具体信息</router-link>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="paginations mt-8">
                    <ul class="unstyled" id="pageList">
                      <a class="h6 fw-600 mb-0" id="pageFirst" @click="recodePageFront">
                        <li>&lt;</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageSecond">
                        <li>{{ jsonSearchRecodeInfo.pageIndex }}</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageThird" @click="recodePageBack">
                        <li>&gt;</li>
                      </a>
                    </ul>
                  </div>
                </div>
                <div v-if="jsonPatientRecodeInfo.recodeMessageList == undefined || jsonPatientRecodeInfo.recodeMessageList.length == 0">
                  <p class="mb-24 dark-gray" style="text-align: center; font-size: 30px">无随访记录</p>
                </div>
              </div>
              <div :class="{
                'tab-pane': true,
                'fade': true,
                'show': changeView == 'langCancerMessage',
                'active': changeView == 'langCancerMessage'
                }" v-if="changeView == 'langCancerMessage'" id="langCancerMessage" role="tabpanel"
                aria-labelledby="today-tab">
                <div v-if="jsonPatientLangCancer.langCancerMessageList !== undefined && jsonPatientLangCancer.langCancerMessageList.length > 0">
                  <div class="row" id="langCancerMessageList">
                    <div class="col-xl-5 blog-box bg-white light-shadow p-24 br-20 align-items-center col-lg-12 mb-12"
                      v-for="langCancerMessage in jsonPatientLangCancer.langCancerMessageList"
                      :key="langCancerMessage.reportId" style="margin-left: 20px;">
                      <h4>第{{ langCancerMessage.seconds }}次随访（肺癌）</h4>
                      <div class="content-box" style="margin-top: 10px;">
                        <div class="d-flex gap-16 mb-24">
                          <div class="d-flex align-items-center gap-8"><img :src="userBk">
                            <h5>医生ID：</h5>
                            <p>{{ langCancerMessage.doctorId }}</p>
                          </div>
                          <div class="vr-line"></div>
                          <div class="d-flex align-items-center gap-8"><img :src="calender">
                            <h5>诊断日期：</h5>
                            <p class="h-16 dark-gray">{{ langCancerMessage.diagnoseTime }}</p>
                          </div>
                        </div>
                        <p class="mb-24 dark-gray">诊断建议：{{ langCancerMessage.diagnosticLogout }}</p>
                        <p class="mb-24 dark-gray">治疗方式：{{ langCancerMessage.therapyMethod }}</p>
                        <p class="mb-24 dark-gray">疗效：{{ langCancerMessage.curativeEffect }}</p>
                        <div class="d-flex justify-content-center">
                          <button class="small-pad cus-btn" style="margin-right: 20px;" @click="removeLangCancerMessage(langCancerMessage.reportId)">删除</button>
                          <router-link :to="'/detailLangCancerMessage?reportId=' + langCancerMessage.reportId"
                            class="small-pad cus-btn ">查看具体信息</router-link>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="paginations mt-8">
                    <ul class="unstyled" id="pageList">
                      <a class="h6 fw-600 mb-0" id="pageFirst" @click="langCancerPageFront">
                        <li>&lt;</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageSecond">
                        <li>{{ jsonSearchLangCancer.pageIndex }}</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageThird" @click="langCancerPageBack">
                        <li>&gt;</li>
                      </a>
                    </ul>
                  </div>
                </div>
                <div v-if="jsonPatientLangCancer.langCancerMessageList == undefined || jsonPatientLangCancer.langCancerMessageList.length == 0">
                  <p class="mb-24 dark-gray" style="text-align: center; font-size: 30px">无肺癌记录</p>
                </div>
              </div>
            </div>
          </section>
          <!-- Blog Area End -->
        </div>
      </div>
      <!-- Main Content End -->
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
  background: url(../assets/image/title-banner.png);
}
</style>