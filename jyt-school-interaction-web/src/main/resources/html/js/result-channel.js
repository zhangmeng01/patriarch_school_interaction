
var objx = {
    flag:true
}
$(function () {
    /**
     * 刚进页面时的数据渲染
     * */
    nowItem();
    /**
     * change向input中添加name
     * */
    $("#btn_file").on("change",function () {
        var imgFile = this.files[0];
        var fr = new FileReader();
        fr.readAsDataURL(imgFile);
        objx.flag = false;
        fr.onload = function() {
            $("#input_value").val(imgFile.name);

        }
    });

    /**
     * 选择考试类型
     * */
    $("#result_types").on("change",function () {
        var result_infos = $("#result_infos option:selected").text();
        var result_types = $("#result_types option:selected").text();
        if(result_types === "考试类型"){
            return;
        }else {
            if(result_infos === "学年"){
                return;
            }else{
                $("#result_contents").val(result_infos + result_types);
                existName();
            }
        }
    });

    /**
     * 选择考试信息
     * */
     $("#result_infos").on("change",function () {
         var result_infos = $("#result_infos option:selected").text();
         var result_types = $("#result_types option:selected").text();
         if(result_infos === "学年"){
             return;
         }else{
             if(result_types === "考试类型"){
                 return;
             }else{
                 $("#result_contents").val(result_infos + result_types);
                 existName();
             }
         }
     })

    /**
     *失去焦点
     * */
    $("#result_contents").on("input",function () {
        existName();
    })

    /**
     * 点击导入失败的确定
     * */
    $("#btns_sures").on("click",function () {
        $(".gray_layersBox").hide();
        $(".error_layersBox").hide();
    })

    /**
     *点击返回列表
    * */
    $("#btn_backs").on("click",function () {
        window.location.href = "./result-list.html";
    })

    /**
     * 点击下载模板
     * */
    $("#btn_download").on("click",function () {
        window.location.href = urls + "/jxhd/excel/exportExamTmp";
    })


});


/**
 * 刚进页面时的数据渲染
* */

function nowItem() {
    $.ajax({
        url: urls+"/jxhd/exam/findSemesterAndClass",
        type: "post",
        dataType: "json",
        success: function (res) {
            /*进行数据渲染*/
            console.log(res.data.variableYearList);
            $("#result_class").html(res.data.grName + res.data.ciName);
            var htmlOptions = "";
            for(var i=0;i<res.data.variableYearList.length;i++){
                htmlOptions += "<option>"+res.data.variableYearList[i].vaYearNum+"</option>";
            }
            $("#result_infos").append(htmlOptions);
        }
    })
};


/**
 * 点击导入
 * */
function fsubmit() {
    var result_infos = $("#result_infos option:selected").text();
    var result_types = $("#result_types option:selected").text();
    var input_value = $("#input_value").val();
    var result_contents = $("#result_contents").val().replace(/\s/g, "");
    if(input_value === ""){
        alert("请选择要导入的文件");
        return false;
    };
    if(objx.flag){
        alert("请选择要导入的文件");
        return false;
    }
    if(result_infos === "学年"){
        alert("请选择学年");
        return false;
    }
    if(result_types === "考试类型"){
        alert("请选择考试类型");
        return false;
    }
    if(result_contents === ""){
        alert("请输入考试名称");
        return false;
    };
    var form=document.getElementById("form1");
    var fd =new FormData(form);
    /*学期*/
    fd.append('vaYearNum',result_infos);
    /**/
    fd.append('classIfy',result_types);
    fd.append('exName',result_contents);
    /**
     * 控制导入的显示
    * */
    $(".gray_layersBox").show();
    $(".result_layersBox").show();
    $.ajax({
        url:urls+"/jxhd/excel/importExamTmp",
        type:"post",
        data:fd,
        processData: false,  // 告诉jQuery不要去处理发送的数据
        contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
        success:function (res) {
            if(res.data.code === 101){
                setTimeout(function () {
                    $(".result_layersBox").hide();
                    $(".suceess_layersBox").show();
                    $("#success_texts").html(res.data.message);
                    setInterval(function () {
                        $(".gray_layersBox").hide();
                        $(".suceess_layersBox").hide();
                        window.location.href = "result-list.html";
                    },2000);
                },2000);


            }else if(res.data.code === 102){
                setTimeout(function () {
                    $(".result_layersBox").hide();
                    $(".suceess_layersBox").hide();
                    $(".error_layersBox").show();
                },2000)

                $("#numbers_errors").html("导入失败："+ res.data.excelWarnDtoList.length + "条数据不符合规范");
                var liHtml = "";
                for(var i=0;i<res.data.excelWarnDtoList.length;i++){
                    liHtml += "<li>请修改第"+res.data.excelWarnDtoList[i].column+"行第"+res.data.excelWarnDtoList[i].row + "列，" + res.data.excelWarnDtoList[i].explain+"</li>";
                }
                $("#message_error").html(liHtml);
            }else if(res.data.code === 103){
                setTimeout(function () {
                    $(".result_layersBox").hide();
                    $(".suceess_layersBox").hide();
                    $(".error_layersBox").show();
                },2000)

                $("#numbers_errors").html( res.data.message);
            }else if(res.data.code === 104){
                setTimeout(function () {
                    $(".result_layersBox").hide();
                    $(".suceess_layersBox").hide();
                    $(".error_layersBox").show();
                },2000)

                $("#numbers_errors").html(res.data.message);
            }
        }
    })
}

/**
 * 点击取消按钮
 * */
function btn_backList() {
    window.location.href = "./result-list.html";
    if(window.event){
        window.event.returnValue = false;
    }else{
        event.preventDefault();//for firefox
    }
}


/**
 * 封装配判断名称是否存在
 * */
function existName(){
    var result_infos = $("#result_infos option:selected").text();
    var result_types = $("#result_types option:selected").text();
    var result_contents = $("#result_contents").val().replace(/\s/g, "");
    if(result_infos == "学年"){
        return false;
    }
    if(result_types == "考试类型"){
        return false;
    }
    $.ajax({
        url:urls+"/jxhd/exam/findExamIsExists",
        type:"post",
        data:{
            "vaYearNum":result_infos,
            "exClassify":result_types,
            "exName":result_contents
        },
        success:function (res) {
            console.log(res);
            if(res.data.code === 202){
                $(".reminder").hide();
            }else if(res.data.code === 201){
                $(".reminder").show();
            }
        }
    })
}



