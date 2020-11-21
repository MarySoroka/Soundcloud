<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:directive.attribute name="errors" type="java.util.List" required="true"
                         description="user"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty errors}">
        <c:forEach items="${errors}" var="error">
            <div class="row">
                <div class="col s12" style="color:#ee6e73;">
                    <c:forEach items="${error.errors}" var="err">
                        <span class="helper-text" data-error="wrong">${err}</span>
                    </c:forEach>
                    <span class="helper-text" data-error="wrong">${error.field}</span>
                </div>
            </div>
        </c:forEach>
    </c:when>
</c:choose>