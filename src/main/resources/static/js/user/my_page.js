$(document).ready(function () {
    $("#update_password_button").click(function () {
        $.ajax({
            type: 'GET',
            url: '/update/password',
            data: 'password=' + $("#password").val() + '&userId=' + $("#userId").val(),
            dataType: 'json',
            success: function (result){
                $(".modal-body").html(result.message);
                $("#myPageButton1").modal();
            }
        });
    });

    $("#delete_button").click(function () {
        $.ajax({
            type: 'GET',
            url: '/update/delete',
            data: 'userId=' + $("#userId").val(),
            dataType: 'json',
            success: function (result){
                $(".modal-body").html(result.message);
                $("#myPageButton2").modal();
            }
        });
    });

});