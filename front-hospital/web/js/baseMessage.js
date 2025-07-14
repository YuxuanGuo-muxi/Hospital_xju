new Vue({
    el: "#app",
    data: {
        flag: true,
        studyUnit: "新疆医科大学第四临床自治区中医医院",
        writeDate: "",
        doctorId: "",
        resource: "",
        patientSex: "",
        patientGroup: "",
        patientAge: "",
        patientName: "",
        patientWork: "",
        patientEduLevel: "",
        patientMarriage: "",
        patientPregnancy: "",
        patientMalignant: "",
        patientAddress: "",
        patientPhone: "",
        patientAllergy: "",
        drugName: "",
        isSmoke: "",
        smokingType: "",
        smokingYears: "",
        smokingAmount: "",
        isQuit: "",
        quitYears: "",
        medicalHistory: "",
        jsonBaseFormDatas: {}, // 初始化为空对象
        jsonAllergyFormData: {}, // 初始化为空对象
        josnSmokingFormData: {}, // 初始化为空对象
        jsonFormDatas: {} // 初始化为空对象
    },
    methods: {
        messageLoad: function () {
            axios.post("http://localhost:8080/baseMessage/messageLoad", JSON.stringify(this.jsonFormDatas)).then(function (response) {
                if (response.data.reslut == true) {
                    alert("提交成功！");
                }
            }).catch(function (error) {
                alert("提交失败！");
            });
        },
        baseForm: function () {
            let flag1 = true;
            let baseFormData = {
                studyUnit : this.studyUnit,
                writeDate : this.writeDate,
                doctorId : this.doctorId,
                resource : this.resource,
                patientSex : this.patientSex,
                patientGroup : this.patientGroup,
                patientAge : this.patientAge,
                patientName : this.patientName,
                patientWork : this.patientWork,
                patientEduLevel : this.patientEduLevel,
                patientMarriage : this.patientMarriage,
                patientPregnancy : this.patientPregnancy,
                patientMalignant : this.patientMalignant,
                patientAddress : this.patientAddress,
                patientPhone : this.patientPhone
            }
            for (let key in baseFormData){
                if (baseFormData[key] == ""){
                    flag1 = false;
                    return flag1;
                }
            }
            this.jsonBaseFormDatas = baseFormData;
            return flag1;
        },
        checkAllergy: function (patientAllergyValue) {
            this.patientAllergy = patientAllergyValue;
        },
        allergyForm: function (patientAllergyValue="yes") {
            let flag2 = true;
            let allergyFormData = {};
            if (patientAllergyValue === "yes") {
                this.patientAllergy = "yes";
                allergyFormData.patientAllergy = "yes";
                allergyFormData.drugName = this.drugName;
            } else {
                this.patientAllergy = "no";
                allergyFormData.patientAllergy = "no";
            }
            for (let key in allergyFormData) {
                if (allergyFormData[key] === "") {
                    flag2 = false;
                    return flag2;
                }
            }
            this.jsonAllergyFormData = allergyFormData;
            return flag2;
        },
        checkSmoking: function (isSmokeValue) {
            this.isSmoke = isSmokeValue;
        },
        checkSmokingType: function (smokingTypeValue) {
            this.smokingType = smokingTypeValue;
        },
        checkIsQuit: function (isQuitValue) {
            this.isQuit = isQuitValue;
        },
        smokingForm: function (isSmokeValue="yes", smokingTypeValue="initative",isQuitValue="yes") {
            let flag3 = true;
            let smokingFormData = {};
            if (isSmokeValue === "yes") {
                this.isSmoke = "yes";
                smokingFormData.isSmoke = this.isSmoke;
                if (smokingTypeValue === "initative"){
                    this.smokingType = "initative";
                    smokingFormData.smokingType = this.smokingType;
                    smokingFormData.smokingYears = this.smokingYears;
                    smokingFormData.smokingAmount = this.smokingAmount;
                    if (isQuitValue === "yes") {
                        this.isQuit = "yes";
                        smokingFormData.isQuit = this.isQuit;
                        smokingFormData.quitYears = this.quitYears;
                    } else {
                        this.isQuit = "no";
                        smokingFormData.isQuit = this.isQuit;
                    }
                }else{
                    this.smokingType = "passivity";
                    smokingFormData.smokingType = this.smokingType;
                    smokingFormData.smokingYears = this.smokingYears;
                }
            }else{
                this.isSmoke = "no";
                smokingFormData.isSmoke = this.isSmoke;
            }
            for (let key in smokingFormData) {
                if (smokingFormData[key] === "") {
                    flag3 = false;
                    return flag3;
                }
            }
            this.josnSmokingFormData = smokingFormData;
            return flag3;
        },

        submitForm: function (event) {
            event.preventDefault();
            this.flag = this.baseForm() && this.allergyForm(this.patientAllergy) && this.smokingForm(this.isSmoke,this.smokingType,this.isQuit) && this.medicalHistory != "";
            for (let key in this.jsonBaseFormDatas) {
                this.jsonFormDatas[key] = this.jsonBaseFormDatas[key];
            }
            for (let key in this.jsonAllergyFormData) {
                this.jsonFormDatas[key] = this.jsonAllergyFormData[key];
            }
            for (let key in this.josnSmokingFormData) {
                this.jsonFormDatas[key] = this.josnSmokingFormData[key];
            }
            this.jsonFormDatas.medicalHistory = this.medicalHistory;
            console.log(this.jsonFormDatas);
            if (this.flag === true){
                this.messageLoad();
            }else{
                alert("请将信息填写完整！");
            }
        }
    }
});