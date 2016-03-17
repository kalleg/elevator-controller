package elevator.rmi.impl;

import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import elevator.Elevators;
import elevator.ElevatorGUI;
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
 * The helper class that implements the
 * <code>javax.swing.event.ChangeListener</code> interface for receiving change
 * events from an the elevators' velocity slider.
 * An object with the <code>VelocityListener</code> class forwards the
 * change (action) events via RMI to a remote listener with the
 * <code>RemoteActionListener</code> remote interface.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 */
public class VelocityListener implements ChangeListener {

  private RemoteActionListener listener;
  private JSlider slider;
    /**
     * Allocates a new <code>VelocityListener</code> used to receive an event
     *  from the velocity slider, include a current velocity of elevators
     *  to a newly created ActionEvent and forward the event to a remote listener.
     * @param listener The <code>RemoteActionListener</code> to which
     *    this <code>VelocityListener</code> must forward action events
     *    from the velocity slider
     */
  public VelocityListener(RemoteActionListener listener) {
    this.listener = listener;
    slider = ElevatorGUI.speedSlider;
    if (slider != null) slider.addChangeListener(this);
  }
  /**
   * Invoked when the velocity has changed by the velocity slider
   * <code>InsideListener</code>
   */
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      int value = (int)source.getValue();
      int delay = (101 - value) * ElevatorGUI.delayStep;
      String velocity = "v " + (float)(Elevators.step / delay);
      try {
        listener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, velocity));
      } catch (RemoteException ex) {
        removeListener();
      }
    }
  }
  private void removeListener() {
    System.err.println("Got RemoteException while calling back to a Velocity listener");
    System.err.println("Removing the listener");
    slider.removeChangeListener(this);
    listener = null; // hopefully will be gc-ed also
  }
}