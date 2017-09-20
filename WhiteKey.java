package cs3500.music.view;

import java.awt.Color;

import javax.swing.JButton;

import cs3500.music.model.PitchType;

/**
 * represents a white key, as a button.
 */
public class WhiteKey  extends JButton implements Key {
  private static int WWD = (WD * 3) / 2; //white width
  private static int WHT = (HT * 3) / 2; //white height
  private final int note;
  private PitchType pitch;
  private int octave;

  /**
   * Constructs a white key button.
   * @param pos is the position of this white key
   */
  public WhiteKey(int pos, PitchType pitch, int o) {
    note = baseNote + 2 * pos
            - (pos + 4) / 7
            - pos / 7;
    int left = 10 + WWD * pos;
    setBackground(Color.WHITE);
    setBounds(left, 10, WWD, WHT);
    setFocusable(false);
    this.pitch = pitch;
    this.octave = o;
  }

  @Override
  public PitchType getPitch() {
    return pitch;
  }

  @Override
  public int getOctave() {
    return octave;
  }

  @Override
  public int getNote() {
    return note;
  }


}

