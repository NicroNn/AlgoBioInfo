package itmo.karenin.genome;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ConstructDeBruijnGraphStringSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.CONSTRUCT_DE_BRUIJN_GRAPH_STRING);
    }
}
