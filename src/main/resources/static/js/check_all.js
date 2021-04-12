$(document).ready(function(){
    //최상단 체크박스 클릭
    $("#checkAll").click(function(){
        if($("#checkAll").is(":checked")){
            $(".chk").prop("checked", true);
        } else {
            $(".chk").prop("checked", false);
        }
    })

    // 전체 선택 중 한개의 체크박스 선택 해제 시 전체선택 체크박스 해제
    $(".chk").click(function(){
        if($("input[name='check[]']:checked").length == 3){
            $("#checkAll").prop("checked", true);
        }else{
            $("#checkAll").prop("checked", false);
        }
    })
})
