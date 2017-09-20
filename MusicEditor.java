package cs3500.music;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;

import javax.sound.midi.MidiUnavailableException;

import cs3500.music.controller.CompositeControllerImp;
import cs3500.music.controller.IController;
import cs3500.music.view.IView;
import cs3500.music.util.ViewFactory;


public class MusicEditor {

  /**
   * The main method for the MusicEditor, runs the given file in the given type of view.
   * @param args the arguments required to run the MusicEditor. The first argument should be the
   *             the path to the file containing the song to play, the second argument should be
   *             a specifier for the type of view to run.
   * @throws IllegalArgumentException if the specified file can't be found.
   */
  public static void main(String[] args) throws IllegalArgumentException {

    // Verifies number of arguments.
    //if (Array.getLength(args) < 2) {
      //throw new IllegalArgumentException("Invalid number of parameters given.");
    //}

    String filePath = null;
    FileReader file;
    String viewType;

    try {
      // Assigns arguments to viewType and file.
      filePath = "mystery-2.txt";//args[0];
      viewType = "composite";//args[1];
      file = new FileReader(filePath);
      IController con = new CompositeControllerImp(file);
      con.run();

    } catch (FileNotFoundException f) {
      throw new IllegalArgumentException("Could not locate the file [" + filePath + "].");
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }

  }
}