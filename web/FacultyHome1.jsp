<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% int i = 0;%>
<%
    if(session.getAttribute("isLogin")!="true"){
        response.sendRedirect("index.jsp");
    }
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Faculty Home 1</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="bootstrap/css/dashboard.css" rel="stylesheet">
<link href="bootstrap/css/mycss.css" rel="stylesheet">
<script src="bootstrap/js/jquery-2.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script src="bootstrap/js/myJavaScript.js"></script>
<script>
    function getCheckForAdd(){
        var addIds = document.getElementsByName("addId");
        var idArr = [];
        var count = 0;
        for(var i = 0; i < addIds.length; i++){
            if(addIds[i].checked){
                idArr.push(addIds[i].value);
                count++;
            }
        }
        if(count == 0){
            alert('Please Choose One Grant At Least!');
        }else{
            if(confirm('Confirm Add '+count+' items?')){
                var url = "addGrantByFM.action?userName=<%=session.getAttribute("userName")%>&ids="+idArr;
                window.location = url;
            }
        }
    }
</script>
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
          	<span class="icon-bar"></span>
          	<span class="icon-bar"></span>
          	<span class="icon-bar"></span>
          </button>
        </div>
        
        <div class="navbar-collapse collapse">
          <label style="color:#FFF; font-size:18px; margin-top:10px; margin-left:80px;">Hi,<%=session.getAttribute("userName")%></label>
          <form class="navbar-form navbar-right"> 
            <input type="text" id="text" class="form-control">
            <button type="button" class="form-control"><span class="glyphicon glyphicon-search"></span></button>
          </form>
       </div>
      </div>
    </div>
      
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
          <h2 class="sub-header">Category</h2>
          <div class="table-responsive">
            <table id="tab2" class="table table-hover">
              <thead>
                <tr>
                  <!--<th><input type="checkbox" onclick="CheckAll2(this.checked)"></th>-->
                  <th>Opt</th>
                  <th>Id</th>
                  <th>Seires</th>
                  <th>Title</th>
                  <th>RSO Deadline</th>
                  <th>SRO Deadline</th>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="grantList">
                    <tr>
                        <!--<td>
                            <a style="margin-left:-5px;">
                                <button class="btn btn-danger btn-xs" onclick="window.location='addGrantByFM.action?grantId=<%=request.getAttribute("grantId")%>&userName=<%=session.getAttribute("userName")%>'">
                                    <span class="glyphicon glyphicon-heart"></span>
                                </button>
                            </a>
                        </td>-->
                        <td><input type="checkbox" name="addId" value="<%=request.getAttribute("grantId")%>" <% if(request.getAttribute("grantChecked")=="disabled"){%>disabled="disabled"><%}%></td>
                        <td><%=++i%></td>
                        <!--注意这里面的属性是bean的属性名称而不是数据库中的属性名称-->
                        <td><%=request.getAttribute("grantSeries")%></td>
                        <td><a href="showDetail.action?grantId=<%=request.getAttribute("grantId")%>" target="_blank"><%=request.getAttribute("grantTitle")%></a></td>
                        <td>deadline from RSO</td>
                        <td>deadline from SRO</td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            <br>
              <a style="margin-left:-5px;">
                  <button class="btn btn-danger btn-md" onclick="getCheckForAdd();">
                      <span class="glyphicon glyphicon-heart"></span>
                  </button>
              </a>
            <br><br>
            <a href="jumpToFM2.jsp" style="margin-left:-5px;"><button class="btn btn-success btn-md"><span class="glyphicon glyphicon-briefcase"></span>&nbsp;&nbsp;Check My List</button></a>
          </div>
        </div>
    </div>
      
    <div class="row">
        <div class="col-sm-4 col-sm-offset-5 col-md-4 col-md-offset-5">
            Total:&nbsp;<s:property value="rowCount"/>&nbsp;Recordings&nbsp;&nbsp; <!--value的值都通过action映射机制对应action中的属性值-->
            Current:&nbsp;<s:property value="pageNow"/>&nbsp;Page&nbsp;&nbsp;

            <s:url id="url_pre" value="showFM1.action">
                <s:param name="pageNow" value="pageNow-1"></s:param>
            </s:url>

            <s:url id="url_next" value="showFM1.action">
                <s:param name="pageNow" value="pageNow+1"></s:param>
            </s:url>


            <s:a href="%{url_pre}">Previous</s:a>
            <s:a href="%{url_next}">Next</s:a>
        </div>
    </div>
</body>
</html>
