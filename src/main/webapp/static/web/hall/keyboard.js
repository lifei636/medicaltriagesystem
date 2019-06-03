/// <reference path="jquery.min.js" />

$(function () {
    var serialNum = $("#serialNum");
    //$("#serialOK").bind("vmousedown", function (e) {
    //    var obj = this;
    //    obj.style.background = "#faa";
    //    setTimeout(function () {
    //        serialOK();
    //        obj.style.background = "white";
    //        return false;
    //    }, 200);
    //})

    //  $("#keyboard li").mousedown(function () {
    $("#keyboard li").bind("vmousedown", function () {
        if (event.stopPropagation) {
            // 针对 Mozilla 和 Opera   
            event.stopPropagation();
        }
        else if (window.event) {
            // 针对 IE   
            window.event.cancelBubble = true;
        }   

        var obj = this;
        obj.style.background = "#55cab3";// "#208e9b";
        var value = this.getAttribute("data-value");
        if (value == "del") {
            if (serialNum.html().length > 0) {
                serialNum.html(serialNum.html().substr(0, serialNum.html().length - 1))
            }
        } else if (value == "delAll") {
            if (serialNum.html().length > 0) {
                serialNum.html("");
            }
        }
        else {
            serialNum.html(serialNum.html() + value)
        }
        setTimeout(function () {
            obj.style.background = "#eee";
        }, 200);
    });
})