package Service;

import Configuration.Configuration;

import java.util.Map;

//import Statistics.PiHealthStatistics;

/**
 * Created by fanta on 5/27/17.
 */
public class RPiCriticalTemperatureService implements IService {
    private int checkTempIntervalS;
    private double shutdownTempC;
    private boolean shutdownOnThrottle;

    private Controller controller;

    // TODO: switch to interface, not class implementation
    public RPiCriticalTemperatureService(Configuration configuration) {
        init(configuration);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // TODO: switch to interface, not implementation
    private void init(Configuration configuration) {
        Map<String, String> config = configuration.getProperties();

        shutdownTempC = Double.parseDouble(config.getOrDefault("rpi.shutdown.temperature.celsius", "70"));
        checkTempIntervalS = Integer.parseInt(config.getOrDefault("rpi.check.temp.interval.s", "15"));
        shutdownOnThrottle = Boolean.parseBoolean(config.getOrDefault("rpi.throttle.shutdown", "true"));

        controller = new Controller(configuration.getResourcesHelper());

        System.out.println(config.toString());
    }

    @Override
    public void start() {
        System.out.println("Starting RPi Critical Temperature Service");

        boolean terminated = false;
        double cpuTemperature;

        while (!terminated) {
            cpuTemperature = controller.getCpuTemperature();

            // If critical temperature detected
            if (cpuTemperature >= shutdownTempC) {
                System.out.println("CPU Temperature: " + cpuTemperature + ", shutdown temperature: " + shutdownTempC);
                System.out.println("Critical temperature detected, shutting down Raspberry Pi...");

                controller.shutdown();
            }

            // If RPi should shutdown on CPU Throttling
            if (shutdownOnThrottle) {

                // Shutdown if CPU throttling detected
                if (controller.isCpuThrottling()) {
                    System.out.println("CPU Throttling detected: " + controller.getCpuFrequency());
                    System.out.println("Shutting down Raspberry Pi...");
                    controller.shutdown();
                }
            }

            sleep(checkTempIntervalS * 1_000);
        }
    }

    // TODO: find a better way to mock controller
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
