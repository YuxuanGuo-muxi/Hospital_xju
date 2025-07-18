<script lang="ts" setup>
import { ref, onMounted, getCurrentInstance } from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import userBk from '@/renderer/assets/picture/user-bk.png'
import calender from '@/renderer/assets/picture/calender.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';
import { useStore } from 'vuex' // 引入useStore 方法

const store = useStore()  // 该方法用于返回store 实例
//患者
let jsonPatientList = ref({});
let patientPage = ref({
  "pageIndex": 1,
  "pageSize": 6
});
//医生账户
let jsonDoctorList = ref({});
let doctorPage = ref({
  "pageIndex": 1,
  "pageSize": 6
});
//医生申请账户
let jsonDoctorApplyList = ref({});
let doctorApplyPage = ref({
  "pageIndex": 1,
  "pageSize": 6
});

//标签
let jsonTagList = ref({});
let tagPage = ref({
  "pageIndex": 1,
  "pageSize": 8
});

//挂载函数
onMounted(async () => {
  let patientList = await dataPatientList(patientPage.value);
  if (patientList !== null) {
    jsonPatientList.value = patientList;
  }

  let doctorList = await dataDoctorList(doctorPage.value);
  if (doctorList !== null) {
    jsonDoctorList.value = doctorList;
  }

  let doctorApplyList = await dataDoctorApplyList(doctorApplyPage.value);
  if (doctorApplyList !== null) {
    jsonDoctorApplyList.value = doctorApplyList;
  }

  let tagList = await dataTagList(tagPage.value);
  if (tagList !== null) {
    jsonTagList.value = tagList;
  }
  // console.log(jsonTagList.value);
});

