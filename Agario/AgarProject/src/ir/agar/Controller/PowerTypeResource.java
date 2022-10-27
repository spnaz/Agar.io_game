package ir.agar.Controller;

import java.io.Serializable;


public class PowerTypeResource implements Serializable {
    private String name;
    private byte[] bin;

    public PowerTypeResource(String name, byte[] bin) {
        this.name = name;
        this.bin = bin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBin() {
        return bin;
    }

    public void setBin(byte[] bin) {
        this.bin = bin;
    }
}
