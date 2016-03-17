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
 * The remote interface for controlling all motors of elevators
 * via RMI stub.
 * <p>The interface is implemented by the proxy class
 * <code>elevator.rmi.impl.MotorsImpl</code>. An object with the
 * <code>MotorsImpl</code> class is created to control all motors at once or a
 * group of motors or one motor of one elevator.
 * The <code>Motors</code> interface can be used to start moving
 * elevators up or down, to stop the elevators and to get current positions
 * of the elevators. The <code>getMotors()</code> static method
 * of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote
 * reference to the Motors object.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *     Motors motors = MakeAll.getMotors();
 *     motors.up(); // move all elevators up
 *     motors.stop(1); // stop the first elevator
 *     if (motors.whereIs(4) < 2.888)
 *        motors.stop(4); // stop the 4th elevator on the 3rd floor
 *     int[] number = {1, 3, 5};
 *     int[] pos = motors.whereAre(number);
 *     motors.up({2, 4}); // move 2nd and 4th elevators up
 *     motors.move({1, 5}, {Motors.UP, Motors.DOWN});
 * </pre></blockquote>
 * It is worth noting that an object with the <code>elevator.rmi.Motors</code>
 * interface allows controlling all motors, a group of motors or one motor,
 * whereas an object with the <code>elevator.rmi.Motor</code> interface allows
 * controlling only one motor.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Motor
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */

public interface Motors extends Remote {
  /**
    * The up command.
    */
  public static final int UP = 1;
  /**
   * The down command.
   */
  public static final int DOWN = -1;
  /**
   * Stop the elevator(s).
   */
  public static final int STOP = 0;
  /**
   * Start moving all elevators down. If not stoped, the elevators stop
   * automatically when they reach the bottom floor.
   *
   * @throws RemoteException if failed to execute
   * @see     #down(int)
   * @see     #down(int[])
   * @see     #move(int[])
   */
  public void down() throws RemoteException;
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
  public void down(int number) throws RemoteException, IllegalParamException;
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
  public void down(int[] number) throws RemoteException, IllegalParamException;
  /**
   * Start moving or stop all elevators.
   *
   * @param command the array of commands to start moving or to stop
   *            the elevators. The length of the <code>commands</code> array must be
   *            not less than the length of <code>number</code>.
   *            A command can be either of <code>Motors.UP</code>,
   *            <code>Motors.DOWN</code>, <code>Motors.STOP</code> which can be
   *            alternatively specified as 1, -1, 0, respectively.
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
  public void move(int[] command) throws RemoteException, IllegalParamException;
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
  public void move(int number[], int[] command) throws RemoteException, IllegalParamException;
  /**
   * Stop all elevators.
   * @exception RemoteException
   * @see     #stop(int)
   * @see     #stop(int[])
   * @see     #move(int[])
   */
  public void stop() throws RemoteException;
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
  public void stop(int number) throws RemoteException, IllegalParamException;
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
  public void stop(int[] number) throws RemoteException, IllegalParamException;
  /**
   * Start moving all elevators up. If not stoped, the elevators stop
   * automatically when they reach the top floor.
   *
   * @throws RemoteException if failed to execute
   * @see     #up(int)
   * @see     #up(int[])
   * @see     #move(int[])
   */
  public void up() throws RemoteException;
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
  public void up(int number) throws RemoteException, IllegalParamException;
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
  public void up(int[] number) throws RemoteException, IllegalParamException;
  /**
   * Get current positions of all elevators.
   *
   * @return    an array of <code>double</code> positions of the elevators
   *            in "floor units". For example, the value 2.5 "floor units" means
   *            that the elevator is located exactly in between the 2nd and 3rd
   *            floors.
   * @throws RemoteException if failed to execute
   * @see     #whereAre(int[])
   * @see     #whereIs(int)
   */
  public double[] whereAre() throws RemoteException;
  /**
   * Get current positions of a group of elevators.
   *
   * @param number an array of integer numbers (1, 2, ...) of elevators
   * @return    an array of <code>double</code> positions of the elevators
   *            in "floor units". For example, the value 2.5 "floor units" means
   *            that the elevator is located exactly in between the 2nd and 3rd
   *            floors.
   * @exception IllegalParamException if any of <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute   *
   * @see     #whereAre()
   * @see     #whereIs(int)
   */
  public double[] whereAre(int[] number) throws RemoteException;
  /**
   * Get a current position of an elevator.
   *
   * @param number the integer number (1, 2, ...) of the elevator
   * @return    a <code>double</code> position of the elevator in "floor units".
   *            For example, the value 2.5 "floor units" means that the elevator
   *            is located exactly in between the 2nd and 3rd floors.
   * @exception IllegalParamException if <code>number</code> is not a
   * 		legal elevator number
   * @throws RemoteException if failed to execute
   * @see     #whereAre(int[])
   * @see     #whereAre()
   */
  public double whereIs(int number) throws RemoteException, IllegalParamException;
}