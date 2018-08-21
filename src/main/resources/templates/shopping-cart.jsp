<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<t:layout title="购物车详情">
  <jsp:attribute name="css">
    <sec:csrfMetaTags />
  </jsp:attribute>
  
  <jsp:attribute name="js">
    <script src="${contextPath}/assets/vendor/jquery-3.3.1.min.js"></script>
    <script src="${contextPath}/assets/js/shopping-cart.js"></script>
  </jsp:attribute>
  
  <jsp:body>
    <c:choose>
      <c:when test="${shoppingCart.items.isEmpty()}">
        <div>购物车空空如也，<a href="${contextPath}/">去逛一逛</a></div>
      </c:when>
      <c:otherwise>
        <table> 
          <tr><th>商品</th><th>单价</th><th>数量</th><th>操作</th></tr>
          <c:forEach items="${shoppingCart.items}" var="item">
            <tr>
              <td>${item.cellphone.brand} ${item.cellphone.model} ${item.cellphone.color}</td>
              <td>￥${item.cellphone.price / 100}</td>
              <td>
                <input type="number" min="1" value="${item.quantity}" 
                       class="item-quantity" 
                       data-cellphone-id="${item.cellphone.id}">  
              </td>
              <td>
                <form action="${contextPath}/uc/shopping-cart/remove-item" method="post">
                  <sec:csrfInput />
                  <input type="hidden" name="cellphoneId" value="${item.cellphone.id}">
                  <button type="submit">删除</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </table>     
        <div>
          <div>总金额: ￥<span id="totalCost">${shoppingCart.totalCost() / 100}</span></div>
          <a href="${contextPath}/uc/orders/add">马上结算</a>
        </div>   
      </c:otherwise>
    </c:choose>
  </jsp:body>
</t:layout>
