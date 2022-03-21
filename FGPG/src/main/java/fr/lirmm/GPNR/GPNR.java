package fr.lirmm.GPNR;


import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

public class GPNR {
	public static void main(String[] args) throws IOException {


		double support = Double.parseDouble(args[0]);
//		String csvFile =  args[1];

		long start_time = 0, end_time = 0, elapsedTime = 0;
		start_time = System.nanoTime();
		long megaByte = 1024L * 1024L ;

//		double support = 0.4;

		GraankAlgo gr = new GraankAlgo();
		gr.run(support);
//		GraankAlgo.clearFiles();
		
		System.out.println("\n ------ Run Time & Peak Heap Memory Result are: ------ ");


		java.util.List<MemoryPoolMXBean> poolss = ManagementFactory.getMemoryPoolMXBeans();
		long total = 0, total_non_heap = 0;
		for (MemoryPoolMXBean memoryPoolMXBean : poolss) {
			if (memoryPoolMXBean.getType() == MemoryType.HEAP) {
				long peakUsed = memoryPoolMXBean.getPeakUsage().getUsed();
				// System.out.println("Peak used for Heap: " + memoryPoolMXBean.getName() + "
				// is: " + peakUsed);
				total = total + peakUsed;
			}

			if (memoryPoolMXBean.getType() == MemoryType.NON_HEAP) {
				long peakUsed_non_heap = memoryPoolMXBean.getPeakUsage().getUsed();
				// System.out.println("Peak used for Non_Heap: " + memoryPoolMXBean.getName() +
				// " is: " + peakUsed_non_heap);
				total_non_heap = total_non_heap + peakUsed_non_heap;
			}

		}

		System.out.println("\nTotal Heap Peak Used : " + (total + total_non_heap) / megaByte);

		end_time = System.nanoTime();

		elapsedTime = end_time - start_time;
		double seconds = (double) elapsedTime / 1000000000.0;
		System.out.println("Program Run time : " + seconds + " seconds");

	}

}

