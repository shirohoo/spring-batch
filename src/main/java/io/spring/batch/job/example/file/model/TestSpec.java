package io.spring.batch.job.example.file.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "TEST_SPEC")
public class TestSpec {

    @Id
    @Column(name = "TEST_SPEC_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WORK_DATE")
    private LocalDate workDate;

    @Column(name = "DATA_SIZE")
    private long dataSize;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEST_SPEC_ID")
    private List<TestSpecData> dataList = new ArrayList<>();

    protected TestSpec() {
    }

    private TestSpec(final LocalDate workDate, final long dataSize, final List<TestSpecData> dataList) {
        this.workDate = workDate;
        this.dataSize = dataSize;
        this.dataList = new ArrayList<>(dataList);
    }

    public static TestSpec of(final LocalDate workDate, final long dataSize, final List<TestSpecData> dataList) {
        return new TestSpec(workDate, dataSize, dataList);
    }

    public boolean verify() {
        return dataSize == dataList.size();
    }

}
