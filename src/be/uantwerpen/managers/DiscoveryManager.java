package be.uantwerpen.managers;

import be.uantwerpen.discovery.DiscoveryListener;
import be.uantwerpen.interfaces.IDiscoveryManager;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by Dries on 19/12/2015.
 */
public class DiscoveryManager implements IDiscoveryManager {
    private DiscoveryListener discoveryListener;
    private final String mCastAddress;

    public DiscoveryManager() {
        this.discoveryListener = new DiscoveryListener(this);
        this.mCastAddress = "224.0.0.230";
        startDiscoveryListener();
    }

    /**
     * Start a new DiscoveryListener
     */
    private void startDiscoveryListener() {
        try {
            this.discoveryListener.openSocket(11336);
            this.discoveryListener.joinGroup(mCastAddress);
            new Thread(discoveryListener).start();
            System.out.println("Awaiting client discoveries");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DiscoveryListener received a discovery request and restarts his listener (low load)
     * @param datagramPacket DiscoveryPacket from a client.
     * @throws IOException
     */
    @Override
    public void discoveryRequest(DatagramPacket datagramPacket) throws IOException {
        String s = new String(datagramPacket.getData(),0,datagramPacket.getLength());
        if (s.equalsIgnoreCase("client")) {
            byte[] reply = "SERVER".getBytes();
            this.discoveryListener.reply(new DatagramPacket(reply, reply.length, datagramPacket.getAddress(), datagramPacket.getPort())); //just resend the packet, so client knows our ip
        }
        restart();
    }

    /**
     * Ends the sockets and starts another listener
     * @throws IOException
     */
    private void restart() throws IOException {
        this.discoveryListener.endConnection(mCastAddress);
        startDiscoveryListener();
    }


}
