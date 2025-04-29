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
  private double[] armMotorSpeeds = { Math.PI / 3, Math.PI / 3 };
  private double thetaTolerance = Math.PI / 6 * 0.05;
  PIDController[] pidControllers = new PIDController[2];
  private double dtSeconds = 0.02;
  private int iterations = 0;
  private double[] motorVelocities = new double[2];

  public ArmHandler() {
    thetas[0] = 0;
    thetas[1] = 0;
    goalThetas[0] = Math.PI * 2 / 3;
    goalThetas[1] = 0;
    for (int i = 0; i < pidControllers.length; i++) {
      pidControllers[i] = new PIDController(5, 0, 0, armMotorSpeeds[i], dtSeconds);
      pidControllers[i].reset(goalThetas[i], thetas[i]);
    }
  }

  public void drawArms(Graphics g) {
    int x1 = startX + (int) (armOneLength * Math.cos(thetas[0]));
    int midwayThroughRect = rectWidth / 2;
    int leftXAdjustment = (int) (midwayThroughRect * (1 - Math.cos(Math.PI / 2 - thetas[0])));
    int rightXAdjustment = (int) (midwayThroughRect * (1 + Math.cos(Math.PI / 2 - thetas[0])));
    int y1 = startY - (int) (armOneLength * Math.sin(Math.PI - thetas[0]));
    int yAdjustment = (int) (midwayThroughRect * Math.sin(Math.PI / 2 - thetas[0]));
    double q2WithPrevious = thetas[1] + thetas[0];
    int x2 = x1 + (int) (armTwoLength * Math.cos(q2WithPrevious));
    int topLeftXAdjustment = (int) (midwayThroughRect * (1 - Math.cos(Math.PI / 2 - q2WithPrevious)));
    int topRightXAdjustment = (int) (midwayThroughRect * (1 + Math.cos(Math.PI / 2 - q2WithPrevious)));
    int y2 = y1 - (int) (armTwoLength * Math.sin(q2WithPrevious));
    int topYAdjustment = (int) (midwayThroughRect * Math.sin(Math.PI / 2 - q2WithPrevious));
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
    iterations = 0;
    ik.ik(mouseX - startX, startY - mouseY, armOneLength, armTwoLength, goalThetas);
    for (int i = 0; i < pidControllers.length; i++) {
      pidControllers[i].reset(goalThetas[i], thetas[i]);
    }
  }

  public void updateThetas() {
    for (int i = 0; i < thetas.length; i++) {
      if (Math.abs(goalThetas[i] - thetas[i]) > thetaTolerance) {
        motorVelocities[i] = (thetas[i] < goalThetas[i] ? 1 : -1) * armMotorSpeeds[i] * dtSeconds;
        thetas[i] += motorVelocities[i];
      }
    }
  }

  public void updateThetasUsingPID() {
    iterations++;
    for (int i = 0; i < thetas.length; i++) {
      if (Math.abs(goalThetas[i] - thetas[i]) > thetaTolerance) {
        motorVelocities[i] = pidControllers[i].calculate(thetas[i],
            iterations * dtSeconds)
            * dtSeconds;
        thetas[i] += motorVelocities[i];
      }
    }
  }
}
