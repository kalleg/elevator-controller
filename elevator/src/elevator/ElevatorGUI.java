package elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
 /**
  * Provides GUI for the Elevators application. The "View" part of the MVC application.
   * Receives action events from the application timer (clock) which generates the events
   * at a predefined rate controlled with the special JSlider gauge.
   * On each action event (time step), the ElevatorGUI object calls the ElevatorGraphics
   * object to perform an animation step, i.e. to inspect the status of the Model
   * (all elevators: motors, doors and scales) and to display
   * a new state (by repainting the ElevatorCanvas) if the state has changed
   * since the previous step.
  */

public class ElevatorGUI extends JFrame implements ActionListener, ChangeListener {
  /**
   * An array of "arrow up" floor buttons
   */
  public static JButton[] arrowUpButton;
  /**
   * An array of "arrow down" floor buttons
   */
  public static JButton[] arrowDownButton;
  /**
   * An array of inside panel buttons
   */
  public static JButton[][] insideButton;
  /**
   * Slider used to control elevators' velocity (delay of the Timer)
   */
  public static JSlider speedSlider;
  /**
   * A reference to the i/o object of the application that provides input and
   * output and an interface to the elevators model (motors, doors and scales)
   */
  public static ElevatorIO io;
  /**
   * The application timer services as a clock that generates action events at a predefined rate
   * controlled with the JSlider gauge.
   * On each action event (time step), the ElevatorGUI object calls the ElevatorGraphics
   * object to perform an animation step, i.t. to inspect the status of the Model
   * (all elevators: motors, doors and scales) and to display a new state
   * (by repainting the ElevatorCanvas)
   *  if the state has changed since the previous stemp.
   */
  public static Timer timer;

