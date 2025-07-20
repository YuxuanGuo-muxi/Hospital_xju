<script setup>
import { ref } from "vue";
import axios from '@/renderer/utils/request';
import title from '@/renderer/assets/image/title.png'
import { ElMessage } from 'element-plus';
let doctorAccount = ref("");
let doctorPassword = ref("");
let heDui = ref("");
let applyMessage = ref("");
let jsonDataRegister = {};
const registerSubmit = (event) => {
  event.preventDefault();
  if (doctorAccount.value === "" || doctorPassword.value === "" || heDui.value === "") {
    ElMessage({
      type: 'warning',
      message: '请填写申请账号、密码、确认密码！'
    });
    // alert("请填写完整信息！");
  } else if (doctorPassword.value != heDui.value) {
    ElMessage({
      type: 'warning',
      message: '两次密码不一致！'
    });
    // alert("两次密码不一致！");
  } else if (doctorPassword.value.length !== 8){
    ElMessage({
      type: 'warning',
      message: '密码需要为8位数！'
    });
  } else {
    jsonDataRegister.applyId = doctorAccount.value;
    jsonDataRegister.password = doctorPassword.value;
    jsonDataRegister.comment = applyMessage.value;
    console.log(jsonDataRegister.value);
    axios.post("/api/apply/register", jsonDataRegister).then(function (response) {
      if (response.data.result === true) {
        ElMessage({
          type: 'success',
          message: '申请成功,请联系管理员审核!'
        });
        // alert("申请成功,请联系管理员审核!")
      }
    }).catch(function (error) {
      ElMessage({
        type: 'error',
        message: '请检查网络连接或联系管理员！'
      });
      // alert("请检查网络连接或联系管理员！");
      console.log(error);
    });
  }
}

let passwordType = ref("password");
</script>

<template>
  <div class="o-scroll">
    <!-- Back To Top Start -->
    <a href="#main-wrapper" id="backto-top" class="back-to-top"><i class="fas fa-angle-up"></i></a>
    <!-- Main Wrapper Start -->
    <div id="main-wrapper" class="main-wrapper overflow-hidden">

      <!-- Main Content Start -->
      <div class="page-content m-0">

        <!-- Signup Form Start -->
        <section class="signup bg-white">
          <img :src="title" alt="" class="title">
          <div class="container-fluid">
            <div class="row">
              <div class=" col-xl-4 col-lg-6 col-md-7 mb-lg-0 mb-48">
                <div class=" form-block">
                  <h5 class="or mb-8">申请账号</h5>
                  <h6 class="mb-24" style="text-align: left;">请输入需要申请的账号密码:</h6>
                  <form id="shenQingForm" action="#" method="post" class="form-group contact-form">
                    <div class="row">
                      <div class="col-sm-12 mb-24">
                        <input type="text" class="form-control" id="doctorAccount" name="doctorAccount" required=""
                               placeholder="请输入账号" v-model="doctorAccount">
                      </div>
                      <div class="col-sm-12 mb-24">
                        <p>（密码要求为8位数！）</p>
                        <input :type="passwordType" class="form-control" id="doctorPassword" name="doctorPassword"
                               required="" placeholder="请输入密码" v-model="doctorPassword">
                      </div>
                      <div class="col-sm-12 mb-12">
                        <input :type="passwordType" class="form-control" id="heDui" name="heDui" required=""
                               placeholder="确认密码" v-model="heDui">
                        <div class="justify-content-end d-flex ">
                          <input type="checkbox" id="showPassword" class="custom-control-input"
                                 @click="passwordType = passwordType === 'password' ? 'text' : 'password'"
                                 style="transform: scale(1.3); margin-right: 10px;">
                          <label class="custom-control-label lightest-black" for="showPassword">显示密码</label>
                        </div>
                      </div>
                      <div class="col-sm-12 mb-24">
                        <input type="text" class="form-control" id="applyMessage" name="applyMessage" required=""
                               placeholder="请输入申请信息" v-model="applyMessage" style="height: 80px;">
                      </div>
                      <div class="d-flex justify-content-end">
                        <router-link to="/" class="login">返回登录</router-link>
                        <input type="submit" class="cus-btn small-pad mb-24" value="申请账号" @click="registerSubmit">
                        <!-- <button type="submit" class="cus-btn small-pad mb-24">登录</button> -->
                        <!-- Alert Message -->
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
        <!-- Signup Form End -->
      </div>
      <!-- Main Content End -->
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