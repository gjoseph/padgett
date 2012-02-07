package net.incongru.padgett

/**
 *
 */
class GroovyUtil {
    static def loadClass(File file) {
        ClassLoader parent = GroovyUtil.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        Class groovyClass = loader.parseClass(file)
        return groovyClass
    }
}
