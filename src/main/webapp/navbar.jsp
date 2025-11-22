<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
    <a href="${pageContext.request.contextPath}/" class="logo">CToon</a>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/browse">Browse</a></li>
        <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
        <li>
            <div style="position: relative; display: flex; align-items: center;">
                <input type="text" placeholder="Search comics..." style="padding: 0.6rem 1rem; border-radius: 20px; border: 2px solid rgba(255,255,255,0.3); background: rgba(255,255,255,0.1); color: white; width: 200px; transition: all 0.3s ease; backdrop-filter: blur(10px);" />
                <span style="position: absolute; right: 12px; color: rgba(255,255,255,0.6); cursor: pointer;">Search</span>
            </div>
        </li>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <li><a href="${pageContext.request.contextPath}/profile.jsp#bookmarks" class="btn">Bookmarks</a></li>
                <li><a href="${pageContext.request.contextPath}/profile.jsp" class="btn">${sessionScope.user.username}</a></li>
                <li>
                    <form method="get" action="${pageContext.request.contextPath}/logout">
                        <button type="submit" class="btn" style="background: rgba(255,255,255,0.2); border: 2px solid rgba(255,255,255,0.5);">Sign Out</button>
                    </form>
                </li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/login.jsp" class="btn">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/signup.jsp" class="btn" style="background: rgba(255,255,255,0.2); border: 2px solid rgba(255,255,255,0.5);">Sign Up</a></li>
            </c:otherwise>
        </c:choose>
        <li>
            <button id="theme-toggle" title="Toggle theme" style="background: transparent; border: none; color: white; cursor: pointer; font-size: 1.1rem;">🌙&nbsp;<span style="font-size:0.9rem;">Light Mode</span></button>
        </li>
    </ul>
</nav>
