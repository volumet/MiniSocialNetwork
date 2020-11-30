<%-- 
    Document   : viewpost
    Created on : Sep 19, 2020, 4:59:30 PM
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Main</title>
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
        <c:set var="thisArticle" value="${requestScope.DETAIL_POST}"></c:set>
            <div class="card set-left-margin" style="width: 50rem;">
                <div class="card-body">
                    <h5 class="card-title">
                    ${thisArticle.title}
                    <c:if test="${thisArticle.ownerId eq sessionScope.LOGIN_ACCOUNT.email}">
                        <a href="DispatcherServlet?btnAction=DeletePost&txtPostId=${thisArticle.postId}" 
                           onclick="return confirm('Are you sure you want to delete this post?');" class="set-left-margin">
                            <i class="fas fa-trash-alt"></i>
                        </a>
                    </c:if>
                </h5>
                <h6 class="card-subtitle mb-2 text-muted">${thisArticle.ownerName} at ${thisArticle.date}</h6>
                <p class="card-text set-background-color-grey">${thisArticle.description}</p>
                <c:if test="${not empty thisArticle.image}">
                    <div class="card bg-dark text-white">
                        <img src="data:image/png;base64,${thisArticle.image}" class="card-img">
                    </div>
                </c:if>
                <div class="set-top-margin-lg">
                    ${thisArticle.likes} 
                    <a href="DispatcherServlet?btnAction=Like&txtEmotioner=${sessionScope.LOGIN_ACCOUNT.email}&txtPostId=${thisArticle.postId}" class="set-right-margin">
                        <i class="far fa-thumbs-up fa-2x"></i>
                    </a>
                    ${thisArticle.dislikes}
                    <a href="DispatcherServlet?btnAction=Dislike&txtEmotioner=${sessionScope.LOGIN_ACCOUNT.email}&txtPostId=${thisArticle.postId}">
                        <i class="far fa-thumbs-down fa-2x"></i>
                    </a>
                </div>

            </div>
        </div>


        <c:set var="listComment" value="${requestScope.LIST_COMMENT}"></c:set>
            <div class="badge badge-primary text-wrap font-size-default set-left-margin set-top-margin-lg" style="width: 10rem;">
                Comment:
            </div>

        <c:forEach var="comment" items="${listComment}" varStatus="counter">
            <div class="card set-all-margin-e-left" style="width: 30rem;">
                <div class="card-body">
                    <h5 class="card-title">
                        ${comment.commentorName} at ${comment.date}
                        <c:if test="${sessionScope.LOGIN_ACCOUNT.email eq comment.commentor}">
                            <a href="DispatcherServlet?btnAction=DeleteComment&txtPostId=${thisArticle.postId}&txtCommentId=${comment.commentId}" 
                               onclick="return confirm('Are you sure you want to delete this comment?');" class="set-left-margin">
                                <i class="fas fa-trash-alt"></i>
                            </a>                  
                        </c:if>
                    </h5>
                    <p class="card-text set-background-color-comment">${comment.description}</p>
                </div>
            </div>
        </c:forEach>

        <br/>

        <form action="DispatcherServlet" method="POST">
            <div class="input-group mb-3 set-left-margin">
                <div class="col-xs-5">
                    <textarea name="txtComment" rows="4" cols="50" class="form-control" placeholder="Comment here..." required=""></textarea> 
                </div>
            </div>
            <c:set var="commentEmpty" value="${requestScope.COMMENT_EMPTY}"></c:set>

            <c:if test="${not empty commentEmpty}">
                <br/>
                <font color="red">
                ${commentEmpty}
                </font>
                <br/>
            </c:if>

            <input type="hidden" name="txtCommentor" value="${sessionScope.LOGIN_ACCOUNT.email}" />
            <input type="hidden" name="txtPostId" value="${thisArticle.postId}" />
            <input type="submit" value="Comment" name="btnAction" class="btn btn-primary btn-margin set-left-margin" />
        </form>

        <br/>
        <a href="DispatcherServlet?btnAction=Return&txtOwnerId=${sessionScope.LOGIN_ACCOUNT.email}" class="set-top-margin-lg set-left-margin">Click here to return to main page</a>  


    </body>
</html>
