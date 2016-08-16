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
</menu>
<div id="wrapper">
    <c:forEach items="${results}" var="result">
        <p><a href="${result.link}" id="title">${result.title}</a></p>
        <p class="link_p">${result.link}</p>
        <p class="link_fragment">${result.fragment}</p>
    </c:forEach>
</div>

</body>
</html>