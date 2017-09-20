package cs3500.music.model;

import java.util.List;
import java.util.Map;

/**
 * Interface for music piece objects. Provides methods to directly operate on the list of notes in
 * the musical composition.
 */
public interface IMusicPiece {

  /**
   * Gets the tempo of this piece.
   * @return tempo of the piece
   */
  int getTempo();

  /**
   * Sets the tempo of this piece to the given value.
   * @param newTempo the new tempo of this piece.
   */
  void setTempo(int newTempo);

  /**
   * Provides copy of the list of notes associated with this IMusicPiece.
   *
   * @return the notes in this music piece.
   */
  List<IMusicNote> getNotes();

  /**
   * Provides copy of the list of notes associated with this IMusicPiece that play on or after
   * the given starting beat.
   * @param start the starting beat after which to retrieve notes.
   * @return the notes that start after the given beat.
   */
  List<IMusicNote> getNotesAfter(int start);

  /** Provides a view of the notes associated with this IMusicPiece mapped out to each beat in
   * the piece.
   * @return the notes played at each beat in the piece.
   */
  Map<Integer, List<IMusicNote>> computeMap();

  /**
   * Provides the duration of each duration set in this IMusicPiece
   *
   * @return the measure duration.
   */
  double getMeasureDuration();

  /**
   * Computes the total duration of all the notes in this Opus, in beats.
   *
   * @return the total duration of the notes in this Opus.
   */
  double getTotalDuration();

  /**
   * Adds the given IMusicNote to this Opus.
   *
   * @param note the IMusicNote to be added.
   */
  void addNote(IMusicNote note);

  /**
   * Deletes the given note from the notes in this Opus. Since the rest of the notes need to
   * stay where they are, the 'deleted' note doesn't run anywhere, it simply stays there and
   * becomes a rest.
   *
   * @param note the note to delete.
   * @throws IllegalArgumentException if the specified note is not contained in this Opus's list of
   *                                  notes.
   */
  void delete(IMusicNote note) throws IllegalArgumentException;

  /**
   * Replaces the note at the given index with the given note.
   *
   * @param currentNote the note to swap in.
   * @param newNote     the note to swap out.
   * @throws IllegalArgumentException if the specified note is not contained in this Opus's list of
   *                                  notes.
   */
  void replace(IMusicNote currentNote, IMusicNote newNote) throws IllegalArgumentException;

  /**
   * Joins the given Opus with this Opus such that the two will play simultaneously.
   *
   * @param other the Opus to join with this one.
   * @throws IllegalArgumentException if the given Opus has a different time signature than this
   *                                  one.
   */
  void joinPieceIntegrated(IMusicPiece other) throws IllegalArgumentException;

  /**
   * Joins the given Opus with this Opus such that the given one plays after this one.
   *
   * @param other the Opus to join with this one.
   * @throws IllegalArgumentException if the given Opus has a different time signature than this
   *                                  one.
   */
  void joinPiecePlayAfter(IMusicPiece other) throws IllegalArgumentException;

  /**
   * Computes the note with the lowest sound in this piece.
   *
   * @return the lowest note in the given list.
   */
  IMusicNote lowestNote();

  /**
   * Computes the note with the highest sound in this piece.
   *
   * @return the highest note in the given list.
   */
  IMusicNote highestNote();

  /**
   * Retrieves the note with the given sound that plays during the given beat.
   * @param sound the sound of the note to return
   * @param beat the beat during which the note to return plays
   * @return the note playing the sound during the beat.
   */
  IMusicNote getNoteAt(SoundPair sound, int beat);

  /**
   * Sorts the given list of IMusicNote on the basis of location.
   *
   * @param collection the list to sort.
   * @return a list of IMusicNote, sorted by location.
   */
  List<IMusicNote> sortByLocation(List<IMusicNote> collection);

  /**
   * Computes the console visualization of this music piece.
   *
   * @param notes the notes to print in the visualization.
   * @return the visualization of this entire music piece, in String representation.
   */
  String visualize(List<IMusicNote> notes);
}
