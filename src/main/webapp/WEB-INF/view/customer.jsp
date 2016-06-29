<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>客户管理</title>
</head>
<body>
<table>
    <thead>
    <tr>
        <th>客户名称</th>
        <th>联系人</th>
        <th>电话号码</th>
        <th>邮箱地址</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customer" items="${customerList}">
      <tr>
          <td>${customer.name}</td>
          <td>${customer.contact}</td>
          <td>${customer.telephone}</td>
          <td>${customer.email}</td>
          <td>${customer.remark}</td>
      </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
