package net.m3u8.main;

import day0705.Main;
import day0705.Video;
import net.m3u8.download.M3u8DownloadFactory;
import net.m3u8.listener.DownloadListener;
import net.m3u8.utils.Constant;

import java.util.Queue;

public class Down {

    public static void go(Queue<Video> queue) {
        if (queue.isEmpty()) {
            System.out.println("所\t有\t任\t务\t完\t成\t!");
            return;
        }
        Video poll = queue.poll();
        M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getInstance(poll.getUrl());
        //设置生成目录
        m3u8Download.setDir("Video/" + poll.getDir());
        //设置视频名称
        m3u8Download.setFileName(poll.getName());
        //设置线程数
        m3u8Download.setThreadCount(10);
        //设置重试次数
        m3u8Download.setRetryCount(5);
        //设置连接超时时间（单位：毫秒）
        m3u8Download.setTimeoutMillisecond(5000L);
        /*
        设置日志级别
        可选值：NONE INFO DEBUG ERROR
        */
        m3u8Download.setLogLevel(Constant.INFO);
        //设置监听器间隔（单位：毫秒）
        m3u8Download.setInterval(5000L);
        //添加额外请求头
        /*Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "text/html;charset=utf-8");
        m3u8Download.addRequestHeaderMap(headersMap);*/

        //先清空
        m3u8Download.clearListener();
        //添加监听器
        m3u8Download.addListener(new DownloadListener() {
            @Override
            public void start() {
                System.out.println();
                System.out.println("开始下载 \t" + poll.getName());
            }

            @Override
            public void process(String downloadUrl, int finished, int sum, float percent) {
            }

            @Override
            public void speed(String speedPerSecond) {

            }

            @Override
            public void end() {
                System.out.println();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("下载完毕✌✌✌ \t" + poll.getName());
                System.out.println();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                go(queue);
            }
        });
        //开始下载
        m3u8Download.start();
    }

}
