package io.spring.batch.job.file.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TestSpecData {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate regDate;

    private String cardNumber;

    private String account;

    @ManyToOne(fetch = FetchType.LAZY)
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

}
