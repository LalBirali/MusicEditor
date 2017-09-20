package cs3500.music.model;

import java.util.List;
import java.util.Map;

/**
 * Interface for a Read-Only Model. Contains all the functionality of a model, excluding anything
 * that mutates the state of the model.
 */
public interface IReadOnlyModel<T, U> {

  /**
   * Provides a copy of the piece associated with the musical composition.
   *
   * @return a copy to the music piece associated with the musical composition.
   */
  U getPiece();

  /**
   * Provides a copy of the piece associated with the musical composition, but only the portion
   * of the piece that plays on or after the given starting beat.
   * @param start the starting beat after which to retrieve the piece
   * @return a sub-view of a copy of the piece associated with the musical composition.
   */
  U getPieceAfter(int start);

  /**
   * Provides a view of the piece associated with the musical composition, with its values
   * organized by time.
   *
   * @return a reference to the music piece associated with the musical composition, organized by
   *         time.
   */
  Map<Integer, List<T>> getPieceByTime();

  /**
   * Computes the note with the lowest sound in the given piece.
   *
   * @param opus the IMusicPiece from which to determine the lowest note.
   * @return the note with the lowest sound.
   */
  T getLowestNote(U opus);

  /**
   * Computes the note with the highest sound in the given piece.
   *
   * @param opus the IMusicPiece from which to determine the lowest note.
   * @return the note with the highest sound.
   */
  T getHighestNote(U opus);

  /**
   * Computes a String representation of the current state of the musical composition, serving as
   * a console visualization.
   *
   * @return a console visualization of current state of musical creator model.
   */
  String getMusicState();

  /**
   * Provides view of the tempo the musical composition plays at.
   * @return the tempo of the musical composition.
   */
  int getTempo();

  /**
   * Computes the total duration of the composition.
   * @return the total duration of the composition.
   */
  int getTotalDuration();

  /**
   * Computes the total range of sounds played by the composition.
   * @return a numeric representation of the range of sounds.
   */
  int totalRange();

  /**
   * Getter for repeats.
   * @return the list of repeats this song represents.
   */
  List<Repeat> getRepeats();

}
