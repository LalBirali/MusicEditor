package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.PitchType;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.BlackKey;
import cs3500.music.view.GUIView;
import cs3500.music.view.IVisualView;
import cs3500.music.view.WhiteKey;

/**
 * Controller for the Visual View. Parses user input and makes the appropriate adjustments to the
 * MusicEditor's visual information.
 */
public class GuiControllerImp implements
        IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>> {
  private IVisualView<IReadOnlyModel<IMusicNote, IMusicPiece>> view;
  private MusicalOperations<IMusicNote, IMusicPiece> model;
  private boolean isPlaying;
  private PitchType prevPitch = PitchType.C;
  private int prevOctave = 0;
  private int noteStartBeat = 0;

  /**
   * Constructs a new GuiControllerImp object from the data in the given music file.
   * @param file contains data on the song to play.
   */
  public GuiControllerImp(Readable file) {
    model = MusicReader.parseFile(file, new MusicModel.ModelBuilder());
    IReadOnlyModel<IMusicNote, IMusicPiece> model2 = new ReadOnlyModel(model);
    view = new GUIView(model2);
    isPlaying = false;
    setKeyListenersForView();
  }

  /**
   * Configures all key listeners, and appropriate events.
   */
  private void setKeyListenersForView() {
    Map<Integer, Runnable> kp = new HashMap<>();
    Map<Integer, Runnable> kr = new HashMap<>();

    kp.put(KeyEvent.VK_LEFT, () -> view.moveRedLine("left"));


    kp.put(KeyEvent.VK_RIGHT, () -> view.moveRedLine("right"));

    kp.put(KeyEvent.VK_SPACE, () -> {
      System.out.println("i got here to the space");
      if (isPlaying) {
        view.pause();
        isPlaying = false;
      } else {
        view.play();
        isPlaying = true;
      }
    });

    kr.put(KeyEvent.VK_HOME, () -> view.goToStart());

    kr.put(KeyEvent.VK_END, () -> view.goToEnd());


    kr.put(KeyEvent.VK_LEFT, () -> view.highlightActive());

    kr.put(KeyEvent.VK_RIGHT, () -> view.highlightActive());

    MoveKeys moveKeys = new MoveKeys();
    moveKeys.setKeyPresses(kp);
    moveKeys.setKeyReleases(kr);
    view.addKeyListeners(moveKeys);
    addMouseListenersToPianoKeys();
  }

  /**
   * Configures the mouse handler to be added to the data the controller manipulates.
   */
  private void addMouseListenersToPianoKeys() {
    MouseHandler mouseHandler = new MouseHandler(this);
    view.addMouseListeners(mouseHandler);
  }

  @Override
  public void run() {
    view.run();
  }

  @Override
  public void addNote(IMusicNote note) {
    if (!isPlaying) {
      model.writeNote(note);
      view.refresh(new ReadOnlyModel(model));
    }
  }

  @Override
  public void playMusic() {
    view.play();
  }

  @Override
  public void pauseMusic() {
    view.pause();
  }

  @Override
  public void goTo(int beatNumber) {
    //this view this controller supports does not provide this functionality
  }

  @Override
  public MusicalOperations<IMusicNote, IMusicPiece> getModel() {
    IMusicPiece opus = this.model.getPiece();
    int measureDuration = (int) Math.ceil(opus.getMeasureDuration());
    int tempo = this.model.getTempo();

    return new MusicModel(opus, measureDuration, tempo);
  }

  @Override
  public void configureMouseHandler(MouseEvent e) {
    JButton btn = (JButton)e.getSource();
    int octave;
    PitchType pitch;
    if (btn instanceof WhiteKey) {
      octave = ((WhiteKey) btn).getOctave();
      pitch  = ((WhiteKey) btn).getPitch();
    } else {
      octave = ((BlackKey) btn).getOctave();
      pitch  = ((BlackKey) btn).getPitch();
    }
    int duration = 1;
    int startBeat = view.getTick();
    if (prevOctave == octave && prevPitch == pitch) {
      noteStartBeat += 1;
      duration += noteStartBeat;
      startBeat = view.getTick() - noteStartBeat;
    } else {
      noteStartBeat = 0;
    }
    prevPitch = pitch;
    prevOctave = octave;
    IMusicNote note = new MusicNote(pitch, duration, octave, startBeat);
    addNote(note);
  }

  @Override
  public IVisualView getView() {
    return view;
  }
}
