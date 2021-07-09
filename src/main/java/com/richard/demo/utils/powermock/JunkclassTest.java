package com.richard.demo.utils.powermock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/7/9 4:34 PM richard.xu Exp $
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class,Junkclass.class}) // 必须声明Junkclass
public class JunkclassTest {

    @Test
    public void junk() {

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("values")).thenReturn("ab,cd");
        Junkclass junkclass = new Junkclass();
        System.out.println(junkclass.tests());
    }
}
