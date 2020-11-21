
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="album" type="com.soundcloud.album.Album" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="track" type="com.soundcloud.track.Track" required="true"
                         description="track"/>
<jsp:directive.attribute name="user" type="com.soundcloud.user.UserDto" required="true"
                         description="user"/>


<li class="collection-item container">
    <div>${track.name} <a href="#!" class="secondary-content">
        <i class="material-icons songPlay" data-link="${track.trackPath}"
           data-name='${user.login} - ${track.name}' cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a></div>
</li>
<li>
    <div class="divider"></div>
</li>