jQuery(function() {
  var tokens         = {x: X, o: O, empty: Empty};
  var game           = new Game(tokens);
  var acceptingInput = true;

  $(".space").click(function() {
    if(!isAcceptingInput()) { return; }

    disableInput();

    var spaceNumber = jQuery(this).attr("-data-number");
    var result      = game.tryToPlaceXToken(spaceNumber);

    if(result instanceof Failure) {
      enableInput();
    }
    else {
      makeMoveRequest({gameOver: endGame, continueGame: enableInput});
    }
  });

  var disableInput     = function() { return acceptingInput = false; };
  var enableInput      = function() { return acceptingInput = true; };
  var isAcceptingInput = function() { return acceptingInput; };

  var makeMoveRequest = function(callbacks) {
    var getRequest = jQuery.getJSON(game.boardQueryString());

    getRequest.success(function(data) {
      console.log(data);
      game = Game.fromArray(tokens, data.newBoard);
      drawBoard(game);
      if(data.winState === "in progress") {
        callbacks.continueGame();
      }
      else {
        callbacks.gameOver(data.winState);
      }
    });
    getRequest.error(function(){
      callbacks.continueGame();
    });
  };

  var endGame = function(message) {
    disableInput();
    var messageDiv  = jQuery("<div id='win-message'>");
    var image       = jQuery("<img src='/public/images/WinFrame.png'>");
    var messageSpan = jQuery("<span>").html(message);
    messageDiv.append(image).append(messageSpan);
    jQuery("#board").prepend(messageDiv);

    var restartGame = function() {
      messageDiv.remove();
      game = new Game(tokens);
      drawBoard(game);
      enableInput();
    };

    setTimeout(restartGame, 2000);
  };

  var drawBoard = function(game) {
    game.eachToken(function(token, index) {
      jQuery(".space[-data-number='" + index.toString() + "']").html(token.toString());
    });
  };
});
