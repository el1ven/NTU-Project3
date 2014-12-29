function mySortString(lag)
{
	var arr = [];
	var oTab = document.getElementById('table');
	for(var i=0;i<oTab.tBodies[0].rows.length;i++) 
	{
		arr[i] = oTab.tBodies[0].rows[i];
	}
	if(oTab.sortLag==lag)
	{
		arr.sort(function(tr1,tr2)
		{
			var n1 = tr1.cells[lag].innerHTML;
			var n2 = tr2.cells[lag].innerHTML;
			return n1.localeCompare(n2);
		});
	    oTab.sortLag=-1;
	}
	else
	{
		arr.sort(function(tr1,tr2)
		{
			var n1 = tr1.cells[lag].innerHTML;
			var n2 = tr2.cells[lag].innerHTML;
			return n2.localeCompare(n1);
		});
	    oTab.sortLag=lag;
	}

	for(var j=0;j<arr.length;j++)
	{
		oTab.tBodies[0].appendChild(arr[j]);	
	}
};

function mySortInt(lag,index)
{
	var arr = [];
	var months = new Array(12);
	months['Jan'] = 1;
	months['Feb'] = 2;
	months['Mar'] = 3;
	months['Apr'] = 4;
	months['May'] = 5;
	months['Jun'] = 6;
	months['Jul'] = 7;
	months['Aug'] = 8;
	months['Sep'] = 9;
	months['Oct'] = 10;
	months['Nov'] = 11;
	months['Dec'] = 12;
	var oTab = document.getElementById('table');
	for(var i=0;i<oTab.tBodies[0].rows.length;i++)
	{
		arr[i] = oTab.tBodies[0].rows[i];
	}
	if(oTab.sortLag==lag)
	{
		arr.sort(function(tr1,tr2)
		{
			if(lag == index)
			{
				var m1 = months[tr1.cells[lag].innerHTML.substring(0,3)];
				var m2 = months[tr2.cells[lag].innerHTML.substring(0,3)];
				var d1 = Number(tr1.cells[lag].innerHTML.substring(4,6));
				var d2 = Number(tr2.cells[lag].innerHTML.substring(4,6));
				if(m1>m2)
				{
					return 1;
				}
				else if(m1<m2)
				{
					return -1;
				}
				else
				{
					if(d1>d2)
					{
						return 1;
					}
					else if(d1<d2)
					{
						return -1;
					}
				}
			}
			else
			{
				var n1 = parseInt(tr1.cells[lag].innerHTML);
				var n2 = parseInt(tr2.cells[lag].innerHTML);
				return n1-n2;
			}
		});
		oTab.sortLag=-1;
	}
	else
	{
		arr.sort(function(tr1,tr2)
		{
			if(lag == index)
			{
				var m1 = months[tr1.cells[lag].innerHTML.substring(0,3)];
				var m2 = months[tr2.cells[lag].innerHTML.substring(0,3)];
				var d1 = Number(tr1.cells[lag].innerHTML.substring(4,6));
				var d2 = Number(tr2.cells[lag].innerHTML.substring(4,6));
				if(m1>m2)
				{
					return -1;
				}
				else if(m1<m2)
				{
					return 1;
				}
				else
				{
					if(d1>d2)
					{
						return -1;
					}
					else if(d1<d2)
					{
						return 1;
					}
				}
			}
			else
			{
				var n1 = parseInt(tr1.cells[lag].innerHTML);
				var n2 = parseInt(tr2.cells[lag].innerHTML);
				return n2-n1;
			}
		});
		oTab.sortLag=lag;
	}
	for(var j=0;j<arr.length;j++)
	{
		oTab.tBodies[0].appendChild(arr[j]);
	}
};	

function schoolFilter(obj,index)
{
	var oTab = document.getElementById('table');
	var oTr = oTab.tBodies[0].rows;
	for(var i=0;i<oTr.length;i++)
	{
		var toSchool = oTr[i].cells[index].innerHTML.toLowerCase();
		oTr[i].style.display = 'none';
		if(toSchool == obj.innerHTML.toLowerCase())
		{
			oTr[i].style.display = 'table-row';
		}
	}
}

function seriesFilter(obj,index)
{
	var oTab = document.getElementById('table');
	var oTr = oTab.tBodies[0].rows;
	for(var i=0;i<oTr.length;i++)
	{
		var toSeries = oTr[i].cells[index].innerHTML.toLowerCase();
		oTr[i].style.display = 'none';
		if(toSeries == obj.innerHTML.substr(0,20).trim().toLowerCase())
		{
			oTr[i].style.display = 'table-row';
		}
	}
}

function dateFilter(obj,index)
{
	var oTab = document.getElementById('table');
	var oTr = oTab.tBodies[0].rows;
	for(var i=0;i<oTr.length;i++)
	{
		var toDate = oTr[i].cells[index].innerHTML.toString();
		oTr[i].style.display = 'none';
		console.log(toDate.substring(0,3));
		if(toDate.substring(0,3) == obj.innerHTML.substr(0,3).trim())
		{
			oTr[i].style.display = 'table-row';
		}
	}
} 

function checkAll(val) 
{ 
	$("input[name='checkbox']").each(function() { 
	this.checked = val; 
	}); 
};

