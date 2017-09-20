package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import cs3500.music.model.IMusicNote;
import cs3500.music.model.IMusicPiece;
import cs3500.music.model.MusicalOperations;

/**
 * MouseListeners class. Handles mouse clicks to update model and view accordingly.
 */
public class MouseHandler implements MouseListener {
  private IController<IMusicNote, MusicalOperations<IMusicNote, IMusicPiece>> controller;

  /**
   * Creates a mouse handler object. Will operate in conjunction with given controller.
   * @param controller is the controller to which this mouse listener will work in conjunction with.
   */
  public MouseHandler(IController<IMusicNote,
          MusicalOperations<IMusicNote, IMusicPiece>> controller) {
    this.controller = controller;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // not concerned for this class
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // not concerned for this class
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    controller.configureMouseHandler(e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // not concerned for this class
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // not concerned for this class
  }
}
