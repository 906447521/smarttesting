function looks_like_html(source) {
    var trimmed = source.replace(/^[ \t\n\r]+/, '');
    var comment_mark = '<' + '!-' + '-';
    return (trimmed && (trimmed.substring(0, 1) === '<' && trimmed.substring(0, 4) !== comment_mark));
}
function looks_like_ognl(source) {
    if(source ==null)
        return false
    source =  source.trim()
    if(source.indexOf("#String")==0 || source.indexOf("#Response")==0 || source.indexOf("#Collection")==0) {
        return true
    }
    return false
}
function looks_like_urlencode(source) {
    if(source ==null)
        return false
    try {
        var pair = source.split("&")
        var a = 0;
        for(var i in pair) {
            s = pair[i].split("=")
            if(s.length > 1)
                a++;
        }
    } catch (e) {
        return false
    }
    if(a == 0)
        return false
    return true
}

function any(a, b) {
    return a || b;
}

function beautify(the) {
    var the = the
    if (the.beautify_in_progress) return;

    the.beautify_in_progress = true;

    var source = the.editor ? the.editor.getValue() : $('#source').val(),
        output,
        opts = {};

    opts.indent_size = '4'
    opts.indent_char = opts.indent_size == 1 ? '\t' : ' ';
    opts.max_preserve_newlines = '5'
    opts.preserve_newlines = opts.max_preserve_newlines !== "-1";
    opts.keep_array_indentation = true
    opts.break_chained_methods = false
    opts.indent_scripts = 'normal'
    opts.brace_style = 'collapse'
    opts.space_before_conditional = true
    opts.unescape_strings = true
    opts.wrap_line_length = '0'
    opts.space_after_anon_function = true;

    if (looks_like_html(source)) {
        output = html_beautify(source, opts);
    } else if (looks_like_urlencode(source)) {
        output = source
    } else if (looks_like_ognl(source)) {
        output = source
    } else {
        output = js_beautify(source, opts);
    }

    if (the.editor) {
        the.editor.setValue(output);
    } else {
        the.val(output);
    }

    the.beautify_in_progress = false;
}

function newThe($textarea) {

    var textarea = $textarea;


    var default_text = '';
    var placeholder = textarea.attr("placeholder");
    var text = '';
    if ((placeholder != undefined || placeholder != null) && placeholder != '') {
        text = placeholder;
    }
    if ((textarea.val() != undefined || textarea.val() != null) && textarea.val() != '') {
        text = textarea.val();
    }

    var the = {
        use_codemirror: (!window.location.href.match(/without-codemirror/)),
        beautify_in_progress: false,
        editor: null, // codemirror editor
        default_text: placeholder,
        $textarea: $textarea
    }

    var textArea = textarea[0];

    if (the.use_codemirror && typeof CodeMirror !== 'undefined') {
        the.editor = CodeMirror.fromTextArea(textArea, {
            theme: 'default',
            lineNumbers: true

        });
        // the.editor.focus();
        the.editor.setValue(text);
        $(the.editor.display.wrapper).click(function () {
            if (the.editor.getValue() == the.default_text) {
                the.editor.setValue('');
            }
        })
        the.editor.on("change", function () {
            the.$textarea.html(the.editor.getValue("\n"))
        })
    }

    var button = textarea.attr("for")

    $('#' + button).click(function () {
        beautify(the)
    });

    var id = "___" + $textarea.attr('id')
    TheTextArea[id] = the

    return the;
}


var TheTextArea = new Object()


function setBeautyTextareaValue(id, value) {
    var oldValue = TheTextArea["___" + id].editor.getValue()
    TheTextArea["___" + id].editor.setValue(oldValue + value + "\r\n")
}

function setBeautyTextareaValueAndClearOld(id, value) {
    TheTextArea["___" + id].editor.setValue(value)
}

function beautytextarea() {
    $(".beautify-textarea").each(function () {
        var the = $(this);
        if (the.attr("beauty-codemirror") != 'true') {
            newThe(the)
            the.attr("beauty-codemirror", 'true');
        }

    })


    $(".tab-hide").hide()
}

$(function () {
    beautytextarea()
});