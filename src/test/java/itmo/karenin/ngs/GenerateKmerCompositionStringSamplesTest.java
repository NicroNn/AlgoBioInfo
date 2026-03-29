package itmo.karenin.ngs;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class GenerateKmerCompositionStringSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.GENERATE_KMER_COMPOSITION_STRING);
    }
}
