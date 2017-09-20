package cs3500.music.model;

/**
 * Represents a pairing of pitch and octave.
 */
public class SoundPair implements Comparable {
  private final PitchType pitch;
  private final int octave;

  /**
   * Constructs a new SoundPair with the given pitch and octave.
   *
   * @param pitch  the PitchType in this pairing.
   * @param octave the octave in this pairing.
   */
  public SoundPair(PitchType pitch, int octave) {
    this.pitch = pitch;
    this.octave = octave;
  }

  /**
   * Provides a view of this SoundPair's pitch.
   *
   * @return the PitchType
   */
  public PitchType getPitch() {
    return pitch;
  }

  /**
   * Provides a view of this SoundPair's octave.
   *
   * @return the octave
   */
  public int getOctave() {
    return octave;
  }

  /**
   * Compares on the basis of pitch and octave.
   *
   * @param o the SoundPair to compare this one to.
   * @return a negative value if this SoundPair comes before the given one, 0 if the two SoundPairs
   *         have the same pitch and octave, and a positive value if this SoundPair comes after
   *         the given one.
   */
  @Override
  public int compareTo(Object o) {
    if (o instanceof SoundPair) {
      SoundPair other = (SoundPair) o;
      if (this.octave < other.getOctave()) {
        return -12;
      } else if (this.octave > other.getOctave()) {
        return 12;
      } else {
        return this.pitch.compareTo(other.getPitch());
      }
    } else {
      return -1000;
    }
  }

  @Override
  public String toString() {
    return this.pitch.toString() + this.octave;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SoundPair soundPair = (SoundPair) o;

    if (octave != soundPair.octave) {
      return false;
    }
    return pitch == soundPair.pitch;
  }

  @Override
  public int hashCode() {
    int result = pitch != null ? pitch.hashCode() : 0;
    result = 31 * result + octave;
    return result;
  }
}
