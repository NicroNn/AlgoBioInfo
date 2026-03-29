package itmo.karenin.alignment;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class FindHighestScoringFittingAlignmentSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.FIND_HIGHEST_SCORING_FITTING_ALIGNMENT);
    }
}
