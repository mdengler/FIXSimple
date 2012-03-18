package com.martindengler.proj.FIXSimple;

import com.martindengler.proj.FIXSimple.spec.ExecTransType;
import com.martindengler.proj.FIXSimple.spec.ExecType;
import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.OrdStatus;
import com.martindengler.proj.FIXSimple.spec.Tag;

import java.util.ArrayList;
import java.util.Arrays;

public class AcceptorSimpleClientHandler extends FIXEndpoint
    implements Runnable {


    public enum State {
        PENDING_LOGIN,
            NORMAL,
            FINISHED,
            UNKNOWN
    }

    // trivial container class
    protected class StateTransition {

        public State toState;
        public ArrayList<FIXMessage> messagesToSend;

        public StateTransition(State toState) {
            this(toState, new ArrayList<FIXMessage>());
        }

        public StateTransition(State toState,
                ArrayList<FIXMessage> messagesToSend) {
            this.toState = toState;
            this.messagesToSend = messagesToSend;
        }
    }


    public State state = State.UNKNOWN;


    public AcceptorSimpleClientHandler(String senderCompId) {
        this.senderCompId = senderCompId;
    }


    public void run() {

        System.err.println("handling connection");

        this.state = State.PENDING_LOGIN;
        String targetCompId = "unknown";

        while (this.state != State.FINISHED) {  // state machine start
            StateTransition transition = handleState(this.state);
            this.state = transition.toState;
            for (FIXMessage m : transition.messagesToSend) {
                try {
                    this.outgoingMessageQueue.put(m);
                } catch (InterruptedException inte) {
                    inte.printStackTrace();

                    // FUTURE: do something better here
                    System.err.println("AcceptorSimpleClientHandler.run():" +
                            " InterrupedException; aborting / finishing up");
                    this.state = State.FINISHED;
                }
            }
        }

        this.stop();

    }


    /**
     * TODO: not thread safe
     */
    public synchronized void stop() {
        this.state = State.FINISHED;
        this.bidirectionalStream.stop();
        System.err.println("AcceptorSimpleClientHandler - stopped");
    }


    protected StateTransition handleState(State currentState) {
        StateTransition transition = new AcceptorSimpleClientHandler.
            StateTransition(currentState);

        FIXMessage incomingMessage = null;  // neater - declared only once

        if (currentState == State.PENDING_LOGIN
                || currentState == State.NORMAL) {
            try {
                incomingMessage = this.incomingMessageQueue.take();
            } catch (InterruptedException inte) {
                inte.printStackTrace();
                return transition;
            }
        }

        switch(currentState) {

        case PENDING_LOGIN:
            if (incomingMessage.getMsgType() != MsgType.LOGON) {
                System.err.format("waiting for LOGON message" +
                        " but got %s instead%s",
                        incomingMessage);
                transition.toState = State.FINISHED;
            } else {
                targetCompId = incomingMessage.get(Tag.SENDERCOMPID);
                FIXMessage responseMessage = FIXMessage
                    .factory(MsgType.LOGON)
                    .putM(Tag.SENDERCOMPID, this.senderCompId)
                    .putM(Tag.TARGETCOMPID, targetCompId)
                    ;
                transition.toState = State.NORMAL;
                transition.messagesToSend.add(responseMessage);
            }
            break;

        case NORMAL:
            if (incomingMessage.getMsgType() == MsgType.LOGOUT) {
                FIXMessage logoutMessage = FIXMessage.factory(MsgType.LOGOUT)
                    .putM(Tag.SENDERCOMPID, this.senderCompId)
                    .putM(Tag.TARGETCOMPID, targetCompId)
                    ;
                transition.toState = State.FINISHED;
                transition.messagesToSend.add(logoutMessage);
                System.err.println("logged " + targetCompId + " out.");
            } else {
                if (incomingMessage.getMsgType() != MsgType.NEW_ORDER_SINGLE) {
                    System.err.println("Ignoring non-NOS message");
                } else if (OrdStatus.fromCode(incomingMessage.get(Tag.ORDSTATUS))
                            != OrdStatus.NEW) {
                        System.err.println("Ignoring NOS with ORDSTATUS != NEW");
                } else {

                    FIXMessage responseMessage = FIXMessage
                        .factory(MsgType.EXECUTION_REPORT)
                        .putM(Tag.LEAVESQTY,     incomingMessage.get(Tag.ORDERQTY))
                        .putM(Tag.ORDSTATUS,     OrdStatus.NEW)
                        .putM(Tag.EXECTYPE,      ExecType.NEW)
                        .putM(Tag.EXECTRANSTYPE, ExecTransType.NEW)
                        ;
                    for (Tag t : Arrays.asList(Tag.CLORDID,
                                    Tag.ORIGCLORDID,
                                    Tag.ORDERQTY,
                                    Tag.CUMQTY,
                                    Tag.LASTSHARES)) {
                        responseMessage.putM(t, incomingMessage.get(t));
                    }
                    transition.messagesToSend.add(responseMessage);
                }
            }
            break;
        }

        return transition;

    }


}