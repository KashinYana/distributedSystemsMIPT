package homework.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReliableSocket {
    public static final int TIMEOUT = 50000;

    private int mPort;

    private DatagramSocket mDatagramSocket;

    public ReliableSocket() throws SocketException {
        mDatagramSocket = new DatagramSocket();
        mDatagramSocket.setSoTimeout(TIMEOUT);
    }

    public ReliableSocket(int port) throws SocketException {
        this.mPort = port;
        mDatagramSocket = new DatagramSocket(port);
        mDatagramSocket.setSoTimeout(TIMEOUT);
    }

    public boolean send(ReliablePacket packet) throws IOException {
        DatagramPacket message = packet.createDatagramPacket();
        mDatagramSocket.send(message);

        byte[] buffer = new byte[ReliablePacket.HEADER_LENGTH];
        DatagramPacket confirmation =
                new DatagramPacket(buffer, ReliablePacket.HEADER_LENGTH, packet.getAddress(), mPort);

        mDatagramSocket.receive(confirmation);

        return checkHeader(message, confirmation);
    }

    private boolean checkHeader(DatagramPacket message, DatagramPacket confirmation) {
        int messageOffset = message.getOffset();
        int confirmationOffset = confirmation.getOffset();

        for (int i = ReliablePacket.ID_LENGTH; i < ReliablePacket.HEADER_LENGTH; ++i) {
            if (message.getData()[messageOffset + i]
                    != confirmation.getData()[confirmationOffset + i]) {
                return false;
            }
        }
        return ReliablePacket.isConfirmation(confirmation);
    }

    public void receive(ReliablePacket packet) throws IOException {
        DatagramPacket message = packet.createDatagramPacket();
        mDatagramSocket.receive(message);
        packet.setLength(message.getLength() - ReliablePacket.HEADER_LENGTH);
        packet.updateBuffer();

        byte[] header = new byte[ReliablePacket.HEADER_LENGTH];
        System.arraycopy(packet.getDatagram(), 0,
                header, 0, ReliablePacket.HEADER_LENGTH);

        header[0] = (byte) ReliablePacket.TYPE.CONFIRMATION.ordinal();
        DatagramPacket confirmation =
                new DatagramPacket(
                        header,
                        ReliablePacket.HEADER_LENGTH,
                        message.getAddress(),
                        message.getPort()
                );

        mDatagramSocket.send(confirmation);
    }

}
