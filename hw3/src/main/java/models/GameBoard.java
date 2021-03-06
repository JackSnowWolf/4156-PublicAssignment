package models;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import utils.Database;


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

    // connect to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    // initialize database.
    jdbc.createPlayerTable(conn, "ASE_I3_PLAYER");
    jdbc.createMoveTable(conn, "ASE_I3_MOVE");
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * Set p1.
   *
   * @param p1 player 1.
   */
  public void setP1(Player p1) {
    this.p1 = p1;

    // save to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    jdbc.addPlayerData(conn, p1);
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * Set p2.
   *
   * @param p2 player 2.
   */
  public void setP2(Player p2) {
    this.p2 = p2;

    // save to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    jdbc.addPlayerData(conn, p2);
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * Get p1.
   *
   * @return player 1.
   */
  public Player getP1() {
    return p1;
  }

  /**
   * Get p2.
   *
   * @return player 2.
   */
  public Player getP2() {
    return p2;
  }

  /**
   * Set gameStarted.
   *
   * @param gameStarted whether game is started.
   */
  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }


  /**
   * if game is started.
   *
   * @return whether game is started.
   */
  public boolean isGameStarted() {
    return gameStarted;
  }

  /**
   * Set turn.
   *
   * @param turn which player's turn.
   */
  public void setTurn(int turn) {
    this.turn = turn;
  }

  /**
   * Get Turn.
   *
   * @return which player's turn.
   */
  public int getTurn() {
    return turn;
  }

  /**
   * Set boardState.
   *
   * @param boardState game board states.
   */
  public void setBoardState(char[][] boardState) {
    this.boardState = boardState.clone();
  }

  /**
   * Get board state.
   *
   * @return game board states.
   */
  public char[][] getBoardState() {
    return boardState.clone();
  }

  /**
   * Set winner.
   *
   * @param winner game winner.
   */
  public void setWinner(int winner) {
    this.winner = winner;
  }

  /**
   * Get winner.
   *
   * @return game winner.
   */
  public int getWinner() {
    return winner;
  }

  /**
   * Set draw.
   *
   * @param draw whether game is draw.
   */
  public void setDraw(boolean draw) {
    isDraw = draw;
  }

  /**
   * if game is draw.
   *
   * @return whether game is draw.
   */
  public boolean isDraw() {
    return isDraw;
  }


  /**
   * p1 start game.
   *
   * @param type "X" or "O" selected by player p1.
   */
  public void startGame(char type) {
    if (isGameStarted()) {
      throw new IllegalArgumentException("Game has already started.");
    }
    p1 = new Player(type, 1);

    // save to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    jdbc.addPlayerData(conn, p1);
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * p2 join game.
   */
  public void joinGame() {
    p2 = new Player(p1.oppent(), 2);
    gameStarted = true;
    turn = 1;

    // save to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    jdbc.addPlayerData(conn, p2);
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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

    if (move.getPlayer() == null) {
      return new Message(false, 400, "Player is not initialized.");
    }
    int playId = move.getPlayer().getId();
    final int x = move.getMoveX();
    final int y = move.getMoveY();

    if (!gameStarted) {
      return new Message(false, 400, "Game is not started, please start game first.");
    }
    if (playId != turn) {
      return new Message(false, 400, String.format("It's not Player %d's turn.", playId));
    }
    if (winner == 1 || winner == 2 || isDraw) {
      return new Message(false, 400, "Game is finished");
    }
    if (x < 0 || x >= 3 || y < 0 || y >= 3) {
      return new Message(false, 400, "Selected position overflow!");
    }
    if (boardState[x][y] != 0) {
      return new Message(false, 400, "Selected position has already been used.");
    }
    boardState[x][y] = move.getPlayer().getType();
    turn = turn == 1 ? 2 : 1;
    checkWinner();

    // save to database.
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    jdbc.addMoveData(conn, move);
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new Message(true, 100, "");
  }

  /**
   * check winner and set winner or isDraw.
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

  /**
   * reload game board from database.
   */
  public void reload() {
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    p1 = jdbc.loadPlayer1(conn);
    p2 = jdbc.loadPlayer2(conn);
    int[][] boardState = jdbc.loadBoardState(conn);

    int p1Steps = 0;
    int p2Steps = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] == 0) {
          this.boardState[i][j] = '\0';
        } else {
          if (boardState[i][j] == 1) {
            p1Steps++;
          } else {
            p2Steps++;
          }
          this.boardState[i][j] = getPlayer(boardState[i][j]).getType();
        }
      }
    }
    // set game started and turn.
    if (p1 != null && p2 != null) {
      setGameStarted(true);
      if (p1Steps == p2Steps) {
        setTurn(1);
      } else {
        setTurn(2);
      }
    }
    // check winner.
    checkWinner();

    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * clean database.
   */
  public void clean() {
    Database jdbc = new Database();
    Connection conn = jdbc.createConnection();
    // clean data.
    jdbc.deleteTable(conn, "ASE_I3_PLAYER");
    jdbc.deleteTable(conn, "ASE_I3_MOVE");

    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}


