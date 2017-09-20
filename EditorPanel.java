package cs3500.music.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.IReadOnlyModel;
import cs3500.music.model.Repeat;
import cs3500.music.model.SoundPair;

/**
 * Panel of the piano, and the notes displayed.
 */
public class EditorPanel extends JPanel implements IEditorPanel {
  private IReadOnlyModel<IMusicNote, IMusicPiece> composition;
  private IPianoPanel panel = new PianoPanel();
  private static final int BEAT_HEIGHT = 12;
  private static final int BEAT_WIDTH = 16;
  private int top;
  private final int y1 = 20;
  private int curBeat;
  private JScrollPane pane;

  private int startX;
  private int startY;
  private int headOffset;

  private Timer scrollTimer;
  private List<IMusicNote> compNotes;
  private List<SoundPair> soundPairs;

  private int windowHeight;
  private List<Repeat> repeats;
  private List<Integer> startRepeats = new ArrayList<>();
  private int curRepeat = 0;
  private int curEnding = 0;
  boolean useRepeats = true;

  /**
   * Creates a new panel. Will place piece of music into a JScrollPane, to allow for scrolling.
   *
   * @param model is the model whose data will be represented as a music editor
   */
  public EditorPanel(IReadOnlyModel<IMusicNote, IMusicPiece> model) {
    super();
    setModel(model);
    this.startX = 32;
    this.startY = 20;
    this.headOffset = this.startX + BEAT_WIDTH;
    this.top = headOffset;
    curBeat = 0;
    pane = new JScrollPane(this);
    initTimer();
  }

  /**
   * If there are repeats, will guide the gui view into moving back to the right spots on the
   * editor.
   */
  private void dealWithRepeats() {
    if (repeats.size() == curRepeat) {
      return ;
    }
    Repeat r = repeats.get(curRepeat);
    List<Integer> endings = r.getEndings();
    if (endings.size() == 1) {
      soloEnding(endings);
    } else {
      multipleEndings(endings);
    }
  }

  /**
   * If there is only one ending for the beginning, then standard repeat will be executed.
   * INVARIANT: the way this method is called in dealWithRepeats(), the endings always has size 1
   * @param endings is solo endings list
   */
  private void soloEnding(List<Integer> endings) {
    int oneEnd = endings.get(0);
    if (curBeat == oneEnd) {
      setCurBeat(startRepeats.get(curRepeat));
      curRepeat++;
    }
  }


  /**
   * If the repeats have multiple endings, deals with the go tos accordingly.
   * @param endings are the multiple endings of a more complex repeat.
   */
  private void multipleEndings(List<Integer> endings) {
    if (curEnding == endings.size()) {
      curRepeat++;
      return ;
    }
    int firstFakeEnding = endings.get(0);
    int firstRealEnding = endings.get(1);
    int goToLast = endings.get(endings.size() - 1);
    if (curEnding == 0) {
      if (curBeat == firstRealEnding) {
        setCurBeat(startRepeats.get(curRepeat));
        curEnding++;
      }
    } else {
      if (curBeat == firstFakeEnding) {
        setCurBeat(endings.get(curEnding));
        curEnding++;
      }
      if (curBeat == goToLast && curEnding != endings.size()) {
        setCurBeat(startRepeats.get(curRepeat));
      }
    }
  }


  /**
   * Initializes the scrollTimer with the tempo and the actions to update the view.
   */
  private void initTimer() {
    int interval = this.composition.getTempo() / 1000 + 1; //Converting microseconds to milliseconds
    scrollTimer = new Timer(interval, e -> {
      moveRedLine("right");
      panel.highlightPlayingNotes(playingAtCurBeat());
    });
  }

  /**
   * Resizes the view based on changes to the composition.
   */
  private void resize() {
    int totalRange = this.composition.totalRange();
    this.windowHeight = (totalRange * BEAT_HEIGHT) + y1;
  }

  /**
   * Paints the note headings on the panel.
   *
   * @param g the Graphics object to paint onto.
   */
  private void paintNoteHeadings(Graphics g) {
    int startAt = this.windowHeight;
    for (SoundPair soundPair : this.soundPairs) {
      g.drawString(soundPair.toString(), BEAT_WIDTH / 2, startAt);
      startAt -= BEAT_HEIGHT;
    }
  }

