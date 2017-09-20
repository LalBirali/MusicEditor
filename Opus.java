package cs3500.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Represents a musical composition, with a List of IMusicNote to represent the notes to be
 * played, and a measure duration.
 */
public class Opus implements IMusicPiece {
  private double measureDuration;
  private List<IMusicNote> notes;
  private int tempo;

  /**
   * Constructs a new Opus object, whose measure length is represented by the given double.
   * @param measureDuration the number of beats that each measure is composed of.
   */
  public Opus(double measureDuration, int tempo) {
    this.measureDuration = measureDuration;
    this.notes = new ArrayList<>();
    this.tempo = tempo;
  }

  /**
   * Constructs a new Opus object, with a measure duration specified by the given double, and
   * contains the notes in the given List of IMusicNote.
   *
   * @param measureDuration the maximum number of beats to allow in a measure.
   * @param notes           the notes that this Opus will contain.
   */
  public Opus(double measureDuration, List<IMusicNote> notes, int tempo) {
    this.measureDuration = measureDuration;
    this.notes = notes;
    this.tempo = tempo;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int newTempo) {
    this.tempo = newTempo;
  }

  @Override
  public List<IMusicNote> getNotes() {
    return new ArrayList<>(this.notes);
  }

  @Override
  public List<IMusicNote> getNotesAfter(int start) {
    Stream<IMusicNote> noteStream = this.notes.stream();
    Stream<IMusicNote> filtered =
            noteStream.filter((IMusicNote note) -> note.getStartLocation() >= start);

    return filtered.collect(Collectors.toList());
  }

  @Override
  public Map<Integer, List<IMusicNote>> computeMap() {
    int totalTime = (int) Math.ceil(getTotalDuration());
    Map<Integer, List<IMusicNote>> map = new TreeMap<>();

    List<SoundPair> sounds = ConsolePrinter.soundPairs(lowestNote(), highestNote());

    for (int t = 0; t <= totalTime; t += 1) {
      List<IMusicNote> toPut = new ArrayList<>();
      for (SoundPair sound : sounds) {
        IMusicNote noteToAdd = getNoteAt(sound, t);
        toPut.add(noteToAdd);
      }
      map.put(t, toPut);
    }

    return map;
  }

  @Override
  public double getMeasureDuration() {
    return this.measureDuration;
  }

  @Override
  public double getTotalDuration() {
    double currMax = 0;
    for (IMusicNote note : this.notes) {
      if (note.getEndLocation() > currMax) {
        currMax = note.getEndLocation();
      }
    }
    return currMax;
  }

  @Override
  public void addNote(IMusicNote note) {
    this.notes.add(note);
  }

  @Override
  public void delete(IMusicNote note) throws IllegalArgumentException {
    if (!(this.notes.contains(note))) {
      throw new IllegalArgumentException("The Note specified to edit does not exist.");
    }
    this.notes.remove(note);
  }

  @Override
  public void replace(IMusicNote currentNote, IMusicNote newNote) throws IllegalArgumentException {
    if (!(this.notes.contains(currentNote))) {
      throw new IllegalArgumentException("The Note specified to edit does not exist.");
    }

    int currIdx = this.notes.indexOf(currentNote);
    this.notes.remove(currIdx);
    this.notes.add(currIdx, newNote);
  }

  @Override
  public void joinPieceIntegrated(IMusicPiece other) throws IllegalArgumentException {
    if (other.getMeasureDuration() != this.measureDuration) {
      throw new IllegalArgumentException("The provided piece has a different time signature than " +
              "the piece to which you are trying to join, must be same time signature.");
    }
    this.notes.addAll(other.getNotes());
  }

  @Override
  public void joinPiecePlayAfter(IMusicPiece other) throws IllegalArgumentException {
    if (other.getMeasureDuration() != this.measureDuration) {
      throw new IllegalArgumentException("The provided piece has a different time signature than " +
              "the piece to which you are trying to join, must be same time signature.");
    }

    double songLen = getTotalDuration();
    for (IMusicNote note : other.getNotes()) {
      note.changeLocations(songLen);
    }

    this.notes.addAll(other.getNotes());
  }

  @Override
  public IMusicNote lowestNote() {
    return Collections.min(this.notes, new NoteSoundComparator());
  }

  @Override
  public IMusicNote highestNote() {
    return Collections.max(this.notes, new NoteSoundComparator());
  }

  @Override
  public IMusicNote getNoteAt(SoundPair sound, int beat) {
    IMusicNote output = new RestNote(sound.getPitch(), sound.getOctave());
    for (IMusicNote note : this.notes) {
      if (note.getPitch().equals(sound.getPitch()) && note.getOctave() == sound.getOctave() &&
              (beat >= note.getStartLocation()) && (beat <= note.getEndLocation())) {
        output = note;
        break;
      }
    }
    return output;
  }

  @Override
  public List<IMusicNote> sortByLocation(List<IMusicNote> collection) {
    Collections.sort(collection, new NoteLocationComparator());
    return collection;
  }

  @Override
  public String visualize(List<IMusicNote> collection) {
    int totalTime = (int) Math.ceil(getTotalDuration());
    ConsolePrinter viewer = new ConsolePrinter(lowestNote(), highestNote(),
            collection, totalTime);
    String header = viewer.headerLine();
    String rest = viewer.subsequentLines();

    return header + "\n" + rest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Opus opus = (Opus) o;

    if (Double.compare(opus.measureDuration, measureDuration) != 0) {
      return false;
    }
    if (tempo != opus.tempo) {
      return false;
    }
    return notes != null ? notes.equals(opus.notes) : opus.notes == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(measureDuration);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + tempo;
    return result;
  }
}
