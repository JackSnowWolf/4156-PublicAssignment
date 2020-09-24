package models;

import com.google.gson.Gson;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Message constructor.
   *
   * @param moveValidity whether move is valid.
   * @param code         response code.
   * @param message      extra error message.
   */
  public Message(boolean moveValidity, int code, String message) {
    this.moveValidity = moveValidity;
    this.code = code;
    this.message = message;
  }

  /**
   * Set move validity.
   *
   * @param moveValidity whether this move is valid.
   */
  public void setMoveValidity(boolean moveValidity) {
    this.moveValidity = moveValidity;
  }

  /**
   * Whether this move is valid.
   *
   * @return whether this move is valid.
   */
  public boolean isMoveValidity() {
    return moveValidity;
  }

  /**
   * Set message code.
   *
   * @param code message code. 100 represents correct.
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   * get message code.
   *
   * @return message code. 100 represents correct.
   */
  public int getCode() {
    return code;
  }

  /**
   * Set message detail.
   *
   * @param message message detailed information.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Get message.
   *
   * @return message detailed information.
   */
  public String getMessage() {
    return message;
  }

  /**
   * get json of Message.
   *
   * @return return game board in json
   */
  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

}
