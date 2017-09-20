package cs3500.music.view;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.MusicalOperations;
import cs3500.music.model.NoteLocationComparator;
import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.Repeat;

/**
 * Represents a MIDI that audially plays a music piece. Implements the functionality described in
 * the IAudioView interface.
 */
public class MidiViewer extends JFrame implements IAudioView<IReadOnlyModel<IMusicNote,
        IMusicPiece>> {
  //Sequencer used for playing a queue of notes (i.e. a pre-defined song).
  private final Sequencer sequencer;

  private IReadOnlyModel<IMusicNote, IMusicPiece> model;

  /**
   * Constructs a new MidiViewer object that can play the song represented in the given input file.
   *
   * @param inputModel the mutable model from which to compute a read only model
   * @param inputSequencer the Sequencer that plays the notes in the song.
   * @throws MidiUnavailableException if not all of the requested MidiDevices are available within
   *                                  the system.
   */
  public MidiViewer(IReadOnlyModel<IMusicNote, IMusicPiece> inputModel, Sequencer
          inputSequencer) throws MidiUnavailableException {
    super();

    this.sequencer = inputSequencer;

    this.sequencer.open();

    refresh(inputModel);

    setSequencerTempo(this.model.getTempo());

    setVisible(false);

    requestKeyFocus();
  }


  /**
   * requests focus for swing, on to the panel where the red line exists. So, the key event can
   * register.
   */
  private void requestKeyFocus() {
    SwingUtilities.invokeLater(MidiViewer.this::requestFocus);
  }

  @Override
  public void refresh(IReadOnlyModel<IMusicNote, IMusicPiece> model) {
    // Retrieves the current tick before refreshing.
    int currTick = getTick();

    // Refreshes
    this.model = model;
    try {
      loadSequence(this.model.getPiece().getNotes());
    } catch (InvalidMidiDataException e) {
      e.getMessage();
    }
    // Sets the tick to where it was before refreshing.
    setTick(currTick);
  }

  @Override
  public Sequencer getSequencer() {
    // this.sequencer is final, cannot be changed.
    return this.sequencer;
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

  /**
   * Sets the tempo of the sequencer to the given new tempo.
   *
   * @param newTempo the new tempo value, in MPQ.
   */
  private void setSequencerTempo(int newTempo) {
    this.sequencer.setTempoInMPQ(newTempo);
  }

  @Override
  public void loadSequence(List<IMusicNote> notes) throws InvalidMidiDataException {
    // Sorts the notes in order of time.
    Collections.sort(notes, new NoteLocationComparator());

    // Creates a new Sequence, gives it an empty initial track
    Sequence sequence = new Sequence(Sequence.PPQ, 1);
    Track mainTrack = sequence.createTrack();

    // Loads notes to the track
    for (IMusicNote note : notes) {
      int vol = note.getVolume();
      int midiNum = note.midiNumber();

      int outputChannel = 10; // Standard piano sound

      // Adds the note to the track
      MidiMessage onMsg = new ShortMessage(ShortMessage.NOTE_ON, outputChannel, midiNum, vol);
      MidiMessage offMsg = new ShortMessage(ShortMessage.NOTE_OFF, outputChannel, midiNum, vol);
      mainTrack.add(new MidiEvent(onMsg, note.getStartLocation()));
      mainTrack.add(new MidiEvent(offMsg, note.getEndLocation())); //Remember to remove +1
    }

    // Link the sequence to the sequencer
    this.sequencer.setSequence(sequence);
  }

  @Override
  public void pause() {
    int originalTempo = this.model.getTempo();

    setSequencerTempo(originalTempo); // Addresses bug where Sequencer tempo resets after .start()

    this.sequencer.stop(); // NOTE: Does not reset the current tick position.

    setSequencerTempo(originalTempo); // Addresses bug where Sequencer tempo resets after .start()
  }

  @Override
  public void play() {
    int originalTempo = this.model.getTempo();

    setSequencerTempo(originalTempo); // Addresses bug where Sequencer tempo resets after .start()

    this.sequencer.start();
    setSequencerTempo(originalTempo); // Addresses bug where Sequencer tempo resets after .start()
  }

  @Override
  public void goToStart() {
    setTick(0);

  }

  @Override
  public void goToEnd() {
    int end = this.model.getTotalDuration();
    setTick(end);
    pause();
  }

  @Override
  public int getTick() {
    int microseconds = (int) this.sequencer.getTickPosition();
    return microseconds;
  }

  @Override
  public void setTick(int beat) {
    // Verifies the input beat.
    if (beat > this.model.getTotalDuration() + 1) {
      throw new IllegalArgumentException("The specified beat must exist within the duration of " +
              "the song!");
    }

    // Sets the tick.
    this.sequencer.setTickPosition(beat);
  }

  @Override
  public void addKeyListeners(KeyListener listener) {
    addKeyListener(listener);
  }

  @Override
  public void run() {
    setVisible(true);
  }
}