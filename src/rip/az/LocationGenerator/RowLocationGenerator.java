package rip.az.LocationGenerator;

public class RowLocationGenerator extends LocationGenerator {
    @Override
    public int[] getLocationForIteration(int iteration) {
        return new int[]{iteration, 0};
    }
}
