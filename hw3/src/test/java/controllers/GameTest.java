package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Message;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class)
public class GameTest {


  /**
   * Runs only once before the testing starts.
   */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);

    System.out.println("Before All");
  }

  /**
   * This method test server health before every test run. It will run every time before a test.
   */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    HttpResponse response = Unirest.get("http://localhost:8080/").asString();
    assertTrue(response.isSuccess());

    System.out.println("Before Each");
  }


  /**
   * This is a test case to evaluate the newgame endpoint.
   */
  @Test
  @Order(1)
  public void newGameTest() {

    // Create HTTP request and get response
    HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);

    // database should be cleaned.
    PlayGame.setGameBoard(null);

    Unirest.get("http://localhost:8080/tictactoe.html").asString();

    char[][] boardState = PlayGame.getGameBoard().getBoardState();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        assertEquals('\0', boardState[i][j]);
      }
    }
    assertNull(PlayGame.getGameBoard().getP1());
    assertNull(PlayGame.getGameBoard().getP2());

    System.out.println("Test New Game");
  }

  /**
   * This is a test case to guarantee PORT Number should be 8080.
   */
  @Test
  @Order(2)
  public void getPortNumberTest() {

    // Port number should be 8080
    assertEquals(8080, PlayGame.getPortNumber());

    System.out.println("Test Get Port Number");
  }

  /**
   * This is a test case to guarantee "/hello" url works correctly.
   */
  @Test
  @Order(3)
  public void helloTest() {
    // Create HTTP request and get response
    HttpResponse response = Unirest.get("http://localhost:8080/hello").asString();
    int restStatus = response.getStatus();
    String responseBody = (String) response.getBody();

    // Check hello url status
    assertEquals(restStatus, 200);
    assertEquals("Hello World!", responseBody);
    System.out.println("Test hello route");
  }

  /**
   * This is a test case to guarantee "/echo" url works correctly.
   */
  @Test
  @Order(4)
  public void echoTest() {
    // Create HTTP request and get response
    HttpResponse response = Unirest.post("http://localhost:8080/echo").asString();
    int restStatus = response.getStatus();

    // Check echo url status
    assertEquals(restStatus, 200);
    System.out.println("Test echo route");
  }

  /**
   * This is a test case to guarantee "/tictactoe.html" url works correctly.
   */
  @Test
  @Order(5)
  public void basicHtmlTest() {
    // Create HTTP request and get response
    HttpResponse response = Unirest.get("http://localhost:8080/tictactoe.html").asString();
    int restStatus = response.getStatus();

    // Check response status
    assertEquals(restStatus, 200);
    System.out.println("Test tic-tac-toe html.");
  }

  /**
   * This is a test case to guarantee "/tictactoe.html" url works correctly.
   */
  @Test
  @Order(5)
  public void basicHtmlPostErrorTest() {
    // Create HTTP request and get response
    HttpResponse response = Unirest.post("http://localhost:8080/tictactoe.html").asString();
    int restStatus = response.getStatus();

    // Check response status
    assertNotEquals(restStatus, 200);
    System.out.println("Test tic-tac-toe html.");
  }


  @Nested
  @TestMethodOrder(OrderAnnotation.class)
  @DisplayName("Before New Game Tests")
  class BeforeNewGameTests {

    @BeforeEach
    @Test
    @Order(1)
    public void beforeNewGameTest() {
      // Check game board before initialized.
      Unirest.get("http://localhost:8080/newgame").asString();
      PlayGame.setGameBoard(null);
      assertNull(PlayGame.getGameBoard());

      System.out.println("Test Before new game.");
    }

    /**
     * This is a test case to evaluate the startgame endpoint.
     */
    @Test
    @Order(2)
    public void startGameTest() {

      // Create a POST request to make a move and get body.
      HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X")
          .asString();
      // Game is not initialized. But the endpoint will reload game from database.
      assertEquals(200, response.getStatus());
      String responseBody = (String) response.getBody();
      assertTrue(response.isSuccess());
      assertNotEquals("", responseBody);

      System.out.println("Test Start Game ");
    }

    /**
     * This is a test case to evaluate the startgame endpoint for reloading db.
     */
    @Test
    @Order(2)
    public void startGameErrorTest() {

      // save to database.
      Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
      Unirest.get("http://localhost:8080/joingame").asString();
      // game crashes.
      PlayGame.setGameBoard(null);

      // Create a POST request to make a move and get body.
      HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X")
          .asString();
      // Game is not initialized. But the endpoint will reload game from database.
      assertEquals(400, response.getStatus());
      String responseBody = (String) response.getBody();
      assertFalse(response.isSuccess());
      assertEquals("BadRequestResponse", responseBody);

      System.out.println("Test Start Game Error When reloading");
    }

    /**
     * This is a test case to evaluate the joingame endpoint.
     */
    @Test
    @Order(3)
    public void joinGameErrorTest() {
      // Create a POST request to startgame endpoint and get the body
      HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
      // Game is not initialized.
      assertEquals(400, response.getStatus());
      String responseBody = (String) response.getBody();
      assertFalse(response.isSuccess());
      assertEquals("BadRequestResponse", responseBody);

      System.out.println("Test join game error.");
    }

    /**
     * This is a test case for two player move error.
     */
    @Test
    @Order(4)
    public void moveGameStartErrorTest() {

      // Create a GET request for player 1 to make a move.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      // Game is not initialized.
      assertEquals(200, response.getStatus());
      String responseBody = (String) response.getBody();
      assertTrue(response.isSuccess());

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));

      // Create a GET request for player 2 to make a move.
      response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0")
          .asString();
      // Game is not initialized.
      assertEquals(200, response.getStatus());
      responseBody = (String) response.getBody();
      assertTrue(response.isSuccess());

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      System.out.println("Test Move Before Join Error.");
    }


  }

  @Nested
  @TestMethodOrder(OrderAnnotation.class)
  @DisplayName("Start Game Tests")
  class StartGameTests {

    /**
     * This method starts a new game before every test run. It will run every time before a test.
     */
    @BeforeEach
    public void newGameTest() {

      // Create HTTP request and get response
      HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
      int restStatus = response.getStatus();

      // Check assert statement (New Game has started)
      assertEquals(restStatus, 200);
      System.out.println("Test New Game");
    }

    /**
     * This is a test case for two player cannot make move before two player join.
     */
    @Test
    @Order(1)
    public void moveBeforeJoinErrorTest() {

      // Create a POST request to startgame endpoint and get the body
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      Gson gson = new Gson();
      Message message = gson.fromJson(jsonObject.toString(), Message.class);

      // Check if player type is correct
      assertNotEquals("", message);

      // Create a POST request to startgame endpoint and get the body
      response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0")
          .asString();
      responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      message = gson.fromJson(jsonObject.toString(), Message.class);

      // Check if player type is correct
      assertNotEquals("", message);

      System.out.println("Test Move Before Join Error.");
    }

    /**
     * This is a test case to evaluate the startgame endpoint.
     */
    @Test
    @Order(2)
    public void startGameTest() {

      // Create a POST request to make a move and get body.
      HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Start Game Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("gameStarted"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      Gson gson = new Gson();
      GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
      Player player1 = gameBoard.getP1();

      // Check if player type is correct
      assertEquals('X', player1.getType());

      System.out.println("Test Start Game");
    }

    /**
     * This is a test case to evaluate the startgame endpoint.
     */
    @Test
    @Order(2)
    public void startGameTest2() {

      // Create a POST request to make a move and get body.
      HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=O")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Start Game Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("gameStarted"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      Gson gson = new Gson();
      GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
      Player player1 = gameBoard.getP1();

      // Check if player type is correct
      assertEquals('O', player1.getType());

      System.out.println("Test Start Game");
    }

    @Test
    @Order(3)
    public void startGameInvalidTypeErrorTest() {
      // Create a POST request to make a move and get body.
      HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=x")
          .asString();
      String responseBody = (String) response.getBody();
      System.out.println("Start Game Invalid Type Error: " + responseBody);
      // Invalid Type Error
      assertEquals(400, response.getStatus());
      assertFalse(response.isSuccess());
      assertEquals("BadRequestResponse", responseBody);

      System.out.println("Test Start Game Invalid Type Error.");
    }

    @Test
    @Order(4)
    public void restoreAfterNewGame() {
      // game crashes.
      PlayGame.setGameBoard(null);
      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      // restore after new game.
      assertNull(PlayGame.getGameBoard().getP1());
      assertNull(PlayGame.getGameBoard().getP2());
      assertFalse(PlayGame.getGameBoard().isGameStarted());
      assertEquals(0, PlayGame.getGameBoard().getTurn());
      assertFalse(PlayGame.getGameBoard().isDraw());
      assertEquals(0, PlayGame.getGameBoard().getWinner());
      System.out.println("Test Restore after new game.");
    }
  }

  @Nested
  @TestMethodOrder(OrderAnnotation.class)
  @DisplayName("Join Game Test")
  class JoinGameTests {

    /**
     * This method starts a new game before every test run. It will run every time before a test.
     */
    @BeforeEach
    public void init() {
      // Start new game
      Unirest.get("http://localhost:8080/newgame").asString();
      System.out.println("New Game.");
      // Create HTTP request and get response
      Unirest.post("http://localhost:8080/startgame").body("type=X")
          .asString();
      System.out.println("Start Game");
      System.out.println("Before All For Join Game Test.");
    }


    /**
     * This is a test case for two player cannot make move before two player join.
     */
    @Test
    @Order(1)
    public void moveBeforeJoinErrorTest() {

      // Create a GET request for player 1 to make a move.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      Gson gson = new Gson();
      Message message = gson.fromJson(jsonObject.toString(), Message.class);

      // Check if player type is correct
      assertNotEquals("", message);

      // Create a GET request for player 2 to make a move.
      response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0")
          .asString();
      responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));

      // ---------------------------- GSON Parsing -------------------------

      // GSON use to parse data to object
      gson = new Gson();
      message = gson.fromJson(jsonObject.toString(), Message.class);

      // Check if player type is correct
      assertNotEquals("", message);

      System.out.println("Test Move Before Join Error.");
    }

    @Test
    @Order(2)
    public void joinGameTest() {
      // Create a GET request to startgame endpoint and get the body
      Unirest.get("http://localhost:8080/joingame").asString();

      // Player 1 can make a move after player 2 joined.
      // Create a GET request for player 1 to make a move.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(true, jsonObject.get("moveValidity"));
      assertEquals(100, jsonObject.get("code"));

      assertTrue(PlayGame.getGameBoard().isGameStarted());
      System.out.println("Test Player 2 Join game successfully.");
    }

    @Test
    @Order(3)
    void restoreAfterStartGame() {

      // game crashes.
      PlayGame.setGameBoard(null);
      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      // player 1 should be restored.
      assertNotNull(PlayGame.getGameBoard().getP1());
      assertFalse(PlayGame.getGameBoard().isGameStarted());
      // Create a GET request to startgame endpoint and get the body
      Unirest.get("http://localhost:8080/joingame").asString();

      // Player 1 can make a move after player 2 joined.
      // Create a GET request for player 1 to make a move.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(true, jsonObject.get("moveValidity"));
      assertEquals(100, jsonObject.get("code"));

      assertTrue(PlayGame.getGameBoard().isGameStarted());
      System.out.println("Test Restore after player 1 started game.");
    }
  }

  @Nested
  @TestMethodOrder(OrderAnnotation.class)
  @DisplayName("After Game Started Tests")
  class AfterGameStartedTests {

    /**
     * This method starts a new game before every test run. It will run every time before a test.
     */
    @BeforeEach
    public void init() {
      // Start new game
      Unirest.get("http://localhost:8080/newgame").asString();
      System.out.println("New Game.");
      // Create HTTP request and get response
      Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
      // Player 2 join game.
      Unirest.get("http://localhost:8080/joingame").asString();

      System.out.println("Game Started");
      System.out.println("Before Each For After Game Started Test.");
    }

    @Test
    @Order(1)
    public void player2MoveFirstErrorTest() {

      // Create a GET request for player 2 to make a move.
      HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test Player 2 Move First Error.");
    }

    @Test
    @Order(2)
    public void player1MoveTwiceErrorTest() {
      // Create a GET request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      // Player 1 cannot make two moves in his turn.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0")
          .asString();

      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test Player 1 Move Twice Error.");
    }

    @Test
    @Order(3)
    public void player2MoveTest() {
      // Create a GET request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      // Player 2 make move in his turn.
      HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(true, jsonObject.get("moveValidity"));
      assertEquals(100, jsonObject.get("code"));
      System.out.println("Test Player 2 Move.");

    }

    @Test
    @Order(4)
    public void player2MoveTwiceErrorTest() {
      // Create a GET request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      // Player 2 make move in his turn.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
      // Player 2 moves twice in one turn.
      HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test Player 2 Move Twice Error.");
    }

    @Test
    @Order(4)
    public void moveAtSamePositionError() {
      // Create a GET request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0")
          .asString();
      // Player 2 make move at used position in his turn.
      HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test move at same position error.");
    }

    @Test
    @Order(5)
    public void movePositionOverflowError() {
      // Create a GET request for player 1 to make a move at overflow position.
      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=3&y=0")
          .asString();

      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test move at overflow position error.");
    }

    @Test
    @Order(6)
    public void player1WinTest() {
      // Create a POST request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();

      // player 1 should win.
      assertEquals(1, PlayGame.getGameBoard().getWinner());
    }

    @Test
    @Order(7)
    public void player2WinTest() {
      // Create a POST request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
      // player 2 should win.
      assertEquals(2, PlayGame.getGameBoard().getWinner());
    }

    @Test
    @Order(8)
    public void gameIsDrawTest() {
      // Create a GET request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();

      // Game should be draw.
      assertTrue(PlayGame.getGameBoard().isDraw());
    }

    @Test
    @Order(9)
    public void moveAfterFinishedError() {
      // Create a GET request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();

      // Player 2 make move after game finished.
      HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(false, jsonObject.get("moveValidity"));
      assertNotEquals(100, jsonObject.get("code"));
      // Check error message.
      assertNotEquals("", jsonObject.get("message"));
      System.out.println("Test move after game finished error.");
    }

    @Test
    @Order(10)
    public void moveRestoreTest() {
      // Create a POST request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 2 make move in his turn.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
      // game crashes.
      PlayGame.setGameBoard(null);

      HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1")
          .asString();
      String responseBody = (String) response.getBody();

      // --------------------------- JSONObject Parsing ----------------------------------

      System.out.println("Move Response: " + responseBody);

      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(responseBody);

      // Check if game started after player 1 joins: Game should not start at this point
      assertEquals(true, jsonObject.get("moveValidity"));
      assertEquals(100, jsonObject.get("code"));

      // check board state.
      assertTrue(PlayGame.getGameBoard().isGameStarted());
      assertEquals('X', PlayGame.getGameBoard().getP1().getType());
      assertEquals('O', PlayGame.getGameBoard().getP2().getType());

      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][0]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[1][0]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[1][1]);

      System.out.println("Test Restore Move.");
    }

    @Test
    @Order(11)
    public void restoreAfterDrawTest() {
      // Create a POST request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();

      // game crashes.
      PlayGame.setGameBoard(null);

      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      assertTrue(PlayGame.getGameBoard().isGameStarted());
      assertTrue(PlayGame.getGameBoard().isDraw());

      assertEquals('X', PlayGame.getGameBoard().getP1().getType());
      assertEquals('O', PlayGame.getGameBoard().getP2().getType());

      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][0]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[0][1]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][2]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[1][0]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[1][1]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[2][0]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[1][2]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[2][2]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[2][1]);

      System.out.println("Test Restore after draw.");
    }


    @Test
    @Order(12)
    public void restoreAfterWin() {
      // Create a POST request for player 1 to make a move
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 1 and player 2 make moves one by one.
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
      Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();

      // game crashes.
      PlayGame.setGameBoard(null);

      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      assertTrue(PlayGame.getGameBoard().isGameStarted());
      assertFalse(PlayGame.getGameBoard().isDraw());
      assertEquals(1, PlayGame.getGameBoard().getWinner());

      assertEquals('X', PlayGame.getGameBoard().getP1().getType());
      assertEquals('O', PlayGame.getGameBoard().getP2().getType());

      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][0]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[1][1]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][1]);
      assertEquals('O', PlayGame.getGameBoard().getBoardState()[1][2]);
      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][2]);

      System.out.println("Test Restore after win.");
    }

    @Test
    @Order(13)
    public void restoreAfterGameStarted() {
      // game crashes.
      PlayGame.setGameBoard(null);

      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      // player 1 and player 2 should be restored.
      assertNotNull(PlayGame.getGameBoard().getP1());
      assertNotNull(PlayGame.getGameBoard().getP2());
      assertEquals('X', PlayGame.getGameBoard().getP1().getType());
      assertEquals('O', PlayGame.getGameBoard().getP2().getType());
      assertTrue(PlayGame.getGameBoard().isGameStarted());
      assertEquals(1, PlayGame.getGameBoard().getTurn());
      assertFalse(PlayGame.getGameBoard().isDraw());
      assertEquals(0, PlayGame.getGameBoard().getWinner());

      System.out.println("Test Restore after player 1 and player 2 both joined.");

    }

    @Test
    @Order(13)
    public void restoreAfterInvalidMove() {

      // Create a POST request for player 1 to make a move.
      Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
      // Player 2 make move at used position in his turn.
      Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();

      // game crashes.
      PlayGame.setGameBoard(null);

      Unirest.get("http://localhost:8080/tictactoe.html").asString();

      // player 1 and player 2 should be restored.
      assertNotNull(PlayGame.getGameBoard().getP1());
      assertNotNull(PlayGame.getGameBoard().getP2());
      assertEquals('X', PlayGame.getGameBoard().getP1().getType());
      assertEquals('O', PlayGame.getGameBoard().getP2().getType());
      assertTrue(PlayGame.getGameBoard().isGameStarted());
      assertEquals(2, PlayGame.getGameBoard().getTurn());
      assertFalse(PlayGame.getGameBoard().isDraw());
      assertEquals(0, PlayGame.getGameBoard().getWinner());

      assertEquals('X', PlayGame.getGameBoard().getBoardState()[0][0]);

      System.out.println("Test Restore after invalid move");

    }
  }


  /**
   * This will run every time after a test has finished.
   */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}

