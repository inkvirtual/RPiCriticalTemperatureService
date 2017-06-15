import Resources.ResourcesHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by fanta on 6/14/17.
 */

@RunWith(Parameterized.class)
public class ResourcesHelperInvalidParamsTest {
    private String resourcesPath;
    private ResourcesHelper resourcesHelper;

    private String content;
    private String leftParam;
    private String rightParam;

    public ResourcesHelperInvalidParamsTest(String content, String leftParam, String rightParam) {
        this.content = content;
        this.leftParam = leftParam;
        this.rightParam = rightParam;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> paramMethod() {
        return Arrays.asList(new Object[][] {
                {null, "12", "34"},              //0
                {"", "12", "34"},                //1
                {"12_substring_34", "34", "34"}, //2
                {"12_substring_34", "23", "34"}, //3
                {"12_substring_34", "33", "33"}, //4
                {"12_substring_34", "12", "33"}, //5
                {"12_substring_34", "", "33"},   //6
                {"12_substring_34", "34", "34"}, //7
                {"12_substring_34", null, "33"}, //8
                {null, null, null},              //9
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getString() throws Exception {
        init("/home/fanta/");

        resourcesHelper.getSubstring(content, leftParam, rightParam);
    }

    private void init(String resourcesPath) {
        this.resourcesPath = resourcesPath;
        resourcesHelper = new ResourcesHelper(resourcesPath);
    }
}
