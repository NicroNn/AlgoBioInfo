package itmo.karenin.alignment;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class AlignTwoStringsUsingAffineGapPenaltiesSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.ALIGN_TWO_STRINGS_USING_AFFINE_GAP_PENALTIES);
    }
}
