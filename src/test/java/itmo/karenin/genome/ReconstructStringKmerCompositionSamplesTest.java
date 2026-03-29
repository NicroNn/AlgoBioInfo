package itmo.karenin.genome;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ReconstructStringKmerCompositionSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.RECONSTRUCT_STRING_KMER_COMPOSITION);
    }
}
