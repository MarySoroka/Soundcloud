
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="album" type="com.soundcloud.album.Album" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="track" type="com.soundcloud.track.Track" required="true"
                         description="track"/>
<jsp:directive.attribute name="user" type="com.soundcloud.user.UserDto" required="true"
                         description="user"/>
<div class="col s3">
    <div class="card hoverable">
        <div class="card-image">
            <img src="data:image/jpg;base64,${album.albumIconBase64}">
            <a id="audiobutton" class="btn-floating halfway-fab waves-effect waves-light teal lighten-1" onclick="togglePlay(${track.id})"><i id="icn" class="material-icons">play_arrow</i></a>
        </div>
        <div class="card-content  grey darken-1">
            <span class="card-title">${user.login}</span>
            <p >${track.name}</p>
        </div>
    </div>
    <audio id="${track.id}" src="${track.trackPath}" preload="auto">
    </audio>
    <script >
        function togglePlay(trackId) {
            const myAudio = document.getElementById(trackId);
            return myAudio.paused ? myAudio.play() : myAudio.pause();
        };
    </script>
</div>