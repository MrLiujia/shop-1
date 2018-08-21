<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>
  <h1>登录</h1>
  
  <#if RequestParameters.error??>
      <h2 style="color: red;">用户名或密码错误</h2>
  </#if>
  
  <#if RequestParameters.logout??>
    <h2>已退出，请重新登录</h2>
  </#if>
  
  <form action="" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    
    <div>
      <label for="username">用户名</label>
      <input type="text" name="username" id="username">
    </div>
    <div>
      <label for="password">密码</label>
      <input type="password" name="password" id="password">    
    </div>
    <div>
      <label for="remember-me">记住我</label>
      <input type="checkbox" name="remember-me" id="remember-me" checked>    
    </div>    
    <div>
      <button type="submit">登录</button>
    </div>
  </form>
</body>
</html>