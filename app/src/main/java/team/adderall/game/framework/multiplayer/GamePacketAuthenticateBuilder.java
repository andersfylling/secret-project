package team.adderall.game.framework.multiplayer;

import java.nio.ByteBuffer;

public class GamePacketAuthenticateBuilder
{

    // movement related
    private final static int INDEX_TOKEN = 12;

    private ByteBuffer buffer;

    public GamePacketAuthenticateBuilder(long sequence, byte type, int userID, int gameID) {
        buffer = ByteBuffer.allocate(GamePacket.BUFFER_SIZE);

        buffer.putLong(GamePacket.INDEX_SEQUENCE, sequence);
        buffer.put(GamePacket.INDEX_TYPE, type);
        buffer.put(GamePacket.INDEX_USER_ID, (byte) userID);
        buffer.put(GamePacket.INDEX_GAME_ID, (byte) gameID);
    }

    public GamePacketAuthenticateBuilder extra(byte extra) {
        buffer.put(GamePacket.INDEX_EXTRA, extra);
        return this;
    }

    public GamePacketAuthenticateBuilder token(Long gameToken) {
        buffer.putLong(INDEX_TOKEN, gameToken);
        return this;
    }

    public GamePacket build() {
        return new GamePacket(buffer.array());
    }
}
