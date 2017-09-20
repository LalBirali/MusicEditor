package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;


/**
 * Interface for the the visual portion of the view. Contains methods that provide functionality
 * for displaying the score and piano for musical compositions.
 */
public interface IVisualView<T> extends IView<T> {

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
   * adds mouse listeners.
   * @param listener is listener to be added.
   */
  void addMouseListeners(MouseListener listener);

  /**
   * Moves red line on the display.
   * @param direction is the direction to be moved.
   */
  void moveRedLine(String direction);


  /**
   * returns music editor portion.
   * @return editor panel
   */
  IEditorPanel getEditorPanel();

  /**
   * is this view playing.
   * @return is playing.
   */
  boolean isPlaying();

  /**
   * negates playing.
   */
  void setPlaying();

  /**
   * Getter for current beat.
   * @return current beat of the song.
   */
  int getCurBeat();


}
