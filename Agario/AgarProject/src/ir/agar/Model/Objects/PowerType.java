package ir.agar.Model.Objects;

import java.awt.*;
import java.io.Serializable;


public interface PowerType extends Serializable {
    Color getColor();

    boolean getAuthority(Authority authority);

    boolean isAtomic();

    boolean isAlive();

    boolean isActive();

    void cycle();

    void construct();

    void destruct();

    void setPlayer(Player player);

    Player getPlayer();
}
