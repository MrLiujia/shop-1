<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="title" value="${shippingAddress.id == null ? '添加收货地址' : '修改收货地址'}" />

<t:layout title="${title}">
  <jsp:attribute name="css">
    <link href="${contextPath}/assets/css/form.css" rel="stylesheet">
  </jsp:attribute>
  
  <jsp:body>  
    <form:form action="" method="post" commandName="shippingAddress">
      <div>
        <label for="name">姓名</label>
        <form:input path="name" />
        <form:errors path="name" cssClass="field-error" />
      </div>

      <div>
        <label for="phoneNumber">手机号</label>
        <form:input path="phoneNumber"/>
        <form:errors path="phoneNumber" cssClass="field-error" />
      </div>
      
      <div>
        <label for="address">详细地址</label>
        <form:textarea path="address" />
        <form:errors path="address" cssClass="field-error" />      
      </div>
      
      <div>
        <button type="submit">保存</button>
      </div>
    </form:form>
  </jsp:body>
</t:layout>
