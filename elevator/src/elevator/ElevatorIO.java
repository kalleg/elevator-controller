package elevator;

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import elevator.rmi.IllegalParamException;
import elevator.rmi.GetAll;
import elevator.rmi.impl.GetAllImpl;
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
 * Provides input and execution of control commands (open/close doors, star/stop moving elevators,
 * set/get values of scales) from standard input, TCP socket connections or/and via Java RMI
 * (from servants),
 * and output of action commands from the floor and inside buttons
 * and current position of elevators to standard output, TCP socket or/and via Java RMI
 * (to listening servants).
 */
public class ElevatorIO extends Thread {
  // get global variables
  int numberOfElevators = Elevators.numberOfElevators;
  int numberOfFloors = Elevators.numberOfFloors;
  int topFloor = Elevators.topFloor;
    /**
     * The buffering character-input stream (BufferedReader) for reading char and String
     * from the input stream of the standard "input" or a TCP socket. Used for reading
     * control commands.
     * Initialized to BufferedReader for reading from the standard input
     */
  protected static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /**
     * The print stream for printing action commands from buttons and current positions
     * of elevators to the output stream of the standard "output" or a TCP socket.
     * Initialized to PrintStream for printing to the standard output
     */
  protected static PrintStream out = System.out;
  private Elevator[] allElevators;
/**
 * Creates an instance of ElevatorIO that opens a TCP socket and waits until
 * a client (a controller) connects if Elevators has started with the "-tcp" option, it
 * creates a GetAllImpl object for controlling Elevators via Java RMI and binds
 * its reference (stub) to
 * the "GetAll" name at the rmiregistry if Elevators has started with the "-rmi" option.
 * @param elevators is a reference to the object of the Elevators primary class used
 * to access the state of the Elevator objects (the Model)
 */
  public ElevatorIO(Elevators elevators) {
    super();
    this.allElevators = elevators.allElevators;
    if (Elevators.tcp) connectToClient(Elevators.inPort, Elevators.outPort);
    if (Elevators.rmi) {
      try {
        if (System.getSecurityManager() == null) {
          System.setSecurityManager(new RMISecurityManager());
        }
        /*java.rmi.Naming.bind("//" + Elevators.rmiHost
          + ":" + Elevators.rmiPort
          + "/GetAll", (GetAll)(new GetAllImpl());*/
        //new SocketPermission("localhost:1024-", "accept,connect,listen,resolve");
        Naming.rebind("//localhost/GetAll", (GetAll)(new GetAllImpl()));
      } catch (Exception e) {
        System.err.println("Failed to create an RMI interface. Bye, bye.");
        e.printStackTrace(System.err);
        System.exit(1);
      }
    }
  }
  /**
   * Opens a TCP socket or a couple of sockets (if different ports are specified for
   * input and output) and waits until a client(s) connects.
   * @param inPort the input port number (defaults to 4711)
   * @param outPort the output port number (defaults to 4711)
   */
  public void connectToClient(int inPort, int outPort) {
        Connector c1, c2 = null;
        c1 = new Connector(inPort);
        c1.setPriority(c1.getPriority() + 1);
        c1.start();
        if (outPort != inPort) {
          c2 = new Connector(outPort);
          c2.setPriority(c2.getPriority() + 1);
          c2.start();
        }
        try {
          c1.join();
        } catch (InterruptedException e) {}
        in = new BufferedReader(new InputStreamReader(c1.getInputStream()));
        if (c2 != null) {
          try {
            c2.join();
          } catch (InterruptedException e) {}
          out = new PrintStream(c2.getOutputStream(), true);
        } else out = new PrintStream(c1.getOutputStream(), true);
  }
  /**
   * Reads and execute control commands from standard or socket (if open) input stream
   * in a separate thread until the thread is closed ot the "quit" ("q")
   * command is read from the stream.
   */
  public void run() {
    // give some time for GUI to start up
    try { sleep(3000); } catch (Exception e) {;}
    readInput();
  }
  // just to remember
  /**
   * Elevators commands
   */
  private final static String[] commands  = {
    "q", "quit",
    "m", "move",
    "d", "door",
    "s", "scale",
    "w", "where",
    "v", "velocity"
  };
  /**
   * Reads elevator commands from the standard or the socket (if open) input stream
   * and executes over and over again until the input stream is closed or
   * the "quit" is read from the stream
   */
  public void readInput() {
    String input;
    StringTokenizer tokenizer;
    int elevatorNumber = 0, value = 0;
    String[] tokens = new String[3];
    try {
      while ((input = in.readLine()) != null) {
        if (input.equals("")) continue;
        tokenizer = new StringTokenizer(input);
        tokens[0] = tokenizer.nextToken();
        if (tokens[0].equalsIgnoreCase("q") || tokens[0].equalsIgnoreCase("quit")) {
          System.out.println("Bye, bye");
          System.exit(0);
        }
        tokens[1] = tokens[2] = null;
        if (tokenizer.hasMoreTokens()) {
          try {
            tokens[1] = tokenizer.nextToken();
            elevatorNumber = Integer.parseInt(tokens[1]);
          } catch (NumberFormatException e) {
                System.err.println("Illegal command: " + input);
                continue;
          }
        }
        if (tokenizer.hasMoreTokens()) {
          try {
            tokens[2] = tokenizer.nextToken();
            value = Integer.parseInt(tokens[2]);
          } catch (NumberFormatException e) {
                System.err.println("Illegal command: " + input);
                continue;
          }
        }
        if ((tokens[0].equalsIgnoreCase("m") || tokens[0].equalsIgnoreCase("move"))
              && tokens[1] != null && tokens[2] != null) {
            motor(elevatorNumber, value);
            continue;
        }
        if ((tokens[0].equalsIgnoreCase("s") || tokens[0].equalsIgnoreCase("scale"))
              && tokens[1] != null && tokens[2] != null) {
          scale(elevatorNumber, value);
          continue;
        }
        if ((tokens[0].equalsIgnoreCase("d") || tokens[0].equalsIgnoreCase("door"))
              && tokens[1] != null && tokens[2] != null) {
          door(elevatorNumber, value);
          continue;
        }
        if ((tokens[0].equalsIgnoreCase("w") || tokens[0].equalsIgnoreCase("where"))
              && tokens[1] != null) {
          where(elevatorNumber);
          continue;
        }
        if (tokens[0].equalsIgnoreCase("v") || tokens[0].equalsIgnoreCase("velocity")) {
          out.println("v " + ElevatorGUI.velocity);
          continue;
        }
        System.err.println("Illegal command: " + input);
      }
    } catch (IOException e) {
      System.err.println("ReadInput: Cannot read input. See below. Exiting... Bye, bye");
      e.printStackTrace();
      System.exit(1);
    }
  }
  /**
   * Executes a "m" ("motor") control command read from standard or socket input stream:
   * starts the elevator moving upwards or downwards, or stops the motor.
   * @param number the integer number of the motor (elevator) to start moving ot
   * to stop
   * @param val the integer code of a motor command: "1" - start moving upwards,
   * "0" - stop, "-1" - start moving downwards.
   */
  // used by TCP and standard IO
  public void motor(int number, int val) {
    if(number < 0 || number > numberOfElevators) {
      System.err.println("Motor: Motornumber " + number + " doesn't exist");
      return;
    }
    if( val > Elevators.UP || val < Elevators.DOWN) {
      System.err.println("Motor: direction " + val + " doesn't exist");
      return;
    }
    if(number == 0)
      for(int i = numberOfElevators - 1; i >= 0; i--)
        synchronized (allElevators[i].motorLock) {
          allElevators[i].Setdir(val);
        }
    else
      synchronized (allElevators[number - 1].motorLock) {
        allElevators[number - 1].Setdir(val);
      }
  }
  /**
   * Executes a "s" ("scale") control command read from standard or socket input stream:
   * sets a given value to the elevator scale.
   * @param number the integer number of the elevator whose scale to set
   * @param val the integer value to be set to the scale of the given elevator.
   */
  // used by TCP and standard IO
  public void scale(int number, int val) {
    if( number < 0 || number > numberOfElevators) {
      System.err.println("Scale: Scalenumber " + number + " doesn't exist");
      return;
    }
    if( val > topFloor || val < 0) {
      System.err.println("Scale: Floor number " + val + " doesn't exist");
      return;
    }
    if(number == 0)
      for(int i = 0; i < numberOfElevators; i++)
          allElevators[i].Setscalepos(val);
    else
        allElevators[number - 1].Setscalepos(val);
  }
  /**
   * Executes a "d" ("door") control command read from standard or socket input stream:
   * starts/stops openning/closing the door.
   * @param number the number of the elevator whose door to open/close
   * @param val the integer code of a door command: "1" - open, "-1" -close, "0" - stop
   */
  // used by TCP and standard IO
  public void door(int number, int val) {
    if( number < 0 || number > numberOfElevators) {
      System.err.println("Door: Doornumber " + number + " doesn't exist");
      return;
    }
    if( val > Elevators.OPEN || val < Elevators.CLOSE) {
      System.err.println("Door: direction " + val + " doesn't exist");
      return;
    }
    if(number == 0)
      for(int i = 0; i < numberOfElevators; i++)
        synchronized (allElevators[i].doorLock) {
          allElevators[i].Setdoor(val);
        }
    else
      synchronized (allElevators[number - 1].doorLock) {
        allElevators[number - 1].Setdoor(val);
      }
  }
  // used by TCP and standard IO
  /**
   * Executes a "w" ("where") control command read from standard or socket input stream:
   * gets and prints the current position of the elevator to standard or socket (if open)
   * input stream. The position is printed as "f <i>n p</i>", where "f" stands for
   * "floor units", <i>n</i> is the elevator number and <i>p</i> is a floating point value of the position
   * in "floor units" (e.g. "f 2 2.5" means that the 2nd elevator is exactly
   * in between the 2nd and the 3rd floors)
   * @param number the number of the elevator whose position to get
   */
  void where(int number)  {
    if( number < 0 || number > numberOfElevators) {
      System.err.println("Where: Elevatornumber " + number + " doesn't exist");
      return;
    }
    if(number == 0)
      for(int i = 0; i < numberOfElevators; i++)
        out.println("f " + (i + 1) + " " + allElevators[i].Getpos());
    else
      out.println("f " + number + " " + allElevators[number - 1].Getpos());
  }
  // used by RMI
  /**
   * Executes a "m" ("motor") control command accepted on an remote interface
   * used to control motor(s) such as Motor, Motors, Elevator and Elevators.
   * Called by an RMI servant that implements one of these interface.
   * Starts the elevator moving upwards or downwards, or stops the motor.
   * @param number the integer number of the motor (elevator) to start moving ot
   * to stop
   * @param val the integer code of a motor command: "1" - start moving upwards,
   * "0" - stop, "-1" - start moving downwards.
   * @throws IllegalParamException if number is not a legal elevator number
   * or/and val is not a legal motor command code
   */
  public void motorRMI(int number, int val) throws IllegalParamException {
    if(number < 0 || number > numberOfElevators)
      throw new IllegalParamException();
    if( val > Elevators.UP || val < Elevators.DOWN)
      throw new IllegalParamException();
    if(number == 0)
      for(int i = numberOfElevators - 1; i >= 0; i--)
        synchronized (allElevators[i].motorLock) {
          allElevators[i].Setdir(val);
        }
    else
      synchronized (allElevators[number - 1].motorLock) {
        allElevators[number - 1].Setdir(val);
      }
  }
  // used by RMI
  /**
   * Executes a "s" ("scale") control command accepted on an remote interface
   * used to control scale(s) such as Scale, Scales, Elevator and Elevators.
   * Called by an RMI servant that implements one of these interface.
   * Sets a given value to the elevator scale.
   * @param number the integer number of the elevator whose scale to set
   * @param val the integer value to be set to the scale of the given elevator.
   * @throws IllegalParamException if number is not a legal elevator number
   * or/and val is not a legal floor number
   */
  public void scaleRMI(int number, int val) throws IllegalParamException {
    if( number < 0 || number > numberOfElevators)
      throw new IllegalParamException();
    if( val > topFloor || val < 0)
      throw new IllegalParamException();
    if(number == 0)
      for(int i = 0; i < numberOfElevators; i++) allElevators[i].Setscalepos(val);
    else allElevators[number - 1].Setscalepos(val);
  }
  // used by RMI
  /**
   * Executes a "d" ("door") control command accepted on an remote interface
   * used to control door(s) such as Door, Doors, Elevator and Elevators.
   * Called by an RMI servant that implements one of these interface.
   * Starts/stops openning/closing the door.
   * @param number the number of the elevator whose door to open/close
   * @param val the integer code of a door command: "1" - open, "-1" -close, "0" - stop
   * @throws IllegalParamException if number is not a legal elevator number
   * or/and val is not a legal door command code
   */
  public void doorRMI(int number, int val) throws IllegalParamException {
    if( number < 0 || number > numberOfElevators)
      throw new IllegalParamException();
    if( val > Elevators.OPEN || val < Elevators.CLOSE)
      throw new IllegalParamException();
    if(number == 0)
      for(int i = 0; i < numberOfElevators; i++)
        synchronized (allElevators[i].doorLock) {
          allElevators[i].Setdoor(val);
        }
    else
      synchronized (allElevators[number - 1].doorLock) {
        allElevators[number - 1].Setdoor(val);
      }
  }
  // used by RMI
  /**
   * Executes a "w" ("where") control command accepted on an remote interface
   * used to control motor(s) such as Motor, Motors, Elevator and Elevators.
   * Called by an RMI servant that implements one of these interface and by
   * PositionListener objects (if any).
   * Returns the current position of the elevator to the calling thread.
   * @param number the number of the elevator whose position to get
   * @return float position of the elevator in "floor units" (e.g.
   * 2.5 "floor units means that the elevator is exactly in between the 2nd and
   * 3rd floor)
   * @throws IllegalParamException if number is not a legal elevator number
   */
  public double whereIs(int number) throws IllegalParamException {
    if( number <= 0 || number > numberOfElevators)
      throw new IllegalParamException();
    return allElevators[number - 1].Getpos();
  }
  // used by RMI
  /**
   * Executes a sequence of "w" ("where") control commands on all elevators
   * accepted on an remote interface used to control motors such as Motors and Elevators.
   * Called by an RMI servant that implements one of these interface and by
   * PositionListener objects (if any).
   * Returns current positions of all elevators to the calling thread.
   * @return an array of floatint point values of positions of the elevator in "floor units" (e.g.
   * 2.5 "floor units means that the elevator is exactly in between the 2nd and
   * 3rd floor)
   */
  public double[] whereAre() throws IllegalParamException {
    double[] position = new double[numberOfElevators];
    for (int i = 0; i < numberOfElevators; i++)
      position[i] = allElevators[i].Getpos();
    return position;
  }
  /**
   * Returns a current value of a scale.
   * Called by an RMI servant that implements one of the interfaces used to
   * control scales such as Scale, Scales, Elevator and Elevators.
   * Returns current value of the scale.
   * @param number the integer number of the elevator whose scale value to get
   * @return integer value of the scale
   * @throws IllegalParamException if number is not a legal elevator number
   */
  // used by RMI
  public int getScalePosition(int number) throws IllegalParamException {
    if( number <= 0 || number > numberOfElevators)
      throw new IllegalParamException();
    return allElevators[number - 1].Getscalepos();
  }
}
/**
 * Creates a server socket and waits for a client (controller) to connect, when
 * connected provides an input stream for reading controller commands and an
 * output stream for printing action commands from elevator and floor buttons and
 * current positions of elevators.
 */
