package cs3500.music.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A helper class to IMusicPiece, provides functionality for printing the notes in the musical piece
 * to the console.
 */
public class ConsolePrinter {
  private final IMusicNote lowestSound;
  private final IMusicNote highestSound;
  private final List<IMusicNote> notes;
  private final int totalDuration;

  /**
   * Constructs a new ConsolePrinter object with a lowest note and highest note, a list of all
   * the notes to be printed as part of the musical piece, and the total duration of the musical
   * piece that is to be printed.
   *
   * @param lowestSound   the note with the lowest sound.
   * @param highestSound  the note with the highest sound.
   * @param notes         the list of notes to print to the console.
   * @param totalDuration the length of time (in beats) for which the music piece should be
   *                      visualized.
   */
  public ConsolePrinter(IMusicNote lowestSound, IMusicNote highestSound, List<IMusicNote> notes, int
          totalDuration) {
    this.lowestSound = lowestSound;
    this.highestSound = highestSound;
    this.notes = notes;
    this.totalDuration = totalDuration;
  }

  /**
   * Computes the lines of output for the console visualization for this song, not including the
   * pitch-octave headers line.
   *
   * @return the lines of console output for the notes in this song.
   */
  public String subsequentLines() {
    SortedMap<SoundPair, List<IMusicNote>> notesMap = mapNotes(this.notes);
    int totalTime = this.totalDuration;
    StringBuilder lines = new StringBuilder("");

    //Computes the line to be printed at each beat in the piece
    for (int t = 0; t <= totalTime; t += 1) {
      StringBuilder thisLine = new StringBuilder(timeColumn(t, totalTime));
      for (Map.Entry<SoundPair, List<IMusicNote>> item : notesMap.entrySet()) {
        if (item.getValue().size() == 0) {
          thisLine.append( "     "); // If there are no notes playing this sound
        } else if (item.getValue().size() == 1) {
          thisLine.append(item.getValue().get(0).consoleString(t)); //If 1 note plays this sound
        } else {
          thisLine.append(outputAtTimeAndSound(t, item.getValue())); //If > 1 notes play this sound
        }
      }
      lines.append(thisLine + "\n");
    }
    return lines.toString();
  }

  /**
   * Determines which note's console String to output out of the given list of notes, based on the
   * given point in time.
   *
   * @param time      the current beat at which the console output is to be determined.
   * @param workNotes the list of notes from which to derive the console output.
   * @return the console String output based on the point in time.
   */
  private String outputAtTimeAndSound(int time, List<IMusicNote> workNotes) {
    List<String> consoleStrs = new ArrayList<>();
    for (IMusicNote note : workNotes) {
      consoleStrs.add(note.consoleString(time));
    }

    if (!(consoleStrs.contains("  X  ") || consoleStrs.contains("  |  "))) {
      return "     "; // If there is no visualization to output at the given beat
    } else if (consoleStrs.contains("  X  ")) {
      return "  X  "; // If a new note begins at the given beat
    } else {
      return "  |  "; // If there is a visualization to output, but no note begins at this beat.
    }
  }

  /**
   * Computes the time column String output for the given time in the song.
   *
   * @param currTime  the current beat number
   * @param totalTime the total duration of the song
   * @return the time column String output
   */
  private String timeColumn(int currTime, int totalTime) {
    int colWidth = String.valueOf(totalTime).length();
    int currWidth = String.valueOf(currTime).length();

    String buffer = "";
    for (int t = 0; t < (colWidth - currWidth); t += 1) {
      buffer += " ";
    }

    return String.valueOf(currTime) + buffer;
  }

  /**
   * Computes a SortedMap that maps all the SoundPairs necessary for viewing the notes in the
   * given list to the IMusicNotes associated at each SoundPair. If there is no note with the
   * pitch and sound of a SoundPair, its value is mapped as null.
   *
   * @param notesList the list of Notes to place into the map.
   * @return SortedMap with IMusicNotes mapped to their corresponding SoundPairs.
   */
  private SortedMap<SoundPair, List<IMusicNote>> mapNotes(List<IMusicNote> notesList) {
    SortedMap<SoundPair, List<IMusicNote>> notesMap = new TreeMap<>();

    //Computes all the sound pairs necessary for viewing
    List<SoundPair> soundPairs = soundPairs(this.lowestSound, this.highestSound);

    //Initializes map with all the necessary sound pairs.
    for (SoundPair pair : soundPairs) {
      List<IMusicNote> value = new LinkedList<>();
      notesMap.put(pair, value);
    }

    //Edits map by placing notes into entries with matching SoundPair keys, overriding the
    //initial values.
    for (IMusicNote note : notesList) {
      SoundPair key = new SoundPair(note.getPitch(), note.getOctave());
      notesMap.get(key).add(note);
    }

    return notesMap;
  }

  /**
   * Computes a line of the note headings to run in header line of the console visualization.
   *
   * @return the header line for the console visualization, not including the time column.
   */
  public String headerLine() {
    List<SoundPair> soundPairs = soundPairs(this.lowestSound, this.highestSound);
    StringBuilder headings = new StringBuilder(initFirstColumn(this.totalDuration));
    for (SoundPair pair : soundPairs) {
      String toAdd;
      String pitchString = pair.getPitch().toString();
      String octaveString = String.valueOf(pair.getOctave());
      if (pitchString.contains("#")) {
        toAdd = " " + pitchString + octaveString + " ";
      } else {
        toAdd = "  " + pitchString + octaveString + " ";
      }
      headings.append(toAdd);
    }
    return headings.toString();
  }

  /**
   * Computes the size of the first column in the console visualization, under which the time
   * labels will be printed.
   *
   * @return a String of spaces whose width is that which the first column will be.
   */
  private String initFirstColumn(int totalDuration) {
    int totalTime = (int) Math.ceil(totalDuration);

    String buffer = "";
    for (int i = 0; i < String.valueOf(totalTime).length(); i += 1) {
      buffer += " ";
    }
    return buffer;
  }

  /**
   * Computes the minimum necessary pairings of pitch and octave necessary for the console
   * visualization.
   *
   * @param low  the lowest MusicNote in the range of sounds to display.
   * @param high the highest MusicNote in the range of sounds to display.
   * @return a List of the SoundPairs to be printed to the console.
   */
  public static List<SoundPair> soundPairs(IMusicNote low, IMusicNote high) {
    List<SoundPair> out = new ArrayList<>();

    firstToLast:
    for (int oct = low.getOctave(); oct <= high.getOctave(); oct += 1) {
      for (PitchType pitch : PitchType.values()) {
        boolean tooEarly = (oct == low.getOctave()) && (pitch.compareTo(low.getPitch()) < 0);
        boolean tooLate = (oct == high.getOctave()) && (pitch.compareTo(high.getPitch()) > 0);

        if (tooEarly) {
          continue;
        } else if (tooLate) {
          break firstToLast;
        } else {
          SoundPair toAdd = new SoundPair(pitch, oct);
          out.add(toAdd);
        }
      }
    }

    return out;
  }
}
