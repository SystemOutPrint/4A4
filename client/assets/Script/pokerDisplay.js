Array.prototype.indexOf = function(val) { 
    for (var i = 0; i < this.length; i++) { 
        if (this[i] == val) 
            return i; 
    } 
    return -1; 
};

Array.prototype.remove = function(val) { 
    var index = this.indexOf(val); 
    if (index > -1) { 
        this.splice(index, 1); 
    }
}; 

var GlobalData = require("network")

cc.Class({
    extends: cc.Component,

    properties: {
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad () {
        this.node.on(cc.Node.EventType.MOUSE_DOWN, this.touchBegin, this);
    },

    touchBegin: function(event) {
        var node = event.target;
        if (node.getComponent(cc.Sprite) != null) {
            console.log(node);
            if (node.y == 10) {
                node.y = 0;
                GlobalData.curPokers.remove(node);
            } else {
                node.y = 10;
                GlobalData.curPokers.push(node);
            }
            console.log(GlobalData.curPokers);
            event.stopPropagation();
        }
    },
});