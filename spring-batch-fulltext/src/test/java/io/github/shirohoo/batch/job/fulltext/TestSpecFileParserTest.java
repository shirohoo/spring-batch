package io.github.shirohoo.batch.job.fulltext;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import org.junit.jupiter.api.Test;

class TestSpecFileParserTest {

    public static final String TEST_FILE_PATH = "src/test/resources/TestSpecFile";

    @Test
    void verify() throws Exception {
        TestSpec testSpec = TestSpecFileParser.parse(new File(TEST_FILE_PATH));
        assertThat(testSpec.verify()).isTrue();
    }

}
