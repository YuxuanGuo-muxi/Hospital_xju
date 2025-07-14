<script setup>
import { ref,onMounted } from 'vue'
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import { ElButton, ElDrawer } from 'element-plus'
import { CircleCloseFilled } from '@element-plus/icons-vue'
import axios from '@/renderer/utils/request'
import { ElMessageBox, ElMessage } from 'element-plus';
import { useStore } from 'vuex' // 引入useStore 方法
const store = useStore()  // 该方法用于返回store 实例

onMounted(async () => {
  // console.log('message:', message);
  console.log(store.state.account);  // store 实例对象
  let identityTest = await identityFunc();
  if (identityTest !== null){
    identity.value = identityTest;
  };
  let dataPatient = await dataPatientLoad();
  if (dataPatient !== null){
    jsonDataPatient.value = await dataPatientLoad();
  };
  count.value = jsonDataPatient.value.length;
  console.log('name:', accountName.value);
  // console.log('identity:', identity.value);
  // console.log(jsonDataPatient.value);
  // console.log('account:', accountGlobal);
  store.commit('setIdentity1', identity.value);
});
const visible = ref(false);
let jsonDataPatient = ref([]);
let identity = ref('');
let count = ref('');
let accountName = ref('');
//判断身份
const identityFunc = async() => {
  try {
    let identityTest = "";
    // console.log(store.state.account);
    const response = await axios.get('/api/doctor/getPower/' + store.state.account);
    console.log('identityFunc:', response.data);
    if (response.data.result === true){
      if (response.data.data.power === 0){
        identityTest = "医生";
        if (response.data.data.doctorName === ''){
          accountName.value = "医生";
        }else{
          accountName.value = response.data.data.doctorName;
        }
      }else if (response.data.data.power === 1){
        identityTest = "管理员";
        if (response.data.data.doctorName === ''){
          accountName.value = "管理员";
        }else{
          accountName.value = response.data.data.doctorName;
        }
      }
      return identityTest;
    }else{
      ElMessage({
        type: 'error',
        message: '获取身份失败！'
      });
      // alert("获取身份失败！");
      return null;
    }
  }catch (error) {
  //   let identityTest;
  //   if (accountGlobal.value === '1'){
  //     identityTest = '管理员';
  //   }else{
  //     identityTest = '医生';
  //   }
  //   return identityTest;
  // }
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
    return null;
  }
};
// 获取七天内的预约信息
const dataPatientLoad = async () => {
  try {
    const response = await axios.get('/api/report/getAllRecords');
    console.log(response.data);
    if (response.data.result === true){
      let dataPatient = [];
      response.data.data.forEach(item => {
        dataPatient.push(item);
      });
      return dataPatient;
    }else{
      ElMessage({
        type: 'error',
        message: '获取七天内的预约信息失败！'
      });
      // alert("获取七天内的预约信息失败！");
      return null;
    }
  }catch (error) {
      ElMessage({
        type: 'error',
        message: '请检查网络连接或联系管理员！'
      });
    // alert("请检查网络连接或联系管理员！");
    return null;
  }
}
</script>

<template>
  <!-- Header Area Start -->
  <header>
    <nav class="main-menu" style="z-index: 2;">
      <div class="container-fluid">
        <div class="main-menu__block">
          <div class="main-menu__left">
            <div class="main-menu__logo">
              <img :src="logo" alt="新疆维吾尔族自治区中医医院" style="height: 60px; width: 60px;">
            </div>
            <div class="main-menu__nav">
              <ul class="main-menu__list">
                <li><router-link :to="{name: 'souSuo'}">随访信息检索</router-link></li>
                <li class="dropdown">
                  <a href="javascript:void(0);">信息录入</a>
                  <ul class="sub-menu">
                    <li><router-link to="/baseMessage">患者基本信息记录</router-link></li>
                    <li><router-link to="/recodeInfo">随访信息记录</router-link></li>
                    <li><router-link to="/langCancer">肺癌记录</router-link></li>
                  </ul>
                </li>
                <li><router-link to="/adminInterface">管理</router-link></li>
                <li class="dropdown">
                  <a href="javascript:void(0);">欢迎访问：{{accountName}}</a>
                  <ul class="sub-menu">
                    <h6>账户：{{store.state.account}}</h6>
                    <h6>身份：{{identity}}</h6>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
          <div class="main-menu__right">
            <el-drawer v-model="visible" :show-close="false">
              <template #header="{ close, titleId, titleClass }">
                <h4 :id="titleId" :class="titleClass">最近的复查预约（最近7天）</h4>
                <el-button type="danger" @click="close">
                  <el-icon class="el-icon--left">
                    <CircleCloseFilled />
                  </el-icon>
                  关闭
                </el-button>
              </template>
              <div class="col-sm-12">
                <div class="flight-block bg-white light-shadow p-24 br-10 mb-16 sal-animate"
                     v-for="patient in jsonDataPatient" :key="patient.patientId">
                  <div class="d-flex justify-content-between align-items-center">
                    <h5 class="color-black">{{patient.nextTime}}(下次要复查的时间)</h5>
                  </div>
                  <hr class="custom-hr">
                  <div class="flight-area d-flex">
                    <div class="airline-name col-sm-6">
                      <div>
                        <h5 class="lightest-black mb-8">{{patient.patientName}}(姓名)</h5>
                        <h6 class="dark-gray">{{patient.patientId}}(身份证号)</h6>
                      </div>
                    </div>
                    <div class="flight-button">
                      <router-link :to="'/detailRecodeMessage?reportId=' + patient.reportId" class="cus-btn btn-sec small-pad">查看随访具体信息</router-link>
                    </div>
                  </div>
                </div>
              </div>
            </el-drawer>
            <div style="position: relative; display: inline-block;">
              <a style="display: inline-block;" @click="visible = true">
                <img :src="clock" style="vertical-align: middle;">
                <span v-if="count > 0"
                      style="position: absolute; top: -1px; right: -1px; background-color: red; width: 8px; height: 8px; border-radius: 50%;"></span>
              </a>
            </div>            
            <div class="main-menu-signup__login d-xl-flex d-none">
              <router-link to="/" class="main-menu__login">
                退出账号
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </header>
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
</style>