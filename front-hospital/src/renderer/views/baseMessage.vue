<script setup>
import { ref, onMounted } from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';

//定义表单初始值
let flag = true;
let studyUnit = ref('新疆医科大学第四临床自治区中医医院');
let createTime = ref('');
let doctorId = ref('');
let source = ref('住院号');
let admissionNumber = ref('');
// let clinicNo = ref('');
let patientGender = ref('');
let patientGroup = ref('');
let patientAge = ref('');
let patientName = ref('');
let patientWork = ref('');
let patientEduLevel = ref('');
let patientMarriage = ref('');
let patientPregnancy = ref('');
let patientMalignant = ref('');
let patientAddress = ref('');
let patientPhone = ref('');
let patientAllergy = ref('');
let drugName = ref('');
let isSmoke = ref('');
let smokingType = ref('');
let smokingYears = ref('');
let smokingAmount = ref('');
let isQuit = ref('');
let quitYears = ref('');
let medicalHistory = ref('');
let jsonBaseFormDatas = ref({});
let jsonAllergyFormDatas = ref({});
let jsonSmokingFormDatas = ref({});
let jsonFormDatas = {};
let patientType = ref('');
onMounted(() => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    createTime.value = `${year}-${month}-${day}`;
    flag = true;
    patientAllergy.value = "是";
    isSmoke.value = "是";
    smokingType.value = "主动";
    isQuit.value = "是";
    medicalHistory.value = "";
});
//获取基本信息表的信息
const baseForm = () => {
    let flag1 = true;
    let baseFormData = {
        "studyUnit": studyUnit.value,
        "createTime": createTime.value,
        "doctorId": doctorId.value,
        "source": source.value,
        "admissionNumber": admissionNumber.value,
        // "clinicNo": clinicNo.value,
        "patientGender": patientGender.value,
        "patientGroup": patientGroup.value,
        "patientAge": patientAge.value,
        "patientName": patientName.value,
        "patientWork": patientWork.value,
        "patientEduLevel": patientEduLevel.value,
        "patientMarriage": patientMarriage.value,
        "patientPregnancy": patientPregnancy.value,
        "patientMalignant": patientMalignant.value,
        "patientAddress": patientAddress.value,
        "patientPhone": patientPhone.value,
        "patientType": patientType.value,
    }
    if (baseFormData.createTime === "" || baseFormData.doctorId === "" || baseFormData.patientName === "" || baseFormData.patientType === "" || baseFormData.admissionNumber === ""){
        flag1 = false;
        return flag1;
    }
    jsonBaseFormDatas.value = baseFormData;
    return flag1;
}
//获取过敏史信息表的信息
const checkAllergy = (patientAllergyValue) => {
    patientAllergy.value = patientAllergyValue;
}
const allergyForm = (patientAllergyValue) => {
    let flag2 = true;
    let allergyFormData = {};
    if (patientAllergyValue === "是") {
        patientAllergy.value = "是";
        allergyFormData.patientAllergy = "是";
        allergyFormData.drugName = drugName.value;
    } else {
        patientAllergy.value = "否";
        allergyFormData.patientAllergy = "否";
    }
    for (let key in allergyFormData) {
        if (allergyFormData[key] === "") {
            flag2 = false;
            return flag2;
        }
    }
    // console.log(allergyFormData);
    jsonAllergyFormDatas.value = allergyFormData;
    return flag2;
}
//确定病例来源
const checkSource = (sourceValue) => {
    source.value = sourceValue;
}
//获取吸烟史信息表的信息
const checkSmoking = (isSmokeValue) => {
    isSmoke.value = isSmokeValue;
}
const checkSmokingType = (smokingTypeValue) => {
    smokingType.value = smokingTypeValue;
}
const checkIsQuit = (isQuitValue) => {
    isQuit.value = isQuitValue;
}
const smokingForm = (isSmokeValue, smokingTypeValue, smokingYearsValue, smokingAmountValue, isQuitValue, quitYearsValue) => {
    let flag3 = true;
    let smokingFormData = {};
    if (isSmokeValue === "是") {
        isSmoke.value = "是";
        smokingFormData.isSmoke = isSmoke.value;
        if (smokingTypeValue === "主动") {
            smokingType.value = "主动";
            smokingFormData.smokingType = smokingType.value;
            smokingFormData.smokingYears = smokingYears.value;
            smokingFormData.smokingAmount = smokingAmount.value;
            if (isQuitValue === "是") {
                isQuit.value = "是";
                smokingFormData.isQuit = isQuit.value;
                smokingFormData.quitYears = quitYears.value;
            } else {
                isQuit.value = "否";
                smokingFormData.isQuit = isQuit.value;
            }
        } else {
            smokingType.value = "被动";
            smokingFormData.smokingType = smokingType.value;
            smokingFormData.smokingYears = smokingYears.value;
        }
    } else {
        isSmoke.value = "否";
        smokingFormData.isSmoke = isSmoke.value;
    }
    for (let key in smokingFormData) {
        if (smokingFormData[key] === "") {
            flag3 = false;
            return flag3;
        }
    }
    jsonSmokingFormDatas.value = smokingFormData;
    return flag3;
}

