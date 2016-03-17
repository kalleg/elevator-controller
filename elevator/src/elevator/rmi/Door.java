package elevator.rmi;
/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * The remote interface for controlling a door of an elevator
 * via RMI stub.
 * <p>The interface is implemented by the proxy class
 * <code>elevator.rmi.impl.DoorImpl</code>. An object with the
 * <code>DoorImpl</code> class is created to control the door of one elevator
 * with a given number.
 * <p>The <code>Door</code> interface can be used to open the
 * door or to close the door. The <code>getDoor(int elevatorNumber)</code>
 * static method of the <code>elevator.rmi.MakeAll</code> class
 * is invoked to get a remote reference to the Door object of a given elevator.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     int elevatorNumber = 3;
 *     Door d = MakeAll.getDoor(elevatorNumber);
 *     ...
 *     d.open();
 * </pre></blockquote>
 * It is worth noting that an object with the <code>elevator.rmi.Door</code> class allows
 * controlling only one door, whereas an object with the <code>elevator.rmi.Doors</code>
 * interface allows controlling all doors, a group of doors or one door.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Doors
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */

public interface Door extends Remote {
  /**
   * Close this door.
   *
   * @throws RemoteException if failed to execute
   * @see #open() open
   */
  public void close() throws RemoteException;
  /**
   * Open this door.
   *
   * @throws RemoteException if failed to execute
   * @see #close() close
   */
  public void open() throws RemoteException;
}