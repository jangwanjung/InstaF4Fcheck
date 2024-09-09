<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<div class="container" style="display: flex ;justify-content: space-between ;margin-top: 30px">
    <div style="margin-left: 100px;">
        <h2>내가팔로우안한사람</h2>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>사진</th>
                <th>아이디</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="follow" items="${iDontFollowBack}">
                <tr>
                    <td>사진</td>
                    <td>${follow}</td>
                    <td>이름</td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
    <div style="margin-right: 100px">
        <h2>나를팔로우하지않은사람</h2>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>사진</th>
                <th>아이디</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="follow" items="${dontFollowMeBack}">
                <tr>
                    <td>사진</td>
                    <td>${follow}</td>
                    <td>이름</td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>


</div>

</body>
</html>