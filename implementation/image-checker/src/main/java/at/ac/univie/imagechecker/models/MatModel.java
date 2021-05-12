package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MatModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cols;
    private int rows;
    private int type;
    private float[] data;

}
