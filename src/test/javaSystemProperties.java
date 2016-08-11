package test;

import java.util.Properties;

public class javaSystemProperties {

	public static void main(String[] args) {
		
		//System.out.println(System.getProperty("os.arch"));
		Properties p = System.getProperties();
		p.list(System.out);
		
		/*
		OPTIONS::

		java.version					Java Runtime Environment version
		java.vendor						Java Runtime Environment vendor
		java.vendor.url					Java vendor URL
		java.home						Java installation directory
		java.vm.specification.version	Java Virtual Machine specification version
		java.vm.specification.vendor	Java Virtual Machine specification vendor
		java.vm.specification.name		Java Virtual Machine specification name
		java.vm.version					Java Virtual Machine implementation version
		java.vm.vendor					Java Virtual Machine implementation vendor
		java.vm.name					Java Virtual Machine implementation name
		java.specification.version		Java Runtime Environment specification version
		java.specification.vendor		Java Runtime Environment specification vendor
		java.specification.name			Java Runtime Environment specification name
		java.class.version				Java class format version number
		java.class.path					Java class path
		java.library.path				List of paths to search when loading libraries
		java.io.tmpdir					Default temp file path
		java.compiler					Name of JIT compiler to use
		java.ext.dirs					Path of extension directory or directories
		os.name							Operating system name
		os.arch							Operating system architecture
		os.version						Operating system version
		file.separator					File separator ("/" on UNIX)
		path.separator					Path separator (":" on UNIX)
		line.separator					Line separator ("\n" on UNIX)
		user.name						User's account name
		user.home						User's home directory
		user.dir						User's current working directory
		*/

		/*
		OUTPUT::

		-- listing properties --
		java.runtime.name=Java(TM) SE Runtime Environment
		sun.boot.library.path=C:\Program Files\Java\jre7\bin
		java.vm.version=23.21-b01
		java.vm.vendor=Oracle Corporation
		java.vendor.url=http://java.oracle.com/
		path.separator=;
		java.vm.name=Java HotSpot(TM) Client VM
		file.encoding.pkg=sun.io
		user.script=
		user.country=US
		sun.java.launcher=SUN_STANDARD
		sun.os.patch.level=Service Pack 2
		java.vm.specification.name=Java Virtual Machine Specification
		user.dir=C:\automation_gui\gui_automation
		java.runtime.version=1.7.0_21-b11
		java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
		java.endorsed.dirs=C:\Program Files\Java\jre7\lib\endorsed
		os.arch=x86
		java.io.tmpdir=C:\Users\sinajayk\AppData\Local\Temp\
		line.separator=

		java.vm.specification.vendor=Oracle Corporation
		user.variant=
		os.name=Windows Vista
		sun.jnu.encoding=Cp1252
		java.library.path=C:\Program Files\Java\jre7\bin;C:\Win...
		java.specification.name=Java Platform API Specification
		java.class.version=51.0
		sun.management.compiler=HotSpot Client Compiler
		os.version=6.0
		user.home=C:\Users\sinajayk
		user.timezone=
		java.awt.printerjob=sun.awt.windows.WPrinterJob
		file.encoding=Cp1252
		java.specification.version=1.7
		user.name=sinajayk
		java.class.path=C:\automation_gui\gui_automation\bin;...
		java.vm.specification.version=1.7
		sun.arch.data.model=32
		java.home=C:\Program Files\Java\jre7
		sun.java.command=test.jlt
		java.specification.vendor=Oracle Corporation
		user.language=en
		awt.toolkit=sun.awt.windows.WToolkit
		java.vm.info=mixed mode, sharing
		java.version=1.7.0_21
		java.ext.dirs=C:\Program Files\Java\jre7\lib\ext;C:...
		sun.boot.class.path=C:\Program Files\Java\jre7\lib\resour...
		java.vendor=Oracle Corporation
		file.separator=\
		java.vendor.url.bug=http://bugreport.sun.com/bugreport/
		sun.cpu.endian=little
		sun.io.unicode.encoding=UnicodeLittle
		sun.desktop=windows
		sun.cpu.isalist=pentium_pro+mmx pentium_pro pentium+m...
		*/
		
		/*
		 * IMPORTANT ONES::
		 os.arch=x86
		 os.name=Windows Vista
		 os.version=6.0
		 user.name=sinajayk
		 java.class.path=C:\automation_gui\gui_automation\bin;...
		 */




	}

}
