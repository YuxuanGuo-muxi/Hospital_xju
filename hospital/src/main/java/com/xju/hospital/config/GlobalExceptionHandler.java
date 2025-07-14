package com.xju.hospital.config;


import cn.hutool.http.HttpUtil;
import com.xju.hospital.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private Result handlerErrorInfo(HttpServletRequest request, Exception e) {
        System.out.println("GlobalException成功拦截");
        log.error("{} Exception", request.getRequestURI(), e);
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        if (stackTraceElements.length > 0) {
            StackTraceElement element = stackTraceElements[0];

            //发送容错信息
            HashMap<String,Object> errMap = new HashMap<>();
            errMap.put("msg","GlobalException成功拦截:  "+"在代码文件  "+element.getFileName()+"的 " +element.getLineNumber()+"行报错，请检查代码和日志!");
            HttpUtil.post("http://localhost:6666/", errMap);

            System.out.println("Exception in file: " + element.getFileName());
            System.out.println("Exception at line: " + element.getLineNumber());
        }
        return  Result.exception(e.getMessage());
    }

}