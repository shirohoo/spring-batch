package io.batch.springbatch.job.custom;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private int id;
    private String name;
    private int age;
    private String address;
}
