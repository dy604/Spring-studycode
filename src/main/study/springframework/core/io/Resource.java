package study.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public interface Resource extends InputStreamSource {

	boolean exists(); //存在性

	boolean isReadable(); //可读性

	boolean isOpen(); //是否处于打开

	URL getURL() throws IOException;

	URI getURI() throws IOException;

	File getFile() throws IOException;

	long contentLength() throws IOException;

	long lastModified() throws IOException;

	Resource createRelative(String relativePath) throws IOException; //基于当前资源创建一个相对资源

	String getFilename();

	String getDescription(); //在错误处理中的打印信息
}
}