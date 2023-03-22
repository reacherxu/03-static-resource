package com.richard.demo.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtil {

	/**
	 * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
	 */
	@Test
	public void testEquals() {
        Assert.assertTrue(StringUtils.equals(null, null));
        Assert.assertFalse(StringUtils.equals(null, "abc"));
        Assert.assertFalse(StringUtils.equals("abc", null));
        Assert.assertTrue(StringUtils.equals("abc", "abc"));
        Assert.assertFalse(StringUtils.equals("abc", "ABC"));

        System.out.println(StringUtils.join("a", "_", "c"));
	}
	
    @Test
    public void testSplit() {
        String sonarCoverage =
                "**/src/main/java/sap/dm/sfd/lib/commons/feign/*,**/src/main/java/sap/dm/sfd/lib/commons/auditlog/*,**/src/main/java/sap/dm/sfd/lib/commons/constants/*,**/src/main/java/sap/dm/sfd/lib/commons/converter/*,**/src/main/java/sap/dm/sfd/lib/commons/entity/*,**/src/main/java/sap/dm/sfd/lib/commons/enums/*,**src/main/java/sap/dm/sfd/lib/commons/kafka/*,**/src/main/java/sap/dm/sfd/lib/commons/kafka/**/*,**/src/main/java/sap/dm/sfd/lib/commons/validator/*,**/src/main/java/sap/dm/sfd/lib/commons/config/*,**/src/main/java/sap/dm/sfd/lib/commons/exception/*,**/src/main/java/sap/dm/sfd/lib/commons/security/*,**/src/main/java/sap/dm/sfd/lib/commons/service/*,**/src/main/java/sap/dm/sfd/lib/commons/migration/*";
        String soarExclusion =
                "**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/conditions/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/config/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/constants/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/dto/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/exception/**";
        String[] files = sonarCoverage.split(",");
        Arrays.stream(files).forEach(file -> {
            System.out.println(file);
        });
        log.info("pe-ms/0b3b82c9-9967-46e5-85f8-60671a2354d9/pe/archive/{date}/{0-24}/tableName".split("/")[4]);

    }

	@Test
    public void testReplacement() {
        Assert.assertTrue(StringUtils.isNotEmpty(" "));

        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "dog");
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
	}
	
	/**
	 * test StringFormat
	 */
	@Test
	public void testStringFormat() {
        System.out.println("time is " + System.currentTimeMillis() + "len is " + String.valueOf(System.currentTimeMillis()).length());


		String info = String.format("%s : %s","time",new Date());
		System.out.println(info);

        String prefix = String.format("%s/%s/pe/archive/", "pe-ms", "0b3b82c9-9967-46e5-85f8-60671a2354d9");
        System.out.println(prefix);


        List<String> indicators = Lists.newArrayList("name1", "name2");
        System.out.println(StringUtils.join(indicators, ","));



	}

    @Test
    public void testJoin() {
        // List<String> list = new ArrayList<String>();
        // System.out.println(StringUtils.join(list, ","));
        //
        // list.add("anny");
        // list.add("zhangsan");
        // list.add("lisi");
        // list.add("wangwu");
        // System.out.println(StringUtils.join(list, ","));
        String nameStr = "Process_Instance_Archive-2022-09-17.zip".split("\\.")[0];
        log.info(nameStr);
        log.info(nameStr.replace("Process_Instance_Archive-", ""));

        String nameStr1 = "2022-09-17.zip".split("\\.")[0];
        log.info(nameStr1);
        log.info(nameStr1.replace("Process_Instance_Archive-", ""));
    }

}
