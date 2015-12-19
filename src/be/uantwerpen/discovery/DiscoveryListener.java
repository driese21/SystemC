package be.uantwerpen.discovery;

import be.uantwerpen.interfaces.IDiscoveryManager;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Dries on 19/12/2015.
 */
public class DiscoveryListener extends Thread {
    private final IDiscoveryManager discoveryManager;
    private MulticastSocket mSocket;

    public DiscoveryListener(IDiscoveryManager discoveryManager) {
        this.discoveryManager = discoveryManager;
    }

    /**
     * Opens Multicast socket on given port
     * @param port port to listen on
     * @throws IOException
     */
    public void openSocket(int port) throws IOException {
        this.mSocket = new MulticastSocket(port);
    }

    /**
     * Join a given multicast group
     * @param mCastAddress desired group (address) to join
     * @throws IOException
     */
    public void joinGroup(String mCastAddress) throws IOException {
        mSocket.joinGroup(InetAddress.getByName(mCastAddress));
    }

    /**
     * Receive a Discovery Request from a client and pass on to manager
     * @throws IOException
     */
    public void receive() throws IOException {
        byte[] buffer = new byte[8];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        mSocket.receive(datagramPacket);
        discoveryManager.discoveryRequest(datagramPacket);
    }

    /**
     * Send reply back, so client knows the IP
     * @param datagramPacket Datagram packet with nothing special
     * @throws IOException
     */
    public void reply(DatagramPacket datagramPacket) throws IOException {
        mSocket.send(datagramPacket);
    }

    /**
     * Leave the group and close the socket
     * @param mCastAddress Group (or address) to leave
     * @throws IOException
     */
    public void endConnection(String mCastAddress) throws IOException {
        mSocket.leaveGroup(InetAddress.getByName(mCastAddress));
        mSocket.close();
    }

    @Override
    public void run() {
        super.run();
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
