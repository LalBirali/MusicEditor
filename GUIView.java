package cs3500.music.view;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;


/**
 * Visual view. With notes display, and piano.
 * If press left and right keys, then red line will move by one beat to the left or the right.
 * The piano in the bottom half of the display will show the corresponding notes.
 * Once the red line reaches the end of the display, and the piece is longer than the window size,
 * the part of the composition shown will move to accommodate where you are in the song.
 * CURRENT VIEW: does not support manually adding notes. Supports readings notes from a text file
 * akin to the format of the text files provided for us in the text folder.
 * NOTE: In future iterations, the views will support manually adding notes, via separate methods
 * and mouse handlers.
 */
public class GUIView extends JFrame implements IVisualView<IReadOnlyModel<IMusicNote,
        IMusicPiece>> {

  private IEditorPanel composition;
  private IPianoPanel piano;
  private IReadOnlyModel<IMusicNote, IMusicPiece> model;
  boolean isPlaying;

  /**
   * Creates the GUIView. A model is read in, then a read-only version of the model's GUI
   * representation is placed on top of the piano panel.
   *
   * @param inputModel the mutable model from which to compute a read only model
   */
  public GUIView(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel) {
    super();
    setTitle("Music Editor");

    this.model = inputModel;
    JPanel listPane = new JPanel();
    listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
    composition = new EditorPanel(model);
    listPane.add(composition.getPane());
    piano = new PianoPanel();
    listPane.add(piano.getContentPane());
    this.add(listPane);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSizes();
    setVisible(false);
    requestKeyFocus();
    isPlaying = false;
  }

  @Override
  public void refresh(IReadOnlyModel<IMusicNote, IMusicPiece> model) {
    this.model = model;
    int curBeat = composition.getCurBeat();
    composition.setModel(model);
    composition.setCurBeat(curBeat + 1);
  }

  @Override
  public IReadOnlyModel<IMusicNote, IMusicPiece> getModel() {
    IMusicPiece opus = this.model.getPiece();
    int measureDuration = (int) Math.ceil(opus.getMeasureDuration());
    int tempo = this.model.getTempo();
    MusicalOperations<IMusicNote, IMusicPiece> mutable =
            new MusicModel(opus, measureDuration, tempo);

    return new ReadOnlyModel(mutable);
  }

  /**
   * requests focus for swing, on to the panel where the red line exists. So, the key event can
   * register.
   */
  private void requestKeyFocus() {
    SwingUtilities.invokeLater(GUIView.this::requestFocus);
  }

  /**
   * sets sizes for the window.
   */
  private void setSizes() {
    Dimension pianoSize = piano.getContentPane().getPreferredSize();
    Dimension compoSize = composition.getPreferredSize();

    setPreferredSize(new Dimension(pianoSize.width,compoSize.height +
                    pianoSize.height + 20));
    setMaximumSize(new Dimension(pianoSize.width,compoSize.height +
                    pianoSize.height + 20));
    setMinimumSize(new Dimension(pianoSize.width,compoSize.height +
                    pianoSize.height + 20));
  }

  @Override
  public void highlightActive() {
    int curBeat = composition.getCurBeat();
    if (curBeat == 0 || curBeat == model.getTotalDuration() + 1) {
      return;
    }
    List<IMusicNote> musicNote = composition.playingAtCurBeat();
    piano.highlightPlayingNotes(musicNote);
  }

  @Override
  public void addKeyListeners(KeyListener listener) {
    addKeyListener(listener);
  }

  @Override
  public void addMouseListeners(MouseListener listener) {
    piano.addMouseListeners(listener);
  }

  @Override
  public void moveRedLine(String direction) {
    composition.moveRedLine(direction);
  }

  @Override
  public IEditorPanel getEditorPanel() {
    return composition;
  }

  @Override
  public boolean isPlaying() {
    return isPlaying;
  }

  @Override
  public void setPlaying() {
    isPlaying = !isPlaying;
  }

  @Override
  public int getCurBeat() {
    return composition.getCurBeat();
  }

  @Override
  public void play() {
    this.composition.scroll(this.piano);
    isPlaying = true;
  }

  @Override
  public void pause() {
    this.composition.haltScroll();
    isPlaying = false;
  }

  @Override
  public void goToStart() {
    pause();
    composition.setCurBeat(0);
    pause();
  }

  @Override
  public void goToEnd() {
    pause();
    int end = this.model.getTotalDuration() + 1;
    composition.setCurBeat(end);
    pause();
  }

  @Override
  public int getTick() {
    return composition.getCurBeat();
  }

  @Override
  public void setTick(int beat) throws IllegalArgumentException {
    if (beat > this.model.getTotalDuration() + 1) {
      throw new IllegalArgumentException("The specified beat must exist within the duration of " +
              "the song!");
    }
    composition.setCurBeat(beat);
  }

  @Override
  public void run() {
    setVisible(true);
  }

}