# JarPatcher
JarPatcher allows you to patch or inject code into jar files on runtime. Put compiled class files into the classes folder that is generated. On run time the class files are loaded in. 

# Usage
```
Arguments are optional
java -jar JarPatcher.jar something.jar "arg1 arg2" --debug
```

#Example
```
Class called JarPatcherTest

package me.virustotal.jarpatchertest;

public class JarPatcherTest {
	
	public static void main(String[] args)
	{
		Test.test();
	}
}

Original called Test

package me.virustotal.jarpatchertest;

public class Test {
	
	public static void test()
	{
		System.out.println("test");
	}
}

New class called Test

package me.virustotal.jarpatchertest;

public class Test {
	
	public static void test()
	{
		System.out.println("no longer testing");
	}
}
```

# How it works
The new test class goes into the "classes" folder. The Test.class should go into the correct directory for example the directory would be /classes/me/virustotal/jarpatchertest/Test.class. Classes in the classes directory are loaded first and then the jar that is provided is parsed and the class files are loaded.
