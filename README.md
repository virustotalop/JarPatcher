# JarPatcher [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/virustotalop/JarPatcher?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
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

# License
JarPatcher is licensed under the [MIT License](https://github.com/virustotalop/JarPatcher/blob/master/LICENSE)

>Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

>The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

# Contributing

Please follow the [allman style](https://en.wikipedia.org/wiki/Indent_style#Allman_style) when contributing code and to sign the contributors file with your github username.
