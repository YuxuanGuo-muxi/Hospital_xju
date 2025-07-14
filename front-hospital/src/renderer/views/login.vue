<script setup>
import { ref } from "vue";
import axios from '@/renderer/utils/request';
import { useRouter } from 'vue-router'; // 从 vue-router 中导入 useRouter
import title from '@/renderer/assets/image/title.png';
import { useStore } from 'vuex' // 引入useStore 方法
import { ElMessageBox, ElMessage } from 'element-plus';
const store = useStore()  // 该方法用于返回store 实例

let account = ref("");
let password = ref("");
let jsonDataLogin = {};
const router = useRouter(); // 初始化 router
//全局变量account赋值
const setAccount = (account) => {
  store.commit('setAccount', account);
}
// 登录提交
const loginSubmit = (event) => {
  event.preventDefault();
  if (!account.value || !password.value) {
    ElMessage({
      type: 'warning',
      message: '账号或密码不能为空！'
    });
    return;
  } else {
    const jsonDataLogin = {
      doctorId: account.value,
      password: password.value
    };
    axios.post("/api/doctor/login", jsonDataLogin)
        .then((response) => {
          if (response.data.result === true) {
            setAccount(account.value);
            console.log('store.state.account', store.state.account);
            ElMessage({
              type: 'success',
              message: '登录成功！'
            });
            router.push({ path: `/souSuo` });
          }else{
            ElMessage({
              type: 'error',
              message: '登录失败！请检查账号或密码是否正确！'
            });
          }
        })
        .catch((error) => {
          ElMessage({
            type: 'error',
            message: '请检查网络连接或联系管理员！'
          });
        });
  }
};
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
                  <h5 class="or mb-8">登录</h5>
                  <h6 class="mb-24" style="text-align: left;">请输入账号密码:</h6>
                  <form method="post" action="#" class="form-group contact-form" id="loginForm">
                    <div class="row">
                      <div class="col-sm-12">
                        <div class="mb-24">
                          <input type="text" class="form-control" id="loginAccount" name="loginAccount" required
                                 placeholder="账号" v-model="account">
                        </div>
                      </div>
                      <div class="col-sm-12">
                        <div class="mb-24">
                          <input :type="passwordType" class="form-control" id="loginPassword" name="loginPassword" required
                                 placeholder="密码" v-model="password">
                          <div class="justify-content-end d-flex ">
                            <input type="checkbox" id="showPassword"
                                   class="custom-control-input" @click="passwordType = passwordType === 'password'? 'text' : 'password'"
                                   style="transform: scale(1.3); margin-right: 10px;">
                            <label class="custom-control-label lightest-black" for="showPassword">显示密码</label>
                          </div>
                        </div>
                      </div>
                      <div class="d-flex justify-content-end">
                        <router-link :to="{name:'register'}" class="sign_up">申请账号</router-link>
                        <!--                        <a href="/register" class="sign_up">申请账号</a>-->
                        <input type="submit" value="登录" class="cus-btn small-pad mb-24" @click="loginSubmit">
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