/*11111111111111111111111111111111111111111111111111111111111111111111111111111*/
$(document).ready(function (){
    $("#btn-p-1").click(function(){
        $("#RC-LC-box1").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-1").click(function(){
        $("#RC-LC-box1").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-1").click(function(){
        $("#btn-p-2, #btn-m-2").show();
        $("#btn-p-1").hide();
    });
});

var select = ["PART 선택"];
var LC_part = ["PART1", "PART2", "PART3", "PART4"];
var RC_part = ["PART5", "PART6", "PART7"];

function partChange1(){

    var selectPart = $("#form1_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form1_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form1_2').append(option);
    }
}
/*22222222222222222222222222222222222222222222222222222222222222222222222222222222*/

$(document).ready(function (){
    $("#btn-p-2").click(function(){
        $("#RC-LC-box2").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-2").click(function(){
        $("#RC-LC-box2").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-2").click(function(){
        $("#btn-p-2, #btn-m-2").hide();
        $("#btn-p-3, #btn-m-3").show();
    });
});

$(document).ready(function (){
    $("#btn-m-2").click(function(){
        $("#RC-LC-box1").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-2").click(function(){
        $("#RC-LC-box1").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-2").click(function(){
        $("#btn-p-2, #btn-m-2").hide();
        $("#btn-p-1").show();
    });
});

function partChange2(){

    var selectPart = $("#form2_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form2_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form2_2').append(option);
    }
}

/*33333333333333333333333333333333333333333333333333333333333333333333333333333*/

$(document).ready(function (){
    $("#btn-p-3").click(function(){
        $("#RC-LC-box3").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-3").click(function(){
        $("#RC-LC-box3").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-3").click(function(){
        $("#btn-p-3, #btn-m-3").hide();
        $("#btn-p-4, #btn-m-4").show();
    });
});

$(document).ready(function (){
    $("#btn-m-3").click(function(){
        $("#RC-LC-box2").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-3").click(function(){
        $("#RC-LC-box2").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-3").click(function(){
        $("#btn-p-3, #btn-m-3").hide();
        $("#btn-p-2, #btn-m-2").show();
    });
});

function partChange3(){


    var selectPart = $("#form3_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form3_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form3_2').append(option);
    }
}

/*444444444444444444444444444444444444444444444444444444444444444444444444444444444*/

$(document).ready(function (){
    $("#btn-p-4").click(function(){
        $("#RC-LC-box4").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-4").click(function(){
        $("#RC-LC-box4").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-4").click(function(){
        $("#btn-p-4, #btn-m-4").hide();
        $("#btn-p-5, #btn-m-5").show();
    });
});

$(document).ready(function (){
    $("#btn-m-4").click(function(){
        $("#RC-LC-box3").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-4").click(function(){
        $("#RC-LC-box3").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-4").click(function(){
        $("#btn-p-4, #btn-m-4").hide();
        $("#btn-p-3, #btn-m-3").show();
    });
});

function partChange4(){


    var selectPart = $("#form4_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form4_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form4_2').append(option);
    }
}


/*55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555*/

$(document).ready(function (){
    $("#btn-p-5").click(function(){
        $("#RC-LC-box5").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-5").click(function(){
        $("#RC-LC-box5").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-5").click(function(){
        $("#btn-p-5, #btn-m-5").hide();
        $("#btn-p-6, #btn-m-6").show();
    });
});

$(document).ready(function (){
    $("#btn-m-5").click(function(){
        $("#RC-LC-box4").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-5").click(function(){
        $("#RC-LC-box4").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-5").click(function(){
        $("#btn-p-5, #btn-m-5").hide();
        $("#btn-p-4, #btn-m-4").show();
    });
});

function partChange5(){


    var selectPart = $("#form5_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form5_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form5_2').append(option);
    }
}

/*66666666666666666666666666666666666666666666666666666666666666666666666666666666666666*/

$(document).ready(function (){
    $("#btn-p-6").click(function(){
        $("#RC-LC-box6").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-6").click(function(){
        $("#RC-LC-box6").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-6").click(function(){
        $("#btn-p-6, #btn-m-6").hide();
        $("#btn-p-7, #btn-m-7").show();
    });
});

$(document).ready(function (){
    $("#btn-m-6").click(function(){
        $("#RC-LC-box5").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-6").click(function(){
        $("#RC-LC-box5").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-6").click(function(){
        $("#btn-p-6, #btn-m-6").hide();
        $("#btn-p-5, #btn-m-5").show();
    });
});

function partChange6(){


    var selectPart = $("#form6_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form6_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form6_2').append(option);
    }
}

/*77777777777777777777777777777777777777777777777777777777777777777777777777777777777777777*/

$(document).ready(function (){
    $("#btn-p-7").click(function(){
        $("#RC-LC-box7").find("label, select, option").prop("disabled", false);
    });
});

$(document).ready(function (){
    $("#btn-p-7").click(function(){
        $("#RC-LC-box7").find("label, select, option").show();
    });
});

$(document).ready(function (){
    $("#btn-p-7").click(function(){
        $("#btn-p-7, #btn-m-7").hide();
        $("#btn-m-8").show();
    });
});

$(document).ready(function (){
    $("#btn-m-7").click(function(){
        $("#RC-LC-box6").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-7").click(function(){
        $("#RC-LC-box6").find("label, select, option").hide();
    });
});


$(document).ready(function (){
    $("#btn-m-7").click(function(){
        $("#btn-p-7, #btn-m-7").hide();
        $("#btn-p-6, #btn-m-6").show();
    });
});

$(document).ready(function (){
    $("#btn-m-8").click(function(){
        $("#btn-p-7, #btn-m-7").show();
    });
});

function partChange7(){

    var selectPart = $("#form7_1").val();

    var changePart;

    if(selectPart === "선택"){
        changePart = select;
    }
    else if(selectPart === "LC"){
        changePart = LC_part;
    }
    else if(selectPart === "RC"){
        changePart = RC_part;
    }

    $('#form7_2').empty();


    for(var count = 0; count < changePart.length; count++){
        var option = $("<option>" + changePart[count] + "</option>");
        $('#form7_2').append(option);
    }
}

/*==============================================================*/
$(document).ready(function (){
    $("#btn-m-8").click(function(){
        $("#RC-LC-box7").find("label, select, option").prop("disabled", true);
    });
});

$(document).ready(function (){
    $("#btn-m-8").click(function(){
        $("#RC-LC-box7").find("label, select, option").hide();
    });
});

$(document).ready(function (){
    $("#btn-m-8").click(function(){
        $("#btn-m-8").hide();
    });
});