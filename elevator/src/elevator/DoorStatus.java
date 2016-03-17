package elevator;

import java.util.Enumeration;

/**
 * Title:        Green Elevator
 * Description:  Green Elevator, 2G1915
 * Copyright:    Copyright (c) 2001
 * Company:      IMIT/KTH
 * @author Vlad Vlassov
 * @version 1.0
 */
/**
 * Enumeration that generates six status of an elevator door: CLOSED,
 * OPEN1, OPEN2, OPEN3, OPEN4, which correspond to diferrent "degree of openness"
 * of the door used in elevator animation.
 */
public class DoorStatus implements Enumeration {
  private int index = 0;
  public static final int CLOSED = 0;
  public static final int OPEN1 = 1;
  public static final int OPEN2 = 2;
  public static final int OPEN3 = 3;
  public static final int OPEN4 = 4;
  public static final int[] STATUS = {CLOSED, OPEN1, OPEN2, OPEN3, OPEN4};
  /**
   * Creates an instance of DoorStatus
   */
  public DoorStatus() {
  }
  public boolean hasMoreElements() {
    /**@todo: Implement this java.util.Enumeration method*/
    return (index < STATUS.length);
  }
  public Object nextElement() {
    /**@todo: Implement this java.util.Enumeration method*/
    return new Integer(STATUS[index++]);
  }
}
