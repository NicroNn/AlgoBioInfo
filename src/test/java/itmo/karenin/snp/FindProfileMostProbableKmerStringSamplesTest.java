package itmo.karenin.snp;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class FindProfileMostProbableKmerStringSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.FIND_PROFILE_MOST_PROBABLE_KMER_STRING);
    }
}
