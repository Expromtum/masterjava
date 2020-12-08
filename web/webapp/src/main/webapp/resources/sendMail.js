function send() {
    $('#result').html("Sending ...");

    var users = $("input:checkbox:checked").map(function () {
        return this.value;
    }).get();

    $.post("sendMail", "users=" + users + "&subject=" + $("#subject").val() + "&body=" + $("#body").val())
        .done(function (result) {
            $('#result').html(result);
        })
        .fail(function (result) {
            $('#result').html(result);
        });
}