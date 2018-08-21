<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="创建订单...">
  <jsp:attribute name="css">
    <link href="${contextPath}/assets/css/form.css" rel="stylesheet">
  </jsp:attribute>
  
  <jsp:body>
    <table> 
      <caption>即将购买以下商品</caption>
      <tr><th>商品</th><th>单价</th><th>数量</th></tr>
      <c:forEach items="${shoppingCart.items}" var="item">
        <tr>
          <td>${item.cellphone.brand} ${item.cellphone.model} ${item.cellphone.color}</td>
          <td>￥${item.cellphone.price / 100}</td>
          <td>${item.quantity}</td>
        </tr>
      </c:forEach>
    </table>
    <div>
      <div>总金额: ￥${shoppingCart.totalCost() / 100}</div>
    </div>   

    <form:form action="" method="post" commandName="orderForm">
      <div>
        <label for="shippingAddressId"></label>
        <form:select path="shippingAddressId">
          <form:option value="">--请选择收货地址--</form:option>
          <form:options items="${shippingAddresses}" itemLabel="name" itemValue="id" />
        </form:select>
        <form:errors path="shippingAddressId" cssClass="field-error" />      
      </div>
  
      <div>
        <button type="submit">确认</button>
      </div>
    </form:form>
  </jsp:body>
    
</t:layout>
