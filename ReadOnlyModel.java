package cs3500.music.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a ReadOnly Adapter for a MusicalOperations model.
 */
public class ReadOnlyModel implements IReadOnlyModel<IMusicNote, IMusicPiece> {
  private final MusicalOperations<IMusicNote, IMusicPiece> inputModel;

  /**
   * Constructs a new ReadOnlyModel object from the given mutable Model.
   * @param inputModel the Model from which to create a Read-Only version.
   */
  public ReadOnlyModel(MusicalOperations<IMusicNote, IMusicPiece> inputModel) {
    this.inputModel = inputModel;
  }

  @Override
  public IMusicPiece getPiece() {
    return this.inputModel.getPiece();
  }

  @Override
  public IMusicPiece getPieceAfter(int start) {
    return this.inputModel.getPieceAfter(start);
  }

  @Override
  public Map<Integer, List<IMusicNote>> getPieceByTime() {
    return this.inputModel.getPieceByTime();
  }

  @Override
  public IMusicNote getLowestNote(IMusicPiece opus) {
    return this.inputModel.getLowestNote(opus);
  }

  @Override
  public IMusicNote getHighestNote(IMusicPiece opus) {
    return this.inputModel.getHighestNote(opus);
  }

  @Override
  public String getMusicState() {
    return this.inputModel.getMusicState();
  }

  @Override
  public int getTempo() {
    return this.inputModel.getTempo();
  }

  @Override
  public int getTotalDuration() {
    return this.inputModel.getTotalDuration();
  }

  @Override
  public int totalRange() {
    return this.inputModel.totalRange();
  }

  @Override
  public List<Repeat> getRepeats() {
    return inputModel.getRepeats();
  }

}
