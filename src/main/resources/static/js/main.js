'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');


    
// var gameForm = document.getElementById("#gameForm");
var gameForm = document.getElementById("#gameModal");

var stompClient = null;
var username = null;
var identifier=null;
var firstNameInput = document.getElementById("firstName") ;
var lastNameInput = document.getElementById("lastName");
var colorInput = document.getElementById("color");

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
function connect(event) {
    username = document.querySelector('#name').value.trim();
    // identifier = document.querySelector('#').value.trim();
    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN', identificationNumber:identifier})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'could not connect server! please try for refresh this page again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            identificationNumber:identifier
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function commitGame(event) {
    document.getElementById("firstName").required = true;
    document.getElementById("lastName").required = true;
    document.getElementById("color").required = true;

    var firstNameContent = firstNameInput.value.trim();
    var lastNameContent = lastNameInput.value.trim();
    var colorContent = colorInput.value.trim();
    if (firstNameContent && lastNameContent && colorContent && stompClient){
        if (firstNameContent.charAt(0)!=lastNameContent.charAt(0)||colorContent.charAt(0)!=lastNameContent.charAt(0)){
            alert('all fields should start by the same letter, please check and try again');
            return;
        }
        var gameMessage ={
            firstName : firstNameInput.value,
            lastName : lastNameInput.value,
            color : colorInput.value,
            gameUserId : identifier
        };
        stompClient.send("/app/chat.game", {}, JSON.stringify(gameMessage));
        firstNameInput.value='';
        lastNameInput.value='';
        colorInput.value='';
    }
    event.preventDefault();
}



function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    identifier=message.identificationNumber;
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined by this identifier:'+message.identificationNumber;
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}




usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)

gameForm.addEventListener('submit',commitGame,true)
// gameForm.addEventListener('submit', connectToGame , true )

