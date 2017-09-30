var httpGet = function (params) {
	var xmlhttp = createHttpRequest();
	xmlhttp.onreadystatechange = function(){
		if (xmlhttp.readyState==4) {
			if (xmlhttp.status==200)
				params.success(xmlhttp.responseText)
			else
				params.error(xmlhttp.responseText)
	    }
	}
	xmlhttp.open("GET",params.url, params.async || true);
	xmlhttp.send();
}

function createHttpRequest() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
	    //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
	    xmlhttp=new XMLHttpRequest();
	} else {
	    // IE6, IE5 浏览器执行代码
	    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}