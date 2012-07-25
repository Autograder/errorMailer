/**
 * @author maklemenz
 */
package helper.hermes;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SysInfo {

	private static OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

	private static Runtime runtime = Runtime.getRuntime();

	private static int MB = (1024 * 1024);

	private double freeMemory;

	private double totalMemory;

	private double maxMemory;

	private double loadAvg;

	private int availableProcessors;


	public SysInfo() {
		// getMemInfo
		freeMemory = ((double) runtime.freeMemory()) / MB;
		totalMemory = ((double) runtime.totalMemory()) / MB;
		maxMemory = ((double) runtime.maxMemory()) / MB;
		// getCpuInfo
		loadAvg = osBean.getSystemLoadAverage();
		availableProcessors = osBean.getAvailableProcessors();
	}


	/**
	 * @return the freeMemory
	 */
	public double getFreeMemory() {
		return freeMemory;
	}


	/**
	 * @return the totalMemory
	 */
	public double getTotalMemory() {
		return totalMemory;
	}


	/**
	 * @return the maxMemory
	 */
	public double getMaxMemory() {
		return maxMemory;
	}


	/**
	 * @return the loadAvg
	 */
	public double getLoadAvg() {
		return loadAvg;
	}


	/**
	 * @return the availableProcessors
	 */
	public int getAvailableProcessors() {
		return availableProcessors;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("SysInfo [freeMemory=%s, totalMemory=%s, maxMemory=%s, loadAvg=%s, availableProcessors=%s]",
						freeMemory, totalMemory, maxMemory, loadAvg, availableProcessors);
	}
	
}
