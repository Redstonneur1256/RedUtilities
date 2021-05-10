package fr.redstonneur1256.redutilities.io;

import fr.redstonneur1256.redutilities.Utils;
import fr.redstonneur1256.redutilities.Validate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GrowingBuffer {

    public static final float DEFAULT_EXPAND_FACTOR = 1.5F;

    private ByteBuffer buffer;
    private float expendFactor;

    private GrowingBuffer(ByteBuffer buffer, float expendFactor) {
        Validate.isTrue(expendFactor > 1, "expendFactor cannot be <= 1");

        this.buffer = buffer;
        this.expendFactor = expendFactor;
    }

    public static GrowingBuffer allocate(int capacity) {
        return new GrowingBuffer(ByteBuffer.allocate(capacity), DEFAULT_EXPAND_FACTOR);
    }

    public static GrowingBuffer allocate(int capacity, float expendFactor) {
        return new GrowingBuffer(ByteBuffer.allocate(capacity), expendFactor);
    }

    public static GrowingBuffer allocateDirect(int capacity) {
        return new GrowingBuffer(ByteBuffer.allocateDirect(capacity), DEFAULT_EXPAND_FACTOR);
    }

    public static GrowingBuffer allocateDirect(int capacity, float expendFactor) {
        return new GrowingBuffer(ByteBuffer.allocateDirect(capacity), expendFactor);
    }

    public static GrowingBuffer wrap(byte[] data) {
        return new GrowingBuffer(ByteBuffer.wrap(data), DEFAULT_EXPAND_FACTOR);
    }

    public static GrowingBuffer wrap(byte[] data, float expendFactor) {
        return new GrowingBuffer(ByteBuffer.wrap(data), expendFactor);
    }

    public static GrowingBuffer wrap(byte[] data, int offset, int length) {
        return new GrowingBuffer(ByteBuffer.wrap(data, offset, length), DEFAULT_EXPAND_FACTOR);
    }

    public static GrowingBuffer wrap(byte[] data, int offset, int length, float expendFactor) {
        return new GrowingBuffer(ByteBuffer.wrap(data, offset, length), expendFactor);
    }

    public byte get() {
        return buffer.get();
    }

    public byte get(int index) {
        return buffer.get(index);
    }

    public GrowingBuffer put(byte b) {
        ensureCapacity(1);
        buffer.put(b);
        return this;
    }

    public GrowingBuffer get(byte[] dst) {
        buffer.get(dst);
        return this;
    }

    public GrowingBuffer get(byte[] dst, int offset, int length) {
        buffer.get(dst, offset, length);
        return this;
    }

    public GrowingBuffer put(byte[] src) {
        ensureCapacity(src.length);
        buffer.put(src);
        return this;
    }

    public GrowingBuffer put(byte[] src, int offset, int length) {
        ensureCapacity(length);
        buffer.put(src, offset, length);
        return this;
    }

    public GrowingBuffer put(int index, byte b) {
        ensureCapacity(1);
        buffer.put(index, b);
        return this;
    }

    public char getChar() {
        return buffer.getChar();
    }

    public char getChar(int index) {
        return buffer.getChar(index);
    }

    public GrowingBuffer putChar(char value) {
        ensureCapacity(2);
        buffer.putChar(value);
        return this;
    }

    public GrowingBuffer putChar(int index, char value) {
        ensureCapacity(2);
        buffer.putChar(index, value);
        return this;
    }

    public short getShort() {
        return buffer.getShort();
    }

    public short getShort(int index) {
        return buffer.getShort(index);
    }

    public GrowingBuffer putShort(short value) {
        ensureCapacity(2);
        buffer.putShort(value);
        return this;
    }

    public GrowingBuffer putShort(int index, short value) {
        ensureCapacity(2);
        buffer.putShort(index, value);
        return this;
    }

    public int getInt() {
        return buffer.getInt();
    }

    public int getInt(int index) {
        return buffer.getInt(index);
    }

    public GrowingBuffer putInt(int value) {
        ensureCapacity(4);
        buffer.putInt(value);
        return this;
    }

    public GrowingBuffer putInt(int index, int value) {
        ensureCapacity(4);
        buffer.putInt(index, value);
        return this;
    }

    public long getLong() {
        return buffer.getLong();
    }

    public long getLong(int index) {
        return buffer.getLong(index);
    }

    public GrowingBuffer putLong(long value) {
        ensureCapacity(8);
        buffer.putLong(value);
        return this;
    }

    public GrowingBuffer putLong(int index, long value) {
        ensureCapacity(8);
        buffer.putLong(index, value);
        return this;
    }

    public float getFloat() {
        return buffer.getFloat();
    }

    public float getFloat(int index) {
        return buffer.getFloat(index);
    }

    public GrowingBuffer putFloat(float value) {
        ensureCapacity(4);
        buffer.putFloat(value);
        return this;
    }

    public GrowingBuffer putFloat(int index, float value) {
        ensureCapacity(4);
        buffer.putFloat(index, value);
        return this;
    }

    public double getDouble() {
        return buffer.getDouble();
    }

    public GrowingBuffer putDouble(double value) {
        ensureCapacity(8);
        buffer.putDouble(value);
        return this;
    }

    public double getDouble(int index) {
        return buffer.getDouble();
    }

    public GrowingBuffer putDouble(int index, double value) {
        ensureCapacity(8);
        buffer.putDouble(index, value);
        return this;
    }

    public String getString() {
        int length = getInt();
        if(length == -1) {
            return null;
        }

        byte[] data = new byte[length];
        get(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public GrowingBuffer putString(String value) {
        if(value == null) {
            putInt(-1);
            return this;
        }

        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        return putInt(data.length).put(data);
    }

    public GrowingBuffer putObject(Object value) {
        if(value == null) {
            return putString(null);
        }

        String className = value.getClass().getName();
        String json = Utils.toJson(value);
        return putString(className).putString(json);
    }

    public Object getObject() {
        String className = getString();
        if(className == null) {
            return null;
        }
        String json = getString();

        try {
            Class<?> type = Class.forName(className);
            return Utils.fromJson(json, type);
        }catch(ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public <T extends Enum<?>> T getEnum(Class<T> type) {
        return type.getEnumConstants()[getShort()];
    }

    public <T extends Enum<?>> GrowingBuffer putEnum(T value) {
        return putShort((short) value.ordinal());
    }

    public int position() {
        return buffer.position();
    }

    public GrowingBuffer position(int position) {
        buffer.position(position);
        return this;
    }

    public ByteOrder order() {
        return buffer.order();
    }

    public GrowingBuffer order(ByteOrder order) {
        buffer.order(order);
        return this;
    }

    public GrowingBuffer mark() {
        buffer.mark();
        return this;
    }

    public GrowingBuffer reset() {
        buffer.reset();
        return this;
    }

    public GrowingBuffer clear() {
        buffer.clear();
        return this;
    }

    public GrowingBuffer flip() {
        buffer.flip();
        return this;
    }

    public GrowingBuffer rewind() {
        buffer.rewind();
        return this;
    }

    public int remaining() {
        return buffer.remaining();
    }

    public int capacity() {
        return buffer.capacity();
    }

    public byte[] array() {
        return buffer.array();
    }

    public byte[] cutArray() {
        return Arrays.copyOfRange(array(), buffer.arrayOffset(), buffer.arrayOffset() + buffer.position());
    }

    public ByteBuffer buffer() {
        return buffer;
    }

    public void ensureCapacity(int bytes) {
        int neededSize = position() + bytes;

        if(capacity() >= neededSize) {
            return;
        }

        float newCapacity = capacity();

        while(newCapacity < neededSize) {
            newCapacity *= expendFactor;
        }
        int capacity = (int) newCapacity;
        int position = buffer.position();

        System.out.println("Expanding to " + Utils.sizeFormat(capacity));

        ByteBuffer newBuffer = buffer.isDirect() ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
        buffer.position(0);
        newBuffer.put(buffer);

        newBuffer.order(buffer.order());
        newBuffer.position(position);
        buffer = newBuffer;
    }

}
