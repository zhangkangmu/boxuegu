package day0705;

import java.util.List;

public class Module {
    private String moduleName;
    private String studentCourseId;
    private List<SectionItem> sectionItems;

    public void setSectionItems(List<SectionItem> sectionItems) {
        this.sectionItems = sectionItems;
    }

    public List<SectionItem> getSectionItems() {
        return sectionItems;
    }

    public String getModuleName() {
        return moduleName.replaceAll("\\\\|/", "");
    }

    public String getStudentCourseId() {
        return studentCourseId;
    }


    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setStudentCourseId(String studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

}
