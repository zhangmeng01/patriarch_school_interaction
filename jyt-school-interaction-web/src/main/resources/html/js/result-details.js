
/**
 * 定义全局的变量
 * */
var objx = {
    page:1,
    counts:"",
    project:[],
    us_id:"",
    delate_id:"",
    pageSize: 10
}

$(function () {
     var name_results = localStorage.getItem("name_result");
     $("#title_results").html(name_results);
    /**
     * 进入页面请求数据
     * */
    setItem();

    /**
     * 添加成绩
     * */
   $("#btn_addResult").on("click",function () {
       var id = window.location.href.split("?")[1].split("=")[1];
       addData(id);
       $(".gray_layersBox").show();
       $(".addition").show();
   });

    /**
     * 添加成绩保存
     * */
   $("#btn_saves").on("click",function () {
       saveData();
   });

    /**
     * 返回
     * */
    $("#btn_imgBack").on("click",function () {
        $(".gray_layersBox").hide();
        $(".addition").hide();
    });

    /**
     * 点击删除弹框中的确定
     * */
    $("#btn_sure").on("click",function () {
        delateItem() ;
        $(".gray_layersBox").hide();
        $(".delete_layersBox").hide();
    });

    /**
     * 点击删除弹框中的取消
     * */
    $(".btn_back").on("click",function () {          //
        $(".gray_layersBox").hide();
        $(".delete_layersBox").hide();
    });

    /**
     * 编辑成绩的返回
     * */
    $("#btn_backImg").on("click",function () {
        $(".gray_layersBox").hide();
        $(".compile").hide();
    });

    /**
     * 编辑保存
     * */
    $("#btns_save").on("click",function () {
        saveEdit();
    })
});


/**
 * 进入页面发起的请求
 * */
function setItem() {
    var id = window.location.href.split("?")[1].split("=")[1];
    //console.log(id);
    $.ajax({
        url: urls+"/jxhd/achievement/findAchievementList",
        type: "post",
        data:{
            "exId":id,
            "page":objx.page,
            "pageSize":objx.pageSize
        },
        dataType: "json",
        success: function (res) {
            console.log(res);
            if(res.data.pageInfo.list.length != ""){
                $(".nothing-box").hide();
                $(".pageContair").show();
                objx.counts = res.data.pageInfo.total;
                // console.log(res.data.pageInfo);
                var tbale_body = "";
                for(var i=0;i<res.data.pageInfo.list.length;i++){
                    var students_no = (i+1)+ (res.data.pageInfo.pageNum-1)*objx.pageSize;
                    //console.log(obj.data.pageInfo.list[i]);
                    var tt = res.data.pageInfo.list[i];
                    var table_head = "<th style='text-align: center'>教育ID</th>"+
                        "<th style='text-align: center'>学籍号</th>"+
                        "<th style='text-align: center'>姓名</th>";
                    tbale_body += "<tr>" +
                        "<td>"+res.data.pageInfo.list[i].us_educationid+"</td>"+
                        "<td>"+res.data.pageInfo.list[i].us_code+"</td>"+
                        "<td>"+res.data.pageInfo.list[i].us_name+"</td>";
                    for(var j=0;j<res.data.examSubjectList.length;j++){
                        //console.log(obj.data.examSubjectList[j].subjectName);
                        // console.log(res.data.examSubjectList[j].subjectName + tt[res.data.examSubjectList[j].subjectName]);

                        if(objx.project.indexOf(res.data.examSubjectList[j].subjectName) == -1){
                            objx.project.push(res.data.examSubjectList[j].subjectName);
                        }
                        table_head += "<th style='text-align: center'>"+res.data.examSubjectList[j].subjectName+"</th>";
                        tbale_body += "<td>"+tt[res.data.examSubjectList[j].subjectName]+"</td>";
                    }
                    table_head += "<th style='text-align: center'>总分</th>"+
                        "<th style='text-align: center'>班级排名</th>"+
                        "<th style='text-align: center'>操作</th>";
                    $("#table_head").html(table_head);
                    tbale_body +=   "<td>"+res.data.pageInfo.list[i].score+"</td>"+
                        "<td>"+students_no+"</td>"+
                        "<td us_Id='"+res.data.pageInfo.list[i].us_id+"'>"+
                        "<span class='btn_edit' onclick='editData(this)' style='margin-right: 10px;'>编辑</span>"+
                        "<span class='btn_delete' onclick='delateSure(this)'>删除</span>"+
                        "</td>"+
                        "</tr>";
                    $("#tbale_body").html(tbale_body);
                    /**
                     * 分页设置
                     * */
                    layui.use('laypage', function () {
                        var laypage = layui.laypage;
                        console.log(objx.page);
                        //执行一个laypage实例
                        laypage.render({
                            elem: 'test1' //注意，这里的 test1 是 ID，不用加 # 号
                            , count: objx.counts //数据总数，从服务端得到
                            , theme: "#4D98E2"
                            , prev: "<"
                            , next: ">"
                            ,limit: 10
                            ,curr: res.data.pageInfo.pageNum
                            , jump: function (obj, first) {
                                //obj包含了当前分页的所有参数，比如：
                                objx.page = obj.curr;
                                //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                                //console.log(obj.limit); //得到每页显示的条数
                                //首次不执行
                                if (!first) {
                                    setItem();
                                }
                            }
                        });
                    });
                }
            }else {
                if(objx.page != 1){
                    objx.page = objx.page-1;
                    setItem();
                }else{
                    $("#tbale_body").html("");
                    $(".nothing-box").show();
                    $(".pageContair").hide();
                    var table_head = "<th style='text-align: center'>教育ID</th>"+
                        "<th style='text-align: center'>学籍号</th>"+
                        "<th style='text-align: center'>姓名</th>";
                    for(var j=0;j<res.data.examSubjectList.length;j++){
                        table_head += "<th style='text-align: center'>"+res.data.examSubjectList[j].subjectName+"</th>";
                    }
                    table_head += "<th style='text-align: center'>总分</th>"+
                        "<th style='text-align: center'>班级排名</th>"+
                        "<th style='text-align: center'>操作</th>";
                    $("#table_head").html(table_head);
                }
            }
           // objx.page = res.data.pageInfo.list.length;

        }
    })
}

