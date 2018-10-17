
/**
 * 定义全局的变量
 * */
var obj = {
    pageNumber: 1,
    conts:"",
    pId:""
};


$(function () {
    yearsItem();


    /**
     * 点击确定
     * */
    $("#btn_delete").on("click",function () {

        delateData();
    });
    /**
     * 点击取消
     * */
    $("#btn_back").on("click",function () {
        $(".gray_layersBox").hide();
        $(".delete_layersBox").hide();
    });
    /**
     * 点击取消
     * */
    $("#btn_images").on("click",function () {
        $(".gray_layersBox").hide();
        $(".delete_layersBox").hide();
    });

    /**
     * 点击导入
     * */
    $("#btn_limt").on("click",function () {
        window.location.href = "result-channel.html";
    });
    /**
     * 点击查询
     * */
    $("#btn_search").on("click",function () {
            getData();
    });
    /**
     * 点击重置
     * */
    $("#btn_reset").on("click",function () {
            $("#result_values").val("");
            for(var i=0;i<$("#select_years option").length;i++){
                if($("#select_years option").eq(i).text() === "请选择"){
                    $("#select_years option").eq(i).prop("selected",true);
                    console.log($("#select_years option")[i]);
                }
            }
            for(var i=0;i<$("#select_result option").length;i++){
                if($("#select_result option").eq(i).text() === "请选择"){
                    $("#select_result option").eq(i).prop("selected",true);
                    console.log($("#select_result option")[i]);
                }
            }
            getData();

    });
});


/**
 *上面学年的方法
 * */
function yearsItem() {
    $.ajax({
        url: urls+"/jxhd/exam/findSemesterAndClass",
        type:"post",
        dataType: "json",
        success: function (res) {
            console.log(res);
            if(res.code ===0){
                var selectHtml = "<option value=''>请选择</option>";
                for(var i=0;i<res.data.variableYearList.length;i++){
                    selectHtml += "<option value=''>"+ res.data.variableYearList[i].vaYearNum +"</option>";
                }
                $("#select_years").html(selectHtml);
            }
            $("#result_values").val("");
            for(var i=0;i<$("#select_years option").length;i++){
                if($("#select_years option").eq(i).text() === "请选择"){
                    $("#select_years option").eq(i).prop("selected",true);
                    console.log(i);
                }
            }
            for(var i=0;i<$("#select_result option").length;i++){
                if($("#select_result option").eq(i).text() === "请选择"){
                    $("#select_result option").eq(i).prop("selected",true);
                }
            }
            getData();
        }
    })
}


/**
* 封装调用ajax加载页面的方法
* */
function getData() {
    /**
    *获取输入的词
     */
    var  result_values = $("#result_values").val() === ""?"":$("#result_values").val();
    var  select_years = $("#select_years option:selected").text();
    var  select_result = $("#select_result option:selected").text();
    if(select_years === "" || select_years === "请选择"){
        select_years = "";
    }else {
        select_years = select_years;
    }
    if(select_result === "" || select_result === "请选择"){
        select_result = "";
    }else {
        select_result = select_result;
    }
    $.ajax({
        url: urls+"/jxhd/exam/findExamList",
        type: "post",
        data: {
            "exName": result_values,
            "vaYearNum": select_years,
            "exClassify": select_result,
            "pageNum": obj.pageNumber,
            "pageSize":10
        },
        dataType: "json",
        success: function (res) {
            //console.log(res);
            if(res.code === 0){
                if(res.data.list.length != 0){
                    $(".nothing-box").hide();
                    $(".pageContair").show();
                    obj.conts = res.data.total;
                    var tableHtml = "";
                    for(var i=0;i<res.data.list.length;i++){
                        console.log(res.data.list[i].vaYearNum);
                        $("#ciName").html("成绩管理："+ res.data.list[i].grName + res.data.list[i].ciName);
                        tableHtml +="<tr>"+
                                        "<td>"+ res.data.list[i].vaYearNum + "</td>"+
                                        "<td>"+ res.data.list[i].exName +"</td>"+
                                        "<td>"+ res.data.list[i].exClassify +"</td>"+
                                        "<td>"+ formatDateTime(res.data.list[i].createTime) +"</td>"+
                                        "<td priId='"+ res.data.list[i].exId +"'>"+
                                            "<span class='details_btn' onclick='detailsOnclick(this)'>详情</span>"+
                                            "<span class='delete_btn' onclick='delateOnclick(this)'>删除</span>"+
                                        "</td>"+
                                     "</tr>";
                    }
                    $("#content_data").html(tableHtml);


                    /**
                     * 分页设置
                     * */
                    layui.use('laypage', function(){
                        var laypage = layui.laypage;

                        //执行一个laypage实例
                        laypage.render({
                            elem: 'test1' //注意，这里的 test1 是 ID，不用加 # 号
                            ,count: obj.conts //数据总数，从服务端得到
                            ,theme: "#4D98E2"
                            ,prev:"<"
                            ,next:">"
                            ,limit: 10
                            ,curr: res.data.pageNum
                            ,jump: function(objx, first){
                                //obj包含了当前分页的所有参数，比如：
                                obj.pageNumber = objx.curr;
                                //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                                //console.log(obj.limit); //得到每页显示的条数
                                //首次不执行
                                if(!first){
                                    getData();
                                }
                            }
                        });
                    });
                }else{
                    if(obj.pageNumber != 1){
                        obj.pageNumber = obj.pageNumber-1;
                        getData();
                    }else{
                        $("#content_data").html("");
                        $(".nothing-box").show();
                        $(".pageContair").hide();
                    }
                }

            }else{
                $(".nothing-box").show();
                $("#content_data").html("");
                $(".pageContair").hide();
            }
        }
    })
}





/**
 * 时间戳转化为正常时间
 * */
function formatDateTime(inputTime) {
    var date = new Date(inputTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d+' '+h+':'+minute;
};



/**
 * 详情方法
 * */
function detailsOnclick(obj) {
    saveData();
    var id = $(obj).parent().attr("priId");
    var name_result = $(obj).parent().parent().find("td:nth-of-type(2)").text();
    localStorage.setItem("name_result",name_result);
    window.location.href = "result-details.html?id="+id;
}


/**
 * 点击删除
 * */
function delateOnclick(tt){
    var pId = $(tt).parent().attr("priid");
    obj.pId = pId;
    $(".gray_layersBox").show();
    $(".delete_layersBox").show();
};


/**
 *删除的方法
 * */
function delateData() {
    $.ajax({
        url: urls+"/jxhd/exam/deleteExam",
        type: "post",
        data: {
            "exId": obj.pId
        },
        dataType: "json",
        success: function (res) {
            if(res.code === 0){
                $(".gray_layersBox").hide();
                $(".delete_layersBox").hide();
                getData();
            }
        }
    })
}


/**
 * 对数据存储进行回显
 * */
function saveData() {

}


