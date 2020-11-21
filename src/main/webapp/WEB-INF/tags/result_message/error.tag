<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:directive.attribute name="errors" type="java.util.List" required="true"
                         description="user"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty errors}">
        <c:forEach items="${errors}" var="error">
            <div class="row">
                <div class="col s10 offset-l1" style="color:#ee6e73;">
                    <span class="helper-text" data-error="wrong">${error}</span>
                </div>
            </div>
        </c:forEach>
    </c:when>
</c:choose>