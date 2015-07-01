package com.github.baev;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 01.07.15
 */
public class LeakTest {

    public static final int TIMES = 100;

    /**
     * This test fails with OOM.
     */
    @Test
    public void loadGroovyBean() throws Exception {
        for (int i = 0; i < TIMES; i++) {
            try (URLClassLoader classLoader = createClassLoader()) {
                classLoader.loadClass("com.github.baev.GroovyTestBean").newInstance();
            }
        }
    }

    /**
     * All works fine in case load java class.
     */
    @Test
    public void loadJavaBean() throws Exception {
        for (int i = 0; i < TIMES; i++) {
            try (URLClassLoader classLoader = createClassLoader()) {
                classLoader.loadClass("com.github.baev.JavaTestBean").newInstance();
            }
        }
    }

    /**
     * Create an {@link URLClassLoader} with few deps. <code>groovy-permgen-leak-testbean-1.0-SNAPSHOT.jar</code>
     * contains <code>com.github.baev.GroovyTestBean</code> and <code>com.github.baev.JavaTestBean</code> classes.
     * You can find the source code here https://github.com/baev/groovy-permgen-leak-testbean
     */
    public URLClassLoader createClassLoader() throws MalformedURLException {
        return new URLClassLoader(new URL[]{
                getClass().getClassLoader().getResource("groovy-all-2.4.3-indy.jar"),
                getClass().getClassLoader().getResource("groovy-permgen-leak-testbean-1.0-SNAPSHOT.jar"),
        }, null);
    }
}
