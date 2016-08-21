package com.annwyn.image.show.utils;


import com.annwyn.image.show.ImageApplication;

import java.text.MessageFormat;

import javax.xml.validation.Validator;

public final class WebAPI {

    private static final String HOST = "http://api.lovebizhi.com/android_v3.php";

    // 首页
    private static final String HOME = "{0}?a=home&mode=2&client_id=1001&model_id=100&channel_id=26&size_id=0&spdy=1&version_code=75&language=zh-CN&mac=&original=0&device=random&uuid=random&device_id=random&screen_width={1}&screen_height={2}&bizhi_width={3}&bizhi_height={4}&p={5}";

    // 分类列表
    private static final String DASHBOARD_LIST = "{0}?a=browse&id=category&spdy=1&device=random&uuid=random&mode=2&client_id=1001&device_id=random&model_id=100&size_id=0&channel_id=26&version_code=75&language=zh-CN&mac=&original=0&screen_width={1}&screen_height={2}&bizhi_width={3}&bizhi_height={4}";

    // 专题列表
    private static final String SPECIAL_LIST = "{0}?a=browse&id=special&mode=2&spdy=1&client_id=1001&size_id=0&channel_id=26&model_id=100&version_code=75&language=zh-CN&mac=&original=0&device=random&device_id=random&uuid=random&screen_width={1}&screen_height={2}&bizhi_width={3}&bizhi_height={4}&p={5}";

    // 詳細列表(分类和列表的详细href中a部分不同)
    @Deprecated
    private static final String DETAIL_LIST_DEPRECATED = "{0}?a=category&mode=2&client_id=1001&order=newest&color_id=3&model_id=100&channel_id=26&size_id=0&spdy=1&version_code=75&language=zh-CN&mac=&original=0&device=random&uuid=random&device_id=random&screen_width={1}&screen_height={2}&bizhi_width={3}&bizhi_height={4}&tid={5}&p={6}";

    // 详细列表
    private static final String DETAIL_LIST = "{0}&p={1}";

    /**
     * 首頁
     * @param page 页码
     * @return url
     */
    public static String getHome(int page) {
        return MessageFormat.format(HOME, HOST, ImageApplication.width, ImageApplication.height,
                ImageApplication.width, ImageApplication.height, String.valueOf(page));
    }

    /**
     * 分类列表
     * @return url
     */
    public static String getDashboardList() {
        return MessageFormat.format(DASHBOARD_LIST, HOST, ImageApplication.width,
                ImageApplication.height, ImageApplication.width, ImageApplication.height);
    }

    /**
     * 专题列表
     * @param page 页码
     * @return url
     */
    public static String getSpecialList(int page) {
        return MessageFormat.format(SPECIAL_LIST, HOST, ImageApplication.width,
                ImageApplication.height, ImageApplication.width, ImageApplication.height, String.valueOf(page));
    }

    /**
     * 詳細列表
     * @param dashboard 分类id
     * @param page 页码
     * @return url
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static String getDetailList(int dashboard, int page) {
        return MessageFormat.format(DETAIL_LIST_DEPRECATED, HOST, ImageApplication.width,
                ImageApplication.height, ImageApplication.width, ImageApplication.height, String.valueOf(dashboard), String.valueOf(page));
    }

    /**
     * 詳細列表
     * @param href 在获取dashboard的时候获取详细列表href的前半段
     * @param page 页码
     * @return url
     */
    public static String getDetailList(String href, int page) {
        return MessageFormat.format(DETAIL_LIST, href, String.valueOf(page));
    }

}
