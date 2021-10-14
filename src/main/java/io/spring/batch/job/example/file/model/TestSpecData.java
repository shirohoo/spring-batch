package io.spring.batch.job.example.file.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_SPEC_DATA")
public class TestSpecData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEST_SPEC_DATA_ID")
    private Long id;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "ACCOUNT")
    private String account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_SPEC_ID")
    private TestSpec testSpec;

    protected TestSpecData() {
    }

    private TestSpecData(final LocalDate regDate, final String cardNumber, final String account) {
        this.regDate = regDate;
        this.cardNumber = cardNumber;
        this.account = account;
    }

    public static TestSpecData of(final LocalDate regDate, final String cardNumber, final String account) {
        return new TestSpecData(regDate, cardNumber, account);
    }

    public static TestSpecData parse(final String data) {
        LocalDate regDate = null;
        String cardNumber = "";
        String account = "";

        int length = data.trim().length();

        if (length > 0) {
            regDate = LocalDate.parse(
                data.substring(1, 9),
                DateTimeFormatter.ofPattern("yyyyMMdd")
            );
        }

        if (length > 8) {
            cardNumber = data.substring(9, 25);
        }

        if (length > 25) {
            account = data.substring(25, 41);
        }

        return TestSpecData.of(regDate, cardNumber, account);
    }

    public void setTestSpec(final TestSpec testSpec) {
        this.testSpec = testSpec;
    }

}
