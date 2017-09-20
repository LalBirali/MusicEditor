package cs3500.music.util;

import javax.sound.midi.MidiUnavailableException;

import cs3500.music.controller.CompositeControllerImp;
import cs3500.music.controller.ConsoleControllerImp;
import cs3500.music.controller.GuiControllerImp;
import cs3500.music.controller.IController;
import cs3500.music.controller.MidiControllerImp;
import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.MusicalOperations;

/**
 * Represents a Factory class to construct different types of Controllers.
 */
public class ControllerFactory {

  /**
   * Computes a specific IController corresponding to the view type specified by the given input
   * String.
   *
   * @param viewName the type of view whose controller to return.
   * @return the IController object corresponding to the given type of view.
   * @throws IllegalArgumentException if an invalid type of view is specified.
   */
  public static IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>>
      createController(String viewName, Readable file) throws IllegalArgumentException {

    IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>> outputController = null;

    switch (viewName.toLowerCase()) {
      case "midi":
        try {
          outputController = new MidiControllerImp(file);
        } catch (MidiUnavailableException e) {
          e.getMessage();
        }
        break;

      case "visual":
        outputController = new GuiControllerImp(file);
        break;

      case "console":
        outputController = new ConsoleControllerImp(file);
        break;

      case "composite":
        try {
          outputController = new CompositeControllerImp(file);
        } catch (MidiUnavailableException e) {
          e.getMessage();
        }
        break;

      default:
        throw new IllegalArgumentException("Invalid type of view specified!");
    }

    return outputController;
  }
}
