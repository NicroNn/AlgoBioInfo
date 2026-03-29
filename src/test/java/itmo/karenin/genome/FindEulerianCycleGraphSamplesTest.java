package itmo.karenin.genome;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class FindEulerianCycleGraphSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.FIND_EULERIAN_CYCLE_GRAPH);
    }
}
