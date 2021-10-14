package io.spring.batch.job.tutorial.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class
Member {

    private int id;

    private String name;

    private int age;

    private String address;

    protected Member() {
    }

    @Builder
    private Member(final int id, final String name, final int age, final String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public static Member of(final int id, final String name, final int age, final String address) {
        return new Member(id, name, age, address);
    }

}
