package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import models.Move;
import models.Player;

public class DatabaseJDBC {

  public static void main(String[] args) {
    DatabaseJDBC jdbc = new DatabaseJDBC();

    Connection conn = jdbc.createConnection();
    boolean tableCreated = jdbc.createPlayerTable(conn, "ASE_I3_PLAYER");
    Player player = new Player('O', 1);
    boolean isDataAdded = jdbc.addPlayerData(conn, player);
    assert tableCreated;
    assert isDataAdded;

    tableCreated = jdbc.createMoveTable(conn, "ASE_I3_MOVE");
    isDataAdded = jdbc.addMoveData(conn, new Move(player, 0, 0));
    assert tableCreated;
    assert isDataAdded;

    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * Create new connection.
   *
   * @return Connection object.
   */
  public Connection createConnection() {
    Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:ase.db");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");

    return c;
  }


  /**
   * Create Player table.
   *
   * @param c Connection object.
   * @param tableName player table name.
   * @return Boolean true if create table successfully, otherwise false.
   */
  public boolean createPlayerTable(Connection c, String tableName) {
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (PLAYER_ID INT NOT NULL,"
          + " PLAYER_TYPE CHARACTER(1) NOT NULL, PRIMARY KEY (PLAYER_ID));";
      System.out.println(sql);
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    System.out.println("Player Table Created Successfully");
    return true;
  }

  /**
   * Create Move table.
   *
   * @param c Connection object.
   * @param tableName player table name.
   * @return Boolean true if create table successfully, otherwise false.
   */
  public boolean createMoveTable(Connection c, String tableName) {
    Statement stmt = null;

    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (PLAYER_ID INT NOT NULL, "
          + "MOVE_X INT NOT NULL, " + "MOVE_Y INT NOT NULL, " + "PRIMARY KEY (MOVE_X, MOVE_Y));";
      System.out.println(sql);
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }

    System.out.println("Move Table Create Successfully");
    return true;
  }

  /**
   * Add player data into player table.
   *
   * @param c Connection object.
   * @param player player data.
   * @return Boolean true if add data successfully, otherwise false.
   */
  public boolean addPlayerData(Connection c, Player player) {
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql =
          "INSERT OR REPLACE INTO ASE_I3_PLAYER (PLAYER_ID, PLAYER_TYPE) " + "VALUES ( " + player
              .getId()
              + ", '" + player.getType() + "' );";
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Records created successfully");
    return true;
  }

  /**
   * Add move data into move table.
   *
   * @param c Connection object.
   * @param move move data.
   * @return Boolean true if add data successfully, otherwise false.
   */
  public boolean addMoveData(Connection c, Move move) {
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql =
          "INSERT OR REPLACE INTO ASE_I3_MOVE (PLAYER_ID, MOVE_X, MOVE_Y) " + "VALUES ( " + move
              .getPlayer()
              .getId() + ", " + move.getMoveX() + ", " + move.getMoveY()
              + " );";
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Records created successfully");
    return true;
  }


  /**
   * Clean all data from table.
   *
   * @param c Connection Object.
   * @param tableName table name.
   * @return Boolean true if add data successfully, otherwise false.
   */
  public boolean deleteTable(Connection c, String tableName) {
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "DELETE FROM " + tableName;
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    System.out.println("Records deleted successfully");
    return true;
  }

  /**
   * Load stored board states (in player id).
   *
   * @param c Connection Object.
   * @return board states (in player id).
   */
  public int[][] loadBoardState(Connection c) {
    int[][] boardState = new int[3][3];
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ASE_I3_MOVE";
      ResultSet resultSet = stmt.executeQuery(sql);

      while (resultSet.next()) {
        int playerId = resultSet.getInt("PLAYER_ID");
        int moveX = resultSet.getInt("MOVE_X");
        int moveY = resultSet.getInt("MOVE_Y");
        if (moveX < 0 || moveX >= 3 || moveY < 0 || moveY >= 3) {
          throw new IllegalArgumentException("move position overflow!");
        }
        boardState[moveX][moveY] = playerId;
      }

    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return boardState;
    }
    System.out.println("Records loaded successfully");
    return boardState;
  }

  /**
   * load player 1 info.
   *
   * @param c Connection object.
   * @return Player 1.
   */
  public Player loadPlayer1(Connection c) {
    Statement stmt = null;
    Player player = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ASE_I3_PLAYER WHERE PLAYER_ID=1;";
      ResultSet resultSet = stmt.executeQuery(sql);
      if (!resultSet.next()) {
        System.out.println("Player is not initialized");
        return player;
      }
      char playerType = resultSet.getString("PLAYER_TYPE").charAt(0);
      player = new Player(playerType, 1);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return player;
    }
    System.out.println("Player loaded successfully");
    return player;
  }

  /**
   * load player 1 info.
   *
   * @param c Connection object.
   * @return Player 2.
   */
  public Player loadPlayer2(Connection c) {
    Statement stmt = null;
    Player player = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      String sql = "SELECT * FROM ASE_I3_PLAYER WHERE PLAYER_ID=2;";
      ResultSet resultSet = stmt.executeQuery(sql);
      if (!resultSet.next()) {
        System.out.println("Player is not initialized");
        return player;
      }
      char playerType = resultSet.getString("PLAYER_TYPE").charAt(0);
      player = new Player(playerType, 2);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return player;
    }
    System.out.println("Player loaded successfully");
    return player;
  }
}


