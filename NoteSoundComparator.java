package cs3500.music.model;

import java.util.Comparator;

/**
 * Represents a Comparator function-object for comparing MusicNote objects by sound. 'Sound' is
 * defined as having an octave and a pitch. Contains a .compare() method, which takes two
 * MusicNotes and compares them by sound.
 */
public class NoteSoundComparator implements Comparator<IMusicNote> {

  @Override
  public int compare(IMusicNote n1, IMusicNote n2) {
    return n1.compareBySound(n2);
  }
}
