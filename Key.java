package cs3500.music.view;

import cs3500.music.model.PitchType;

/**
 * represents a key object.
 */
public interface Key {

  int WD = 10;
  int HT = (WD * 20) / 2;
  int baseNote = 16;

  /**
   * Getter for note.
   * @return integer representation of a note.
   */
  int getNote();

  /**
   * Getter for pitch type of this key.
   * @return this key's pitch type
   */
  PitchType getPitch();

  /**
   * Getter for octave of this key.
   * @return this key's octave
   */
  int getOctave();


}
