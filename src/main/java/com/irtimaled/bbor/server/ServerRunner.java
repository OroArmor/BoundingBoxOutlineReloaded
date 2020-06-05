package com.irtimaled.bbor.server;

import com.irtimaled.bbor.Versions;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerRunner {
    private static final Map<String, String> VANILLA_SERVER_JARS = new HashMap<>();

    private static final String[] LIBRARIES = {
            "https://github.com/irtimaled/Mixin/releases/download/org/spongepowered/mixin/0.7.11-SNAPSHOT/mixin-0.7.11-SNAPSHOT.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm/6.2/asm-6.2.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/6.2/asm-commons-6.2.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm-tree/6.2/asm-tree-6.2.jar",
            "https://libraries.minecraft.net/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar"
    };

    private static final ThrowableConsumer<URL> addURL;

    static {
        VANILLA_SERVER_JARS.put("1.14.4", "https://launcher.mojang.com/v1/objects/3dc3d84a581f14691199cf6831b71ed1296a9fdf/server.jar");
        VANILLA_SERVER_JARS.put("1.14.2", "https://launcher.mojang.com/v1/objects/808be3869e2ca6b62378f9f4b33c946621620019/server.jar");
        VANILLA_SERVER_JARS.put("1.13.2", "https://launcher.mojang.com/v1/objects/3737db93722a9e39eeada7c27e7aca28b144ffa7/server.jar");

        try {
            Method method = URLClassLoader.class
                    .getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            addURL = url -> method.invoke(ClassLoader.getSystemClassLoader(), url);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void addURLToClasspath(File file) throws MalformedURLException {
        addURL.accept(file.toURI().toURL());
    }

    public static void run(List<String> args) throws IOException {
        String serverJarUrl = VANILLA_SERVER_JARS.get(Versions.minecraft);

        addURLToClasspath(getOrDownload(new File("."), serverJarUrl));
        for (String url : LIBRARIES) {
            addURLToClasspath(getOrDownload(new File("libs"), url));
        }

        args = new ArrayList<>(args);
        args.add("--tweakClass");
        args.add("com.irtimaled.bbor.launch.ServerTweaker");

        System.out.println("Launching server...");
        Launch.main(args.toArray(new String[0]));
    }

    private static File getOrDownload(File directory, String url) throws IOException {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File target = new File(directory, fileName);
        if (target.isFile()) {
            return target;
        }
        target.getParentFile().mkdirs();

        System.out.println("Downloading library: " + url);
        new FileOutputStream(target).getChannel()
                .transferFrom(Channels.newChannel(new URL(url).openStream()), 0, Long.MAX_VALUE);

        return target;
    }
}
