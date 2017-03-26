package hello.batchimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;

public class LoggedFlatFileItemReader<T> extends FlatFileItemReader<T>{

    Logger log = LoggerFactory.getLogger(LoggedFlatFileItemReader.class);

    @Override
    protected T doRead() throws Exception {
        T read = super.doRead();
        log.debug("read: "+read.toString());
        return read;
    }
}
