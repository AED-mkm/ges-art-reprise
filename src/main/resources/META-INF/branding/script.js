(function () {
    window.addEventListener("load", function () {
        setTimeout(function () {
            var logo = document.getElementsByClassName('link');
            logo[0].href = "universe.com";
            logo[0].target = "_blank";
            logo[0].children[0].alt = "OPEN API universe";
            logo[0].children[0].src = "src/main/resources/logo.png";
        });
    });
})();
