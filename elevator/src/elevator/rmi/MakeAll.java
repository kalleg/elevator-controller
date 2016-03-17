package elevator.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.awt.event.ActionListener;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The <code>MakeAll</code> class provides class's methods for obtaining
 * remote references (stubs) to the objects of the Elevators application, for
 * adding event listeners interesting in receiving action
 * events (action commands) from elevator's and floor buttons, for adding position listeners
 * for monitoring positions of moving elevators and velocity listeners for monitoring
 * changes of elevators' velocity.
 * The references obtained can be used
 * for controlling different components of the Elevators such as motors, doors,
 * scales. Which interface to use depends on a level of granularity in a multithreaded
 * application that controls Elevators. According to the typical in Java name convention,
 * a static "getFoo" method of the <code>MakeAll</code> class returns a remote
 * reference (a stub) with the
 * "Foo" interface used to control a "foo" elevator component. For
 * example, the <code>getDoor(int number)</code> method returns an RMI stub with
 * the <code>Door</code> interface for controlling a door of the elevator with the
 * given number. The <code>getDoors()</code> method returns an RMI stub with
 * the <code>Doors</code> interface for controlling doors of all, a group or one
 * elevator via one stub.
 * <p>The <code>MakeAll</code> class allows obtaining references to remote
 * objects with the following remote interfaces defined in the <elevator.rmi> package:
 * <p>
 * <center><table border="1">
 * <tr><td><a href="Motor.html">Motor</a><td>To control a motor of one elevator:
 *                                            to start moving the elevator up
 *                                            or down, to stop the elevator
 *                                            and to test a current position
 *                                            of the elevator.</tr>
 * <tr><td><a href="Door.html">Door</a><td>To control a door of one elevator:
 *                                           to open/close the door.</tr>
 * <tr><td><a href="Scale.html">Scale</a><td>To access a scale (level indicator)
 *                                            of one elevator: to set/get value
 *                                            of the scale.</tr>
 * <tr><td><a href="Elevator.html">Elevator</a><td>To control all components (motor,
 *                                            door and scale) of one elevator. The
 *                                            interface extends <code>Motor</code>,
 *                                            <code>Door</code> and <code>Scale</code></tr>
 * <tr><td><a href="Motors.html">Motors</a><td>To control motors of all elevators,
 *                                             a group of elevators,
 *                                             or one elevator.</tr>
 * <tr><td><a href="Doors.html">Doors</a><td>To control doors of all elevators,
 *                                             a group of elevators,
 *                                             or one elevator.</tr>
 * <tr><td><a href="Scales.html">Scales</a><td>To access scales of all elevators,
 *                                             a group of elevators,
 *                                             or one elevator.</tr>
 * <tr><td><a href="Elevators.html">Elevators</a><td>To control all components (motors,
 *                                            doors and scales of all elevators,
 *                                            a group of elevators or one elevator.
 *                                            The interface extends <code>Motors</code>,
 *                                            <code>Doors</code> and <code>Scales</code></tr>
 * </table> </center>
 * <p>The <code>MakeAll</code> class also allows adding action listeners interesting in
 * receiving action events (action commands) from the elevators' and floor buttons,
 * and adding position listeners interested in monitoring positions of moving elevators.
 * A listener class must implement the <code>java.awt.event.ActionListener</code> interface.
 * The <code>MakeAll</code> class  provides the following methods for adding action
 * listeners:
 * <p>
 * <center><table border="1">
 * <tr><td><code>addFloorListener</code><td>Adds <code>ActionListener</code> object(s)
 *                                          to receive events from floor buttons.
 *                                          An action command that comes with an
 *                                          event is "b <i>f d</i>", where "b" stands
 *                                          for "button", <i>f</i> is
 *                                          an integer number of the floor where
 *                                          the button was pressed, <i>d</i> is
 *                                          the direction, upwards or downwards,
 *                                          assigned to the button.</tr>
 * <tr><td><code>addInsideListener</code><td>Adds an <code>ActionListener</code> object(s)
 *                                          to receive events inside button panel(s).
 *                                          An action command that comes with an
 *                                          event is "p <i>n f</i>", where "p" stands
 *                                          for "panel", <i>n</i> is
 *                                          an integer number of the elevator cabin
 *                                          where the button was pressed, <i>f</i> is
 *                                          the integer floor number
 *                                          assigned to the button.</tr>
 * <tr><td><code>addPositionListener</code><td>Adds <code>ActionListener</code> object(s)
 *                                          for monitoring positions of elevator(s).
 *                                          When an elevator starts moving,
 *                                          listener(s) receives a sequence of action
 *                                          events carrying position values
 *                                          at a predefined rate of the
 *                                          elevators' Timer. An action command
 *                                          that comes with an action event is
 *                                          "f <i>n p</i>", where "f" stands for
 *                                          "floor units", <i>n</i> is the integer
 *                                          elevator number, <i>p</i> is a float value
 *                                          of the current position of the
 *                                          moving elevator. For example, "f 2 2.5"
 *                                          means that the 2nd elevator is (was?) exactly
 *                                          in between the 2nd and the 3rd floors.</tr>
 * <tr><td><code>addVelocityListener</code><td>Adds <code>ActionListener</code> object(s)
 *                                          for monitoring changes of the elevators' velocity
 *                                          that have been made by the velocity slider.
 *                                          An action command
 *                                          that comes with an action event is
 *                                          "v <i>value</i>", where "v" stands for
 *                                          "velocity", <i>value</i> is a float value
 *                                          of the current velocity set for
 *                                          elevators' motors in "floor units" per millisecond</tr>
 * </table> </center>
 * <p>To set the name of the host where the RMI registry is located and
 * the number of the port on which the registry accepts calls, the
 * <code>init()</code> static methods of the <code>MakeAll</code>
 * class can be called once in the application, before calling any other
 * methods of the class. Both the host name and the port number are optional
 * parameters. If a host name is omitted, the name defaults to the local host.
 * If a port number is omitted, then the number defaults to the registry default
 * port number 1099.
 * For example:
 * <p><blockquote><pre>
 * public class Control extends Thread implements ActionListener {
 *   Motor motor;
 *   Door door;
 *   Scale scale;
 *   Elevators elevators;
 *  public Control() {
 *  }
 *  public void run() {
 *   try {
 *     MakeAll.init("localhost"); // actually not needed for localhost
 *     MakeAll.addFloorListener(this);
 *     MakeAll.addInsideListener(this);
 *     // monitor position of 3rd elevator
 *     MakeAll.addPositionListener(3, this);
 *     motor =MakeAll.getMotor(1);
 *     scale = MakeAll.getScale(1);
 *     door = MakeAll.getDoor(1);
 *     for (int i = 2; i >= 0; i--) {
 *       door.open();
 *       sleep(2000);
 *       door.close();
 *       sleep(3000);
 *     }
 *     elevators = MakeAll.getElevators();
 *     elevators.up();
 *     sleep(3000);
 *     elevators.stop();
 *     sleep(3000);
 *     elevators.down();
 *   } catch (Exception e) {
 *     e.printStackTrace();
 *     System.exit(1);
 *   }
 *     Object obj = new Object();
 *   synchronized (obj) {
 *     try { wait(); } catch (Exception e) {}
 *   }
 *  }
 *  public static void main(String[] args) {
 *   Control control1 = new Control();
 *   control1.start();
 *  }
 *  public void actionPerformed(ActionEvent e) {
 *   System.out.println(e.getActionCommand());
 *  }
 * }
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.Motor
 * @see     elevator.rmi.Door
 * @see     elevator.rmi.Scale
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Motors
 * @see     elevator.rmi.Doors
 * @see     elevator.rmi.Scales
 * @see     elevator.rmi.Elevators
 */

public final class MakeAll {
  /**
   * The "primary" reference to the Elevators application obtained from the RMI registry.
   */
  private static GetAll getAll = null;
  private static int numberOfElevators, numberOfFloors, topFloor;
  private static String host;
  /**
   * Private "cache" of references that might be requested
   */
  private static Door[] door = null;
  private static Motor[] motor = null;
  private static Scale[] scale = null;
  private static Doors doors = null;
  private static Motors motors = null;
  private static Scales scales = null;
  private static Elevator[] elevator = null;
  private static Elevators elevators = null;
  private static LinkButton[] insideButtons = null;
  private static LinkButton[] floorButton = null;
  private static LinkButton[] position = null;
  private static LinkButton velocity = null;
    /**
     * Disallow anyone from creating one of <code>MakeAll</code>
     */
  private MakeAll() { }
  /**
   * Adds an array of <code>ActionListener</code> to receive events from all floor buttons.
   * All listeners receive an action event when a floor button is pressed.
   * An action command that comes with the event is "b <i>f d</i>" where "b"
   * stands for "button", <i>f</i> is the integer number of the floor where the button
   * was pressed, <i>d</i> is an integer code of the direction ("-1" means downwards,
   * "1" means upwards) assigned to the button.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addFloorListener(ActionListener listener[])
        throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    for (int i = listener.length - 1; i >= 0; i --)
      if (listener[i] == null) throw new IllegalParamException();
      else addFloorListener(listener[i]);
  }
  /**
   * Adds an <code>ActionListener</code> to receive events from all floor buttons.
   * The listener receives an action event when a floor button is pressed.
   * An action command that comes with the event is "b <i>f d</i>" where "b"
   * stands for "button", <i>f</i> is the integer number of the floor where the button
   * was pressed, <i>d</i> is an integer code of the direction ("-1" means downwards,
   * "1" means upwards) assigned to the button.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addFloorListener(ActionListener listener)
        throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (floorButton == null) floorButton = new LinkButton[numberOfFloors + 1];
    // special case: one LinkButton for all floor buttons
    if (floorButton[numberOfFloors] == null) {
      floorButton[numberOfFloors] = new LinkButton();
      getAll.makeFloorListener(numberOfFloors,
          (RemoteActionListener)floorButton[numberOfFloors]);
    }
    floorButton[numberOfFloors].addActionListener(listener);
  }
  /**
   * Adds one <code>ActionListener</code> to receive events from a group
   * of floor buttons.
   * The listener receives an action event when a floor button is pressed.
   * An action command that comes with the event is "b <i>f d</i>" where "b"
   * stands for "button", <i>f</i> is the integer number of the floor where the button
   * was pressed, <i>d</i> is an integer code of the direction ("-1" means downwards,
   * "1" means upwards) assigned to the button.
   * @param floor An array of integer floor numbers where the floor buttons are located
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addFloorListener(int[] floor, ActionListener listener)
      throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (floor == null || listener == null) throw new IllegalParamException();
    for (int i = floor.length - 1; i >= 0; i--) addFloorListener(floor[i], listener);
  }
  /**
   * Adds a group of <code>ActionListener</code> to receive events from a couple of
   * floor buttons located on a given floor.
   * All listeners receive an action event when a floor button is pressed.
   * An action command that comes with the event is "b <i>f d</i>" where "b"
   * stands for "button", <i>f</i> is the integer number of the floor where the button
   * was pressed, <i>d</i> is an integer code of the direction ("-1" means downwards,
   * "1" means upwards) assigned to the button.
   * @param floor The integer number of the floor where the buttons are located
   * @param listener An array of <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addFloorListener(int floor, ActionListener listener[])
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (floor > topFloor || floor < 0) throw new IllegalParamException();
    if (floorButton == null) floorButton = new LinkButton[numberOfFloors + 1];
    if (floorButton[floor] == null) {
      floorButton[floor] = new LinkButton();
      getAll.makeFloorListener(floor, (RemoteActionListener)floorButton[floor]);
    }
    for (int j = listener.length - 1; j >= 0; j--)
      if (listener[j] == null) throw new IllegalParamException();
      else floorButton[floor].addActionListener(listener[j]);
  }
  /**
   * Adds <code>ActionListener</code> to receive events from floor buttons located
   * on a given floor.
   * The listener receives an action event when a floor button is pressed.
   * An action command that comes with the event is "b <i>f d</i>" where "b"
   * stands for "button", <i>f</i> is the integer number of the floor where the button
   * was pressed, <i>d</i> is an integer code of the direction ("-1" means downwards,
   * "1" means upwards) assigned to the button.
   * @param floor The integer number of the floor where the buttons are located
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addFloorListener(int floor, ActionListener listener)
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (floor > topFloor || floor < 0) throw new IllegalParamException();
    if (floorButton == null) floorButton = new LinkButton[numberOfFloors + 1];
    if (floorButton[floor] == null) {
      floorButton[floor] = new LinkButton();
      getAll.makeFloorListener(floor, (RemoteActionListener)floorButton[floor]);
    }
    floorButton[floor].addActionListener(listener);
  }
  /**
   * Adds a group of <code>ActionListener</code> to receive events from all
   * inside panel buttons.
   * All listeners receive an action event when a button is pressed inside an
   * elevator. An action command
   * that comes with the event is "p <i>n f</i>" where "p" stands for "panel", <i>n</i> is the
   * integer number of the elevator where the button was pressed, <i>f</i> is an integer
   * floor number assigned to the button.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addInsideListener(ActionListener listener[])
        throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    for (int i = listener.length - 1; i >= 0; i--)
      if (listener[i] == null) throw new IllegalParamException();
      else addInsideListener(listener[i]);
  }
  /**
   * Adds an <code>ActionListener</code> to receive events from all inside panel buttons.
   * The listener receives an action event when a button is pressed inside an
   * elevator. An action command
   * that comes with the event is "p <i>n f</i>" where "p" stands for "panel", <i>n</i> is the
   * integer number of the elevator where the button was pressed, <i>f</i> is an integer
   * floor number assigned to the button.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addInsideListener(ActionListener listener)
        throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (insideButtons == null) insideButtons = new LinkButton[numberOfElevators + 1];
    // special case: one LinkButton for all inside panel buttons
    if (insideButtons[numberOfElevators] == null) {
      insideButtons[numberOfElevators] = new LinkButton();
      getAll.makeInsideListener(numberOfElevators,
          (RemoteActionListener)insideButtons[numberOfElevators]);
    }
    insideButtons[numberOfElevators].addActionListener(listener);
  }
  /**
   * Adds one <code>ActionListener</code> to receive events from a group
   * of inside panel buttons.
   * The listener receives an action event when a button is pressed inside the
   * elevator. An action command
   * that comes with the event is "p <i>n f</i>" where "p" stands for "panel", <i>n</i> is the
   * integer number of the elevator where the button was pressed, <i>f</i> is an integer
   * floor number assigned to the button.
   * @param number An array of integer elevator numbers where the panels are located
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addInsideListener(int[] number, ActionListener listener)
      throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (number == null || listener == null) throw new IllegalParamException();
    for (int i = number.length - 1; i >= 0; i--) addInsideListener(number[i], listener);

  }
  /**
   * Adds a group of <code>ActionListener</code> to receive events from inside panel buttons located
   * in a given elevator.
   * A listener receives an action event when a button is pressed inside the
   * elevator. An action command
   * that comes with the event is "p <i>n f</i>" where "p" stands for "panel", <i>n</i> is the
   * integer number of the elevator where the button was pressed, <i>f</i> is an integer
   * floor number assigned to the button.
   * For the Stop button f = Elevators.SPECIAL_FOR_STOP, i.e. 32000.
   * @param number The integer number of the elevator where the button panel is located
   * @param listener An array of <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addInsideListener(int number, ActionListener listener[])
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (insideButtons == null) insideButtons = new LinkButton[numberOfElevators + 1];
    int i = number - 1;
    if (insideButtons[i] == null) {
      insideButtons[i] = new LinkButton();
      getAll.makeInsideListener(i, (RemoteActionListener)insideButtons[i]);
    }
    for (int j = listener.length - 1; j >= 0; j--)
    if (listener[j] == null) throw new IllegalParamException();
    else insideButtons[i].addActionListener(listener[j]);
  }
  /**
   * Adds one <code>ActionListener</code> to receive events from inside panel buttons located
   * in a given elevator.
   * The listener receives an action event when a button is pressed inside the
   * elevator. An action command
   * that comes with the event is "p <i>n f</i>" where "p" stands for "panel", <i>n</i> is the
   * integer number of the elevator where the button was pressed, <i>f</i> is an integer
   * floor number assigned to the button.
   * For the Stop button f = Elevators.SPECIAL_FOR_STOP, i.e. 32000.
   * @param number The integer number of the elevator where the button panel is located
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addInsideListener(int number, ActionListener listener)
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (insideButtons == null) insideButtons = new LinkButton[numberOfElevators + 1];
    int i = number - 1;
    if (insideButtons[i] == null) {
      insideButtons[i] = new LinkButton();
      getAll.makeInsideListener(i, (RemoteActionListener)insideButtons[i]);
    }
    insideButtons[i].addActionListener(listener);
  }
  /**
   * Adds a group of <code>ActionListener</code> for monitoring postions of all elevators.
   * When an elevator starts moving, all listeners receives a sequence of action
   * events carrying position values at a predefined rate of the elevators' Timer. An action command
   * that comes with an action event is "f <i>n p</i>", where "f" stands for "floor units",
   * <i>n</i> is the integer elevator number,  <i>p</i> is a float value of the current
   * position of the moving elevator.
   * @param listener an array of <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addPositionListener(ActionListener listener[])
        throws RemoteException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    for (int i = listener.length - 1; i >= 0; i --)
      if (listener[i] == null) throw new IllegalParamException();
      else addPositionListener(listener[i]);
  }
  /**
   * Adds one <code>ActionListener</code> for monitoring postions of all elevators.
   * When an elevator starts moving, the listener receives a sequence of action
   * events carrying position values at a predefined rate of the elevators' timer. An action command
   * that comes with an action event is "f <i>n p</i>", where "f" stands for "floor units",
   * <i>n</i> is the integer elevator number,  <i>p</i> is a float value of the current
   * position of the moving elevator.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @throws IllegalParamException if <code>listener</code> is null
   */
  public static void addPositionListener(ActionListener listener)
        throws RemoteException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (position == null) position = new LinkButton[numberOfElevators + 1];
    // specail case: one LinkButton for all elevators
    if (position[numberOfElevators] == null) {
      position[numberOfElevators] = new LinkButton();
      getAll.makePositionListener(numberOfElevators,
          (RemoteActionListener)position[numberOfElevators]);
    }
    position[numberOfElevators].addActionListener(listener);
  }
  /**
   * Adds one <code>ActionListener</code> for monitoring postions of a group
   * of elevators. When an elevator starts moving, the listener receives
   * a sequence of action
   * events carrying position values at a predefined rate set to the elevators' timer. An action command
   * that comes with an action event is "f <i>n p</i>", where "f" stands for "floor units",
   * <i>n</i> is the integer elevator number,  <i>p</i> is a float value of the current
   * position of the moving elevator.
   * @param number An array of integer elevator numbers
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addPositionListener(int[] number, ActionListener listener)
      throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (number == null || listener == null) throw new IllegalParamException();
    for (int i = number.length - 1; i >= 0; i--) addPositionListener(number[i], listener);

  }
  /**
   * Adds a group of <code>ActionListener</code> for monitoring postions of one
   * elevator. When the elevator starts moving, all listeners receive
   * a sequence of action
   * events carrying position values at a predefined rate set to the elevators' timer. An action command
   * that comes with an action event is "f <i>n p</i>", where "f" stands for "floor units",
   * <i>n</i> is the integer elevator number,  <i>p</i> is a float value of the current
   * position of the moving elevator.
   * @param number The integer number of the elevator
   * @param listener An array of <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addPositionListener(int number, ActionListener[] listener)
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (position == null) position = new LinkButton[numberOfElevators];
    int i = number - 1;
    if (position[i] == null) {
      position[i] = new LinkButton();
      getAll.makePositionListener(i, (RemoteActionListener)position[i]);
    }
    for (int j = listener.length - 1; j >= 0; j--)
      if (listener[j] == null) throw new IllegalParamException();
      else position[i].addActionListener(listener[j]);
  }
  /**
   * Adds one <code>ActionListener</code> for monitoring postions of one
   * elevator. When the elevator starts moving, the listener receives
   * a sequence of action
   * events carrying position values at a predefined rate set to the elevators' Timer.
   * An action command that comes with an action event is "f <i>n p</i>",
   * where "f" stands for "floor units",
   * <i>n</i> is the integer elevator number,  <i>p</i> is a float value of the current
   * position of the moving elevator.
   * @param number The integer number of the elevator
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addPositionListener(int number, ActionListener listener)
     throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (position == null) position = new LinkButton[numberOfElevators];
    int i = number - 1;
    if (position[i] == null) {
      position[i] = new LinkButton();
      getAll.makePositionListener(i, (RemoteActionListener)position[i]);
    }
    position[i].addActionListener(listener);
  }
  /**
   * Adds one <code>ActionListener</code> for monitoring chnages in elevator velocity.
   * When the velocity has changed by the velocity gauge, the listener receives
   * an action
   * events carrying current value of the velocity.
   * An action command that comes with an action event is "v <i>value</i>",
   * where "v" stands for "velocity",
   * <i>value</i> is the float value of the elevators' velocity.
   * @param listener <code>ActionListener</code> to be added.
   * @throws RemoteException if failed to add the listener
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static void addVelocityListener(ActionListener listener)
     throws RemoteException, MalformedURLException, NotBoundException  {
    if (listener == null) throw new IllegalParamException();
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (velocity == null) {
      velocity = new LinkButton();
      getAll.makeVelocityListener((RemoteActionListener)velocity);
    }
    velocity.addActionListener(listener);
  }
  /**
   * Returns an object with the <code>Door</code> interface which is
   *    used for controlling a door of the elevator with the given number.
   * @param number An integer number of elevator whose <code>Door</code>
   *     to get.
   * @return An object with the <code>Door</code> interface.
   * @throws RemoteException if failed to get an <code>Door</code> object.
   * @throws IllegalParamException if <code>number</code> is not a legal
   *    elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Door
   */
  public static Door getDoor(int number) throws RemoteException,
        IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (door == null) door = new Door[numberOfElevators];
    if (door[number - 1] == null) door[number - 1] = getAll.getDoor(number);
    return door[number - 1];
  }
  /**
   * Returns an array of objects with the <code>Door</code>
   *    interface which is used for controlling a door of the elevator with the
   *    given number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Door</code> to get.
   * @return An array of objects with the <code>Door</code> interface.
   * @throws RemoteException if failed to get <code>Door</code> objects.
   * @throws IllegalParamException if some number(s) in <code>number</code> is
   *    not a legal elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Door
   */
  public static Door[] getDoor(int[] number) throws RemoteException,
      IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number.length > numberOfElevators) throw new IllegalParamException();
    Door[] door = new Door[number.length];
    for (int i = 0; i < number.length; i++) door[i] = getDoor(i);
    return door;
  }
  /**
   * Returns an object with the <code>Doors</code> interface that
   *    is used for controlling doors of elevators via Java RMI.
   * @return An object with the <code>Doors</code> interface.
   * @throws RemoteException if failed to get an <code>Doors</code> object.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Doors
   */
  public static Doors getDoors() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (doors == null) doors = getAll.getDoors();
    return doors;
  }
  /**
   * Returns an object with the <code>Elevator</code> interface
   *    that is used for controlling all components (the motor, the door, and
   *    the scale) of the elevator with the given number
   *    via Java RMI.
   * @param number The integer number of elevator whose <code>Elevator</code> to get.
   * @return An object with the <code>Elevator</code> interface.
   * @throws RemoteException if failed to get an <code>Elevator</code> object.
   * @throws IllegalParamException if <code>number</code> is not a legal
   *    elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Elevator
   */
  public static Elevator getElevator(int number)
        throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (elevator == null) elevator = new Elevator[numberOfElevators];
    if (elevator[number - 1] == null) elevator[number - 1] = getAll.getElevator(number);
    return elevator[number - 1];
  }
  /**
   * Returns an array of objects with the <code>Elevator</code>
   *    interface which is used for controlling all components (a motor, a door,
   *    a scale) of an elevators with a given
   *    number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Elevator</code> to get.
   * @return An array of objects with the <code>Elevator</code> interface.
   * @throws RemoteException if failed to get <code>Elevator</code> objects.
   * @throws IllegalParamException if some number(s) in <code>number</code> is
   *    not a legal elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Elevator
   */
  public static Elevator[] getElevator(int[] number)
      throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number.length > numberOfElevators) throw new IllegalParamException();
    Elevator[] elevator = new Elevator[number.length];
    for (int i = 0; i < number.length; i++) elevator[i] = getElevator(i);
    return elevator;
  }
  /**
   * Returns an object with the <code>Elevators</code> interface that is
   *    used for controlling all components (motors, doors, scales) of all
   *    elevators via Java RMI.
   * @return An object with the <code>Elevators</code> interface.
   * @throws RemoteException if failed to get an <code>Elevators</code> object.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Elevators
   */
  public static Elevators getElevators() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (elevators == null) elevators = getAll.getElevators();
    return elevators;
  }
  /**
   * Returns the total number of elevators.
   * @return An interger number of elevators.
   * @throws RemoteException is failed to execute
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static int getNumberOfElevators() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    return numberOfElevators;
  }
  /**
   * Returns the total number of floors.
   * @return An interger number of floors.
   * @throws RemoteException is failed to execute
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static int getNumberOfFloors() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    return numberOfFloors;
  }
  /**
   * Returns an object with the <code>Motor</code> interface which is
   *    used for controlling a motor of the elevator with the given number
   *    via Java RMI.
   * @param number An integer number of elevator whose <code>Motor</code>
   *      to get.
   * @return An object with the <code>Motor</code> interface.
   * @throws RemoteException if failed to get an <code>Motor</code> object.
   * @throws IllegalParamException if <code>number</code> is not a legal
   *    elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Motor
   */
  public static Motor getMotor(int number) throws RemoteException, IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (motor == null) motor = new Motor[numberOfElevators];
    if (motor[number - 1] == null) motor[number - 1] = getAll.getMotor(number);
    return motor[number - 1];
  }
  /**
   * Returns an array of objects with the <code>Motor</code> interface which is
   *    used for controlling a motor of the elevator with the given number
   *    via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Motor</code> to get.
   * @return An array of objects with the <code>Motor</code> interface.
   * @throws RemoteException if failed to get <code>Motor</code> objects.
   * @throws IllegalParamException if some number(s) in <code>number</code> is
   *    not a legal elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Motor
   */
  public static Motor[] getMotor(int[] number) throws RemoteException,
        IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number.length > numberOfElevators) throw new IllegalParamException();
    Motor[] motor = new Motor[number.length];
    for (int i = 0; i < number.length; i++) motor[i] = getMotor(i);
    return motor;
  }
  /**
   * Returns an object with the <code>Motors</code> interface that is
   *    used for controlling motors of elevators via Java RMI.
   * @return An object with the <code>Motors</code> interface.
   * @throws RemoteException if failed to get an <code>Motors</code> object.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Motors
   */
  public static Motors getMotors() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (motors == null) motors = getAll.getMotors();
    return motors;
  }
  /**
   * Returns the velocity of an elevator in "floor units" per millisecond.
   * @return A float velocity of an elevator in "floor units" per millisecond.
   * @throws RemoteException is failed to execute
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   */
  public static double getVelocity() throws RemoteException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    return getAll.getVelocity();
  }
  /**
   * Returns an object with the <code>Scale</code> interface which is
   *    used for controlling a scale of the elevator with the given number
   *    via Java RMI.
   * @param number An integer number of elevator whose <code>Scale</code>
   *     to get.
   * @return An object with the <code>Scale</code> interface.
   * @throws RemoteException if failed to get an <code>Scale</code> object.
   * @throws IllegalParamException if <code>number</code> is not a legal
   *    elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Scale
   */
  public static Scale getScale(int number) throws RemoteException,
        IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number > numberOfElevators || number < 1) throw new IllegalParamException();
    if (scale == null) scale = new Scale[numberOfElevators];
    if (scale[number - 1] == null) scale[number - 1] = getAll.getScale(number);
    return scale[number - 1];
  }
  /**
   * Returns an array of objects with the <code>Scale</code>
   *    interface which is used for controlling a scale of the elevator with
   *    the given number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Scale</code> to get.
   * @return An array of objects with the <code>Scale</code> interface.
   * @throws RemoteException if failed to get <code>Scale</code> objects.
   * @throws IllegalParamException if some number(s) in <code>number</code> is
   *    not a legal elevator number.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Scale
   */
  public static Scale[] getScale(int[] number) throws RemoteException,
        IllegalParamException, MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (number.length > numberOfElevators) throw new IllegalParamException();
    Scale[] scale = new Scale[number.length];
    for (int i = 0; i < number.length; i++) scale[i] = getScale(i);
    return scale;
  }
  /**
   * Returns an object with the <code>Scales</code> interface that is
   *    used for controlling scales of elevators via Java RMI.
   * @return An object with the <code>Scales</code> interface.
   * @throws RemoteException if failed to get an <code>Scales</code> object.
   * @throws MalformedURLException if the URL of the Elevators proxy object (getALL)
   *      is not an appropriately formatted URL.
   * @throws NotBoundException is failed to obtain a reference to the getAll
   * @see elevator.rmi.Scales
   */
  public static Scales getScales() throws RemoteException,
          MalformedURLException, NotBoundException  {
    if (getAll == null) init(host, java.rmi.registry.Registry.REGISTRY_PORT);
    if (scales == null) scales = getAll.getScales();
    return scales;
  }
  /**
   * Initializes the <code>MakeAll</code> class that provide RMI access to the
   * Elevators application. The RMI registry host defaults to "localhost".
   * The RMI registry port defaults to 1099.
   * @throws RemoteException if failed contact the rmi registry.
   * @throws MalformedURLException if the name of the Elevators proxy object
   *      is not an appropriately formatted URL.
   * @throws NotBoundException if failed to get a reference to the Elevators
   *      proxy object
   */
  public static void init() throws RemoteException, MalformedURLException, NotBoundException {
    init(host, java.rmi.registry.Registry.REGISTRY_PORT);
  }
  /**
   * Initializes the <code>MakeAll</code> class that provide RMI access to the
   * Elevators application. The RMI registry host defaults to "localhost".
   * @param port The number of the port on which the RMI registry accepts calls.
   * @throws RemoteException if failed contact the rmi registry.
   * @throws MalformedURLException if the name of the Elevators proxy object
   *      is not an appropriately formatted URL.
   * @throws NotBoundException if failed to get a reference to the Elevators
   *      proxy object
   */
  public static void init(int port) throws RemoteException, MalformedURLException, NotBoundException {
    init(host, port);
  }
  /**
   * Initializes the <code>MakeAll</code> class that provide RMI access to the
   * Elevators application. The RMI registry port defaults to 1099.
   * @param host A name of the host where the RMI registry is located
   * @throws RemoteException if failed contact the rmi registry.
   * @throws MalformedURLException if the name of the Elevators proxy object
   *      is not an appropriately formatted URL.
   * @throws NotBoundException if failed to get a reference to the Elevators
   *      proxy object
   */
  public static void init(String host) throws RemoteException, MalformedURLException, NotBoundException {
    init(host, java.rmi.registry.Registry.REGISTRY_PORT);
  }
  /**
   * Initializes the <code>MakeAll</code> class that provide RMI access to the
   * Elevators application.
   * @param host A name of the host where the RMI registry is located
   * @param port The number of the port on which the RMI registry accepts calls.
   * @throws RemoteException if failed contact the rmi registry.
   * @throws MalformedURLException if the name of the Elevators proxy object
   *      is not an appropriately formatted URL.
   * @throws NotBoundException if failed to get a reference to the Elevators
   *      proxy object
   */
  public static void init(String host, int port) throws RemoteException, MalformedURLException, NotBoundException {
    if (host == null)
      try {
        host = java.net.InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
	// If that failed, at least try "" (localhost) anyway...
	host = "";
      }
    getAll = (GetAll)Naming.lookup("rmi://" + host + ":" + port + "/GetAll");
    numberOfElevators = getAll.getNumberOfElevators();
    numberOfFloors = getAll.getNumberOfFloors();
    topFloor = getAll.getTopFloor();
  }
}