  Elevators elevators;
  ElevatorGraphics graphics;
  boolean animateAllAtOnce = true; // animate all elevators at time tick
  boolean frozen = true;
  boolean first = true;
  int number = 0; // number of an elevator to animate
  // get gloval variables
  int numberOfElevators = Elevators.numberOfElevators;
  int numberOfFloors = Elevators.numberOfFloors;
  int topFloor = Elevators.topFloor;
  /**
   * Initial value set to the rate slider controlling the Timer.
   */
  public static final int SLIDER_INIT = 50;
  /**
   * Timer delay step
   */
  public static int delayStep = 5;
  /**
   * Timer delay value. Initialized to (101 - SLIDER_INIT) * delayStep.
   * Computed as (101- value_of_slider) * delayStep
   */
  private static int delay = (101 - SLIDER_INIT) * delayStep;
  /**
   * Velocity of the elevator in floors/milisec
   */
  public static double velocity = Elevators.step / delay;
  /**
   * Creates a graphical user interface of the Elevator Application,
   * creates an object with the ElevatorEvents class to listen for action
   * events from buttons and to print action commands carried in the events,
   * creates an object with the ElevatorGraphics used to animate elevators,
   * creates an array of objects with the ElevatorCanvas class used to display
   * elevators,
   * creates and starts the Timer object that clocks animation in the
   * ElevatorGraphics object,
   * creates a object with the ElevatorIO class to handle application input/output
   * via standard input/output or TCP sockets or/and Java RMI in separate threads.
   */
  public ElevatorGUI(String name, Elevators elevators) {
    this.elevators = elevators;
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {;}
    setTitle(name);
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    io = new ElevatorIO(elevators);
    ElevatorEvents listener = new ElevatorEvents(io.out);
    graphics = new ElevatorGraphics(elevators);
    addWindowListener(listener);
    // create arrays for widgets
    ElevatorCanvas[] elevatorCanvas = new ElevatorCanvas[numberOfElevators + 1];
    JProgressBar[] elevatorGauge = new JProgressBar[numberOfElevators + 1];
    insideButton = new JButton[numberOfElevators + 1][numberOfFloors + 1];
    arrowUpButton = new JButton[numberOfFloors + 1];
    arrowDownButton = new JButton[numberOfFloors + 1];
    // load images (ArrowUp.gif and ArrowDown.gif) for floor buttons
    boolean imageButton = true;
    ImageIcon arrowUpButtonIcon = null,
              arrowDownButtonIcon = null;
    URL imageURL = ClassLoader.getSystemResource("images/ArrowUp.gif");
    if (imageURL != null) {
      arrowUpButtonIcon = new ImageIcon(imageURL);
    } else {
      imageButton = false;
    }
    imageURL = ClassLoader.getSystemResource("images/ArrowDown.gif");
    if (imageURL != null) {
      arrowDownButtonIcon = new ImageIcon(imageURL);
    } else {
      imageButton = false;
    }
    // now we are ready to build panels
    // create a panel with elevators views (each with canvas, inside controls)
    JButton jbutton;
    JPanel p1, p2, p3, p4, p5;
    JProgressBar gauge;
    Dimension insideControlsPanelSize = new Dimension(0, 0);
    p1 = new JPanel(); // elevators panel
    p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
    for (int i = 0; i < numberOfElevators; i++) {
      p2 = new JPanel(); // elevator panel
      p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
      p2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Elevator " + (i + 1)));
      // elevator canvas (one per elevator)
      ElevatorCanvas canvas = elevatorCanvas[i] = new ElevatorCanvas();
      p2.add(canvas);
      elevators.allElevators[i].Setwin(canvas);
      // floor gauge (one per elevator)
      p3 = new JPanel();
      p3.setLayout(new BorderLayout());
      p3.setBackground(Color.gray);
      gauge = elevatorGauge[i] = new JProgressBar(JProgressBar.VERTICAL, 0, topFloor);
      gauge.setValue(0);
      gauge.setString(String.valueOf(gauge.getValue()));
      gauge.setStringPainted(true);
      gauge.setBorder(BorderFactory.createRaisedBevelBorder());
      elevators.allElevators[i].Setscale(gauge);
      p3.add(gauge, "West");
      // inside panels (one per elevator) with a buttons
      p4 = new JPanel(new BorderLayout());
      int ncols = 2;
      int nrows = (numberOfFloors + 1) / 2 + (numberOfFloors + 1) % 2;
      p5 = new JPanel(new GridLayout(nrows, ncols));
      p5.setBackground(Color.gray);
      // inside buttons
      for (int n = numberOfFloors - 1, m = topFloor; n >= 0 ; n--, m--) {
          jbutton = insideButton[i][n] = (m == 0)?
            new JButton("BV") :  new JButton(String.valueOf(m));
          jbutton.setBackground(Color.gray);
          jbutton.setActionCommand("p "+ (i + 1) + " "+ n);
          jbutton.addActionListener(listener);
          p5.add(jbutton);
          //group.add(jbutton);
      }
      p4.add(p5, "Center");
      // Stop inside button
      jbutton = insideButton[i][numberOfFloors] = new JButton("Stop");
      jbutton.setBackground(Color.gray);
      jbutton.setActionCommand("p "+ (i + 1) + " " + Elevators.SPECIAL_FOR_STOP);
      jbutton.addActionListener(listener);
      p4.add(jbutton, "South");
      p3.add(p4, "Center");
      p2.add(p3);
      insideControlsPanelSize = p3.getPreferredSize();
      p1.add(p2); // add the elevator panel
    }
    contentPane.add(p1, "Center"); // the elevators panel
    // create outside controls panel (Floor buttons, and speed controller)
    p1 = new JPanel(); // outside controls panel
    p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
    p2 = new JPanel(); // floor buttons panel
    p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
    p2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Floor Buttons"));
    for (int i = numberOfFloors - 1; i >= 0 ; i--) {
      p3 = new JPanel(); // floor panel
      p3.setBorder(BorderFactory.createRaisedBevelBorder());
      p3.add(new JLabel((i == 0)? "BV" : String.valueOf(i)));
      if (i > 0) {
        if (imageButton) {
          jbutton = arrowDownButton[i] = new JButton(arrowDownButtonIcon);
          jbutton.setPreferredSize(
            new Dimension(arrowDownButtonIcon.getIconWidth() + 10, jbutton.getPreferredSize().height)
          );
        } else
          jbutton = arrowDownButton[i] = new JButton("Down");
        jbutton.setActionCommand("b "+ i + " -1");
        jbutton.addActionListener(listener);
        p3.add(jbutton);
      }
      if (i < topFloor) {
        if (imageButton) {
          jbutton = arrowUpButton[i] = new JButton(arrowUpButtonIcon);
          jbutton.setPreferredSize(
            new Dimension(arrowUpButtonIcon.getIconWidth() + 10, jbutton.getPreferredSize().height)
          );
        } else
          jbutton = arrowUpButton[i] = new JButton("Up");
        jbutton.setActionCommand("b "+ i + " 1");
        jbutton.addActionListener(listener);
        p3.add(jbutton);
      }
      p2.add(p3);
    }
    p1.add(p2);
    p2 = new JPanel(); // speed panel
    p2.setBorder(BorderFactory.createRaisedBevelBorder());
    p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
    speedSlider = new JSlider(JSlider.VERTICAL, 0, 100, SLIDER_INIT);
    speedSlider.addChangeListener(this);
    speedSlider.setMajorTickSpacing(20);
    speedSlider.setMinorTickSpacing(10);
    speedSlider.setPaintTicks(true);
    speedSlider.setPaintLabels(true);
    speedSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
    p2.add(speedSlider);
    p2.setPreferredSize(insideControlsPanelSize);
    p1.add(p2);
    contentPane.add(p1, "East"); // add floor controls panel
    //Set up a timer that calls this object's action handler.
    timer = new Timer(delay, this);
    timer.setInitialDelay(delay);
    System.err.println("Starting UI and IO");
    pack();
    if (delay > 0) startAnimation();
    setVisible(true);
    io.setPriority(io.getPriority() + 1);
    io.start();
  }
  /**
   * Invoked from the Timer object (that services as a clock)
   * with a predefined rate controlled with a JSlider guage
   */
  public void actionPerformed(ActionEvent e) { // from the timer
    if (animateAllAtOnce) graphics.animateAll();
    else {
      graphics.animate(number);
      number++;
      if (number >= numberOfElevators) number = 0;
    }
  }
  /**
   * Gets a new value from the JSlider gauge used to control the Timer rate,
   * computes and sets a new rate to the Timer and restarts the Timer with the new
   * rate. Invoked when the Timer rate is changed with the JSlider gauge.
   */
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      int value = (int)source.getValue();
      delay = (101 - value) * delayStep;
      velocity = (double)(Elevators.step / delay);
      io.out.println("v " + velocity);
      if (delay == 0) {
        if (!frozen) stopAnimation();
      } else {
        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        if (first) startAnimation();
        else restartAnimation();
      }
    }
  }
  public void startAnimation() {
    //Start timer!
    timer.start();
    frozen = false;
    first = false;
  }
  public void stopAnimation() {
    //Stop timer!
    timer.stop();
    frozen = true;
  }
  public void restartAnimation() {
    //Restart timer!
    timer.restart();
    frozen = false;
  }
}