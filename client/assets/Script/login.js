
var GlobalData = require("network")

cc.Class({
    extends: cc.Component,

    properties: {
        userNameEdit: cc.EditBox,
        passwordEdit: cc.EditBox,
    },


    onLoad () {
        
    },

    login: function(event) {
        if (GlobalData.createConnection()) {
            this.scheduleOnce(function(){
                if (GlobalData.networkLogin(this.userNameEdit.string, this.passwordEdit.string)) {
                    // NOOP
                    GlobalData.initAllPokerSpriteFrames();
                }
            }, 1);
            
        }
    }
});