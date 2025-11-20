# CToon Website - Navigation & Pathing Guide

## Page Navigation Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    🎨 CToon (Logo)                          │
│  [Home] [Browse] [Profile] [Search...] [Login]             │
└─────────────────────────────────────────────────────────────┘
                              │
                 ┌────────────┼────────────┐
                 │            │            │
            Home Page    Browse Page   Profile Page
            (/index)     (/browse)     (/profile)
                │            │            │
                │      ┌──────┴──────┐    │
                │      │             │    │
             Comic Page ──────── Comic Detail
             (/comic)           (/comic?id=XX)
                │
             Chapter View (/chapter?id=XX)
```

## All Links Use Correct Pathing

### Using `${pageContext.request.contextPath}`

Every link, form action, and resource reference uses proper context paths:

```jsp
<!-- Navigation Links -->
<a href="${pageContext.request.contextPath}/">Home</a>
<a href="${pageContext.request.contextPath}/browse.jsp">Browse</a>
<a href="${pageContext.request.contextPath}/profile.jsp">Profile</a>
<a href="${pageContext.request.contextPath}/login.jsp">Login</a>

<!-- CSS & JS Resources -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/theme.js"></script>

<!-- Form Actions -->
<form action="${pageContext.request.contextPath}/auth" method="post">

<!-- Comic Links -->
<a href="${pageContext.request.contextPath}/comic.jsp">View Comic</a>
<a href="${pageContext.request.contextPath}/chapter.jsp?id=45">Read Chapter</a>
```

## Page Mapping

| Page | File | URL | Purpose |
|------|------|-----|---------|
| Home | `index.jsp` | `/` | Landing page with featured comics |
| Browse | `browse.jsp` | `/browse.jsp` | Browse & filter comics by category |
| Comic Detail | `comic.jsp` | `/comic.jsp` | View comic info, chapters, ratings |
| Chapter | `chapter.jsp` | `/chapter.jsp?id=XX` | Read individual chapters |
| Profile | `profile.jsp` | `/profile.jsp` | User profile, history, bookmarks |
| Login | `login.jsp` | `/login.jsp` | Authentication page |
| Signup | `signup.jsp` | `/signup.jsp` | New account registration |

## Dynamic Link Examples

### From Home to Browse
```jsp
<!-- index.jsp -->
<a href="${pageContext.request.contextPath}/browse.jsp">Browse Comics</a>
```

### From Browse to Comic Detail
```jsp
<!-- browse.jsp -->
<a href="${pageContext.request.contextPath}/comic.jsp" class="btn">View</a>
```

### From Comic Detail to Chapter
```jsp
<!-- comic.jsp -->
<a href="${pageContext.request.contextPath}/chapter.jsp?id=45">Chapter 45</a>
```

### From Chapter Back to Comic
```jsp
<!-- chapter.jsp -->
<a href="${pageContext.request.contextPath}/comic.jsp">← Back to Comic</a>
```

## Navbar (Included on All Pages)

The navbar appears on every page via `<%@ include file="navbar.jsp" %>`:

```jsp
<!-- navbar.jsp structure -->
<nav>
    <a href="${pageContext.request.contextPath}/" class="logo">🎨 CToon</a>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/browse.jsp">Browse</a></li>
        <li><a href="${pageContext.request.contextPath}/profile.jsp">Profile</a></li>
        <li><input type="text" placeholder="Search comics..." /></li>
        <li><a href="${pageContext.request.contextPath}/login.jsp" class="btn">Login</a></li>
    </ul>
</nav>
```

## Static Resources

All static resources use context-relative paths:

```
/css/style.css
  ↑
  └─ Accessed via: ${pageContext.request.contextPath}/css/style.css

/js/theme.js
  ↑
  └─ Accessed via: ${pageContext.request.contextPath}/js/theme.js
```

## Form Actions

All forms use context-relative paths to controllers:

```jsp
<!-- Login/Signup -->
<form action="${pageContext.request.contextPath}/auth" method="post">

<!-- Future: Comments -->
<form action="${pageContext.request.contextPath}/comment" method="post">

<!-- Future: Ratings -->
<form action="${pageContext.request.contextPath}/rating" method="post">
```

## How to Test Navigation

1. Start the application
2. Navigate from Home → Browse
3. From Browse → Click any Comic
4. From Comic → Click any Chapter
5. From Chapter → Use back button or "Back to Comic" link
6. Use navbar to go anywhere from anywhere

All links should work correctly regardless of context path or URL depth.

## Key Benefits of This Approach

✅ **Deployment Agnostic**: Works with any context path (ROOT, /app, /ctoon, etc.)
✅ **No Hardcoded Paths**: Never hardcode `/` or relative paths
✅ **Mobile Friendly**: All links work on all devices
✅ **Future Proof**: Easy to refactor URL structure later
✅ **SEO Friendly**: Clean, semantic HTML structure
✅ **Maintainable**: All links centralized in JSP files
