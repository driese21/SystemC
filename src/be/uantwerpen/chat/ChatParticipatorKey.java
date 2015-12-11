package be.uantwerpen.chat;

import be.uantwerpen.rmiInterfaces.IChatParticipator;

/**
 * Created by Dries on 8/12/2015.
 */
public class ChatParticipatorKey {
    private final String userName;
    private final IChatParticipator participator;
    private final boolean host;

    public ChatParticipatorKey(String userName, IChatParticipator participator, boolean host) {
        this.userName = userName;
        this.participator = participator;
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public IChatParticipator getParticipator() {
        return participator;
    }

    public boolean isHost() { return host; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof String) return ((String) obj).equalsIgnoreCase(userName);
        return obj instanceof ChatParticipatorKey && ((ChatParticipatorKey) obj).getUserName().equalsIgnoreCase(userName);
    }
}
