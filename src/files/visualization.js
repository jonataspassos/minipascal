var zoom, translateX, translateY, reset, help;

(function () {

    var KEY_DOWN = 40, KEY_UP = 38, KEY_LEFT = 37, KEY_RIGHT = 39;
    var KEY_END = 35, KEY_BEGIN = 36;
    var KEY_BACK_TAB = 8, KEY_TAB = 9, KEY_SH_TAB = 16, KEY_ENTER = 13, KEY_ESC = 27, KEY_SPACE = 32, KEY_DEL = 46;
    var KEY_A = 65, KEY_B = 66, KEY_C = 67, KEY_D = 68, KEY_E = 69, KEY_F = 70, KEY_G = 71, KEY_H = 72, KEY_I = 73,
        KEY_J = 74, KEY_K = 75, KEY_L = 76, KEY_M = 77, KEY_N = 78, KEY_O = 79, KEY_P = 80, KEY_Q = 81, KEY_R = 82,
        KEY_S = 83, KEY_T = 84, KEY_U = 85, KEY_V = 86, KEY_W = 87, KEY_X = 88, KEY_Y = 89, KEY_Z = 90;
    var KEY_PF1 = 112, KEY_PF2 = 113, KEY_PF3 = 114, KEY_PF4 = 115, KEY_PF5 = 116, KEY_PF6 = 117, KEY_PF7 = 118, KEY_PF8 = 119;

    var REMAP_KEY_T = 5019;

    var factor = 0.1;
    var z = 1.0;
    var tx = 0;
    var ty = 0;
    function setTransform() {
        document.querySelector("#dynamic").setAttribute("transform", "translate(" + tx + "," + ty + ")scale(" + z + "," + z + ")");
    }

    function w() {
        return document.querySelector("svg").getBoundingClientRect().width;
    }

    function h() {
        return document.querySelector("svg").getBoundingClientRect().height;
    }

    function checkEventObj(_event_) {
        // --- IE explorer
        if (window.event)
            return window.event;
        // --- Netscape and other explorers
        else
            return _event_;
    }
    //false - Zoom out; True - Zoom In
    zoom = function (s) {
        if (s == false) {
            z /= 1 + factor;
            tx /= 1 + factor;
            ty /= 1 + factor;
        } else {
            z *= 1 + factor;
            tx *= 1 + factor;
            ty *= 1 + factor;
        }


        setTransform()
    }
    //false - Translate Camera to left; True - Translade Xamera to right
    translateX = function (s) {
        if (s == false) {
            tx += w() * factor;
        } else {
            tx -= w() * factor;
        }
        setTransform()
    }
    //false - Translate Camera to down; True - Translade Xamera to Up
    translateY = function (s) {
        if (s == false) {
            ty += h() * factor;
        } else {
            ty -= h() * factor;
        }
        setTransform()
    }
    
    reset = function () {
        z = 1.0;
        tx = 0;
        ty = 0;
        setTransform();
    }

    help = function () {
        alert("This is a visualization of SVG.\n" +
            "You can:\n" +
            "   Change Zoom:\n" +
            "       Zoom        (Ctrl + Up_arrow , Ctrl + Dowm_arrow)\n" +
            "   Change translation:\n" +
            "       Translate X (Left_arrow , Right_arrow)\n" +
            "       Translate Y (Up_arrow , Down_arrow)\n" +
            "   Reset View (R)\n"
        );
    }

    document.onkeydown = function (_event_) {
        var winObj = checkEventObj(_event_);
        var intKeyCode = winObj.keyCode;
        var intAltKey = winObj.altKey;
        var intCtrlKey = winObj.ctrlKey;
        function finishKey() {
            winObj.keyCode = intKeyCode = REMAP_KEY_T;
            winObj.returnValue = false;
            return false;
        }


        // 1° --- Access with [ALT/CTRL+Key]
        if (intAltKey || intCtrlKey) {

            if (intKeyCode == KEY_RIGHT || intKeyCode == KEY_UP) {
                zoom(true);
                return finishKey();
            } else if (intKeyCode == KEY_LEFT || intKeyCode == KEY_DOWN) {
                zoom(false);
                return finishKey();
            } else if (intKeyCode == KEY_R) {
                reset();
                return finishKey();
            }
        }
        // 2 ° --- Access without [ALT/CTRL+Key]
        else {

            if (intKeyCode == KEY_RIGHT) {
                translateX(true);
                return finishKey();
            } else if (intKeyCode == KEY_LEFT) {
                translateX(false);
                return finishKey();
            } else if (intKeyCode == KEY_UP) {
                translateY(false);
                return finishKey();
            } else if (intKeyCode == KEY_DOWN) {
                translateY(true);
                return finishKey();
            } else if (intKeyCode == KEY_R) {
                reset();
                return finishKey();
            }

        }
    }
	
	document.querySelector("svg").addEventListener("wheel", function(_event_){
        var winObj = checkEventObj(_event_);
        if(winObj.ctrlKey){//Zoom
            zoom(winObj.deltaY>0)
        }else if(winObj.altKey){//X Translation
            translateX(winObj.deltaY>0)
        }else{//y Translation
            translateY(winObj.deltaY>0)
        }
        winObj.returnValue = false;
        return false;
    });
	
    var zoonables = document.querySelectorAll(".expression");
    zoonables.forEach(function (d, i) {
        d.setAttribute("style", "cursor: pointer;");
        d.addEventListener("click", function () {
            reset();
            var svg = document.querySelector("svg");

            var w = this.getBoundingClientRect();
            var h = w.height * z,
                x = (w.x - svg.getBoundingClientRect().x) * z,
                y = (w.y - svg.getBoundingClientRect().y) * z;
            w = w.width * z;

            x -= w / 2;
            w += w / 2;

            z = Math.min(svg.getBoundingClientRect().width / w, svg.getBoundingClientRect().height / h);
            tx = -x * z;
            ty = -y * z;

            setTransform();
        }, false);
    });
})()