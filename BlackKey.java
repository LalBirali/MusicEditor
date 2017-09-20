package cs3500.music.view;

import java.awt.Color;

import javax.swing.JButton;

import cs3500.music.model.PitchType;

/**
 * represents a black key, as a button.
 */
public class BlackKey extends JButton implements Key {
  private final int note;
  private PitchType pitch;
  private int octave;

  /**
   * Constructs a black key button.
   * @param pos is the position of this black key
   */
  public BlackKey(int pos, PitchType pitch, int octave) {
    note = baseNote + 1 + 2 * pos + (pos + 3) / 5 + pos / 5;
    int left = 10 + WD
            + ((WD * 3) / 2) * (pos + (pos / 5)
            + ((pos + 3) / 5));
    setBackground(Color.BLACK);
    setBounds(left, 10, WD, HT);
    setFocusable(false);
    this.pitch = pitch;
    this.octave = octave;
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



