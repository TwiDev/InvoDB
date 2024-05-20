package ch.twidev.invodb.common.exceptions;

import java.io.Serial;
import java.net.InetSocketAddress;

public class AuthenticationException extends DriverException {

    @Serial
    private static final long serialVersionUID = 0L;
    private final InetSocketAddress endPoint;

    public AuthenticationException(InetSocketAddress inetSocketAddress, String message) {
        super(String.format("Authentication error on host %s: %s", inetSocketAddress.toString(), message));
        this.endPoint = inetSocketAddress;
    }

    private AuthenticationException(InetSocketAddress endPoint, String message, Throwable cause) {
        super(message, cause);
        this.endPoint = endPoint;
    }

    public InetSocketAddress getEndPoint() {
        return this.endPoint;
    }


}
