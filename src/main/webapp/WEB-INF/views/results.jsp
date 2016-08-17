<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>Test Indexer</title>
<head>
    <link rel="stylesheet" href="resources/style.css">
</head>
<body>
<header>
    <form method="GET" action="/search">
        <input type="text" name="q">
        <button type="submit"><p>Search</p></button>
    </form>
</header>
<menu>
    <ul>
        <li><a href="fake" class="menu_link">Все</a></li>
        <li><a href="fake" class="menu_link">Картинки</a></li>
        <li><a href="fake" class="menu_link">Видео</a></li>
        <li><a href="fake" class="menu_link">Новости</a></li>
        <li><a href="fake" class="menu_link">Карты</a></li>
        <li><a href="fake" class="menu_link">Еще</a></li>
        <li><a href="fake" class="menu_link">Интсрументы поиска</a></li>
    </ul>
    <div id="reset"></div>
</menu>
<form name="sort_form" method="POST">
    <input type="hidden" name="sort">
    <input class="sort_button" type="button" value="Relevance" onclick="button1()">
    <input class="sort_button" type="button" value="Alphabetic" onclick="button2()">
    <input class="sort_button" type="button" value="Normal" onclick="button3()">
</form>
<script language="JavaScript">
    function button1() {
        document.sort_form.sort.value = "relevance"
        document.sort_form.submit()
    }
    function button2() {
        document.sort_form.sort.value = "alphabetic"
        document.sort_form.submit()
    }
    function button3() {
        document.sort_form.sort.value = "normal"
        document.sort_form.submit()
    }
</script>
<div id="wrapper">
    <c:forEach items="${results}" var="result">
        <p><a href="${result.link}"
              id="title">${result.title.length() gt 100 ? result.title.substring(0, 100) : result.title}</a></p>
        <p class="link_p">${result.link}</p>
        <p class="link_fragment">${result.fragment}</p>
    </c:forEach>
</div>

</body>
</html>