package homework.udp;

import java.io.IOException;

public class UDPServer {

    static public int PORT = 50000;

    public static void main(String[] args) throws IOException {
        int length = 1000;
        byte[] buffer = new byte[length];

        ReliablePacket packet = new ReliablePacket(buffer, length);
        ReliableSocket socket = new ReliableSocket(PORT);

        socket.receive(packet);
        String result = new String(packet.getBuffer(), 0, packet.getLength());

        System.out.println("message: " + result);
    }
}