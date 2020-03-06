package cn.diyai;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author wxmgcs@gmail.com
 * @create 2020-03-06 9:54 下午
 */
public class ImageAreaSearchTest {
    @Test
    public void testMatch() throws IOException {
        ImageAreaSearch areaSearch = new ImageAreaSearch();
        ClassLoader classLoader = areaSearch.getClass().getClassLoader();
        String searchPath = classLoader.getResource("search.png").getPath();
        File searchFile = new File(searchPath);

        String imagesPath = classLoader.getResource("images").getPath();

        Assert.assertFalse(areaSearch.match(new File(imagesPath,"1.png"),searchFile,true));
        Assert.assertTrue(areaSearch.match(new File(imagesPath,"2.png"),searchFile,true));
    }
}
