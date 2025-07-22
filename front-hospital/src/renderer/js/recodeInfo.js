document.addEventListener('DOMContentLoaded', function () {
    var recodeInfoForm = document.getElementById("recodeInfoForm");
    var flag = true;

    recodeInfoForm.addEventListener("submit", function (event) {
        event.preventDefault();
        var formData = new FormData(recodeInfoForm);
        // flag = validateFormData(formData);

        var jsonObject = {};
        formData.forEach(function (value, key) {
            if (!jsonObject.hasOwnProperty(key)) {
                jsonObject[key] = value;
            } else {
                if (!Array.isArray(jsonObject[key])) {
                    jsonObject[key] = [jsonObject[key]];
                }
                jsonObject[key].push(value);
            }
        });

        console.log(jsonObject);
        var jsonString = JSON.stringify(jsonObject);
        if (flag) {
            submitFormData(jsonString);
        } else {
            alert("请填写完整信息");
        }
    });

    function submitFormData(jsonString) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/baseMessage/messageLoad", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                alert("提交成功！");
            } else if (xhr.readyState === 4) {
                alert("提交失败！");
            }
        };
        xhr.send(jsonString);
    }
});