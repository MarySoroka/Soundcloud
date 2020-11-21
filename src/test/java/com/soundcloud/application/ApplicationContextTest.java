package com.soundcloud.application;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ApplicationContextTest {
    static {
        ApplicationContext.initialize();
    }
    private final ApplicationContext applicationContext = ApplicationContext.getInstance();
    private  final BeanEnrollTest bean = new BeanEnrollTest();;
    @Test
    public void enrollBean() {
        BeanEnrollTest bean = applicationContext.getBean(BeanEnrollTest.class);
        Assert.assertNotNull(bean);
    }

    @Test
    public void enrollBeanByClass() {
        applicationContext.enrollBean(bean);
        BeanEnrollTest bean = applicationContext.getBean("BeanEnrollTest");
        Assert.assertNotNull(bean);
    }
    @Test
    public void getBean() {
        applicationContext.getBean(BeanEnrollTest.class);
        Assert.assertEquals(bean.getClass(),applicationContext.getBean(bean.getClass()).getClass());
    }



}
