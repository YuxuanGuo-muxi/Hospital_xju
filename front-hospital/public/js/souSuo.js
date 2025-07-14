let jsonDataSearch = {
    "tagList": [],
    "searchText": "",
    "pageIndex": 0,
    "sortMode": ""
};
//页码列表
let pageListVue = new Vue({
    el: '#pageList',
    data: {
        // 定义分页数据
        pageIndex: jsonDataSearch.pageIndex
    },
    methods: {
        first : async function() {
            jsonDataSearch.pageIndex -= 1;
            this.pageIndex -= 1;
            // console.log(jsonDataSearch);
            let jsonPatientList = await patientListVue.dataPatientLoad(JSON.stringify(jsonDataSearch));
            patientListVue.creatPatientList(jsonPatientList);
        },
        third : async function() {
            jsonDataSearch.pageIndex += 1;
            this.pageIndex += 1;
            // console.log(jsonDataSearch);
            let jsonPatientList = await patientListVue.dataPatientLoad(JSON.stringify(jsonDataSearch));
            patientListVue.creatPatientList(jsonPatientList);
        }
    }
});
//标签列表
let lableListVue = new Vue({
    el: '#tagCheckbox',
    data: {
        // 定义标签数据
        jsonDataLabel: {}
    },
    methods: {
        dataLabelLoad : async function(){
            //向后台请求标签数量前五的数据
            try{
                const response = await axios.get('http://localhost:8080/souSuo/dataLabelLoad');
                if (response.data.result == "true"){
                    let jsonLabelList = {
                        "tagList": []
                    };
                    response.data.data.forEach(function(label){
                        jsonLabelList.tagList.push(label);
                    });
                    return jsonLabelList;
                }else{
                    alert("标签列表请求失败！");
                }
            } catch (error) {
                alert("标签列表请求失败！");
                let jsonLabelList = {
                    "tagList": [
                        {
                            "tagId": "1",
                            "tagName": "标签1",
                            "count": "10"
                        },
                        {
                            "tagId": "2",
                            "tagName": "标签2",
                            "count": "20"
                        },
                        {
                            "tagId": "3",
                            "tagName": "标签3",
                            "count": "30"
                        },
                        {
                            "tagId": "4",
                            "tagName": "标签4",
                            "count": "40"
                        },
                        {
                            "tagId": "5",
                            "tagName": "标签5",
                            "count": "50"
                        }
                    ]
                };
                return jsonLabelList ;
            }
        },
        checkBoxChange: async function(tagName, isChecked) {
            if (isChecked){
                if (jsonDataSearch.tagList.includes(tagName) == false){
                    jsonDataSearch.tagList.push(tagName);
                }
            }else{
                if (jsonDataSearch.tagList.includes(tagName) == true){
                    jsonDataSearch.tagList.splice(jsonDataSearch.tagList.indexOf(tagName), 1);
                }
            }
            // console.log(jsonDataSearch);
            let jsonPatientList = await patientListVue.dataPatientLoad(JSON.stringify(jsonDataSearch));
            // console.log(jsonPatientList);
            patientListVue.creatPatientList(jsonPatientList);
        }
    },
    async mounted(){
        this.jsonDataLabel = await this.dataLabelLoad();
    }
});
//患者列表
let patientListVue = new Vue({
    el: '#divPatientList',
    data: {
        // 定义患者数据
        jsonDataPatient: {}
    },
    methods: {
        creatPatientList: function(jsonPatientList) {
            this.jsonDataPatient = jsonPatientList;
        },
        dataPatientLoad: async function (jsonDataSearchString) {
            try {
                const response = await axios.get('http://localhost:8081/patient/get', jsonDataSearchString);
                if (response.data.result == "true"){
                    alert("患者列表请求成功！");
                    let jsonPatientList = {
                        "patientList": []
                    }
                    response.data.data.forEach(function(patient){
                        jsonPatientList.patientList.push(patient);
                    })
                    return jsonPatientList;
                }else{
                    alert("患者列表请求失败！");
                }
            } catch (error) {
                alert("患者列表请求失败！");
                let jsonPatientList = {
                    "patientList": [
                        {
                            "patientId": "000001",
                            "patientName": "患者1",
                            "patientTag": "标签1",
                            "nextTime": "2024-05-01",
                            "result": "患者1的结果",
                            "representation": "患者1的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000002",
                            "patientName": "患者2",
                            "patientTag": "标签2",
                            "nextTime": "2024-05-02",
                            "result": "患者2的结果",
                            "representation": "患者2的表征",
                            "image": "../image/patient2.png"
                        },
                        {
                            "patientId": "000003",
                            "patientName": "患者3",
                            "patientTag": "标签3",
                            "nextTime": "2024-05-03",
                            "result": "患者3的结果",
                            "representation": "患者3的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000004",
                            "patientName": "患者4",
                            "patientTag": "标签4",
                            "nextTime": "2024-05-04",
                            "result": "患者4的结果",
                            "representation": "患者4的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000005",
                            "patientName": "患者5",
                            "patientTag": "标签5",
                            "nextTime": "2024-05-05",
                            "result": "患者5的结果",
                            "representation": "患者5的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000006",
                            "patientName": "患者6",
                            "patientTag": "标签1",
                            "nextTime": "2024-05-06",
                            "result": "患者6的结果",
                            "representation": "患者6的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000007",
                            "patientName": "患者7",
                            "patientTag": "标签2",
                            "nextTime": "2024-05-07",
                            "result": "患者7的结果",
                            "representation": "患者7的表征",
                            "image": "../image/patient1.png"
                        },
                        {
                            "patientId": "000008",
                            "patientName": "患者8",
                            "patientTag": "标签3",
                            "nextTime": "2024-05-08",
                            "result": "患者8的结果",
                            "representation": "患者8的表征",
                            "image": "../image/patient1.png"
                        }
                    ]
                }
                return jsonPatientList;
            }
        },
        searchButtonListeners : function(){
            //搜索按钮事件绑定
            const searchButton = document.getElementById('searchButton');
            searchButton.addEventListener('click',async function() {
                const searchText = document.getElementById('searchText').value;
                jsonDataSearch.searchText = searchText;
                // console.log(jsonDataSearch);
                if (searchText == ""){
                    alert("请输入搜索内容！");
                }else{
                    // console.log(jsonDataSearch);
                    jsonPatientList = await patientListVue.dataPatientLoad(JSON.stringify(jsonDataSearch));
                    patientListVue.creatPatientList(jsonPatientList);
                }
            });
        },
        filterSearchListeners : function(){
            const sortModeForm = document.getElementById("sortModeForm");
            sortModeForm.querySelectorAll('input[type="radio"]').forEach(function(sortMode) {
                sortMode.addEventListener('change',async function() {
                    if (this.checked) {
                        // 单选按钮被选中时执行的操作
                        jsonDataSearch.sortMode = this.value;
                        // console.log(jsonDataSearch);
                        jsonPatientList = await patientListVue.dataPatientLoad(JSON.stringify(jsonDataSearch));
                        patientListVue.creatPatientList(jsonPatientList);
                    }
                });
            });
        }        
    },
    async mounted(){
        this.jsonDataPatient = await this.dataPatientLoad(JSON.stringify(jsonDataSearch));
        this.searchButtonListeners();
        this.filterSearchListeners();
    }
});
