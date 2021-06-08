# 🚀 Batch ?
- 큰 단위의 작업을 일괄 처리, 현업에서는 이를 배치작업, 배치성 작업이라고 부른다
    - 대용량 데이터 계산에 주로 사용(ex> 매출 집계 등)
- 비실시간성 작업이며 규칙적인 주기(스케쥴러, 크론 등을 활용)로 실행 됨
- 트래픽이 뜸한 새벽시간대에 많이 실행되므로 서버자원을 최대로 활용

# 🚀 Spring-Batch ?

![img.png](src/images/img.png)

- Batch 처리를 쉽게 하기 위한 Spring 생태계의 Framework
- Spring Triangle (DI, AOP, PSA) 활용 가능
- 다양한 사용자를 고려해 설계되어 확장성과 사용성이 매우 좋다   
- `Job`과 `Step`으로 나뉘며 `Step`은 `Tasklet`과 `Chunk`로 나뉨  
- 간단한 작업(Tasklet), 대규모 작업(Chunk)

# 🏁 Spring-Batch 설정

### 🍔 build.gradle 설정
```groovy
implementation 'org.springframework.boot:spring-boot-starter-batch'
```

### 🍔 SpringBootApplication 설정

```java
@EnableBatchProcessing // 필수: Spring-Batch의 기능을 활성화
@SpringBootApplication
public class SpringBatchApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }
    
}

```

### 🍔 application.yaml 설정

```yaml
spring:
  batch:
    job:
      ## 실행옵션에 job name이 없을 경우 아무런 job도 실행하지 않음 (안전장치)
      ## ex) --job.name=itemReaderJob
      names: ${job.name:NONE}

    ## always - Spring-Batch DDL이 DB에 항상 반영 // 개발환경 추천
    ## embedded - Embedded DB인 경우에만 Spring-Batch DDL이 DB에 반영 // 개발환경 추천
    ## never - Spring-Batch DDL이 DB에 절대 반영되지 않음 (직접 SQL을 관리) // 운영환경 추천
    
    initialize-schema: embedded (cf.default)
```

### 🍔 Database 설정

![img_2.png](src/images/img_2.png)

`Spring-Batch`는 DB에 `Metadata table`을 생성하여, 이 데이터들을 기반으로 동작하므로 `BatchApplication`과 연결된 `DB`에 `Spring-Batch DDL`을 적용해줘야 한다

이 `DDL`은 `Spring-Batch Core`에 포함돼있으므로, 자신이 사용하는 `DB`에 맞는 `sql`파일을 적용하면 된다

```text
path: spring-batch-core/org.springframework/batch/core/*
```

![img_4.png](src/images/img_4.png)

![img_3.png](src/images/img_3.png)

---

# 🚀 Spring-Batch 구조

![img_1.png](src/images/img_1.png)


### 🍀 Metadata
- `BATCH_JOB_INSTANCE`
  - `Job`이 실행되며 생성되는 최상위 계층의 테이블
  - `job_name`과 `job_key`를 기준으로 하나의 `row`가 생성되며, 같은 `job_name`과 `job_key`가 저장될 수 없다 (Unique key)
  - `job_key`는 `BATCH_JOB_EXECUTION_PARAMS`에 저장되는 `Parameter`를 나열해 암호화해 저장한다
- `BATCH_JOB_EXECUTION`
  - Job이 실행되는 동안 시작/종료 시간, job 상태 등을 관리한다
- `BATCH_JOB_EXECUTION_PARAMS`
  - Job을 실행하기 위해 주입된 parameter 정보 저장한다
- `BATCH_JOB_EXECUTION_CONTEXT`
  - Job이 실행되며 공유해야할 데이터를  직렬화해 저장한다
- `BATCH_STEP_EXECUTION`
  - `Step`이 실행되는 동안 필요한 데이터 또는 실행된 결과 저장한다
- `BATCH_STEP_EXECUTION_CONTEXT`
  - `Step`이 실행되며 공유해야할 데이터를 직렬화해 저장한다

---

