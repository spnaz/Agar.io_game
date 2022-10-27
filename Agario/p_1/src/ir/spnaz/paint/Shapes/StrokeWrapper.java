package ir.spnaz.paint.Shapes;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by bh on 03/08/2016.
 */
public class StrokeWrapper implements Serializable {
    private Stroke stroke = new BasicStroke(1);

    public StrokeWrapper() {
        stroke = new BasicStroke(1);
    }

    public StrokeWrapper(Stroke stroke) {
        this.stroke = stroke;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    private void writeObject(ObjectOutputStream out) throws IOException{

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        stroke = new BasicStroke(1);
    }
}
