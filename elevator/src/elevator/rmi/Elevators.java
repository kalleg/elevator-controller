package elevator.rmi;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * The remote interface is used for controlling all components of all elevators,
 * i.e. motors, scales (level indicators) and doors of all elevator, a group of
 * elevators or one elevator.
 * <p>The <code>Elevators</code> interface extends <code>Motors</code>,
 * <code>Doors</code>, <code>Scales</code> and <code>AllInsideButtons</code>
 * interfaces.
 * <p>The <code>Elevators</code> interface is implemented by the class
 * <code>elevator.rmi.impl.ElevatorsImpl</code>. An object with the
 * <code>ElevatorsImpl</code> class is created to control motors, scales and
 * doors of all elevators.
 * <p>The <code>Elevators</code> remote interface can be used to start moving
 * elevators up or down, to stop elevators, to close/open the elevator doors,
 * to get current positions of the elevators, to set/get values of the scales of
 * the elevators.
 * The <code>getElevators()</code> static method
 * of the <code>elevator.rmi.MakeAll</code> class is invoked to get a remote
 * reference to the <code>Elevators</code> object.
 * For example:
 * <p><blockquote><pre>
 *    MakeAll.init(localhost);
 *     ...
 *    Elevators elevators = MakeAll.getElevators();
 *    ...
 *    // move the 3rd elevator up to the 5th floor
 *    double where = elevators.getPosition(3);
 *    elevators.up(3);
 *    do {
 *      elevators.setScalePosition(3, (int)where);
 *      sleep(10);
 *    } while ((where = elevators.whereIs(3)) < 4.888)
 *    elevators.stop(3);
 *    elevators.setScalePosition(3, 5);
 *    elevators.open(3);
 *    sleep(3000);
 *    elevators.close(3);
 * </pre></blockquote>
 * <p>The interface <code>Elevators</code> allows controlling all
 * components (motors, doors, scales and button listeners) of all elevators via
 * one object.
 * <p>It is worth noting that an object with the
 * <code>elevator.rmi.Elevator</code> interface allows controlling all
 * components (a motor, a scale, a door and the button event listeners) of one
 * elevator, whereas an object with the <code>elevator.rmi.Motor</code>
 * interface allows controlling only the motor, an object with the
 * <code>elevator.rmi.Door</code> interface allows controlling only the door and
 * an object with the <code>elevator.rmi.Scale</code> interface allows
 * controlling only the scale of one elevator.
 * <p>The interfaces <code>elevator.rmi.Motors</code>,
 * <code>elevator.rmi.Doors</code>, <code>elevator.rmi.Scales</code> allows
 * controlling all motors, all doors and all scales of all elevators,
 * respectively.
 * <p>By analogy, the interface <code>elevator.rmi.AllInsideButtons</code>
 * allows adding action listeners to the inside button of all elevators,
 * a group of elevator or one elevator, whereas
 * the interface <code>elevator.rmi.InsideButtons</code> adds listener(s) to
 * inside buttons of only one elevator.
 * <p>Thus, different interfaces provide
 * different level of granularity in controlling the elevators. Choice of the
 * interface(s) for an elevator control multithreaded program should depend on
 * granularity of threads controlling elevators and/or their different
 * components.
 * @author Vlad Vlassov, IMIT/KTH, Stockholm, Sweden
 * @version 1.0
 * @see     elevator.rmi.MakeAll
 * @see     elevator.rmi.Motor
 * @see     elevator.rmi.Door
 * @see     elevator.rmi.Scale
 * @see     elevator.rmi.Motors
 * @see     elevator.rmi.Doors
 * @see     elevator.rmi.Scales
 * @see     elevator.rmi.Elevators
 */
public interface Elevators extends Motors, Doors, Scales {
}