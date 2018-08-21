<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="我的订单">
  <jsp:body>
    <table> 
      <tr><th>创建时间</th><th>订单状态</th><th>操作</th></tr>
      <c:forEach items="${orders}" var="order">
        <tr>
          <td><fmt:formatDate value="${order.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          <td>${order.stateText()}</td>
          <td><a href="${contextPath}/uc/orders/${order.id}">详情</a></td>
        </tr>
      </c:forEach>
    </table>
  </jsp:body>
</t:layout>
