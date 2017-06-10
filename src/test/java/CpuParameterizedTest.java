import RaspberryPi.Cpu;
import RaspberryPi.PiBash;
import Resources.ResourcesHelper;
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
                {Cpu.LOW_FREQUENCY, false},
                {Cpu.LOW_FREQUENCY - 1, false},
                {Cpu.LOW_FREQUENCY - 2, true},
                {Cpu.LOW_FREQUENCY - 10, true},
                {Cpu.LOW_FREQUENCY - 100, true},
                {Cpu.LOW_FREQUENCY + 1, false},
                {Cpu.LOW_FREQUENCY + 2, true},
                {Cpu.LOW_FREQUENCY + 10, true},
                {Cpu.LOW_FREQUENCY + 20, true},
                {Cpu.LOW_FREQUENCY + 100, true},
                {Cpu.HIGH_FREQUENCY, false},
                {Cpu.HIGH_FREQUENCY - 1, false},
                {Cpu.HIGH_FREQUENCY - 2, true},
                {Cpu.HIGH_FREQUENCY - 10, true},
                {Cpu.HIGH_FREQUENCY - 20, true},
                {Cpu.HIGH_FREQUENCY - 100, true},
                {Cpu.HIGH_FREQUENCY + 1, false},
                {Cpu.HIGH_FREQUENCY + 5, false},
                {Cpu.HIGH_FREQUENCY + 10, false}  //false
        });
    }

    @Test
    public void test_isCpuThrottling() throws Exception{
        doReturn(String.valueOf(cpuFrequency)).when(bash).execute(anyString());

        Assert.assertEquals("CPU expected to throttle: " + cpuThrottlingExpected + ", CPU Frequency: " + cpuFrequency + "MHz", cpu.isCpuThrottling(), cpuThrottlingExpected);
        verify(bash, times(1)).execute(anyString());
    }

}
