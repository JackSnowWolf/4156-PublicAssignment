package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;


  /**
   * Move constructor.
   *
   * @param player player type
   * @param moveX  move x position
   * @param moveY  move y position
   */
  public Move(Player player, int moveX, int moveY) {
    this.player = player;
    this.moveX = moveX;
    this.moveY = moveY;
  }

  public Player getPlayer() {
    return player;
  }

  public int getMoveX() {
    return moveX;
  }

  public int getMoveY() {
    return moveY;
  }
}
