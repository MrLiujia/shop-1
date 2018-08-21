<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="收货地址列表">
  <div>
    <a href="${contextPath}/uc/shipping-addresses/add">添加</a>
  </div>
  <table>
    <tr><th>姓名</th><th>手机号</th><th>地址</th><th>操作</th></tr>
    <c:forEach items="${shippingAddresses}" var="shippingAddress">
      <tr>
        <td>${shippingAddress.name}</td>
        <td>${shippingAddress.phoneNumber}</td>
        <td>${shippingAddress.address}</td>
        <td><a href="${contextPath}/uc/shipping-addresses/${shippingAddress.id}/edit">修改</a></td>
      </tr>
    </c:forEach>
  </table>
</t:layout>
