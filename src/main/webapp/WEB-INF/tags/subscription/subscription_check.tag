<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<jsp:directive.attribute name="subscribe" type="java.lang.Boolean" required="true"
                         description="Is subscribed"/>
<jsp:directive.attribute name="uploadPage" type="java.lang.String" required="true"
                         description="page"/>
<jsp:directive.attribute name="profilePage" type="java.lang.String" required="true"
                         description="page"/>
<c:choose>
    <c:when test="${subscribe}">
        <div class="row white-text grey darken-2">
            <div class="col s10 offset-l1 section">
                <fmt:message key="upload.no.albums"/> <a
                    href="${uploadPage}"><fmt:message
                    key="navbar.upload"/></a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row white-text grey darken-2">
            <div class="col s10 offset-l1 section">
                <h4><fmt:message key="subscribe.text"/></h4>
                <i class="material-icons medium white-text right">music_note</i>
                <p><fmt:message key="upload.subscribe"/>
                    <a href="${profilePage}"><fmt:message key="navbar.profile"/></a></p>
            </div>
        </div>
    </c:otherwise>
</c:choose>