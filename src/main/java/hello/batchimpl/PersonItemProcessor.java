package hello.batchimpl;

import hello.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info(Thread.currentThread().getId() + " Converting (" + person + ") into (" + transformedPerson + ")");
        Thread.sleep(200);
//        Thread.sleep(10 + new Random().nextInt(1000));
        log.debug(Thread.currentThread().getId() + " done");
        return transformedPerson;
    }


}
