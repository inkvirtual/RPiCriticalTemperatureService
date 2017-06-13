package Resources;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by fanta on 5/27/17.
 */
public class ResourcesHelper {
    private String resourcesPath;

    public ResourcesHelper(String resourcesPath) {
        if (resourcesPath == null || resourcesPath.length() == 0)
            throw new IllegalArgumentException(
                    "Null or empty \"resourcesPath\" argument");

        init(resourcesPath);
    }

    private void init(String resourcesPath) {
        this.resourcesPath = normalizePath(resourcesPath);
    }

    public String normalizePath(String path) {
        if (path.charAt(path.length() - 1) != '/')
            path += '/';
        return path;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public String getFullPath(String fileName) {
        if (null == fileName || fileName.length() == 0) {
            System.err.println("null or empty filename");
            return null;
        }

        return resourcesPath + fileName;
    }

    public boolean checkPropsFileNameValid(String propsFileName) {
        if (propsFileName == null || propsFileName.length() == 0)
            throw new IllegalArgumentException("Null or empty properties file name");

        try {
            String[] tokens = propsFileName.split(".");

            if (tokens.length == 1)
                throw new IllegalArgumentException("Invalid properties file name:" + propsFileName);

            if (!propsFileName.endsWith(".properties"))
                throw new IllegalArgumentException("Invalid properties file name extension");

        } catch (Exception ex) {
            throw ex;
        }

        return true;
    }

    public String getSubstring(String content, String leftParam, String rightParam) {
        if (content == null || content.length() == 0)
            throw new IllegalArgumentException("Null or empty content");

        if (leftParam == null || leftParam.length() == 0)
            throw new IllegalArgumentException("Null or empty leftParam");

        if (rightParam == null || rightParam.length() == 0)
            throw new IllegalArgumentException("Null or empty rightParam");

        int beginIndex = content.indexOf(leftParam);
        if (beginIndex == -1)
            throw new IllegalArgumentException("Incorrect leftParam");

        String updatedContent = content.substring(beginIndex + leftParam.length());

        int endIndex = updatedContent.indexOf(rightParam);
        if (endIndex == -1)
            throw new IllegalArgumentException("Incorrect rightParam");

        return updatedContent.substring(0, endIndex);
    }
}
