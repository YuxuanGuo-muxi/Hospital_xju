let jsonPatientMessage = {}
let special_work = ['miner','cementer','spinner','other']
let unspecial_work = ['worker','farmer','intellectual','custodian','service','retirement','unemployed','laid_off']
let jsonDataChange = {}
let jsonPatientId = {}
let jsonPatientRecodeInfo = {}
let jsonSearchRecodeInfo = {
    "patient_id":"",
    "pageIndex":0
}
document.addEventListener("DOMContentLoaded",function(){
    // 获取当前页面的patient_id参数
    let searchParams = new URLSearchParams(window.location.search);
    let patient_id = searchParams.get('patient_id');
    jsonPatientId.patient_id = patient_id;
    jsonPatientMessage =dataDetailPatient(JSON.stringify(jsonPatientId));
    showPatientMessage(jsonPatientMessage);
    changePatientMessage();

    // 获取患者随访记录
    jsonSearchRecodeInfo.patient_id = patient_id;
    jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
    creatRecodeList(jsonPatientRecodeInfo);

    // 获取患者随访记录分页信息
    creatPageList(jsonSearchRecodeInfo.pageIndex);
    
    //绑定tab切换事件
    const baseForm_tab = document.getElementById("baseForm-tab");
    const recodeForm_tab = document.getElementById("recodeForm-tab");
    const baseMessage = document.getElementById("baseMessage")
    const recodeMessage = document.getElementById("recodeMessage")
    baseForm_tab.addEventListener("click",function(){
        baseMessage .classList.add("active");
        baseMessage .classList.add("show");
        recodeMessage.classList.remove("active");
        recodeMessage.classList.remove("show");
    });
    recodeForm_tab.addEventListener("click",function(){
        baseMessage.classList.remove("active");
        baseMessage.classList.remove("show");
        recodeMessage.classList.add("active");
        recodeMessage.classList.add("show");
    });

    bindPageChangeListeners()
});

// 绑定页面切换事件
function bindPageChangeListeners() {
    const pageStart = document.getElementById('pageStart');
    const pageFirst = document.getElementById('page'+(jsonSearchRecodeInfo.pageIndex));
    const pageSecond = document.getElementById('page'+(jsonSearchRecodeInfo.pageIndex+1));
    const pageThird = document.getElementById('page'+(jsonSearchRecodeInfo.pageIndex+2));
    const pageFinal = document.getElementById('pageFinal');
    pageStart.addEventListener('click', function() {
        jsonSearchRecodeInfo.pageIndex = 1;
        // console.log(jsonSearchRecodeInfo);
        jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
        creatRecodeList(jsonPatientRecodeInfo);
        creatPageList(jsonSearchRecodeInfo.pageIndex);
        bindPageChangeListeners();
    });
    pageFirst.addEventListener('click', function() {
        // console.log(jsonSearchRecodeInfo);
        jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
        creatRecodeList(jsonPatientRecodeInfo);
        creatPageList(jsonSearchRecodeInfo.pageIndex);
        bindPageChangeListeners();
    });
    pageSecond.addEventListener('click', function() {
        jsonSearchRecodeInfo.pageIndex += 1;
        // console.log(jsonSearchRecodeInfo);
        jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
        creatRecodeList(jsonPatientRecodeInfo);
        creatPageList(jsonSearchRecodeInfo.pageIndex);
        bindPageChangeListeners();
    });
    pageThird.addEventListener('click', function() {
        jsonSearchRecodeInfo.pageIndex += 2;
        // console.log(jsonSearchRecodeInfo);
        jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
        creatRecodeList(jsonPatientRecodeInfo);
        creatPageList(jsonSearchRecodeInfo.pageIndex);
        bindPageChangeListeners();
    });
    pageFinal.addEventListener('click', function() {
        jsonSearchRecodeInfo.pageIndex = -1;
        // console.log(jsonSearchRecodeInfo);
        jsonPatientRecodeInfo = dataRecodeList(JSON.stringify(jsonSearchRecodeInfo));
        creatRecodeList(jsonPatientRecodeInfo);
        creatPageList(jsonSearchRecodeInfo.pageIndex);
        bindPageChangeListeners();
    });
}

