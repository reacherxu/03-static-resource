package com.richard.demo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;

import lombok.extern.slf4j.Slf4j;

/**
 * 名字无所谓，但是要有SpringBootApplication注解
 * 要有main方法，调用SpringApplication.run
 *
 * @author richard.xu03@sap.com
 * @version $Id: DemoApplication.java, v 0.1 May 23, 2020 3:36:39 PM richard.xu Exp $
 */
@SpringBootApplication
@ServletComponentScan
@EnableRetry
@Slf4j
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    private final JdbcTemplate template;

    public DemoApplication(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void run(String... args) {
        String sql = "SELECT count(*)  FROM USR";
        int count = template.queryForObject(sql, Integer.class);
        log.info("user count is " + count);
    }
}
