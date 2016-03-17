package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import elevator.ElevatorGUI;
import elevator.Elevators;
import elevator.rmi.RemoteActionListener;


/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The helper class <code>FloorListener</code> that implements the
 * <code>java.awt.event.ActionListener</code> interface for receiving action
 * events from floor buttons.
 * An object with the <code>FloorListener</code> class forwards the
 * action events via RMI to a remote listener with the
 * <code>RemoteActionListener</code> remote interface.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see java.awt.event.ActionListener
 * @see java.awt.event.ActionEvent
 */
public class FloorListener implements ActionListener {
  private RemoteActionListener listener;
  private int floor;
  private int numberOfFloors = Elevators.numberOfFloors;
  private JButton[] arrowDownButton = ElevatorGUI.arrowDownButton;
  private JButton[] arrowUpButton = ElevatorGUI.arrowUpButton;
    /**
     * Allocates a new <code>FloorButtonListener</code> used to receive events
     *  from floor buttons and to forward the events to a remote listener.
     * @param floor the relative number of the floor (0, 1, ...) where the buttons are located.
     * @param listener The <code>RemoteActionListener</code> to which
     *    this <code>FloorButtonListener</code> must forward action events
     *    from the button
     */
  public FloorListener(int floor, RemoteActionListener listener) {
    this.floor = floor;
    this.listener = listener;
    if (floor == numberOfFloors) { // special case: one listener for all buttons
      for (int i = numberOfFloors - 1; i >= 0; i--) {
        if (arrowDownButton != null && arrowDownButton[i] != null)
          arrowDownButton[i].addActionListener(this);
        if (arrowUpButton != null && arrowUpButton[i] != null)
          arrowUpButton[i].addActionListener(this);
      }
    } else { // only one floor
      if (arrowDownButton != null && arrowDownButton[floor] != null)
        arrowDownButton[floor].addActionListener(this);
      if (arrowUpButton != null && arrowUpButton[floor] != null)
        arrowUpButton[floor].addActionListener(this);
    }
  }
  /**
   * Invoked when an floor button is pressed. The action
   * event is forwarded to the remote listener of this
   * <code>FloorButtonListener</code>
   */
  public void actionPerformed(ActionEvent e) {
    try {
      listener.actionPerformed(e);
    } catch (RemoteException ex) {
      System.err.println("Got RemoteException while calling back to a listener");
      System.err.println("Removing the listener");
      if (floor == numberOfFloors) {
        for (int i = numberOfFloors - 1; i >= 0; i--) {
          if (arrowDownButton[i] != null)
            arrowDownButton[i].removeActionListener(this);
          if (arrowUpButton[i] != null)
            arrowUpButton[i].removeActionListener(this);
        }
      } else { // one floor
        if (arrowDownButton[floor] != null)
          arrowDownButton[floor].removeActionListener(this);
        if (arrowUpButton[floor] != null)
          arrowUpButton[floor].removeActionListener(this);
      }
      listener = null; // hopefully will be gc-ed also
    }
  }
}