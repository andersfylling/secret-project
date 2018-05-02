package team.adderall.game.framework.multiplayer;

import java.nio.ByteBuffer;

public class GamePacketResponse extends GamePacket
{
    protected final ByteBuffer buffer;

    public GamePacketResponse(byte[] data) {
        buffer = ByteBuffer.allocate(GamePacket.BUFFER_SIZE);
        buffer.put(data);
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }

    public long sequence() {
        return buffer.getLong(INDEX_SEQUENCE);
    }

    public int type() {
        return (int) buffer.get(INDEX_TYPE);
    }

    public int userID() {
        return (int) buffer.get(INDEX_USER_ID);
    }

    public int gameID() {
        return (int) buffer.get(INDEX_GAME_ID);
    }

    public int extra() {
        return (int) buffer.get(INDEX_EXTRA);
    }

    // movement specific
    // type == 80
    public long x() {
        return buffer.getLong(GamePacketMovementBuilder.INDEX_X);
    }

    public long y() {
        return buffer.getLong(GamePacketMovementBuilder.INDEX_Y);
    }

    public boolean inAir() {
        byte inAir = buffer.get(GamePacketMovementBuilder.INDEX_IN_AIR);
        return inAir == GamePacketMovementBuilder.IS_IN_AIR;
    }
}
