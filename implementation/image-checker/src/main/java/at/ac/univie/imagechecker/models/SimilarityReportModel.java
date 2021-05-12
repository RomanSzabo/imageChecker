package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimilarityReportModel {

    private boolean passed;
    private boolean registered;
    private boolean isTampered;
    private List<CheckResults> checkResultsList;

    public void addCheckResult(CheckResults c) {
        checkResultsList.add(c);
    }

}
