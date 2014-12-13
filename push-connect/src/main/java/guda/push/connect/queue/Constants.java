package guda.push.connect.queue;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Created by foodoon on 2014/12/13.
 */
public class Constants {

    private Constants() {}  // can't construct

    /** JVM vendor info. */
    public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
    public static final String JVM_VERSION = System.getProperty("java.vm.version");
    public static final String JVM_NAME = System.getProperty("java.vm.name");

    /** The value of <tt>System.getProperty("java.version")</tt>. **/
    public static final String JAVA_VERSION = System.getProperty("java.version");

    /** The value of <tt>System.getProperty("os.name")</tt>. **/
    public static final String OS_NAME = System.getProperty("os.name");
    /** True iff running on Linux. */
    public static final boolean LINUX = OS_NAME.startsWith("Linux");
    /** True iff running on Windows. */
    public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
    /** True iff running on SunOS. */
    public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
    /** True iff running on Mac OS X */
    public static final boolean MAC_OS_X = OS_NAME.startsWith("Mac OS X");
    /** True iff running on FreeBSD */
    public static final boolean FREE_BSD = OS_NAME.startsWith("FreeBSD");

    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");

    /** @deprecated With Lucene 4.0, we are always on Java 6 */
    @Deprecated
    public static final boolean JRE_IS_MINIMUM_JAVA6 =
            new Boolean(true).booleanValue(); // prevent inlining in foreign class files

    public static final boolean JRE_IS_MINIMUM_JAVA7;
    public static final boolean JRE_IS_MINIMUM_JAVA8;

    /** True iff running on a 64bit JVM */
    public static final boolean JRE_IS_64BIT;

    static {
        boolean is64Bit = false;
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Object unsafe = unsafeField.get(null);
            final int addressSize = ((Number) unsafeClass.getMethod("addressSize")
                    .invoke(unsafe)).intValue();
            //System.out.println("Address size: " + addressSize);
            is64Bit = addressSize >= 8;
        } catch (Exception e) {
            final String x = System.getProperty("sun.arch.data.model");
            if (x != null) {
                is64Bit = x.indexOf("64") != -1;
            } else {
                if (OS_ARCH != null && OS_ARCH.indexOf("64") != -1) {
                    is64Bit = true;
                } else {
                    is64Bit = false;
                }
            }
        }
        JRE_IS_64BIT = is64Bit;

        // this method only exists in Java 7:
        boolean v7 = true;
        try {
            Throwable.class.getMethod("getSuppressed");
        } catch (NoSuchMethodException nsme) {
            v7 = false;
        }
        JRE_IS_MINIMUM_JAVA7 = v7;

        if (JRE_IS_MINIMUM_JAVA7) {
            // this method only exists in Java 8:
            boolean v8 = true;
            try {
                Collections.class.getMethod("emptySortedSet");
            } catch (NoSuchMethodException nsme) {
                v8 = false;
            }
            JRE_IS_MINIMUM_JAVA8 = v8;
        } else {
            JRE_IS_MINIMUM_JAVA8 = false;
        }
    }

    // this method prevents inlining the final version constant in compiled classes,
    // see: http://www.javaworld.com/community/node/3400
    private static String ident(final String s) {
        return s.toString();
    }

    // NOTE: we track per-segment version as a String with the "X.Y" format, e.g.
    // "4.0", "3.1", "3.0". Therefore when we change this constant, we should keep
    // the format.
    /**
     * This is the internal Lucene version, recorded into each segment.
     */
    public static final String LUCENE_MAIN_VERSION = ident("4.6");

    /**
     * This is the Lucene version for display purposes.
     */

}
