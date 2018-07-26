<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="创建订单...">
  <table> 
    <caption>即将购买以下商品</caption>
    <tr><th>商品</th><th>单价</th><th>数量</th></tr>
    <c:forEach items="${shoppingCart.items}" var="item">
      <tr>
        <td>${item.cellphone.brand} ${item.cellphone.model} ${item.cellphone.color}</td>
        <td>￥${item.cellphone.price / 100}</td>
        <td>${item.amount}</td>
      </tr>
    </c:forEach>
  </table>
  <div>
    <div>总金额: ￥${shoppingCart.totalCost() / 100}</div>
  </div>   

</t:layout>
