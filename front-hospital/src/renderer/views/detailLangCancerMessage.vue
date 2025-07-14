<script setup>
import { ref, onMounted } from "vue";
import axios from '@/renderer/utils/request';
import { useRoute } from "vue-router";
import logo from '@/renderer/assets/picture/logo.png'
import clock from '@/renderer/assets/picture/clock.png'
import Header from "@/renderer/components/header.vue";
import Footer from "@/renderer/components/footer.vue";
import { ElMessageBox, ElMessage } from 'element-plus';

const route = useRoute();

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
//肺癌记录ID
const reportId = ref('');
// 表单状态
let formStatus = ref(true);
//挂载函数
onMounted(async () => {
    //获取查询参数
  const queryParams = route.query;
  const reportId = queryParams.reportId;
  baseMessageForm.value = await dataDetailLangCancer(reportId);
  console.log("baseMessageForm.value:",baseMessageForm.value);
});
// 获取肺癌详情数据
const dataDetailLangCancer = async (reportId) => {
    console.log("reportId:",reportId);
    try {
        const response = await axios.get("/api/report/getCancerInfoById/" + reportId);
        if (response.data.result === true) {
            console.log(response.data.data);
            return response.data.data;
        } else {
            ElMessage({
                type: 'warning',
                message: '该患者没有录入基本信息！'
            });
            return null;
        }
    } catch (error) {
        ElMessage({
            type: 'error',
            message: '请检查网络连接或联系管理员！'
        });
        let baseMessageForm1 = ref({
            patientId: "231",
            patientName: "张三",
            patientAge: "12",
            patientBirthday: "2003-01-01",
            patientGender: "男",
            diagnoseTime: "2022-01-01",//诊断日前
            diagnosticLogout: "无",//诊断建议
            curativeEffect: "无",//疗效
            therapyMethod: "靶向治疗",//治疗方式
        });
        return baseMessageForm1.value;
    }
};
// 编辑按钮
const editButton = () => {
    event.preventDefault();
    formStatus.value = false;
};
// 保存编辑
const saveEditMessage = async () => {
    event.preventDefault();
    try {
        console.log("baseMessageForm.value:",baseMessageForm.value);
        const response = await axios.post("/api/report/updateCancer", baseMessageForm.value);
        if (response.data.result === true) {
            ElMessage({
                type:'success',
                message: '保存成功！'
            });
            formStatus.value = true;
        } else {
            ElMessage({
                type: 'error',
                message: '保存失败！'
            });
        }
    } catch (error) {
        ElMessage({
            type: 'error',
            message: '请检查网络连接或联系管理员！'
        });
    }
};
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
                    :to="'/detailPatient?patientId=' + baseMessageForm.patientId">患者具体信息（点击可返回）</router-link>>肺癌信息记录</h6>
                <section class="flight-booking">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-xl-12">
                                <div class="booking-form">
                                    <form action="#" id="baseMessageForm" method="post" role="form"
                                        class="contact-form form-wizard">
                                        <div class="wizard-content overflow-visible mb-24">
                                            <fieldset id="baseForm" class="tab-pane show wizard-fieldset p-0">
                                                <div class="detail-form mb-32">
                                                    <div class="d-flex justify-content-end">
                                                      <button class="cus-btn small-pad" @click="editButton">进行编辑
                                                      </button>
                                                      <button class="cus-btn small-pad" id="saveButton" style="margin-left: 10px;"
                                                        @click="saveEditMessage">保存编辑
                                                      </button>
                                                    </div>
                                                    <h4 class="black p-0 mb-12">肺癌信息表：</h4>
                                                    <div class="row ">
                                                        <div class="col-sm-3 mb-12">
                                                            <h5>姓名：</h5>
                                                            <input type="text" class="form-control wizard-required"
                                                                id="patientName" name="patientName" placeholder="请输入……"
                                                                v-model="baseMessageForm.patientName" :readonly="formStatus">
                                                            <div class="wizard-form-error"></div>
                                                        </div>
                                                        <div class="col-sm-3 mb-12">
                                                            <h5>年龄(岁)：</h5>
                                                            <input type="text" class="form-control wizard-required"
                                                                id="patientAge" name="patientAge" placeholder="请输入……"
                                                                v-model="baseMessageForm.patientAge" :readonly="formStatus">
                                                            <div class="wizard-form-error"></div>
                                                        </div>
                                                        <div class="col-sm-3 mb-12">
                                                            <h5>出生日期：</h5>
                                                            <input type="date" class="form-control wizard-required"
                                                                id="patientBirthday" name="patientBirthday"
                                                                v-model="baseMessageForm.patientBirthday" :readonly="formStatus">
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
                                                                            v-model="baseMessageForm.patientGender" :disabled="formStatus">
                                                                        <label class="custom-control-label"
                                                                            for="man">男</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="patientGender"
                                                                            class="custom-control-input" id="women"
                                                                            value="女"
                                                                            v-model="baseMessageForm.patientGender" :disabled="formStatus">
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
                                                                id="doctorName" name="doctorName" placeholder="请输入……"
                                                                v-model="baseMessageForm.doctorName" :readonly="formStatus">
                                                            <div class="wizard-form-error"></div>
                                                        </div>
                                                        <div class="col-sm-3 mb-12">
                                                            <h5>诊断日期：</h5>
                                                            <input type="date" class="form-control wizard-required"
                                                                id="diagnosis" name="diagnosis"
                                                                v-model="baseMessageForm.diagnoseTime" :readonly="formStatus">
                                                            <div class="wizard-form-error"></div>
                                                        </div>
                                                        <div class="col-sm-10 mb-12">
                                                            <h5>诊断建议（字数限制250字）：</h5>
                                                            <textarea class="form-control wizard-required"
                                                                id="diagnosticLogout" name="diagnosticLogout"
                                                                placeholder="请输入……"
                                                                v-model="baseMessageForm.diagnosticLogout"
                                                                style="border: none;" :readonly="formStatus"></textarea>
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
                                                                            value="靶向治疗" :disabled="formStatus"
                                                                            v-model="baseMessageForm.therapyMethod"
                                                                            :checked="baseMessageForm.therapyMethod === '靶向治疗'">
                                                                        <label class="custom-control-label"
                                                                            for="靶向治疗">靶向治疗</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="therapyMethod"
                                                                            class="custom-control-input" id="免疫治疗"
                                                                            value="免疫治疗" :disabled="formStatus"
                                                                            v-model="baseMessageForm.therapyMethod"
                                                                            :checked="baseMessageForm.therapyMethod === '免疫治疗'">
                                                                        <label class="custom-control-label"
                                                                            for="免疫治疗">免疫治疗</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="therapyMethod"
                                                                            class="custom-control-input" id="化疗"
                                                                            value="化疗" :disabled="formStatus"
                                                                            v-model="baseMessageForm.therapyMethod"
                                                                            :checked="baseMessageForm.therapyMethod === '化疗'">
                                                                        <label class="custom-control-label"
                                                                            for="化疗">化疗</label>
                                                                    </div>
                                                                    <div class=" radio-button" style="flex: 1;">
                                                                        <input type="radio" name="therapyMethod"
                                                                            class="custom-control-input" id="放疗"
                                                                            value="放疗" :disabled="formStatus"
                                                                            v-model="baseMessageForm.therapyMethod"
                                                                            :checked="baseMessageForm.therapyMethod === '放疗'">
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
                                                                placeholder="请输入……" :readonly="formStatus"
                                                                v-model="baseMessageForm.curativeEffect"
                                                                style="border: none;"></textarea>
                                                            <div class="wizard-form-error"></div>
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
</style>