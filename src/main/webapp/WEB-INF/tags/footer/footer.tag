<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:directive.attribute name="page" type="java.lang.String" required="true"
                         description="page"/>
<jsp:directive.attribute name="help" type="java.lang.String" required="true"
                         description="page"/>
<jsp:directive.attribute name="policy" type="java.lang.String" required="true"
                         description="page"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="/i18n/ApplicationMessages" scope="application"/>
<c:choose>
    <c:when test="${not empty requestScope.get('lang')}">
        <fmt:setLocale value="${requestScope.get('lang')}"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="${cookie['lang'].value}"/>
    </c:otherwise>
</c:choose>
<footer class="grey darken-3 z-depth-4 page-footer">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <p class="grey-text text-lighten-4"></p>
            </div>
            <div class="col l4 offset-l2 s12">
                <ul>
                    <li><a class="grey-text text-lighten-3" href="${pageContext.request.contextPath}/?${help}"><fmt:message key="footer.policy"/></a></li>
                    <li><a class="grey-text text-lighten-3" href="${pageContext.request.contextPath}/?${policy}"><fmt:message key="footer.help"/></a></li>
                </ul>
                <ul id="dropdown2" class="dropdown-content">
                    <li><a href="${pageContext.request.contextPath}?${page}&lang=en"><fmt:message key="links.lang.en"/></a></li>
                    <li><a href="${pageContext.request.contextPath}?${page}&lang=ru"><fmt:message key="links.lang.ru"/></a></li>
                </ul>
                <a class="btn dropdown-trigger" href="#!" data-target="dropdown2">
                    ${requestScope.lang}
                        <i class="material-icons right">arrow_drop_down</i>
                </a>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
             <fmt:message key="footer.name"/>
        </div>
    </div>
</footer>
<script type="text/javascript">
    M.AutoInit();
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.dropdown-trigger');
        var instances = M.Dropdown.init(elems, options);
        var instance = M.Dropdown.getInstance(elem);
        instance.dropdownEl.lang;
    });
</script>