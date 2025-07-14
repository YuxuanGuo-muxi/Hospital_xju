<script setup>
import {ref, onMounted} from "vue";
import axios from '@/renderer/utils/request';
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import {ElMessageBox, ElMessage} from 'element-plus';

let baseMessageForm = ref({
  patientId: "",
  patientName: "",
  patientAge: "",
  patientBirthday: "",
  patientGender: "",
  diagnoseTime: "",//诊断日前
  diagnosticLogout: "",//诊断建议
  curativeEffect: "",//疗效
  therapyMethod: "",//治疗方式
});
//当前时间
const currentTime = ref('');
//挂载函数
onMounted(async () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  currentTime.value = `${year}-${month}-${day}`;
  console.log(currentTime.value);
});
// 获取患者详情数据
const dataDetailPatient = async (patientId) => {
  try {
    const response = await axios.get("/api/patient/getById/" + patientId);
    if (response.data.result === true) {
      console.log(response.data.data);
      console.log(response.data.data.patientType);
      if (response.data.data.patientType === "肺癌" || response.data.data.patientType === '肺结节转肺癌') {
        return response.data.data;
      } else {
        ElMessage({
          type: 'warning',
          message: '该患者未患癌症，请确认先修改患者类型！'
        });
        return null;
      }

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
  }
}
//展示form
let showForm = ref(false);
//patientId确定
const patientIdConfirm = async (event, patientId) => {
  event.preventDefault(); // 防止默认表单提交行为
  // 这里的逻辑保持不变
  if (patientId.trim() === "") {
    ElMessage({
      type: 'warning',
      message: '请输入身份证号！'
    });
    // alert("请输入身份证号！");
    return;
  }
  try {
    const patientData = await dataDetailPatient(patientId);
    console.log('patientData:', patientData);
    if (patientData !== null) {
      baseMessageForm.value.patientName = patientData.patientName;
      baseMessageForm.value.patientAge = patientData.patientAge;
      baseMessageForm.value.patientBirthday = patientData.patientBirthday;
      baseMessageForm.value.patientGender = patientData.patientGender;
      baseMessageForm.value.diagnoseTime = currentTime.value;
      showForm.value = true;
    } else {
      showForm.value = false;
    }
    // 这里可以根据需要对患者数据进行处理或显示
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '请检查网络连接或联系管理员！'
    });
    // alert("请检查网络连接或联系管理员！");
  }
};
// 避免二次提交flag
let submitFlag = false;
//提交
const submitFormMessage = (event) => {
  event.preventDefault(); // 防止默认表单提交行为
  if (submitFlag) {
    ElMessage({
      type: 'warning',
      message: '请勿重复提交！'
    })
    return;
  }
  console.log('肺癌信息：', baseMessageForm.value);
  axios.post('/api/report/langCancer', baseMessageForm.value).then(res => {
    console.log(res);
    if (res.data.result === true) {
      ElMessage({
        type: 'success',
        message: '信息录入成功！'
      });
    }
  }).catch(err => {
    ElMessage({
      type: 'error',
      message: '信息录入失败！'
    });
    return;
  });
  submitFlag = true;
};
</script>
<template>
  <div class="o-scroll">
    <!-- Back To Top Start -->
    <a href="#main-wrapper" id="backto-top" class="back-to-top"></a>
    <!-- Main Wrapper Start -->
    <div id="main-wrapper" class="main-wrapper overflow-hidden">

      <!-- Header Area Start -->
      <Header/>
      <!-- Header Area end -->
      <!-- Main Content Start -->
      <div class="page-content m-0">
        <h6 style="padding: 0px 110px;">>肺癌记录录入 (本人承诺患者以上资料仅用作研究，不传播、不泄露)</h6>
        <section class="flight-booking">
          <div class="container-fluid">
            <div class="row">
              <div class="col-xl-12">
                <div class="booking-form">
                  <form action="#" id="recodeInfoForm" method="post" role="form"
                        class="contact-form form-wizard">
                    <div class="wizard-content overflow-visible mb-24">

                      <div class="col-sm-4 mb-12 d-flex align-items-center"
                           style="margin-top: 20px; margin-left: 40px;">
                        <h5 class="col-sm-3">身份证号：</h5>
                        <input type="text" class="form-control wizard-required col-sm-4"
                               id="patientId" name="patientId" placeholder="请输入身份证号并点击确定，以继续进行填写表格"
                               v-model="baseMessageForm.patientId">
                        <div class="wizard-form-error"></div>
                        <button class="cus-btn col-sm-2" style="margin-left: 10px;"
                                @click="patientIdConfirm($event, baseMessageForm.patientId)">确定
                        </button>
                      </div>
                      <div v-if="showForm">
                        <fieldset id="baseForm" class="tab-pane show wizard-fieldset p-0">
                          <div class="detail-form mb-32">
                            <h4 class="black p-0 mb-12">肺癌信息表：</h4>
                            <div class="row ">
                              <div class="col-sm-3 mb-12">
                                <h5>姓名：</h5>
                                <input type="text" class="form-control wizard-required"
                                       id="patientName" name="patientName"
                                       placeholder="请输入……"
                                       v-model="baseMessageForm.patientName">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>年龄(岁)：</h5>
                                <input type="text" class="form-control wizard-required"
                                       id="patientAge" name="patientAge"
                                       placeholder="请输入……"
                                       v-model="baseMessageForm.patientAge">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>出生日期：</h5>
                                <input type="date" class="form-control wizard-required"
                                       id="patientBirthday" name="patientBirthday"
                                       v-model="baseMessageForm.patientBirthday">
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-3">
                                <div class="tab-pane active" id="patientGender">
                                  <h5>性别：</h5>
                                  <div class="custom-control mb-12"
                                       style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="patientGender"
                                             class="custom-control-input" id="man"
                                             value="男"
                                             v-model="baseMessageForm.patientGender">
                                      <label class="custom-control-label"
                                             for="man">男</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="patientGender"
                                             class="custom-control-input" id="women"
                                             value="女"
                                             v-model="baseMessageForm.patientGender">
                                      <label class="custom-control-label"
                                             for="women">女</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-3 mb-12">
                                <h5>医生姓名：</h5>
                                <input type="text" class="form-control wizard-required"
                                       id="doctorName" name="doctorName"
                                       placeholder="请输入……"
                                       v-model="baseMessageForm.doctorName">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-3 mb-12">
                                <h5>诊断日期：</h5>
                                <input type="date" class="form-control wizard-required"
                                       id="diagnosis" name="diagnosis"
                                       v-model="baseMessageForm.diagnoseTime">
                                <div class="wizard-form-error"></div>
                              </div>
                              <div class="col-sm-10 mb-12">
                                <h5>诊断建议（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required"
                                          id="diagnosticLogout" name="diagnosticLogout"
                                          placeholder="请输入……"
                                          v-model="baseMessageForm.diagnosticLogout"
                                          style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-5">
                                <div class="tab-pane active" id="therapyMethod">
                                  <h5>治疗方式：</h5>
                                  <div class="custom-control mb-12"
                                       style="display: flex; margin-top: 5px;">
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="therapyMethod"
                                             class="custom-control-input" id="靶向治疗"
                                             value="靶向治疗"
                                             v-model="baseMessageForm.therapyMethod">
                                      <label class="custom-control-label"
                                             for="靶向治疗">靶向治疗</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="therapyMethod"
                                             class="custom-control-input" id="免疫治疗"
                                             value="免疫治疗"
                                             v-model="baseMessageForm.therapyMethod">
                                      <label class="custom-control-label"
                                             for="免疫治疗">免疫治疗</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="therapyMethod"
                                             class="custom-control-input" id="化疗"
                                             value="化疗"
                                             v-model="baseMessageForm.therapyMethod">
                                      <label class="custom-control-label"
                                             for="化疗">化疗</label>
                                    </div>
                                    <div class=" radio-button" style="flex: 1;">
                                      <input type="radio" name="therapyMethod"
                                             class="custom-control-input" id="放疗"
                                             value="放疗"
                                             v-model="baseMessageForm.therapyMethod">
                                      <label class="custom-control-label"
                                             for="放疗">放疗</label>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                            <div class="row ">
                              <div class="col-sm-10 mb-12">
                                <h5>疗效（字数限制250字）：</h5>
                                <textarea class="form-control wizard-required"
                                          id="curativeEffect" name="curativeEffect"
                                          placeholder="请输入……"
                                          v-model="baseMessageForm.curativeEffect"
                                          style="border: none;"></textarea>
                                <div class="wizard-form-error"></div>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                        <div class="col-sm-2" style="margin: 0 auto;">
                          <input style="width: 200px;" type="submit" class="cus-btn"
                                 @click="submitFormMessage($event)" value="提交"></input>
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
      <Footer/>
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
</style>