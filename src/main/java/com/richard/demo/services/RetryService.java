package com.richard.demo.services;

import java.util.concurrent.TimeoutException;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/2/14 2:10 PM richard.xu Exp $
 */
public interface RetryService {

    public void service(String command) throws Exception;

    public void recover(TimeoutException e);
}
