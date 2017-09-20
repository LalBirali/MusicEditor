package cs3500.music.view;

/**
 * Interface for the overall view of the Music player. Contains functionality for generating
 * textual, visual, and audio *views*.
 */
public interface IView<T> {
  /**
   * Retrieves a copy of the musical model loaded in to the Viewer object.
   * @return a copy of the model
   */
  T getModel();

  /**
   * Runs each respective view type. For IConsoleView, this means printing the textual view to
   * the console. For IVisualView, this means projecting the score and keyboard. For IAudioView,
   * this means playing the song audio.
   */
  void run();

  /**
   * Refreshes the data in this IView based on changes to the associated model.
   * @param model the updated model by which to update the view.
   */
  void refresh(T model);

  /**
   * Plays the playback, whether it be audio or visual.
   */
  void play();

  /**
   * Pauses the playback of the View.
   */
  void pause();

  /**
   * Resets the player to set its current point back to the beginning of the sequence.
   */
  void goToStart();

  /**
   * Sets the player tick to the ending point of the song.
   */
  void goToEnd();

  /**
   * Gets the current beat that the song is playing at.
   */
  int getTick();

  /**
   * Sets the current beat to the given beat.
   *
   * @param beat the beat to which to set the tick.
   * @throws IllegalArgumentException if the specified beat is beyond the range of how long the song
   *                                  is.
   */
  void setTick(int beat) throws IllegalArgumentException;
}
