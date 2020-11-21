

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <script src="http://localhost:8080/soundcloud/jsp/static/js/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="http://localhost:8080/soundcloud/jsp/static/js/custom.js"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<script type="text/javascript">
    M.AutoInit();
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.dropdown-trigger');
        var instances = M.Dropdown.init(elems, options);
    });
    $('.dropdown-trigger').dropdown();
</script>