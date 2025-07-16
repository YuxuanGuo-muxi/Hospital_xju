//患者列表
let patientListVue = new Vue({
    el: '#patientList',
    data: {
        jsonPatientList: {}
    },
    methods: {
        removePatient : function(patientId){
            axios.post("http://localhost:8080/adminInterface/removePatient?patientId="+patientId).then(function(response){
                alert("患者删除成功！");
                patientListVue.jsonPatientList = patientListVue.dataPatientList(patientPageListVue.pageIndex);
            }).catch(function(error){
                alert("患者删除失败！");
            })
        },
        // 获取患者列表数据
        dataPatientList : function(pageIndex = 0){
            axios.get("http://localhost:8080/adminInterface/dataPatientList?pageIndex="+pageIndex).then(function(response){
                return response.data;
            }).catch(function(error){
                alert("患者列表获取失败！");
            })
            let jsonPatientList = {
                "patientList": [
                    {
                        "patientId": "000001",
                        "patientName": "患者1",
                        "patientTag": "标签1",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient1.png"
                    },
                    {
                        "patientId": "000002",
                        "patientName": "患者2",
                        "patientTag": "标签2",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient2.png"
                    },
                    {
                        "patientId": "000003",
                        "patientName": "患者3",
                        "patientTag": "标签3",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient1.png"
                    },
                    {
                        "patientId": "000004",
                        "patientName": "患者4",
                        "patientTag": "标签4",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient2.png"
                    },
                    {
                        "patientId": "000005",
                        "patientName": "患者5",
                        "patientTag": "标签5",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient1.png"
                    },
                    {
                        "patientId": "000006",
                        "patientName": "患者6",
                        "patientTag": "标签6",
                        "nextTime": "2025-07-29",
                        "image": "../image/patient2.png"
                    }
                ]
            };
            return jsonPatientList;
        }
    },
    mounted() {
        this.jsonPatientList = this.dataPatientList();
    }
})
//患者页码列表
let patientPageListVue = new Vue({
    el: '#patientPageList',
    data: {
        // 定义分页数据
        pageIndex: 0
    },
    methods: {
        first : function() {
            this.pageIndex -= 1;
            patientListVue.jsonPatientList = patientListVue.dataPatientList(this.pageIndex);
        },
        third : function() {
            this.pageIndex += 1;
            patientListVue.jsonPatientList = patientListVue.dataPatientList(this.pageIndex);
        }
    }
});
//医生账户列表
let doctorListVue = new Vue({
    el: '#doctorList',
    data: {
        jsonDoctorList: {}
    },
    methods: {
        dataDoctorList : function(pageIndex = 0){
            axios.get("http://localhost:8080/adminInterface/dataDoctorList?pageIndex="+pageIndex).then(function(response){
                return response.data;
            }).catch(function(error){
                alert("医生列表获取失败！");
            })
            let jsonDoctorList = {
                "doctorList": [
                    {
                        "doctorId": "000001",
                        "doctorAccount": "d12d15",
                        "doctorPassword": "12345678901",
                        "doctorName": "医生1"
                    },
                    {
                        "doctorId": "000002",
                        "doctorAccount": "d15343",
                        "doctorPassword": "12345678902",
                        "doctorName": "医生2"
                    },
                    {
                        "doctorId": "000003",
                        "doctorAccount": "d93nf8",
                        "doctorPassword": "12345678903",
                        "doctorName": "医生3"
                    },
                    {
                        "doctorId": "000004",
                        "doctorAccount": "eu9e123",
                        "doctorPassword": "12345678904",
                        "doctorName": "医生4"
                    },
                    {
                        "doctorId": "000005",
                        "doctorAccount": "d12d15",
                        "doctorPassword": "12345678905",
                        "doctorName": "医生5"
                    },
                    {
                        "doctorId": "000006",
                        "doctorAccount": "d15343",
                        "doctorPassword": "12345678906",
                        "doctorName": "医生6"
                    }
                ]
            };
            return jsonDoctorList;
        }
    },
    mounted() {
        this.jsonDoctorList = this.dataDoctorList();
    }
})
//医生页码列表
let doctorPageListVue = new Vue({
    el: '#doctorPageList',
    data: {
        // 定义分页数据
        pageIndex: 0
    },
    methods: {
        first : function() {
            this.pageIndex -= 1;
            doctorListVue.jsonDoctorList = doctorListVue.dataDoctorList(this.pageIndex);
        },
        third : function() {
            this.pageIndex += 1;
            doctorListVue.jsonDoctorList = doctorListVue.dataDoctorList(this.pageIndex);
        }
    }
});
//医生账号申请
let doctorApplyListVue = new Vue({
    el: '#doctorApplyList',
    data: {
        jsonDoctorApplyList: {}
    },
    methods: {
        dataDoctorApplyList : function(pageIndex = 0){
            axios.get("http://localhost:8080/adminInterface/dataDoctorApplyList?pageIndex="+pageIndex).then(function(response){
                return response.data;
            }).catch(function(error){
                alert("医生申请列表获取失败！");
            })
            let jsonDoctorApplyList = {
                "doctorApplyList": [
                    {
                        "doctorAccount": "eu9e123",
                        "doctorPassword": "12345678901",
                        "applyMessage": "我是张医生，申请账号"
                    },
                    {
                        "doctorAccount": "d12d15",
                        "doctorPassword": "12345678902",
                        "applyMessage": "我是李医生，申请账号"
                    },
                    {
                        "doctorAccount": "d15343",
                        "doctorPassword": "12345678903",
                        "applyMessage": "我是王医生，申请账号"
                    },
                    {
                        "doctorAccount": "d93nf8",
                        "doctorPassword": "12345678904",
                        "applyMessage": "我是陈医生，申请账号"
                    },
                    {
                        "doctorAccount": "d93nf8",
                        "doctorPassword": "12345678907",
                        "applyMessage": "我是杨医生，申请账号"
                    },
                    {
                        "doctorAccount": "d12d15",
                        "doctorPassword": "12345678908",
                        "applyMessage": "我是熊医生，申请账号"
                    }
                ]
            };
            return jsonDoctorApplyList;
        },
        agreeApply : function(doctorAccount){
            axios.post("http://localhost:8080/adminInterface/agreeApply?doctorAccount="+doctorAccount).then(function(response){
                alert("医生账号申请已同意！");
            }).catch(function(error){
                alert("同意失败！");
            })
            doctorApplyListVue.jsonDoctorApplyList = doctorApplyListVue.dataDoctorApplyList(doctorApplyPageListVue.pageIndex);
        }
    },
    mounted() {
        this.jsonDoctorApplyList = this.dataDoctorApplyList();
    }
})
//医生申请页码列表
let doctorApplyPageListVue = new Vue({
    el: '#doctorApplyPageList',
    data: {
        // 定义分页数据
        pageIndex: 0
    },
    methods: {
        first : function() {
            this.pageIndex -= 1;
            doctorApplyListVue.jsonDoctorApplyList = doctorApplyListVue.dataDoctorApplyList(this.pageIndex);
        },
        third : function() {
            this.pageIndex += 1;
            doctorApplyListVue.jsonDoctorApplyList = doctorApplyListVue.dataDoctorApplyList(this.pageIndex);
        }
    }
});
//标签列表
let tagListVue = new Vue({
    el: '#tagList',
    data: {
        jsonTagList: {}
    },
    methods: {
        dataTagList : function(pageIndex = 0){
            axios.get("http://localhost:8080/adminInterface/dataTagList?pageIndex="+pageIndex).then(function(response){
                return response.data;
            }).catch(function(error){
                alert("标签列表获取失败！");
            })
            let jsonTagList = {
                "tagList": [
                    {
                        "tagId": "000001",
                        "tagName": "标签1",
                        "tagDisabled": false
                    },
                    {
                        "tagId": "000002",
                        "tagName": "标签2",
                        "tagDisabled": false
                    },
                    {
                        "tagId": "000003",
                        "tagName": "标签3",
                        "tagDisabled": false
                    },
                    {
                        "tagId": "000004",
                        "tagName": "标签4",
                        "tagDisabled": true
                    }
                ]
            };
            return jsonTagList;
        },
        disabledTag : function(tagId){
            axios.post("http://localhost:8080/adminInterface/disabledTag?tagId="+tagId).then(function(response){
                alert("标签禁用成功！");
            }).catch(function(error){
                alert("标签禁用失败！");
            })
        },
        removeTag : function(tagId){
            axios.post("http://localhost:8080/adminInterface/removeTag?tagId="+tagId).then(function(response){
                alert("标签删除成功！");
            }).catch(function(error){
                alert("标签删除失败！");
            })
            tagListVue.jsonTagList = tagListVue.dataTagList(tagPageListVue.pageIndex);
        }
    },
    mounted() {
        this.jsonTagList = this.dataTagList();
    }
})
//标签页码列表
let tagPageListVue = new Vue({
    el: '#tagPageList',
    data: {
        // 定义分页数据
        pageIndex: 0
    },
    methods: {
        first : function() {
            this.pageIndex -= 1;
            tagListVue.jsonTagList = tagListVue.dataTagList(this.pageIndex);
        },
        third : function() {
            this.pageIndex += 1;
            tagListVue.jsonTagList = tagListVue.dataTagList(this.pageIndex);
        }
    }
});
//创造标签
let createTagVue = new Vue({
    el: '#createTag',
    data: {
        tagName: ""
    },
    methods: {
        createTag : function(tagName){
            axios.post("http://localhost:8080/adminInterface/createTag?tagName="+tagName).then(function(response){
                alert("标签创建成功！");
                createTagVue.tagName = "";
                tagListVue.jsonTagList = tagListVue.dataTagList(tagPageListVue.pageIndex);
            }).catch(function(error){
                alert("标签创建失败！");
            })
        },
        createButton : function(){
            if(this.tagName == ""){
                alert("标签名称不能为空！");
            }else{
                this.createTag(this.tagName);
            }
        }
    }
});
                    
