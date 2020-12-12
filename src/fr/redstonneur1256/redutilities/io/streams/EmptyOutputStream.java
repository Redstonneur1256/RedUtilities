package fr.redstonneur1256.redutilities.io.streams;

import java.io.OutputStream;

public class EmptyOutputStream extends OutputStream {

    public EmptyOutputStream() {
        super();
    }

    @Override
    public void write(int b) {

    }

    @Override
    public void write(byte[] b) {

    }

    @Override
    public void write(byte[] b, int off, int len) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }

}
