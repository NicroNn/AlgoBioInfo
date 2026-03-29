package itmo.karenin.alignment;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class FindHighestScoringGlobalAlignmentTwoStringsSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.FIND_HIGHEST_SCORING_GLOBAL_ALIGNMENT_TWO_STRINGS);
    }
}
