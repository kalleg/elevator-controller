package elevator.rmi.impl;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import javax.swing.JButton;
import elevator.ElevatorIO;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.Elevator;
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
 * The class <code>ElevatorImpl</code> implements the remote interface
 * <code>elevator.rmi.Elevator</code> that is used for controlling a motor,
 * a scale (level indicator) and a door of an elevator via RMI.
 * The <code>Elevator</code> remote interface can be used to start moving the
 * elevator up or down, to close/open the elevator's door, to set/get a value
 * of the scale of the elevator, to get a current position
 * of the elevator.
 * The <code>getElevator()</code> static method
 * of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote reference to the
 * <code>Elevator</code> object.
 * For example:
 * <p><blockquote><pre>
 *    MakeAll.init(localhost);
 *     ...
 *    Elevator e3 = MakeAll.getElevator(3);
 *    double where = e3.whereIs();
 *    e3.up();
 *    do {
 *      e3.setScalePosition((int)where);
 *      sleep(10);
 *    } while ((where = e3.whereIs()) < 4.999)
 *    e3.stop();
 *    e3.setScalePosition(5);
 *    e3.open();
 *    sleep(3000);
 *    e3.close();
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Motor
 * @see     elevator.rmi.Door
 * @see     elevator.rmi.Scale
 */
public class ElevatorImpl extends UnicastRemoteObject implements Elevator {
  private int number;
  private ElevatorIO io = ElevatorGUI.io;
    /**
     * Allocates a new <code>ElevatorImpl</code> object that represents
     * all components (motor, scale, door) of an elevator
     * with the given number.
     * @param elevatorNumber The integer number of the elevator
     * @exception  RemoteException  if the object cannot be created
     */
  public ElevatorImpl(int elevatorNumber) throws RemoteException {
    super();
    number = elevatorNumber;
  }
  public void down() throws RemoteException { // corresponds to "m number -1"
    io.motorRMI(number, Elevators.DOWN);
  }
  public void stop() throws RemoteException { // corresponds to "m number 0"
    io.motorRMI(number, Elevators.STOP);
  }
  public void up() throws RemoteException { // corresponds to "m number 1"
    io.motorRMI(number, Elevators.UP);
  }
  public double whereIs() throws RemoteException { // corresponds to "w number"
    return io.whereIs(number);
  }
  public void close() throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.CLOSE);
  }
  public void open() throws RemoteException, IllegalParamException {
    io.doorRMI(number, Elevators.OPEN);
  }
  public int getScalePosition() throws RemoteException {
    return io.getScalePosition(number);
  }
  public void setScalePosition(int level) throws RemoteException, IllegalParamException {
    io.scaleRMI(number, level);
  }
}
