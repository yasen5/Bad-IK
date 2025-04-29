public class AngleMath {
  public static double normalizeAngleNegToPos(double angle) {
    angle = angle % (2 * Math.PI);
    return Math.abs(angle) < Math.PI ? angle : angle + (angle < 0 ? 2 * Math.PI : -2 * Math.PI);
  }

  public static double normalizeAngle(double angle) {
    angle = angle % (2 * Math.PI);
    if (angle < 0) {
      angle = 2 * Math.PI + angle;
    }
    return angle;
  }

  public static boolean positiveIsShortestPath(double angle1, double angle2) {
    double difference = angle2 - angle1;
    return difference > 0 ? difference <= Math.PI : difference < -Math.PI;
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
