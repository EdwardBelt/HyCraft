package es.edwardbelt.hycraft.protocol;

public enum ConnectionState {
    HANDSHAKING,
    STATUS,
    CONFIGURATION,
    LOGIN,
    PLAY;

    public static ConnectionState fromHandshakeIntent(int intent) {
        return switch (intent) {
            case 1 -> STATUS;
            case 2 -> LOGIN;
            case 3 -> CONFIGURATION;
            default -> throw new IllegalArgumentException("invalid handshake intent: " + intent);
        };
    }
}
