<%-- 
    Document   : register
    Created on : Sep 16, 2020, 10:45:19 AM
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="css/all.css">
        <link rel="stylesheet" type="text/css" href="css/custom.css">
        <style type="text/css">
            body{
                padding: 20px 0px;
            }
        </style>
    </head>
    <body>
        <div class="badge badge-primary font-size-default set-left-margin" style="width: 30rem;">
            <h2 class="text-font-bold">REGISTER</h2>
        </div>
        <div class="border border-primary border-padding set-max-width-1000 set-left-margin set-div-background-color">
            <form action="DispatcherServlet" method="POST">
                <c:set var="error" value="${requestScope.ERROR}"></c:set>
                    <div class="form-group row">
                        <label for="inputEmail" class="col-sm-2 col-form-label">Email:</label>

                        <div class="col-sm-3">
                            <input type="email" name="txtEmail" value="" required class="form-control"  id="inputEmail"/> 
                        </div>
                    </div>

                <c:if test="${not empty error.emailExistedErr}">
                    <font color="red">
                    ${error.emailExistedErr}
                    </font>
                    <br/>
                </c:if>
                    
                <c:if test="${not empty error.emailFormatErr}">
                    <font color="red">
                    ${error.emailFormatErr}
                    </font>
                    <br/>
                </c:if>

                <c:if test="${not empty error.emailLengthErr}">
                    <font color="red">
                    ${error.emailLengthErr}
                    </font>
                    <br/>
                </c:if>

                <div class="form-group row">
                    <label for="inputName" class="col-sm-2 col-form-label">Name:</label>

                    <div class="col-sm-3">
                        <input type="text" name="txtName" value="" required class="form-control"  id="inputName"/> 
                    </div>
                </div>    

                <c:if test="${not empty error.nameLengthErr}">
                    <font color="red">
                    ${error.nameLengthErr}
                    </font>
                    <br/>
                </c:if>

                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-2 col-form-label">Password:</label>

                    <div class="col-sm-3">
                        <input type="password" name="txtPassword" value="" required class="form-control"  id="inputPassword"/> 
                    </div>
                </div> 

                <c:if test="${not empty error.passLengthErr}">
                    <font color="red">
                    ${error.passLengthErr}
                    </font>
                    <br/>
                </c:if>

                <div class="form-group row">
                    <label for="inputConfirm" class="col-sm-2 col-form-label">Confirm password :</label>

                    <div class="col-sm-3">
                        <input type="password" name="txtConfirm" value="" required class="form-control"  id="inputConfirm"/> 
                    </div>
                </div>     

                <c:if test="${not empty error.confirmErr}">
                    <font color="red">
                    ${error.confirmErr}
                    </font>
                </c:if>
                <br/>
                <input type="submit" value="Register" name="btnAction" class="btn btn-primary" />
                <input type="reset" value="Reset" class="btn btn-primary" />
            </form>
            <br/>
        </div>


        <div class="font-size-default set-left-margin set-top-margin-lg" style="width: 20rem;">
            <h6>Have an account? Go to login 
                <a href="login.html">here</a>
            </h6>           
        </div>

    </body>
</html>