/**
 * 点击添加成绩
 * */
function addData(id) {
    $.ajax({
        url: urls+"/jxhd/achievement/toAaddAchievement",
        type: "post",
        data:{"exId":id},
        success:function (res) {
            console.log(res);
            if(res.code === 0){
                var students_selects = "<option>请选择学生</option>";
                var students_addResult = "";
                for(var i=0;i<res.data.userStudentList.length;i++){
                    students_selects+="<option content='"+res.data.userStudentList[i].usId+"'>"+res.data.userStudentList[i].usName+"</option>";
                }
                $("#students_selects").html(students_selects);
                for(var i=0;i<res.data.examSubjectList.length;i++){
                    students_addResult += "<tr>"+
                                                "<td class='ftps'>"+res.data.examSubjectList[i].subjectName+"</td>"+
                                                "<td><input type='text' class='result-numbers'   id='"+res.data.examSubjectList[i].esId+"' onfocus='resultNumbers(this)' onkeyup='clearNumber(this)' maxlength='5'  oninput='num(this)'></td>"+
                                           "</tr>";
                }
                $("#students_addResult").html(students_addResult);
                console.log(students_addResult);
                $("#students_addResult tr:first-child td:nth-of-type(2) input").addClass("active");
            }
        }
    })
}

/**
 * 添加成绩的保存
 * */
function saveData() {
    var id = window.location.href.split("?")[1].split("=")[1];
    /*获取下拉框学生名字*/
    var  students_selects = $("#students_selects option:selected").text();
    var us_id = "";
    if(students_selects === "" || students_selects === "请选择学生"){
        alert("请您选择学生");
        return false;
    }else {
        students_selects = students_selects;
        //us_id = $("#select_result option:selected").attr("usid");
        us_id = $('#students_selects').find("option:selected").attr("content");
        console.log(us_id);
    }
    var arr = [];
    for(var i=0;i<$("#students_addResult tr").length;i++){
        var dataCode = {};
        var tt=$("#students_addResult tr");
        var s = tt.eq(i).find('td:first-child').text();
        var inputs = tt.eq(i).find('td:first-child').next().find("input").val();
        dataCode.subjectName = s;
        if(inputs === ""){
            alert("请您填写学生的成绩");
            return false;
        }else {
            dataCode.score = inputs;
        }
        arr.push(dataCode);
    }
    console.log(arr);
    var UsAchievementDto = {
        "exId":id*1,
        "usId":us_id*1,
        "subjectScoreList":arr
    }
    $.ajax({
        url: urls+"/jxhd/achievement/addAchievement",
        type: "post",
        data: JSON.stringify(UsAchievementDto),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success:function (res) {
            /*判断是否保存成功*/
            if(res.code === 0){
                if(res.data.code === 302){
                    alert(res.data.message);
                }else if(res.data.code === 301){
                    $(".gray_layersBox").hide();
                    $(".addition").hide();
                    /*刷新页面*/
                    setItem();
                }
            }
        }
    })
}

/**
 * 点击编辑按钮
 * */
