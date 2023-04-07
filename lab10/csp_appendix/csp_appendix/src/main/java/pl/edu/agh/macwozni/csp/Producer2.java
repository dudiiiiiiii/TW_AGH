package pl.edu.agh.macwozni.csp;

import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.CSProcess;

public class Producer2 implements CSProcess {

  private One2OneChannelInt channel;
  private int start;

  public Producer2(final One2OneChannelInt out, int start) {
    channel = out;
    this.start = start;
  } // constructor

  public void run() {
    int item;
    for (int k = 0; k < 100; k++) {
      item = (int) (Math.random() * 100) + 1;
      channel.out().write(item);
    }
    channel.out().write(-1);
    System.out.println("Producer" + start + " ended.");
  } // run

} // class Producer