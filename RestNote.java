package cs3500.music.model;

/**
 * Represents a Musical note with a pitch and octave.
 */
public class RestNote implements IMusicNote {
  private final PitchType pitch;
  private final int octave;

  /**
   * Constructs a new RestNote object with a pitch and an octave.
   * @param pitch the pitch that this RestNote falls under.
   * @param octave the octave that this RestNote falls under.
   */
  public RestNote(PitchType pitch, int octave) {
    //Verify midi number:
    int midi = pitch.ordinal() + ((1 + octave) * 12);
    if (midi > 127) {
      throw new IllegalArgumentException("The note is too high!");
    }
    if (midi < 0) {
      throw new IllegalArgumentException("The note is too low!");
    }

    this.pitch = pitch;
    this.octave = octave;
  }

  @Override
  public PitchType getPitch() {
    return this.pitch;
  }

  @Override
  public double getDuration() {
    return 0;
  }

  @Override
  public int getOctave() {
    return this.octave;
  }

  @Override
  public int getStartLocation() {
    return -1;
  }

  @Override
  public int getEndLocation() {
    return -1;
  }

  @Override
  public SoundPair getSoundPair() {
    return new SoundPair(this.pitch, this.octave);
  }

  @Override
  public int midiNumber() {
    IMusicNote checker = new RestNote(PitchType.C, this.octave);
    int midi = this.pitch.ordinal() + ((1 + this.octave) * 12);
    return midi;
  }

  @Override
  public int getInstrument() {
    return 1;
  }

  @Override
  public int getVolume() {
    return 0;
  }

  @Override
  public void setVolume(int newVolume) {
    if (newVolume < 0 || newVolume > 127) {
      throw new IllegalArgumentException("Volume value must be between 0 and 127, inclusive.");
    }
    //Do nothing, because a RestNote has no volume, thus changing it will have no effect.
  }

  @Override
  public void changeLocations(double delta) throws IllegalArgumentException {
    throw new IllegalArgumentException("Cannot move a RestNote since it is, in practice, the lack" +
            " of a note.");
  }

  @Override
  public int compareByLocation(IMusicNote other) {
    if (other.getStartLocation() < 0) {
      return 0;
    } else {
      return -1;
    }
  }

  @Override
  public int compareBySound(IMusicNote other) {
    if (this.octave < other.getOctave()) {
      return -12;
    } else if (this.octave > other.getOctave()) {
      return 12;
    } else {
      return this.pitch.compareTo(other.getPitch());
    }
  }

  @Override
  public String consoleString(int currBeat) {
    return "     ";
  }
}
