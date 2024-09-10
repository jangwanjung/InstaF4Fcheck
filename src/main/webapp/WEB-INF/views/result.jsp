<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>맞팔추적기</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<div class="container" style="margin-top: 30px">
    <div>
        <h4>내가팔로우안한사람(${iDontFollowBack.size()}명)</h4>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>아이디</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="follow" items="${iDontFollowBack}">
                <tr>
                    <td>${follow}</td>
                    <td>${followersMap[follow]}</td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
    <div>
        <h4>나를팔로우하지않은사람(${dontFollowMeBack.size()}명)</h4>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>아이디</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="follow" items="${dontFollowMeBack}">
                <tr>
                    <td>${follow}</td>
                    <td>${followingMap[follow]}</td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>


</div>

</body>
</html>