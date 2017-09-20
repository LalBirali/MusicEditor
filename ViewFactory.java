package cs3500.music.util;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.view.CompositeView;
import cs3500.music.view.ConsoleViewer;
import cs3500.music.view.GUIView;
import cs3500.music.view.IView;
import cs3500.music.view.MidiViewer;

/**
 * Represents a Factory class to construct different types of Views.
 */
public class ViewFactory {

  /**
   * Computes a specific IView of a view type specified by the given input String.
   *
   * @param viewName the type of view to return.
   * @return the IView object corresponding to the given type of view.
   * @throws IllegalArgumentException if an invalid type of view is specified.
   */
  public static IView createView(String viewName, Readable file) throws IllegalArgumentException {
    IView<IReadOnlyModel<IMusicNote, IMusicPiece>> outputView = null;
    MusicalOperations<IMusicNote, IMusicPiece> mutableModel = MusicReader.parseFile(file, new
            MusicModel.ModelBuilder());
    IReadOnlyModel<IMusicNote, IMusicPiece> inputModel = new ReadOnlyModel(mutableModel);

    switch (viewName.toLowerCase()) {
      case "midi":
        try {
          outputView = new MidiViewer(inputModel, MidiSystem.getSequencer());
        } catch (MidiUnavailableException e) {
          e.getMessage();
        }
        break;

      case "visual":
        outputView = new GUIView(inputModel);
        break;

      case "console":
        outputView = new ConsoleViewer(inputModel);
        break;

      case "composite":
        try {
          outputView = new CompositeView(inputModel, MidiSystem.getSequencer());
        } catch (MidiUnavailableException e) {
          e.getMessage();
        }
        break;

      default:
        throw new IllegalArgumentException("Invalid type of view specified!");
    }

    return outputView;
  }
}
