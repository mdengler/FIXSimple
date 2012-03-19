/*
 * Copyright (C) 2012 Martin Dengler <martin@martindengler.com>
 *
 * Licensed under the GNU GPL v3+; see LICENSE file
 *
 */

package com.martindengler.proj.FIXSimple.util;

import java.io.IOException;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramSocketReader extends Reader {

    //TODO: WIP

    private DatagramSocket socket;

    public DatagramSocketReader(DatagramSocket ds) {
        this.socket = ds;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (this.socket == null)
            return -1;

        byte[] buffer = new byte[4096];
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(p);
        System.arraycopy(buffer, 0, cbuf, off, Math.min(len, p.getLength()));
        return p.getLength();
    }

    public void close() {
        this.socket = null;
    }

}