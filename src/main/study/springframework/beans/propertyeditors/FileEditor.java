package study.springframework.beans.propertyeditors;

import study.springframework.core.io.Resource;
import study.springframework.core.io.ResourceEditor;
import study.springframework.util.Assert;
import study.springframework.util.ResourceUtils;
import study.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;

/**
 * Created by pc on 2016/10/13.
 */
public class FileEditor extends PropertyEditorSupport {

    private final ResourceEditor resourceEditor;


    /**
     * Create a new FileEditor,
     * using the default ResourceEditor underneath.
     */
    public FileEditor() {
        this.resourceEditor = new ResourceEditor();
    }

    /**
     * Create a new FileEditor,
     * using the given ResourceEditor underneath.
     * @param resourceEditor the ResourceEditor to use
     */
    public FileEditor(ResourceEditor resourceEditor) {
        Assert.notNull(resourceEditor, "ResourceEditor must not be null");
        this.resourceEditor = resourceEditor;
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
            return;
        }

        // Check whether we got an absolute file path without "file:" prefix.
        // For backwards compatibility, we'll consider those as straight file path.
        if (!ResourceUtils.isUrl(text)) {
            File file = new File(text);
            if (file.isAbsolute()) {
                setValue(file);
                return;
            }
        }

        // Proceed with standard resource location parsing.
        this.resourceEditor.setAsText(text);
        Resource resource = (Resource) this.resourceEditor.getValue();

        // If it's a URL or a path pointing to an existing resource, use it as-is.
        if (ResourceUtils.isUrl(text) || resource.exists()) {
            try {
                setValue(resource.getFile());
            }
            catch (IOException ex) {
                throw new IllegalArgumentException(
                        "Could not retrieve File for " + resource + ": " + ex.getMessage());
            }
        }
        else {
            // Create a relative File reference and hope for the best.
            setValue(new File(text));
        }
    }

    @Override
    public String getAsText() {
        File value = (File) getValue();
        return (value != null ? value.getPath() : "");
    }
}
