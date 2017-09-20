package cs3500.music.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.JPanel;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.PitchType;

/**
 * Represents the piano display.
 */
public class PianoPanel extends JPanel implements IPianoPanel {

  private final int OCTAVES = 10; // change as desired

  private WhiteKey[] whites = new WhiteKey [7 * OCTAVES + 1];
  private BlackKey[] blacks = new BlackKey [5 * OCTAVES];

  private  JPanel contentPane;

  private Map<String, Integer> noteToPosOnPiano = new HashMap<>();
  private Map<Integer, PitchType> pitches = new HashMap<>();
  private Map<Integer, PitchType> pitchesSharps = new HashMap<>();

  /**
   * Constructs a new panel. Setting the preferred dimension size. And also assigns
   * Values for the string representations of the 12 pitches.
   */
  public PianoPanel() {
    contentPane = new JPanel(null) {
      @Override
      public Dimension getPreferredSize() {
        int count = getComponentCount();
        Component last = getComponent(count - 1);
        Rectangle bounds = last.getBounds();
        int width = 10 + bounds.x + bounds.width;
        int height = 10 + bounds.y + bounds.height;

        return new Dimension(width, height);
      }

      @Override
      public boolean isOptimizedDrawingEnabled() {
        return false;
      }
    };

    initializeKeyPitch();

    createBlackKeys();
    createWhiteKeys();

    initializeKeys();
  }

  @Override
  public void initializeKeyPitch() {
    noteToPosOnPiano.put("C", 0);
    noteToPosOnPiano.put("D", 1);
    noteToPosOnPiano.put("E", 2);
    noteToPosOnPiano.put("F", 3);
    noteToPosOnPiano.put("G", 4);
    noteToPosOnPiano.put("A", 5);
    noteToPosOnPiano.put("B", 6);

    noteToPosOnPiano.put("C#", 0);
    noteToPosOnPiano.put("D#", 1);
    noteToPosOnPiano.put("F#", 2);
    noteToPosOnPiano.put("G#", 3);
    noteToPosOnPiano.put("A#", 4);

    pitches.put(0, PitchType.C);
    pitches.put(1, PitchType.D);
    pitches.put(2, PitchType.E);
    pitches.put(3, PitchType.F);
    pitches.put(4, PitchType.G);
    pitches.put(5, PitchType.A);
    pitches.put(6, PitchType.B);

    pitchesSharps.put(0, PitchType.Csharp);
    pitchesSharps.put(1, PitchType.Dsharp);
    pitchesSharps.put(2, PitchType.Fsharp);
    pitchesSharps.put(3, PitchType.Gsharp);
    pitchesSharps.put(4, PitchType.Asharp);
  }

  @Override
  public void initializeKeys() {
    for (WhiteKey key : whites) {
      key.setBackground(Color.white);
    }

    for (BlackKey key : blacks) {
      key.setBackground(Color.black);
    }
  }


  /**
   * Fills in array of white keys and adds the buttons to the whites array.
   */
  private void createWhiteKeys() {
    for (int i = 0; i < whites.length; i++) {
      whites [i] = new WhiteKey(i, pitches.get(i % 7), (i / 7) + 1);
      contentPane.add(whites [i]);
    }
  }

  /**
   * Fills in array of black keys and adds the buttons to the blacks array.
   */
  private void createBlackKeys() {
    for (int i = 0; i < blacks.length; i++) {
      blacks [i] = new BlackKey(i, pitchesSharps.get(i % 5), (i / 5) + 1);
      contentPane.add(blacks [i]);
    }
  }


  @Override
  public JPanel getContentPane() {
    return contentPane;
  }



  @Override
  public void highlightPlayingNotes(List<IMusicNote> noteList) {

    initializeKeys();
    for (IMusicNote note : noteList) {
      if (note.getPitch().toString().length() == 1) {
        //white key
        int pos = (note.getOctave() - 1) * 7 + noteToPosOnPiano.get(note.getPitch().toString());
        whites[pos].setBackground(Color.red);
      } else {
        int pos = (note.getOctave() - 1) * 5 + noteToPosOnPiano.get(note.getPitch().toString());
        blacks[pos].setBackground(Color.red);

      }
    }
  }

  @Override
  public void addMouseListeners(MouseListener listener) {
    for (BlackKey key: blacks) {
      key.addMouseListener(listener);
    }
    for (WhiteKey key: whites) {
      key.addMouseListener(listener);
    }
  }

}
