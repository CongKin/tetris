
package packets;

import java.io.Serializable;

public class SignalPacket implements Serializable {
    private String message;
    //private String hostName = null;

    public SignalPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    /*public String getHostName() {
    return hostName;
    }
    
    public void setHostName(String hostName) {
    this.hostName = hostName;
    }*/
}
