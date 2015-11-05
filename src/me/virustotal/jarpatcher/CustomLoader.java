package me.virustotal.jarpatcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Stack;

public class CustomLoader extends URLClassLoader {

	public CustomLoader(URL[] urls) 
	{
		super(urls);
	}
	
	public void setURLS(URL[] urlArray)
	{
		try 
		{
			Object ucp = this.getClass().getField("ucp").get(new Object());
			Field urls = ucp.getClass().getField("urls");
			Stack<URL> newStack = new Stack<URL>();
			for(URL url : urlArray)
			{
				newStack.push(url);
			}
			urls.set(ucp, newStack);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
	}

}
