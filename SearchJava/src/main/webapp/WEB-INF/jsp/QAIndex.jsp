<%--
  Created by IntelliJ IDEA.
  User: cao_y
  Date: 2018/1/27
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>三国志QA</title>
    <link type="text/css" href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link type="text/css" href="/css/index.css" rel="stylesheet">
    <link type="text/css" href="/css/iconfont.css" rel="stylesheet">
    <link rel="shortcut icon" href="/images/favicon.ico">
    <script type="text/javascript" src="/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="/js/index.js"></script>
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
                        设置<b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a>重置数据</a></li>
                    </ul>
                </li>
            </ul>
        </div>


    </nav>
    <div class="container">
        <div class="starter-template">
            <h1>
                请输入关于三国志的问题
                <i class="iconfont icon-info"></i>
            </h1>
        </div>
    </div>
</body>



</html>
