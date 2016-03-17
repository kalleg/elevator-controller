package elevator;

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
 * Provides animation of elevators at a rate of the amination Timer.
 * The <code>animate</code> or the <code>aminateAll</code>
 * methods are called at the rate set to the animation Timer of the ElevatorGUI object.
 * The methods check whether the state of the elevator(s) Model has changed and
 * if so, requests the ElevatorCanvas object(s) to repaint the elevator(s) with the new state.
 * How to animate, one elevator at a time step (with the animate method) or
 * all elevators at a time step (with the animateAll method) is controlled by
 * a "hard-coded" boolean variable <code>animateAllAtOnce</code> defined in the ElevatorGUI class.
 * Maybe this should be controlled with a command line option (to be fixed).
 * Now the option is set to true that means that the animateAll methos is called
 * each time step to update the view of ALL elevators rather than one at a time.
 */
public class ElevatorGraphics {
  // get values of some global variables
  int numberOfElevators = Elevators.numberOfElevators;
  int numberOfFloors = Elevators.numberOfFloors;
  int topFloor = Elevators.topFloor;
  // own
  private Elevator[] allElevators;
  private java.io.PrintStream out;
  /**
   * Creates an object with the ElevatorGraphics class, gets a reference to array
   * of Elevator objects (the model of elevators) and the ElevatorIO object.
   * @param elevators a reference to the object of the Elevators primary class
   */
  public ElevatorGraphics(Elevators elevators) {
    allElevators = elevators.allElevators;
    out = ElevatorIO.out;
  }
  /**
   * Gets a current state of one elevator and compares with the old (previous) state,
   * requests to repaint the elevator if the old state has changed, stores the new
   * state as the old state. Invoked on an event from the application Timer of the
   * ElevatorGUI object.
   * @param number is the elevator number to be animated
   * @see #animateAll()
   */
  public void animate(int number) { // only elevator with the current number
    double position;
    int stat, boxdir, doordir;
    // to guarantee atomicity of this read-modify-write
    synchronized (allElevators[number].motorLock) {
      boxdir = allElevators[number].Getdir();
      if(boxdir != Elevators.STOP){
	  //get position;
	  position = allElevators[number].Getpos();
	  //modify position;
	  position = position + (double)(boxdir * Elevators.step); // OBS fixed ? !!!;
	  //contol position;
	  if(position < 0.0) {
            boxdir = Elevators.STOP;
            position = (double)0.0;
	  }
	  if(position > topFloor) {
	      boxdir = Elevators.STOP;
	      position = topFloor;
	  }
	  //write back position and direction;
	  allElevators[number].Setpos(position);
	  allElevators[number].Setdir(boxdir);
      }
    }
    // to guarantee atomicity of this read-modify-write
    synchronized (allElevators[number].doorLock) {
      doordir = allElevators[number].Getdoor();
      if(doordir != Elevators.STOP){
          //get doorstat;
	  stat = allElevators[number].Getdoorstat();
	  //contol door;
	  if(stat == DoorStatus.CLOSED && doordir == Elevators.CLOSE) {
	      doordir = Elevators.STOP;
	  }
	  if(stat == DoorStatus.OPEN4 && doordir == Elevators.OPEN) {
	      doordir = Elevators.STOP;
	  }
	  //modify door;
	  stat = stat + doordir;
	  //write back doorstat and doordir;
	  allElevators[number].Setdoorstat(stat);
	  allElevators[number].Setdoor(doordir);
      }
    }
    if(doordir != Elevators.STOP || boxdir != Elevators.STOP) presentation(number);
    scalePresentation(number);
  }
  /**
   * Gets a current state of all elevator (one by one) and compare with the old (previous) state,
   * requests to repaint the elevator if the old state has changed, stores the new
   * state as the old state. Invoked on an event from the application Timer of the
   * ElevatorGUI object.
   * @param number is the elevator number to be animated
   * @see #animate(int)
   */
  public void animateAll() { // animate all elevators
    for (int i = 0; i < numberOfElevators; i++) {
      animate(i);
    }
  }
  /**
   * Requests the corresponding ElevatorCanvas to repaint the given elevator
   * according to its current (new) state,
   * prints a current position of the elevator to the output stream (standard or
   * socket)
   */
  private void presentation(int number) {
    double position = allElevators[number].Getpos();
    int stat = allElevators[number].Getdoorstat();
    ElevatorCanvas window = (ElevatorCanvas)allElevators[number].Getwin();
    window.showElevator(position, stat);
    if (Elevators.posOutput)
      out.println("f " + (number + 1) + " " + position);
  }
  /**
   * Presents a (new) value of the scale of the elevator with the given number
   * on the corresponding JProgressBar
   * @param number the integer number of the elevator whose scale value to present
   * on the JProgressBar
   */

  private void scalePresentation(int number) {
    int scalePosition  = allElevators[number].Getscalepos();
    javax.swing.JProgressBar scale = (javax.swing.JProgressBar)allElevators[number].Getscale();
    if (scale.getValue() != scalePosition)  {
      scale.setValue(scalePosition);
      scale.setString(String.valueOf(scalePosition));
    }
  }
}