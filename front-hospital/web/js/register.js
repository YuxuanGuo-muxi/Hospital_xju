new Vue({
    el: "#shenQingForm",
    data: {
        doctorAccount: "",
        doctorPassword: "",
        heDui: "",
        applyMessage: "",
        jsonDataRegister: {}
    },
    methods: {
        registerSubmit: function (event) {
            event.preventDefault();
            if (this.doctorAccount == "" || this.doctorPassword == "" || this.heDui == ""){
                alert("请填写完整信息！");
            }else if(this.doctorPassword!= this.heDui){
                alert("两次密码不一致！");
            }else{
                this.jsonDataRegister.doctorAccount = this.doctorAccount;
                this.jsonDataRegister.doctorPassword = this.doctorPassword;
                this.jsonDataRegister.applyMessage = this.applyMessage;
                console.log(this.jsonDataRegister);
                axios.post("http://localhost:8080/register/register", JSON.stringify(this.jsonDataRegister)).then(function (response) {
                    if (response.data.result === true){
                        alert("注册成功,请返回登入!")
                    }
                }).catch(function (error) {
                    alert("注册失败,请检查网络或联系管理员!");
                    console.log(error);
                });
            }
        }
    }
})