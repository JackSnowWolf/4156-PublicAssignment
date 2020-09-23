package models;

import com.google.gson.Gson;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Message constructor.
   * @param moveValidity whether move is valid.
   * @param code response code.
   * @param message extra error message.
   */
  public Message(boolean moveValidity, int code, String message) {
    this.moveValidity = moveValidity;
    this.code = code;
    this.message = message;
  }

  /**
   * get json of Message.
   * @return return game board in json
   */
  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
