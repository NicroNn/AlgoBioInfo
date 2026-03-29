package itmo.karenin.indexing;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ConstructBurrowsWheelerTransformStringSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.CONSTRUCT_BURROWS_WHEELER_TRANSFORM_STRING);
    }
}
