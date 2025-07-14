package com.xju.hospital.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类，用于上传图片到OSS
 */
@Component
public class AliOSSUtils {

    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId = "LTAI5tFdpkxnoQBj48Rk7AVj";
    private String accessKeySecret = "tK3S51zmIppcFJ9oET4iKOT7up11KL";
    private String bucketName = "sky-msz";
    private String suffix = "oss-cn-hangzhou.aliyuncs.com";

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        // Generate unique file name by appending UUID
        String fileName = generateUniqueFileName();

        // Upload file to OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        // Construct file access URL
        String url = constructFileUrl(fileName);

        // Close the ossClient
        ossClient.shutdown();

        return url;
    }

    private String generateUniqueFileName() {
        // Generate random prefix with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPrefix = generateRandomPrefix();
        return randomPrefix + "-" + timestamp + ".jpg";
    }

    private String generateRandomPrefix() {
        // Generate random prefix with English characters
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }


    private String constructFileUrl(String fileName) {
        // Construct file access URL
        return "https://" + bucketName + "." + suffix + "/" + fileName;
    }

}