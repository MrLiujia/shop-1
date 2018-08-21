<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="订单详情">
  <jsp:body>
    <div>
      <div>订单创建于: <fmt:formatDate value="${order.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
      <div>订单状态: ${order.stateText()}</div>  
    </div>
    <table> 
      <caption>订单项</caption>
      <tr><th>商品</th><th>单价</th><th>数量</th></tr>
      <c:forEach items="${order.items}" var="item">
        <tr>
          <td>${item.cellphone.brand} ${item.cellphone.model} ${item.cellphone.color}</td>
          <td>￥${item.cellphone.price / 100}</td>
          <td>${item.quantity}</td>
        </tr>
      </c:forEach>
    </table>
    <div>
      <div>总金额: ￥<span id="totalCost">${order.totalCost() / 100}</span></div>
    </div>
    
    <div>收货地址: 
      ${order.shippingAddress.name}(${order.shippingAddress.address})
    </div>
    
    <c:if test="${order.state == 'Created'}">
      <div>
        <form action="${contextPath}/uc/orders/${order.id}/pay" method="post">
          <sec:csrfInput />
          <button type="submit">支付宝付款</button>
        </form>
      </div>    
    </c:if>
  </jsp:body>
</t:layout>
