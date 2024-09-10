<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<div class="container">
    <br>
    <br>
    <h2>인스타그램 맞팔 추적기</h2>
    <br>
    ⚠️비공개계정을 공개계정으로 바꿔주세요.
    <br>
    ⚠️팔로워수와 팔로잉수의 합이 1000명이면 대략 10초가걸립니다
    <br>
    ️ ⚠️팔로워수와 팔로잉수가 2000명이상인 계정은 검색하지말아주세요~
    <br>
    <br>
    <form id="checkForm" action="/check" method="post" onsubmit="showLoading()">
        <div class="form-group">
            <label for="username">아이디</label>
            <input type="text" class="form-control" id="username" placeholder="ID입력" name="username">
        </div>
        <button type="submit" class="btn btn-primary" id="startButton">확인하기</button>
    </form>
    <br>
</div>
<script>
    function showLoading() {
        const button = document.getElementById('startButton');
        button.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Loading..';
        button.disabled = true;
    }
    window.addEventListener( "pageshow", function ( event ) {
        var historyTraversal = event.persisted ||
            ( typeof window.performance != "undefined" &&
                window.performance.navigation.type === 2 );
        if ( historyTraversal ) {
            // Handle page restore.
            window.location.reload();
        }
    });


</script>
</body>
</html>
