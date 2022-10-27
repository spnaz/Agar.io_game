package ir.spnaz.agar.Model.Objects;

public enum PowerType {
    SPEED_UP(0x247BA0, 'S'), GODMODE(0x70C1B3, 'G'), ONE_PIECE( 0xB2DBBF,'O'), DESTRUCTOR(0xFF1654,'D'), MAKE_HALF( 0xF3FFBD,'H');

    int color;
    char id;

    PowerType(int color, char id) {
        this.color = color;
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public char getId() {
        return id;
    }
}
