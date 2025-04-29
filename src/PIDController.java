import java.util.function.Function;

public class PIDController {
  private double kP, kI, kD;
  private double goalPos;
  private double kMaxSpeed;
  private double startPosition;
  private double acceptablePositionError = 0.005; // E_max
  private double previousPosition, previousVelocity, previousAcceleration;
  private double dt;
  private double lambda, b, c;
  private boolean usingNegativeRoute = true;
  boolean debug;
  private Function<Double, Double> normalizer = AngleMath::normalizeAngle;

  public PIDController(double kP, double kI, double kD, double maxSpeed, double dt) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.dt = dt;
    kMaxSpeed = maxSpeed;
    previousPosition = 0;
    previousVelocity = 0;
    previousAcceleration = 0;
  }

  public void reset(double goalPos, double startPosition) {
    if (debug) {
      System.out.println("Resetting");
    }
    usingNegativeRoute = !AngleMath.positiveIsShortestPath(startPosition, goalPos);
    int startingQuadrant = AngleMath.quadrant(startPosition);
    if (debug) {
      System.out.println("StartingPosition:" + startPosition + "\tStartingQuadrant: " + startingQuadrant
          + "\tUsingNegativeRoute: " + usingNegativeRoute);
    }
    normalizer = AngleMath::normalizeAngle;
    if (usingNegativeRoute) {
      if (startingQuadrant == 1 || startingQuadrant == 2) {
        normalizer = AngleMath::normalizeAngleNegToPos;
      }
    } else {
      if (startingQuadrant == 3 || startingQuadrant == 4) {
        normalizer = AngleMath::normalizeAngleNegToPos;
      }
    }
    this.startPosition = normalizer.apply(startPosition);
    this.goalPos = normalizer.apply(goalPos);
    computeSCurve();
  }

  public double calculate(double measurement, double time) {
    measurement = normalizer.apply(measurement);
    double posError = getTargetPositionFromProfile(time) - measurement;
    double currentVelocity = (previousPosition - measurement) / dt;
    double velocityError = currentVelocity - getTargetVelocityFromProfile(time);
    // TODO add integral error
    previousPosition = measurement;
    previousVelocity = currentVelocity;
    return kP * posError + kD * velocityError;
  }

  private void computeSCurve() {
    lambda = Math.abs(acceptablePositionError / (goalPos - startPosition));
    b = 4 * Math.abs(kMaxSpeed / (goalPos - startPosition));
    c = Math.log((1 - lambda) / lambda) / b;
    if (goalPos - startPosition == 0) {
      lambda = 0;
      b = 0;
      c = 0;
    }
  }

  private double getTargetPositionFromProfile(double time) {
    double targetPosition = startPosition + (goalPos - startPosition) / (1 + Math.exp(-b * (time - c)));
    // if (debug) {
    // System.out.println("StartPosition:" + String.format("%.3f", startPosition) +
    // "\tGoalPos:"
    // + String.format("%.3f", goalPos) + "\ttime:" + String.format("%.3f", time)
    // + "\tTarget position: " + String.format("%.3f", targetPosition) + "\tNegative
    // path: " + usingNegativeRoute);
    // }
    return targetPosition;
  }

  private double getTargetVelocityFromProfile(double time) {
    return (goalPos - startPosition) * b * Math.pow(Math.E, -b * (time - c)) / (1 + Math.exp(-b * (time - c)));
  }

  public void printPosInfo() {
    // System.out
    // .println("StartPos" + startPosition + " GoalPos: " + goalPos + " using
    // negative route: " + usingNegativeRoute);
  }
}
