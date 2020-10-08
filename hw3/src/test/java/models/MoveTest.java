package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class MoveTest {

  @Test
  @Order(1)
  public void moveTest() {
    Player player = new Player('X', 1);
    Move move = new Move(player, 0, 2);

    assertEquals(player.getType(), move.getPlayer().getType());
    assertEquals(player.getId(), move.getPlayer().getId());
    assertEquals(0, move.getMoveX());
    assertEquals(2, move.getMoveY());

    System.out.println("Test move constructor.");

  }
}
