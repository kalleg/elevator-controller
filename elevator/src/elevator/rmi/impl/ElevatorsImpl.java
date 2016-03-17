package elevator.rmi.impl;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import elevator.ElevatorIO;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.IllegalParamException;
import elevator.rmi.RemoteActionListener;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The class <code>ElevatorsImpl</code> implements the remote interface
 * <code>elevator.rmi.Elevators</code> that is used for controlling motors,
 * scales (level indicators) and doors of all elevator via RMI.
 * The <code>Elevators</code> remote interface can be used to start moving
 * elevators up or down, to close/open doors, to set/get values
 * of the elevator scales, to get current positions
 * of the elevators.
 * The <code>getElevators()</code> static method
 * of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote reference to the
 * <code>Elevators</code> object.
 * For example:
 * <p><blockquote><pre>
 *    MakeAll.init(localhost);
 *     ...
 *    Elevators elevators = MakeAll.getElevators();
 *    ...
 *    // move the 3rd elevator up to the 5th floor
 *    double where = elevators.whereIs(3);
 *    elevators.up(3);
 *    do {
 *      elevators.setScalePosition(3, (int)where);
 *      sleep(10);
 *    } while ((where = elevators.whereIs(3)) < 4.888)
 *    elevators.stop(3);
 *    elevators.setScalePosition(3, 5);
 *    elevators.open(3);
 *    sleep(3000);
 *    elevators.close(3);
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Elevators
 * @see     elevator.rmi.Motors
 * @see     elevator.rmi.Doors
 * @see     elevator.rmi.Scales
 */
public class ElevatorsImpl extends UnicastRemoteObject implements elevator.rmi.Elevators {

