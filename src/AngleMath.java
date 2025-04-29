public class AngleMath {
  private static boolean debug = false;

  public static double normalizeAngleNegToPos(double angle) {
    angle = angle % (2 * Math.PI);
    return Math.abs(angle) < Math.PI ? angle : angle + (angle < 0 ? 2 * Math.PI : -2 * Math.PI);
  }

  // public static double normalizeAngleZeroToTwoPI(double angle) {
  // angle = angle % (2 * Math.PI);
  // if (angle < 0) {
  // angle = 2 * Math.PI + angle;
  // }
  // return angle;
  // }

  // public static double normalizeAngle(double angle, double lowerBound, double
  // upperBound) {
  // double fullDistance = upperBound - lowerBound;
  // angle = angle % (fullDistance/2);
  // if (angle < 0) {
  // angle = fullDistance + angle;
  // }
  // return angle;
  // }

  public static double normalizeAngle(double angle) {
    if (debug)
      System.out.println("Starting angle: " + angle);
    angle = angle % (2 * Math.PI);
    if (debug)
      System.out.println("Angle at after putting between 0 and 2pi: " + angle);
    if (angle < 0) {
      angle = 2 * Math.PI + angle;
    }
    if (debug)
      System.out.println("Ending angle: " + angle);
    return angle;
  }

  // public static boolean positiveIsShortestPath(double angle1, double angle2) {
  // if (angle2 > angle1) {
  // return angle2 - angle1 <= Math.PI;
  // } else {
  // return 2 * Math.PI - (angle1 - angle2) <= Math.PI;
  // }
  // }

  // New
  public static boolean positiveIsShortestPath(double angle1, double angle2) {
    double difference = angle2 - angle1;
    return difference > 0 ? difference <= Math.PI : difference < -Math.PI;
  }

  public static void setDebug(boolean value) {
    debug = value;
  }

  public static int quadrant(double angle) {
    angle = normalizeAngle(angle);
    if (angle < Math.PI / 2) {
      return 1;
    } else if (angle < Math.PI) {
      return 2;
    } else if (angle < 3 * Math.PI / 2) {
      return 3;
    } else {
      return 4;
    }
  }
}
