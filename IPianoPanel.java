package cs3500.music.view;

import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import cs3500.music.model.IMusicNote;

/**
 * Interface for a piano panel, contains functionality for displaying aspects of the piano.
 */
public interface IPianoPanel {

  /**
   * Assigns each key a pitch.
   */
  void initializeKeyPitch();

  /**
   * Initializes the colors of the keys on the piano.
   */
  void initializeKeys();

  /**
   * Getter for this content pane.
   * @return the content pane of this panel.
   */
  JPanel getContentPane();

  /**
   * Uses the notes given that are the notes playing at the current beat on the piano,
   * based on the position of the white line.
   * @param noteList list of notes currently playing at the beat
   */
  void highlightPlayingNotes(List<IMusicNote> noteList);

  /**
   * Adds mouse listener to this piano panel keys.
   * @param listener is mouselistener to be added.
   */
  void addMouseListeners(MouseListener listener);



}
