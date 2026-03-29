package itmo.karenin.snp;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ImplementMotifEnumerationSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.IMPLEMENT_MOTIF_ENUMERATION);
    }
}