  /**
   * Renders the notes onto the Editor Panel.
   *
   * @param g the Graphics object to paint onto.
   */
  private void renderNotes(Graphics g) {
    for (IMusicNote note : this.compNotes) {

      for (int t = note.getStartLocation(); t <= note.getEndLocation(); t += 1) {

        int xLoc = headOffset + (t * BEAT_WIDTH);
        int yLoc = startY + (Math.abs((this.soundPairs.indexOf(note.getSoundPair())) -
                this.soundPairs.size()) * BEAT_HEIGHT) - BEAT_HEIGHT;

        paintNote(g, note, xLoc, yLoc, t);
      }
    }
  }

  /**
   * Renders the lines denoting the measures and the rows for each sound. Also renders the
   * numerical headings for each measure.
   *
   * @param g the Graphics object to paint onto.
   */
  private void renderMeasures(Graphics g) {
    g.setColor(Color.BLACK);
    int totalDuration = this.composition.getTotalDuration();

    // Vertical lines
    for (int i = 0; i <= totalDuration + 1; i += 1) {
      if (i % this.composition.getPiece().getMeasureDuration() == 0) {
        int xLoc = headOffset + (i * BEAT_WIDTH);
        g.fillRect(xLoc, startY, 2, this.composition.totalRange() *
                BEAT_HEIGHT);
        g.drawString(String.valueOf(i), xLoc, startY);
      }
    }

    // Horizontal lines + numerical markers
    for (int i = 0; i <= this.soundPairs.size() * BEAT_HEIGHT; i += BEAT_HEIGHT) {
      g.fillRect(headOffset, startY + i, (totalDuration + 1) *
              BEAT_WIDTH, 2);
    }
  }

  /**
   * Renders the red line marking the current beat in the song.
   *
   * @param g the Graphics object to paint onto.
   */
  private void renderRedLine(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(2));
    g.setColor(Color.RED);

