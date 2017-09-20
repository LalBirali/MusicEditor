package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.Repeat;

/**
 * Represents a composite view that combines the Visual and Audio elements of the View in this
 * MVC project.
 */
public class CompositeView implements ICompositeView<IReadOnlyModel<IMusicNote, IMusicPiece>> {
  private IAudioView<IReadOnlyModel<IMusicNote, IMusicPiece>> midi;
  private IVisualView<IReadOnlyModel<IMusicNote, IMusicPiece>> gui;
  private IReadOnlyModel<IMusicNote, IMusicPiece> model;
  /**
   * Constructs a new CompositeView object that can visualize and play the song data from the
   * given file.
   * @param inputModel the mutable model from which to compute a read only model
   * @param seq the Sequencer that the IAudioView uses to play the audio of the notes in the file.
   */
  public CompositeView(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel, Sequencer seq) throws
          MidiUnavailableException {
    this.model = inputModel;
    this.midi = new MidiViewer(inputModel, seq);
    this.gui = new GUIView(inputModel);
  }

  @Override
  public void play() {
    this.midi.play();
    this.gui.play();
  }

  @Override
  public void pause() {
    this.midi.pause();
    this.gui.pause();

  }

  @Override
  public void goToStart() {
    this.midi.goToStart();
    this.gui.goToStart();
  }

  @Override
  public void goToEnd() {
    this.midi.goToEnd();
    this.gui.goToEnd();
  }

  @Override
  public int getTick() {
    // There can be situations where arrow keys move the current tick in the GUI, leaving the
    // MIDI behind. However, the opposite never happens. So, the tick is retrieved from the GUI.
    return this.gui.getTick();
  }

  @Override
  public void setTick(int beat) throws IllegalArgumentException {
    if (beat > this.model.getTotalDuration() + 1) {
      throw new IllegalArgumentException("The specified beat must exist within the duration of " +
              "the song!");
    }

    this.midi.setTick(beat);
    this.gui.setTick(beat);
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

  @Override
  public void run() {
    this.gui.run();
    this.midi.run();
  }

  @Override
  public void refresh(IReadOnlyModel<IMusicNote, IMusicPiece> model) {
    this.model = model;
    this.midi.refresh(model);
    this.gui.refresh(model);
  }

  @Override
  public void highlightActive() {
    gui.highlightActive();
  }

  @Override
  public void addKeyListeners(KeyListener listener) {
    gui.addKeyListeners(listener);
    midi.addKeyListeners(listener);
  }

  @Override
  public void moveRedLine(String direction) {
    gui.moveRedLine(direction);
  }

  @Override
  public void addMouseListeners(MouseListener listener) {
    gui.addMouseListeners(listener);
  }

  @Override
  public IEditorPanel getEditorPanel() {
    return gui.getEditorPanel();
  }

  @Override
  public boolean isPlaying() {
    return gui.isPlaying();
  }

}