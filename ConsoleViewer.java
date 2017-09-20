package cs3500.music.view;


import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;

/**
 * Represents a viewer for the console output visualization of the song.
 */
public class ConsoleViewer implements IConsoleView<IReadOnlyModel<IMusicNote, IMusicPiece>> {

  private IReadOnlyModel<IMusicNote, IMusicPiece> model;

  /**
   * Constructs a new ConsoleViewer object that can compute a console visualization of the song
   * described in the given input file.
   * @param inputModel the mutable model from which to compute a read only model
   */
  public ConsoleViewer(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel) {
    this.model = inputModel;
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
  public String consoleView(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel) {
    return new PrinterForConsole(model).getMusicState();
  }

  @Override
  public void run() {
    System.out.print(this.consoleView(this.model));
  }

  @Override
  public void refresh(IReadOnlyModel<IMusicNote, IMusicPiece> model) {
    this.model = model;
  }

  @Override
  public void play() {
    // Does nothing in ConsoleView.
  }

  @Override
  public void pause() {
    // Does nothing in ConsoleView.
  }

  @Override
  public void goToStart() {
    // Does nothing in ConsoleView.
  }

  @Override
  public void goToEnd() {
    // Does nothing in ConsoleView.
  }

  @Override
  public int getTick() {
    // Does nothing in ConsoleView.
    return 0;
  }

  @Override
  public void setTick(int beat) throws IllegalArgumentException {
    // Does nothing in ConsoleView.
  }
}
