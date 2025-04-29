import javax.swing.JFrame;

public class Runner {
  public static void main(String[] args) {
    Screen screen = new Screen();
    JFrame frame = new JFrame("Frame");
    frame.add(screen);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    // for (int i = 0; i < 10; i++) {
    // double firstVal = (int) (Math.random() * 8);
    // double secondVal = (int) (Math.random() * 8);
    // System.out.println("Positive is shortest path " + firstVal + "/ 4" + " to " +
    // secondVal + "/ 4"
    // + "\t" + AngleMath.positiveIsShortestPath(firstVal * Math.PI / 4, secondVal *
    // Math.PI / 4));
    // }
    // double firstVal = 7;
    // double secondVal = 1;
    // System.out.println("Positive is shortest path " + firstVal + "/ 4" + " to " +
    // secondVal + "/ 4"
    // + "\t" + AngleMath.positiveIsShortestPath(firstVal * Math.PI / 4, secondVal *
    // Math.PI / 4));
  }
}
