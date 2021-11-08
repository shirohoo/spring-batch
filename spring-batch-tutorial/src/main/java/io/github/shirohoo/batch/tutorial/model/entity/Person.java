package io.github.shirohoo.batch.tutorial.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "PERSON")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private int age;

    @Column(name = "ADDRESS")
    private String address;

    protected Person() {
    }

    @Builder
    private Person(final Long id, final String name, final int age, final String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public static Person of(final Long id, final String name, final int age, final String address) {
        return new Person(id, name, age, address);
    }

}