//创建分页列表
function creatPageList(pageIndex){
    // console.log(pageIndex);
    //父容器
    const pageList = document.getElementById('pageList');
    pageList.innerHTML = "";
    // 父容器
    const pageStartA = document.createElement("a");
    const pageFirstA = document.createElement("a");
    const pageSecondA = document.createElement("a");
    const pageThirdA = document.createElement("a");
    const pageFinalA = document.createElement("a");

    pageStartA.classList.add("h6", "fw-600", "mb-0");
    pageStartA.id = "pageStart";
    
    pageFirstA.classList.add("h6", "fw-600", "mb-0");
    pageFirstA.id = "page" + pageIndex;

    pageSecondA.classList.add("h6", "fw-600", "mb-0");
    pageSecondA.id = "page" + (pageIndex + 1);

    pageThirdA.classList.add("h6", "fw-600", "mb-0");
    pageThirdA.id = "page" + (pageIndex + 2);

    pageFinalA.classList.add("h6", "fw-600", "mb-0");
    pageFinalA.id = "pageFinal";
    
    //子容器
    const pageStart = document.createElement("li");
    pageStart.classList.add("active");
    const pageFirst = document.createElement("li");
    const pageSecond = document.createElement("li");
    const pageThird = document.createElement("li");
    const pageFinal = document.createElement("li");

    //内容
    if (pageIndex == -1){
        pageStart.textContent = "<<";
        pageFirst.textContent = "final";
        pageSecond.textContent = 0;
        pageThird.textContent = 1;
        pageFinal.textContent = ">>";
    }else{
        pageStart.textContent = "<<";
        pageFirst.textContent = pageIndex;
        pageSecond.textContent = pageIndex + 1;
        pageThird.textContent = pageIndex + 2;
        pageFinal.textContent = ">>";
    }

    //包含
    pageStartA.appendChild(pageStart);
    pageFirstA.appendChild(pageFirst);
    pageSecondA.appendChild(pageSecond);
    pageThirdA.appendChild(pageThird);
    pageFinalA.appendChild(pageFinal);

    //父容器包含子容器
    pageList.appendChild(pageStartA);
    pageList.appendChild(pageFirstA);
    pageList.appendChild(pageSecondA);
    pageList.appendChild(pageThirdA);
    pageList.appendChild(pageFinalA);
}
//随访记录列表获取
function dataRecodeList(jsonSearchRecodeInfoString){
    axios.get("http://localhost:8080/detailPatient/dataRecodeList",jsonSearchRecodeInfoString).then(function(response){
        return response.data;
    }).catch(function(error){
        alert("获取随访记录列表失败")
    });
    let jsonPatientRecodeInfo = {
        recodeMessageList:[
            {
                "doctor_id":"000005",
                "writeDate":"2021-06-01",
                "message":"患者的情况属于正常，无明显异常。",
                "report_id":"000001",
                "report_times":1
            },
            {
                "doctor_id":"000006",
                "writeDate":"2021-06-02",
                "message":"患者的情况属于正常，无明显异常。",
                "report_id":"000002",
                "report_times":2
            },
            {
                "doctor_id":"000007",
                "writeDate":"2021-06-03",
                "message":"患者的情况属于正常，无明显异常。",
                "report_id":"000003",
                "report_times":3
            },
            {
                "doctor_id":"000008",
                "writeDate":"2021-06-04",
                "message":"患者的情况属于正常，无明显异常。",
                "report_id":"000004",
                "report_times":4
            }
        ]
    }
    return jsonPatientRecodeInfo;
}
//显示随访记录列表
function creatRecodeList(jsonPatientRecodeInfo){
    const recodeMessageList = document.getElementById("recodeMessageList");
    recodeMessageList.classList.add("row");
    recodeMessageList.innerHTML = "";
    jsonPatientRecodeInfo.recodeMessageList.forEach(function(item){
        const div1 = document.createElement("div");
        div1.classList.add("col-xl-5","blog-box","bg-white","light-shadow","p-24","br-20","align-items-center","col-lg-12","mb-12");
        div1.style.marginLeft = "20px";

        const h4 = document.createElement("h4");
        h4.textContent = "第"+item.report_times+"次随访";
        
        const div1_div1 = document.createElement("div");
        div1_div1.classList.add("content-box");
        div1_div1.style.marginTop = "10px";

        const div1_div1_div1 = document.createElement("div");
        div1_div1_div1.classList.add("d-flex","gap-16","mb-24");

        const div1_div1_div1_div1 = document.createElement("div");
        div1_div1_div1_div1.classList.add("d-flex","align-items-center","gap-8");
        const div1_div1_div1_div1_img = document.createElement("img");
        div1_div1_div1_div1_img.src = "../picture/user-bk.png";
        const div1_div1_div1_div1_h5 = document.createElement("h5");
        div1_div1_div1_div1_h5.textContent = "医生ID：";
        const div1_div1_div1_div1_p = document.createElement("p");
        div1_div1_div1_div1_p.textContent = item.doctor_id;
        div1_div1_div1_div1.appendChild(div1_div1_div1_div1_img);
        div1_div1_div1_div1.appendChild(div1_div1_div1_div1_h5);
        div1_div1_div1_div1.appendChild(div1_div1_div1_div1_p);

        const div1_div1_div1_div2 = document.createElement("div");
        div1_div1_div1_div2.classList.add("vr-line");

        const div1_div1_div1_div3 = document.createElement("div");
        div1_div1_div1_div3.classList.add("d-flex","align-items-center","gap-8");
        const div1_div1_div1_div3_img = document.createElement("img");
        div1_div1_div1_div3_img.src = "../picture/calender.png";
        const div1_div1_div1_div3_h5 = document.createElement("h5");
        div1_div1_div1_div3_h5.textContent = "随访日期：";
        const div1_div1_div1_div3_p = document.createElement("p");
        div1_div1_div1_div3_p.classList.add("h-16","dark-gray");
        div1_div1_div1_div3_p.textContent = item.writeDate;
        div1_div1_div1_div3.appendChild(div1_div1_div1_div3_img);
        div1_div1_div1_div3.appendChild(div1_div1_div1_div3_h5);
        div1_div1_div1_div3.appendChild(div1_div1_div1_div3_p);

        const div1_div1_p = document.createElement("p");
        div1_div1_p.classList.add("mb-24","dark-gray");
        div1_div1_p.textContent = item.message;
        const div1_div1_a = document.createElement("a");
        div1_div1_a.classList.add("small-pad","cus-btn");
        div1_div1_a.href = "detailRecodeMessage.html?report_id="+item.report_id;
        div1_div1_a.textContent = "查看具体信息";
        
        div1_div1_div1.appendChild(div1_div1_div1_div1);
        div1_div1_div1.appendChild(div1_div1_div1_div2);
        div1_div1_div1.appendChild(div1_div1_div1_div3);
        div1_div1.appendChild(div1_div1_div1);
        div1_div1.appendChild(div1_div1_p);
        div1_div1.appendChild(div1_div1_a);

        div1.appendChild(h4);
        div1.appendChild(div1_div1);
        
        recodeMessageList.appendChild(div1);
    });
}
// 显示患者信息
function showPatientMessage(jsonPatientMessage){
    const writeDate = document.getElementById("writeDate");
    writeDate.value = jsonPatientMessage.writeDate;
    writeDate.readOnly = true;

    const doctor_id = document.getElementById("doctor_id");
    doctor_id.value = jsonPatientMessage.doctor_id;
    doctor_id.readOnly = true;

    const resourceList = document.querySelectorAll("input[name='resource']");
    resourceList.forEach(function(item) {
        if (item.value === jsonPatientMessage.resource) {
            item.checked = true;
        }
        item.disabled = true;
    });

    const patient_sexList = document.querySelectorAll("input[name='patient_sex']");
    patient_sexList.forEach(function(item) {
        // console.log(item.value);
        if (item.value === jsonPatientMessage.patient_sex) {
            item.checked = true;
        }   
        item.disabled = true;
    });
    
    const patient_group = document.getElementById("patient_group");
    patient_group.value = jsonPatientMessage.patient_group;
    patient_group.readOnly = true;
    
    const patient_age = document.getElementById("patient_age");
    patient_age.value = jsonPatientMessage.patient_age;
    patient_age.readOnly = true;

    const patient_name = document.getElementById("patient_name");
    patient_name.value = jsonPatientMessage.patient_name;
    patient_name.readOnly = true;

    const patient_workList = document.querySelectorAll("input[name='patient_work']");
    patient_workList.forEach(function(item) {
        if (item.value === jsonPatientMessage.patient_work) {
            item.checked = true;
        }
        item.disabled = true;
    });
    const special_workA = document.getElementById("special_work");
    const unspecial_workA = document.getElementById("unspecial_work");
    special_workA.style.pointerEvents = "none";
    unspecial_workA.style.pointerEvents = "none";
    special_work.forEach(function(item){
        if (item === jsonPatientMessage.patient_work){
            // console.log(item);
            special_workA.classList.add("active");
            const teshu = document.getElementById("teshu");
            teshu.classList.add("active");
        }
    });
    unspecial_work.forEach(function(item){
        if (item === jsonPatientMessage.patient_work){
            // console.log(item);
            unspecial_workA.classList.add("active");
            const feiteshu = document.getElementById("feiteshu");
            feiteshu.classList.add("active");
        }
    });

    const patient_edu_levelList = document.querySelectorAll("input[name='patient_edu_level']");
    patient_edu_levelList.forEach(function(item) {
        if (item.value === jsonPatientMessage.patient_edu_level) {
            item.checked = true;
        }
        item.disabled = true;
    });

    const patient_marriageList = document.querySelectorAll("input[name='patient_marriage']");
    patient_marriageList.forEach(function(item) {
        if (item.value === jsonPatientMessage.patient_marriage) {
            item.checked = true;
        }
        item.disabled = true;
    });

    const patient_pregnancyList = document.querySelectorAll("input[name='patient_pregnancy']");
    patient_pregnancyList.forEach(function(item) {
        if (item.value === jsonPatientMessage.patient_pregnancy) {
            item.checked = true;
        }
        item.disabled = true;
    });

    const patient_malignantList = document.querySelectorAll("input[name='patient_malignant']");
    patient_malignantList.forEach(function(item) {
        if (item.value === jsonPatientMessage.patient_malignant) {
            item.checked = true;
        }
        item.disabled = true;
    });

    const pattient_address = document.getElementById("patient_address");
    pattient_address.value = jsonPatientMessage.patient_address;
    pattient_address.readOnly = true;

    const patient_phone = document.getElementById("patient_phone");
    patient_phone.value = jsonPatientMessage.patient_phone;
    patient_phone.readOnly = true;  

    const haveAllergyA = document.getElementById("haveAllergyA");
    const noHaveAllergyA = document.getElementById("noHaveAllergyA");
    haveAllergyA.style.pointerEvents = "none";
    noHaveAllergyA.style.pointerEvents = "none";
    if (jsonPatientMessage.patient_allergy === "yes"){
        haveAllergyA.classList.add("active");
        const haveAllergy = document.getElementById("haveAllergy");
        haveAllergy.classList.add("active");
        const drug_name = document.getElementById("drug_name");
        drug_name.value = jsonPatientMessage.drug_name;
        drug_name.readOnly = true;
    }else{
        noHaveAllergyA.classList.add("active");
    }

    const smokingA = document.getElementById("smokingA");
    const noSmokingA = document.getElementById("noSmokingA");
    smokingA.style.pointerEvents = "none";
    noSmokingA.style.pointerEvents = "none";
    if (jsonPatientMessage.is_smoke === "yes"){
        smokingA.classList.add("active");
        const smoking = document.getElementById("smoking");
        smoking.classList.add("active");
        const initativeWay = document.getElementById("initativeWay");
        const passivityWay = document.getElementById("passivityWay");
        initativeWay.style.pointerEvents = "none";
        passivityWay.style.pointerEvents = "none";
        if (jsonPatientMessage.smoking_type === "initative"){
            initativeWay.classList.add("active");
            const initative = document.getElementById("initative");
            initative.classList.add("active");
            const smoking_years = document.getElementById("initativeSmokingYears");
            smoking_years.value = jsonPatientMessage.smoking_years;
            smoking_years.readOnly = true;

            const smoking_amount = document.getElementById("smoking_amount");
            smoking_amount.value = jsonPatientMessage.smoking_amount;
            smoking_amount.readOnly = true;

            const quitSmoking = document.getElementById("quitSmoking");
            const noQuitSmoking = document.getElementById("noQuitSmoking");
            quitSmoking.style.pointerEvents = "none";
            noQuitSmoking.style.pointerEvents = "none";
            if (jsonPatientMessage.is_quit === "yes"){
                quitSmoking.classList.add("active");
                const trueQuitSmoking = document.getElementById("true");
                trueQuitSmoking.classList.add("active");
                const quit_years = document.getElementById("quit_years");
                quit_years.value = jsonPatientMessage.quit_years;
                quit_years.readOnly = true;
            }else{
                noQuitSmoking.classList.add("active");
            }
        }else{
            passivityWay.classList.add("active");
            const passivity = document.getElementById("passivity");
            passivity.classList.add("active");
            const smoking_years = document.getElementById("passivitySmokingYears");
            smoking_years.value = jsonPatientMessage.smoking_years;
            smoking_years.readOnly = true;
        }
    }else{
        noSmokingA.classList.add("active");
    }

    const medical_historyList = document.querySelectorAll("option");
    medical_historyList.forEach(function(item) {
        if (item.value === jsonPatientMessage.medical_history) {
            item.selected = true;
        }
        item.disabled = true;
    });
    const tagList = document.getElementById("tagList");
    tagList.innerHTML = "";
    jsonPatientMessage.tags.forEach(function(item) {
        const tag_div = document.createElement("div");
        tag_div.classList.add("filter-checkbox","col-sm-3","mb-12");
        const tag_input = document.createElement("input");
        tag_input.type = "checkbox";
        tag_input.id = item.tag_id;
        tag_input.checked = true;
        tag_input.disabled = true;
        const tag_label = document.createElement("label");
        tag_label.classList.add("color-black");
        tag_label.setAttribute("for",item.tag_id);
        tag_label.textContent = item.tag_name;
        tag_div.appendChild(tag_input);
        tag_div.appendChild(tag_label);
        tagList.appendChild(tag_div);
    });
}

