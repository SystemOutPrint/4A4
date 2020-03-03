var GlobalData = require("network")

cc.Class({
    extends: cc.Component,

    properties: {
    },

    pokerAction: function (event) {
       console.log("go");
       var pokers = [];
       GlobalData.curPokers.forEach(function(val, index, arr) {
           console.log(arr[index]);
           var sprite = arr[index].getComponent(cc.Sprite);
           pokers.push(sprite.spriteFrame.name)
       });

       GlobalData.pokerAction(pokers);
    },

    pokerCancel: function (event) {
       console.log("cancel");
       GlobalData.curPokers.forEach(function(val, index, arr) {
           console.log(arr[index]);
           arr[index].y = 0;
       });
       
       GlobalData.pokerAction([]);
    }

    // update (dt) {},
});
