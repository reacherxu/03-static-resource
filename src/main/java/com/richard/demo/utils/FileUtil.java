package com.richard.demo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class FileUtil {


	/**
	 * 
	 * @param content
	 *            content
	 * @param path
	 *            file path
	 */
	public static void writeFile(String content, String path) {
		BufferedWriter writer = null;

		try {
			// check if file exists
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}

			// write file
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
            System.err.println(e.getStackTrace().toString());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
                System.err.println("Error in closing the BufferedWritter" + e.getStackTrace().toString());
			}
		}
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

        System.out.println(1 / 0);
    }
}
