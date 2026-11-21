package com.xiesn.springboot.i18n.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.i18n.error.I18nErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

/**
 * 三个演示场景：登录失败、参数在控制器层校验、文件类型不允许。
 */
@Service
public class I18nDemoService {

    private static final Logger log = LoggerFactory.getLogger(I18nDemoService.class);

    /** 教学用固定口令，不要拿到真实登录里用 */
    static final String DEMO_PASSWORD = "secret";

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "webp");

    /**
     * 校验教学口令。
     *
     * @param username 用户名，只写日志，不作为口令
     * @param password 待校验口令
     */
    public void login(String username, String password) {
        if (!DEMO_PASSWORD.equals(password)) {
            log.info("登录失败 username={}", username);
            throw new BusinessException(I18nErrorCode.LOGIN_INVALID_CREDENTIALS);
        }
        log.info("登录成功 username={}", username);
    }

    /**
     * 按扩展名判断是否允许。不含点或无扩展名一律拒绝。
     *
     * @param filename 原始文件名
     */
    public void checkFile(String filename) {
        String extension = extensionOf(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            log.info("拒绝文件类型 filename={} extension={}", filename, extension);
            throw new BusinessException(I18nErrorCode.FILE_TYPE_NOT_ALLOWED);
        }
        log.info("接受文件类型 filename={}", filename);
    }

    private static String extensionOf(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
