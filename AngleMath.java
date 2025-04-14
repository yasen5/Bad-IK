public class AngleMath {
    public static double normalizeAngle(double angle) {
        angle = angle % (2 * Math.PI);
        return Math.abs(angle) < Math.PI ? angle : angle + (angle < 0 ? 2 * Math.PI : -2 * Math.PI);
    }
}
