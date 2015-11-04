package me.virustotal.jarpatcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarPatcher {

	private static File classFolder = new File("classes");

	public static void main(String[] args)
	{
		if(!classFolder.exists())
			classFolder.mkdir();
		
		String[] newArgs = null;
		if(args.length > 1)
		{
			System.out.println(args[1]);
			newArgs = args[1].split(" ");
		}

		File file = new File(args[0]);
		if(!file.exists())
		{
			System.out.println("That file does not exist!");
			return;
		}

		JarFile jarFile = null;
		Manifest manifest = null;
		String main = null;
		URLClassLoader urlLoader = null;
		Enumeration<JarEntry> jarEntries = null;
		try
		{
			jarFile = new JarFile(file);
			manifest = jarFile.getManifest();
			main = manifest.getMainAttributes().getValue("Main-Class");
			urlLoader = new URLClassLoader(new URL[] {file.toURL()}, JarPatcher.class.getClassLoader());
			jarEntries = jarFile.entries();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}


		if(classFolder.listFiles().length > 0)
		{
			URLClassLoader cFileLoader;
			for(File cFile : classFolder.listFiles())
			{
				try 
				{
					cFileLoader = new URLClassLoader(new URL[]{file.toURL()}, JarPatcher.class.getClassLoader());
					String entryName = cFile.getCanonicalPath().replace("/", ".");
					entryName = entryName.substring(0, entryName.lastIndexOf("."));
					cFileLoader.loadClass(entryName);
				} 
				catch (IOException | ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
		}

		while(jarEntries.hasMoreElements())
		{
			JarEntry entry = jarEntries.nextElement();
			System.out.println(entry.getName());

			if(entry.getName().endsWith(".class"))
			{
				String entryName = entry.getName().replace("/", ".");
				entryName = entryName.substring(0, entryName.lastIndexOf("."));
				try 
				{
					urlLoader.loadClass(entryName);
				}
				catch(NoClassDefFoundError | ClassNotFoundException ex) //This should be updated in the future, I blame maven for making me add this
				{
					continue;
				}
			}
		}
		try
		{
			Class<?> mainClass = urlLoader.loadClass(main);
			if(mainClass == null)
				System.out.println("null");
			Method mainMethod = mainClass.getDeclaredMethod("main", new Class[] {String[].class});

			for(String string : newArgs)
			{
				System.out.println("args: " + string);
			}

			mainMethod.invoke(mainClass.newInstance(), new Object[] {newArgs});
			urlLoader.close();
			jarFile.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}