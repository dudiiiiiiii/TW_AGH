package pl.edu.agh.macwozni.csp;

import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.CSProcess;

/**
 * Consumer class:
 * reads one int from input channel, displays it,
 * then terminates.
 */

public class Consumer2 implements CSProcess {
  private One2OneChannelInt in;
  private One2OneChannelInt req;

  public Consumer2(final One2OneChannelInt req, final One2OneChannelInt in){
    this.req = req;
    this.in = in;
  } // constructor

  public void run() {
    int item;
    while(true){
      req.out().write(0);
      item = in.in().read();
      if (item<0){
        break;
      }
      System.out.println(item);
    }
    System.out.println("Consumer ended.");
  } // run

} // class Consumer
