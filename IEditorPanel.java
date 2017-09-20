package cs3500.music.view;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JScrollPane;
import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;

/**
 * Interface for the Editor Panel, contains functionality required in computing and displaying
 * the editor panel.
 */
public interface IEditorPanel {

  /**
   * Computes a list of the notes playing at the current beat.
   * @return the notes currently playing.
   */
  List<IMusicNote> playingAtCurBeat();

  /**
   * Getter for current beat of editor display, based on position of the red line.
   * @return current beat of the red line
   */
  int getCurBeat();

  /**
   * Sets the current beat of the editor display to the given beat.
   * @param beat the beat to set the current beat to.
   */
  void setCurBeat(int beat);

  /**
   * Ensure that the placeholder bar is in the view. If the bar is out of the view, move the
   * view so that the placeholder is visible again.
   */
  void redisplayWindowForBar();

  /**
   * Getter for this pane.
   * @return a JScrollPane for this editor panel object.
   */
  JScrollPane getPane();

  /**
   * Implementation of the getPreferredSize() method derived from the JPanel class.
   * @return the preferred size of the Editor Panel display.
   */
  Dimension getPreferredSize();

  /**
   * Moves red line on display over one beat, either left or right.
   * @param direction is the direction to move, either "left" or "right"
   */
  void moveRedLine(String direction);

  /**
   * Assigns the given model as the model to be associated with this Editor Panel.
   */
  void setModel(IReadOnlyModel<IMusicNote, IMusicPiece> newModel);

  /**
   * Scrolls the red cursor-bar, starting at the beat the song is currently at.
   */
  void scroll(IPianoPanel panel);

  /**
   * Stops scrolling, while maintaining the current beat position.
   */
  void haltScroll();
}
