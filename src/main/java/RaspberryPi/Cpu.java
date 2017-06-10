package RaspberryPi;

import Resources.ResourcesHelper;

/*
 * Created by dev on 28.03.2017.
 */
public class Cpu {
    public static final int LOW_FREQUENCY = 600_000;
    public static final int HIGH_FREQUENCY = 1_200_000;

    private PiBash bash;
    private ResourcesHelper resourcesHelper;

    public Cpu(PiBash bash, ResourcesHelper resourcesHelper) {
        this.resourcesHelper = resourcesHelper;
        this.bash = bash;
    }

    public int getUsage() {
        return Integer.parseInt(bash.execute(
                resourcesHelper.getFullPath("get_cpu_usage.sh")));
    }

    public double getTemperature() {
        return Double.parseDouble(bash.execute(
                resourcesHelper.getFullPath("get_cpu_temperature.sh")));
    }

    public int getFrequency() {
        return Integer.parseInt(bash.execute(
                resourcesHelper.getFullPath("get_cpu_frequency.sh")));
    }

    public boolean isCpuThrottling() {
        int frequency = getFrequency();

        //[600_010,1_199_990]
        if (frequency > (LOW_FREQUENCY + 10) && frequency < (HIGH_FREQUENCY - 10))
            return true;

        //(infinity, 599_990]
        if (frequency < (LOW_FREQUENCY - 10))
            return true;

        return false;
    }
}