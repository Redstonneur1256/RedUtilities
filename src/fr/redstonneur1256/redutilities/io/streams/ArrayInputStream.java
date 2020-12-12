package fr.redstonneur1256.redutilities.io.streams;

import fr.redstonneur1256.redutilities.Maths;

import java.io.InputStream;
import java.util.List;

// Used for a class that will come in the future
public class ArrayInputStream extends InputStream {

    private List<byte[]> data;
    private int index;
    private byte[] current;
    private int currentPos;

    public ArrayInputStream(List<byte[]> data) {
        this.data = data;
        this.index = -1;
        this.current = null;
        this.currentPos = 0;
        this.peekArray(0);
    }

    @Override
    public synchronized int read() {
        if(current == null || currentPos >= current.length) {
            peekArray(index + 1);
        }
        if(current == null) {
            return -1;
        }
        return current[currentPos++];
    }

    public long availableLong() {
        long remaining = current == null ? 0 : current.length - currentPos;
        for(int i = index + 1; i < data.size(); i++) {
            remaining += data.get(i).length;
        }
        return remaining;
    }


    @Override
    public int available() {
        return Maths.toInt(availableLong());
    }

    @Override
    public synchronized void reset() {
        peekArray(0);
    }

    private void peekArray(int pos) {
        if(pos >= data.size()) {
            this.index = pos;
            this.current = null;
            this.currentPos = 0;
            return;
        }
        byte[] value = data.get(pos);

        this.index = pos;
        this.current = value;
        this.currentPos = 0;

    }

}
