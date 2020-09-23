package models;


public class Player {

  private char type;

  private int id;

  Player(char type, int id) {
    this.type = type;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public char getType() {
    return type;
  }

  /**
   * get oppent player type.
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
