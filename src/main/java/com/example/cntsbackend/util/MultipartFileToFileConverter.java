package com.example.cntsbackend.util;

import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.datatypes.Bool;

import java.io.*;

public class MultipartFileToFileConverter {

    public static File convert(MultipartFile multipartFile) throws IOException {
        // 生成临时文件
        File tempFile = new File(multipartFile.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(multipartFile.getBytes());
        }
        String etag = Etag.file(tempFile);
        // 删除临时文件
        tempFile.delete();
        // 文件保存路径
        String prefix = System.getProperty("user.dir") + "\\data\\upload\\";
        // 查看文件夹下是否有重名文件
        if(checkFile(prefix, etag)){
            return null;
        }else {
            File file = new File(prefix + etag + getSuffix(multipartFile.getOriginalFilename()));
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

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return 后缀名
     */
    public static String getSuffix(String filename) {
        String[] fileNameArray = filename.split("\\.");
        StringBuilder suffix = new StringBuilder();
        for (int i = 1; i < fileNameArray.length; i++) {
            suffix.append("."+fileNameArray[i]);
        }
        return suffix.toString();
    }
}
