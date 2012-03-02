package knn;

import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
	
	private static boolean debugMode;
	
	public static void DEBUG(Object obj)
	{
		if (debugMode)
			System.out.println( "*** DEBUG ***   " + obj.toString() );
	}

	public static void main(String args[])
	{
		Classifier classifier = null;
		long timeStart, timeEnd;
		
		/* Display Main usage */
		if (args.length == 0)
		{
			Main.usage();
			return ;
		}
			
		/*
		 * 0. Initialize arguments
		 */
		try {
			int argsLength = args.length; 
			
			/* DebugMode argument */
			if (args[ argsLength-1 ].equals("d") )
			{
				Main.debugMode = true;
				System.out.println("Debug Mode ON.\n");
				argsLength--;
			}
			
			/* classifier args */
			String classifierName = "knn." + args[0].toUpperCase();
			classifier = (Classifier)Class.forName(classifierName).newInstance();
			String classifierArgs[] = new String[ argsLength - 1 ];
			
			/* classifier usage */
			if (classifierArgs.length == 0)
			{
				classifier.usage();
				return;				
			}
			
			/*
			 * Initialize classifier
			 */
			System.out.println("Initializing dataset...");	
			timeStart = System.currentTimeMillis();			
			for (int i = 0; i < classifierArgs.length; i++)
				classifierArgs[i] = args[i+1];			
			classifier.initialize(classifierArgs);
			timeEnd = System.currentTimeMillis();
			Main.DEBUG("Time used:(ms): " + (timeEnd - timeStart) );
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("Initialize failed.");
			e.printStackTrace();
			return;
		}
		
		/*
		 * 1. train 
		 */
		System.out.println("Training data...");
		timeStart = System.currentTimeMillis();
		classifier.train();
		timeEnd = System.currentTimeMillis();
		Main.DEBUG("Time used:(ms): " + (timeEnd - timeStart) );
		Main.DEBUG("Training Time/record: " + 
				(double)(timeEnd - timeStart) / classifier.trainDS.size());
		
		System.out.println();
		
		/*
		 * 2. test 
		 */
		// classifier.createDataset();
		System.out.println("Testing data...");
		Main.DEBUG("Training Dataset size: " + classifier.testDS.size());		
		timeStart = System.currentTimeMillis();
		double accuracy = -1;
		try {
			accuracy = classifier.test();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		timeEnd = System.currentTimeMillis();
		// report
		System.out.printf("  Accuracy: %.2f%%\n", accuracy );

		System.out.println("  Time used:(ms): " + (timeEnd - timeStart) );
		Main.DEBUG("Testing Time/record: " + 
				(double)(timeEnd - timeStart) / classifier.trainDS.size());
		System.out.println();
		
		Main.DEBUG("Training Dataset size: " + classifier.trainDS.size());		
		
		
	}

	/**
	 * Display help message
	 */
	private static void usage() {
		String help = "java knn.Main <classifier name> [ corresponding arguments ] [d]\n\n"
				+ "Corresponding arguments view be displayed after entering classifier name"+
				"\n\n" +
				"classifier name - knn, lsh (case insensitive)\n" +
				"d - debug mode\n";
		System.out.println(help);
	}
	
}
