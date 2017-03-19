package hello.batchconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AsyncBatchConfiguration.class, BatchConfiguration.class, ResourceConfiguration.class} )
public class MainConfiguration {
}
