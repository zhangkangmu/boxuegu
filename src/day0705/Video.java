package day0705;

/**
 * 视频三要素
 * 1.视频链接地址
 * 2.视频的路径结构——目录
 * 3.视频名称
 */
public class Video {

    private String url;
    private String dir;
    private String name;

    public Video() {
    }

    public Video(String url, String dir, String name) {
        this.url = url;
        this.dir = dir;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getDir() {
        return dir;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setName(String name) {
        this.name = name;
    }

}