function editData(_this) {
    console.log(objx.project);
    var arrA = objx.project;
    var arrB =  [];
    var info = {};
    objx.us_id = $(_this).parent().attr("us_id");
    /*进行回显*/
    var numbers = $(_this).parent().parent();
    var tds = numbers.find("td");
    console.log(tds.length-3);
    //console.log(numbers.find("td:nth-of-type(3)").text());
    /**
     * 渲染学生的名字*/
    var students_names = numbers.find("td:nth-of-type(3)").text()
    $("#students_names").text(students_names);
    for(var i=3;i<tds.length-3;i++){
        console.log(tds.eq(i).text());
        var tt = tds.eq(i).text()
        arrB.push(tt);
    }
    function ArrayToObj(arrA, arrB) {

        for(var i = 0; i < arrA.length; i++ ) {
            info[arrA[i]] = arrB[i];
        }

        return info;
    }
    ArrayToObj(arrA, arrB);
    var htmls = "";
    for(var key in info){
        htmls += "<tr>"+
                        "<td>"+key+"</td>"+
                        "<td><input type='text' class='result-numbers' value='"+info[key]+"' onfocus='resultNumbers(this)' onkeyup='clearNumber(this)' maxlength='5'  oninput='num(this)'></td>"+
                    "</tr>";
      //  console.log(key+':'+info[key])
    }
   // console.log(htmls);
    $("#edit_contents").html(htmls);
    $(".gray_layersBox").show();
    $(".compile").show();
    $("#edit_contents tr:first-child td:nth-of-type(2) input").addClass("active");
}

/**
 * 点击编辑保存
 * */
function saveEdit() {
    var id = window.location.href.split("?")[1].split("=")[1];
    /*获取下拉框学生名字*/
    var  students_selects = $("#students_selects option:selected").text();
    var us_id = "";
    var arr = [];
    for(var i=0;i<$("#edit_contents tr").length;i++){
        var dataCode = {};
        var tt=$("#edit_contents tr");
        var s = tt.eq(i).find('td:first-child').text();
        var inputs = tt.eq(i).find('td:first-child').next().find("input").val();
        console.log(s+inputs);
        dataCode.subjectName = s;
        if(inputs === ""){
            alert("请您填写学生成绩");
            return false;
        }else {
            dataCode.score = inputs;
        }
        arr.push(dataCode);
    }
    console.log(arr);
    var UsAchievementDto = {
        "exId":id*1,
        "usId":objx.us_id*1,
        "subjectScoreList":arr
    }
    /*获取该学生的成绩*/
    $.ajax({
        url: urls+"/jxhd/achievement/updateAchievement",
        type: "post",
        data: JSON.stringify(UsAchievementDto),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success:function (res) {
            console.log(res);
            /*判断是否保存成功*/
            if(res.code === 0){
                if(res.data.code === 302){
                    alert(res.data.message);
                }else if(res.data.code === 301){
                    $(".gray_layersBox").hide();
                    $(".compile").hide();
                    /*刷新页面*/
                    setItem();
                }
            }
        }
    })
}

/**
 * 点击删除
 * */
function delateSure(item) {
    objx.delate_id = $(item).parent().attr("us_id");
    $(".gray_layersBox").show();
    $(".delete_layersBox").show();
}

/**
 * 点击删除弹框中的确定
 * */
function delateItem() {
    var id = window.location.href.split("?")[1].split("=")[1];
    /*获取要删除的id*/
    $.ajax({
        url: urls+"/jxhd/achievement/deleteAchievement",
        type: "post",
        data:{
            "exId":id*1,
            "usId":objx.delate_id
        },
        success:function (res) {
            /*判断是否删除*/
            if(res.code == 0){
                $(".gray_layersBox").hide();
                $(".delete_layersBox").hide();
                /*刷新页面*/
                setItem();
            }
        }
    })
}

/**
 * 获得焦点
 * */
function resultNumbers(_this) {
    $(".result-numbers").removeClass("active");
    $(_this).addClass("active");
};

/**
 *
 * */
function clearNumber(obj) {
    console.log($(obj).val());
    if($(obj).val()>150){
        var content = $(obj).val().substring(0,$(obj).val().length-1);
        $(obj).val(content);
    }
    if($(obj).val()*1 ===0 ){
        $(obj).val(0);
    }
    var tt = $(obj).val().substring(0,1);
    if($(obj).val().length>1 && tt ==="0"){
        var values = $(obj).val().substring(1,$(obj).val().length);
        $(obj).val(values);
    }
}

function num(obj){
    obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
    obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
    obj.value = obj.value.replace(/\.{2,}/g,""); //只保留第一个, 清除多余的
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d).*$/,'$1$2.$3'); //只能输入两个小数
}