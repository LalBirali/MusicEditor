package cs3500.music.view;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;

/**
 * The interface for the textual view as part of the Music Composition's view. Contains
 * functionality for viewing the view as a textual, console output.
 */
public interface IConsoleView<T> extends IView<T> {

  /**
   * Computes the textual, console view of the the given composition.
   * @param inputModel the composition to view in the console.
   * @return the console view of the musical composition.
   */
  String consoleView(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel);
}
