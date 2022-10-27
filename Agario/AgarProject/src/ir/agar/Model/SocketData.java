package ir.agar.Model;

import java.io.Serializable;


public class SocketData implements Serializable {
    private DataType type;
    private Serializable data;

    public SocketData(DataType type, Serializable data) {
        this.type = type;
        this.data = data;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    public enum DataType{
        CONNECTION_MODE, REGISTER, REGISTER_RESPONSE, LOGIN, LOGIN_RESPONSE, SERVER_DATA, SERVER_DATA_RESPONSE, GAME_DATA, MOUSE_CORDS, NEW_CLASSES, LOOSE
    }
}
