<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="/login" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required /><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required /><br><br>

        <button type="submit">Login</button>
    </form>

    <c:if test="${not empty error}">
        <div style="color: red; margin-top: 10px;">
            <strong>Error:</strong> ${error}
        </div>
    </c:if>
</body>
</html>
