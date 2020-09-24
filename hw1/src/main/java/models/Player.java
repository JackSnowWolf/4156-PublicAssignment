package models;


public class Player {

  private char type;

  private int id;

  /**
   * Player constructor.
   *
   * @param type 'X' or 'O' selected by player.
   * @param id   player id.
   */
  Player(char type, int id) {
    this.type = type;
    this.id = id;
  }

  /**
   * Set player id.
   *
   * @param id player id.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get player id.
   *
   * @return player id.
   */
  public int getId() {
    return id;
  }

  /**
   * Set player type.
   *
   * @param type 'X' or 'O' selected by player.
   */
  public void setType(char type) {
    this.type = type;
  }

  /**
   * Get player type.
   *
   * @return type 'X' or 'O' selected by player.
   */
  public char getType() {
    return type;
  }

  /**
   * get oppent player type.
   *
   * @return oppent player type in char.
   */
  public char oppent() {
    switch (type) {
      case 'X':
        return 'O';
      case 'O':
        return 'X';
      default:
        throw new IllegalArgumentException("type should be 'O' or 'X'");
    }
  }

}
