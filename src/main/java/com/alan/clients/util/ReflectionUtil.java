package com.alan.clients.util;

import com.alan.clients.Client;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtil {
    public static Class<?>[] getClassesInPackage(String string) {
        try {
            Set<String> set = ReflectionUtil.getClassNamesFromJarFile(Paths.get(ReflectionUtil.rX(), new String[0]).toFile());
            ArrayList arrayList = new ArrayList();
            for (String string2 : set) {
                try {
                    if (!string2.startsWith(string)) continue;
                    Class<?> clazz = Class.forName(string2);
                    arrayList.add(clazz);
                }
                catch (ClassNotFoundException | NoClassDefFoundError | UnsupportedClassVersionError throwable) {}
            }
            return (Class[])arrayList.toArray(new Class[0]);
        }
        catch (Exception exception) {
            File file = ReflectionUtil.getPackageDirectory(string);
            if (!file.exists()) {
                throw new IllegalArgumentException("Could not get directory resource for package " + string);
            }
            return ReflectionUtil.getClassesInPackage(string, file);
        }
    }

    public static Set<String> getClassNamesFromJarFile(File file) throws java.io.IOException {
        HashSet<String> hashSet;
        HashSet<String> hashSet2 = new HashSet<String>();
        JarFile jarFile = new JarFile(file);
        try {
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                if (!jarEntry.getName().endsWith(".class")) continue;
                String string = jarEntry.getName().replace("/", ".").replace(".class", "");
                hashSet2.add(string);
            }
            hashSet = hashSet2;
        }
        catch (Throwable throwable) {
            try {
                jarFile.close();
            }
            catch (Throwable throwable2) {
                throwable.addSuppressed(throwable2);
                throw throwable;
            }
            throw throwable;
        }
        jarFile.close();
        return hashSet;
    }

    private static Class<?>[] getClassesInPackage(String string, File file) {
        ArrayList arrayList = new ArrayList();
        String[] stringArray = Objects.requireNonNull(file.list());
        int length = stringArray.length;
        int n3 = 0;
        while (n3 < length) {
            String string2 = stringArray[n3];
            if (string2.endsWith(".class")) {
                String string3 = ReflectionUtil.buildClassname(string, string2);
                try {
                    arrayList.add(Class.forName(string3));
                }
                catch (ClassNotFoundException classNotFoundException) {
                    System.err.println("Error creating class " + string3);
                }
            } else if (!string2.contains(".")) {
                String string4 = string + (string.endsWith(".") ? "" : ".") + string2;
                arrayList.addAll(Arrays.asList(ReflectionUtil.getClassesInPackage(string4, ReflectionUtil.getPackageDirectory(string4))));
            }
            ++n3;
        }
        return (Class[])arrayList.toArray(new Class[0]);
    }

    public static String buildClassname(String string, String string2) {
        return string + "." + string2.replace(".class", "");
    }

    private static File getPackageDirectory(String string) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("Can't get class loader.");
        }
        URL uRL = classLoader.getResource(string.replace('.', '/'));
        if (uRL == null) {
            throw new RuntimeException("Package " + string + " not found on classpath.");
        }
        return new File(uRL.getFile());
    }

    public static boolean dirExist(String string) {
        if (Thread.currentThread().getContextClassLoader().getResource(string.replace('.', '/')) == null) return false;
        return true;
    }

    public static String rX() throws java.net.URISyntaxException {
        return new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }

    public static Field a(String string, Class<?> clazz) {
        try {
            Field field = clazz.getField(string);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            return null;
        }
    }

    public static void a(Class<?> clazz, String string, Object object) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        Field field = clazz.getDeclaredField(string);
        field.setAccessible(true);
        Field field2 = Field.class.getDeclaredField("modifiers");
        field2.setAccessible(true);
        field2.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        field.set(null, object);
    }

    public static Method b(String string, Class<?> clazz) {
        try {
            Method method = clazz.getMethod(string, new Class[0]);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
            return null;
        }
    }
}
