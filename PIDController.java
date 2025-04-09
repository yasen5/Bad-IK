public class PIDController {
    private double kP, kI, kD;
    private double goalPos;
    private double kMaxSpeed;
    private double startPosition, startVelocity;
    private double acceptablePositionError = 0.005; // E_max
    private double previousPosition, previousVelocity, previousAcceleration;
    private double dt;
    private double lambda, b, c;

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

    public void reset(double goalPos, double startVelocity, double startPosition) {
        this.goalPos = goalPos;
        this.startVelocity = startVelocity;
        this.startPosition = startPosition;
        computeSCurve();
    }

    public double calculate(double measurement, double time) {
        double posError = getTargetPositionFromProfile(time) - measurement;
        double currentVelocity = (previousPosition - measurement) / dt;
        double velocityError = currentVelocity - getTargetVelocityFromProfile(time);
        // TODO add integral error
        previousPosition = measurement;
        previousVelocity = currentVelocity;
        // return getTargetPositionFromProfile(time);
        return kP * posError + kD * velocityError;
    }

    private void computeSCurve() {
        lambda = acceptablePositionError / (goalPos - startVelocity);
        b = 4 * Math.abs(kMaxSpeed / (goalPos - startVelocity));
        c = Math.log((1 - lambda) / lambda) / b;
    }

    private double getTargetPositionFromProfile(double time) {
        return startPosition + (goalPos - startPosition) / (1 + Math.exp(-b * (time - c)));
    }

    private double getTargetVelocityFromProfile(double time) {
        return (goalPos - startPosition) * b * Math.pow(Math.E, -b * (time - c)) / (1 + Math.exp(-b * (time - c)));
    }
}
