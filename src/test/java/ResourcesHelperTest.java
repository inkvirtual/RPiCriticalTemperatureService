import Resources.ResourcesHelper;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fanta on 5/28/17.
 */
public class ResourcesHelperTest {
    private String resourcesPath;
    private ResourcesHelper resourcesHelper;

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidArgumentPath_null() throws Exception {
        new ResourcesHelper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidArgumentPath_empty() throws Exception {
        new ResourcesHelper("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_checkPropsFileNameValid_nullFileName() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_checkPropsFileNameValid_emptyFileName() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_checkPropsFileNameValid_invalidExtension() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file.propertiess");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_checkPropsFileNameValid_noExtension() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_checkPropsFileNameValid_noExtension2() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        resourcesHelper.checkPropsFileNameValid("file");
    }

    @Test
    public void test_checkPropsFileNameValid() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertTrue("File extension not valid",
                resourcesHelper.checkPropsFileNameValid("file.properties"));
    }

    @Test
    public void test_normalizePath_1() throws Exception {
        resourcesPath = "/home/fanta";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        String normalizedPath = resourcesHelper.normalizePath(resourcesPath);
        Assert.assertEquals("Path was not normalized", normalizedPath, resourcesPath + "/");
    }

    @Test
    public void test_normalizePath_2() throws Exception {
        resourcesPath = "/home/fanta/";
        resourcesHelper = new ResourcesHelper(resourcesPath);

        String normalizedPath = resourcesHelper.getResourcesPath();
        Assert.assertEquals("Path was not normalized", normalizedPath, resourcesPath);
    }

    @Test
    public void test_getFullPath_nullOrEmptyFileName() throws Exception {
        resourcesPath = "/home/fanta";
        ResourcesHelper resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertNull("Method should return null", resourcesHelper.getFullPath(null));
        Assert.assertNull("Method should return null", resourcesHelper.getFullPath(""));
    }

    @Test
    public void test_getFullPath_validFileName() throws Exception {
        resourcesPath = "/home/fanta";
        String fileName = "script.sh";
        ResourcesHelper resourcesHelper = new ResourcesHelper(resourcesPath);

        Assert.assertEquals("Full path not correct" , resourcesHelper.getFullPath(fileName), resourcesPath + "/" + fileName);
    }

    @Test
    public void test_getSubstring_1() throws Exception {
        init("/home/fanta");

        String content = "12left_substring_right23";

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "12left", "right23"));
    }

    @Test
    public void test_getSubstring_2() throws Exception {
        init("/home/fanta");

        String content = "_substring_";

        Assert.assertEquals("substring",
                resourcesHelper.getSubstring(content, "_", "_"));
    }

    @Test
    public void test_getSubstring_3() throws Exception {
        init("/home/fanta");

        String content = "12left_substring_right23";

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "12left", "right23"));
    }

    @Test
    public void test_getSubstring_4() throws Exception {
        init("/home/fanta");

        String content = "12left_substring_right23";

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "left", "right23"));
    }

    @Test
    public void test_getSubstring_5() throws Exception {
        init("/home/fanta");

        String content = "12left_substring_right23";

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "12left", "right"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getSubstring_invalidArgument() throws Exception {
        init("/home/fanta");

        String content = null;

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "12left", "right23"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getSubstring_invalidArgument_2() throws Exception {
        init("/home/fanta");

        String content = "";

        Assert.assertEquals("_substring_",
                resourcesHelper.getSubstring(content, "12left", "right23"));
    }

    // TODO: switch all tests related to getSubstring to use paramterized tests - more convenient!

    private void init(String resourcesPath) {
        this.resourcesPath = resourcesPath;
        resourcesHelper = new ResourcesHelper(resourcesPath);
    }
}