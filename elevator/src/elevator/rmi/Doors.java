package elevator.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */

/**
 * The remote interface for controlling doors of elevators
 * via RMI stub.
 * <p>The interface is implemented by the proxy class
 * <code>elevator.rmi.impl.DoorsImpl</code>. An object with the
 * <code>DoorsImpl</code> class is created to control doors of elevators.
 * <p>The <code>elevator.rmi.Doors</code> interface can be used to open or to
 * close all doors, a group of doors or one door. The <code>getDoors()</code>
 * static method of the <code>elevator.rmi.MakeAll</code> class is invoked to
 * get a remote reference to the <code>elevator.rmi.Doors</code> object.
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
 * It is worth noting that an object with the <code>elevator.rmi.Doors</code>
 * inteface allows controlling all doors, a group of doors or one door, whereas
 * an object with the <code>elevator.rmi.Door</code> class allows controlling
 * only one door.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Door
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */
public interface Doors extends Remote {
  /**
    * The command to close a door(s).
    */
  public static final int CLOSE = -1;
  /**
    * The command to open a door(s).
    */
  public static final int OPEN = 1;
  /**
   * Close all doors.
   * @exception RemoteException if failed to execute
   * @see     #close(int)
   * @see     #close(int[])
   */
  public void close() throws RemoteException;
  /**
   * Close a door of an elevator with a given number.
   * @param number the integer number (1, 2, ...) of the door (elevator) to close
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #close()
   * @see     #close(int[])
   */
  public void close(int number) throws RemoteException, IllegalParamException;
  /**
   * Close doors of a group of elevators with given numbers.
   * @param number the array of integer numbers of doors (elevators) to close
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @exception RemoteException if failed to execute
   * @see     #close()
   * @see     #close(int)
   */
  public void close(int[] number) throws RemoteException, IllegalParamException;
  /**
   * Open all doors.
   * @exception RemoteException  if failed to execute
   * @see     #open(int)
   * @see     #open(int[])
   */
  public void open() throws RemoteException;
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
  public void open(int number) throws RemoteException, IllegalParamException;
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
  public void open(int[] number) throws RemoteException, IllegalParamException;
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
  public void operate(int[] command) throws RemoteException, IllegalParamException;
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
  public void operate(int[] number, int[] command) throws RemoteException, IllegalParamException;
}