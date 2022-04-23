package rip.az.LocationGenerator;

public class ColumnLocationGenerator extends LocationGenerator {
    @Override
    public int[] getLocationForIteration(int iteration) {
        return new int[]{0, iteration};
    }
}
