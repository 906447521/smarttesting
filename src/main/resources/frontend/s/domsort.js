var domsortMouseDown = function (e, the, fn) {

    e = window.event || e;

    var that = $(the),
        height = 'height',
        width = 'width';

    if (that.css('box-sizing') == 'border-box') {
        height = 'outerHeight';
        width = 'outerWidth';
    }

    var $doc = $(document),
        fnEmpty = function () {
        },

        settings = $.extend(true, {

            down: fnEmpty,
            move: fnEmpty,
            up: fnEmpty,

            target: 'li',
            cloneStyle: {
                'background-color': '#ddd'
            },
            floatStyle: {
                'position': 'fixed',
                'border': '1px solid #ddd',
                'box-shadow': '-2px 2px 6px rgba(0,0,0,.2)',
                'webkitTransform': 'rotate(2deg)',
                'mozTransform': 'rotate(2deg)',
                'msTransform': 'rotate(2deg)',
                'transform': 'rotate(2deg)',
                'z-index': 999999
            }

        }, {});

    if (e.which != 1) {
        return;
    }

    var tagName = e.target.tagName.toLowerCase();
    if (tagName == 'input' || tagName == 'textarea' || tagName == 'select') {
        return;
    }

    var THIS = the,
        $this = $(THIS),
        offset = $this.offset(),
        disX = e.pageX - offset.left,
        disY = e.pageY - offset.top,

        clone = $this.clone()
            .css(settings.cloneStyle)
            .css('height', $this[height]())
            .empty(),

        hasClone = 1,

        thisOuterHeight = $this.outerHeight(),
        thatOuterHeight = that.outerHeight(),

        upSpeed = thisOuterHeight,
        downSpeed = thisOuterHeight,
        maxSpeed = thisOuterHeight * 3;

    settings.down.call(THIS);

    $doc.on('mousemove.DDSort', function (e) {
        if (hasClone) {
            $this.before(clone)
                .css('width', $this[width]())
                .css(settings.floatStyle)
                .appendTo($("main"));

            hasClone = 0;
        }

        var left = e.pageX - disX,
            top = e.pageY - disY,

            prev = clone.prev(),
            next = clone.next().not($this);

        $this.css({
            left: left,
            top: top
        });

        if (prev.length && top < prev.offset().top + prev.outerHeight() / 2) {

            clone.after(prev);

        } else if (next.length && top + thisOuterHeight > next.offset().top + next.outerHeight() / 2) {

            clone.before(next);

        }


        var thatScrollTop = that.scrollTop(),
            thatOffsetTop = that.offset().top,
            scrollVal;

        if (top < thatOffsetTop) {

            downSpeed = thisOuterHeight;
            upSpeed = ++upSpeed > maxSpeed ? maxSpeed : upSpeed;
            scrollVal = thatScrollTop - upSpeed;

        } else if (top + thisOuterHeight - thatOffsetTop > thatOuterHeight) {

            upSpeed = thisOuterHeight;
            downSpeed = ++downSpeed > maxSpeed ? maxSpeed : downSpeed;
            scrollVal = thatScrollTop + downSpeed;
        }

        that.scrollTop(scrollVal);

        settings.move.call(THIS);

    })
        .on('mouseup.DDSort', function () {

            $doc.off('mousemove.DDSort mouseup.DDSort');

            if (!hasClone) {
                clone.before($this.removeAttr('style')).remove();
                settings.up.call(THIS);
            }

            if(fn)
                fn()

        });
    return false;
}
