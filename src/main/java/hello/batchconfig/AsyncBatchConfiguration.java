package hello.batchconfig;

import hello.Person;
import hello.batchimpl.JobCompletionNotificationListener;
import hello.batchimpl.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.concurrent.Future;

@Configuration
@Import({BatchConfiguration.class})
public class AsyncBatchConfiguration {

    @Autowired
    private BatchConfiguration batchConfiguration;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(asyncStep())
                .end()
                .build();
    }

    @Bean
    public Step asyncStep() {
        return stepBuilderFactory.get("asyncStep")
                .<Person, Future<Person>>chunk(3)
                .reader(batchConfiguration.reader())
                .processor(asyncProcessor())
                .writer(asyncWriter())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public ItemProcessor<Person, Future<Person>> asyncProcessor() {
        AsyncItemProcessor<Person, Person> asyncProcessor = new AsyncItemProcessor<>();
        asyncProcessor.setDelegate(new PersonItemProcessor());
        asyncProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncProcessor;
    }

    @Bean
    public ItemWriter<Future<Person>> asyncWriter() {
//        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
//        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
//        writer.setDataSource(dataSource);
        AsyncItemWriter<Person> personAsyncItemWriter = new AsyncItemWriter<>();
        personAsyncItemWriter.setDelegate(batchConfiguration.writer());
        return personAsyncItemWriter;
    }

}
