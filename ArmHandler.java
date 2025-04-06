import java.awt.Color;
import java.awt.Graphics;

public class ArmHandler {
    private Color black = new Color(0, 0, 0);
    private InverseKinematics ik = new InverseKinematics();
    private int armOneLength = 200;
    private int armTwoLength = 300;
    private double[] thetas = new double[2];
    private double[] goalThetas = new double[2];
    private int startX = 300;
    private int startY = 550;
    private int rectWidth = 15;
    private double[] armMotorSpeeds = { Math.PI / 6, Math.PI / 6 };
    private double thetaTolerance = Math.PI / 6 * 0.11;

    public ArmHandler() {
        // runIK(targetPos[0], targetPos[1]);
        thetas[0] = 0;
        thetas[1] = 0;
        goalThetas[0] = Math.PI * 2 / 3;
        goalThetas[1] = Math.PI * 2 / 3;
    }

    public void drawArms(Graphics g) {
        int x1 = startX + (int) (armOneLength * Math.cos(thetas[0]));
        int midwayThroughRect = rectWidth / 2;
        int leftXAdjustment = (int) (midwayThroughRect * (1 - Math.cos(Math.PI / 2 - thetas[0])));
        int rightXAdjustment = (int) (midwayThroughRect * (1 + Math.cos(Math.PI / 2 - thetas[0])));
        int y1 = startY - (int) (armOneLength * Math.sin(Math.PI - thetas[0]));
        int yAdjustment = (int) (midwayThroughRect * Math.sin(Math.PI / 2 - thetas[0]));
        int x2 = x1 + (int) (armTwoLength * Math.cos(thetas[1]));
        int topLeftXAdjustment = (int) (midwayThroughRect * (1 - Math.cos(Math.PI / 2 - thetas[1])));
        int topRightXAdjustment = (int) (midwayThroughRect * (1 + Math.cos(Math.PI / 2 - thetas[1])));
        int y2 = y1 - (int) (armTwoLength * Math.sin(thetas[1]));
        int topYAdjustment = (int) (midwayThroughRect * Math.sin(Math.PI / 2 - thetas[1]));
        g.setColor(black);
        g.fillPolygon(
                new int[] { startX + leftXAdjustment, startX + rightXAdjustment, x1 + rightXAdjustment,
                        x1 + leftXAdjustment },
                new int[] { startY - yAdjustment, startY + yAdjustment, y1 + yAdjustment, y1 - yAdjustment }, 4);
        g.fillPolygon(new int[] { x1 + topLeftXAdjustment, x1 + topRightXAdjustment, x2 + topRightXAdjustment,
                x2 + topLeftXAdjustment },
                new int[] { y1 - topYAdjustment, y1 + topYAdjustment, y2 + topYAdjustment, y2 - topYAdjustment }, 4);
        g.fillPolygon(new int[] { x1 + leftXAdjustment, x1 + rightXAdjustment, x1 + topRightXAdjustment,
                x1 + topLeftXAdjustment },
                new int[] { y1 - yAdjustment, y1 + yAdjustment, y1 + topYAdjustment, y1 - topYAdjustment }, 4);
    }

    public void runIK(int mouseX, int mouseY) {
        ik.ik(mouseX - startX, startY - mouseY, armOneLength, armTwoLength, goalThetas);
    }

    public void updateThetas(double dt) {
        for (int i = 0; i < thetas.length; i++) {
            double trueGoalTheta = goalThetas[i];
            for (int j = 0; j < i; j++) {
                trueGoalTheta -= goalThetas[j];
            }
            double trueTheta = thetas[i];
            for (int j = 0; j < i; j++) {
                trueTheta -= thetas[j];
            }
            if (Math.abs(trueGoalTheta - trueTheta) > thetaTolerance) {
                thetas[i] += (trueTheta < trueGoalTheta ? 1 : -1) * armMotorSpeeds[i] * dt;
            }
        }
    }

    public double getThetaOne() {
        return thetas[0];
    }
}