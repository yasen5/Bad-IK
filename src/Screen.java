import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Dimension;
import java.awt.Color;

public class Screen extends JPanel implements MouseListener {
  private int debugCounter = 0;
  ArmHandler handler = new ArmHandler();
  int dtInMilliseconds = 20;
  ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  Runnable animate = new Runnable() {
    public void run() {
      handler.updateThetasUsingPID();
      repaint();
    }
  };

  public Screen() {
    setFocusable(true);
    addMouseListener(this);
    setBackground(new Color(100, 100, 100));
    executor.scheduleAtFixedRate(animate, 0, dtInMilliseconds, TimeUnit.MILLISECONDS);
  }

  @Override
  public void paintComponent(Graphics g) {
    handler.drawArms(g);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1000, 1000);
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    System.out.println("\n\n\n");
    debugCounter++;
    if (debugCounter == 2) {
      handler.pidControllers[0].debug = true;
    }
    handler.runIK(e.getX(), e.getY());
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}
