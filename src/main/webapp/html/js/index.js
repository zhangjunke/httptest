var msgList=new Map([]);
var caseList=new Map([]);
var timeList=new Map([]);
var codeList=new Map([]);
var conditionList=new Map([]);
var urlprefix="";
function  searchSubmit() {
    var departmentV=document.getElementById('department');
    var nameV=document.getElementById('name');
    var serverV=document.getElementById('server');
    var APIV=document.getElementById('API');
    var data={'department':departmentV.selectedIndex.toString(),'name':nameV.selectedIndex.toString(),'server':serverV.selectedIndex.toString(),'API':APIV.selectedIndex.toString(),};
    var searchUrl=urlprefix+'/mocker/MockDetailSearch';

    $.post(searchUrl, data, function (list1) {
        var data1 = JSON.parse(list1);
        var list = data1.list;
        var listBody=document.getElementById('listBody');
        if (list != null) { //后台传回来的select选项
            listBody.innerHTML="";
            for (var i = 0; i < list.length; i++) {
               var id=list[i].id;
               var author=list[i].author;
               var mockType=list[i].mockType;
               var mockCaseName=list[i].mockCaseName;
               var mock_timeout=list[i].mock_timeout;
               var mockCode=list[i].mockCode;
               var mockCondition=list[i].mockCondition;
               var mockMsg=list[i].mockResponseMsg;
               msgList.set(JSON.stringify(i),JSON.stringify(mockMsg));
               caseList.set(JSON.stringify(i),JSON.stringify(mockCaseName));
               timeList.set(JSON.stringify(i),JSON.stringify(mock_timeout));
               codeList.set(JSON.stringify(i),JSON.stringify(mockCode));
               conditionList.set(JSON.stringify(i),JSON.stringify(mockCondition));

                var tr=document.createElement('tr');
                id=document.createElement('td');
                author=document.createElement('td');
                mockType=document.createElement('td');
                mockCaseName=document.createElement('td');
                mockCode=document.createElement('td');
                mock_timeout=document.createElement('td');
                mockCondition=document.createElement('td');
                var option=document.createElement('td');

                id.innerHTML=list[i].id;
                author.innerHTML=list[i].author;
                mockType.innerHTML=list[i].mockType;
                mockCaseName.innerHTML=list[i].mockCaseName;
                mock_timeout.innerHTML=list[i].mock_timeout;
                mockCode.innerHTML=list[i].mockCode;
                mockCondition.innerHTML=list[i].mockCondition;
                option.innerHTML="<td class=\"center\">\n" +
                    "\t<input id=\"btn1\" type=\"button\" onclick=\"viewDetail('"+i+"')\" value=\"查看\" class=\"tdBtn checkBtn\"/>\n" +
                    "\t<input type=\"button\" onclick=\"modifyDetail('"+i+"','"+list[i].id+"')\"  value=\"修改\" class=\"tdBtn modifyBtn\"/>\n" +
                    "\t<input type=\"button\" onclick=\"conditionSetting('"+list[i].id+"')\" value=\"设置\" class=\"tdBtn settingBtn\"/>\n" +
                    "\t<input type=\"button\"  onclick=\"deleteDetail('"+list[i].id+"')\" value=\"删除\" class=\"tdDelBtn\"/>\n" +
                    "    </td>";
                tr.appendChild(id);
                tr.appendChild(author);
                tr.appendChild(mockType);
                tr.appendChild(mockCaseName);
                tr.appendChild(mockCode);
                tr.appendChild(mock_timeout);
                tr.appendChild(mockCondition);
                tr.appendChild(option);
                listBody.appendChild(tr);
            }
        }

    })
    }

function  viewDetail(i) {
    var text = '<div id="pop" class="pop"><div class="title" id="title" style="width:600px;word-wrap:break-word">'+msgList.get(i)
        +'</div><div class="btn" id="btn-right" onclick="cancel(this)">'+'关闭'+'</div></div>';
    openPop("#container","#pop",500,600,text);
}
function cancel(){
    $("#container").empty();
}

function modifyDetail(i,id){

    window.open("edit_mocker.html?"+"mockCaseName="+encodeURI(caseList.get(i))+"&amp;mockCode="+
        encodeURI(codeList.get(i))+"&amp;mockTime="+encodeURI(timeList.get(i))+
        "&amp;mockMsg="+encodeURI(msgList.get(i))+"&amp;id="+encodeURI(id));
}
function deleteDetail(detailid){
    var msg = "确认删除？";
    if (confirm(msg)==true){
        var url=urlprefix+"/mocker/DeleteDetail";
        var data = {'mockDetail_id': detailid};
        $.post(url, data, function (list1) {
            alert(list1);
            if(list1=="提交成功！"){
                location.href="indexPage.html";
            }
        });
        return true;
    }else{
        return false;
    }
}

function creatDetail() {
    var name0 = document.getElementById("name");
    var API0 = document.getElementById("API");
    if (name0.selectedIndex<=0 | API0.selectedIndex<=0) {
        alert("请先选择姓名和接口名！");
    } else {
    var nameindex = name0.selectedIndex; // selectedIndex代表的是你所选中项的index
    var name = name0.options[nameindex].text;
    var APIindex = API0.selectedIndex; // selectedIndex代表的是你所选中项的index
    var API = API0.options[APIindex].text;

    window.open("createDetail.html?name=" + encodeURI(name) + "&API=" + API);
}
}

function conditionSetting(detailid){
    var url=urlprefix+"/mocker/MockConditionSearch";
    var data = {'mockDetail_id': detailid};
    var S="detailId:"+detailid+"&amp;";
    $.post(url, data, function (string) {
        var list1=string.split(",");
        for(var i=0;i<list1.length-1;i++){
            var conId=list1[i].split(":")[0];
            var conS=list1[i].split(":")[1];
            S+=conId+":"+conS+"&amp;";
        }
        window.open("conditionSetting.html?"+S);
        });
}
