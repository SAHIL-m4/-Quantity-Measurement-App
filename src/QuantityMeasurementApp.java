public class QuantityMeasurementApp {
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0);
        private final double conversionFactor;
        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }
        public double getConversionFactor() {
            return conversionFactor;
        }
    }
    public static class Length {
        private final double value;
        private final LengthUnit unit;
        public Length(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }
        private double convertToBaseUnit() {
            return value * unit.getConversionFactor();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length length = (Length) o;
            return Double.compare(this.convertToBaseUnit(), length.convertToBaseUnit()) == 0;
        }
    }
    public static void demonstrateFeetEquality() {
        Length f1 = new Length(1.0, LengthUnit.FEET);
        Length f2 = new Length(1.0, LengthUnit.FEET);
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + f1.equals(f2) + ")");
    }
    public static void demonstrateInchesEquality() {
        Length i1 = new Length(1.0, LengthUnit.INCHES);
        Length i2 = new Length(1.0, LengthUnit.INCHES);
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + i1.equals(i2) + ")");
    }
    public static void demonstrateCrossUnitEquality() {
        Length feet = new Length(1.0, LengthUnit.FEET);
        Length inches = new Length(12.0, LengthUnit.INCHES);
        System.out.println("Input: 1.0 ft and 12.0 inches");
        System.out.println("Output: Equal (" + feet.equals(inches) + ")");
    }
    public static void main(String[] args) {
        System.out.println("--- UC1: Feet Equality ---");
        demonstrateFeetEquality();
        System.out.println("\n--- UC2: Inches Equality ---");
        demonstrateInchesEquality();
        System.out.println("\n--- UC3: Cross-Unit Comparison ---");
        demonstrateCrossUnitEquality();
    }
}