window.document.onload = function () {
    document.documentElement.style.webkitTouchCallout = "none"; //禁止弹出菜单
    document.documentElement.style.webkitUserSelect = "none";//禁止选中
    document.documentElement.style.w
}
var spots = {}, touches, timer;

document.addEventListener('touchstart', function (e) {
    e.preventDefault();
    [].forEach.call(e.targetTouches, function (touch) {
        //对每一根触摸在屏幕上的手指都生成一个元素，并且用touch.identifier作为该元素的唯一标识，触摸结束后清除引用的元素
        if (spots[touch.identifier]) {
            return;
        }
        var spot = spots[touch.identifier] = document.createElement('div');
        spot.classList.add('spot');
        spot.style.top = touch.pageY - 35;
        spot.style.left = touch.pageX - 35;
        document.body.appendChild(spot);
    })
    timer = setInterval(function () {
        renderTouches(touches);
    }, 16);
}, false);

document.addEventListener('touchmove', function (e) {
    e.preventDefault();
    touches = e.touches;
});

function renderTouches(touches) {
    if (!touches) {
        return;
    };
    [].forEach.call(touches, function (touch) {
        var spot = spots[touch.identifier];
        if (spot) {
            spot.style.top = touch.pageY - 35;
            spot.style.left = touch.pageX - 35;
        }
    })
}

document.addEventListener('touchend', function (e) {
    e.preventDefault();
    [].forEach.call(e.changedTouches, function (touch) {
        var spot = spots[touch.identifier];
        if (spot) {
            document.body.removeChild(spot);
            delete spots[touch.indentifier];
        }
    })
    if (e.changedTouches.length === 0) {
        clearInterval(timer);
    }
})