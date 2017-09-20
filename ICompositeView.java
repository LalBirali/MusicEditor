package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Interface for the Composite View. Contains functionality for running and controlling the
 * visual and audio elements of the View simultaneously.
 */
public interface ICompositeView<T> extends IView<T> {

  /**
   * Highlights the active notes on the piano view as they are played.
   */
  void highlightActive();

  /**
   * Adds key listener to this view.
   * @param listener is listener to be added.
   */
  void addKeyListeners(KeyListener listener);

  /**
   * Moves red line on the display.
   * @param direction is the direction to be moved.
   */
  void moveRedLine(String direction);

  /**
   * adds mouse listeners.
   * @param listener is the listener to be added.
   */
  void addMouseListeners(MouseListener listener);


  /**
   * returns music editor portion.
   * @return editor panel
   */
  IEditorPanel getEditorPanel();


  boolean isPlaying();


}
