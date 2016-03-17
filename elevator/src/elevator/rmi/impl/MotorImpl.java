package elevator.rmi.impl;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import elevator.ElevatorGUI;
import elevator.ElevatorIO;
import elevator.Elevators;
import elevator.rmi.Motor;

/**
 * The class <code>MotorImpl</code> implements the
 * <code>elevator.rmi.Motor</code> remote interface.
 * An object with this class represents a motor of one elevator
 * and is used to control the motor of the elevator and to test position of
 * the elevator via RMI. The object servers as a proxy to the motor of the
 * elevator.
 * <p>An object with this class is created
 * when a thread invokes the <code>getMotor</code> static method on the
 * <code>elevator.rmi.MakeAll</code> class with the elevator number (1, 2, ...)
 * as a parameter. The calling thread gets a RMI stub of the
 * <code>MotorImpl</code> object with the <code>Motor</code> interface used to
 * control the motor of the given elevator. For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     int elevatorNumber = 3;
 *     Motor m = MakeAll.getMotor(elevatorNumber);
 *     m.up();
 *     while (m.whereIs() < 2.888) sleep(10);
 *     m.stop();
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Motor
 */

public class MotorImpl extends UnicastRemoteObject implements Motor {
  private int number;
  private ElevatorIO io = ElevatorGUI.io;
    /**
     * Allocates a new <code>MotorImpl</code> object that represents
     * a motor of an elevator with the given elevator number.
     *
     * @param   elevatorNumber   the number of the elevator whose
     *                           motor is represented by this object.
     * @exception  RemoteException  if the motor object cannot be created
     */
  public MotorImpl(int elevatorNumber) throws RemoteException {
    super();
    this.number = elevatorNumber;
  }
  /**
   * Start this motor to move the elevator down. If not stopped, the elevator
   * stops automatically when it reaches the bottom floor.
   *
   * @throws RemoteException if failed to execute
   * @see #up() up
   * @see #stop() stop
   * @see #whereIs() whereIs
   */
  public void down() throws RemoteException { // corresponds to "m number -1"
    io.motorRMI(number, Elevators.DOWN);
  }
  /**
   * Stop this motor (stop the elevator).
   *
   * @throws RemoteException if failed to execute
   * @see #up() up
   * @see #down() down
   * @see #whereIs() whereIs
   */
  public void stop() throws RemoteException { // corresponds to "m number 0"
    io.motorRMI(number, Elevators.STOP);
  }
  /**
   * Start this motor to move the elevator up. If not stopped, the elevator stops
   * automatically when it reaches the top floor.
   *
   * @exception RemoteException
   * @see #down() down
   * @see #stop() stop
   * @see #whereIs() whereIs
   */
  public void up() throws RemoteException { // corresponds to "m number 1"
    io.motorRMI(number, Elevators.UP);
  }
  /**
   * Returs a current position of the elevator moved with this motor.
   *
   * @return    a <code>double</code> position of the elevator in "floor units".
   *            For example, the value 2.5 "floor units" means that the elevator
   *            is located exactly in between the 2nd and 3rd floors.
   * @throws RemoteException if failed to execute
   * @see #down() down
   * @see #stop() stop
   * @see #up() up
   */
  public double whereIs() throws RemoteException { // corresponds to "w number"
    return io.whereIs(number);
  }
}