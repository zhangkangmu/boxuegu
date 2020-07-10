package day0705;

public class PointItem {
    private String pointId;
    private String pointName;
    private String videoId;
    private String ccVideoId;

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setCcVideoId(String ccVideoId) {
        this.ccVideoId = ccVideoId;
    }

    public String getPointName() {
        return pointName.replaceAll("\\\\|/","");
    }

    public String getVideoId() {
        return videoId;
    }

    public String getCcVideoId() {
        return ccVideoId;
    }

}
