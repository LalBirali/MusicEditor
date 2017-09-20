package cs3500.music.model;

/**
 * Represents a musical note, with a pitch, octave, and duration.
 */
public class MusicNote implements IMusicNote {
  private final PitchType pitch;
  private final double duration;
  private final int octave;
  private int startLocation;
  private int endLocation;
  private int instrument;
  private int volume;

  /**
   * Constructs a new MusicNote object, with a pitch of the given PitchType, lasting as long as
   * the given double, and in the octave denoted by the given int.
   *
   * @param pitch    the pitch that the MusicNote plays at
   * @param duration the length of the note, in beats
   * @param octave   the octave in the chromatic-scale that this MusicNote plays at
   * @param startLocation the beat in the song when this note begins playing
   */
  public MusicNote(PitchType pitch, double duration, int octave, int startLocation) throws
          IllegalArgumentException {

    //Verify midi number:
    int midi = pitch.ordinal() + ((1 + octave) * 12);
    if (midi > 127) {
      throw new IllegalArgumentException("The note is too high!");
    }
    if (midi < 0) {
      throw new IllegalArgumentException("The note is too low!");
    }

    this.pitch = pitch;
    this.duration = duration;
    if (octave < 1) {
      throw new IllegalArgumentException("Invalid octave specified, value must be at least 1.");
    } else {
      this.octave = octave;
    }
    this.startLocation = startLocation;
    this.endLocation = (int) Math.ceil(this.startLocation - 1 + this.duration);
    this.instrument = 1;
    this.volume = 120;
  }

  /**
   * Constructs a new MusicNote object with the given int inputs for startingLocation,
   * endingLocation, instrument, midiNum, and volume.
   * @param start the starting location of this music note.
   * @param end the ending location of this music note.
   * @param instrument the instrument this note plays on.
   * @param midiNum the Midi Number value for representing the sound of this note.
   * @param volume the volume this note plays at.
   * @throws IllegalArgumentException if either of the given midiNum or given volume values are not
   *         within the range [0, 127].
   */
  public MusicNote(int start, int end, int instrument, int midiNum, int volume) throws
          IllegalArgumentException {
    if (volume < 0 || volume > 127) {
      throw new IllegalArgumentException("Volume must be in range [0, 127].");
    }
    if (midiNum < 0 || midiNum > 127) {
      throw new IllegalArgumentException("Midi Number must be in range [0, 127].");
    }
    this.startLocation = start;
    this.endLocation = end - 1;
    this.duration = (this.endLocation - start);
    this.instrument = instrument;
    this.volume = volume;
    this.octave = (midiNum - 12) / 12;
    this.pitch = PitchType.values()[midiNum % 12];
  }

  @Override
  public PitchType getPitch() {
    return pitch;
  }

  @Override
  public double getDuration() {
    return duration;
  }

  @Override
  public int getOctave() {
    return octave;
  }

  @Override
  public int getStartLocation() {
    return startLocation;
  }

  @Override
  public int getEndLocation() {
    return endLocation;
  }

  @Override
  public SoundPair getSoundPair() {
    return new SoundPair(this.pitch, this.octave);
  }

  @Override
  public int getInstrument() {
    return Integer.valueOf(this.instrument);
  }

  @Override
  public int getVolume() {
    return Integer.valueOf(this.volume);
  }

  @Override
  public void setVolume(int newVolume) {
    if (newVolume < 0 || newVolume > 127) {
      throw new IllegalArgumentException("Volume value must be between 0 and 127, inclusive.");
    }
    this.volume = newVolume;
  }

  @Override
  public void changeLocations(double delta) {
    this.startLocation += delta;
    this.endLocation = (int) Math.ceil(this.startLocation - 1 + this.duration);
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
  public int compareByLocation(IMusicNote other) {
    if (this.startLocation == other.getStartLocation()) {
      if (this.endLocation < other.getEndLocation()) {
        return -17;
      } else if (this.endLocation > other.getEndLocation()) {
        return 17;
      } else {
        int soundCheck = this.compareBySound(other);
        return soundCheck + (2 * ((int) Math.signum(soundCheck)));
      }
    } else {
      return (this.startLocation - other.getStartLocation()) * 18;
    }
  }

  @Override
  public String consoleString(int currBeat) {
    if (currBeat == this.startLocation) {
      return "  X  ";
    } else if (currBeat > this.startLocation && currBeat <= this.endLocation) {
      return "  |  ";
    } else {
      return "     ";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MusicNote musicNote = (MusicNote) o;

    if (Double.compare(musicNote.duration, duration) != 0) {
      return false;
    }
    if (octave != musicNote.octave) {
      return false;
    }
    if (startLocation != musicNote.startLocation) {
      return false;
    }
    if (endLocation != musicNote.endLocation) {
      return false;
    }
    return pitch == musicNote.pitch;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = pitch != null ? pitch.hashCode() : 0;
    temp = Double.doubleToLongBits(duration);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + octave;
    result = 31 * result + startLocation;
    result = 31 * result + endLocation;
    return result;
  }

  @Override
  public int midiNumber() {
    int midi = this.pitch.ordinal() + ((1 + this.octave) * 12);
    return midi;
  }
}
