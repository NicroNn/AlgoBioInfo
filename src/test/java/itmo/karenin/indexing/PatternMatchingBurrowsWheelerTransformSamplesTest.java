package itmo.karenin.indexing;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class PatternMatchingBurrowsWheelerTransformSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.PATTERN_MATCHING_BURROWS_WHEELER_TRANSFORM);
    }
}
