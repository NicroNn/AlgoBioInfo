package itmo.karenin.phylogenetics;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ConstructUPGMATreeSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.CONSTRUCT_UPGMA_TREE);
    }
}
