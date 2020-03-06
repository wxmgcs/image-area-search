package cn.diyai;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author wxmgcs@gmail.com
 * @create 2020-03-06 9:54 下午
 */
public class ImageAreaSearchTest {

    ImageAreaSearch areaSearch;
    File searchFile;
    String imagesPath;

    @Before
    public void setup(){
        areaSearch = new ImageAreaSearch();
        ClassLoader classLoader = areaSearch.getClass().getClassLoader();
        String searchPath = classLoader.getResource("search.png").getPath();
        searchFile = new File(searchPath);
        imagesPath = classLoader.getResource("images").getPath();
    }

    @Test
    public void testMatch() throws IOException {
        Assert.assertFalse(areaSearch.match(new File(imagesPath,"1.png"),searchFile,true));
        Assert.assertTrue(areaSearch.match(new File(imagesPath,"2.png"),searchFile,true));
    }

    @Test
    public void testMarkArea() throws IOException {
        Assert.assertTrue(areaSearch.markArea(new File(imagesPath,"2.png"),searchFile,true));

    }
}
