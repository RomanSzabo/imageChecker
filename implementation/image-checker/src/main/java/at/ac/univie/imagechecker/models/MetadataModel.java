package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MetadataModel {

    List<MetadataElementModel> metadata;

}
