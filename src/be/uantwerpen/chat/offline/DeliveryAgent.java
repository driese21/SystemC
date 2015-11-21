package be.uantwerpen.chat.offline;

import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.rmiInterfaces.IChatParticipator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Dries on 21/11/2015.
 */
public class DeliveryAgent extends Thread {
    private ArrayList<IChatParticipator> participators;
    private Message message;
    private IChatParticipator newParticipator;
    private ChatNotificationType chatNotificationType;

    public DeliveryAgent(ArrayList<IChatParticipator> participators, Message message, ChatNotificationType cnt) {
        this.participators = participators;
        this.message = message;
        this.chatNotificationType = cnt;
    }

    public DeliveryAgent(ArrayList<IChatParticipator> participators, IChatParticipator newParticipator, ChatNotificationType chatNotificationType) {
        this.participators = participators;
        this.newParticipator = newParticipator;
        this.chatNotificationType = chatNotificationType;
    }

    private void deliver() {
        ArrayList<IChatParticipator> failedDelivery = new ArrayList<>(participators.size());
        failedDelivery.addAll(participators.stream().filter(chatParticipator -> !deliver(chatParticipator)).collect(Collectors.toList()));
        if (failedDelivery.size()==0) return;
        int retries=0;
        for (IChatParticipator participator : failedDelivery) {
            while (retries<5) {
                try {
                    if (!deliver(participator)) System.out.println(participator + " is unreachable...");
                    retries++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            retries=0;
        }
    }

    private boolean deliver(IChatParticipator participator) {
        try {
            if (message != null) participator.notifyListener(chatNotificationType, message);
            else if (newParticipator != null) participator.notifyListener(chatNotificationType, newParticipator);
            else System.out.println("Both message and participator are null");
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
