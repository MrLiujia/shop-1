<#include 'layout.ftl'>

<@layout title="首页">
  <form action="" method="get">
    <div>
        <label for="brand">品牌</label>
        <@spring.formSingleSelect "cellphone.brand" 
            {
                '': '--请选择品牌--', 
                '苹果': '苹果', 
                '锤子': '锤子', 
                '华为': '华为'
            }>
        </@spring.formSingleSelect>
    </div>
    
    <div>
        <label for="os">操作系统</label>
        <@spring.formSingleSelect "cellphone.os" 
            {
                '': '--请选择操作系统--', 
                'Android': 'Android', 
                'IOS': 'IOS', 
                'Windows Phone': 'Windows Phone'
            }>
        </@spring.formSingleSelect>
    </div>
    
    <div>
        <label for="cpuBrand">CPU品牌</label>
        <@spring.formSingleSelect "cellphone.cpuBrand" 
            {
                '': '--请选择CPU品牌--', 
                '高通': '高通', 
                '联发科': '联发科'
            }>
        </@spring.formSingleSelect>
    </div>    
    
    <div>
        <label for=cpuCoreCount>CPU核心数</label>
        <@spring.formInput "cellphone.cpuCoreCount" 
                           'min="1" max="16"' 
                           "number" />
    </div>
    
    <div>
        <label for=ram>运行内存</label>
        <@spring.formInput "cellphone.ram" 
                           'min="1" max="16" placeholder="GB"' 
                           "number" />
    </div>
    
    <div>
        <label for=storage>机身存储</label>
        <@spring.formInput path="cellphone.storage"
                           fieldType="number" 
                           attributes='min="16" max="256" placeholder="GB"' />
    </div> 
    
    <div>
      <button type="submit">搜索</button>
    </div>
  </form:form>
  
   <table>
    <tr>
      <th>品牌</th>
      <th>型号</th>
      <th>操作系统</th>
      <th>CPU</th>
      <th>RAM</th>
      <th>存储</th>
      <th>颜色</th>
      <th>价格</th>
      <th></th>
    </tr>
    <#list cellphones as cellphone>
      <tr>
        <td>${cellphone.brand}</td>
        <td>${cellphone.model}</td>
        <td>${cellphone.os}</td>
        <td>${cellphone.cpuBrand} ${cellphone.cpuCoreCount}</td>
        <td>${cellphone.ram}</td>
        <td>${cellphone.storage}</td>
        <td>${cellphone.color}</td>
        <td>${cellphone.price}</td>
        <td><a href="/cellphones/${cellphone.id}">详情</a></td>
      </tr>
    </#list>
  </table>
</@layout>
