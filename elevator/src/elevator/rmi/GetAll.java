package elevator.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import elevator.rmi.IllegalParamException;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The remote interface for obtaining references to remote objects
 * of the Elevators application used for controlling elevator components: motors,
 * doors, scales, and for making button listeners which forward action events
 * from the buttons to remote listeners via the <code>RemoteActionListener</code>
 * interface.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Motor
 * @see     elevator.rmi.Door
 * @see     elevator.rmi.Scale
 * @see     elevator.rmi.Motors
 * @see     elevator.rmi.Doors
 * @see     elevator.rmi.Scales
 * @see     elevator.rmi.Elevator
 * @see     elevator.rmi.Elevators
 */
public interface GetAll extends Remote {
  /**
   * Returns an object with the <code>Door</code> interface which is
   *    used for controlling a door of the elevator with the given number.
   * @param number An integer number of elevator whose <code>Door</code>
   *     to get.
   * @return An object with the <code>Door</code> interface.
   * @throws RemoteException if failed to get/create an <code>Door</code> object.
   * @see elevator.rmi.Door
   */
  public Door getDoor(int number) throws RemoteException;
  /**
   * Returns an array of objects with the <code>Door</code>
   *    interface which is used for controlling a door of the elevator with the
   *    given number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Door</code> to get.
   * @return An array of objects with the <code>Door</code> interface.
   * @throws RemoteException if failed to get/create <code>Door</code> objects.
   * @see elevator.rmi.Door
   */
  public Door[] getDoor(int[] number) throws RemoteException;
  /**
   * Returns an object with the <code>Doors</code> interface that
   *    is used for controlling doors of elevators via Java RMI.
   * @return An object with the <code>Doors</code> interface.
   * @throws RemoteException if failed to get/create an <code>Doors</code> object.
   * @see elevator.rmi.Doors
   */
  public Doors getDoors() throws RemoteException;
  /**
   * Returns an object with the <code>Elevator</code> interface
   *    that is used for controlling all components (the motor, the door, and
   *    the scale) of the elevator with the given number
   *    via Java RMI.
   * @param number The integer number of elevator whose <code>Elevator</code> to get.
   * @return An object with the <code>Elevator</code> interface.
   * @throws RemoteException if failed to get/create an <code>Elevator</code> object.
   * @see elevator.rmi.Elevator
   */
  public Elevator getElevator(int number) throws RemoteException;
  /**
   * Returns an array of objects with the <code>Elevator</code>
   *    interface which is used for controlling all components (a motor, a door,
   *    a scale) of an elevators with a given
   *    number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Elevator</code> to get.
   * @return An array of objects with the <code>Elevator</code> interface.
   * @throws RemoteException if failed to get/create <code>Elevator</code> objects.
   * @see elevator.rmi.Elevator
   */
  public Elevator[] getElevator(int[] number) throws RemoteException;
  /**
   * Returns an object with the <code>Elevators</code> interface that is
   *    used for controlling all components (motors, doors, scales) of all
   *    elevators via Java RMI.
   * @return An object with the <code>Elevators</code> interface.
   * @throws RemoteException if failed to get/create an <code>Elevators</code> object.
   * @see elevator.rmi.Elevators
   */
  public Elevators getElevators() throws RemoteException;
  /**
   * Returns the velocity of an elevator in "floor units" per millisecond.
   * @return A double velocity of an elevator in "floor units" per millisecond.
   * @throws RemoteException is failed to execute
   */
  public double getVelocity() throws RemoteException;
  /**
   * Returns the total number of elevators.
   * @return An interger number of elevators.
   * @throws RemoteException is failed to execute
   */
  public int getNumberOfElevators() throws RemoteException;
  /**
   * Returns the total number of floors.
   * @return An interger number of floors.
   * @throws RemoteException is failed to execute
   */
  public int getNumberOfFloors() throws RemoteException;
  /**
   * Returns an object with the <code>Motor</code> interface which is
   *    used for controlling a motor of the elevator with the given number
   *    via Java RMI.
   * @param number An integer number of elevator whose <code>Motor</code>
   *      to get.
   * @return An object with the <code>Motor</code> interface.
   * @throws RemoteException if failed to get/create an <code>Motor</code> object.
   * @see elevator.rmi.Motor
   */
  public Motor getMotor(int number) throws RemoteException;
  /**
   * Returns an array of objects with the <code>Motor</code> interface which is
   *    used for controlling a motor of the elevator with the given number
   *    via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Motor</code> to get.
   * @return An array of objects with the <code>Motor</code> interface.
   * @throws RemoteException if failed to get/create <code>Motor</code> objects.
   * @see elevator.rmi.Motor
   */
  public Motor[] getMotor(int[] number) throws RemoteException;
  /**
   * Returns an object with the <code>Motors</code> interface that is
   *    used for controlling motors of elevators via Java RMI.
   * @return An object with the <code>Motors</code> interface.
   * @throws RemoteException if failed to get/create an <code>Motors</code> object.
   * @see elevator.rmi.Motors
   */
  public Motors getMotors() throws RemoteException;
  /**
   * Returns an object with the <code>Scale</code> interface which is
   *    used for controlling a scale of the elevator with the given number
   *    via Java RMI.
   * @param number An integer number of elevator whose <code>Scale</code>
   *     to get.
   * @return An object with the <code>Scale</code> interface.
   * @throws RemoteException if failed to get an <code>Scale</code> object.
   * @see elevator.rmi.Scale
   */
  public Scale getScale(int number) throws RemoteException;
  /**
   * Returns an array of objects with the <code>Scale</code>
   *    interface which is used for controlling a scale of the elevator with
   *    the given number via Java RMI.
   * @param number An array of integer numbers of elevators whose
   *     <code>Scale</code> to get.
   * @return An array of objects with the <code>Scale</code> interface.
   * @throws RemoteException if failed to get/create <code>Scale</code> objects.
   * @see elevator.rmi.Scale
   */
  public Scale[] getScale(int[] number) throws RemoteException;
  /**
   * Returns an object with the <code>Scales</code> interface that is
   *    used for controlling scales of elevators via Java RMI.
   * @return An object with the <code>Scales</code> interface.
   * @throws RemoteException if failed to get/create an <code>Scales</code> object.
   * @see elevator.rmi.Scales
   */
  public Scales getScales() throws RemoteException;
  /**
   * Returns the number of the top floor.
   * @return An interger number of the top floor.
   * @throws RemoteException is failed to execute
   */
  public int getTopFloor() throws RemoteException;
  /**
   * Creates <code>FloorListener</code> that receives action events
   * from floor buttons located at a given floor and forwards the events to the
   * specified action listener via the listener's <code>RemoteActionListener</code> remote
   * interface.
   * @param floor is the relative number (0, 1, ...) of the floor whose
   *  <code>FloorListener</code> to make
   * @param listener is <code>RemoteActionListener</code> to forward events to
   * @throws RemoteException if failed to make an
   *    <code>InsideListener</code> object.
   * @see elevator.rmi.RemoteActionListener
   */
  public void makeFloorListener(int floor, RemoteActionListener listener) throws RemoteException;
  /**
   * Creates <code>InsideListener</code> that receives action events
   * from inside panel buttons of a given elevator and forwards the events to the
   * specified action listener via the listener's <code>RemoteActionListener</code> remote
   * interface.
   * @param number is the relative number (0, 1, ...) of the elevator whose
   *  <code>InsideListener</code> to make
   * @param listener is <code>RemoteActionListener</code> to forward events to
   * @throws RemoteException if failed to make an
   *    <code>InsideListener</code> object.
   * @see elevator.rmi.RemoteActionListener
   */
  public void makeInsideListener(int number, RemoteActionListener listener) throws RemoteException;
  /**
   * Creates <code>PositionListener</code> that receives action events
   * from the elevator timer and forwards the events with a current position of
   * an elevator with the given number to the
   * specified action listener via the listener's <code>RemoteActionListener</code> remote
   * interface.
   * @param number is the relative number (0, 1, ...) of the elevator whose
   *  <code>InsideListener</code> to make
   * @param listener is <code>RemoteActionListener</code> to forward events to
   * @throws RemoteException if failed to make an
   *    <code>PositionListener</code> object.
   * @see elevator.rmi.RemoteActionListener
   */
  public void makePositionListener(int number, RemoteActionListener listener) throws RemoteException;
  /**
   * Creates <code>VelocityListener</code> that receives an action event
   * when the velocity of elevators has changed by the velocity slider.
   * @param listener is <code>RemoteActionListener</code> to forward events to
   * @throws RemoteException if failed to make an
   *    <code>PositionListener</code> object.
   * @see elevator.rmi.RemoteActionListener
   */
  public void makeVelocityListener(RemoteActionListener listener) throws RemoteException;
}