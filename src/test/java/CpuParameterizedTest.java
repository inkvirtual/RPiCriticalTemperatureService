import RaspberryPi.Cpu;
import RaspberryPi.PiBash;
import Resources.ResourcesHelper;
import Service.ServiceMain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by fanta on 6/10/17.
 */

@RunWith(Parameterized.class)
public class CpuParameterizedTest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CpuParameterizedTest.class);

    private PiBash bash;
    private ResourcesHelper resourcesHelper;
    private Cpu cpu;

    private int cpuFrequency;
    private boolean cpuThrottlingExpected;

    public CpuParameterizedTest(int frequency, boolean throttlingExpected) {
        bash = mock(PiBash.class);
        resourcesHelper = mock(ResourcesHelper.class);
        cpu = new Cpu(bash, resourcesHelper);

        doReturn("").when(resourcesHelper).getFullPath(anyString());

        this.cpuFrequency = frequency;
        this.cpuThrottlingExpected = throttlingExpected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameterizedMethod() {
        return Arrays.asList(new Object[][] {
                {Cpu.LOW_FREQUENCY, false},         //0
                {Cpu.LOW_FREQUENCY - 1, false},     //1
                {Cpu.LOW_FREQUENCY - 2, true},      //2
                {Cpu.LOW_FREQUENCY - 10, true},     //3
                {Cpu.LOW_FREQUENCY - 100, true},    //4
                {Cpu.LOW_FREQUENCY + 1, false},     //5
                {Cpu.LOW_FREQUENCY + 2, true},      //6
                {Cpu.LOW_FREQUENCY + 10, true},     //7
                {Cpu.LOW_FREQUENCY + 20, true},     //8
                {Cpu.LOW_FREQUENCY + 100, true},    //9
                {Cpu.HIGH_FREQUENCY, false},        //10
                {Cpu.HIGH_FREQUENCY - 1, false},    //11
                {Cpu.HIGH_FREQUENCY - 2, true},     //12
                {Cpu.HIGH_FREQUENCY - 10, true},    //13
                {Cpu.HIGH_FREQUENCY - 20, true},    //14
                {Cpu.HIGH_FREQUENCY - 100, true},   //15
                {Cpu.HIGH_FREQUENCY + 1, false},    //16
                {Cpu.HIGH_FREQUENCY + 5, false},    //17
                {Cpu.HIGH_FREQUENCY + 10, false}    //18
        });
    }

    @Test
    public void checkIfCpuIsThrottling() throws Exception{
        when(resourcesHelper.getSubstring(any(), any(), any())).thenCallRealMethod();
        doReturn(("frequency(45)=" + String.valueOf(cpuFrequency))).when(bash).execute(anyString());

        ServiceMain.LOGGER.info("checkIfCpuIsThrottling: CPU Frequency {}, expected to throttle:{}", cpuFrequency, cpuThrottlingExpected);

        Assert.assertEquals("CPU expected to throttle: " + cpu.isCpuThrottling() +
                ", CPU Frequency: " + cpuFrequency + "MHz", cpuThrottlingExpected, cpuThrottlingExpected);
        verify(bash, times(1)).execute(anyString());
    }

}
