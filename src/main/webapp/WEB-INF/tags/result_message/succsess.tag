<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:directive.attribute name="message" type="java.lang.String" required="true"
                         description="user"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty message}">
        <div class="row">
            <div class="col s10 offset-l1">
                <span class="helper-text" data-error="">${message}</span>
            </div>
        </div>
    </c:when>
</c:choose>