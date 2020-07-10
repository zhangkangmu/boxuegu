package day0705;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class Boxuegu {

    String courseId;
    String studentId;
    String _uc_t_;

    /**
     * 博学谷视频下载构造函数
     *
     * @param courseId  课程id
     * @param studentId 学生id
     * @param _uc_t_    认证Cookie值
     */
    public Boxuegu(String courseId, String studentId, String _uc_t_) {
        this.courseId = "&courseId=" + courseId;
        this.studentId = "&studentId=" + studentId;
        this._uc_t_ = _uc_t_;
    }

    String gradeName = null;

    public String getGradeName() {
        if (gradeName == null) {
            //https://www.boxuegu.com/courseDetail/courseInfo?courseId=xxx
            gradeName = JSONObject.parseObject(getContent("https://www.boxuegu.com/courseDetail/courseInfo?" + courseId)).getJSONObject("result").getString("gradeName");
        }
        return gradeName.replaceAll("\\\\|/", "_");
    }

    /**
     * 通过课程id报名
     *
     * @param courseId
     */
    public void signCourseById(String courseId) {
        postJson("https://www.boxuegu.com/web/save", "{\"totalAmount\":0,\"courseCoupon\":{\"" + courseId + "\":0}}", _uc_t_);
    }

    /**
     * 报名全部免费课程
     */
    public void signCourse() throws Exception {
        JSONArray list = JSON.parseObject(Boxuegu.getContent("https://www.boxuegu.com/course/all?pageNum=1&pageSize=500", _uc_t_)).getJSONArray("list");
        list.forEach(o -> {
            JSONObject object = (JSONObject) o;
            if ("0.00".equals(object.getString("coursePrice"))) {
                System.out.println(object.getString("courseId"));
                signCourseById(object.getString("courseId"));
            }
        });
    }

    /**
     * 进行网络连接
     *
     * @param url 链接地址
     * @return 返回接收到的数据
     */
    public String getContent(String url) {
        try {
            return Jsoup.connect(url).header("Cookie", "_uc_t_=" + _uc_t_).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getContent(String url, String _uc_t_) {
        try {
            return Jsoup.connect(url).header("Cookie", "_uc_t_=" + _uc_t_).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postJson(String url, String data, String _uc_t_) {
        try {
            return Jsoup.connect(url).header("Cookie", "_uc_t_=" + _uc_t_).header("Content-Type", "application/json").ignoreContentType(true).requestBody(data).method(Connection.Method.POST).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存储文件夹信息
     */
    List<String> modules = new ArrayList<>();

    /**
     * 获取文件夹信息
     *
     * @return 返回List
     */
    public List<String> getModules() {
        if (modules.size() == 0) {
            //https://www.boxuegu.com/courseStudyCenter/queryUserCourseStudyRoute?courseId=xxx
            //result[""0""].moduleAndPhaseHomeWorkList[""0""].id
            JSONObject.parseObject(getContent("https://www.boxuegu.com/courseStudyCenter/queryUserCourseStudyRoute?" + courseId))
                    .getJSONArray("result").forEach(m -> {
                ((JSONObject) m).getJSONArray("moduleAndPhaseHomeWorkList").forEach(o -> {
                    modules.add("&moduleId=" + ((JSONObject) o).getString("id"));
                });
            });

            JSONArray result = JSONObject.parseObject(getContent("https://www.boxuegu.com/courseStudyCenter/queryUserCourseStudyExtend?" + courseId))
                    .getJSONArray("result");
            if (result != null) {
                result.forEach(m -> {
                    ((JSONObject) m).getJSONArray("moduleAndPhaseHomeWorkList").forEach(o -> {
                        modules.add("&moduleId=" + ((JSONObject) o).getString("id"));
                    });
                });
            }
        }
        return modules;
    }

    /**
     * 获取sietid字段
     *
     * @param moduleId 文件夹id
     * @return 貌似都是一样的
     */
    public String getSiteId(String moduleId) {
        //https://www.boxuegu.com/coursePlay/getCourseCurrentVideo?moduleId=105488&courseId=2739
        //String siteid = "&"+JSONObject.parseObject(getContent("https://www.boxuegu.com/coursePlay/getCourseCurrentVideo?courseId=445"+moduleId))
        //        .getJSONObject("result")
        //        .getString("h5PlayCode")
        //        .split("&")[1];
        //貌似是一样的
        return "&siteid=78665FEF083498AB";
    }

    public Module getSections(String moduleId) {
        //https://www.boxuegu.com/coursePlay/getCourseKnowledgeTree?courseId=445&moduleId=100730
        //result.moduleVo.sectionItems
        JSONObject moduleVo = JSONObject.parseObject(getContent("https://www.boxuegu.com/coursePlay/getCourseKnowledgeTree?" + courseId + moduleId)).getJSONObject("result").getJSONObject("moduleVo");
        return moduleVo == null ? null : JSONObject.parseObject(moduleVo.toString(), Module.class);
    }

    public List<ModuleFile> getFiles(String moduleId) {
        return JSON.parseArray(JSON.parseObject(getContent("https://www.boxuegu.com/coursePlay/getCourseFileBySectionId?" + moduleId)).getJSONArray("result").toJSONString(), ModuleFile.class);
    }

    public String getVc(String videoId, String moduleId) {
        //https://www.boxuegu.com/ccVideo/getCcVerificationCode?type=D&studentId=b5f82539bf4a11ea881d98039b073fa8&videoId=10176039&courseId=2739&moduleId=105488
        return "&vc=" + JSON.parseObject(getContent("https://www.boxuegu.com/ccVideo/getCcVerificationCode?type=D" + studentId + "&videoId=" + videoId + courseId + moduleId)).getJSONObject("result").getString("verificationcode");
    }

    public String getM3u8Url(String vid, String siteid, String vc) {
        //https://p.bokecc.com/servlet/getvideofile?vid=04F65BB55367031F9C33DC5901307461&siteid=78665FEF083498AB&vc=891da1ed65084321a129b6f340a719db
        String m3u8 = getContent("https://p.bokecc.com/servlet/getvideofile?vid=" + vid + siteid + vc);
        JSONArray jsonArray = JSON.parseObject(m3u8.replace("null(", "").replace(")", "")).getJSONArray("copies");
        return ((JSONObject) jsonArray.get(jsonArray.size() - 1)).getString("playurl");
    }

}
