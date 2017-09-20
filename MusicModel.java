package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import cs3500.music.util.CompositionBuilder;

/**
 * Represents a model for a musical composition; includes an IMusicPiece object containing the
 * sequence of notes, and functionality for writing and edition the composition.
 */
public class MusicModel implements MusicalOperations<IMusicNote, IMusicPiece> {
  private IMusicPiece song;
  private int measureLength;
  private int tempo;
  private List<Repeat> repeats = new ArrayList<>();

  /**
   * Constructs a new MusicModel, initializing the measures field to a new, empty list.
   */
  public MusicModel(int tempo) {
    this.measureLength = 4;
    this.tempo = tempo;
    this.song = new Opus(this.measureLength, this.tempo);
  }

  /**
   * Constructs a new MusicModel, initializing the measures field to the given List of Measures.
   *
   * @param opus the Opus object that will be the value of the song field.
   */
  public MusicModel(IMusicPiece opus, int measureDuration, int tempo) {
    this.song = opus;
    this.measureLength = measureDuration;
    this.tempo = tempo;
  }

  @Override
  public IMusicPiece getPiece() {
    return new Opus(this.song.getMeasureDuration(), this.song.getNotes(), this.song.getTempo());
  }

  @Override
  public IMusicPiece getPieceAfter(int start) {
    List<IMusicNote> afterNotes = this.song.getNotesAfter(start);

    return new Opus(this.song.getMeasureDuration(), afterNotes, this.song.getTempo());
  }

  @Override
  public Map<Integer, List<IMusicNote>> getPieceByTime() {
    return this.song.computeMap();
  }

  @Override
  public void writeNote(IMusicNote addedNote) {
    this.song.addNote(addedNote);
  }

  @Override
  public void removeNote(IMusicNote note) throws IllegalArgumentException {
    this.song.delete(note);
  }

  @Override
  public void replaceNote(IMusicNote currentNote, IMusicNote newNote) throws
          IllegalArgumentException {
    this.song.replace(currentNote, newNote);
  }

  @Override
  public void combinePieces(IMusicPiece toJoin, boolean simultaneous) throws
          IllegalArgumentException {
    if (simultaneous) {
      this.song.joinPieceIntegrated(toJoin);
    } else {
      this.song.joinPiecePlayAfter(toJoin);
    }
  }

  @Override
  public IMusicNote getLowestNote(IMusicPiece opus) {
    return opus.lowestNote();
  }

  @Override
  public IMusicNote getHighestNote(IMusicPiece opus) {
    return opus.highestNote();
  }

  @Override
  public String getMusicState() {
    return this.song.visualize(this.song.getNotes());
  }

  @Override
  public void setTempo(int newTempo) {
    this.tempo = newTempo;
    this.song.setTempo(newTempo);
  }

  @Override
  public int getTempo() {
    return Integer.valueOf(this.tempo);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MusicModel model = (MusicModel) o;

    if (measureLength != model.measureLength) {
      return false;
    }
    if (tempo != model.tempo) {
      return false;
    }
    return song != null ? song.equals(model.song) : model.song == null;
  }

  @Override
  public int hashCode() {
    int result = song != null ? song.hashCode() : 0;
    result = 31 * result + measureLength;
    result = 31 * result + tempo;
    return result;
  }

  @Override
  public int getTotalDuration() {
    return ((int) Math.ceil(this.song.getTotalDuration()));
  }

  @Override
  public int getMeasureLength() {
    return Integer.valueOf(this.measureLength);
  }

  @Override
  public int totalRange() {
    return ConsolePrinter.soundPairs(getLowestNote(this.song), getHighestNote(this.song)).size();
  }

  @Override
  public List<Repeat> getRepeats() {
    return repeats;
  }

  @Override
  public void addRepeat(Repeat repeat) {
    if (repeats.isEmpty()) {
      repeats.add(repeat);
    } else {
      for (int i = 0; i < repeats.size() - 1; i++) {
        Repeat r = repeats.get(i);
        Repeat r2 = repeats.get(i + 1);
        List<Integer> r1Endings = r.getEndings();
        List<Integer> repeatEndings = repeat.getEndings();
        if (repeat.getStartBeat() > r.getStartBeat()) {
          if (repeat.getStartBeat() < r2.getStartBeat()
                  && repeat.getStartBeat() >= r1Endings.get(r1Endings.size() - 1)) {
            if (repeatEndings.get(repeatEndings.size() - 1) < r2.getStartBeat()) {
              repeats.add(i + 1, repeat);
              break;
            }
          }
        } else if (repeat.getStartBeat() < r.getStartBeat()){
          if (repeatEndings.get(repeatEndings.size() - 1) <= r.getStartBeat()) {
            repeats.add(i, repeat);
            break;
          }
        }
      }
      if (!repeats.contains(repeat)) {
        repeats.add(repeat);
      }
    }
  }

  /**
   * A Builder class for MusicalOperations objects.
   */
  public static final class ModelBuilder implements CompositionBuilder<MusicalOperations<IMusicNote,
          IMusicPiece>> {
    private int currentTempo;
    private final int defaultMeasureDuration;
    private IMusicPiece piece;

    /**
     * Constructs a new ModelBuilder object with default values for tempo, measureDuration, and
     * piece.
     */
    public ModelBuilder() {
      this.currentTempo = 20000;
      this.defaultMeasureDuration = 4;
      this.piece = new Opus(this.defaultMeasureDuration, this.currentTempo);
    }

    @Override
    public MusicalOperations<IMusicNote, IMusicPiece> build() {
      return new MusicModel(this.piece, this.defaultMeasureDuration, this.currentTempo);
    }

    @Override
    public CompositionBuilder<MusicalOperations<IMusicNote, IMusicPiece>> setTempo(int tempo) {
      this.currentTempo = tempo;
      this.piece.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<MusicalOperations<IMusicNote, IMusicPiece>> addNote(int start, int
            end, int instrument, int pitch, int volume) {
      this.piece.addNote(new MusicNote(start, end, instrument, pitch, volume));
      return this;
    }
  }
}
