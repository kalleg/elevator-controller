package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import elevator.ElevatorIO;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.Doors;
import elevator.rmi.IllegalParamException;
/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The class <code>DoorsImpl</code> implements the remote interface
 * <code>elevator.rmi.Doors</code> used for controlling doors of elevators
 * via RMI. The <code>Doors</code> interface can be used to open or to close all doors,
 * a group of elevators or one door. The <code>getDoors()</code>
 * static method of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote
 * reference to the <code>elevator.rmi.Doors</code> object.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     Doors doors = MakeAll.getDoors();
 *     ...
 *     doors.open(); // open all doors
 *     doors.close(1); // close the first door
 *     doors.close({1, 3, 5}); // close doors 1, 3 and 5
 *     doors.operate({2, 4}, {Doors.OPEN, Doors.CLOSE}); // open door 2 and close door 4
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Door
 */
public class DoorsImpl extends UnicastRemoteObject implements Doors {
  private ElevatorIO io = ElevatorGUI.io;
  int numberOfElevators = Elevators.numberOfElevators;
    /**
     * Allocates a new <code>DoorsImpl</code> object that represents
     * doors of all elevators.
     *
     * @exception  RemoteException  if the object cannot be created
     */
  public DoorsImpl() throws RemoteException {
    super();
  }
  /**
   * Close all doors.
   * @exception RemoteException if failed to execute
   * @see     #close(int)
   * @see     #close(int[])
   */
  public void close() throws RemoteException {
    io.doorRMI(0, Elevators.CLOSE);
  }
  /**
   * Close a door of an elevator with a given number.
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
   * @param command the array of integer commands (open, close) to all doors.
   *          The length of <code>command</code> must be not less than the total
   *          number of doors (elevators). A command can be specified either
   *          <code>Doors.OPEN</code> or <code>Doors.CLOSE</code>. A command can
   *          be also specified as 1 or -1, respectively.
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #operate(int[], int[])
   * @see     #OPEN
   * @see     #CLOSE
   */
  public void operate(int[] direction) throws RemoteException, IllegalParamException {
    if (direction.length < numberOfElevators) throw new IllegalParamException();
    for (int i = 0; i < direction.length; i++) io.doorRMI(i, direction[i]);
  }
  /**
   * Open/close a group of doors.
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
  public void operate(int[] number, int[] direction) throws RemoteException, IllegalParamException{
    if (number.length > numberOfElevators) throw new IllegalParamException();
    else if (number.length > direction.length) throw new IllegalParamException();
    else for (int i = 0; i < number.length; i++) io.doorRMI(number[i], direction[i]);
  }
}