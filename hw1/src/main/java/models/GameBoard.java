package models;


import com.google.gson.Gson;


public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;

  /**
   * GameBoard Constructor.
   */
  public GameBoard() {
    p1 = null;
    p2 = null;
    gameStarted = false;
    turn = 0;
    isDraw = false;
    boardState = new char[3][3];
  }

  /**
   * p1 start game.
   *
   * @param type "X" or "O" selected by player p1.
   */
  public void startGame(char type) {
    p1 = new Player(type, 1);
    turn = 1;
  }

  /**
   * p2 join game.
   */
  public void joinGame() {
    p2 = new Player(p1.oppent(), 2);
    gameStarted = true;
  }

  /**
   * get json of game board.
   *
   * @return return game board in json
   */
  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }


  /**
   * getPlayer.
   *
   * @param id player id.
   * @return corresponding player.
   */
  public Player getPlayer(int id) {
    switch (id) {
      case 1:
        return p1;
      case 2:
        return p2;
      default:
        throw new IllegalArgumentException("Player Id doesn't exist.");
    }
  }

  /**
   * get player id.
   *
   * @param type player type.
   * @return corresponding player id.
   */
  public int getPlayerId(char type) {
    if (type == p1.getType()) {
      return 1;
    } else if (type == p2.getType()) {
      return 2;
    } else {
      throw new IllegalArgumentException(String.format("Invalid player type '%c'!", type));
    }
  }

  /**
   * player makes a move.
   *
   * @param move player that makes this move.
   */
  public Message move(Move move) {
    int playId = move.getPlayer().getId();
    int x = move.getMoveX();
    int y = move.getMoveY();

    if (!gameStarted) {
      return new Message(false, 400, "Game is not started, please start game first.");
    }
    if (playId != turn) {
      return new Message(false, 400, String.format("It's not Player %d's turn.", playId));
    }
    if (x < 0 || x > 3 || y < 0 || y > 3) {
      return new Message(false, 400, "Selected position overflow!");
    }
    if (boardState[x][y] != 0) {
      return new Message(false, 400, "Selected position has already been used.");
    }
    boardState[x][y] = move.getPlayer().getType();
    turn = turn == 1 ? 2 : 1;
    checkWinner();
    return new Message(false, 100, "");
  }

  /**
   * check winner.
   */
  private void checkWinner() {
    for (int i = 0; i < 3; i++) {
      if (boardState[i][0] == boardState[i][1] && boardState[i][0] == boardState[i][2]) {
        if (boardState[i][0] == 0) {
          continue;
        }
        winner = getPlayerId(boardState[i][0]);
        return;
      }
      if (boardState[0][i] == boardState[1][i] && boardState[0][i] == boardState[2][i]) {
        if (boardState[0][i] == 0) {
          continue;
        }
        winner = getPlayerId(boardState[0][i]);
        return;
      }
    }
    if (boardState[0][0] == boardState[1][1] && boardState[0][0] == boardState[2][2]) {
      if (boardState[0][0] != 0) {
        winner = getPlayerId(boardState[0][0]);
        return;
      }
    }
    if (boardState[0][2] == boardState[1][1] && boardState[0][2] == boardState[2][0]) {
      if (boardState[0][2] != 0) {
        winner = getPlayerId(boardState[0][2]);
        return;
      }
    }

    int filledNum = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] != 0) {
          filledNum++;
        }
      }
    }
    if (filledNum == 9) {
      isDraw = true;
    }
  }

}


