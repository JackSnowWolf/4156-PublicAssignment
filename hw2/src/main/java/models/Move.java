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

  /**
   * Set player.
   *
   * @param player game player.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Get player.
   *
   * @return game player.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set moveX.
   *
   * @param moveX move x position.
   */
  public void setMoveX(int moveX) {
    this.moveX = moveX;
  }

  /**
   * Set moveY.
   *
   * @param moveY move y position.
   */
  public void setMoveY(int moveY) {
    this.moveY = moveY;
  }

  /**
   * Get moveX.
   *
   * @return move x position.
   */
  public int getMoveX() {
    return moveX;
  }

  /**
   * Get moveY.
   *
   * @return move y position.
   */
  public int getMoveY() {
    return moveY;
  }
}
