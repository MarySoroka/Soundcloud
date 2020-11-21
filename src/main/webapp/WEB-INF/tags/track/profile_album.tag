
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="album" type="com.soundcloud.album.Album" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="user" type="com.soundcloud.user.UserDto" required="true"
                         description="user"/>
<div class="col s3">
    <div class="card hoverable">
        <div class="card-image">
            <img src="data:image/jpg;base64,${album.albumIconBase64}">
        </div>
        <div class="card-content  grey darken-1">
            <span class="card-title">${user.login}</span>
            <a  href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.PROFILE}"><p >${album.name}</p></a>
        </div>
    </div>
</div>