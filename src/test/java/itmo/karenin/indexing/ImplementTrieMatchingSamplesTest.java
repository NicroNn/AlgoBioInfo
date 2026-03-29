package itmo.karenin.indexing;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ImplementTrieMatchingSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.IMPLEMENT_TRIE_MATCHING);
    }
}
