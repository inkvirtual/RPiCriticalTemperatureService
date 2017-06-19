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

    public double getTemperature() {
        // temp=39.7'C
        String temperatureString = bash.execute(resourcesHelper.getFullPath("get_cpu_temperature.sh"));

        return Double.parseDouble(resourcesHelper.getSubstring(temperatureString, "=", "'C"));
    }

    public int getFrequency() {
        // frequency(45)=600000000
        // 600_000_000
        String frequencyString = bash.execute(resourcesHelper.getFullPath("get_cpu_frequency.sh"));
        String parsedFrequency = resourcesHelper.getSubstring(frequencyString, "=", "");

        return Integer.parseInt(parsedFrequency) / 1_000;
    }

    public boolean isCpuThrottling() {
        int frequency = getFrequency();
        int deviation = 1;

        //(600_001,1_199_999)
        if (frequency > (LOW_FREQUENCY + deviation)
                && frequency < (HIGH_FREQUENCY - deviation))
            return true;

        //(infinity, 599_999)
        if (frequency < (LOW_FREQUENCY - deviation))
            return true;

        return false;
    }
}
