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
 * The remote interface for controlling floor indicators (scales) of elevators
 * via RMI stub.
 * <p>The interface is implemented by the class
 * <code>elevator.rmi.impl.ScalesImpl</code>. An object with the
 * <code>ScalesImpl</code> class is
 * created to set (or to get) values to the scales of elevators.
 * <p>The Scales interface can be used to set or get
 * value of scales of all elevators at once, a group of elevators or one
 * elevator. The <code>getScales()</code> static method of the
 * <code>elevator.rmi.MakeAll</code> class is invoked to get a remote
 * reference to the Scales object.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *    Scales scales = MakeAll.getScales();
 *    Motors motors = MakeAll.getMotors();
 *    int[] where = (int[])motors.whereAre();
 *    scales.setScalePosition(where);
 * </pre></blockquote>
 * It is worth noting that an object with the <code>elevator.rmi.Scales</code>
 * interface allows controlling all scales, a group of scales or one scale,
 * whereas an object with the <code>elevator.rmi.Scale</code> class allows
 * controlling only one scale.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Scale
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */
public interface Scales extends Remote {
  /**
   * Get scale positions (level numbers) from all scales.
   *
   * @return an array of integer level numbers set to all scales
   * @exception RemoteException if failed to execute
   * @see     #getScalePosition(int)
   * @see     #getScalePosition(int[])
   */
  public int[] getScalePosition() throws RemoteException;
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
  public int getScalePosition(int number) throws RemoteException, IllegalParamException;
  /**
   * Get scale positions (level numbers) from a group of scales specified by the
   * array <code>number</code>
   *
   * @return an array of integer level numbers set to the group of scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in the
   *            <code>number</code> array is not a legal elevator number
   * @see     #getScalePosition()
   * @see     #getScalePosition(int)
   */
  public int[] getScalePosition(int[] number) throws RemoteException, IllegalParamException;
  /**
   * Set a specified scale position (level) to scales of all elevators.
   *
   * @param level the integer floor number (1, 2, ...) to be set to scales of
   *            all elevators.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int level) throws RemoteException, IllegalParamException;
  /**
   * Set specified scale positions (levels) to scales of all elevators.
   *
   * @param level the array of integer values (floor numbers) to be set to
   *        scales of all elevators.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in <code>level</code> is
   *            not a legal floor number
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] level) throws RemoteException, IllegalParamException;
  /**
   * Set specified scale positions (levels) to scales of a group of elevators.
   *
   * @param number the array of integer numbers of scales (elevators) to be set
   * @param level the integer floor number (level) to be set to the specified
   *            scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number, or some element in <code>number</code> is not a
   * 		legal elevator number.
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] number, int level) throws RemoteException, IllegalParamException;
  /**
   * Set a specified scale position (level) to scales of a group of elevators.
   *
   * @param number the array of integer numbers of scales (elevators) to be set
   * @param level the array of integer values (floor numbers) to be set to the
   *        scales.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if some element in <code>level</code>
   *            is not a legal floor number, or some element in
   *            <code>number</code> is not a legal elevator number, or length of
   *            the <code>level</code> array is less than the length of the
   *            <code>number</code> array
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int[])
   * @see     #setScalePosition(int,int)
   */
  public void setScalePosition(int[] number, int[] level) throws RemoteException, IllegalParamException;
  /**
   * Set a specified scale position (level) to a scale of the specified elevator.
   *
   * @param number the integer number of the scale (elevator) to be set
   * @param level the integer floor number (level) to be set to the specified scale.
   * @exception RemoteException if failed to execute
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number, or <code>number</code> is not a
   * 		legal elevator number.
   * @see     #setScalePosition(int)
   * @see     #setScalePosition(int[])
   * @see     #setScalePosition(int[],int)
   * @see     #setScalePosition(int[],int[])
   */
  public void setScalePosition(int number, int level) throws RemoteException, IllegalParamException;
}