package homework.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class ReliablePacket {

    private byte[] mBuffer;
    private int mLength;
    private InetAddress mInetAddress;
    private int mPort;

    private byte[] datagram;

    public static final int CODE_LENGTH = 1;
    public static final int ID_LENGTH = 7;
    public static final int HEADER_LENGTH = CODE_LENGTH + ID_LENGTH;

    public static enum TYPE {CONFIRMATION, MESSAGE};

    private final Random mRandom = new Random();

    public ReliablePacket(byte[] buffer, int length, InetAddress address, int port) {
        this.mBuffer = buffer;
        this.mLength = length;
        this.mInetAddress = address;
        this.mPort = port;

        datagram = new byte[buffer.length + HEADER_LENGTH];
    }

    public ReliablePacket(byte[] buffer, int length) throws UnknownHostException {
        this(buffer, length, InetAddress.getLocalHost(), 0);
    }

    public byte[] getBuffer() {
        return mBuffer;
    }

    public InetAddress getAddress() {
        return mInetAddress;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public byte[] getDatagram() {
        return datagram;
    }

    public static boolean isConfirmation(DatagramPacket confirmation) {
        return confirmation.getData()[confirmation.getOffset()]
                == (byte) TYPE.CONFIRMATION.ordinal();
    }

    public DatagramPacket createDatagramPacket() {
        setHeaderData();
        return new DatagramPacket(datagram, mLength + HEADER_LENGTH, mInetAddress, mPort);
    }

    private void setHeaderData() {
        System.arraycopy(mBuffer, 0, datagram, HEADER_LENGTH, mLength);
        for (int i = ID_LENGTH; i < ID_LENGTH + CODE_LENGTH; ++i) {
            datagram[i] = (byte) mRandom.nextInt(256);
        }
    }

    public void updateBuffer() {
        System.arraycopy(datagram, HEADER_LENGTH, mBuffer, 0, mLength);
    }
}
