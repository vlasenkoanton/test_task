<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<title>Test Indexer</title>
<head>
    <link rel="stylesheet" href="resources/style.css">
</head>
<body>
<a href="/index" class="perform_index">Perform Index</a>

<form method="GET" action="search">
    <input type="text" name="q">
    <button type="submit">Search</button>
</form>


</body>
</html>
