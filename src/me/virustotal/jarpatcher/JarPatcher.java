package me.virustotal.jarpatcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarPatcher {

	private static File classFolder = new File("classes");
	private static ArrayList<String> loadedClasses = new ArrayList<String>();
	private static URLClassLoader urlLoader;
	
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

		try 
		{
			urlLoader = new URLClassLoader(new URL[] {classFolder.toURL(), file.toURL()}, JarPatcher.class.getClassLoader());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
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
			//JarPatcher.setURI(file);
			//urlLoader = new URLClassLoader(new URL[] {file.toURL()}, JarPatcher.class.getClassLoader());
			System.out.println("url: " + file.toURL().getPath());
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
					System.out.println(entryName);
					if(!JarPatcher.loadedClasses.contains(entryName))
						urlLoader.loadClass(entryName);
					else
						System.out.println("Contains entry name!");
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

			if(newArgs != null)
			{
				for(String string : newArgs)
				{
					System.out.println("args: " + string);
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
					System.out.println("url: " + new File("classes").toURL().getPath());
					String absPath = file.getAbsolutePath();
					String classesPath = absPath.substring(absPath.indexOf("classes"));
					String entryName = classesPath.substring(classesPath.indexOf("\\") + 1).replace("\\", ".");
					entryName = entryName.substring(0, entryName.indexOf(".class"));
					System.out.println("entname: " + entryName);
					urlLoader.loadClass(entryName);
					loadedClasses.add(entryName);
					System.out.println("Loaded file!");
				}
				else 
				{
					JarPatcher.loadClassFiles(file.listFiles());
				}
			}
		}catch(StackOverflowError | NullPointerException | ClassNotFoundException | IOException | SecurityException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
}