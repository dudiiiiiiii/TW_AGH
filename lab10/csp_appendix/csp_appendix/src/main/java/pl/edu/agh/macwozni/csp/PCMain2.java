package pl.edu.agh.macwozni.csp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.Channel;

/**
 * Main program class for Producer/Consumer example.
 * Sets up channel, creates one of each process
 * then executes them in parallel, using JCSP.
 */

public final class PCMain2 {

  public static void main(String[] args) {
    new PCMain2();
  } // main

  public PCMain2() { // Create channel object
    final One2OneChannelInt[] prodChan = {
        (One2OneChannelInt) Channel.one2oneInt(),
        (One2OneChannelInt) Channel.one2oneInt()
    };

    final One2OneChannelInt[] consReq = {
        (One2OneChannelInt) Channel.one2oneInt(),
        (One2OneChannelInt) Channel.one2oneInt()
    };

    final One2OneChannelInt[] consChan = {
        (One2OneChannelInt) Channel.one2oneInt(),
        (One2OneChannelInt) Channel.one2oneInt()
    };

    // Create parallel construct
    CSProcess[] procList = { new Producer2(prodChan[0], 0),
        new Producer2(prodChan[1], 100),
        new Buffer(prodChan, consReq, consChan),
        new Consumer2(consReq[0], consChan[0]),
        new Consumer2(consReq[1], consChan[1])
    }; // Processes
    Parallel par = new Parallel(procList); // PAR construct
    par.run(); // Execute processes in parallel
  } // PCMain constructor

} // class PCMain