// 获取患者详情数据
const dataDetailPatient = async (patientId) => {
  try {
    const response = await axios.get("/api/patient/getById/" + patientId);
    if (response.data.result === true) {
      console.log(response.data.data);
      return response.data.data;
    } else {
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    return null;
  }
}
// 避免二次提交flag
let submitFlag = false;
//提交表单
const submitForm = async (event) => {
    event.preventDefault();
    if (submitFlag) {
        ElMessage({
            type: 'warning',
            message: '请勿重复提交！'
        });
        return;
    }
    if (admissionNumber.value !== ""){
        let patientMessage = await dataDetailPatient(admissionNumber.value);
        if (patientMessage !== null) {
            ElMessage({
                type: 'warning',
                message: '该患者已有基本信息，请检查病案号！'
            });
            return;
        }
    }
    flag = baseForm();
    allergyForm(patientAllergy.value);
    smokingForm(isSmoke.value, smokingType.value, smokingYears.value, smokingAmount.value, isQuit.value, quitYears.value);
    for (let key in jsonBaseFormDatas.value) {
        jsonFormDatas[key] = jsonBaseFormDatas.value[key];
    }
    for (let key in jsonAllergyFormDatas.value) {
        jsonFormDatas[key] = jsonAllergyFormDatas.value[key];
    }
    for (let key in jsonSmokingFormDatas.value) {
        jsonFormDatas[key] = jsonSmokingFormDatas.value[key];
    }
    jsonFormDatas.medicalHistory = medicalHistory.value;
    if (flag === true) {
        messageLoad();
    } else {
        ElMessage({
            type: 'error',
            message: '填表日期、调查人员ID、患者姓名、患者类型、住院号或门诊号不能为空！'
        });
        return;
    }
    submitFlag = true;
}
//表单数据上传
const messageLoad = () => {
    console.log(jsonFormDatas)
    axios.post("/api/patient/add", jsonFormDatas).then(function (response) {
        if (response.data.result === true) {
            ElMessage({
                type: 'success',
                message: '提交成功！'
            });
        }
    }).catch(function (error) {
        ElMessage({
            type: 'error',
            message: '提交失败！'
        });
    });
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
                <h6 style="padding: 0px 110px;">>基本信息录入 (本人承诺患者以上资料仅用作研究，不传播、不泄露)</h6>
                <!-- Flight-Booking Start -->
                <section class="flight-booking">
                    <div class="container-fluid" id="app">
                        <div class="row">
                            <div class="col-xl-12">
                                <div class="booking-form">
                                    <form action="#" id="baseForm" method="post" role="form"
                                        class="contact-form form-wizard">
                                        <div class="wizard-content overflow-visible mb-12">
                                            <fieldset id="baseMessageForm" class="tab-pane show wizard-fieldset p-0">
                                                <div id="wizardValidator">
                                                    <div class="detail-form mb-32">
                                                        <h4 class="black p-0 mb-12">基本资料表：</h4>
                                                        <div class="row ">
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>研究单位：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="studyUnit" name="studyUnit" readonly
                                                                    value="新疆医科大学第四临床自治区中医医院" v-model="studyUnit">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>填表日期：</h5>
                                                                <input type="date" class="form-control wizard-required"
                                                                    id="createTime" name="createTime" value=""
                                                                    v-model="createTime">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>调查人员ID：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="doctorId" name="doctorId" placeholder="请输入……"
                                                                    value="" v-model="doctorId">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                        </div>
                                                        <div class="row ">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5>病例来源：</h5>
                                                                    <ul class="nav nav-tabs card-header-tabs "
                                                                        data-bs-tabs="tabs">
                                                                        <li class="nav-item">
                                                                            <a href="#admissionNumberPane"
                                                                                :class="{
                                                                                    'cus-btn': true,
                                                                                    'primary-light': true,
                                                                                    'primary': true,
                                                                                    'active': source === '住院号'
                                                                                }"
                                                                                aria-current="true" data-bs-toggle="tab"
                                                                                @click="checkSource('住院号')">
                                                                                住院号
                                                                            </a>
                                                                        </li>
                                                                        <li class="nav-item">
                                                                            <a href="#clinicNoPane"
                                                                                :class="{
                                                                                    'cus-btn': true,
                                                                                    'primary-light': true,
                                                                                    'primary': true,
                                                                                    'active': source === '门诊号'
                                                                                }"
                                                                                aria-current="false"
                                                                                data-bs-toggle="tab"
                                                                                @click="checkSource('门诊号')">
                                                                                门诊号
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div class="card-body tab-content mb-12">
                                                                    <div class="tab-pane active" id="admissionNumberPane">
                                                                        <div class="col-sm-5">
                                                                            <h5>身份证号：</h5>
                                                                            <input type="text"
                                                                                class="form-control wizard-required"
                                                                                id="admissionNumber" name="admissionNumber"
                                                                                placeholder="请输入……" value=""
                                                                                v-model="admissionNumber">
                                                                            <div class="wizard-form-error"></div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="tab-pane" id="clinicNoPane">
                                                                        <div class="col-sm-5">
                                                                            <h5>身份证号：</h5>
                                                                            <input type="text"
                                                                                class="form-control wizard-required"
                                                                                id="clinicNo" name="admissionNumber"
                                                                                placeholder="请输入……" value=""
                                                                                v-model="admissionNumber">
                                                                            <div class="wizard-form-error"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <h5>性别：</h5>
                                                                <div class="custom-control mb-12"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientGender"
                                                                            class="custom-control-input" id="man"
                                                                            value="男" v-model="patientGender">
                                                                        <label class="custom-control-label"
                                                                            for="man">男</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientGender"
                                                                            class="custom-control-input" id="women"
                                                                            value="女" v-model="patientGender">
                                                                        <label class="custom-control-label"
                                                                            for="women">女</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-5">
                                                                <h5>患者类型：</h5>
                                                                <div class="custom-control mb-12"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientType"
                                                                            class="custom-control-input" id="肺结节"
                                                                            value="肺结节" v-model="patientType">
                                                                        <label class="custom-control-label"
                                                                            for="肺结节">肺结节</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1; margin-left: -50px;">
                                                                        <input type="radio" name="patientType"
                                                                            class="custom-control-input" id="肺结节转肺癌"
                                                                            value="肺结节转肺癌" v-model="patientType">
                                                                        <label class="custom-control-label"
                                                                            for="肺结节转肺癌">肺结节转肺癌</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientType"
                                                                            class="custom-control-input" id="肺癌"
                                                                            value="肺癌" v-model="patientType">
                                                                        <label class="custom-control-label"
                                                                            for="肺癌">肺癌</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-sm-2 mb-12">
                                                                <h5>民族：</h5>
                                                                <input type="text" name="patientGroup"
                                                                    class="form-control wizard-required"
                                                                    id="patientGroup" value="" placeholder="请输入……"
                                                                    v-model="patientGroup">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="col-sm-2 mb-12">
                                                                <h5>年龄(岁)：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="patientAge" name="patientAge"
                                                                    placeholder="请输入……" value="" v-model="patientAge">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>姓名：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="patientName" name="patientName"
                                                                    placeholder="请输入……" value="" v-model="patientName">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <ul class="nav nav-tabs card-header-tabs "
                                                                        data-bs-tabs="tabs">
                                                                        <li class="nav-item">
                                                                            <a href="#teshu"
                                                                                class="cus-btn primary-light primary active"
                                                                                aria-current="true"
                                                                                data-bs-toggle="tab">
                                                                                特殊职业：
                                                                            </a>
                                                                        </li>
                                                                        <li class="nav-item">
                                                                            <a href="#feiteshu"
                                                                                class="cus-btn primary-light primary"
                                                                                aria-current="false"
                                                                                data-bs-toggle="tab">
                                                                                非特殊职业：
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div class="card-body tab-content">
                                                                    <div class="tab-pane active" id="teshu">
                                                                        <div class="custom-control mb-12"
                                                                            style="display: flex;">
                                                                            <div class=" radio-button" style="flex: 1;">
                                                                                <input type="radio" name="patientWork"
                                                                                    class="custom-control-input"
                                                                                    id="miner" value="矿工"
                                                                                    v-model="patientWork">
                                                                                <label class="custom-control-label"
                                                                                    for="miner">矿工</label>
                                                                            </div>
                                                                            <div class=" radio-button" style="flex: 1;">
                                                                                <input type="radio" name="patientWork"
                                                                                    class="custom-control-input"
                                                                                    id="cementer" value="水泥工"
                                                                                    v-model="patientWork">
                                                                                <label class="custom-control-label"
                                                                                    for="cementer">水泥工</label>
                                                                            </div>
                                                                            <div class="radio-button" style="flex: 1;">
                                                                                <input type="radio" name="patientWork"
                                                                                    class="custom-control-input"
                                                                                    id="spinner" value="纺织工"
                                                                                    v-model="patientWork">
                                                                                <label class="custom-control-label"
                                                                                    for="spinner">纺织工</label>
                                                                            </div>
                                                                            <div class="radio-button" style="flex: 1;">
                                                                                <input type="radio" name="patientWork"
                                                                                    class="custom-control-input"
                                                                                    id="other" value="其它"
                                                                                    v-model="patientWork">
                                                                                <label class="custom-control-label"
                                                                                    for="other">其它</label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="tab-pane" id="feiteshu">
                                                                        <form action="#" method="post">
                                                                            <div class="custom-control mb-12"
                                                                                style="display: flex;">
                                                                                <div class=" radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="woker" value="工人"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="woker">工人</label>
                                                                                </div>
                                                                                <div class=" radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="farmer" value="农民"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="farmer">农民</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="intellectual"
                                                                                        value="知识分子"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="intellectual">知识分子</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="custodian" value="管理人员"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="custodian">管理人员</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="service" value="服务行业"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="service">服务行业</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="retirement"
                                                                                        value="离退休"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="retirement">离退休</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="unemployed"
                                                                                        value="无业"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="unemployed">无业</label>
                                                                                </div>
                                                                                <div class="radio-button"
                                                                                    style="flex: 1;">
                                                                                    <input type="radio"
                                                                                        name="patientWork"
                                                                                        class="custom-control-input"
                                                                                        id="laid_off" value="下岗"
                                                                                        v-model="patientWork">
                                                                                    <label class="custom-control-label"
                                                                                        for="laid_off">下岗</label>
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
                                                                <div class="custom-control mb-12"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientEduLevel"
                                                                            class="custom-control-input" id="elementary"
                                                                            value="小学及以下"
                                                                            v-model="patientEduLevel">
                                                                        <label class="custom-control-label"
                                                                            for="elementary">小学及以下</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientEduLevel"
                                                                            class="custom-control-input" id="secondary"
                                                                            value="初中、高中及中专" v-model="patientEduLevel">
                                                                        <label class="custom-control-label"
                                                                            for="secondary">初中、高中及中专</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientEduLevel"
                                                                            class="custom-control-input" id="college+"
                                                                            value="大专、本科及以上" v-model="patientEduLevel">
                                                                        <label class="custom-control-label"
                                                                            for="college+">大专、本科及以上</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-sm-10">
                                                                <h5>婚姻状况：</h5>
                                                                <div class="custom-control mb-12"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMarriage"
                                                                            class="custom-control-input" id="unmarried"
                                                                            value="未婚" v-model="patientMarriage">
                                                                        <label class="custom-control-label"
                                                                            for="unmarried">未婚</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMarriage"
                                                                            class="custom-control-input" id="beMarried"
                                                                            value="已婚" v-model="patientMarriage">
                                                                        <label class="custom-control-label"
                                                                            for="beMarried">已婚</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMarriage"
                                                                            class="custom-control-input" id="separate"
                                                                            value="分居" v-model="patientMarriage">
                                                                        <label class="custom-control-label"
                                                                            for="separate">分居</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMarriage"
                                                                            class="custom-control-input" id="divorce"
                                                                            value="离异" v-model="patientMarriage">
                                                                        <label class="custom-control-label"
                                                                            for="divorce">离异</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMarriage"
                                                                            class="custom-control-input"
                                                                            id="spouseBereaved" value="丧偶"
                                                                            v-model="patientMarriage">
                                                                        <label class="custom-control-label"
                                                                            for="spouseBereaved">丧偶</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>妊娠史：</h5>
                                                                <div class="custom-control"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientPregnancy"
                                                                            class="custom-control-input"
                                                                            id="havePregnancy" value="无孕产"
                                                                            v-model="patientPregnancy">
                                                                        <label class="custom-control-label"
                                                                            for="havePregnancy">无孕产</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientPregnancy"
                                                                            class="custom-control-input"
                                                                            id="noHavePregnancy" value="有孕产"
                                                                            v-model="patientPregnancy">
                                                                        <label class="custom-control-label"
                                                                            for="noHavePregnancy">有孕产</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-3 mb-12">
                                                                <h5>恶性肿瘤家族史：</h5>
                                                                <div class="custom-control"
                                                                    style="display: flex; margin-top: 5px;">
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMalignant"
                                                                            class="custom-control-input"
                                                                            id="noHaveMalignancy"
                                                                            value="无"
                                                                            v-model="patientMalignant">
                                                                        <label class="custom-control-label"
                                                                            for="noHaveMalignancy">无</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientMalignant"
                                                                            class="custom-control-input"
                                                                            id="haveMalignancy" value="有"
                                                                            v-model="patientMalignant">
                                                                        <label class="custom-control-label"
                                                                            for="haveMalignancy">有</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-sm-6">
                                                                <h5>家庭住址：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="patientAddress" name="patientAddress"
                                                                    placeholder="请输入……" value=""
                                                                    v-model="patientAddress">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                            <div class="col-sm-5">
                                                                <h5>联系电话：</h5>
                                                                <input type="text" class="form-control wizard-required"
                                                                    id="patientPhone" name="patientPhone"
                                                                    placeholder="请输入……" value="" v-model="patientPhone">
                                                                <div class="wizard-form-error"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <fieldset id="alleryForm" class="tab-pane show wizard-fieldset p-0">
                                                <div id="wizardValidator">
                                                    <div class="detail-form mb-12">
                                                        <h4 class="black p-0 mb-12">药物过敏史：</h4>
                                                        <div class="row ">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5>有无过敏史：</h5>
                                                                    <ul class="nav nav-tabs card-header-tabs "
                                                                        data-bs-tabs="tabs">
                                                                        <li class="nav-item">
                                                                            <a href="#haveAllergy"
                                                                                class="cus-btn primary-light primary active"
                                                                                aria-current="true" data-bs-toggle="tab"
                                                                                id="haveAllergyA"
                                                                                @click="checkAllergy('是')">
                                                                                有
                                                                            </a>
                                                                        </li>
                                                                        <li class="nav-item">
                                                                            <a href="#noHaveAllergy"
                                                                                class="cus-btn primary-light primary "
                                                                                aria-current="false"
                                                                                data-bs-toggle="tab" id="noHaveAllergyA"
                                                                                @click="checkAllergy('否')">
                                                                                无
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div class="card-body tab-content">
                                                                    <div class="tab-pane active" id="haveAllergy">
                                                                        <div class="col-sm-5">
                                                                            <h5>过敏药物名称：</h5>
                                                                            <input type="text"
                                                                                class="form-control wizard-required"
                                                                                id="drugName" name="drugName"
                                                                                placeholder="请输入……" value=""
                                                                                v-model="drugName">
                                                                            <div class="wizard-form-error"></div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="tab-pane" id="noHaveAllergy"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <fieldset id="smokingForm" class="tab-pane show wizard-fieldset p-0">
                                                <div id="wizardValidator">
                                                    <div class="detail-form mb-12">
                                                        <h4 class="black p-0 mb-12">吸烟史：</h4>
                                                        <div class="row">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5>有无吸烟史：</h5>
                                                                    <ul class="nav nav-tabs card-header-tabs "
                                                                        data-bs-tabs="tabs">
                                                                        <li class="nav-item">
                                                                            <a href="#smoking"
                                                                                class="cus-btn primary-light primary active"
                                                                                aria-current="true" data-bs-toggle="tab"
                                                                                id="smokingA"
                                                                                @click="checkSmoking('是')">
                                                                                有
                                                                            </a>
                                                                        </li>
                                                                        <li class="nav-item">
                                                                            <a href="#noSmoking"
                                                                                class="cus-btn primary-light primary "
                                                                                aria-current="false"
                                                                                data-bs-toggle="tab" id="noSmokingA"
                                                                                @click="checkSmoking('否')">
                                                                                无
                                                                            </a>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div class="card-body tab-content">
                                                                    <div class="tab-pane active" id="smoking">
                                                                        <div class="card-header">
                                                                            <h5>吸烟方式：</h5>
                                                                            <ul class="nav nav-tabs card-header-tabs "
                                                                                data-bs-tabs="tabs">
                                                                                <li class="nav-item">
                                                                                    <a href="#initative"
                                                                                        class="cus-btn primary-light primary active"
                                                                                        aria-current="true"
                                                                                        data-bs-toggle="tab"
                                                                                        id="initativeWay"
                                                                                        @click="checkSmokingType('主动')">
                                                                                        主动
                                                                                    </a>
                                                                                </li>
                                                                                <li class="nav-item">
                                                                                    <a href="#passivity"
                                                                                        class="cus-btn primary-light primary"
                                                                                        aria-current="false"
                                                                                        data-bs-toggle="tab"
                                                                                        id="passivityWay"
                                                                                        @click="checkSmokingType('被动')">
                                                                                        被动
                                                                                    </a>
                                                                                </li>
                                                                            </ul>
                                                                        </div>
                                                                        <div class="card-body tab-content">
                                                                            <div class="tab-pane active" id="initative">
                                                                                <div class="col-sm-5 mb-12">
                                                                                    <div class="col-sm-5">
                                                                                        <h5>吸烟年限(年)：</h5>
                                                                                        <input type="text"
                                                                                            class="form-control wizard-required"
                                                                                            id="initativeSmokingYears"
                                                                                            name="smokingYears"
                                                                                            placeholder="请输入……" value=""
                                                                                            v-model="smokingYears">
                                                                                        <div class="wizard-form-error">
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="col-sm-5 mb-12">
                                                                                    <h5>吸烟量(支/天)：</h5>
                                                                                    <input type="text"
                                                                                        class="form-control wizard-required"
                                                                                        id="smokingAmount"
                                                                                        name="smokingAmount"
                                                                                        placeholder="请输入……" value=""
                                                                                        v-model="smokingAmount">
                                                                                    <div class="wizard-form-error">
                                                                                    </div>
                                                                                </div>
                                                                                <div class="card-header">
                                                                                    <h5>是否戒烟：</h5>
                                                                                    <ul class="nav nav-tabs card-header-tabs "
                                                                                        data-bs-tabs="tabs">
                                                                                        <li class="nav-item">
                                                                                            <a href="#true"
                                                                                                class="cus-btn primary-light primary active"
                                                                                                aria-current="true"
                                                                                                data-bs-toggle="tab"
                                                                                                id="quitSmoking"
                                                                                                @click="checkIsQuit('是')">
                                                                                                是
                                                                                            </a>
                                                                                        </li>
                                                                                        <li class="nav-item">
                                                                                            <a href="#false"
                                                                                                class="cus-btn primary-light primary"
                                                                                                aria-current="false"
                                                                                                data-bs-toggle="tab"
                                                                                                id="noQuitSmoking"
                                                                                                @click="checkIsQuit('否')">
                                                                                                否
                                                                                            </a>
                                                                                        </li>
                                                                                    </ul>
                                                                                </div>
                                                                                <div class="card-body tab-content">
                                                                                    <div class="tab-pane active"
                                                                                        id="true">
                                                                                        <div class="col-sm-5">
                                                                                            <h5>戒烟年限(年)：</h5>
                                                                                            <input type="text"
                                                                                                class="form-control wizard-required"
                                                                                                id="quitYears"
                                                                                                name="quitYears"
                                                                                                placeholder="请输入……"
                                                                                                value=""
                                                                                                v-model="quitYears">
                                                                                            <div
                                                                                                class="wizard-form-error">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="tab-pane" id="false">
                                                                                        <div class="col-sm-5">
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                            <div class="tab-pane" id="passivity">
                                                                                <div class="col-sm-5">
                                                                                    <div class="col-sm-5">
                                                                                        <h5>吸烟年限(年)：</h5>
                                                                                        <input type="text"
                                                                                            class="form-control wizard-required"
                                                                                            id="passivitySmokingYears"
                                                                                            name="smokingYears"
                                                                                            placeholder="请输入……" value=""
                                                                                            v-model="smokingYears">
                                                                                        <div class="wizard-form-error">
                                                                                        </div>
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
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <fieldset id="pastMedicalHistoryForm"
                                                class="tab-pane show wizard-fieldset p-0">
                                                <div id="wizardValidator">
                                                    <div class="detail-form mb-12">
                                                        <h4 class="black p-0 mb-12">既往病史：</h4>
                                                        <div class="selectForm col-sm-3">
                                                            <div class="select">
                                                                <select name="medicalHistory" id="medicalHistory"
                                                                    class="wizard-required" v-model="medicalHistory">
                                                                    <option selected="" disabled="" value="">点击选择
                                                                    </option>
                                                                    <option value="慢支">慢支</option>
                                                                    <option value="支气管哮喘">支气管哮喘</option>
                                                                    <option value="慢阻肺">慢阻肺</option>
                                                                    <option value="肺结核">肺结核</option>
                                                                    <option value="高血压">高血压</option>
                                                                    <option value="冠心病">冠心病</option>
                                                                    <option value="糖尿病">糖尿病</option>
                                                                    <option value="肺心病">肺心病</option>
                                                                    <option value="肺间质性疾病">肺间质性疾病
                                                                    </option>
                                                                    <option value="其他">其他</option>
                                                                </select>
                                                            </div>
                                                            <div class="wizard-form-error"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <div class="col-sm-2" style="margin: 0 auto;">
                                                <input style="width: 200px;" type="submit" class="cus-btn"
                                                    @click="submitForm" value="提交"></input>
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


            <!-- Footer Start -->

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

header {
    background: url(../assets/image/title-banner.png);
}
</style>