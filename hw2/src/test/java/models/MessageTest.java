package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class MessageTest {

  @Test
  @Order(1)
  public void messageTest() {
    // Create a new message
    Message message = new Message(true, 100, "");

    assertEquals(100, message.getCode());
    assertTrue(message.isMoveValidity());
    assertEquals("", message.getMessage());

    // Revise message information through setters.
    message.setCode(200);
    message.setMoveValidity(false);
    message.setMessage("invalid");

    assertEquals(200, message.getCode());
    assertFalse(message.isMoveValidity());
    assertNotEquals("", message.getMessage());

    System.out.println("Test Message");
  }

  @Test
  @Order(2)
  public void toJsonTest() {
    // Create a new message
    Message message = new Message(true, 100, "");

    // message should be converted to json correctly.
    assertEquals("{\"moveValidity\":true,\"code\":100,\"message\":\"\"}", message.toJson());
    System.out.println("Test Message toJson");
  }
}
