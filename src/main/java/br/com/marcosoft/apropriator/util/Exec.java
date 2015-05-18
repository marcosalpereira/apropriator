package br.com.marcosoft.apropriator.util;

import java.io.IOException;

public class Exec {

	public static void jar(String jarFile, String... args) {
		try {
			final String[] cmdArray = new String[args.length + 3];
			cmdArray[0] = "java";
			cmdArray[1] = "-jar";
			cmdArray[2] = jarFile;
			System.arraycopy(args, 0, cmdArray, 3, args.length);

			final Process proc = Runtime.getRuntime().exec(cmdArray);
			System.out.println(proc);

//			final BufferedReader stdError = new BufferedReader(
//					new InputStreamReader(proc.getErrorStream()));
//			// read any errors from the attempted command
//			System.out.println("Here is the standard error of the command (if any):\n");
//			String line;
//			while ((line = stdError.readLine()) != null) {
//				System.out.println(line);
//			}
//			System.out.println("Exit Value:" + proc.exitValue());

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}
}