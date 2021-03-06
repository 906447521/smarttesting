X = {}
X.HTTP = function (options) {
    var defaults = {
        timeout: 30000,
        loading: true,
        async: true,
        type: 'POST',
        cache: false
    };
    //$.ajaxSetup({ cache: false });
    options = $.extend({},
        defaults, {},
        options);
    var complete = options.complete;
    var error = options.error;
    var success = options.success;
    options.complete = function (jqXHR, textStatus) {
        if (complete) {
            try {
                complete(jqXHR, textStatus);
            } catch (e) {

            }
        }

    };
    options.error = function (jqXHR, textStatus, errorThrown) {
        if (error) {
            try {
                error(jqXHR, textStatus, errorThrown, 'error, url:' + options.url);
            } catch (e) {
                Main.message.log(e);
            }
        } else {
            if (jqXHR.status == 401) {

            } else if (jqXHR.readyState == 4) {

            }
        }
    };
    options.success = function (data, textStatus, jqXHR) {
        if (success) {
            try {
                if (typeof(data) != 'object') {
                    data = jQuery.parseJSON(data);

                }

                try {
                    if (data.code == 1) {
                        console.log(data)
                        M.toast({html: data.message})
                    }
                } catch (e) {
                }

                success(data, textStatus, jqXHR);
            } catch (e) {

            }
        }
    };

    $.ajax(options);
}

X.GetUrlParam = function (paraName) {
    var url = document.location.toString();
    var arrObj = url.split("?");

    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;

        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");

            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}

X.checkAll = function (that) {
    var that = $(that);
    var checkfor = that.attr("check-for")
    if (that.not(':checked').length > 0) {
        $("." + checkfor).prop("checked", false)
    } else {
        $("." + checkfor).prop("checked", true)
    }
}
function dateFormat(datesrc, format) {
    var date = new Date(datesrc)
    var o = {
        "M+": date.getMonth() + 1, //month
        "d+": date.getDate(), //day
        "H+": date.getHours(), //hour
        "m+": date.getMinutes(), //minute
        "s+": date.getSeconds(), //second
        "q+": Math.floor((date.getMonth() + 3) / 3), //quarter
        "S": date.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

function splitLen(s1, s2) {
    try {
        if (s1 == null || s1 == '') {
            return 0
        }
        if (s1.indexOf(s2) < 0) {
            return 1
        }
        return s1.split(s2).length;
    } catch (e) {
        return 0;
    }
}
$(function () {
    // Main.$menus.each(function () {
    //     var url = that.url;
    //     var nav = $(this).attr("main-href");
    //     if (nav && nav != '/') {
    //         if (("/" + url + "/").indexOf(nav) >= 0) {
    //             $(this).addClass("active")
    //             return false
    //         }
    //     }
    // })
})