package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import models.Move;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class DatabaseTest {

  private static Database jdbc;
  private static Connection conn;
  private static String playerTableName = "ASE_I3_PLAYER";
  private static String moveTableName = "ASE_I3_MOVE";

  /**
   * This method starts before all tests run. init database table formate.
   */
  @Test
  @BeforeAll
  public static void init() {
    jdbc = new Database();
    // get connection to database
    conn = jdbc.createConnection();
    assertNotNull(conn);
    boolean tableCreated = jdbc.createPlayerTable(conn, "ASE_I3_PLAYER");
    assertTrue(tableCreated);

    tableCreated = jdbc.createMoveTable(conn, "ASE_I3_MOVE");
    assertTrue(tableCreated);
    boolean isDeleted = jdbc.deleteTable(conn, playerTableName);
    assertTrue(isDeleted);
    isDeleted = jdbc.deleteTable(conn, moveTableName);
    assertTrue(isDeleted);

    System.out.println("initialize database.");
  }

  @Test
  @Order(1)
  public void addPlayerTest() {
    jdbc.addPlayerData(conn, new Player('X', 1));
    Player player = jdbc.loadPlayer1(conn);
    assertEquals(1, player.getId());
    assertEquals('X', player.getType());

    System.out.println("Test add player");
  }

  @Test
  @Order(2)
  public void addMoveTest() {
    jdbc.addMoveData(conn, new Move(new Player('O', 2), 0, 0));
    int[][] boardState = jdbc.loadBoardState(conn);
    assertEquals(2, boardState[0][0]);

    System.out.println("Test add move");
  }

  @Test
  @Order(3)
  public void loadPlayer1Test() {
    Player player = jdbc.loadPlayer1(conn);
    assertEquals(1, player.getId());
    assertEquals('X', player.getType());

    System.out.println("Test load player1");

    jdbc.addPlayerData(conn, new Player('O', 2));


  }

  @Test
  @Order(3)
  public void loadPlayer2Test() {

    jdbc.addPlayerData(conn, new Player('O', 2));
    Player player = jdbc.loadPlayer2(conn);
    assertEquals(2, player.getId());
    assertEquals('O', player.getType());

    System.out.println("Test load player1");
  }

  @Test
  @Order(4)
  public void loadBoardStateTest() {
    jdbc.addMoveData(conn, new Move(new Player('O', 2), 0, 0));
    jdbc.addMoveData(conn, new Move(new Player('X', 1), 1, 1));
    int[][] boardState = jdbc.loadBoardState(conn);
    assertEquals(2, boardState[0][0]);
    assertEquals(1, boardState[1][1]);
    assertEquals(0, boardState[1][0]);

    System.out.println("Test load board state");
  }

  @Test
  @Order(5)
  public void deleteTable1Test() {
    // delete move table.
    jdbc.deleteTable(conn, moveTableName);

    int[][] boardState = jdbc.loadBoardState(conn);
    assertEquals(0, boardState[0][0]);
    assertEquals(0, boardState[1][1]);
    assertEquals(0, boardState[1][0]);

    System.out.println("Test delete table.");
  }


  @Test
  @Order(5)
  public void deleteTable2Test() {
    // delete player table.
    jdbc.deleteTable(conn, playerTableName);

    Player player = null;
    player = jdbc.loadPlayer1(conn);
    assertNull(player);
    player = jdbc.loadPlayer2(conn);
    assertNull(player);

    System.out.println("Test delete table.");
  }

  /**
   * This method starts after all tests run.
   */
  @AfterAll
  public static void stop() {
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
