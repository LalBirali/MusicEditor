package cs3500.music.model;

/**
 * Represents a Type for the pitch of a music note in the Western system of music.
 */
public enum PitchType {
  C("C"),
  Csharp("C#"),
  D("D"),
  Dsharp("D#"),
  E("E"),
  F("F"),
  Fsharp("F#"),
  G("G"),
  Gsharp("G#"),
  A("A"),
  Asharp("A#"),
  B("B");

  private final String stringRep;

  /**
   * Constructs an instance of PitchType with the given string representation.
   * @param stringRep the String representation of the PitchType value.
   */
  PitchType(String stringRep) {
    this.stringRep = stringRep;
  }

  @Override
  public String toString() {
    return this.stringRep;
  }
}
