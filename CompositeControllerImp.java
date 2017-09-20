package cs3500.music.controller;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;


import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.PitchType;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.Repeat;
import cs3500.music.util.MusicReader;
import cs3500.music.view.BlackKey;
import cs3500.music.view.CompositeView;
import cs3500.music.view.ICompositeView;
import cs3500.music.view.IVisualView;
import cs3500.music.view.WhiteKey;

/**
 * Controller for the Composite View. Parses user input and makes the appropriate adjustments to
 * both the MusicEditor's audio information and visual information, simultaneously.
 */
public class CompositeControllerImp implements
        IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>> {
  private ICompositeView<IReadOnlyModel<IMusicNote, IMusicPiece>> view;
  private MusicalOperations<IMusicNote, IMusicPiece> model;
  private boolean currentlyPlaying;
  private PitchType prevPitch = PitchType.C;
  private int prevOctave = 0;
  private int noteStartBeat = 0;
  private boolean constructingRepeat = false;
  private int beginningRepeat = 0;
  private List<Integer> endings = new ArrayList<>();

  /**
   * Constructs a new CompositeControllerImp object from the data in the given music file.
   * @param file contains data on the song to play.
   * @throws MidiUnavailableException if the system cannot access the MidiDevice requested.
   */
  public CompositeControllerImp(Readable file) throws MidiUnavailableException {
    model = MusicReader.parseFile(file, new MusicModel.ModelBuilder());
    //model.addRepeat(new Repeat(0, new ArrayList<>(Arrays.asList(5))));
    //model.addRepeat(new Repeat(6, new ArrayList<>(Arrays.asList(18, 30, 40, 48))));
    view = new CompositeView(new ReadOnlyModel(model), MidiSystem.getSequencer());
    currentlyPlaying = false;
    setKeyListenersForView();
  }

  /**
   * Configures all key listeners, and appropriate events.
   */
  private void setKeyListenersForView() {
    Map<Integer, Runnable> kp = new HashMap<>();
    Map<Integer, Runnable> kr = new HashMap<>();

    kp.put(KeyEvent.VK_LEFT, () -> {
      view.moveRedLine("left");
      view.setTick(view.getTick());
    });


    kp.put(KeyEvent.VK_RIGHT, () -> {
      view.moveRedLine("right");
      view.setTick(view.getTick());
    });

    kp.put(KeyEvent.VK_SPACE, () -> {
      if (currentlyPlaying) {
        pauseMusic();
      } else {
        playMusic();
      }
    });

    kp.put(KeyEvent.VK_R, new Runnable() {
      @Override
      public void run() {
       if (constructingRepeat) {
         constructingRepeat = false;
         model.addRepeat(new Repeat(beginningRepeat, endings));
         view.refresh(new ReadOnlyModel(model));

       } else {
         constructingRepeat = true;
         endings = new ArrayList<>();
       }
      }
    });

    kp.put(KeyEvent.VK_B, new Runnable() {
      @Override
      public void run() {
        if (constructingRepeat) {
          beginningRepeat = view.getTick();
        }
      }
    });

    kp.put(KeyEvent.VK_E, new Runnable() {
      @Override
      public void run() {
        if (constructingRepeat) {
          endings.add(view.getTick());
          Collections.sort(endings);
        }
      }
    });

    kr.put(KeyEvent.VK_HOME, () -> view.goToStart());

    kr.put(KeyEvent.VK_END, () -> {
      view.goToEnd();
      view.pause();
    });


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
    if (!currentlyPlaying) {
      model.writeNote(note);
      view.refresh(new ReadOnlyModel(model));
    }
  }

  @Override
  public void playMusic() {
    view.play();
    currentlyPlaying = true;
  }

  @Override
  public void pauseMusic() {
    view.pause();
    currentlyPlaying = false;
  }

  @Override
  public void goTo(int beatNumber) {
    //not necessary for this functionality
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
    this.addNote(note);
  }

  @Override
  public IVisualView getView() {
    return null;
  }

}

