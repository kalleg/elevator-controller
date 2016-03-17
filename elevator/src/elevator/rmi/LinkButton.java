package elevator.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
 /**
 * The class <code>LinkButton</code> implements the
 * <code>elevator.rmi.RemoteActionListener</code> interface.
 * An object with the <code>LinkButton</code> class serves as a proxy for receiving
 * action events from a floor button(s), an inside button panel(s) or the timers
 * of the Elevators application via RMI. The LinkButton object maintains a list of listeners
 * interested in receiving action events from the button(s), the panel(s) or the
 * timer. When the LinkButton object receives an action event  it forwards the event to all
 * registered listeners. A listener class must implement the
 * <code>java.awt.event.ActionListener</code> interface.
 * Listeners interesting in receiving action commands from  inside button panels
 * or floor buttons are added to a <code>LinkButton</code> by the
 * <code>addInsideListener</code> and <code>addFloorListener</code> static
 * methods of the <code>elevator.rmi.MakeAll</code> class, respectively. A
 * position listener is added to a <code>LinkButton</code> by the
 * <code>addPositioneListener</code> method of the <code>elevator.rmi.MakeAll</code>
 * class.
 *
 * <p>A remote reference to a <code>LinkButton</code> object with the
 * <code>RemoteActionListener</code> interface is passed
 * to a <code>FloorListener</code> (<code>InsideListener</code> or code>PositionListener</code>)
 * object created at the Elevators application which "localy" receives action events
 * from a button or the timer and forwards the event via JavaRMI to the
 * <code>LinkButton</code> which, in its turn, forwards the event to the
 * subscribed listeners.
 *
 * </pre></blockquote>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see java.awt.event.ActionListener
 * @see java.awt.event.ActionEvent
 * @see elevator.rmi.RemoteActionListener
 * @see elevator.rmi.impl.FloorListener
 * @see elevator.rmi.impl.InsideListener
 */
public class LinkButton extends UnicastRemoteObject implements RemoteActionListener {

  private Vector listenerList = new Vector();
    /**
     * Creates an instance of the <code>LinkButton</code>
     */
  public LinkButton() throws RemoteException {
    super();
  }
    /**
     * Receives an <code>ActionEvent</code> from an elevator button via Java
     * RMI and passes the event to all subscribed listeners.
     * @param e the <code>ActionEvent</code> from an elevator button
     */
  public void actionPerformed(ActionEvent e) throws RemoteException {
    Vector list = (Vector)listenerList.clone();
    for (int i = 0; i < list.size(); i++)
      ((ActionListener)listenerList.elementAt(i)).actionPerformed(e);
  }
    /**
     * Adds an <code>ActionListener</code> to this button.
     * @param listener the <code>ActionListener</code> to be added
     */
  public void addActionListener(ActionListener listener) {
    listenerList.addElement(listener);
  }
    /**
     * Removes an <code>ActionListener</code> from this button.
     * @param listener the <code>ActionListener</code> to be removed
     */
  public void removeActionListener(ActionListener listener) {
    listenerList.removeElement(listener);
  }
}