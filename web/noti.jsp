<%-- 
    Document   : noti
    Created on : Sep 25, 2020, 2:59:31 PM
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Notification</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.min.css">	
        <link rel="stylesheet" type="text/css" href="css/all.css">
        <link rel="stylesheet" type="text/css" href="css/custom.css">
        <style type="text/css">
            body{
                padding: 0px 0px;
            }
        </style>
    </head>
    <body>
        <div class="header-color set-padding">
            <a href="DispatcherServlet?btnAction=Return&txtOwnerId=${sessionScope.LOGIN_ACCOUNT.email}" class="set-left-margin">
                <i class="fas fa-home fa-2x"></i>
            </a> 
                
            <div class="badge set-badge-color text-wrap font-size-default set-margin set-left-margin" style="width: 20rem;">
                Welcome, <font color="#FA3030">${sessionScope.LOGIN_ACCOUNT.name}</font> 
            </div>



            <c:set var="noti" value="${requestScope.NOTI}"></c:set>
            <a href="DispatcherServlet?btnAction=Notification&txtOwnerId=${sessionScope.LOGIN_ACCOUNT.email}">
                <div class="border noti-icon">
                    <i class="fas fa-bell fa-2x"></i>
                </div>

            </a>        
            <c:if test="${noti gt 0}">
                <font color="red">
            </c:if>
            <h6 class="display-inline">${noti}</h6>    
            <c:if test="${noti gt 0}">
                </font>
            </c:if>


            <div class="badge set-badge-color text-wrap set-left-margin" style="width: 6rem;">
                <a href="DispatcherServlet?btnAction=Logout" class="logout-color">                   
                    <i class="fas fa-sign-out-alt fa-1x">Logout</i>
                </a> 
            </div>

            <form action="DispatcherServlet">
                <div class="input-group mb-3 set-margin set-left-margin">
                    <div class="col-xs-3 set-left-margin">
                        <input type="text" name="txtSearchValue" value="${param.txtSearchValue}" class="form-control" placeholder="Search here!" style="width: 40rem;" required="" />
                        <input type="hidden" name="txtPage" value="1" />
                    </div>
                    <div class="input-group-append">
                        <input type="submit" value="Search" name="btnAction" class="btn btn-primary" id="button-addon2" /> 
                    </div>
                </div>
                <br/> 
            </form>
        </div>
        <br/>
        <div class="badge badge-primary text-wrap font-size-default set-left-margin" style="width: 40rem;">
            <h2>Notification</h2>
        </div>
        <br/>

        <c:set var="listNoti" value="${requestScope.NOTI_LIST}"></c:set>
        <c:choose>
            <c:when test="${not empty listNoti}">
                <c:forEach var="noti" items="${listNoti}" varStatus="counter">
                    <div class="card set-margin set-left-margin" style="width: 50rem;">
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${noti.action eq 'COMMENTED'}">
                                    <a href="DispatcherServlet?btnAction=View&txtPostId=${noti.postId}">
                                        <p class="card-text set-background-color-grey">${noti.interactorName} has ${noti.action}: "${noti.detail}" in your post at ${noti.date}</p>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="DispatcherServlet?btnAction=View&txtPostId=${noti.postId}">
                                        <p class="card-text set-background-color-grey">${noti.interactorName} has ${noti.action} in your post at ${noti.date}</p>                                       
                                    </a>                   
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${empty listNoti}">
                <h5 class="set-top-margin-lg set-left-margin">You have no notification!</h5>
            </c:when>
        </c:choose>
        <br/>
        <a href="DispatcherServlet?btnAction=Return&txtOwnerId=${sessionScope.LOGIN_ACCOUNT.email}" class="set-left-margin set-top-margin-lg">Click here to return to main page</a>        

    </body>
</html>
