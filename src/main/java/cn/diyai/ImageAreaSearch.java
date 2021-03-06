package cn.diyai;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author wxmgcs@gmail.com
 * @create 2020-03-06 上午11:44
 */
public class ImageAreaSearch {
    private int matchX = -1;
    private int matchY = -1;

    private void init() {
        matchX = -1;
        matchY = -1;
    }

    /**
     * @param srcFile               被查找的图
     * @param searchFile            查找的区域
     * @param statisticsElapsedTime 是否统计耗时
     * @return
     * @throws IOException
     */
    boolean match(File srcFile, File searchFile, boolean statisticsElapsedTime) throws IOException {
        init();
        long start = System.currentTimeMillis();
        boolean isMatch = match(srcFile, searchFile);
        long end = System.currentTimeMillis();
        if (statisticsElapsedTime) {
            System.out.println("耗时:" + (end - start));
        }

        return isMatch;
    }

    public boolean markArea(File srcFile, File searchFile, boolean statisticsElapsedTime) throws IOException {
        boolean isMatch = match(srcFile, searchFile, statisticsElapsedTime);
        if (!isMatch) {
            return false;
        }

        BufferedImage src = ImageIO.read(srcFile);
        BufferedImage search = ImageIO.read(searchFile);

        final int[] data = src.getRGB(matchX, matchY, search.getWidth(), search.getHeight(), null, 0, search.getWidth());
        Arrays.fill(data, 0x00000000);
        src.setRGB(matchX, matchY, search.getWidth(), search.getHeight(), data, 0, search.getWidth());
        ImageIO.write(src, "png", new File(srcFile.getParent(), srcFile.getName() + ".mark.png"));
        return true;
    }

    /**
     * 把被搜索的图片作为滑块，在样本上滑动
     *
     * @param srcFile
     * @param searchFile
     * @return
     * @throws IOException
     */
    private boolean match(File srcFile, File searchFile) throws IOException {
        BufferedImage src = ImageIO.read(srcFile);
        BufferedImage search = ImageIO.read(searchFile);
        int[][] srcRGB = getRGB(src);
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int[][] searchRGB = getRGB(search);
        int searchWidth = search.getWidth();
        int searchHeight = search.getHeight();
        int searchX = 0;
        int searchY = 0;
        int maxX = src.getWidth() - search.getWidth() - 1;
        int maxY = src.getHeight() - search.getHeight() - 1;
        int searchPointCount = searchWidth * searchHeight;
        boolean isMatch = false;
        // 滑块的移动范围
        for (int x = 0; x < maxX && !isMatch; x++) {
            for (int y = 0; y < maxY && !isMatch; y++) {
                try {
                    // 找到了匹配的起始点,比对被滑块覆盖的区域
                    if (isEqual(srcRGB[x][y], searchRGB[searchX][searchY])) {
                        boolean isExit = false;
                        searchX = 0;
                        searchY = 0;

                        if (x + searchWidth > srcWidth || y + searchHeight > srcHeight) {
                            continue;
                        }

                        // 如果在区域内找到一个不匹配，立即结束查找
                        int matchPointCount = 0;
                        for (int x1 = x; x1 < x + searchWidth && !isExit; x1++) {
                            for (int y1 = y; y1 < y + searchHeight; y1++) {
                                if (!isEqual(srcRGB[x1][y1], searchRGB[searchX][searchY])) {
                                    isExit = true;
                                    break;
                                }
                                matchPointCount++;
                                searchY++;
                            }
                            searchY = 0;
                            searchX++;
                        }

                        if (searchPointCount == matchPointCount) {
                            isMatch = true;
                            matchX = x;
                            matchY = y;
                        }
                        searchX = 0;
                        searchY = 0;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isMatch;
    }

    /**
     * 匹配两个点的rgb值是否相等
     *
     * @param rgb1
     * @param rgb2
     * @return
     */
    private boolean isEqual(int rgb1, int rgb2) {
        int[] rgbArr1 = getRGB(rgb1);
        int[] rgbArr2 = getRGB(rgb2);
        return rgbArr1[0] == rgbArr2[0]
                && rgbArr1[1] == rgbArr2[1]
                && rgbArr1[2] == rgbArr2[2];
    }

    private static int[] getRGB(int rgbBI) {
        int[] rgb = new int[3];
        rgb[0] = (rgbBI & 0xff0000) >> 16;
        rgb[1] = (rgbBI & 0xff00) >> 8;
        rgb[2] = (rgbBI & 0xff);
        return rgb;
    }

    /**
     * 将getRGB(w, h)获取的ARGB转化成RGB
     *
     * @param bfImage
     * @return
     */
    private int[][] getRGB(BufferedImage bfImage) {
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        int[][] result = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result[x][y] = bfImage.getRGB(x, y) & 0xFFFFFF;
            }
        }
        return result;
    }
}
