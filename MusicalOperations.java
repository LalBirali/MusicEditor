package cs3500.music.model;

import com.sun.org.apache.regexp.internal.RE;

import java.util.List;
import java.util.Map;

/**
 * This is the interface for the music creator model, parametrized over the generic type T. When
 * it is implemented, T can be substituted with an implementation of musical notes. The interface
 * contains declarations for the operations that run into creating music.
 */
public interface MusicalOperations<T, U> {

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
   * Provides functionality to write a musical composition one note at a time. Adds the given
   * IMusicNote at the location denoted by the integer for the starting location of the IMusicNote.
   *
   * @param addedNote the note to write into the musical composition.
   */
  void writeNote(T addedNote);

  /**
   * Removes the note in the musical composition specified by the input IMusicNote.
   *
   * @param note the specific note to remove from the composition.
   */
  void removeNote(T note);

  /**
   * In the musical composition, replaces the first given IMusicNote with the second given
   * IMusicNote.
   *
   * @param currentNote the IMusicNote to swap out.
   * @param newNote     the IMusicNote to swap in.
   */
  void replaceNote(T currentNote, T newNote);

  /**
   * Combines the given IMusicPiece with the existing IMusicPiece, resulting in the two pieces
   * playing in one song.
   *
   * @param toJoin       the IMusicPiece to add to the existing one.
   * @param simultaneous true if the pieces should play simultaneously after combination, false if
   *                     the given piece should play after the existing piece.
   */
  void combinePieces(U toJoin, boolean simultaneous);


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
   * Sets the tempo of this musical composition to the given tempo.
   * @param newTempo the tempo to set this musical composition to.
   */
  void setTempo(int newTempo);

  /**
   * Provides view of the tempo the musical composition plays at.
   * @return the tempo of the musical composition.
   */
  int getTempo();

  /**
   * Provides a view of the length of each measure in the musical composition.
   * @return the measure length in the musical composition.
   */
  int getMeasureLength();

  /**
   * Computes the total duration of the piece associated with this composition.
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

  /**
   * Adds the repeat to this musical piece if it is valid.
   * This means that it's durations cannot overlap with any other repeats, and it's duration
   * cannot exceed the total length of the song.
   * @param repeat is the repeat to be added if valid.
   */
  void addRepeat(Repeat repeat);

}
