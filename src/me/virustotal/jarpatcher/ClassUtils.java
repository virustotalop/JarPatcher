package me.virustotal.jarpatcher;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {
	
	private static URLClassLoader urlLoader;
	
	protected static void setClassLoader(URL[] urls)
	{
		urlLoader = new URLClassLoader(urls, JarPatcher.class.getClassLoader());
	}

	protected static void setClassLoader(File[] files)
	{
		List<URL> urls = new ArrayList<URL>();
		for(File file : files)
		{
			try 
			{
				urls.add(file.toURL());
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			}
		}
		setClassLoader(urls.toArray(new URL[urls.size()]));
	}
	
	protected static URLClassLoader getUrlLoader()
	{
		return urlLoader;
	}
	
	protected static void loadClassFiles(File[] files)
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
					ClassUtils.getUrlLoader().loadClass(entryName);
					JarPatcher.loadedClasses.add(entryName);
				}
				else 
				{
					ClassUtils.loadClassFiles(file.listFiles());
				}
			}
		}
		catch(StackOverflowError | NullPointerException | ClassNotFoundException | SecurityException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

}
