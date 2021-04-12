$(document).ready(function(){
    $("#add-question").click(function(){
        add_question();
        $('.btn-Remove').click(function(){
            /* $(".select-form-div").remove();*/
            $(this).prev().prev().prev().remove()
            $(this).prev().prev().remove()
            $(this).prev().remove()
            $(this).next().remove()
            $(this).remove();
            /*$("#select-form-div").remove();*/

        });
    });
});

function add_question(){

    var html ='<select class="form-line-select" th:name="select-form1">\
            <option selected>선택</option>\
            <option value="LC">LC</option>\
            <option value="RC">RC</option></select>\
            <select class="form-line-select" th:name="select-form2">\
            <option selected>PART 선택</option>\
            <option value="PART1">PART1</option>\
            <option value="PART2">PART2</option>\
            <option value="PART3">PART3</option>\
            <option value="PART4">PART4</option></select>\
            <select class="form-line-select" th:name="select-form3">\
            <option selected>문항선택</option>\
            <option value="5">5</option>\
            <option value="10">10</option>\
            <option value="15">15</option>\
            <option value="20">20</option></select>\
            <button type="button" class="btn far fa-trash-alt btn-Remove"></button><br>';

    $("#box").append(html);
}