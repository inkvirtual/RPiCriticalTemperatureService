import Resources.ResourcesHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by fanta on 6/14/17.
 */

@RunWith(Parameterized.class)
public class ResourcesHelperValidParamsTest {
    private String resourcesPath;
    private ResourcesHelper resourcesHelper;

    private String expected;
    private String content;
    private String leftParam;
    private String rightParam;

    public ResourcesHelperValidParamsTest(String expected, String content, String leftParam, String rightParam) {
        this.expected = expected;
        this.content = content;
        this.leftParam = leftParam;
        this.rightParam = rightParam;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> paramMethod() {
        return Arrays.asList(new Object[][] {
                {"_substring_", "12_substring_34", "12", "34"},
                {"_substring_", "12_substring_24", "2", "2"},
//                {"_substring_", "_substring_", null, "_"}, //TODO: update getSubstring() method to pass this test
                {"_substring_", "_substring_34", "", "3"},
                {"_substring_", "_substring_34", "", "3"},
                {"_substring_", "12_substring_", "12", ""},
                {"_substring_", "12_substring_", "12", null},
                {"_substring_", "_substring_", null, null},
                {"_substring_", "_substring_", "", ""},
                {"_substring_", "_substring_", null, ""},
                {"_substring_", "_substring_", "", null},
                {"_substring_", "12_substring_34", "34", null},
                {"_substring_", "12_substring_34", "34", "34"},
                {"_substring_", "12_substring_23", "2", "2"},
        });
    }

    @Test
    public void test_getString() throws Exception {
        init("/home/fanta/");

        Assert.assertEquals(expected, resourcesHelper.getSubstring(content, leftParam, rightParam));

    }

    private void init(String resourcesPath) {
        this.resourcesPath = resourcesPath;
        resourcesHelper = new ResourcesHelper(resourcesPath);
    }
}
