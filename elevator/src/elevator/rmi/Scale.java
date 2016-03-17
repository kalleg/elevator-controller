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
 * The remote interface for controlling a floor indicator (a scale) of an
 * elevator via RMI stub.
 * <p>The interface is implemented by the class
 * <code>elevator.rmi.impl.ScaleImpl</code>. An object with the
 * <code>ScaleImpl</code> class is created to set (or to get) a value to the
 * scale of one elevator with a given number.
 * <p>The <code>Scale</code> interface can be used to set or get a
 * value of the scale. The <code>getScale(int elevatorNumber)</code>
 * static method of the <code>elevator.rmi.MakeAll</code> class is invoked to
 * get a remote reference to the Scale object of the
 * given elevator.
 * For example:
 * <p><blockquote><pre>
 *     MakeAll.init(localhost);
 *     ...
 *    int elevatorNumber = 3;
 *    Scale scale = MakeAll.getScale(elevatorNumber);
 *    Motor m = MakeAll.getMotor(elevatorNumber);
 *    double where = m.whereIs();
 *    m.up();
 *    do {
 *      scale.setScalePosition((int)where);
 *      sleep(10);
 *    } while ((where = m.whereIs()) < 4.888)
 *    m.stop();
 *    scale.setScalePosition(5);
 * </pre></blockquote>
 * It is worth noting that an object with the <code>elevator.rmi.Scales</code>
 * interface allows controlling all scales, a group of scales or one scale,
 * whereas an object with the <code>elevator.rmi.Scale</code> class allows
 * controlling only one scale.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Scales
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */
public interface Scale extends Remote {
  /**
   * Get a scale position (a level number) from this scale.
   *
   * @return an integer level number set to the scale
   * @exception RemoteException if failed to execute
   * @see     #setScalePosition(int) setScalePosition
   */
  public int getScalePosition() throws RemoteException;
  /**
   * Set a scale position (a level number) to this scale.
   * @param level the integer value, i.e. a floor number, to be set to this
   *            scale
   * @exception IllegalParamException if <code>level</code> is not a
   * 		legal floor number
   * @exception RemoteException if failed to execute
   * @see     #getScalePosition() getScalePosition
   */
  public void setScalePosition(int level) throws RemoteException, IllegalParamException;
}