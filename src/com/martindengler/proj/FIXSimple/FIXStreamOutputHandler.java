package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;


public class FIXStreamOutputHandler implements Callable<Object> {

    private FIXStream parent;
    private Writer outputWriter;

    public FIXStreamOutputHandler(FIXStream parent, Writer outputWriter) {
        this.parent = parent;
        this.outputWriter = outputWriter;
    }

    public Object call() {

        FIXStream.State state = parent.getState();

        while (state == FIXStream.State.ACTIVE
                || state == FIXStream.State.CLOSED_IN) {
            try {
                FIXMessage outgoing = parent.outputQueue().take();

                System.err.format("FIXStreamOutputHandler:" +
                        " writing message to wire: %s%n",
                        outgoing.toString(false));

                outputWriter.write(outgoing.toWire());
                outputWriter.flush();

                System.err.println("FIXStreamOutputHandler: wrote message.");

            } catch (InterruptedException inte) {

                // NOP, as we will just retry later

            } catch (IOException ioe) {

                System.err.println("FIXStreamOutputHandler:" +
                        " IOException in outputWriter.write();" +
                        " stopping");
                System.err.println(ioe.getMessage());
                System.err.println(ioe.toString());
                switch (state) {
                case ACTIVE:
                    parent.setState(FIXStream.State.CLOSED_OUT);
                    break;
                case CLOSED_IN:
                    parent.setState(FIXStream.State.CLOSED_ALL);
                    break;
                }
                break;

            }
        }

        System.err.println("outputHandler finished");

        return null;

    }

}