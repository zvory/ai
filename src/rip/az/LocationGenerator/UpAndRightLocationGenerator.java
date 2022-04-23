package rip.az.LocationGenerator;

public class UpAndRightLocationGenerator extends LocationGenerator {
    @Override
    public int[] getLocationForIteration(int iteration) {
        return new int[]{iteration, iteration};
    }
}
