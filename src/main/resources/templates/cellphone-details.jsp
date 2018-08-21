<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="手机详情">
  ${cellphone.brand} ${cellphone.model}
  <div>
    <form action="${contextPath}/uc/shopping-cart/add" method="post">
      <sec:csrfInput />
      <input type="hidden" name="cellphoneId" value="${cellphone.id}">
      <button type="submit">添加到购物车</button>
    </form>
  </div>
</t:layout>
