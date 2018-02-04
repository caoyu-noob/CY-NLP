<%--
  Created by IntelliJ IDEA.
  User: cao_y
  Date: 2018/1/27
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title path="<%=path %>">三国志QA</title>
    <link type="text/css" href="<%=path %>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link type="text/css" href="<%=path %>/css/index.css" rel="stylesheet">
    <link type="text/css" href="<%=path %>/css/iconfont.css" rel="stylesheet">
    <link type="text/css" href="<%=path %>/css/loaders.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="<%=path %>/images/favicon.ico">
    <script type="text/javascript" src="<%=path %>/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="<%=path %>/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="<%=path %>/js/index.js"></script>
    <script type="text/javascript" src="<%=path %>/js/loaders.css.js"></script>
</head>
<body>
    <nav class="navbar navbar-inverse nav-bar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand">三国志知识QA系统(Demo)</a>
            </div>
            <ul class="nav navbar-nav navbar-right">
                <li id="status" status="${dataAvailable}">
                    <a>数据正常</a>
                </li>
                <li id="setting">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="iconfont icon-shezhi"></i> 设置<b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li id="reset-data"><a><i class="iconfont icon-weixiu"></i> 重置数据</a></li>
                    </ul>
                </li>
            </ul>
        </div>


    </nav>
    <div class="container">
        <div class="starter-template">
            <h1>
                请输入关于三国志的问题
                <i id="help-icon" class="iconfont icon-bangzhuxiantiao" data-toggle="tooltip" data-placement="bottom"
                data-html="true" title="<iframe src='/css/tooltip.html'  frameborder='0'></iframe>"></i>
            </h1>
        </div>
        <div class="input-group" style="width:100%;">
            <div class="form-inline text-center question-form">
                <div class="form-group">
                    <input id="search-content" type="text"
                           class="form-control input-lg" placeholder="请输入一个问题">
                </div>
                <button id="search-btn" type="button" class="btn btn-primary btn-lg">
                    <i class="iconfont icon-sousuo"></i> 搜索
                </button>
            </div>
        </div>
        <div id="loading" class="loader-inner ball-pulse"></div>
        <div class="answer-area">
            <h2></h2>
        </div>
    </div>

</body>



</html>
