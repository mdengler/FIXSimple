package com.martindengler.proj.FIXSimple;

import com.martindengler.proj.FIXSimple.spec.Tag;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.martindengler.proj.FIXSimple.FIXMessage.*;


public class FIXStreamInputHandler implements Callable<Object> {

    private FIXStream parent;
    private Reader inputReader;

    public FIXStreamInputHandler(FIXStream parent, Reader inputReader) {
        this.parent = parent;
        this.inputReader = inputReader;
    }

    public Object call() {

        FIXMessage readMessage = null;
        FIXStream.State state = parent.getState();

        while (state == FIXStream.State.ACTIVE
                || state == FIXStream.State.CLOSED_OUT) {

            try {

                readMessage = readOneMessage(inputReader);

            } catch (IOException ioe) {
                readMessage = null;
                System.err.println("FIXStreamInputHandler: " +
                        " readOneMessage threw IOException; stopping input.");
                System.err.println(ioe.getMessage());
                System.err.println(ioe.toString());
                switch (state) {
                case ACTIVE:
                    parent.setState(FIXStream.State.CLOSED_IN);
                    break;
                case CLOSED_OUT:
                    parent.setState(FIXStream.State.CLOSED_ALL);
                    break;
                }
            } catch (RuntimeException e) {
                System.err.println("FIXStreamInputHandler:" +
                        " readOneMessage threw IOException;" +
                        " stopping.");
                System.err.println(e.getMessage());
                System.err.println(e.toString());
                switch (state) {
                case ACTIVE:
                    parent.setState(FIXStream.State.CLOSED_IN);
                    break;
                case CLOSED_OUT:
                    parent.setState(FIXStream.State.CLOSED_ALL);
                    break;
                }
                throw e;
            }

            if (readMessage == null) {
                System.err.println("FIXStreamInputHandler:" +
                        " stream is closed for messages");
                switch (state) {
                case ACTIVE:
                    parent.setState(FIXStream.State.CLOSED_IN);
                    break;
                case CLOSED_OUT:
                    parent.setState(FIXStream.State.CLOSED_ALL);
                    break;
                }

                return null;

            } else {
                System.err.format("FIXStreamInputHandler:" +
                        " got message from readOneMessage: %s%n",
                        readMessage);

                parent.inputQueue().add(readMessage);

            }

        }

        System.err.println("FIXStreamInputHandler finished");

        return null;

    }


    /**
     * returns null if there are no more messages to read
     */
    private FIXMessage readOneMessage(Reader inputReader) throws IOException {
        //tl;dr: this method is long, and very ugly
        //
        // So we have to parse a byte stream of unknown length to read
        // exactly the FIX message for FIXMessage.fromBytes().
        //
        // Here's what we do:
        //
        // 0) create the variable "buffer" to hold the FIX message
        // 1) fill it until we get the "preamble": up to the BodyLength value
        // 2) carefully read the BodyLength value (also put in buffer)
        // 3) using BodyLength, read the rest (pre-checksum) into buffer
        // 4) read the checksum tag/value pair into buffer
        // 5) use FIXMessage.fromBytes(buffer) to return the message


        //TODO: ensure we don't overflow this buffer
        int bufferLength = 10 * 1024;

        byte[] buffer = new byte[bufferLength];
        Arrays.fill(buffer, SOH_byte);  // TODO: document / consider repercussions of this guard value

        int lastReadCharacter = -1;
        int countReadCharacters = 0;

        // inefficient but simpler
        byte[] preambleBuffer = new byte[FIX_PREAMBLE.length];

        while (!Arrays.equals(FIX_PREAMBLE, preambleBuffer)
                &&
                (lastReadCharacter = inputReader.read()) != -1) {

            preambleBuffer[countReadCharacters] = (byte) lastReadCharacter;
            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if ((lastReadCharacter == -1)
                &&
                countReadCharacters == 0) {
            return null;
        }

        if (!Arrays.equals(FIX_PREAMBLE, preambleBuffer)) {
            String msg = String.format("problem reading FIX stream;" +
                    "read %s but no FIX Preamble (%s) found.",
                    new String(preambleBuffer).replace(FIXMessage.SOH, "<SOH>"),
                    new String(FIX_PREAMBLE).replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        int bodyLengthStartIndex = countReadCharacters;
        while ((lastReadCharacter = inputReader.read()) != -1
                &&
                lastReadCharacter != (int) SOH_byte) {

            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if (lastReadCharacter != (int) SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    "read %s but SOH character found.",
                    new String(buffer, 0, countReadCharacters)
                    .replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        /* we should have started reading message length at buffer index
         * 12, and say the length was "256", so countReadCharacters goes
         * 12 - get new lastReadCharacter, it's 2, countReadCharacters = 13
         * 13 - get new lastReadCharacter, it's 5, countReadCharacters = 14
         * 14 - get new lastReadCharacter, it's 6, countReadCharacters = 15
         * 15 - get new lastReadCharacter, it's SOH, countReadCharacters = 15
         * so the bodyLengthOffset is 15 - 12 = 3.
         */
        int bodyLengthOffset = (countReadCharacters - bodyLengthStartIndex);

        // now update the buffer with the read SOH and increase the count
        buffer[countReadCharacters++] = (byte) lastReadCharacter;  // it's SOH

        Integer bodyLength = Integer.parseInt(new String(buffer,
                        bodyLengthStartIndex,
                        bodyLengthOffset));
        Integer restOfBodyLength = bodyLength - countReadCharacters;

        // FUTURE: this seems a bit risky, consider how to make it safer
        for (int i=0; i < restOfBodyLength; i++)
            buffer[countReadCharacters++] = (byte) inputReader.read();

        if (buffer[countReadCharacters - 1] != SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    " was supposed to read %s chars before trailer" +
                    " (per fix message prefix %s)" +
                    " but last char read was not SOH; read [%s]",
                    restOfBodyLength,
                    new String(buffer, 0, bodyLengthStartIndex)
                    .replace(FIXMessage.SOH, "<SOH>"),
                    new String(buffer, 0, countReadCharacters)
                                       ); //this is why eclipse indenting FAILs
            throw new IllegalStateException(msg);
        }


        // we just have checksum tag/value pair + its SOH left
        Integer startOfChecksumIndex = countReadCharacters;
        byte[] checksumTagBytes = Tag.CHECKSUM.getCode()
            .toString().getBytes(ISO_8859_1);
        while ((lastReadCharacter = inputReader.read()) != -1
                &&
                lastReadCharacter != (int) SOH_byte) {

            buffer[countReadCharacters++] = (byte) lastReadCharacter;
        }

        if (lastReadCharacter != (int) SOH_byte) {
            String msg = String.format("problem reading FIX stream;" +
                    " was supposed to read trailing checksum tag/value" +
                    " pair and SOH, but actually read: [%s]",
                    new String(buffer,
                            startOfChecksumIndex,
                            countReadCharacters - startOfChecksumIndex - 1)
                    .replace(FIXMessage.SOH, "<SOH>"));
            throw new IllegalStateException(msg);
        }

        // now update the buffer with the read SOH and increase the count
        buffer[countReadCharacters++] = (byte) lastReadCharacter;  // it's SOH

        FIXMessage message = FIXMessage.fromWire(Arrays.
                copyOfRange(buffer, 0, countReadCharacters));
        return message;

    }

}