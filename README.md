# image-area-search

#### 介绍
java实现的区域找图功能,单张检测用时 < 400ms

#### 原理
    把search.png当做滑动窗口，在images/xxx.png上横向和纵向扫描
    
1.在下面两种图中    
![原图](src/main/resources/images/1.png) ![原图](src/main/resources/images/2.png)

2.查找以下区域

![原图](src/main/resources/search.png)

3.标记区域的效果图

![原图](src/main/resources/2.png.mark.png)