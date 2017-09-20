package cs3500.music.model;

/**
 * Interface for music note objects. Provides methods for directly operating on individual music
 * notes.
 */
public interface IMusicNote {

  /**
   * Provides a view of the PitchType of the music note.
   *
   * @return the pitch of this IMusicNote
   */
  PitchType getPitch();

  /**
   * Provides view of the duration of the music note.
   *
   * @return the duration of this IMusicNote
   */
  double getDuration();

  /**
   * Provides view of the octave the music note plays at.
   *
   * @return the octave of this IMusicNote
   */
  int getOctave();

  /**
   * Provides a reference to the beat in the song when the note begins playing.
   *
   * @return the starting location of the note.
   */
  int getStartLocation();

  /**
   * Provides a reference to the beat in the song when the note stops playing.
   *
   * @return the ending location of the note.
   */
  int getEndLocation();

  /**
   * Computes a SoundPair object for this note based on its pitch and octave.
   *
   * @return a SoundPair matching the note's values
   */
  SoundPair getSoundPair();

  /**
   * Computes the Midi Number representation of this note, based on the note's pitch and octave.
   * The maximum range of Midi Number outputs is [0, 127]
   * @return the Midi Number representation of this note.
   */
  int midiNumber();

  /**
   * Gets the instrument value.
   * @return the instrument value;
   */
  int getInstrument();

  /**
   * Gets the volume value.
   * @return the volume value;
   */
  int getVolume();

  /**
   * Sets the the volume to the given volume.
   * @param newVolume the value to set the volume to.
   * @throws IllegalArgumentException if the volume value is not within range [0, 127].
   */
  void setVolume(int newVolume) throws IllegalArgumentException;

  /**
   * Updates the start and end locations of this MusicNote by the given amount.
   *
   * @param delta amount by which to change the locations.
   */
  void changeLocations(double delta);

  /**
   * Compares this MusicNote with the given MusicNote on the basis of location.
   *
   * @param other the MusicNote to compare with this one.
   * @return negative value if this MusicNote comes before the given one, 0 if the two notes have
   *         the same location, or a positive value if this MusicNote comes after the given one.
   */
  int compareByLocation(IMusicNote other);

  /**
   * Compares this MusicNote with the given MusicNote on the basis of pitch and octave. 'Sound'
   * is defined as having an octave and a pitch.
   *
   * @param other the MusicNote to compare to this one.
   * @return negative value if this MusicNote has a lower sound than the given one, 0 if the two
   *         notes have the same sound, or a positive value if the this MusicNote has a higher
   *         sound than the given one.
   */
  int compareBySound(IMusicNote other);

  /**
   * Computes the String to add the the musical composition's console visualization.
   *
   * @param currBeat the time point during the song, as denoted by the beat.
   * @return the String to write to the console visualization of the musical piece.
   */
  String consoleString(int currBeat);
}
