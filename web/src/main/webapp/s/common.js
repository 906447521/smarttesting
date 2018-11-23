X = {}
X.HTTP = function (options) {
    var defaults = {
        timeout: 30000,
        loading: true,
        async: true,
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