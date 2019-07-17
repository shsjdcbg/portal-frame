package cn.dyx.portal.frame.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Description：客户端IP公共类
 *
 * @author dyx
 * @date 2019/7/16 17:07
 */
public class IpUtils {

    /**
     * 根据HttpServletRequest对象获取客户端真实的IP地址
     *
     * @param request HttpServletRequest对象
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (invalidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (invalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean invalidIp(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }
}
