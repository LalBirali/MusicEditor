package cs3500.music.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a repeat.
 */
public class Repeat {
  private int startBeat;
  private List<Integer> endings;

  public Repeat(int startBeat, List<Integer> endings) {
    if (endings.isEmpty()) {
      throw new IllegalArgumentException("not a valid repeat");
    }
    this.startBeat = startBeat;
    Collections.sort(endings);
    if (endings.get(0) <= startBeat ) {
      throw new IllegalArgumentException("not a valid repeat");
    }
    this.endings = endings;
  }

  /**
   * Is the ending to be added.
   * @param a is the beat of the ending to be added.
   */
  public void addEnding(int a) {
    if (a > startBeat && !endings.contains(a)) {
      endings.add(a);
      Collections.sort(endings);
    }
  }

  /**
   * Getter for start beat.
   * @return
   */
  public int getStartBeat() {
    return startBeat;
  }

  /**
   * Getter for all endings associated with this beginning.
   * @return all ending beats for this repeat.
   */
  public List<Integer> getEndings() {
    List<Integer> ends = new ArrayList<>();
    for (Integer integer : endings) {
      ends.add(integer);
    }
    return ends;
  }

  /**
   * Gets last ending of the repeat.
   * @return last ending beat of the repeat.
   */
  public int getRepeatDuration() {
    return endings.get(endings.size() - 1);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other instanceof Repeat) {
      Repeat r2 = (Repeat) other;
      if (r2.getStartBeat() == startBeat) {
        if (r2.getEndings().size() == endings.size()) {
          endings.containsAll(r2.getEndings());
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return startBeat * 17;
  }

}



