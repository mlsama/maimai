package com.maimai.admin.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * mlsama
 * 2017/12/17 22:33
 * 描述:图片上传控制器
 */
@Controller
public class PictrueUploadController {
    /** 注入分布式文件系统的请求IP地址 */
    @Value("${fastdfsUrl}")
    private String fastdfsUrl;

    /**
     *  文件上传,需要返回上传的路径url给页面显示图片和error,error=0时表示成功
     */
    @PostMapping(path="/pic/upload")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam("pic")MultipartFile multipartFile) throws Exception{
        Map<String,Object> data = new HashMap<>();
        data.put("error", 1);
        try{
            //加载配置文件得到文件绝对地址
            String conf_filename = this.getClass().getResource("/fastdfs_client.conf").getPath();
            //调用客户端全局类的初始化方法加载配置文件
            ClientGlobal.init(conf_filename);
            //创建FastDFS存储客户端对象
            StorageClient storageClient = new StorageClient();
            //上传文件到FastDFS分布式文件系统
            String[] arr = storageClient.upload_file(multipartFile.getBytes(),
                    FilenameUtils.getExtension(multipartFile.getOriginalFilename()), null);
            //拼接最后请求的地址
            StringBuilder url = new StringBuilder(fastdfsUrl);
            for (String str : arr){
                url.append("/" + str);
            }
            data.put("url", url.toString());
            data.put("error", 0);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
}
