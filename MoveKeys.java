package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Keyboard handler class. Modeled from lecture notes code. Handles key presses to update model
 * and view accordingly.
 */
public class MoveKeys implements KeyListener {
  private Map<Integer, Runnable> keyPresses;
  private Map<Integer, Runnable> keyReleases;

  /**
   * Constructs a new MoveKeys object with maps of key presses and key releases.
   */
  public MoveKeys() {
    keyPresses = new HashMap<>();
    keyReleases = new HashMap<>();
  }

  /**
   * Sets the appropriate actions for important key presses.
   *
   * @param keyPresses is the map of runnable events based on where key is pressed
   */
  public void setKeyPresses(Map<Integer, Runnable> keyPresses) {
    this.keyPresses = keyPresses;
  }

  /**
   * Sets the appropriate actions for important key released.
   *
   * @param keyReleases is the map of runnable events based on where key is released
   */
  public void setKeyReleases(Map<Integer, Runnable> keyReleases) {
    this.keyReleases = keyReleases;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    //not concerned with typing events
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (keyPresses.containsKey(e.getKeyCode())) {
      keyPresses.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (keyReleases.containsKey(e.getKeyCode())) {
      keyReleases.get(e.getKeyCode()).run();
    }
  }

  public Map<Integer, Runnable> getKeyPresses() {
    return keyPresses;
  }

  public Map<Integer, Runnable> getKeyReleases() {
    return keyReleases;
  }

}
