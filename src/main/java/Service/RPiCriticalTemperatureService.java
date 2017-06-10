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
        System.out.println("----------------------------------------");
        System.out.println("RPi Critical Temperature Service started!\n");
        sleep(2_000);

        boolean terminated = false;
        double cpuTemperature;
        int cpuFrequency;

        while (!terminated) {
            cpuTemperature = controller.getCpuTemperature();

            // DEBUG
            {
                System.out.println("DEBUG: Cpu Temp: " + cpuTemperature + "C");
            }


            // If critical temperature detected
            if (cpuTemperature >= shutdownTempC) {
                System.out.println("WARN: CPU Temperature: " + cpuTemperature + "C, shutdown temperature: " + shutdownTempC + "C");
                System.out.println("\nCRITICAL: Critical temperature detected, shutting down Raspberry Pi...");

                controller.shutdown();
                terminated = true;
            }

            // If RPi should shutdown on CPU Throttling
            if (shutdownOnThrottle) {
                cpuFrequency = controller.getCpuFrequency();

                // DEBUG
                {
                    System.out.println("DEBUG: CPU Frequency: " + cpuFrequency + "MHz");
                }

                // Shutdown if CPU throttling detected
                if (controller.isCpuThrottling()) {
                    System.out.println("WARN: CPU Throttling detected: " + cpuFrequency + "MHz");
                    System.out.println("\nCRITICAL: Shutting down Raspberry Pi...");

                    controller.shutdown();
                    terminated = true;
                }
            }

            sleep(checkTempIntervalS * 1_000);
        }

        System.out.println("\n-------------------------------------------");
        System.out.println("RPi Critical Temperature Service terminated!");
    }

    // TODO: find a better way to mock controller
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
