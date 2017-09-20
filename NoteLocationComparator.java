package cs3500.music.model;

import java.util.Comparator;

/**
 * Represents a Comparator function-object for comparing notes on the basis of location.
 */
public class NoteLocationComparator implements Comparator<IMusicNote> {

  @Override
  public int compare(IMusicNote n1, IMusicNote n2) {
    return n1.compareByLocation(n2);
  }
}
