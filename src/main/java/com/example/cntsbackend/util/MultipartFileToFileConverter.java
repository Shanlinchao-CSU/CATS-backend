package com.example.cntsbackend.util;

import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.datatypes.Bool;

import java.io.*;

public class MultipartFileToFileConverter {

    public static File convert(MultipartFile multipartFile) throws IOException {
        // 文件保存路径
        String prefix = System.getProperty("user.dir") + "/data/upload/";
        // 查看文件夹下是否有重名文件
        if(checkFile(prefix, multipartFile.getOriginalFilename())){
            return null;
        }else {
            File file = new File(prefix + multipartFile.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }
            return file;
        }
    }

    /**
     * 检查文件夹下是否有该文件
     *
     * @param path 文件路径
     * @param filename 文件名
     * @return 是否有该文件 true/false
     */
    public static Boolean checkFile(String path, String filename) {
        File folder = new File(path);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Folder path is not a directory.");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().equals(filename)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 检验文件唯一性
     *
     * @param file 文件
     * @return 文件的etag
     */
    public static String etag(File file) {
        try {
            return Etag.file(file);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 检验文件唯一性
     *
     * @param file 文件
     * @param etag etag
     * @return 是否一致
     */
    public static Boolean etag(File file, String etag) throws IOException {
        String fileName = getFileName(file);
        return etag.equals(fileName);
    }


    /**
     * 处理文件名  —— 以.分割，取最初
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        String[] fileNameArray = fileName.split("\\.");
        return fileNameArray[0];
    }
}
