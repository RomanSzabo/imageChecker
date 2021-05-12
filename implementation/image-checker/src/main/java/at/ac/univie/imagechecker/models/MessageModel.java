package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageModel {

    private String from;
    private String image;
    private String text;
    private String date;

}