### 🍀 Job
- `Job`은 배치의 실행 단위(Application)를 의미함
- jar 실행 시 `Job Name`을 실행옵션으로 주면 해당 `Job`만 실행 할 수 있다  
- `Job`은 `JobLauncher`에 의해 실행 됨
- `Job`은 N개의 `Step`을 순차적으로 실행할 수 있고, 전체적인 흐름제어를 한다

![img_5.png](src/images/img_5.png)

- `JobInstance`: `BATCH_JOB_INSTANCE` 테이블과 매핑
  - 새로운 JobInstance의 생성 기준은 JobParameters의 중복 여부이다
  - 다른 JobParameters로 Job이 실행되면 새로운 JobInstance가 생성된다
  - 같은 JobParameters Job이 실행되면, 이미 생성된 JobInstance 실행된다.
- `JobExecution`: `BATCH_JOB_EXECUTION` 테이블과 매핑
  - JobExecution은 항상 새로 생성된다
- `JobParameters`: `BATCH_JOB_EXECUTION_PARAMS` 테이블과 매핑
- `ExecutionContext`: `BATCH_JOB_EXECUTION_CONTEXT` 테이블과 매핑
- `StepExecution`: `BATCH_STEP_EXECUTION` 테이블과 매핑
- `ExecutionContext`: `BATCH_STEP_EXECUTION_CONTEXT` 테이블과 매핑

---

### 🍀 Step
- `Step`은 `Job`의 세부 실행 단위이며, 하나의 `Job`에 여러개가 등록 될 수 있다
- `Step`은 2가지 종류로 나눌 수 있다
  - `Tasklet`: 하나의 작업을 그대로 진행(주로 작은 작업)
  - `Chunk`: 하나의 작업을 여러번 쪼개서 진행(주로 대규모 작업), 3가지 작업을 거친다
    - `ItemReader`: 배치 아이템을 읽는다. ***더이상 읽을 아이템이 없다면 `Job을 종료`***한다.
    - `ItemProcessor`: 읽은 아이템에 특정한 가공을 거친다. `optional`이므로 생략가능하다.
    - `ItemWriter`: 아이템을 최종 처리한다. 예를들면 `DB`에 `DML`을 `commit`하거나, 파일을 작성한다.

---

# 🚀 Data Sharing

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SharedJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    /**
     * <pre>
     * 각 Step끼리는 데이터 공유가 되지 않으므로
     * emptyStepKey이 출력되는게 정상
     * StepExecution은 Step하나에 종속적이며,
     * JobExecution은 Job전체에서 공유할 수 있다
     *
     * JobExecution = 전역
     * StepExecution = 지역
     * </pre>
     */
    @Bean
    public Job shareJob() {
        return jobBuilderFactory.get("shareJob")
                                .incrementer(new RunIdIncrementer())
                                .start(this.shareStep1())
                                .next(this.shareStep2())
                                .build();
    }
    
    @Bean
    public Step shareStep1() {
        return stepBuilderFactory.get("shareStep1")
                                 .tasklet((contribution, chunkContext)->{
                                     StepExecution stepExecution = contribution.getStepExecution();
            
                                     ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
                                     stepExecutionContext.putString("stepKey", "step execution context");
            
                                     JobExecution jobExecution = stepExecution.getJobExecution();
                                     ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
                                     jobExecutionContext.putString("jobKey", "job execution context");
            
                                     JobParameters jobParameters = jobExecution.getJobParameters();
                                     JobInstance jobInstance = jobExecution.getJobInstance();
            
                                     log.info(">>>>>>>>>> shareStep1\njobName: {}\nstepName: {}\nparameter:{}",
                                              jobInstance.getJobName(),
                                              stepExecution.getStepName(),
                                              jobParameters.getLong("run.id")
                                             );
            
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
    
    @Bean
    public Step shareStep2() {
        return stepBuilderFactory.get("shareStep2")
                                 .tasklet((contribution, chunkContext)->{
                                     StepExecution stepExecution = contribution.getStepExecution();
            
                                     ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
            
                                     JobExecution jobExecution = stepExecution.getJobExecution();
                                     ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
            
                                     log.info(">>>>>>>>>> shareStep2\njobKey: {}\nstepKey: {}",
                                              jobExecutionContext.getString("jobKey", "emptyJobKey"),
                                              stepExecutionContext.getString("stepKey", "emptyStepKey")
                                             );
            
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
}
```
