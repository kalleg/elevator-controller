package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import elevator.ElevatorIO;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.Door;
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
 * The class <code>DoorImpl</code> implements the
 * <code>elevator.rmi.Door</code> remote interface.
 * An object with this class represents a door of one elevator
 * and is used to open/close the door of the elevator.
 * The object servers as an RMI proxy to the door of the elevator.
 * <p>An object with this class is created
 * when a thread invokes the <code>getDoor</code> static method on the
 * <code>elevator.rmi.MakeAll</code> class with the elevator number (1, 2, ...)
 * as a parameter. The calling thread gets a RMI stub of the
 * <code>DoorImpl</code> object with the <code>Door</code> interface used to
 * control the door of the given elevator. For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     int elevatorNumber = 3;
 *     Door d = MakeAll.getDoor(elevatorNumber);
 *     ...
 *     d.open();
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Motor
 */
public class DoorImpl extends UnicastRemoteObject implements Door {

  private int number;
  private ElevatorIO io = ElevatorGUI.io;
    /**
     * Allocates a new <code>DoorImpl</code> object that represents
     * a door of an elevator with the given elevator number.
     *
     * @param   elevatorNumber   the number of the elevator whose
     *                           door is represented by this object.
     * @exception  RemoteException  if the door object cannot be created
     */
  public DoorImpl(int elevatorNumber) throws RemoteException {
    this.number = elevatorNumber;
  }
  public void close() throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.CLOSE);
  }
  public void open() throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.OPEN);
  }
}