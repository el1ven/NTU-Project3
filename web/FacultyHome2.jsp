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
<title>Faculty Home 2</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="bootstrap/css/dashboard.css" rel="stylesheet">
<link href="bootstrap/css/mycss.css" rel="stylesheet">
<script src="bootstrap/js/jquery-2.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script src="bootstrap/js/myJavaScript.js"></script>
<script>
    function getId(){
        //在表单中获取每一行数据的grantId并用JS传递到form表单之中
        var a = document.getElementById("grantHideId").innerHTML;
        var b = document.getElementById("grantId");
        b.setAttribute("value", a);
    }
</script>
<script>
    function getCheckForDelete(){
        var deleteIds = document.getElementsByName("deleteId");
        var idArr = [];
        var count = 0;
        for(var i = 0; i < deleteIds.length; i++){
            if(deleteIds[i].checked){
                idArr.push(deleteIds[i].value);
                count++;
            }
        }
        if(count == 0){
            alert('Please Choose One Grant At Least!');
        }else{
            if(confirm('Confirm Delete '+count+' items?')){
                var url = "deleteOfFM.action?userName=<%=session.getAttribute("userName")%>&ids="+idArr;
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
    
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-10 col-md-offset-1">
          <h2 class="sub-header">My List</h2>
          <div class="table-responsive">
            <table id="tab1" class="table table-hover">
              <thead>
                <tr>
                  <th>Opt</th>
                  <th>Id</th>
                  <th>Seires</th>
                  <th>Title</th>
                  <th>RSO Deadline</th>
                  <th>SRO Deadline</th>
                  <th>Set Deadline<span class="glyphicon glyphicon-time" style="float:left;"></span></th>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="grantList">
                    <tr>
                        <!--<td>
                            <a style="margin-left:-5px;">
                                <button class="btn btn-danger btn-xs" onclick="window.location='deleteOfFM.action?grantId=<%=request.getAttribute("grantId")%>&userName=<%=session.getAttribute("userName")%>'">
                                    <span class="glyphicon glyphicon-trash"></span>
                                </button>
                            </a>
                        </td>-->
                        <td><input type="checkbox" name="deleteId" value="<%=request.getAttribute("grantId")%>"></td>
                        <td><%=++i%></td>
                        <!--注意这里面的属性是bean的属性名称而不是数据库中的属性名称-->
                        <td><%=request.getAttribute("grantSeries")%></td>
                        <td><a href="detail.jsp" target="_blank"><%=request.getAttribute("grantTitle")%></a></td>
                        <td>deadline from RSO</td>
                        <td>deadline from SRO</td>
                        <td><button onclick="getId()" class="btn btn-info btn-xs " style="margin-left:20px;" data-toggle="modal" data-target=".bs-example-modal-sm"><span class="glyphicon glyphicon-cog"></span></button>
                        </td>
                        <td id="grantHideId"><%=request.getAttribute("grantId")%></td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            <br>
              <a style="margin-left:-5px;">
                  <button class="btn btn-danger btn-md" onclick="getCheckForDelete();">
                      <span class="glyphicon glyphicon-trash"></span>
                  </button>
              </a>
            <br>
            <br>
            <a href="jumpToFM1.jsp" style="margin-left:-5px;"><button class="btn btn-success btn-md"><span class="glyphicon glyphicon-home"></span>&nbsp;&nbsp;Back To Category</button></a>
            <!--<a href="../../../Desktop/new/FacultyHome2.html" style="margin-left:1395px;"><button class="btn btn-danger btn-md"><span class="glyphicon glyphicon-home"></span></button></a>-->
          </div>
        </div>
      </div>
   
   </div>
   
   <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true">
  		<div class="modal-dialog modal-sm">
    		<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                </div>
                <div class="modal-body">
                	<div class="container" style="margin-left:40px;">
        			  <div class="row">
            		    <div class="col-sm-2">
                			<form class="form-signin" role="form" name="addDeadlineForm" action="deadline.action" method="post">

                                <input type="text" class="input-xs" style="width:10px;visibility: hidden;" name="grantId" id="grantId"/>

                                <br>

                                <span class="glyphicon glyphicon-time"></span>
                                <label style="margin-left:10px;">Set Deadline 1</label>
                                <input type="date" class="form-control input-sm" style="margin-bottom:15px;" name="deadline1"/>
                                
                                <span class="glyphicon glyphicon-time"></span>
                                <label style="margin-left:10px;">Set Deadline 2</label>
                                <input type="date" class="form-control input-sm" style="margin-bottom:15px;" name="deadline2"/>
                                <span class="glyphicon glyphicon-time"></span><label style="margin-left:10px;">Set Deadline 3</label> 
                                <input type="date" class="form-control input-sm" style="margin-bottom:15px;" name="deadline3" />

                                <input type="submit" value="Save" class="btn btn-md btn-success" style="margin-left:140px;" />
                                <!--<a href="#">
                                    <button class="btn btn-success btn-md"  style="margin-left:60px; margin-top:10px;">
                                    	<span class="glyphicon glyphicon-floppy-disk"></span>
                                    </button>
                                </a> -->
        			  		</form>
                        </div>
                      </div>
            		</div>
                </div>
            </div>
        </div>
   </div>
</body>
</html>
