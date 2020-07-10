package day0705;

import java.util.List;

public class SectionItem {
    private String sectionId;
    private String sectionName;
    private List<PointItem> pointItems;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setPointItems(List<PointItem> pointItems) {
        this.pointItems = pointItems;
    }

    public List<PointItem> getPointItems() {
        return pointItems;
    }

    public String getSectionName() {
        return sectionName.replaceAll("\\\\|/","");
    }

}
