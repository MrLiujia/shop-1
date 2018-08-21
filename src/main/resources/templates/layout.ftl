<#import "/spring.ftl" as spring />

<#macro css>
</#macro>

<#macro js>
</#macro>

<#macro layout title>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>${title}</title>
  
  <link href="/assets/css/app.css" rel="stylesheet">
  <@css />
</head>
<body>
  <div class="header"> 
    <a href="/">首页</a>
    <#if sec.authenticated>
      ${userDetails.username}
      [${userProvince}]
      <a href="/uc/shopping-cart">购物车</a>
      <a href="/uc/shipping-addresses/">收货地址</a>
      <a href="/uc/orders/">订单</a>
      <form action="/logout" method="post" style="display: inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit">退出</button>
      </form>  
    </#if>  
    
    <#if sec.anonymous>
      <a href="/login">登录</a>
      <a href="/register">注册</a>
    </#if>
  </div>
  
  <div class="content">
    <h1>${title}</h1>
    <#nested>
  </div>
  
  <div class="footer">copyright 2018</div>
   
  <@js />
</body>
</html>
</#macro>