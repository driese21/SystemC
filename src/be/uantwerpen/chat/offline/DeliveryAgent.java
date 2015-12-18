package be.uantwerpen.chat.offline;

import be.uantwerpen.chat.ChatParticipatorKey;
import be.uantwerpen.chat.Message;
import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.rmiInterfaces.IChatParticipator;

import java.rmi.RemoteException;
import java.util.HashSet;

/**
 * Created by Dries on 21/11/2015.
 *
 * This class delivers the messages to the participators.
 */
public class DeliveryAgent extends Thread {
    private HashSet<ChatParticipatorKey> participators;
    private Message message;
    private ChatParticipatorKey cpk;
    private ChatNotificationType chatNotificationType;

    public DeliveryAgent(HashSet<ChatParticipatorKey> participators, Message message, ChatNotificationType cnt) {
        this.participators = participators;
        this.message = message;
        this.chatNotificationType = cnt;
    }

    public DeliveryAgent(HashSet<ChatParticipatorKey> participators, ChatParticipatorKey cpk, ChatNotificationType chatNotificationType) {
        this.participators = participators;
        this.cpk = cpk;
        this.chatNotificationType = chatNotificationType;
    }

    /**
     * Checks if all participators are reachable.
     * If a participator is unreachable, first send the message to the reachable participators,
     * and then retry delivery to the unreachable participator 5 times.
     *
     */
    private void deliver() {
        HashSet<ChatParticipatorKey> failedDelivery = new HashSet<>(participators.size());
        participators.forEach(cpk -> { if (!deliver(cpk.getParticipator())) failedDelivery.add(cpk);});
        if (failedDelivery.size()==0) return;
        failedDelivery.forEach(cpk -> {
            int retries = 0;
            while (retries < 5) {
                try {
                    if (!deliver(cpk.getParticipator())) System.out.println(cpk.getUserName() + " is unreachable...");
                    retries++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Takes care of the delivery to reachable chat participators
     *
     * @param participator the participator(s) who need to get the message
     * @return
     */
    private boolean deliver(IChatParticipator participator) {
        try {
            if (message != null) participator.notifyListener(message);
            else if (cpk != null) {
                if (chatNotificationType == ChatNotificationType.USERJOINED)
                    participator.notifyListener(cpk.getParticipator(), cpk.isHost());
                else if (chatNotificationType == ChatNotificationType.USERLEFT)
                    participator.notifyListener(cpk.getUserName());
            } else System.out.println("Both message and participator are null");
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        super.run();
        deliver();
    }
}
