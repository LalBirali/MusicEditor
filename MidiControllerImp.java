package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IAudioView;
import cs3500.music.view.IVisualView;
import cs3500.music.view.MidiViewer;

/**
 * Controller for Audio View. Parses user input and makes the appropriate adjustments to the
 * MusicEditor's audio information.
 */
public class MidiControllerImp implements IController<IMusicNote, MusicalOperations<IMusicNote,
        IMusicPiece>>, KeyListener {
  private MusicalOperations<IMusicNote, IMusicPiece> model;
  private boolean currentlyPlaying;
  private IAudioView<IReadOnlyModel<IMusicNote, IMusicPiece>> view;

  /**
   * Constructs a new MidiControllerImp object from the data in the given music file.
   * @param file contains data on the song to play.
   * @throws MidiUnavailableException if the system cannot access the MidiDevice requested.
   */
  public MidiControllerImp(Readable file) throws MidiUnavailableException {
    model = MusicReader.parseFile(file, new MusicModel.ModelBuilder());
    currentlyPlaying = false;
    view = new MidiViewer(new ReadOnlyModel(model), MidiSystem.getSequencer());
    view.addKeyListeners(this);
  }


  @Override
  public void run() {
    view.run();
    currentlyPlaying = false;
  }

  @Override
  public void addNote(IMusicNote note) {
    //this functionality not provided in this view.
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
    //this functionality not provided in this view.
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
    //not necessary functionality
  }

  @Override
  public IVisualView getView() {
    return null;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    //this functionality not provided in this view.
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      if (currentlyPlaying) {
        view.pause();
        currentlyPlaying = false;
      } else {
        view.play();
        currentlyPlaying = true;
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_HOME) {
      view.goToStart();
    }
    if (e.getKeyCode() == KeyEvent.VK_END) {
      view.goToEnd();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    //this functionality not provided in this view.
  }
}
