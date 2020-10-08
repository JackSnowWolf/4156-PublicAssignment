package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class PlayerTest {

  @Test
  @Order(1)
  public void playerTest() {
    // Create a new player.
    Player player = new Player('X', 1);

    assertEquals('X', player.getType());
    assertEquals(1, player.getId());

    System.out.println("Test Player.");
  }

  @Test
  @Order(2)
  public void oppentTest() {
    // Create a new player.
    Player player = new Player('X', 1);
    assertEquals('O', player.oppent());

    System.out.println("Test Opponent.");
  }

  @Test
  @Order(2)
  public void oppentTest2() {
    // Create a new player.
    Player player = new Player('O', 1);
    assertEquals('X', player.oppent());

    System.out.println("Test Opponent.");
  }

  @Test
  @Order(3)
  public void invalidTypeError() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Player('1', 0);
    });
    assertEquals("type should be 'O' or 'X'", exception.getMessage());

    System.out.println("Test invalid Type Error.");

  }
}
