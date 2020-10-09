package fr.redstonneur1256.redutilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class FileLogger {
	
	private static PrintStream originalOut;
	private static PrintStream originalErr;
	private static File file;
	private static FileWriter writer;
	public static void setup(File file, boolean replaceOld) throws IOException {
		if(file.exists() && replaceOld) {
			BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
			if(!file.renameTo(new File(format.format(attrs.creationTime().toMillis()) + ".log"))) {
				System.err.println("Failed to rename actual file");
			}
		}
		
		writer = new FileWriter(file);
		
		originalOut = System.out;
		originalErr = System.err;

		PrintStream out = new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				originalOut.write(b);
				writer.write(b);
				if(b == '\n')
					writer.flush();
			}
		});
		PrintStream err = new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				originalErr.write(b);
				writer.write(b);
				if(b == '\n')
					writer.flush();
			}
		});
		System.setOut(out);
		System.setErr(err);
	}
	
	public static void close() throws IOException {
		writer.flush();
		writer.close();
		System.setOut(originalOut);
		System.setErr(originalErr);
	}
	
}