var timer = null;
function startMove(target)
{
	var slidebar = document.getElementById('slidebar');
	var icon = document.getElementById('icon-menu');
	clearInterval(timer);
	timer = setInterval(function()
	{
		var speed = (target-slidebar.offsetLeft)/10;
		speed = speed > 0 ? Math.ceil(speed):Math.floor(speed); 
		if(target == slidebar.offsetLeft)
		{
			clearInterval(timer);
		}
		else
		{
			slidebar.style.left = slidebar.offsetLeft + speed + 'px';
		}
	},30);
}
/*********************************editor.js**********************************************/
function initToolbarBootstrapBindings() 
{
  var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier', 'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times', 'Times New Roman', 'Verdana'], fontTarget = $('[title=Font]').siblings('.dropdown-menu');

	  $.each(fonts, function (idx, fontName) 
	  {
	  	fontTarget.append($('<li><a data-edit="fontName ' + fontName +'" style="font-family:\''+ fontName +'\'">'+fontName + '</a></li>'));
  	  });
	  
  	  $('a[title]').tooltip({container:'body'});
	
	  $('.dropdown-menu input').click(function() {return false;}).change(function () {$(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');}).keydown('esc', function () {this.value='';$(this).change();});

      $('[data-role=magic-overlay]').each(function () 
	  { 
		var overlay = $(this), target = $(overlay.data('target')); 
		overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
  	  });
  
  	  if ("onwebkitspeechchange"  in document.createElement("input")) 
	  {
		var editorOffset = $('#editor').offset();
		$('#voiceBtn').css('position','absolute').offset({top: editorOffset.top, left: editorOffset.left+$('#editor').innerWidth()-35});
  	  } 
	  else 
	  {
		$('#voiceBtn').hide();
  	  }
};

function showErrorAlert (reason, detail) 
{
	var msg='';
	if (reason==='unsupported-file-type') 
	{ 
		msg = "Unsupported format " +detail; 
	}
	else 
	{
		console.log("error uploading file", reason, detail);
	}
	
	$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'+ '<strong>File upload error</strong> '+msg+' </div>').prependTo('#alerts');
};

/*****************************************************************************************/
$(function()
{
	$('#icon-menu,#slidebar').mouseover(function()//右侧边框滑动
	{
		startMove(0);
	});
	$('#icon-menu,#slidebar').mouseout(function()
	{
		startMove(-160);
	});

	$('.pagination').find('li').click(function()//分页控制
	{
		$('.pagination').find('li').attr('class','');
		$(this).attr('class','active');
	});

	$('#icon-menu').hover(function() {
		$(this).css({cursor: 'pointer'});
	});

	$("#table tr").each(function()//悬浮出现小手
	{
		var td_school = $(this).children("td").eq("3");
		var td_people = $(this).children("td").eq("4");
		
		td_school.hover(function() 
		{
			$(this).css({cursor: 'pointer'});
		});
		td_people.hover(function()
		{
			$(this).css({cursor: 'pointer'});
		});
	});

	$('th').click(function()//排序小三角
	{
		if($(this).find('span').attr('class') == "dropup")
		{
			$(this).find('span').attr('class','dropdown');
		}
		else
		{
			$(this).find('span').attr('class','dropup');
		}
	});

	$('#add').click(function()//日期添加按钮
	{
		$('#description').after('<div class="row" style="margin-top:30px;"><div class="col-md-9 col-md-offset-3"><label class="col-sm-3 control-label" style="font-size:20px;">DeadLine</label><br></div></div><div class="row" style="margin-top:10px;"><div class="col-md-8 col-md-offset-2"><label class="control-label" style="font-size:20px; margin-left:-50px;">Date</label><div class="col-sm-4"><input type="date" class="form-control input-sm" style="margin-left:250px; height:24px;"></div></div></div><div class="row" style="margin-top:15px; margin-left:130px;"><div class="col-md-8 col-md-offset-3"><label class="col-sm-3 control-label" style="font-size:20px; margin-left:-30px;">Description</label><textarea class="form-control" rows="2" style="margin-left:45px; width:610px;"></textarea></div></div>');
		$('#minus').css('display','block');
	});

	$('#minus').click(function()//日期删减按钮
	{
		$('#icon').prev().remove();
		if($('#description').next().attr("id") == 'icon')
		{
			$('#minus').css('display','none');
		}
		else
		{
			$('#minus').css('display','block');
		}
	});

	$('#increase').click(function()//上传添加按钮
	{
		$('#upload').after('<div class="row" style="margin-top:10px;"><div class="col-md-8 col-md-offset-3"><div class="form-group"><div class="fileinput fileinput-new input-group" data-provides="fileinput"><div class="form-control"><i class="glyphicon glyphicon-file fileinput-exists"></i><span class="fileinput-filename"></span></div><span class="input-group-addon btn btn-success btn-file"><span class="fileinput-new">Select file</span><span class="fileinput-exists">Change</span><input type="file" name="..."></span><a href="#" class="input-group-addon btn btn-danger fileinput-exists" data-dismiss="fileinput">Remove</a></div></div></div></div>');
		$('#remove').css('display','block');
	});

	$('#remove').click(function()//上传移出按钮
	{
		$('#loadicon').prev().remove();
		if($('#upload').next().attr("id") == 'loadicon')
		{
			$('#remove').css('display','none');
		}
		else
		{
			$('#remove').css('display','block');
		}
	});

	initToolbarBootstrapBindings(); //表单编辑器
	$('#editor').wysiwyg({ fileUploadError: showErrorAlert} );
	window.prettyPrint && prettyPrint();
});