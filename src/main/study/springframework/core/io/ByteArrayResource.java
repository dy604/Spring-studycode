package study.springframework.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by dy on 2016/10/10.
 */
public class ByteArrayResource extends AbstractResource {

    private final byte[] byteArray;

    private final String description;

    public ByteArrayResource(byte[] byteArray) {
        this(byteArray, "resource loaded from byte array");
    }

    public ByteArrayResource(byte[] byteArray, String description) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Byte array must not be null");
        }
        this.byteArray = byteArray;
        this.description = (description != null ? description : "");
    }

    public final byte[] getByteArray() {
        return this.byteArray;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public long contentLength() throws IOException {
        return this.byteArray.length;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.byteArray);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this ||
                (obj instanceof ByteArrayResource && Arrays.equals(((ByteArrayResource) obj).byteArray, this.byteArray)));
    }

    @Override
    public int hashCode() {
        return (byte[].class.hashCode() * 29 * this.byteArray.length);
    }
}
