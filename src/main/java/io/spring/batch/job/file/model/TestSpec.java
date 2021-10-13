package io.spring.batch.job.file.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
public class TestSpec {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate workDate;

    private long dataSize;

    @Getter
    @OneToMany(mappedBy = "testSpec", cascade = CascadeType.ALL)
    private List<TestSpecData> dataList = new ArrayList<>();

    protected TestSpec() {
    }

    private TestSpec(final LocalDate workDate, final long dataSize, final List<TestSpecData> dataList) {
        this.workDate = workDate;
        this.dataSize = dataSize;
        this.dataList = dataList;
    }

    public static TestSpec of(final LocalDate workDate, final long dataSize, final List<TestSpecData> dataList) {
        return new TestSpec(workDate, dataSize, dataList);
    }

    public boolean verify() {
        return dataSize == dataList.size();
    }

}
