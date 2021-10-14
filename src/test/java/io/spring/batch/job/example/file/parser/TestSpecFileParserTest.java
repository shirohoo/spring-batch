package io.spring.batch.job.example.file.parser;

import static org.assertj.core.api.Assertions.assertThat;
import io.spring.batch.job.example.file.model.TestSpec;
import io.spring.batch.job.example.file.model.TestSpecData;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestSpecFileParserTest {

    public static final String TEST_FILE_PATH = "src/test/resources/TestSpecFile";

    private final TestSpecFileParser parser = new TestSpecFileParser();

    private List<TestSpecData> mockDataList;

    @BeforeEach
    void setUp() {
        mockDataList = List.of(
            TestSpecData.of(LocalDate.of(2021, 8, 13), "4129210000000409", "4129210000000409"),
            TestSpecData.of(LocalDate.of(2021, 8, 13), "4129210000000409", ""),
            TestSpecData.of(LocalDate.of(2021, 8, 13), "4129210000000409", "4129210000000409"),
            TestSpecData.of(LocalDate.of(2021, 8, 13), "4129210000000409", "")
        );
    }

    @Test
    void verify() throws Exception {
        TestSpec testSpec = parser.parse(new File(TEST_FILE_PATH));
        assertThat(testSpec.verify()).isTrue();
    }

    @Test
    void parse() throws Exception {
        TestSpec infoOfDay = parser.parse(new File(TEST_FILE_PATH));
        List<TestSpecData> dataList = infoOfDay.getDataList();
        assertThat(dataList).usingRecursiveComparison().isEqualTo(mockDataList);
    }

}