// 改变患者信息
function changePatientMessage(){
    const changeButton = document.getElementById("changeButton");
    const writeDate = document.getElementById("writeDate");
    const doctor_id = document.getElementById("doctor_id");
    const resourceList = document.querySelectorAll("input[name='resource']");
    const patient_sexList = document.querySelectorAll("input[name='patient_sex']");
    const patient_group = document.getElementById("patient_group");
    const patient_age = document.getElementById("patient_age");
    const patient_name = document.getElementById("patient_name");
    const special_workA = document.getElementById("special_work");
    const unspecial_workA = document.getElementById("unspecial_work");
    const patient_workList = document.querySelectorAll("input[name='patient_work']");
    const patient_edu_levelList = document.querySelectorAll("input[name='patient_edu_level']");
    const patient_marriageList = document.querySelectorAll("input[name='patient_marriage']");
    const patient_pregnancyList = document.querySelectorAll("input[name='patient_pregnancy']");
    const patient_malignantList = document.querySelectorAll("input[name='patient_malignant']");
    const pattient_address = document.getElementById("patient_address");
    const patient_phone = document.getElementById("patient_phone");
    const haveAllergyA = document.getElementById("haveAllergyA");
    const noHaveAllergyA = document.getElementById("noHaveAllergyA");
    const drug_name = document.getElementById("drug_name");
    const smokingA = document.getElementById("smokingA");
    const noSmokingA = document.getElementById("noSmokingA");
    const initativeWay = document.getElementById("initativeWay");
    const passivityWay = document.getElementById("passivityWay");
    const smoking_years = document.getElementById("initativeSmokingYears");
    const smoking_amount = document.getElementById("smoking_amount");
    const quitSmoking = document.getElementById("quitSmoking");
    const noQuitSmoking = document.getElementById("noQuitSmoking");
    const quit_years = document.getElementById("quit_years");
    const medical_historyList = document.querySelectorAll("option");
    const tagList = document.getElementById("tagList");
    changeButton.addEventListener("click",function(){
        writeDate.readOnly = false;
        doctor_id.readOnly = false;
        resourceList.forEach(function(item) {
            item.disabled = false;
        });
        patient_sexList.forEach(function(item) {
            item.disabled = false;
        });
        patient_group.readOnly = false;
        patient_age.readOnly = false;
        patient_name.readOnly = false;
        special_workA.style.pointerEvents = "auto";
        unspecial_workA.style.pointerEvents = "auto";
        patient_workList.forEach(function(item) {
            item.disabled = false;
        });
        patient_edu_levelList.forEach(function(item) {
            item.disabled = false;
        });
        patient_marriageList.forEach(function(item) {
            item.disabled = false;
        });
        patient_pregnancyList.forEach(function(item) {
            item.disabled = false;
        });
        patient_malignantList.forEach(function(item) {
            item.disabled = false;
        });
        pattient_address.readOnly = false;
        patient_phone.readOnly = false;
        haveAllergyA.style.pointerEvents = "auto";
        noHaveAllergyA.style.pointerEvents = "auto";
        drug_name.readOnly = false;
        smokingA.style.pointerEvents = "auto";
        noSmokingA.style.pointerEvents = "auto";
        initativeWay.style.pointerEvents = "auto";
        passivityWay.style.pointerEvents = "auto";
        smoking_years.readOnly = false;
        smoking_amount.readOnly = false;
        quitSmoking.style.pointerEvents = "auto";
        noQuitSmoking.style.pointerEvents = "auto";
        quit_years.readOnly = false;
        medical_historyList.forEach(function(item) {
            item.disabled = false;
        });
        tagList.querySelectorAll("input[type='checkbox']").forEach(function(item) {
            item.disabled = false;
        });
    });
    writeDate.addEventListener("change",function(){
        jsonDataChange.writeDate = writeDate.value;
    });
    doctor_id.addEventListener("change",function(){
        jsonDataChange.doctor_id = doctor_id.value;
    });
    resourceList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.resource = item.value;
        });
    });
    patient_sexList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_sex = item.value;
        });
    });
    patient_group.addEventListener("change",function(){
        jsonDataChange.patient_group = patient_group.value;
    });
    patient_age.addEventListener("change",function(){
        jsonDataChange.patient_age = patient_age.value;
    });
    patient_name.addEventListener("change",function(){
        jsonDataChange.patient_name = patient_name.value;
    });
    patient_workList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_work = item.value;
        });
    });
    patient_edu_levelList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_edu_level = item.value;
        });
    });
    patient_marriageList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_marriage = item.value;
        });
    });
    patient_pregnancyList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_pregnancy = item.value;
        });
    });
    patient_malignantList.forEach(function(item) {
        item.addEventListener("change",function(){
            jsonDataChange.patient_malignant = item.value;
        });
    });
    pattient_address.addEventListener("change",function(){
        jsonDataChange.patient_address = pattient_address.value;
    });
    patient_phone.addEventListener("change",function(){
        jsonDataChange.patient_phone = patient_phone.value;
    });
    haveAllergyA.addEventListener("click",function(){
        jsonDataChange.patient_allergy = "yes";
    });
    noHaveAllergyA.addEventListener("click",function(){
        jsonDataChange.patient_allergy = "no";
        jsonDataChange.drug_name = "";
    });
    drug_name.addEventListener("change",function(){
        jsonDataChange.drug_name = drug_name.value;
    });
    smokingA.addEventListener("click",function(){
        jsonDataChange.is_smoke = "yes";
    });
    noSmokingA.addEventListener("click",function(){
        jsonDataChange.is_smoke = "no"; 
        jsonDataChange.smoking_type = "";
        jsonDataChange.smoking_years = "";
        jsonDataChange.smoking_amount = "";
        jsonDataChange.is_quit = "";
        jsonDataChange.quit_years = "";
    });
    initativeWay.addEventListener("click",function(){
        jsonDataChange.smoking_type = "initative";
    });
    passivityWay.addEventListener("click",function(){
        jsonDataChange.smoking_type = "passivity";
        jsonDataChange.smoking_years = "";
        jsonDataChange.smoking_amount = "";
        jsonDataChange.is_quit = "";
        jsonDataChange.quit_years = "";
    });
    smoking_years.addEventListener("change",function(){
        jsonDataChange.smoking_years = smoking_years.value;
    });
    smoking_amount.addEventListener("change",function(){
        jsonDataChange.smoking_amount = smoking_amount.value;
    });
    quitSmoking.addEventListener("click",function(){
        jsonDataChange.is_quit = "yes";
    });
    noQuitSmoking.addEventListener("click",function(){
        jsonDataChange.is_quit = "no";
        jsonDataChange.quit_years = "";
    });
    quit_years.addEventListener("change",function(){
        jsonDataChange.quit_years = quit_years.value;
    });
    const medical_historySelect = document.getElementById("medical_history");
    medical_historySelect.addEventListener("change",function(){
        medical_historyList.forEach(function(item) {
            if (item.selected){
                jsonDataChange.medical_history = item.value;
            }
        });
    });
    jsonDataChange.removeTags = [];
    tagList.querySelectorAll("input[type='checkbox']").forEach(function(item) {
        item.addEventListener("change", function() {
            if (!item.checked) {
                // 检查该值是否已存在于数组中
                if (jsonDataChange.removeTags.indexOf(item.id) === -1) {
                    jsonDataChange.removeTags.push(item.id);
                }
            } else {
                // 如果已经存在，则将其从数组中移除
                var index = jsonDataChange.removeTags.indexOf(item.id);
                if (index !== -1) {
                    jsonDataChange.removeTags.splice(index, 1);
                }
            }
        });
    });
    

    const saveButton = document.getElementById("saveButton");
    saveButton.addEventListener("click",function(){
        // console.log(jsonDataChange);
        saveChangePatientMessage(JSON.stringify(jsonDataChange));
        jsonPatientMessage = dataDetailPatient(JSON.stringify(jsonPatientId));
        showPatientMessage(jsonPatientMessage);
    });
}
//保存编辑的患者信息
function saveChangePatientMessage(jsonDataChangeString){
    axios.post("http://localhost:8080/saveChangePatientMessage",jsonDataChangeString).then(function(response){
        if (response.data.code === 200){
            alert("保存成功！");
        }else{
            alert("保存失败！");
        }
    }).catch(function(error){
        alert("保存失败！")
    });

}
// 获取患者信息 
function dataDetailPatient(jsonPatientIdString){
    axios.get("http://localhost:8080/detaDetailPatient",jsonPatientIdString).then(function(response){
        return response.data;
    }).catch(function(error){
        alert("获取患者基本信息失败！")
    });
    //测试数据
    let jsonPatientMessage = {
        "patient_id": "000001",
        "doctor_id": "000001",
        "drug_name": "阿司匹林",
        "patient_sex": "man",
        "is_quit": "yes",
        "is_smoke": "yes",
        "medical_history": "diabetes_mellitus",
        "patient_address": "北京市海淀区",
        "patient_age": "30",
        "patient_allergy": "yes",
        "patient_edu_level": "college+",
        "patient_group": "汉族",
        "patient_malignant": "noHaveMalignancy",
        "patient_marriage": "unmarried",
        "patient_name": "张三",
        "patient_phone": "13812345678",
        "patient_pregnancy": "noHavePregnancy",
        "patient_work": "miner",
        "quit_years": "1",
        "resource": "admissionNumber",
        "smoking_amount": "4",
        "smoking_type": "initative",
        "smoking_years": "10",
        "writeDate": "2024-01-01",
        "tags": [
            {
                "tag_id": "000001",
                "tag_name": "特别关注"
            },
            {
                "tag_id": "000002",
                "tag_name": "紧急"
            }
        ]
    }
    return jsonPatientMessage;
}
