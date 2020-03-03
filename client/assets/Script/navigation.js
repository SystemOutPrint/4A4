
var GlobalData = require("network")

cc.Class({
    extends: cc.Component,

    properties: {
        roomIdEdit: cc.EditBox,
        roomIdLabel: cc.Label,
    },


    onLoad () {
        
    },

    createRoom: function(event) {
        GlobalData.createRoom();
    },

    joinRoom: function(event) {
        var roomId = Number(this.roomIdEdit.string);
        GlobalData.joinRoom(roomId);
        this.renderRoomIdLabel(roomId);
    },

    renderRoomIdLabel: function(roomId) {
        this.roomIdLabel.string = roomId;
    }
});