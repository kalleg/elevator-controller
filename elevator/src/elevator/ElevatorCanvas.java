package elevator;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * ElevatorCanvas is a panel used to display a current state of one elevator.
 * It repaints the cabin on a signal from the timer if
 * the elevator has to be repainted, i.e. when it is moving upwards
 * or downwards (i.e. its y-position is changing over the time)
 * or/and its door is closing or opening.
 */
public class ElevatorCanvas extends JPanel {
  // we need for graphics
  /**
   * The floor push width (actually not used here) because the graphics has changed.
   * Let's keep it.
   */
  protected static int FLOORPUSHWIDTH = 25;
  /**
   * The elevator width used to compute some geometry along the x-axis
   */
  protected static int ELEVATORWIDTH;
  /**
   * The elevator height used to compute some geometry and
   * a position of elevator along the y-axis
   */
  protected static int ELEVATORHEIGHT;
  /**
   * The floor height (actually equal to ELEVATORHEIGHT).
   */
  protected static int FLOORHEIGHT;
  /**
   * Six elevator images used to aminate a moving elevator and opening/closing
   * door.
   */
  protected static Image[] elevatorImages = new Image[5];

  { // need to load elevator images (static)
    MediaTracker tracker = new MediaTracker(new JPanel());
    for (int i = 0; i < elevatorImages.length; i++ ) {
      String imageFileName = "images/elevatordoorsopen" + i + ".gif";
      URL imageURL = ClassLoader.getSystemResource(imageFileName);
      if (imageURL != null) {
        elevatorImages[i] = Toolkit.getDefaultToolkit().getImage(imageURL);
        tracker.addImage(elevatorImages[i], 0);
      } else {
        System.err.println("Fatal error: Image file " + imageFileName + " not found");
        System.exit(1);
      }
    }
    try {
      tracker.waitForAll();
    } catch (java.lang.InterruptedException e) {;}
    // assume equal size
    ELEVATORWIDTH = elevatorImages[0].getWidth(this);
    ELEVATORHEIGHT = FLOORHEIGHT = elevatorImages[0].getHeight(this);
    System.out.println(FLOORHEIGHT);
  }

  private int y, x, stat = 0;
  int topFloor = Elevators.topFloor;
  int numberOfFloors = Elevators.numberOfFloors;
  private boolean first = true;
  double step = Elevators.step;
/**
 * Creates an instance of ElevatorCanvas that is used to display one elevator.
 */
  public ElevatorCanvas() {
    super();
    int height = Elevators.numberOfFloors * FLOORHEIGHT;
    setPreferredSize(new Dimension(ELEVATORWIDTH, height));
    setBackground(Color.green);
  }
  /**
   * Paints an elevator a current image of the elevator
   * that corresponds to the current state of elevator's door
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g); //paint background
    if (first) {
      x = (getWidth() - ELEVATORWIDTH) / 2;
      y = (getHeight() - ELEVATORHEIGHT);
      first = false;
    }
    for (int i = 1, w = this.getWidth(), h; i < numberOfFloors; i ++) {
      h = FLOORHEIGHT * i;
      g.drawLine(0, h, w, h);
    }
    g.drawImage(ElevatorCanvas.elevatorImages[stat], x, y, this);
  }
  /**
   * Invoked when the state of the elevator or its door displayed by this
   * ElevatorCanvas has changed
   */
  protected void showElevator(double pos, int stat) {
    this.y = ElevatorYCoord(pos);
    this.stat = (int)stat;
    this.repaint();
  }
  /**
   * Computes the top-left y-coordinate of the image of the elevator cabin to be repainted
   */
  private int ElevatorYCoord(double pos) {
    return (int)((double)((topFloor - pos) * FLOORHEIGHT)) ;
  }
}