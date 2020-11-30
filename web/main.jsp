<%-- 
    Document   : main
    Created on : Sep 18, 2020, 8:51:40 AM
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
                
            <div class="badge set-badge-color text-wrap font-size-default set-left-margin" style="width: 20rem;">
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
            <br/>

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

        <div class="badge badge-primary text-wrap font-size-default set-left-margin set-top-margin-lg" style="width: 20rem;">
            Want to share something?
        </div>

        <c:set var="error" value="${requestScope.BLANK_POSTING}"></c:set>
        <c:if test="${not empty error}">
            <br/>
            <font color="red">
            ${error} <br/>
            </font>
        </c:if>


        <form action="DispatcherServlet" method="post" enctype="multipart/form-data" class="form-padding border border-primary form-margin set-left-margin set-top-margin-md" style="width: 40rem;">
            <div class="input-group mb-3 form-margin">
                <div class="col-xs-3">
                    <input type="text" name="txtTitle" value="" class="form-control" placeholder="Title" required="" />
                </div>

                <br/>
            </div>

            <div class="input-group mb-3 form-margin">
                <div class="col-xs-5">
                    <textarea name="txtareaPost" rows="4" cols="50" class="form-control" placeholder="Share something here..." required=""></textarea> 
                </div>
            </div>

            <br/>

            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="inputGroupFileAddon01">Upload</span>
                </div>
                <div class="custom-file">
                    <input type="file" name="photo" accept="image/*" class="custom-file-input" id="inputGroupFile01" aria-describedby="inputGroupFileAddon01" />
                    <label class="custom-file-label set-max-width" for="inputGroupFile01">Choose file</label>             
                </div>
            </div>


            <input type="hidden" name="txtOwnerId" value="${sessionScope.LOGIN_ACCOUNT.email}" />
            <input type="submit" value="Post" name="btnAction" class="btn btn-primary btn-margin" /> 
            <br/> 
            <br/>
        </form>

        <c:set var="listOfArticles" value="${requestScope.SEARCH_RESULT}"></c:set>


        <c:if test="${not empty listOfArticles}">
            <div class="badge badge-primary text-wrap font-size-default set-left-margin set-top-margin-lg" style="width: 20rem;">
                Search Result 
            </div>

            <br/>


            <c:forEach var="article" items="${listOfArticles}" varStatus="counter">
                <form action="DispatcherServlet">
                    <div class="card set-margin set-left-margin" style="width: 50rem;">
                        <div class="card-body">
                            <h5 class="card-title">${article.title}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">${article.ownerName} at ${article.date}</h6>
                            <p class="card-text set-background-color-grey">${article.description}</p>
                            <c:if test="${not empty article.image}">
                                <div class="card bg-dark text-white">
                                    <img src="data:image/png;base64,${article.image}" class="card-img">
                                </div>
                            </c:if>
                            <input type="hidden" name="txtPostId" value="${article.postId}" />
                            <input type="submit" value="View" name="btnAction" class="btn btn-primary btn-margin" />
                        </div>
                    </div>
                </form>
                <br/>
            </c:forEach>


            <c:set var="endPos" value="${requestScope.PAGING_SIZE}"></c:set>

                <nav aria-label="Page navigation" class="set-left-margin">
                    <ul class="pagination">
                    <c:if test="${param.txtPage ne 1}">
                        <li class="page-item">
                            <a class="page-link" href="DispatcherServlet?txtSearchValue=${param.txtSearchValue}&txtPage=${param.txtPage - 1}&btnAction=Search">
                                Previous
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${param.txtPage eq 1}">
                        <li class="page-item">
                            <a class="page-link disabled" href="main.jsp">
                                Previous
                            </a>
                        </li>
                    </c:if>    


                    <c:forEach var="thisPage" begin="1" end="${endPos}" varStatus="counter">
                        <li class="page-item">
                            <c:if test="${param.txtPage ne counter.count}">
                                <a class="page-link" href="DispatcherServlet?txtSearchValue=${param.txtSearchValue}&txtPage=${counter.count}&btnAction=Search">
                                    ${counter.count}
                                </a>
                            </c:if>
                            <c:if test="${param.txtPage eq counter.count}">
                                <a href="main.jsp" class="page-link disabled">${counter.count}</a>
                            </c:if>
                        </li>
                    </c:forEach>


                    <c:if test="${param.txtPage ne endPos}">
                        <li class="page-item">
                            <a class="page-link" href="DispatcherServlet?txtSearchValue=${param.txtSearchValue}&txtPage=${param.txtPage + 1}&btnAction=Search">
                                Next
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${param.txtPage eq endPos}">
                        <li class="page-item">
                            <a class="page-link disabled" href="main.jsp">
                                Next
                            </a>
                        </li>
                    </c:if> 
                </ul>
            </nav>
        </c:if>

    </body>
</html>