//获取患者列表
const dataPatientList = async (patientPage) => {
  try {
    const response = await axios.get("/api/admin/getPatientList/" + patientPage.pageIndex + "/" + patientPage.pageSize);
    if (response.data.result === true) {
      console.log(response.data);
      let patientList = {
        "patientList": []
      }
      response.data.data.forEach(function (patient) {
        patientList.patientList.push(patient);
      })
      return patientList;
    } else {
      ElMessage({
        type: 'error',
        message: '患者列表获取失败！'
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
}
//删除患者信息
const removePatient = async (patientId) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const confirm = await ElMessageBox.confirm('确定要删除该患者信息吗（会将所有相关信息删除,包括随访记录以及癌症记录）？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).catch(()=>{
      ElMessage({
        type: 'info',
        message: '取消删除！'
      });
    })  ;
    if (confirm !== 'confirm') {
      return;
    }
    const response = await axios.get("api/admin/removePatient/" + patientId);
    console.log(response.data);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '患者信息删除成功！'
      });
      jsonPatientList.value = await dataPatientList(patientPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '患者信息删除失败！'
      });
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
  }
}
//患者信息页码
const patientPageFront = async () => {
  if (patientPage.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    return;
  }
  patientPage.value.pageIndex -= 1;
  jsonPatientList.value = await dataPatientList(patientPage.value);
}
const patientPageBack = async () => {
  let flag = true;
  patientPage.value.pageIndex += 1;
  let patientList = await dataPatientList(patientPage.value);
  if (patientList.patientList.length === 0) {
    patientPage.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了'
    });
    return;
  }
  jsonPatientList.value = patientList;
}
//获取医生列表
const dataDoctorList = async (doctorPage) => {
  try {
    const response = await axios.get("/api/admin/getDoctorList/" + doctorPage.pageIndex + "/" + doctorPage.pageSize);
    if (response.data.result === true) {
      let doctorList = {
        "doctorList": []
      }
      response.data.data.forEach(function (doctor) {
        doctorList.doctorList.push(doctor);
      })
      return doctorList;
    } else {
      ElMessage({
        type: 'error',
        message: '医生列表获取失败！'
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
}
//删除医生账户
const removeDoctor = async (doctorId) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const confirm = await ElMessageBox.confirm('确定要删除该医生账户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).catch(()=>{
      ElMessage({
        type: 'info',
        message: '取消删除！'
      });
    })  ;
    if (confirm !== 'confirm') {
      return;
    }
    const response = await axios.get("/api/admin/removeDoctor/" + doctorId);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '医生账户删除成功！'
      });
      jsonDoctorList.value = await dataDoctorList(doctorPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '医生账户删除失败！'
      });
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
  }
}
//医生账户页码
const doctorPageFront = async () => {
  if (doctorPage.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    return;
  }
  doctorPage.value.pageIndex -= 1;
  jsonDoctorList.value = await dataDoctorList(doctorPage.value);
}
const doctorPageBack = async () => {
  doctorPage.value.pageIndex += 1;
  // console.log(doctorPage.value);
  let doctorList = await dataDoctorList(doctorPage.value);
  if (doctorList.doctorList.length === 0) {
    doctorPage.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    return;
  }
  jsonDoctorList.value = doctorList;
}
//获取医生申请账户
const dataDoctorApplyList = async (doctorApplyPage) => {
  try {
    const response = await axios.get("/api/admin/getApplyList/" + doctorApplyPage.pageIndex + "/" + doctorApplyPage.pageSize);
    console.log(response.data);
    if (response.data.result === true) {
      let DoctorApplyList = {
        "doctorApplyList": []
      }
      response.data.data.forEach(function (doctorApply) {
        DoctorApplyList.doctorApplyList.push(doctorApply);
      })
      return DoctorApplyList;
    } else {
      ElMessage({
        type: 'error',
        message: '医生申请列表获取失败！'
      });
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("医生申请列表获取失败！");
    return null;
  }
}
//同意医生申请
const agreeApply = async (doctorAccount) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const response = await axios.get("/api/admin/agreeApply/" + doctorAccount);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '医生账号申请已同意！'
      });
      // alert("医生账号申请已同意！");
      jsonDoctorList.value = await dataDoctorList(doctorPage.value);
      jsonDoctorApplyList.value = await dataDoctorApplyList(doctorApplyPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '同意失败！'
      });
      // alert("同意失败！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
  }
}
//不同意医生申请
const noAgreeApply = async (doctorAccount) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const confirm = await ElMessageBox.confirm('确定要不同意该医生账户申请吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).catch(()=>{
      ElMessage({
        type: 'info',
        message: '取消不同意！'
      });
    });
    if (confirm !== 'confirm') {
      return;
    };
    const response = await axios.get("/api/admin/disAgreeApply/" + doctorAccount);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '医生账号申请已批回！'
      });
      // alert("医生账号申请已批回！");
      jsonDoctorApplyList.value = await dataDoctorApplyList(doctorApplyPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '不同意失败！'
      });
      // alert("不同意失败！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
  }
}
//医生申请页码列表
const doctorApplyPageFront = async () => {
  if (doctorApplyPage.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    // alert("已经是第一页了！");
    return;
  }
  doctorApplyPage.value.pageIndex -= 1;
  jsonDoctorApplyList.value = await dataDoctorApplyList(doctorApplyPage.value);
}
const doctorApplyPageBack = async () => {
  doctorApplyPage.value.pageIndex += 1;
  // console.log(doctorApplyPage.value);
  let doctorApplyList = await dataDoctorApplyList(doctorApplyPage.value);
  // console.log(doctorApplyList.doctorApplyList.length);
  if (doctorApplyList.doctorApplyList.length === 0) {
    doctorApplyPage.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    // alert("已经是最后一页了！");
    return;
  }
  jsonDoctorApplyList.value = doctorApplyList;
}
//获取标签列表
const dataTagList = async (tagPage) => {
  try {
    const response = await axios.get("/api/admin/getTagsList/" + tagPage.pageIndex + "/" + tagPage.pageSize);
    if (response.data.result === true) {
      let TagList = {
        "tagList": []
      }
      response.data.data.forEach(function (tag) {
        TagList.tagList.push(tag);
      })
      return TagList;
    } else {
      // alert("标签列表为空！");
      return null;
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("标签列表获取失败！");
    return null;
  }
}
//删除标签
const removeTag = async (tagId) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const confirm = await ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
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
    const response = await axios.get("/api/admin/removeTag/" + tagId);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '标签删除成功！'
      });
      // alert("标签删除成功！");
      jsonTagList.value = await dataTagList(tagPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '标签删除失败！'
      });
      // alert("标签删除失败！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
  }
}
//标签页码
const tagPageFront = async () => {
  if (tagPage.value.pageIndex === 1) {
    ElMessage({
      type: 'info',
      message: '已经是第一页了！'
    });
    return;
  }
  tagPage.value.pageIndex -= 1;
  // console.log(tagPage.value);
  jsonTagList.value = await dataTagList(tagPage.value);
}
const tagPageBack = async () => {
  tagPage.value.pageIndex += 1;
  // console.log(tagPage.value);
  let tagList = await dataTagList(tagPage.value);
  // console.log(tagList.tagList.length);
  if (tagList.tagList.length === 0) {
    tagPage.value.pageIndex -= 1;
    ElMessage({
      type: 'info',
      message: '已经是最后一页了！'
    });
    // alert("已经是最后一页了！");
    return;
  }
  jsonTagList.value = tagList;
}
//创造标签
let tagName = ref('');
const createTag = async (tagName) => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
  }
  try {
    const tag = {
      tagName: tagName,
      count: 0,
      tagDescription: tagName
    }
    const response = await axios.post("/api/admin/addTag", tag);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '标签创建成功！'
      });
      // alert("标签创建成功！");
      jsonTagList.value = await dataTagList(tagPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '标签创建失败！'
      });
      // alert("标签创建失败！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("标签创建失败！");
  }
}
const createButton = async () => {
  let flag = true;
  if (tagName.value === "") {
    ElMessage({
      type: 'error',
      message: '标签名称不能为空！'
    });
    // alert("标签名称不能为空！");
  } else {
    jsonTagList.value.tagList.forEach(function (tag) {
      console.log(tag.tagName);
      if (tag.tagName === tagName.value) {
        ElMessage({
          type: 'error',
          message: '标签已存在！'
        });
        // alert("标签已存在！");
        flag = false;
      }
    });
    if (flag) {
      await createTag(tagName.value);
    }
  }
}
//修改标签隐藏状态
const modifyTag = async (tagId, tagDisabled) => {
  try {
    const response = await axios.get("/api/admin/disabledTag/" + tagId);
    if (response.data.result === true) {
      ElMessage({
        type: 'success',
        message: '标签修改成功！'
      });
      // alert("标签修改成功！");
      jsonTagList.value = await dataTagList(tagPage.value);
    } else {
      ElMessage({
        type: 'error',
        message: '标签修改失败！'
      });
      // alert("标签修改失败！");
    }
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("标签修改失败！");
  }
}
//验证锁定状态
let status = ref(false);
const verify = () => {
  if (status.value) {
    ElMessageBox.prompt('请输入本账户的密码', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password'  // 设置输入框为密码输入框
    }).then(({ value }) => {
      try {
        let jsonDataLogin = {
          doctorId: store.state.account,
          password: value
        };
        console.log(jsonDataLogin);
        axios.post("/api/doctor/login", jsonDataLogin).then(response => {
          if (response.data.result === true) {
            ElMessage({
              type: 'success',
              message: '密码正确，解锁成功！'
            });
          } else {
            ElMessage({
              type: 'error',
              message: '密码错误，解锁失败！'
            });
            status.value = false;
          }
        }).catch(error => {
          console.log(error);
        });
      } catch (error) {
        console.log(error);
      }
    }).catch(() => {
      ElMessage({
        type: 'info',
        message: '取消输入'
      });
      status.value = false;
    });
  }
}
//确定解锁状态
const checkLock = () => {
  if (status.value === false) {
    ElMessage({
      type: 'warning',
      message: '请先解锁！'
    });
    return;
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
        <h6 style="padding: 0px 110px;">>管理</h6>
        <div class="page-content m-0">

          <!-- Blog Area Start -->
          <section class="news-blog mb-8">
            <div class="container-fluid">
              <div class="d-flex align-items-center">
                <ul class="nav nav-tabs mb-32 sal-animate" id="myTab" role="tablist" style="margin: 0;">
                  <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="patientsMessage-tab" data-bs-toggle="tab"
                      data-bs-target="#patientsMessage" type="button" role="tab" aria-controls="all"
                      aria-selected="true">患者信息管理</button>
                  </li>
                  <!-- <li class="vr-line"></li> -->
                  <li class="nav-item" role="presentation" v-if="store.state.identity1 === '管理员'">
                    <button class="nav-link" id="doctorsAccount-tab" data-bs-toggle="tab"
                      data-bs-target="#doctorsAccount" type="button" role="tab" aria-controls="today"
                      aria-selected="true">医生账户管理</button>
                  </li>
                  <li class="nav-item" role="presentation" v-if="store.state.identity1 === '管理员'">
                    <button class="nav-link" id="doctorsApplication-tab" data-bs-toggle="tab"
                      data-bs-target="#doctorsApplication" type="button" role="tab" aria-controls="today"
                      aria-selected="true">医生账户申请管理</button>
                  </li>
                  <li class="nav-item" role="presentation" v-if="store.state.identity1 === '管理员'">
                    <button class="nav-link" id="doctorsApplication-tab" data-bs-toggle="tab"
                      data-bs-target="#labelManagement" type="button" role="tab" aria-controls="today"
                      aria-selected="true">标签管理</button>
                  </li>
                </ul>
                <el-switch @change="verify" v-model="status" active-color="#09c41c" inactive-color="#ff4949"
                  style="margin-left: 20px; --el-color-primary: #09c41c;" active-text="解锁" inactive-text="锁定">
                </el-switch>
              </div>
              <div class="tab-content mt-32 mb-24" id="myTabContent">
                <div class="tab-pane fade show active" id="patientsMessage" role="tabpanel" aria-labelledby="all-tab">
                  <div class="row sal-animate">
                    <section class="flight-listing-page mb-60">
                      <div class="container-fluid" style="padding: 0;">
                        <div class="row" id="patientList">
                          <div class="col-sm-6" v-for="patient in jsonPatientList.patientList" :key="patient.patientId"
                            v-if="jsonPatientList !== null">
                            <div class="flight-block bg-white light-shadow p-24 br-10 mb-16">
                              <div class="flight-area">
                                <div class="airline-name col-sm-6">
                                  <div>
                                    <h5 class="lightest-black mb-8">
                                      姓名:{{ patient.patientName }}</h5>
                                  </div>
                                </div>
                                <div class="flight-button col-sm-4">
                                  <router-link :to="'/detailPatient?patientId=' + patient.admissionNumber"
                                    class="cus-btn btn-sec">查看具体信息</router-link>
                                </div>
                              </div>
                              <hr class="custom-hr">
                              <div class="d-flex justify-content-between align-items-center">
                                <h5 class="dark-gray col-sm-11">
                                  身份证号:{{ patient.admissionNumber }}
                                </h5>
                                <button class="accordion-button color-primary h5 collapsed col-sm-2"
                                  @click="removePatient(patient.patientId)">
                                  删除
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="paginations mt-8">
                          <ul class="unstyled" id="patientPageList">
                            <a class="h6 fw-600 mb-0" id="pageFirst" @click="patientPageFront">
                              <li>&lt;</li>
                            </a>
                            <a class="h6 fw-600 mb-0" id="pageSecond">
                              <li>{{ patientPage.pageIndex }}</li>
                            </a>
                            <a class="h6 fw-600 mb-0" id="pageThird" @click="patientPageBack">
                              <li>&gt;</li>
                            </a>
                          </ul>
                        </div>
                      </div>
                    </section>
                  </div>
                </div>
                <div class="tab-pane fade" id="doctorsAccount" role="tabpanel" aria-labelledby="today-tab">
                  <div class="row" id="doctorList">
                    <div class="col-xl-4 col-lg-12 col-md-6 col-sm-6 mb-24" v-for="doctor in jsonDoctorList.doctorList"
                      :key="doctor.doctorId" v-if="jsonDoctorList !== null">
                      <div class="blog-box bg-white light-shadow p-24 br-20">
                        <div class="row align-items-center">
                          <div class="col-lg-12">
                            <div class="content-box" style="margin-top: 10px;">
                              <div class="d-flex gap-16 mb-24">
                                <div class="d-flex align-items-center gap-8">
                                  <img :src="userBk" alt="">
                                  <h5>医生账户：</h5>
                                  <p class="h-16 dark-gray">{{ doctor.doctorId }}</p>
                                </div>
                                <div class="vr-line"></div>
                                <div class="d-flex align-items-center gap-8">
                                  <h5>密码：</h5>
                                  <p class="h-16 dark-gray" v-if="status === true">{{ doctor.password }}</p>
                                  <p class="h-16 dark-gray" v-if="status === false">解锁后显示</p>
                                </div>
                              </div>
                              <p class="dark-gray mb-24">医生姓名：{{ doctor.doctorName }}</p>
                              <div class="d-flex justify-content-center col-sm-12">
                                <button class="cus-btn small-pad" @click="removeDoctor(doctor.doctorId)">删除账号</button>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="paginations mt-8">
                    <ul class="unstyled" id="doctorPageList">
                      <a class="h6 fw-600 mb-0" id="pageFirst" @click="doctorPageFront">
                        <li> &lt;</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageSecond">
                        <li>{{ doctorPage.pageIndex }}</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageThird" @click="doctorPageBack">
                        <li>&gt;</li>
                      </a>
                    </ul>
                  </div>
                </div>
                <div class="tab-pane fade" id="doctorsApplication" role="tabpanel" aria-labelledby="today-tab">
                  <div class="row" id="doctorApplyList">
                    <div class="col-xl-4 col-lg-12 col-md-6 col-sm-6 mb-24"
                      v-for="doctor in jsonDoctorApplyList.doctorApplyList" :key="doctor.doctorAccount"
                      v-if="jsonDoctorApplyList !== null">
                      <div class="blog-box bg-white light-shadow p-24 br-20">
                        <div class="row align-items-center">
                          <div class="col-lg-12">
                            <div class="content-box" style="margin-top: 10px;">
                              <div class="d-flex gap-16 mb-24">
                                <div class="d-flex align-items-center gap-8">
                                  <img :src="userBk" alt="">
                                  <h5>申请账户：</h5>
                                  <p class="h-16 dark-gray">{{ doctor.applyId }}
                                  </p>
                                </div>
                                <div class="vr-line"></div>
                                <div class="d-flex align-items-center gap-8">
                                  <h5>密码：</h5>
                                  <p class="h-16 dark-gray" v-if="status === true">
                                    {{ doctor.password }}
                                  </p>
                                  <p class="h-16 dark-gray" v-if="status === false">解锁后显示</p>
                                </div>
                              </div>
                              <p class="dark-gray mb-24">{{ doctor.comment }}</p>
                              <div class="d-flex justify-content-center col-sm-12">
                                <button class="cus-btn small-pad" style="margin-right: 10px;"
                                  @click="agreeApply(doctor.applyId)">同意</button>
                                <button class="cus-btn small-pad" @click="noAgreeApply(doctor.applyId)">不同意</button>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="paginations mt-8">
                    <ul class="unstyled" id="doctorApplyPageList">
                      <a class="h6 fw-600 mb-0" id="pageFirst" @click="doctorApplyPageFront">
                        <li> &lt;</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageSecond">
                        <li>{{ doctorApplyPage.pageIndex }}</li>
                      </a>
                      <a class="h6 fw-600 mb-0" id="pageThird" @click="doctorApplyPageBack">
                        <li>&gt;</li>
                      </a>
                    </ul>
                  </div>
                </div>
                <div class="tab-pane fade" id="labelManagement" role="tabpanel" aria-labelledby="today-tab">
                  <div class="row">
                    <div class="tab-pane active" id="flight">
                      <div class="card">
                        <div class="card-header">
                          <ul class="nav nav-tabs card-header-tabs " data-bs-tabs="tabs">
                            <li class="nav-item">
                              <a href="#nowLabel" class="cus-btn primary-light primary active" aria-current="true"
                                data-bs-toggle="tab">
                                查看现有标签
                              </a>
                            </li>
                            <li class="nav-item">
                              <a href="#createLabel" class="cus-btn primary-light primary " aria-current="false"
                                data-bs-toggle="tab">
                                自定义标签
                              </a>
                            </li>
                          </ul>
                        </div>
                        <div class="card-body tab-content">
                          <div class="tab-pane active" id="nowLabel">
                            <div class="row" id="tagList">
                              <div class="col-xl-3 col-lg-12 col-md-6 col-sm-6 mb-24" v-for="tag in jsonTagList.tagList"
                                :key="tag.tagId" v-if="jsonTagList !== null">
                                <div class="blog-box bg-white light-shadow p-24 br-20">
                                  <div class="row align-items-center">
                                    <div class="col-lg-12">
                                      <div class="content-box" style="margin-top: 10px;">
                                        <div class="d-flex gap-16 mb-24">
                                          <div class="d-flex align-items-center gap-8">
                                            <h5>标签：</h5>
                                            <p class="h-16 dark-gray">
                                              {{ tag.tagName }}</p>
                                          </div>
                                        </div>
                                        <div class="d-flex justify-content-center gap-16">
                                          <span class="d-flex align-items-center gap-8">
                                            <!-- <input type="checkbox" :id="'disabledLabelTag' + tag.tagId"
                                              :checked="tag.tagDisabled"
                                              @change="modifyTag(tag.tagId, $event.target.checked)">
                                            <label :for="'disabledLabelTag' + tag.tagId">隐藏标签</label> -->
                                          </span>
                                          <button class="cus-btn small-pad" @click="removeTag(tag.tagId)">删除</button>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="paginations mt-8">
                              <ul class="unstyled" id="tagPageList">
                                <a class="h6 fw-600 mb-0" id="pageFirst" @click="tagPageFront">
                                  <li>&lt;</li>
                                </a>
                                <a class="h6 fw-600 mb-0" id="pageSecond">
                                  <li>{{ tagPage.pageIndex }}</li>
                                </a>
                                <a class="h6 fw-600 mb-0" id="pageThird" @click="tagPageBack">
                                  <li>&gt;</li>
                                </a>
                              </ul>
                            </div>
                          </div>
                          <div class="tab-pane" id="createLabel">
                            <div id="createTag">
                              <div class="detail-form mb-32">
                                <h5>标签名：</h5>
                                <div class="row">
                                  <div class="col-sm-4">
                                    <input type="text" class="form-control wizard-required mb-24" id="labelName"
                                      name="labelName" placeholder="请输入……" v-model="tagName">
                                    <div class="wizard-form-error"></div>
                                  </div>
                                  <div class="col-sm-2">
                                    <button class="cus-btn" style="margin: 0 auto;display: block;"
                                      @click="createButton">创建标签</button>
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
              </div>
            </div>
          </section>
          <!-- Blog Area End -->
        </div>
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
  background: url(../assets/image/title-banner.png);
}

</style>
<style>
  .el-input {
    --el-input-focus-border: #09c41c !important;
    --el-input-focus-border-color: #09c41c !important;
  }
  .el-button--primary {
    --el-button-text-color: var(--el-color-white);
    --el-button-bg-color: #09c41c !important;
    --el-button-border-color: #09c41c !important;
    --el-button-outline-color: #09c41c !important;
    --el-button-active-color: #04620e !important;
    --el-button-hover-text-color: var(--el-color-white);
    --el-button-hover-link-text-color: #1e9261 !important;
    --el-button-hover-bg-color: #1e9261 !important;
    --el-button-hover-border-color: #1e9261 !important;
    --el-button-active-bg-color: #04620e !important;
    --el-button-active-border-color: #04620e !important;
    --el-button-disabled-text-color: var(--el-color-white);
    --el-button-disabled-bg-color: #04620e !important;
    --el-button-disabled-border-color: #04620e !important;
  }
  .el-button {
    --el-button-hover-text-color: #2cc93b !important;
    --el-button-hover-bg-color: #e1f4e3 !important;
    --el-button-hover-border-color: #e1f4e3 !important;
    --el-button-active-border-color: #2cc93b !important;
    --el-button-outline-color: #b3ffba !important;
    }
</style>
