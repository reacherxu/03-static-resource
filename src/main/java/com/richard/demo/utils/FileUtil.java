package com.richard.demo.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {
    /**
     * compress multiple files into one zip file
     *
     * @param srcfile source file list
     * @param zipfile
     */
    public static void zipFiles(List<File> srcfile, File zipfile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(zipfile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            for (int i = 0; i < srcfile.size(); i++) {
                zip(srcfile.get(i), zipOutputStream);
            }
        } catch (Exception e) {
            log.error("Error occurs when zip file", e);
        }
    }

    private static void zip(File srcFile, ZipOutputStream zipOutputStream) {
        byte[] buf = new byte[1024];
        try (FileInputStream in = new FileInputStream(srcFile)) {
            zipOutputStream.putNextEntry(new ZipEntry(srcFile.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
        } catch (FileNotFoundException e) {
            log.error("{} not found", srcFile.getName());
        } catch (IOException e) {
            log.error("Error occurred while zip file {} ", srcFile.getName());
        }
    }


    /**
     * @param content file content
     * @param path file path
     */
    public static void writeFile(String content, String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            log.error("An error occurred while creating file {} ", path, e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            log.error("An error occurred while writing to  file {} ", path, e);
        }
    }

    public static String getTempLoaction() {
        String location = null;

        try {
            location = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException var2) {
            log.error("file not found");
        }

        return location;
    }

    public static boolean createDirectory(File dir) {
        boolean result = false;
        try {
            result = dir.exists() ? true : dir.mkdir();
        } catch (Exception e) {
            log.error("An error occurred while creating dir {} ", dir.getPath(), e);
        }
        return result;
    }

    public static Resource test1() {
        // test sr hierarchy
        // parent folder
        String rootFolder = FileUtil.getTempLoaction() + "ServiceRegistry";
        FileUtil.createDirectory(new File(rootFolder));
        String serviceMappingPath = rootFolder + "/ServiceMapping.json";
        String errorPath = rootFolder + "/Error.json";

        File srcFile = new File(rootFolder);
        String fileName = new Date().toString();
        File outFile = new File(FileUtil.getTempLoaction() + fileName + ".zip");

        try {
            String mapping = prepareMapping();
            FileUtil.writeFile(mapping, serviceMappingPath);
            FileUtil.writeFile(new JsonObject().toString(), errorPath);

            // sub folder
            Gson gson = new Gson();
            String serviceFolder = rootFolder + "/Services";
            FileUtil.createDirectory(new File(serviceFolder));
            String serviceJson = serviceFolder + "/service1.json";
            String service = prepareService();
            FileUtil.writeFile(gson.toJson(service), serviceJson);


            String cdtFolder = rootFolder + "/ComplexDataTypes";
            FileUtil.createDirectory(new File(cdtFolder));
            String cdtJson = cdtFolder + "/cdt1.json";
            String cdt = prepareCdt();
            FileUtil.writeFile(gson.toJson(cdt), cdtJson);
            log.info("completing hierarchy....");

            log.info("starting zipping....");

            CompactAlgorithm.zipFiles(srcFile, outFile);
            log.info("completing zipping....");
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // FileUtils.deleteQuietly(srcFile);
            // FileUtils.deleteQuietly(outFile);
        }
        return new FileSystemResource(outFile.getPath());
    }



    public static void main(String[] args) throws IOException {
        // Resource file = test1();
        // log.info(file.getFilename());
        // String dir = "temp";
        // File file = new File(getTempLoaction() + "/" + dir);
        // createDirectory(file);
        // createDirectory(new File(getTempLoaction() + "/" + dir + "/bbb"));
        //
        // zipFiles(Arrays.asList(file), new File(getTempLoaction() + "/" + dir + ".zip"));

        // 不存文件的压缩方式
        String before = readFromFile(getTempLoaction() + "test2.txt");

        byte[] bytes = zipByteArrayOutputStream(before.getBytes(), "test.zip");
        System.out.println(new String(bytes, Charset.defaultCharset()));
        FileUtil.writeFile(new String(bytes, Charset.defaultCharset()), FileUtil.getTempLoaction() + "test10.txt");

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);

        byte[] byte1 = uncompress(bin);
        FileUtil.writeFile(new String(byte1, Charset.defaultCharset()), FileUtil.getTempLoaction() + "test11.txt");

        System.out.println(new String(byte1, Charset.defaultCharset()));

        readFromFile(FileUtil.getTempLoaction() + "test2.txt");
    }

    /**
     * 从文件中读取文本
     */
    public static String readFromFile(String filename) throws IOException {

        BufferedInputStream bufferedInput = null;
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();

        try {

            // 创建BufferedInputStream 对象
            bufferedInput = new BufferedInputStream(new FileInputStream(filename));

            int bytesRead = 0;

            // 从文件中按字节读取内容，到文件尾部时read方法将返回-1
            while ((bytesRead = bufferedInput.read(buffer)) != -1) {

                // 将读取的字节转为字符串对象
                String chunk = new String(buffer, 0, bytesRead);
                sb.append(chunk);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭 BufferedInputStream
            try {
                if (bufferedInput != null)
                    bufferedInput.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 文件流压缩
     * 
     * @param baisByte 需要压缩的字节输出流(ByteArrayOutputStream)的字节数组
     * @param fileName 需要压缩的文件名
     * @return 压缩后字节数组输出流转为的字符串
     * @throws IOException
     */
    public static byte[] zipByteArrayOutputStream(byte[] baisByte, String fileName) {
        byte[] result = null;

        try (// 1.将需要压缩的字节输出流，转为字节数组输入流，
                ByteArrayInputStream bais = new ByteArrayInputStream(baisByte);
                // 2.创建字节数组输出流，用于返回压缩后的输出流字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 3.创建压缩输出流
                ZipOutputStream zipOut = new ZipOutputStream(baos);) {


            zipOut.setMethod(ZipOutputStream.DEFLATED);// 进行压缩存储
            zipOut.setLevel(Deflater.BEST_COMPRESSION);// 压缩级别值为0-9共10个级别(值越大，表示压缩越利害)
            // 4.设置ZipEntry对象，并对需要压缩的文件命名
            zipOut.putNextEntry(new ZipEntry(fileName));

            // 5.读取要压缩的字节输出流，进行压缩
            int temp = 0;
            while ((temp = bais.read()) != -1) {
                zipOut.write(temp); // 压缩输出
            }
            zipOut.closeEntry();
            result = baos.toByteArray();
        } catch (IOException e) {
            log.error("[zipByteArrayOutputStream] zip file failed, file name is {}", fileName);
        }

        return result;
    }

    // refer to https://blog.csdn.net/weixin_42919496/article/details/81518931
    // https://blog.csdn.net/u012909738/article/details/81536761
    public static byte[] uncompress(InputStream inputStream) {
        byte[] content = new byte[10240];
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry ze = null;
            while (((ze = zis.getNextEntry()) != null) && !ze.isDirectory()) {
                String name = ze.getName();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10240];
                int length = -1;
                while ((length = zis.read(buffer, 0, buffer.length)) > -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                content = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
            }
        } catch (IOException e) {
            log.error("[uncompress] uncompress file failed", e);
        }
        return content;
    }



    private static String prepareCdt() {
        Gson gson = new Gson();
        Object1 car = Object1.builder().number("01").result("aa").build();
        return gson.toJson(car);
    }

    private static String prepareService() {
        Gson gson = new Gson();
        Car car = Car.builder().color("yellow").type("Cadilac").build();
        return gson.toJson(car);
    }

    private static String prepareMapping() {
        com.google.gson.JsonObject mapping = new com.google.gson.JsonObject();

        JsonArray serviceArray = new JsonArray();
        com.google.gson.JsonObject obj1 = new com.google.gson.JsonObject();
        obj1.addProperty("serviceId", 1);
        obj1.addProperty("metadataId", 1);
        obj1.addProperty("name", "testService");
        obj1.addProperty("groupPath", "/user defined/test");
        serviceArray.add(obj1);

        JsonArray cdtArray = new JsonArray();
        com.google.gson.JsonObject obj2 = new com.google.gson.JsonObject();
        obj2.addProperty("id", 2);
        obj2.addProperty("name", "testCDT");
        obj2.addProperty("groupPath", "/user defined/test/cdt");
        cdtArray.add(obj2);

        mapping.add("ServiceMapping", serviceArray);
        mapping.add("CDTMapping", cdtArray);

        return mapping.toString();
    }

    @Test
    public void test() throws Exception {
        // 表示classpath的路径，就是bin的绝对路径名
        System.out.println(FileUtil.class.getResource("/"));
        System.out.println(FileUtil.class.getClassLoader().getResource(""));
        // System.out.println(ResourceUtils.getURL("classpath:").getPath());

        // 表示当前类的folder的名字
        System.out.println(FileUtil.class.getResource(""));

        // 尽量不要使用user.dir,因为得出的结果各不相同
        System.out.println(System.getProperty("user.dir"));


    }
}
