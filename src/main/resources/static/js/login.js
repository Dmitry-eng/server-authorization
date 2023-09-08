$(document).ready(function () {
alert(2)

    $("#loginButton").click(function (event) {

        $.ajax({
            statusCode: {
                200: function (xhr) {
                    window.localStorage.setItem("Authorization", xhr.type + " " + xhr.accessToken);
                }
            },


                        url: "/account/auth/login",
                        type: 'POST',
contentType: "application/json",
                        data: JSON.stringify(buildLoginJSON()),
                        dataType: 'JSON',
                        cache: false,
                        processData: false,
        })
            .done(function () {
            });

    });
});

function buildLoginJSON() {
            var account = new Object();
            account.login = $("#login").val();
            account.password = $("#password").val();
            return account;
}