    g.fillRect(headOffset + (BEAT_WIDTH * (this.curBeat)), startY, 2,
            this.composition.totalRange() * BEAT_HEIGHT);
  }

  /**
   * Paints note at current beat.
   *
   * @param g      Graphics object to draw.
   * @param note   is the String console representation of the note at the current beat.
   * @param startX is the x coordinate to start drawing rectangle
   * @param startY is the y coordinate to start drawing rectangle
   */
  private void paintNote(Graphics g, IMusicNote note, int startX, int startY, int beat) {
    String singleNote = note.consoleString(beat);
    if (singleNote.equalsIgnoreCase("  X  ")) {
      g.setColor(Color.BLACK);
      g.fillRect(startX, startY, BEAT_WIDTH, BEAT_HEIGHT);
    } else if (singleNote.equalsIgnoreCase("  |  ")) {
      g.setColor(Color.GREEN);
      g.fillRect(startX, startY, BEAT_WIDTH, BEAT_HEIGHT);
    } else {
      g.setColor(Color.WHITE);
      g.fillRect(startX - 30, startY, BEAT_WIDTH + 30, BEAT_HEIGHT);
      g.setColor(Color.BLACK);
      g.setFont(new Font("Serif", Font.BOLD, 10));
      g.drawString(singleNote, startX - 15, startY + 10);
    }
  }

  /**
   * Paint all the Repeats at the top to signify where they are in the piece.
   * @param g object that will do the drawing.
   */
  private void paintRepeats(Graphics g) {
    for (Repeat r: repeats) {
      paintSingleRepeat(g, r);
    }
  }

  /**
   * Paints one repeat. An "R" signifies the beginning to go when repeating.
   * The numbers represent the accompanying endings.
   * @param g object that will do the drawing
   * @param r is the repeat to be painted
   */
  private void paintSingleRepeat(Graphics g, Repeat r) {
    int start = r.getStartBeat();
    g.setFont(new Font("Times New Roman", Font.BOLD, 10));
    g.drawString("R", headOffset + BEAT_WIDTH * start, 10);
    List<Integer> endings = r.getEndings();
    for (int i = 0; i < endings.size(); i++) {
      g.drawString(String.valueOf(i + 1),
              headOffset + BEAT_WIDTH * endings.get(i), 10);
    }
  }


  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    paintNoteHeadings(g2d);
    renderNotes(g2d);
    renderMeasures(g2d);
    renderRedLine(g2d);
    paintRepeats(g2d);
    redisplayWindowForBar();
  }

  @Override
  public Dimension getPreferredSize() {
    try {
      int width = this.composition.getTotalDuration() * BEAT_WIDTH + startX;
      int height = this.soundPairs.size() * BEAT_HEIGHT + startY;

      return new Dimension(width, height);
    } catch (Exception exc) {
      return new Dimension(1600, 360);
    }
  }

  @Override
  public List<IMusicNote> playingAtCurBeat() {
    List<IMusicNote> allNotesAtCurBeat = composition.getPieceByTime().get(curBeat);
    if (allNotesAtCurBeat != null) {
      List<IMusicNote> currentlyPlaying = new ArrayList<>();
      for (IMusicNote noteAtBeat : allNotesAtCurBeat) {
        if (!noteAtBeat.consoleString(curBeat).equalsIgnoreCase("     ")) {
          currentlyPlaying.add(noteAtBeat);
        }
      }
      return currentlyPlaying;
    }
    return new ArrayList<>();
  }

  @Override
  public int getCurBeat() {
    return curBeat;
  }

  @Override
  public void setCurBeat(int beat) {
    this.curBeat = beat;
    if (curBeat == 0) {
      curRepeat = curBeat;
      curEnding = 0;
    }
    this.top = headOffset + (beat * BEAT_WIDTH);

    repaint();
  }

  @Override
  public void redisplayWindowForBar() {
    Rectangle rectangle = this.getVisibleRect();
    int xLocationOfBar = headOffset + getCurBeat() * BEAT_WIDTH;
    if (xLocationOfBar > rectangle.getWidth() + rectangle.getX()) {
      int newWindowY = (int) rectangle.getY();
      rectangle.setLocation(xLocationOfBar, newWindowY);
      scrollRectToVisible(rectangle);
    } else if (xLocationOfBar < rectangle.getX()){
      int newWindowY = (int) rectangle.getY();
      rectangle.setLocation(xLocationOfBar - (int)rectangle.getWidth(), newWindowY);
      scrollRectToVisible(rectangle);
    }
  }

  @Override
  public JScrollPane getPane() {
    return pane;
  }

  @Override
  public void moveRedLine(String direction) {

    Rectangle rect = this.getVisibleRect();

    int x = headOffset + (BEAT_WIDTH * (this.curBeat));
    int y = startY;
    int width = (int) rect.getWidth();
    int height = (int) rect.getHeight();
    int tm = this.composition.getTempo() / 1000;

    if (direction.equalsIgnoreCase("left")) {
      if (curBeat > 0) {
        this.top -= BEAT_WIDTH;
        curBeat -= 1;
        if (useRepeats) {
          dealWithRepeats();
        }
        repaint(tm, x, y, width, height);
      }
    } else {
      if (curBeat < this.composition.getTotalDuration() + 1) {
        this.top += BEAT_WIDTH;
        curBeat += 1;
        if (useRepeats) {
          dealWithRepeats();
        }
        repaint(tm, x, y, width, height);
      }
    }
  }

  @Override
  public void setModel(IReadOnlyModel<IMusicNote, IMusicPiece> newModel) {
    this.composition = newModel;
    curBeat += 1;
    top += BEAT_WIDTH;

    this.compNotes = this.composition.getPiece().getNotes();
    this.soundPairs = PrinterForConsole.soundPairs(this.composition.getPiece().lowestNote(),
            this.composition.getPiece().highestNote());
    repeats = newModel.getRepeats();
    for (Repeat r : repeats) {
      if (!startRepeats.contains(r.getStartBeat())) {
        startRepeats.add(r.getStartBeat());
      }
    }
    this.resize();
    repaint(this.getVisibleRect());
  }

  @Override
  public void scroll(IPianoPanel panel) {
    if (curBeat == composition.getTotalDuration()) {
      this.scrollTimer.stop();
    }
    this.scrollTimer.start();
    this.panel = panel;
  }

  @Override
  public void haltScroll() {
    this.scrollTimer.stop();
  }


}