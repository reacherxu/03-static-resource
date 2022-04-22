package com.richard.demo.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

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
                "**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/conditions/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/config/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/constants/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/dto/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/exception/**";
        String soarExclusion =
                "**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/conditions/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/config/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/constants/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/dto/**,**/src/main/java/com/sap/dm/fnd/serviceregistry/executor/exception/**";
        String[] files = sonarCoverage.split(",");
        Arrays.stream(files).forEach(file -> {
            System.out.println(file);
        });
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
		String info = String.format("%s : %s","time",new Date());
		System.out.println(info);

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

    }

}
