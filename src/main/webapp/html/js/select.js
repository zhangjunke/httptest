var urlprefix="";
var url_1=urlprefix+"/mocker/index/type=department";
var url_2=urlprefix+"/mocker/index/type=server";
var url_3 =urlprefix+"/mocker/selectOption/type=department";
var url_4 =urlprefix+"/mocker/selectOption/type=server";
var arr_department = ["请选择部门"];
var arr_server = ["请选择系统"];
var arr_name = [["请选择姓名"]];
var arr_API = [["请选择接口"]];
$.ajax({
    type:"get",
    url:url_1,//访问后台去数据库查询select的选项
    success:function(departmentList){
        var data1=JSON.parse(departmentList);
        var data = data1.list;
        var unitObj=document.getElementById("department"); //页面上的<html:select>元素
        unitObj.options.add(new Option("请选择部门",0));
        if(data!=null){ //后台传回来的select选项
            for(var i=0;i<data.length;i++){
                //遍历后台传回的结果，一项项往select中添加option
                unitObj.options.add(new Option(data[i].name,data[i].id));
                arr_department.join(data[i].name,data[i].id);
            }
        }
    },
    error:function(){
        alert('Error');
    }
});


$.ajax({
    type:"get",
    url:url_2,//访问后台去数据库查询select的选项
    success:function(serverList){
        var data1=JSON.parse(serverList);
        var data = data1.list;
        var unitObj=document.getElementById("server"); //页面上的<html:select>元素
        unitObj.options.add(new Option("请选择系统",0));
        if(data!=null){ //后台传回来的select选项
            for(var i=0;i<data.length;i++){
                //遍历后台传回的结果，一项项往select中添加option
                unitObj.options.add(new Option(data[i].name,data[i].id));
                arr_server.join(data[i].name,data[i].id);
            }
        }
    },
    error:function(){
        J.alert('Error');
    }
});

    function  changeSelect1(index) {
        department.selectedIndex = index;
        var selectedOption1 = document.form1.department.options[index].text;
        var data = {'name': selectedOption1};
        $.post(url_3, data, function (list1) {
            arr_name = [["请选择姓名"]];
            var data1 = JSON.parse(list1);
            var list = data1.list;
            $("#name").empty();
            var unitObj = document.getElementById("name"); //页面上的<html:select>元素
            unitObj.options.add(new Option("请选择姓名", 0));
            if (list != null) { //后台传回来的select选项
                for (var i = 0; i < list.length; i++) {
                    //遍历后台传回的结果，一项项往select中添加option
                    unitObj.options.add(new Option(list[i].name, list[i].id));
                    arr_name.join(list[i].name, list[i].id);
                }
            }
        })
    }

        function  changeSelect2(index) {
            server.selectedIndex = index;
            var selectedOption1 = document.form1.server.options[index].text;
            var data = {'name': selectedOption1};
            $.post(url_4, data, function (list1) {
                arr_API = [["请选择接口"]];
                var data1 = JSON.parse(list1);
                var list = data1.list;
                $("#API").empty();
                var unitObj = document.getElementById("API"); //页面上的<html:select>元素
                unitObj.options.add(new Option("请选择接口", 0));
                if (list != null) { //后台传回来的select选项
                    for (var i = 0; i < list.length; i++) {
                        //遍历后台传回的结果，一项项往select中添加option
                        unitObj.options.add(new Option(list[i].name, list[i].id));
                        arr_API.join(list[i].name, list[i].id);
                    }
                }
            })
        }
