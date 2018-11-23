(function () {


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

    template.helper('floor', Math.floor);
    template.helper('round', Math.round);
    template.helper('ceil', Math.ceil);
    template.helper('jsonStringify', JSON.stringify);
    template.helper('splitLen', splitLen);
    template.helper('dateFormat', dateFormat);

})()

