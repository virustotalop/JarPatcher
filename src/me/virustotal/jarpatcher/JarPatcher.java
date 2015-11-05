package me.virustotal.jarpatcher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarPatcher {

	private static File classFolder = new File("classes");
	private static ArrayList<String> loadedClasses = new ArrayList<String>();
	private static URLClassLoader urlLoader;
	private static boolean debug = false;

	public static void main(String[] args)
	{
		if(!classFolder.exists())
			classFolder.mkdir();

		String[] newArgs = null;
		
		if(args.length == 0)
		{
			System.out.println("A jar file must be provided!");
			return;
		}
		
		if(args.length > 1)
		{
			newArgs = args[1].split(" ");
			if(Arrays.asList(newArgs).contains("--debug"))
			{
				JarPatcher.debug = true;
			}
		}

		File file = new File(args[0]);
		if(!file.exists())
		{
			System.out.println("That file does not exist!");
			return;
		}

		try 
		{
			urlLoader = new URLClassLoader(new URL[] {classFolder.toURL(), file.toURL()}, JarPatcher.class.getClassLoader());
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}

		if(classFolder.listFiles().length > 0)
		{
			JarPatcher.loadClassFiles(classFolder.listFiles());
		}

		JarFile jarFile = null;
		Manifest manifest = null;
		String main = null;
		Enumeration<JarEntry> jarEntries = null;
		try
		{
			jarFile = new JarFile(file);
			manifest = jarFile.getManifest();
			main = manifest.getMainAttributes().getValue("Main-Class");
			jarEntries = jarFile.entries();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		while(jarEntries.hasMoreElements())
		{
			JarEntry entry = jarEntries.nextElement();

			if(entry.getName().endsWith(".class"))
			{
				String entryName = entry.getName().replace("/", ".");
				entryName = entryName.substring(0, entryName.lastIndexOf("."));
				try 
				{
					if(!JarPatcher.loadedClasses.contains(entryName))
					{
						urlLoader.loadClass(entryName);
						
						if(debug)
						{
							System.out.println(entryName);
						}
					}
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
			Method mainMethod = mainClass.getDeclaredMethod("main", new Class[] {String[].class});

			if(JarPatcher.debug)
			{
				if(newArgs != null)
				{
					String str = "args: ";
					for(String string : newArgs)
					{
						str += string + " ";
					}
					
					System.out.println(str);
				}
			}

			mainMethod.invoke(null, new Object[] {newArgs});
			urlLoader.close();
			jarFile.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void loadClassFiles(File[] files)
	{
		try
		{
			for(File file : files)
			{
				if(file.isFile())
				{
					String absPath = file.getAbsolutePath();
					String classesPath = absPath.substring(absPath.indexOf("classes"));
					String entryName = classesPath.substring(classesPath.indexOf("\\") + 1).replace("\\", ".");
					entryName = entryName.substring(0, entryName.indexOf(".class"));
					urlLoader.loadClass(entryName);
					loadedClasses.add(entryName);
				}
				else 
				{
					JarPatcher.loadClassFiles(file.listFiles());
				}
			}
		}
		catch(StackOverflowError | NullPointerException | ClassNotFoundException | SecurityException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
}