package study.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dy on 2016/10/10.
 */
public interface WritableResource extends Resource {

    boolean isWritale();

    OutputStream getOutputStream() throws IOException;
}
