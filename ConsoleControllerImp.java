package cs3500.music.controller;

import java.awt.event.MouseEvent;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleViewer;
import cs3500.music.view.IConsoleView;
import cs3500.music.view.IVisualView;

/**
 * Controller for console. Parses user input and makes the appropriate adjustments to the
 * MusicEditor's console information.
 */
public class ConsoleControllerImp implements
        IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>> {
  private IConsoleView<IReadOnlyModel<IMusicNote, IMusicPiece>> view;
  private MusicalOperations<IMusicNote, IMusicPiece> model;

  /**
   * Constructs a new ConsoleControllerImp object from the data in the given music file.
   * @param file contains data on the song to play.
   */
  public ConsoleControllerImp(Readable file) {
    model = MusicReader.parseFile(file, new MusicModel.ModelBuilder());
    view = new ConsoleViewer(new ReadOnlyModel(model));
  }

  @Override
  public void run() {
    view.run();
  }

  @Override
  public void addNote(IMusicNote note) {
    //this view this controller supports does not provide this functionality
  }

  @Override
  public void playMusic() {
    //this view this controller supports does not provide this functionality
  }

  @Override
  public void pauseMusic() {
    //this view this controller supports does not provide this functionality
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
    //not necessary functionality
  }

  @Override
  public IVisualView getView() {
    return null;
  }
}
