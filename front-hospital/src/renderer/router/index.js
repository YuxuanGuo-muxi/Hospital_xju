import { createRouter, createWebHistory, createWebHashHistory } from "vue-router";
import Login from "@/renderer/views/login.vue";
import Register from "@/renderer/views/register.vue";
import BaseMessage from "@/renderer/views/baseMessage.vue";
import SouSuo from "@/renderer/views/souSuo.vue";
import AdminInterface from "@/renderer/views/adminInterface.vue";
import DetailPatient from "@/renderer/views/detailPatient.vue";
import DetailRecodeMessage from "@/renderer/views/detailRecodeMessage.vue";
import RecodeInfo from "@/renderer/views/recodeInfo.vue";
import LangCancer from "@/renderer/views/langCancer.vue"
import DetailLangCancerMessage from "@/renderer/views/detailLangCancerMessage.vue"

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      name: "login",
      component: Login,
    },
    {
      path: "/register",
      name: "register",
      component: Register,
    },
    {
      path: "/souSuo",
      name: "souSuo",
      component: SouSuo,
    },
    {
      path: "/baseMessage",
      name: "baseMessage",
      component: BaseMessage,
    },
    {
      path: "/adminInterface",
      name: "adminInterface",
      component: AdminInterface,
    },
    {
      path: "/detailPatient",
      name: "detailPatient",
      component: DetailPatient,
    },
    {
      path: "/detailRecodeMessage",
      name: "detailRecodeMessage",
      component: DetailRecodeMessage,
    },
    {
      path: "/recodeInfo",
      name: "recodeInfo",
      component: RecodeInfo,
    },
    {
      path: "/langCancer",
      name: "langCancer",
      component: LangCancer,
    },
    {
      path: "/detailLangCancerMessage",
      name: "detailLangCancerMessage",
      component: DetailLangCancerMessage,
    }
  ],
});
export default router;
