import { ref } from 'vue';
import axios from '@/renderer/utils/request';

export function useLoginLogic() {
    const account = ref("");
    const password = ref("");
    const jsonDataLogin = ref({});

    const loginSubmit = (event) => {
        event.preventDefault();
        if (!account.value || !password.value) {
            alert("账号或密码不能为空！");
            return;
        } else {
            jsonDataLogin.value.account = account.value;
            jsonDataLogin.value.password = password.value;
            axios.post("http://localhost:8080/login/loginSubmit", JSON.stringify(jsonDataLogin.value))
                .then((response) => {
                    if (response.data.result === "true") {
                        alert("登录成功！");
                        window.location.href = "/Users/biwei/Desktop/随访软件开发/开发1/html/souSuo.html?account=" + account.value;
                    }
                })
                .catch((error) => {
                    alert("登录失败！");
                    console.log(error);
                });
        }
    };
    return {
        account,
        password,
        loginSubmit
    };
}
