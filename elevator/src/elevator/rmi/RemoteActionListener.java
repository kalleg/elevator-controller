package elevator.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.io.Serializable;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */

/**
 * The remote listener interface for receiving action events via RMI.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see java.awt.event.ActionEvent
 * @see elevator.rmi.impl.FloorListener
 * @see elevator.rmi.impl.InsideListener
 */
public interface RemoteActionListener extends Remote, Serializable {
  /**
   * Remotely invoked when an action event is fired by a source (button or timer).
   */
  public void actionPerformed(ActionEvent e) throws RemoteException;
}