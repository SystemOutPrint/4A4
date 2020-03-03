
var networkGlobalData = {
    websocket: null,
    tk: null,
    roleId: null,
    allPokerSpriteFrames: null,
    myPokers: null,
    curPokers: [],

    initAllPokerSpriteFrames: function() {
        networkGlobalData.allPokerSpriteFrames = new Map();
       
        cc.loader.loadRes("cardback", cc.SpriteFrame, function (err, spriteFrame) {
            networkGlobalData.allPokerSpriteFrames["cardback"] = spriteFrame;
        });

        for (var i = 1; i <= 13; i++) {
            for (var j = 0; j <= 3; j++) {
                var resName = i + "_" + j;
                console.log("loadRes: " + resName);
                cc.loader.loadRes(resName, cc.SpriteFrame, function (err, spriteFrame) {
                    if (err != null) {
                        console.error(err);
                    } else {
                        networkGlobalData.allPokerSpriteFrames[spriteFrame.name] = spriteFrame;
                    }
                });
            }
        }
    },

    state: function(map) {
        var cur = cc.director.getScene();
        if (typeof cur != "mainView") {
            cc.director.loadScene("mainView", function() {
                networkGlobalData.drawGame(map);
            });
        } else {
            networkGlobalData.drawGame(map);
        }

        

        // var respMessage = document.getElementById("respMessage");
        // respMessage.value = respMessage.value + "\nstate: " + JSON.stringify(map);
    },

    drawGame: function(map) {
        networkGlobalData.curPokers = [];
        var curActionRoleId = map["curActionRoleId"];

        var gameOver = map["gameOverMsg"].length > 0;
        if (!gameOver) {
            // timer
            var delay = 0;
            var interval = 1;
            var repeat = map["leftSecond"];
            var count = repeat;
            var myZone = cc.find("Game/pokerTimer");
            var myZoneComponent = myZone.getComponent(cc.Label)
            myZoneComponent.schedule(function() {
                var str = count--;
                if (map["prevRoleId"] = curActionRoleId) {
                    str = "←" + str;
                } else if (map["nextRoleId"] == curActionRoleId) {
                    str += "→"
                }
                myZoneComponent.string = str;
            }, interval, repeat, delay);

            // button
            var actionButton = cc.find("Game/action");
            var cancelButton = cc.find("Game/cancel");
            if (curActionRoleId == networkGlobalData.roleId) {
                actionButton.active = true
                cancelButton.active = true
            } else {
                actionButton.active = false
                cancelButton.active = false
            }


        }

        // my pokers
        networkGlobalData.myPokers = map["myPokers"];
        var myZone = cc.find("Game/myZone");
        for (var i = 0; i < 18; i++) {
            var sprite = myZone.children[i].getComponent(cc.Sprite);
            if (sprite != null) {
                var myPokers = map["myPokers"];
                if (i < myPokers.length) {
                    var resName = myPokers[i];
                    sprite.spriteFrame = networkGlobalData.allPokerSpriteFrames[resName];
                } else {
                    sprite.spriteFrame = null;
                }
            }
        }

        
        networkGlobalData.actionPokers = map["actionPokers"];
        var actionZone = cc.find("Game/actionPokers");
        var gameOverZone = cc.find("Game/gameOver");
        if (gameOver == false) {
            // action pokers
            for (var i = 0; i < 18; i++) {
                var sprite = actionZone.children[i].getComponent(cc.Sprite);
                if (sprite != null) {
                    var actionPokers = map["actionPokers"];
                    if (i < actionPokers.length) {
                        var resName = actionPokers[i];
                        sprite.spriteFrame = networkGlobalData.allPokerSpriteFrames[resName];
                    } else {
                        sprite.spriteFrame = null;
                    }
                }
            }
            gameOverZone.active = false;
        } else {
            for (var i = 0; i < 18; i++) {
                var sprite = actionZone.children[i].getComponent(cc.Sprite);
                if (sprite != null) {
                    sprite.spriteFrame = null;
                }
            }
            
            gameOverZone.getComponent(cc.Label).string = map["gameOverMsg"];
        }

        // prevInfo
        var prevInfoStr = "角色id: " + map["prevRoleId"] + "\n剩余牌数: " + map["prevPokerNums"];
        var prevInfoLabel = cc.find("Game/prevInfo").getComponent(cc.Label);
        prevInfoLabel.string = prevInfoStr;
       
        // prevPokers
        var prevPokerNums = map["prevPokerNums"];
        var prevPokersZone = cc.find("Game/prev");
        for (var i = 0; i < 18; i++) {
            var sprite = prevPokersZone.children[i].getComponent(cc.Sprite);
            if (i < prevPokerNums) {
                sprite.spriteFrame = networkGlobalData.allPokerSpriteFrames["cardback"];
            } else {
                sprite.spriteFrame = null;
            }
        }

        // nextInfo
        var nextInfoStr = "角色id: " + map["nextRoleId"] + "\n剩余牌数: " + map["nextPokerNums"];
        var nextInfoLabel = cc.find("Game/nextInfo").getComponent(cc.Label);
        nextInfoLabel.string = nextInfoStr;

        // nextPokers
        var nextPokerNums = map["nextPokerNums"];
        var nextPokerZone = cc.find("Game/next");
        for (var i = 0; i < 18; i++) {
            var sprite = nextPokerZone.children[i].getComponent(cc.Sprite);
            if (i < nextPokerNums) {
                sprite.spriteFrame = networkGlobalData.allPokerSpriteFrames["cardback"];
            } else {
                sprite.spriteFrame = null;
            }
        }
    },

    pokerActionSuccess: function(map) {
        networkGlobalData.curPokers = [];
    },

    pokerAction: function(pokers) {
        var map = {"pokers": pokers, "tk": networkGlobalData.tk};
        var msg = JSON.stringify(map);
        var json = "6;" + msg
        this.sendMsg(json);
    },

    joinRoomSuccess: function(map) {
        networkGlobalData.tk = map["tk"];
    },

    joinRoom: function(roomId) {
        var map = {"roomId": roomId};
        var msg = JSON.stringify(map);
        var json = "4;" + msg
        this.sendMsg(json);
    },

    createRoomSuccess: function(map) {
        networkGlobalData.tk = map["tk"]
        var roomId = map["roomId"] 
        
        var roomIdLabel = cc.find("Navigation/roomIdLabel").getComponent(cc.Label);
        roomIdLabel.string = roomId;
    },

    createRoom: function() {
        var map = {"roleId": this.roleId};
        var msg = JSON.stringify(map);
        var json = "2;" + msg
        this.sendMsg(json);
        return true;
    },

    loginSuccess: function(map) {
        this.roleId = map["roleId"];
        cc.director.loadScene('navigation');
        return true;
    },

    networkLogin: function(userName, password) {
        // var json2map=JSON.parse(json);
        // var map2json=JSON.stringify(map);
        var map = {"userName": userName, "password": password};
        var msg = JSON.stringify(map);
        var json = "0;" + msg
        this.sendMsg(json);
        return true
    },

    sendMsg: function(msg) { //发送消息 
        if (this.websocket.readyState == WebSocket.OPEN) { //如果WebSocket是打开状态
            this.websocket.send(msg); //send()发送消息
        }
    },

    createConnection: function() {
        //如果浏览器支持WebSocket
        if (window.WebSocket) {
            this.websocket = new WebSocket("wss://caijiahe.com:9999/ws");  //获得WebSocket对象
            
            //当有消息过来的时候触发
            this.websocket.onmessage = function (event) {
                var group = event.data.split(';');
                var map = JSON.parse(group[1]);
                switch(group[0]) {
                    case "1":
                        networkGlobalData.loginSuccess(map);
                        break;
                    case "3":
                        networkGlobalData.createRoomSuccess(map);
                        break;
                    case "5":
                        networkGlobalData.joinRoomSuccess(map);
                        break;
                    case "7":
                        networkGlobalData.state(map);
                        break;
                    case "8":
                        networkGlobalData.pokerActionSuccess(map);
                }
            }

            //连接关闭的时候触发
            this.websocket.onclose = function (event) {
                // var respMessage = document.getElementById("respMessage");
                // respMessage.value = respMessage.value + "\n断开连接";
            }

            //连接打开的时候触发
            this.websocket.onopen = function (event) {
                // var respMessage = document.getElementById("respMessage");
                // respMessage.value = "建立连接";
            }
            return true;
        } else {
            return false;
        }
    },
}

module.exports = networkGlobalData