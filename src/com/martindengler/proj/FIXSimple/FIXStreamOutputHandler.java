package com.martindengler.proj.FIXSimple;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;

import static com.martindengler.proj.FIXSimple.FIXMessage.FIX_PREAMBLE;
import static com.martindengler.proj.FIXSimple.FIXMessage.ISO_8859_1;
import static com.martindengler.proj.FIXSimple.FIXMessage.SOH_byte;


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