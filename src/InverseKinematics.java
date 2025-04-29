public class InverseKinematics {
  public void ik(double tX, double tY, int armOneLength, int armTwoLength, double[] thetas) {
    double targetVectorMagnitude = Math.hypot(tX, tY);
    double maxMagnitude = armOneLength + armTwoLength;
    if (targetVectorMagnitude > armOneLength + armTwoLength) {
      tX = tX / targetVectorMagnitude * maxMagnitude;
      tY = tY / targetVectorMagnitude * maxMagnitude;
    }
    double targetTheta = Math.atan2(tY, tX);
    double d = tX * tX + tY * tY;
    double intermediary = (d - armOneLength * armOneLength - armTwoLength * armTwoLength)
        / (2 * armOneLength * armTwoLength);
    if (Math.abs(intermediary) > 1) {
      intermediary = intermediary > 0 ? 1 : -1;
    }
    double q2 = Math.acos(intermediary);
    double beta = Math.atan2(armTwoLength * Math.sin(q2), (armOneLength + armTwoLength * Math.cos(q2)));
    double q1 = targetTheta
        - beta;
    thetas[0] = q1;
    thetas[1] = q2;
  }
}
