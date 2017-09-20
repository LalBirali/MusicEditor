package cs3500.music.view;

import java.awt.event.KeyListener;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;

import cs3500.music.model.IMusicNote;

/**
 * Interface for the the audio portion of the view. Contains methods that provide functionality
 * of playing the audio of the music compositions.
 */
public interface IAudioView<T> extends IView<T> {

  /**
   * Loads the given list of note into the sequence to be played by the sequencer.
   *
   * @param notes the list of notes to be played.
   * @throws InvalidMidiDataException when an illegal message is generated
   */
  void loadSequence(List<IMusicNote> notes) throws InvalidMidiDataException;

  /**
   * Provides copy of this MidiViewer's Sequencer.
   *
   * @return the Sequencer.
   */
  Sequencer getSequencer();

  /**
   * Adds key listener to this view.
   * @param listener is listener to be added.
   */
  void addKeyListeners(KeyListener listener);
}
