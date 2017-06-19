import Resources.ResourcesHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by fanta on 5/28/17.
 */
public class ResourcesHelperTest {
    private String resourcesPath;
    private ResourcesHelper resourcesHelper;

    @Test(expected = IllegalArgumentException.class)
    public void initializationThrowsExceptionIfNullResourcesPathArg() throws Exception {
        new ResourcesHelper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializationThrowsExceptionIfEmptyResourcesPathArg() throws Exception {
        new ResourcesHelper("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameThrowsExceptionIfNullFileName() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameThrowsExceptionIfEmptyFileName() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameIsValidThrowsExceptionWhenInvalidExtension() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file.propertiess");
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameIsValidThrowsExceptionWhenNoExtension() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameIsValidThrowsExceptionWhenNoExtension2() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file");
    }

    @Test(expected = IllegalArgumentException.class)
    public void filenameIsValidThrowsExceptionWhenNoExtension3() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("fileproperties");
    }

    @Test
    public void filenameIsValidWhenValidParameters() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertTrue("File extension not valid",
                resourcesHelper.checkPropsFileNameValid("file.properties"));
    }

    @Test
    public void pathIsNormalizedIfResourcesPathDoesNotContainSlash() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        String normalizedPath = resourcesHelper.normalizePath(resourcesPath);
        Assert.assertEquals("Path was not normalized", normalizedPath, resourcesPath + "/");
    }

    @Test
    public void pathIsNormalizedIfResourcesPathContainSlash() throws Exception {
        resourcesPath = "/home/fanta/";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        String normalizedPath = resourcesHelper.getResourcesPath();
        Assert.assertEquals("Path was not normalized", normalizedPath, resourcesPath);
    }

    @Test
    public void fullPathIsNullWhenNullOrEmptyFileName() throws Exception {
        resourcesPath = "/home/fanta";
        ResourcesHelper resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertNull("Method should return null", resourcesHelper.getFullPath(null));
        Assert.assertNull("Method should return null", resourcesHelper.getFullPath(""));
    }

    @Test
    public void fullPathIsCorrectWhenValidFileName() throws Exception {
        resourcesPath = "/home/fanta";
        String fileName = "script.sh";
        ResourcesHelper resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertEquals("Full path not correct", resourcesHelper.getFullPath(fileName), resourcesPath + "/" + fileName);
    }

    private void init(String resourcesPath) {
        this.resourcesPath = resourcesPath;
        resourcesHelper = new ResourcesHelper(resourcesPath);
    }
}