class Connector extends Thread {
  ServerSocket serverSocket;
  Socket socket;
    /**
     * Creates a server socket on a specified port to listen for a connection
     * requests in a separate thread.
     * @param      port  the port number.
     */
  Connector(int port) {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Cannot create a server socket on port " + port + ". Bye, bye.");
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }
  /**
   * Listens for a connection to be made to this socket in a separate thread
   * and accepts  it. The method blocks until a connection is made.
   */
  public void run() {
    try {
      socket = serverSocket.accept();
    } catch (IOException e) {
      System.err.println("Got IOException, see below. Cannot continue. Exeting... Bye, bye.");
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }
    /**
     * Returns an input stream for the socket of this Connector.
     *
     * @return     an input stream for reading bytes from the socket of this Connector.
     */
  InputStream getInputStream() {
    try {
      return socket.getInputStream();
    } catch (IOException e) {
      System.err.println("Got IOException, see below. Cannot continue. Exeting... Bye, bye.");
      e.printStackTrace(System.err);
      System.exit(1);
    }
    return null;
  }
    /**
     * Returns an output stream for the socket of this Connector.
     *
     * @return     an output stream for writing bytes to the socket of this Connector.
     */
  OutputStream getOutputStream() {
    try {
      return socket.getOutputStream();
    } catch (IOException e) {
      System.err.println("Got IOException, see below. Cannot continue. Exeting... Bye, bye.");
      e.printStackTrace(System.err);
      System.exit(1);
    }
    return null;
  }
}