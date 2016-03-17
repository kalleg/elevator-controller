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
 * The remote interface for controlling a motor of an elevator
 * via RMI stub. The interface is implemented by the class
 * <code>elevator.rmi.impl.MotorImpl</code>. An object with the
 * <code>MotorImpl</code> class is created to control one elevator with
 * a given number.
 * <p>The <code>Motor</code> interace can be used to start moving
 * the elevator up or down, to stop the elevator and to test a current position
 * of the elevator. The <code>getMotor(int elevatorNumber)</code> static method
 * of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote
 * reference to the Motor object of a given elevator.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     int elevatorNumber = 3;
 *     Motor m = MakeAll.getMotor(elevatorNumber);
 *     m.up();
 *     while (m.whereIs() < 2.888) sleep(10);
 *     m.stop();
 * </pre></blockquote>
 * It worth noting that an object with the <code>elevator.rmi.Motor</code>
 * interface allows controlling only one motor, whereas an object with the
 * <code>elevator.rmi.Motors</code> interface that allows controlling all motors
 * at once, a group of motors or one motor.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll  MakeAll
 * @see     elevator.rmi.Motors   Motors
 * @see     elevator.rmi.Elevator Elevator
 * @see     elevator.rmi.Elevators  Elevators
 */

public interface Motor extends Remote {
  /**
   * Start this motor to move the elevator down. If not stopped, the elevator
   * stops automatically when it reaches the bottom floor.
   *
   * @throws RemoteException if failed to execute
   * @see #up() up
   * @see #stop() stop
   * @see #whereIs() whereIs
   */
  public void down() throws RemoteException;
  /**
   * Stop this motor (stop the elevator).
   *
   * @throws RemoteException if failed to execute
   * @see #up() up
   * @see #down() down
   * @see #whereIs() whereIs
   */
  public void stop() throws RemoteException;
  /**
   * Start this motor to move the elevator up. If not stopped, the elevator
   * stops automatically when it reaches the top floor.
   * @exception RemoteException
   * @see #down() down
   * @see #stop() stop
   * @see #whereIs() whereIs
   */
  public void up() throws RemoteException;
  /**
   * Returs a current position of the elevator moved with this motor.
   * @return    a <code>double</code> position of the elevator in "floor units".
   *            For example, the value 2.5 "floor units" means that the elevator
   *            is located exactly in between the 2nd and 3rd floors.
   * @throws RemoteException if failed to execute
   * @see #down() down
   * @see #stop() stop
   * @see #up() up
   */
  public double whereIs() throws RemoteException;
}