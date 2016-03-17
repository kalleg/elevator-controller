package elevator.rmi;

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
 * THe <code>IllegalParamException</code> class represents exceptions that may
 * occur during the execution (or the attempt to execute) of a method call
 * of remote interfaces used to control components of the Elevators application.
 * Most of methods (with arguments) of the remote interfaces list
 * <code>IllegalParamException</code> in their throws clauses.
 *
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 */
public class IllegalParamException extends RemoteException {
    /**
     * Constructs a <code>IllegalParamException</code> with no specified
     * detail message.
     */
  public IllegalParamException() {
    super();
  }
}