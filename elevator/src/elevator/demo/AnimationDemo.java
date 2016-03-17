package elevator.demo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import elevator.rmi.*;
/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */

/**
 * The class is a primary class of the AnimationDemo Java application which
 * illustrates a way of controling the Elevators application via Java RMI.
 * <p>The program generates a sequence of control commands submitted to the
 * <a href="../Elevators.html">elevator.Elevators</a> application via Java RMI.
 * The program assumes that the rmiregistry (and Elevators) runs on "localhost"
 * and there are at least 3 elevators and 4 floors.
 * It opens/closes the door of the 1-st elevator with the interval of 2 sec.,
 * then it starts all motors to move all elevators up for 2 sec., stops them,
 * and moves them down. After 2 sec, it starts moving the 3-rd elevator
 * up to the 4-th floor updating its scale indicator each 0.1 sec.
 *
 * <p>To run the application, first, start rmiregistry, then Elevators and, finally,
 * AnimationDemo. It's recommended to start Elevators and AnimationDemo in different
 * windows so that you can keep Elevators running when you restart the demo
 * or when you start your own control/animation program.
 * To see result of AnimationDemo, you should look at Elevators. You may also click on
 * buttons of Elevators: action commands of buttons should be printed to the standard
 * output of AnimationDemo.You may also control the Elevators, when necessary,
 * via standard input. Try, for example, "m 0 1" (move all up), "m 0 -1" (move
 * all down), "d 3 1" (open the 3rd door), "s 3 0" (set the 3rd scale to 0).
 * <p>Starting both applications
 * you must indicate a path to <code>elevator.jar</code> with the <code>-classpath</code> option.
 * The JAR file is located under the <code>lib</code> directory of the Elevators home.
 * Starting Elevators you must also specify the <code>-Djava.security.policy</code>
 * property that is set to the <code>rmi.policy</code> file located
 * under the <code>lib</code> directory and the <code>-Djava.rmi.server.codebase</code>
 * property that poits to the <code>elevator.jar</code>.
 * For example (assume that the Elevators is located under "D:\home\vlad\edu\elevator\"),
 * <p><blockquote><pre>
 * C:\>start rmiregistry
 * C:\>java -classpath D:\home\vlad\edu\elevator\lib\elevator.jar -Djava.security.policy=D:\home\vlad\edu\elevator\lib\rmi.policy -Djava.rmi.server.codebase=file:d:\home\vlad\edu\elevator\lib\elevator.jar elevator.Elevators -top 5 -number 5 -rmi
 * C:\>java -classpath D:\home\vlad\edu\elevator\lib\elevator.jar elevator.demo.AnimationDemo
 * </pre></blockquote>
 * Where <code>rmi.security</code> is a text file that may specify the following permission:
 * <p><blockquote><pre>
 * grant {
 * // Allow everything for now
 * permission java.security.AllPermission;
 * };
 * </pre></blockquote>
 * The <code>elevator.jar</code> file is a JAR file that includes all resources
 * of the Elevators application including classes and interfaces needed to
 * develop a control program with Java RMI.
 * @see elevator.Elevators
 * @see elevator.rmi.MakeAll
 */
public class AnimationDemo extends Thread implements ActionListener {
    Motor motor;
    Door door;
    Scale scale;
    Elevators elevators;
    String rmihost;
  /**
   * Creates an instace of <code>AnimationDemo</code> to run in a separate thread
   */
  public AnimationDemo(String[] args) {
    rmihost = (args.length > 0)? args[0] : "localhost";
  }
  /**
   * Runs the demo in a thread
   */
  public void run() {
    try {
      MakeAll.init(rmihost);
      MakeAll.addFloorListener(this);
      MakeAll.addInsideListener(this);
      MakeAll.addPositionListener(this);
      //MakeAll.addPositionListener(3, this);
      motor =MakeAll.getMotor(1);
      scale = MakeAll.getScale(1);
      door = MakeAll.getDoor(1);
        door.open();
        sleep(2000);
        door.close();
        sleep(2000);
        door.open();
        sleep(2000);
        door.close();
      elevators = MakeAll.getElevators();
      elevators.up();
      sleep(2000);
      elevators.stop();
      sleep(2000);
      elevators.down();
      sleep(2000);
      Elevator e3 = MakeAll.getElevator(3);
      double where = e3.whereIs();
      System.out.println(where);
      e3.up();
      do {
        e3.setScalePosition((int)where);
        sleep(100);
      } while ((where = e3.whereIs()) < 3.999);
      e3.stop();
      e3.setScalePosition(4);
      e3.open();
      sleep(3000);
      e3.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
  /**
   * The entry point of the <code>AnimationDemo</code> application. Create
   * a <code>AnimationDemo</code> thread and start it.
   */
  public static void main(String[] args) {
    (new AnimationDemo(args)).start();
  }
  /**
   * Invoked when any button on the Elevators is pressed. Prints an action command
   * assigned to the button.
   */
  public void actionPerformed(ActionEvent e) {
    System.out.println("command=" + e.getActionCommand());
  }
}
