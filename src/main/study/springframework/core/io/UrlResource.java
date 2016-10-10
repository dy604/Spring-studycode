package study.springframework.core.io;

import study.springframework.util.Assert;
import study.springframework.util.ResourceUtils;
import study.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created by dy on 2016/10/10.
 */
public class UrlResource extends AbstractFileResolvingResource {

    private final URL url;

    private final URL cleanedUrl;

    private final URI uri;

    public UrlResource(URL url) {
        Assert.notNull(url, "URL must not be null");
        this.url = url;
        this.cleanedUrl = getCleanedUrl(this.url, url.toString());
        this.uri = null;
    }

    public UrlResource(URI uri) throws MalformedURLException {
        Assert.notNull(uri, "URI must not be null");
        this.url = uri.toURL();
        this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
        this.uri = null;
    }

    private URL getCleanedUrl(URL originalUrl, String originalPath) {
        try {
            return new URL(StringUtils.cleanPath(originalPath));
        } catch (MalformedURLException ex) {
            return originalUrl;
        }
    }

    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        ResourceUtils.useCachesIfNecessary(con);
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }

    @Override
    public URL getURL() throws IOException {
        return this.url;
    }

    @Override
    public URI getURI() throws IOException {
        if (this.uri != null) {
            return this.uri;
        } else {
            return super.getURI();
        }
    }

    @Override
    protected File getFile(URI uri) throws IOException {
        if (this.uri != null) {
            return super.getFile(this.uri);
        } else {
            return super.getFile();
        }
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        return new UrlResource(new RUL(this.url, relativePath));
    }

    @Override
    public String getFilename() {
        return new File(this.url.getFile()).getName();
    }

    @Override
    public String getDescription() {
        return "URL [" + this.url + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this ||
                (obj instanceof UrlResource && this.cleanedUrl.equals(((UrlResource) obj).cleanedUrl)));
    }

    @Override
    public int hashCode() {
        return this.cleanedUrl.hashCode();
    }
}
