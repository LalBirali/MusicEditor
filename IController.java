package cs3500.music.controller;

import java.awt.event.MouseEvent;

import cs3500.music.view.IVisualView;

/**
 * Controller interface, parametrized over the type of note and type of model to be represented.
 */
public interface IController<T, U> {

  /**
   * Initializes the view and starts the music editor, for user interaction.
   */
  void run();

  /**
   * Adds a note to the composition based on user interaction with the keyboard.
   * @param note is note to be added
   */
  void addNote(T note);

  /**
   * Will direct the composite view to play music, moving the beat to the tempo of the song.
   */
  void playMusic();

  /**
   * Will pause the playing of the music in the composite view.
   */
  void pauseMusic();

  /**
   * Directs the view to display the composition of music starting at the given start beat,
   * moving the red line to the start beat.
   * @param beatNumber is the beat to run to
   */
  void goTo(int beatNumber);

  /**
   * Getter for the model that the controller manipulates.
   * @return a copy of the model used in implementations of this controller
   */
  U getModel();

  /**
   * Configures actions for specific controllers on mouse events.
   * @param e is the given mouse event
   */
  void configureMouseHandler(MouseEvent e);

  /**
   * Getter for view.
   * @return the view
   */
  IVisualView getView();
}
