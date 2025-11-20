<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - CToon</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <main>
        <h2>Login to CToon</h2>
        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <input type="hidden" name="action" value="login" />
            <div>
                <label for="username">Username or Email</label>
                <input type="text" id="username" name="username" placeholder="Enter your username or email" required>
            </div>
            <div>
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit">Login</button>
        </form>
        
        <!-- Unauthenticated users are treated as guests automatically; explicit guest login removed -->

        <div style="text-align: center; margin-top: 2rem;">
            <p>Don't have an account? <a href="${pageContext.request.contextPath}/signup.jsp">Sign Up</a></p>
        </div>
    </main>
    
    <footer>
        <button id="theme-toggle">🌙 Dark Mode</button>
    </footer>
</body>
</html>
