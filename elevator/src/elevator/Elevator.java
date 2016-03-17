package elevator;

import javax.swing.JComponent;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * Represents a state of components of one elevator, i.e.
 * the motor, the elevator cabin, the door and the scale, and provides methods for inspecting
 * and altering the state.
 * <p>An object with this class holds the following state components:
 * <ul>
 * <li>The current state of the elevator motor which can be in one of three possible
 * states: (i) stopped, (ii) moving the cabin upwards, (iii) moving the cabin downwards.
 * <li>The current y-position of the elevator cabin.
 * <li>The current state of the door which can be in one of six different states
 * reflecting different "degree of openness" of the door from "completely open"
 * to "completely closed".
 * <li>The current direction of the movement of the door (opening, closing, still).
 * <li>The current position of the elevator scale (the level indicator)
 * </ul>
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.Elevator
 */
public class Elevator {
  /**
   * Object used to synchronize updates to the state of the motor of this elevator
   */
  protected Object motorLock = new Object();
  /**
   * Object used to synchronize updates to the state of the door of this elevator
   */
  protected Object doorLock = new Object();
  // private fields
  private int boxdir = 0;
  private int doordir = 0;
  private double boxpos = 0;
  private int scalepos = 0;
  private int doorstat = DoorStatus.CLOSED;
  private int topFloor = 0;
  private int number = 0;
  private JComponent window;
  private JComponent scale;
/**
 * Constructs an instance of <code>Elevator</code> that represents the elevator
 * with the given number. Sets the position of the elevator cabin at the bottom
 * floor, sets the value of the elevator scale to the bottom floor number.
 * @param number the integer number of the elevator represented by this <code>Elevator</code>
 */
  public Elevator(int number) {
    scalepos = 0;
    boxpos = 0.0;
    this.topFloor = Elevators.topFloor;
    this.number = number;
  }
  /**
   * Sets position of the elevator cabin to the specified double value.
   * @param f float position to be set.
   */
  public void Setpos(double f) {
    if(f < 0){
	System.err.println("In Setpos: Position out of range = " + f);
	boxpos = 0;
	return;
    }
    if(f > topFloor){
	System.err.println("In Setpos: Position out of range = " + f);
	boxpos = topFloor;
	return;
    }
    boxpos = f; //still here ?
  }
  /**
   * Sets the state of the door to the specified state
   *  (DoorStatus.OPEN1, ... DoorStatus.OPEN4, DoorStatus.CLOSED).
   * @param s the integer state code to be set.
   */
  public void Setdoorstat(int s) {
    if(s < DoorStatus.CLOSED){
	System.err.println("In Setdoorstat: Doorstatus out of range = " + s);
	doorstat = DoorStatus.CLOSED;
	return;
    }
    if(s > DoorStatus.OPEN4){
	System.err.println("In Setdoorstat: Doorstatus out of range = " + s);
	doorstat = DoorStatus.OPEN4;
	return;
    }
    doorstat = s; //still here ?
  }
  /**
   * Sets the direction of the motor to the specified direction,
   * e.g. moving downwards (-1), moving upwards (1), none = stopped (0).
   * @param d an integer code of the direction to be set.
   */
  public void Setdir(int d) {
    if(d < -1 || d > 1){
	System.err.println("In Setdir: Direction out of range = " + d);
	boxdir = 0;
    }else{
	boxdir = d;
    }
  }
  /**
   * Sets the movement direction of the door to the specified direction, e.g. closing (-1),
   * opening (1), still (0)
   * @param d the integer code of the direction to be set.
   */
  public void Setdoor(int d) {
    if(d < -1 || d > 1){
	System.err.println("In Setdoor: Direction out of range = " + d);
	doordir = 0;
    }else{
	doordir = d;
    }
  }
  /**
   * Sets the scale position to the specified value (level number).
   * @param s the integer position value to be set.
   */
  public void Setscalepos(int s) {
    if(s < 0 || s > topFloor){
	System.err.println("In Setscale: Scalevalue out of range = " + s);
    }else{
	scalepos = s;
    }
  }
  /**
   * Sets a GUI component (window) with the <code>javax.swing.JComponent</code> class used to display
   *  this <code>Elevator<code>.
   * @param w <code>JComponent</code> to be set.
   */
  public void Setwin(JComponent w) {
    window = w;
  }
  /**
   * Sets a GUI component (e.g. gauge) with the <code>javax.swing.JComponent</code>
   * class used to display the scale value of this <code>Elevator<code>.
   * @param w <code>JComponent</code> to be set.
   */
  public void Setscale(JComponent s) {
    scale = s;
  }
  /**
   * Returns the current position of the elevator cabin.
   * @return the current position of the cabin as a double value in "floor units".
   */
  public double Getpos() {
    return boxpos;
  }
  /**
   * Returns the current state of the door
   *  (DoorStatus.OPEN1, ... DoorStatus.OPEN4, DoorStatus.CLOSED).
   * @return the integer code of the current state of the door.
   */
  public int Getdoorstat() {
    return doorstat;
  }
  /**
   * Returns the current direction of movement of the elevator cabin (motor), e.g.
   *  1 (upwards), -1 (downwards), 0(stopped).
   * @return the integer code of the cabin movement direction.
   */
  public int Getdir() {
    return boxdir;
  }
  /**
   * Returns the current direction of movement of the door, e.g.
   *  1 (opening), -1 (closing), 0 (still).
   * @return the the integer code of the door movement direction.
   */
  public int Getdoor() {
    return doordir;
  }
  /**
   * Returns the current position (level) of the elevator scale.
   * @return the the integer value of the current position of the elevator scale.
   */
  public int Getscalepos() {
    return scalepos;
  }
  /**
   * Returns the GUI component with the <code>javax.swing.JComponent</code> class used to display
   *  this <code>Elevator<code>.
   * @return <code>JComponent</code> used to display this <code>Elevator<code>.
   */
  JComponent Getwin() {
    return window;
  }
  /**
   * Returns the GUI component with the <code>javax.swing.JComponent</code> class
   * used to display the value of the scale of this <code>Elevator<code>.
   * @return <code>JComponent</code> used to display this <code>Elevator<code>.
   */
  JComponent Getscale() {
    return scale;
  }
}