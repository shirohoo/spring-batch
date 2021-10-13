package io.spring.batch.job.file.parser;

import io.spring.batch.job.file.model.TestSpec;
import io.spring.batch.job.file.model.TestSpecData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TestSpecFileParser {

    public static TestSpec parse(final File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());

        if (lines.size() < 1) {
            throw new IllegalArgumentException("The data in file BH165.B79.D is empty.");
        }

        return TestSpec.of(
            getWorkDate(lines),
            getInfoCount(lines),
            lines.stream()
                .map(TestSpecData::parse)
                .collect(Collectors.toUnmodifiableList())
        );

    }

    private static LocalDate getWorkDate(final List<String> lines) {
        return LocalDate.parse(
            lines.remove(0).substring(1, 9),
            DateTimeFormatter.ofPattern("yyyyMMdd")
        );
    }

    private static Long getInfoCount(final List<String> lines) {
        return Long.valueOf(
            lines.remove(lines.size() - 1).substring(1, 8)
        );
    }

}
