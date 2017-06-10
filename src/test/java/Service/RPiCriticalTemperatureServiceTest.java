package Service;

import Configuration.Configuration;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by fanta on 5/30/17.
 */
public class RPiCriticalTemperatureServiceTest {
    private Configuration mockConfiguration;
    private Controller mockController;
    // TODO: switch to interface
    private RPiCriticalTemperatureService service;

    @Before
    public void setUp() throws Exception {
        mockConfiguration = mock(Configuration.class);
        mockController = mock(Controller.class);

//        doReturn(setConfig).when(mockConfiguration).getProperties();
//
//        service = new RPiTemperatureService(mockConfiguration);
//        service.setController(mockController);
    }

    @After
    public void tearDown() throws Exception {
    }

    // Invalid test
    @Ignore
    @Test
    public void test_AssertCheckConfiguration() throws Exception {
        Map<String, String> setConfig = setupConfig(
                75,
                15,
                true
        );

        doReturn(setConfig).when(mockConfiguration).getProperties();

        service = new RPiCriticalTemperatureService(mockConfiguration);
        service.setController(mockController);

        Assert.assertEquals("Test configuration not set properly", setConfig, mockConfiguration.getProperties());
    }

    @Test
    public void test_start_criticalTemp() throws Exception {
        Map<String, String> setConfig = setupConfig(
                75,
                2,
                false
        );

        doReturn(setConfig).when(mockConfiguration).getProperties();
        doNothing().when(mockController).shutdown();

        service = new RPiCriticalTemperatureService(mockConfiguration);
        service.setController(mockController);


        doReturn(60d).
                doReturn(70d).
                doReturn(74d).
                doReturn(75d).
                doReturn(74d).
                doReturn(70d).
                when(mockController).getCpuTemperature();

        service.start();

        verify(mockController, times(1)).shutdown();
        verify(mockController, times(4)).getCpuTemperature();
        verify(mockController, times(0)).getCpuFrequency();
    }

    @Test
    public void test_start_throttle() throws Exception {
        Map<String, String> setConfig = setupConfig(
                75,
                2,
                true
        );

        doReturn(setConfig).when(mockConfiguration).getProperties();
        doNothing().when(mockController).shutdown();

        service = new RPiCriticalTemperatureService(mockConfiguration);
        service.setController(mockController);


        doReturn(60d).
                doReturn(70d).
                doReturn(71d).
                doReturn(70d).
                when(mockController).getCpuTemperature();

        doReturn(1_200_000).
                doReturn(1_199_999).
                doReturn(1_199_998).
                doReturn(1_199_997).
                when(mockController).getCpuFrequency();

        doReturn(false).
                doReturn(false).
                doReturn(true).
                doReturn(true).
                when(mockController).isCpuThrottling();

        service.start();

        verify(mockController, times(1)).shutdown();
        verify(mockController, times(3)).getCpuTemperature();
        verify(mockController, times(3)).getCpuFrequency();
    }

//    @Ignore
//    @Test
//    public void test_start_1() throws Exception {
//        Map<String, String> setConfig = setupConfig(
//                false,
//                65d,
//                false,
//                0,
//                20,
//                Fan.FanFailureAction.LOG,
//                500
//        );
//
//        doReturn(setConfig).when(mockConfiguration).getProperties();
//
//        service = new RPiTemperatureService(mockConfiguration);
//        service.setController(mockController);
//
//        Assert.assertEquals("Test configuration not set properly", setConfig, mockConfiguration.getProperties());
//
//        doReturn(45d).
////                doReturn(50d).
////                doReturn(55d).
////                doReturn(60d).
////                doReturn(65d).
//        doReturn(70d).
//                doReturn(75d).
//                doReturn(75d).
//                doReturn(75d).
//                doReturn(70d).
//                doReturn(65d).
//                doReturn(60d).
//                doReturn(55d).
//                doReturn(55d).
//                doReturn(55d).
//                when(mockController).getCpuTemperature();
//
//        doReturn(true).when(mockController).startFan();
//
//        doReturn(false).
//                doReturn(false).
//                doReturn(true).
//                when(mockController).isFanOn();
////        doReturn(false).when(mockController).isCpuThrottling();
//
//        service.start();
//
//    }

    private Map<String, String> setupConfig(
            double shutdownTempC,
            int checkTempIntervalS,
            boolean shutdownOnThrottle
    ) {
        Map<String, String> config = new HashMap<>();
        config.put("rpi.shutdown.temperature.celsius", String.valueOf(shutdownTempC));
        config.put("rpi.check.temp.interval.s", String.valueOf(checkTempIntervalS));
        config.put("rpi.throttle.shutdown", String.valueOf(shutdownOnThrottle));

        return config;
    }
}