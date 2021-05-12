package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BasicResponse {

    int status;
    String message;

}
