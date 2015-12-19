package be.uantwerpen.interfaces;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by Dries on 19/12/2015.
 */
public interface IDiscoveryManager {
    void discoveryRequest(DatagramPacket datagramPacket) throws IOException;
}
