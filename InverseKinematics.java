public class InverseKinematics {
    public void ik(double tX, double tY, int armOneLength, int armTwoLength, double[] thetas) {
        double targetVectorMagnitude = Math.hypot(tX, tY);
        double maxMagnitude = armOneLength + armTwoLength;
        if (tX == 0) {
            tX = 0.000001;
        }
        if (targetVectorMagnitude > armOneLength + armTwoLength) {
            tX = tX / targetVectorMagnitude * maxMagnitude;
            tY = tY / targetVectorMagnitude * maxMagnitude;
        }
        System.out.println("tx: " + tX + " ty: " + tY);
        System.out.println("Magnitude:" + targetVectorMagnitude);
        double targetTheta = Math.atan(tY / tX);
        if (targetVectorMagnitude < 215) { // 215
            targetTheta += Math.PI;
        }
        if (tX < 0) {
            targetTheta += Math.PI;
        }
        double d = tX * tX + tY * tY;
        double intermediary = (d - armOneLength * armOneLength - armTwoLength * armTwoLength)
                / (2 * armOneLength * armTwoLength);
        if (Math.abs(intermediary) > 1) {
            intermediary = intermediary > 0 ? 1 : -1;
        }
        double q2 = Math.acos(intermediary);
        double beta = Math.atan(armTwoLength * Math.sin(q2) / (armOneLength + armTwoLength * Math.cos(q2)));
        double q1 = targetTheta
                - beta;
        thetas[0] = q1;
        thetas[1] = q1 + q2;
    }
}