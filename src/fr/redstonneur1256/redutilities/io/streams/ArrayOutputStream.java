package fr.redstonneur1256.redutilities.io.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

// Used for a class that will come in the future
public class ArrayOutputStream extends OutputStream {

    private int maxArraySize;
    private List<byte[]> data;

    public ArrayOutputStream(int maxArraySize, List<byte[]> data) {
        this.maxArraySize = maxArraySize;
        this.data = data;
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[] {(byte) b});
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int offset, int len) {
        if(len < 0 || offset < 0 || offset + len < 0 || offset + len > b.length || offset > b.length || len > b.length) {
            throw new IndexOutOfBoundsException();
        }

        int off = offset;
        int remaining;
        while(off < len) {
            byte[] arr = data.isEmpty() ? null : data.get(data.size() - 1);

            remaining = len + offset - off;

            int written;
            if(arr == null || arr.length == maxArraySize) {
                written = Math.min(remaining, maxArraySize);
                data.add(Arrays.copyOfRange(b, off, off + written));

            }else {

                int oldLength = arr.length;
                int newSize = Math.min(oldLength + remaining, maxArraySize);
                int toCopy = newSize - oldLength;

                arr = Arrays.copyOf(arr, newSize);
                data.set(data.size() - 1, arr);

                System.arraycopy(b, off, arr, oldLength, toCopy);


                written = toCopy;
            }

            off += written;

        }
    }

}
