package itmo.karenin.indexing;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ReconstructStringBurrowsWheelerTransformSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.RECONSTRUCT_STRING_BURROWS_WHEELER_TRANSFORM);
    }
}