  private ElevatorIO io = ElevatorGUI.io;
  int numberOfElevators = Elevators.numberOfElevators;
    /**
     * Allocates a new <code>ElevatorsImpl</code> object that represents
     * all components (motors, scales, doors and inside buttons) of all elevators.
     *
     * @exception  RemoteException  if the object cannot be created
     */
  public ElevatorsImpl() throws RemoteException {
    super();
  }
  // MotorsImpl
  /**
   * Start moving all elevators down. If not stoped, the elevators stop
   * automatically when they reach the bottom floor.
   *
   * @throws RemoteException if failed to execute
   * @see     #down(int)
   * @see     #down(int[])
   * @see     #move(int[])
   */
  public void down() throws RemoteException {
    io.motorRMI(0, Elevators.DOWN);
  }
  /**
   * Start moving an elevator down. If not stoped, the elevator stops automatically
   * when it reaches the bottom floor.
   *
   * @param number the integer number (1, 2, ...) of the elevator to start
   *            moving down
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #down(int[])
   * @see     #down()
   * @see     #move(int[],int[])
   */
  public void down(int number) throws RemoteException, IllegalParamException  {
    io.motorRMI(number, Elevators.DOWN);
  }
  /**
   * Start moving a group of elevators down. If not stoped, the elevators stop
   * automatically when they reach the bottom floor.
   *
   * @param number the array of integer numbers of elevators to start
   *            moving down
   * @exception IllegalParamException if any elevator number from the
   *            <code>number</code> array is not a legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #down(int)
   * @see     #down()
   * @see     #move(int[],int[])
   */
  public void down(int[] number) throws RemoteException, IllegalParamException  {
    for (int i = 0; i < number.length; i++) io.motorRMI(number[i], Elevators.DOWN);
  }
  /**
   * Start moving or stop all elevators .
   *
   * @param command the array of commands to start moving or to stop
   *            elevators. The length of the <code>command</code> array must be
   *            not less than the number of elevators. A command can be either
   *            of <code>Motors.UP</code>, <code>Motors.DOWN</code>,
   *            <code>Motors.STOP</code> which can be alternatively specified as
   *            1, -1, 0, respectively.
   * @exception IllegalParamException if some element in the
   *            <code>command</code> array is not a legal command, or the
   *            length of the <code>command</code> array is less than the
   *            number of elevators.
   * @throws RemoteException if failed to execute
   * @see     #move(int[], int[])
   * @see     #up()
   * @see     #down()
   * @see     #stop()
   */
  public void move(int[] command) throws RemoteException, IllegalParamException {
    if (command.length < numberOfElevators) throw new IllegalParamException();
    else
      for (int i = 0; i < numberOfElevators; i++) io.motorRMI(i, command[i]);
  }
  /**
   * Start moving or stop a group of elevators.
   *
   * @param number the array of integer numbers of elevators to be moved or stopped
   * @param command the array of commands to start moving or to stop
   *            the elevators. The length of the <code>command</code> array must be
   *            not less than the length of <code>number</code>.
   *            A command can be either of <code>Motors.UP</code>,
   *            <code>Motors.DOWN</code>, <code>Motors.STOP</code> which can be
   *            alternatively specified as 1, -1, 0, respectively.
   * @exception IllegalParamException if some element in the
   *            <code>command</code> array is not a legal command, or some
   *            element in the <code>number</code> array is not a legal elevator
   *            number, or the length of the <code>command</code> array is
   *            less than the length of the <code>number</code> array.
   * @throws RemoteException if failed to execute
   * @see     #move(int[])
   * @see     #up(int[])
   * @see     #down(int[])
   * @see     #stop(int[])
   */
  public void move(int[] number, int[] command) throws RemoteException, IllegalParamException {
    if (number.length > numberOfElevators) throw new IllegalParamException();
    else if (number.length > command.length) throw new IllegalParamException();
    else
      for (int i = 0; i < number.length; i++) io.motorRMI(number[i], command[i]);
  }
  /**
   * Stop all elevators.
   * @exception RemoteException
   * @see     #stop(int)
   * @see     #stop(int[])
   * @see     #move(int[])
   */
  public void stop() throws RemoteException {
    io.motorRMI(0, Elevators.STOP);
  }
  /**
   * Stop an elevator with a given number.
   *
   * @param number the integer number (1, 2, ...) of the elevator to be stoped
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #stop(int[])
   * @see     #stop()
   * @see     #move(int[],int[])
   */
  public void stop(int number) throws RemoteException, IllegalParamException  {
    io.motorRMI(number, Elevators.STOP);
  }
  /**
   * Stop a group of elevators.
   *
   * @param number the array of integer numbers of elevators to be stopped
   * @exception IllegalParamException if some element in from the
   *            <code>number</code> array is not a legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #stop(int)
   * @see     #stop()
   * @see     #move(int[],int[])
   */
  public void stop(int[] number) throws RemoteException, IllegalParamException  {
    for (int i = 0; i < number.length; i++) io.motorRMI(number[i], Elevators.STOP);
  }
  /**
   * Start moving all elevators up. If not stoped, the elevators stop
   * automatically when they reach the top floor.
   *
   * @throws RemoteException if failed to execute
   * @see     #up(int)
   * @see     #up(int[])
   * @see     #move(int[])
   */
  public void up() throws RemoteException {
    io.motorRMI(0, Elevators.UP);
  }
  /**
   * Start moving an elevator up. If not stoped, the elevator stops
   * automatically when it reaches the top floor.
   *
   * @param number the integer number (1, 2, ...) of the elevator to start
   *            moving up
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #up(int[])
   * @see     #up()
   * @see     #move(int[],int[])
   */
  public void up(int number) throws RemoteException, IllegalParamException {
    io.motorRMI(number, Elevators.UP);
  }
  /**
   * Start moving a group of elevators up. If not stoped, the elevators stop
   * automatically when they reach the top floor.
   *
   * @param number the array of integer numbers of elevators to start
   *            moving up
   * @exception IllegalParamException if any elevator number from the
   *            <code>number</code> array is not a legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #up(int)
   * @see     #up()
   * @see     #move(int[],int[])
   */
  public void up(int[] number) throws RemoteException, IllegalParamException {
    for (int i = 0; i < number.length; i++) io.motorRMI(number[i], Elevators.UP);
  }
  /**
   * Get current positions of all elevators.
   *
   * @return    an array of <code>double</code> positions of the elevators.
   * @throws RemoteException if failed to execute
   * @see     #whereAre(int[])
   * @see     #whereIs(int)
   */
  public double[] whereAre() throws RemoteException {
    return io.whereAre();
  }
  /**
   * Get current positions of a group of elevators.
   *
   * @param number an array of integer numbers (1, 2, ...) of elevators
   * @return    an array of <code>double</code> positions of the elevators.
   * @exception IllegalParamException if any of <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute   *
   * @see     #whereAre()
   * @see     #whereIs(int)
   */
  public double[] whereAre(int[] number) throws RemoteException {
    double[] r = new double[number.length];
    for (int i = 0; i < number.length; i++) r[i] = io.whereIs(number[i]);
    return r;
  }
  /**
   * Get a current position of an elevator.
   *
   * @param number the integer number (1, 2, ...) of the elevator
   * @return    a <code>double</code> position of the elevator in "floors".
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #whereAre(int[])
   * @see     #whereAre()
   */
  public double whereIs(int number) throws RemoteException, IllegalParamException  {
    return io.whereIs(number);
  }
  // DoorsImpl
  /**
   * Close all doors.
   *
   * @exception RemoteException if failed to execute
   * @see     #close(int)
   * @see     #close(int[])
   */
  public void close() throws RemoteException {
    io.doorRMI(0, Elevators.CLOSE);
  }
  /**
   * Close a door of an elevator with a given number.
   *
   * @param number the integer number (1, 2, ...) of the door (elevator) to close
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #close()
   * @see     #close(int[])
   */
  public void close(int number) throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.CLOSE);
  }
  /**
   * Close doors of a group of elevators with given numbers.
   *
   * @param number the array of integer numbers of doors (elevators) to close
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #close()
   * @see     #close(int)
   */
  public void close(int[] number) throws RemoteException, IllegalParamException {
    for (int i = 0; i < number.length; i++) io.doorRMI(number[i], Elevators.CLOSE);
  }
  /**
   * Open all doors.
   *
   * @exception RemoteException  if failed to execute
   * @see     #open(int)
   * @see     #open(int[])
   */
  public void open() throws RemoteException {
    io.doorRMI(0, Elevators.OPEN);
  }
  /**
   * Open a door of an elevator with a given number.
   *
   * @param number the integer number (1, 2, ...) of the door (elevator) to open
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #open()
   * @see     #open(int[])
   */
  public void open(int number) throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.OPEN);
  }
  /**
   * Open doors of a group of elevators with given numbers.
   *
   * @param number the array of integer numbers of doors (elevators) to open
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #open()
   * @see     #open(int)
   */
  public void open(int[] number) throws RemoteException, IllegalParamException {
    for (int i = 0; i < number.length; i++) io.doorRMI(number[i], Elevators.OPEN);
  }
  /**
   * Open/close all doors.
   *
   * @param command the array of integer commands (open, close) to all doors.
   *          The length of <code>command</code> must be not less than the total
   *          number of doors (elevators). A command can be specified either
   *          <code>Doors.OPEN</code> or <code>Doors.CLOSE</code>. A command can
   *          be also specified as 1 or -1, respectively.
   * @exception IllegalParamException if some element in the
   *            <code>command</code> array is not a legal command
   * @exception RemoteException if failed to execute
   * @see     #operate(int[], int[])
   * @see     #OPEN
   * @see     #CLOSE
   */
  public void operate(int[] command) throws RemoteException, IllegalParamException {
    if (command.length < numberOfElevators) throw new IllegalParamException();
    for (int i = 0; i < command.length; i++) io.doorRMI(i, command[i]);
  }
  /**
   * Open/close a group of doors.
   *
   * @param command the array of integer commands (open, close) to all doors.
   *          The length of <code>command</code> must be not less than the
   *          number of doors specified in array <code>number</code>.
   *          A command can be specified either <code>Doors.OPEN</code>
   *          or <code>Doors.CLOSE</code>. A command can
   *          be also specified as 1 or -1, respectively.
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number or/and
   *            some element in <code>command</code> is not a legal command,
   *            or the length of the <code>command</code> array is
   *            less than the length of the <code>number</code> array.
   * @exception RemoteException if failed to execute
   * @see     #operate(int[])
   * @see     #OPEN
   * @see     #CLOSE
   */
  public void operate(int[] number, int[] command) throws RemoteException, IllegalParamException{
    if (number.length > numberOfElevators) throw new IllegalParamException();
    else if (number.length > command.length) throw new IllegalParamException();
    else for (int i = 0; i < number.length; i++) io.doorRMI(number[i], command[i]);
  }
  // ScalesImpl
  /**
   * Get scale positions (level numbers) from all scales.
   *
   * @return an array of integer level numbers set to all scales
   * @exception RemoteException if failed to execute
   * @see     #getScalePosition(int)
   * @see     #getScalePosition(int[])
   */
  public int[] getScalePosition() throws RemoteException {
    int[] p = new int[numberOfElevators];
    for (int i = 0; i < numberOfElevators; i++) p[i] = io.getScalePosition(i);
    return p;
  }
  /**
   * Get a scale position (a level number) from the scale of a specified elevator.
   *
   * @return an integer level number set to the scale of the elevator.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @see     #getScalePosition()
   * @see     #getScalePosition(int[])
   */
  public int getScalePosition(int number) throws RemoteException, IllegalParamException {
    return io.getScalePosition(number);
  }
  /**
   * Get scale positions from a group of scales specified by the
   * array <code>number</code>
   *
   * @return an array of integer level numbers set to the group of scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @see     #getScalePosition()
   * @see     #getScalePosition(int)
   */
  public int[] getScalePosition(int[] number) throws RemoteException, IllegalParamException {
    if (number.length > numberOfElevators) throw new IllegalParamException();
    int[] p = new int[number.length];
    for (int i = 0; i < number.length; i++) p[i] = io.getScalePosition(number[i]);
    return p;
  }
  /**
   * Set a specified value to scales of all elevators.
   *
   * @param level the integer values to be set to scales of
   *            all elevators.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int level) throws RemoteException, IllegalParamException {
    for (int i = 0; i < numberOfElevators; i++) io.scaleRMI(i, level);
  }
  /**
   * Set specified values to scales of all elevators.
   *
   * @param level the array of integer values to be set to scales
   *        of all elevators.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in <code>level</code> is not a
   * 		legal floor number
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] level) throws RemoteException, IllegalParamException {
    if (level.length != numberOfElevators) throw new IllegalParamException();
    else for (int i = 0; i < numberOfElevators; i++) io.scaleRMI(i, level[i]);
  }
  /**
   * Set specified values to scales of a group of elevators.
   *
   * @param number the array of numbers of scales (elevators) to be set
   * @param level the integer value to be set to the specified scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number, or some element in <code>number</code> is not a
   * 		legal elevator number.
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] number, int level)
      throws RemoteException, IllegalParamException {
    if (number.length > numberOfElevators) throw new IllegalParamException();
    else for (int i = 0; i < number.length; i++) io.scaleRMI(number[i], level);
  }
  /**
   * Set a specified value to scales of a group of elevators.
   *
   * @param number the array of numbers of scales (elevators) to be set
   * @param level the array of integer values to be set to the scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in <code>level</code> is not a
   * 		legal floor number, or some element in <code>number</code> is not a
   * 		legal elevator number, or length of the <code>level</code> array
   *            is less than the length of the <code>number</code> array
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] number, int[] level)
      throws RemoteException, IllegalParamException  {
    if (number.length > numberOfElevators) throw new IllegalParamException();
    else if (number.length > level.length) throw new IllegalParamException();
    else for (int i = 0; i < number.length; i++) io.scaleRMI(number[i], level[i]);
  }
  /**
   * Set a specified scale position (level) to a scale of the specified elevator.
   *
   * @param number the integer number of the scale (elevator) to be set
   * @param level the integer value to be set to the specified scale.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number, or <code>number</code> is not a
   * 		legal elevator number.
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   */
  public void setScalePosition(int number, int level)
      throws RemoteException, IllegalParamException {
    io.scaleRMI(number, level);
  }
}

