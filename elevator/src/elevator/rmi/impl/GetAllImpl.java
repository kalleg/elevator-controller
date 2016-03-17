package elevator.rmi.impl;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import elevator.rmi.*;
import elevator.ElevatorGUI;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The class implements the <code>GetAll</code> remote interface that provides
 * references to components of elevators to be controlled via RMI.
 * An object of the class creates servants with specific interfaces and provides
 * servants' references to the requesting clients via <code>GetAll</code> remote
 * interface.
 * The policy is to create only one instance of each servant (motor, door, etc.)
 * like in real hardware.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.GetAll
 */
public class GetAllImpl extends UnicastRemoteObject implements GetAll {
/**
 * some "global" variables
 */
  int numberOfElevators = elevator.Elevators.numberOfElevators;
  int numberOfFloors = elevator.Elevators.numberOfFloors;
  int topFloor = elevator.Elevators.topFloor;
  /**
   * Private "cache" of servants whose references might be requested by multiple
   * clients. Our policy to provide only one servant per elevator component(s)
   * like in real hardware.
   */
  private DoorImpl[] doorImpl = null; // n servants (one per elevator)
  private MotorImpl[] motorImpl = null; // n servants (one per elevator)
  private ScaleImpl[] scaleImpl = null; // n servants (one per elevator)
  private ElevatorImpl[] elevatorImpl = null; // n servants (one per elevator)

  private DoorsImpl doorsImpl = null; // one servant
  private MotorsImpl motorsImpl = null; // one servant
  private ScalesImpl scalesImpl = null; // one servant
  private ElevatorsImpl elevatorsImpl = null; // one servant

  public GetAllImpl() throws RemoteException {
  }
  public Door getDoor(int number) throws RemoteException {
    if (doorImpl == null) doorImpl = new DoorImpl[numberOfElevators];
    if (doorImpl[number - 1] == null) doorImpl[number - 1] = new DoorImpl(number);
    return doorImpl[number - 1];
  }
  public Door[] getDoor(int[] number) throws RemoteException {
    Door[] result = new DoorImpl[number.length];
    for (int i = number.length - 1; i >= 0; i--)
      result[i] = getDoor(number[i]);
    return result;
  }
  public Doors getDoors() throws RemoteException {
    if (doorsImpl == null) doorsImpl = new DoorsImpl();
    return doorsImpl;
  }
  public Elevator[] getElevator(int[] number) throws RemoteException {
    Elevator[] result = new ElevatorImpl[number.length];
    for (int i = number.length - 1; i >= 0; i--)
      result[i] = getElevator(number[i]);
    return result;
  }
  public Elevator getElevator(int number) throws RemoteException {
    if (elevatorImpl == null) elevatorImpl = new ElevatorImpl[numberOfElevators];
    if (elevatorImpl[number - 1] == null)
      elevatorImpl[number - 1] = new ElevatorImpl(number);
    return elevatorImpl[number - 1];
  }
  public Elevators getElevators() throws RemoteException {
    if (elevatorsImpl == null) elevatorsImpl = new ElevatorsImpl();
    return elevatorsImpl;
  }
  public void makeFloorListener(int floor, RemoteActionListener listener)
          throws RemoteException {
    new FloorListener(floor, listener);
  }
  public int getNumberOfElevators() throws RemoteException {
    return numberOfElevators;
  }
  public int getNumberOfFloors() throws RemoteException {
    return numberOfFloors;
  }
  public void makeInsideListener(int number, RemoteActionListener listener) throws RemoteException {
    new InsideListener(number, listener);
  }
  public void makePositionListener(int number, RemoteActionListener listener) throws RemoteException {
    new PositionListener(number, listener);
  }
  public void makeVelocityListener(RemoteActionListener listener) throws RemoteException {
    new VelocityListener(listener);
  }
  public Motor[] getMotor(int[] number) throws RemoteException {
    Motor[] result = new Motor[number.length];
    for (int i = number.length - 1; i >= 0 ; i--)
      result[i] = getMotor(number[i]);
    return result;
  }
  public Motor getMotor(int number) throws RemoteException {
    if (motorImpl == null) motorImpl = new MotorImpl[numberOfElevators];
    if (motorImpl[number - 1] == null) motorImpl[number - 1] = new MotorImpl(number);
    return motorImpl[number - 1];
  }
  public Motors getMotors() throws RemoteException {
    if (motorsImpl == null) motorsImpl = new MotorsImpl();
    return motorsImpl;
  }
  public Scale getScale(int number) throws RemoteException {
    if (scaleImpl == null) scaleImpl = new ScaleImpl[numberOfElevators];
    if (scaleImpl[number - 1] == null) scaleImpl[number - 1] = new ScaleImpl(number);
    return scaleImpl[number - 1];
  }
  public Scale[] getScale(int[] number) throws RemoteException {
    Scale[] result = new Scale[number.length];
    for (int i = number.length - 1; i >= 0; i--)
      result[i] = getScale(number[i]);
    return result;
  }
  public Scales getScales() throws RemoteException {
    if (scalesImpl == null) scalesImpl = new ScalesImpl();
    return scalesImpl;
  }
  public int getTopFloor() throws RemoteException {
    return topFloor;
  }
  public double getVelocity() throws RemoteException {
    return ElevatorGUI.velocity;
  }
}