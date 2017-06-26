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

    private boolean exitServiceIfTerminated;
    private boolean serviceTerminated = false;

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

        exitServiceIfTerminated = Boolean.parseBoolean(config.getOrDefault("service.exit.if.terminated", "false"));

        controller = new Controller(configuration.getResourcesHelper());

        ServiceMain.LOGGER.info(config.toString());
    }

    @Override
    public void start() {
        ServiceMain.LOGGER.info("----------------------------------------");
        ServiceMain.LOGGER.info("RPi Critical Temperature Service started!\n");
        sleep(2_000);

        double cpuTemperature;
        int cpuFrequency;

        while (!stopExecution()) {
            cpuTemperature = controller.getCpuTemperature();

            // Switch to debug
                ServiceMain.LOGGER.info("DEBUG: Cpu Temp: {}C", cpuTemperature);

            // If critical temperature detected
            if (cpuTemperature >= shutdownTempC) {
                ServiceMain.LOGGER.info("WARN: CPU Temperature: {}C, shutdown temperature: {}", cpuTemperature, shutdownTempC);
                ServiceMain.LOGGER.error("\nCRITICAL: Critical temperature detected, shutting down Raspberry Pi...");

                controller.shutdown();
                serviceTerminated = true;
            }

            // If RPi should shutdown on CPU Throttling
            if (shutdownOnThrottle) {
                cpuFrequency = controller.getCpuFrequency();

                // Switch to debug
                    ServiceMain.LOGGER.info("DEBUG: CPU Frequency: {}MHz", cpuFrequency);

                // Shutdown if CPU throttling detected
                if (controller.isCpuThrottling()) {
                    ServiceMain.LOGGER.info("WARN: CPU Throttling detected: {}MHz", cpuFrequency);
                    ServiceMain.LOGGER.error("\nCRITICAL: Shutting down Raspberry Pi...");

                    controller.shutdown();
                    serviceTerminated = true;
                }
            }

            sleep(checkTempIntervalS * 1_000);
        }

        ServiceMain.LOGGER.info("\n-------------------------------------------");
        ServiceMain.LOGGER.info("RPi Critical Temperature Service Terminated!");
    }

    private boolean stopExecution() {
        return (serviceTerminated && exitServiceIfTerminated);
    }

    // TODO: find a better way to mock controller
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
