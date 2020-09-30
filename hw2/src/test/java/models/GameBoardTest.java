package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class GameBoardTest {

  private GameBoard gameBoard;

  /**
   * This method starts a game board before every test run. It will run every time before a test.
   */
  @BeforeEach
  public void init() {
    gameBoard = new GameBoard();
    System.out.println("Before Each");
  }


  @Nested
  @DisplayName("Before player 1 start game tests")
  class BeforeStartGameTests {

    @Test
    @Order(1)
    public void newGameTest() {

      // check gameBoard status after initialized.
      assertEquals(0, gameBoard.getTurn());
      assertFalse(gameBoard.isGameStarted());
      assertFalse(gameBoard.isDraw());
      assertEquals(0, gameBoard.getWinner());
      assertNotEquals("", gameBoard.toJson());

      System.out.println("Test new game status.");
    }

    @Test
    @Order(1)
    public void player1MoveErrorTest() {
      assertEquals(0, gameBoard.getTurn());
      assertFalse(gameBoard.isGameStarted());
      // player 1 can move before game started.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      assertFalse(message.isMoveValidity());
      assertNotEquals(100, message.getCode());
      assertNotEquals("", message.getMessage());

      System.out.println("Test Player cannot move before game started.");
    }

    @Test
    @Order(2)
    public void player2MoveErrorTest() {
      assertEquals(0, gameBoard.getTurn());
      assertFalse(gameBoard.isGameStarted());
      // player 1 can move before game started.
      Message message = gameBoard.move(new Move(gameBoard.getP2(), 0, 0));
      assertFalse(message.isMoveValidity());
      assertNotEquals(100, message.getCode());
      assertNotEquals("", message.getMessage());

      System.out.println("Test Player cannot move before game started.");
    }


  }

  @Nested
  @DisplayName("Before game starts tests")
  class BeforeJoinGameTests {

    /**
     * This method starts a game board before every test run. It will run every time before a test.
     */
    @BeforeEach
    public void init() {
      gameBoard.startGame('X');
      System.out.println("Before Each for each before game starts test.");
    }

    @Test
    @Order(1)
    public void player1MoveErrorTest() {
      assertEquals(0, gameBoard.getTurn());
      assertFalse(gameBoard.isGameStarted());
      // player 1 can move before game started.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      assertFalse(message.isMoveValidity());
      assertNotEquals(100, message.getCode());
      assertNotEquals("", message.getMessage());

      System.out.println("Test Player cannot move before game started.");
    }

    @Test
    @Order(2)
    public void player2MoveErrorTest() {
      assertEquals(0, gameBoard.getTurn());
      assertFalse(gameBoard.isGameStarted());
      // player 1 can move before game started.
      Message message = gameBoard.move(new Move(gameBoard.getP2(), 0, 0));
      assertFalse(message.isMoveValidity());
      assertNotEquals(100, message.getCode());
      assertNotEquals("", message.getMessage());

      System.out.println("Test Player cannot move before game started.");
    }


  }

  @Nested
  @DisplayName("After game started tests")
  class AfterGameStartedTests {


    /**
     * This method starts a game board before every test run. It will run every time before a test.
     */
    @BeforeEach
    public void init() {
      gameBoard.startGame('X');
      gameBoard.joinGame();
      System.out.println("Before Each for each after game starts test.");
    }

    @Test
    @Order(1)
    public void afterGameStartedTest() {
      // gameStarted should be true if two player join game.
      assertTrue(gameBoard.isGameStarted());
      System.out.println("Game should be started");

      // the first turn should be player 1.
      assertEquals(1, gameBoard.getTurn());
      System.out.println("Test after game started.");
    }

    @Test
    @Order(2)
    public void moveTest() {
      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, 0));

      // move should be valid
      assertEquals(100, message.getCode());
      assertTrue(message.isMoveValidity());

      System.out.println("Test move.");
    }

    @Test
    @Order(3)
    public void player1Win1Test() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 0, 1));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 2));
      gameBoard.move(new Move(gameBoard.getP1(), 0, 2));

      // Player 1 should win this game.
      assertEquals(1, gameBoard.getWinner());
      assertFalse(gameBoard.isDraw());
      System.out.println("Test Player can win");
    }


    @Test
    @Order(3)
    public void player1Win2Test() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 1, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 2));
      gameBoard.move(new Move(gameBoard.getP1(), 2, 0));

      // Player 1 should win this game.
      assertEquals(1, gameBoard.getWinner());
      assertFalse(gameBoard.isDraw());
      System.out.println("Test Player can win");
    }

    @Test
    @Order(3)
    public void player1Win3Test() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 2));
      gameBoard.move(new Move(gameBoard.getP1(), 2, 2));

      // Player 1 should win this game.
      assertEquals(1, gameBoard.getWinner());
      assertFalse(gameBoard.isDraw());
      System.out.println("Test Player can win");
    }

    @Test
    @Order(3)
    public void player1Win4Test() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 2, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 0));
      gameBoard.move(new Move(gameBoard.getP1(), 0, 2));

      // Player 1 should win this game.
      assertEquals(1, gameBoard.getWinner());
      assertFalse(gameBoard.isDraw());
      System.out.println("Test Player can win");
    }

    @Test
    @Order(4)
    public void player2WinTest() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 2, 1));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 2));
      gameBoard.move(new Move(gameBoard.getP1(), 0, 2));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 0));
      // Player 2 should win this game.
      assertEquals(2, gameBoard.getWinner());
      assertFalse(gameBoard.isDraw());
      System.out.println("Test Player can win");
    }

    @Test
    @Order(5)
    public void drawTest() {
      // Player make moves in turns.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 1));
      gameBoard.move(new Move(gameBoard.getP1(), 0, 2));
      gameBoard.move(new Move(gameBoard.getP2(), 1, 0));
      gameBoard.move(new Move(gameBoard.getP1(), 1, 1));
      gameBoard.move(new Move(gameBoard.getP2(), 2, 0));
      gameBoard.move(new Move(gameBoard.getP1(), 1, 2));
      gameBoard.move(new Move(gameBoard.getP2(), 2, 2));
      gameBoard.move(new Move(gameBoard.getP1(), 2, 1));
      // Game is draw.
      assertEquals(0, gameBoard.getWinner());
      assertTrue(gameBoard.isDraw());
      System.out.println("Test Game is draw.");
    }


    @Test
    @Order(6)
    public void player2MoveFirstErrorTest() {
      // Player 2 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP2(), 0, 0));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test player 2 move first error.");
    }

    @Test
    @Order(7)
    public void player1MoveTwiceErrorTest() {
      // Player 1 makes a move twice.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, 1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test player 1 move twice error.");
    }


    @Test
    @Order(8)
    public void player2MoveTwiceErrorTest() {
      // Player 2 makes a move twice.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      gameBoard.move(new Move(gameBoard.getP2(), 0, 1));
      Message message = gameBoard.move(new Move(gameBoard.getP2(), 2, 1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test player 1 move twice error.");
    }

    @Test
    @Order(9)
    public void moveAtUsedPositionErrorTest() {
      // Player 1 makes a move.
      gameBoard.move(new Move(gameBoard.getP1(), 0, 0));
      Message message = gameBoard.move(new Move(gameBoard.getP2(), 0, 0));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move at used position error error.");
    }

    @Test
    @Order(9)
    public void movePositionOverFlowError1Test() {
      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, -1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move position overflow error.");
    }

    @Test
    @Order(9)
    public void movePositionOverFlowError2Test() {
      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), -1, 1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move position overflow error.");
    }

    @Test
    @Order(9)
    public void movePositionOverFlowError3Test() {
      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, 3));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move position overflow error.");
    }

    @Test
    @Order(9)
    public void movePositionOverFlowError4Test() {
      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 3, 0));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move position overflow error.");
    }

    @Test
    @Order(10)
    public void moveAfterFinishedError1Test() {
      // Set game winner.
      gameBoard.setWinner(1);

      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, -1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move after finished error 1.");
    }

    @Test
    @Order(11)
    public void moveAfterFinishedError2Test() {
      // Set game is draw.
      gameBoard.setDraw(true);

      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, -1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move after finished error 2.");
    }


    @Test
    @Order(11)
    public void moveAfterFinishedError3Test() {
      // Set game winner.
      gameBoard.setWinner(2);

      // Player 1 makes a move.
      Message message = gameBoard.move(new Move(gameBoard.getP1(), 0, -1));

      // move should not be valid
      assertNotEquals(100, message.getCode());
      assertFalse(message.isMoveValidity());
      assertNotEquals("", message.getMessage());

      System.out.println("Test move after finished error 3.");
    }

    /**
     * This test case is used to check get player function.
     */
    @Test
    @Order(12)
    public void getPlayerTest() {
      // Get player 1
      Player player1 = gameBoard.getPlayer(1);
      assertEquals(1, player1.getId());
      assertEquals('X', player1.getType());

      System.out.println("Test get player.");
    }

    /**
     * This test case is used to check get player function.
     */
    @Test
    @Order(13)
    public void getPlayerTest2() {
      // Get player 2
      Player player2 = gameBoard.getPlayer(2);
      assertEquals(2, player2.getId());
      assertEquals('O', player2.getType());

      System.out.println("Test get player.");
    }

    /**
     * This test case is used to check get player function.
     */
    @Test
    @Order(14)
    public void getPlayerErrorTest() {
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          gameBoard.getPlayer(3)
      );

      assertEquals("Player Id doesn't exist.", exception.getMessage());

      System.out.println("Test get player error.");
    }


    /**
     * This test case is used to check get player Id function.
     */
    @Test
    @Order(15)
    public void getPlayerIdTest() {
      // Get player 1
      int playerId = gameBoard.getPlayerId('X');
      assertEquals(1, playerId);

      System.out.println("Test get player Id.");
    }

    /**
     * This test case is used to check get player Id function.
     */
    @Test
    @Order(16)
    public void getPlayerIdTest2() {
      // Get player 1
      int playerId = gameBoard.getPlayerId('O');
      assertEquals(2, playerId);

      System.out.println("Test get player Id.");
    }


    /**
     * This test case is used to check get player Id function.
     */
    @Test
    @Order(17)
    public void getPlayerIdErrorTest() {
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          gameBoard.getPlayerId('x')
      );

      assertEquals(String.format("Invalid player type '%c'!", 'x'), exception.getMessage());

      System.out.println("Test get player Id error.");
    }
  }
}
