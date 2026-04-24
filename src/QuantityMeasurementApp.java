import java.util.Objects;
public class Length {
    private final double value;
    private final LengthUnit unit;
    public enum LengthUnit {
        FEET(12.0), 
        INCHES(1.0), 
        YARDS(36.0), 
        CENTIMETERS(0.393701);
        private final double conversionFactor;
        LengthUnit(double factor) {
            this.conversionFactor = factor;
        }
        public double getConversionFactor() {
            return conversionFactor;
        }
    }
    public Length(double value, LengthUnit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }
    private double convertToBaseUnit() {
        double rawInches = this.value * this.unit.getConversionFactor();
        return Math.round(rawInches * 100.0) / 100.0;
    }
    public Length convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit null");
        double valueInInches = this.value * this.unit.getConversionFactor();
        double convertedValue = valueInInches / targetUnit.getConversionFactor();
        double roundedValue = Math.round(convertedValue * 100.0) / 100.0;  
        return new Length(roundedValue, targetUnit);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length that = (Length) o;
        return Double.compare(this.convertToBaseUnit(), that.convertToBaseUnit()) == 0;
    }
    @Override
    public int hashCode() {
        return Objects.hash(convertToBaseUnit());
    }
    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }
    public static class App {
        public static void demonstrateEquality(Length l1, Length l2) {
            System.out.printf("Comparing [%s] and [%s] -> Equal: %b\n", l1, l2, l1.equals(l2));
        }
        public static void main(String[] args) {
            System.out.println("=== Quantity Measurement App (UC5) ===\n");
            Length oneFoot = new Length(1, LengthUnit.FEET);
            Length resultInInches = oneFoot.convertTo(LengthUnit.INCHES);
            System.out.println("Conversion: " + oneFoot + " is " + resultInInches);

            Length threeYards = new Length(3, LengthUnit.YARDS);
            System.out.println("Conversion: " + threeYards + " is " + threeYards.convertTo(LengthUnit.FEET));

            // Equality Examples (Cross-unit)
            Length twelveInches = new Length(12, LengthUnit.INCHES);
            demonstrateEquality(oneFoot, twelveInches);

            Length cm = new Length(2.54, LengthUnit.CENTIMETERS);
            Length inch = new Length(1, LengthUnit.INCHES);
            demonstrateEquality(cm, inch);
        }
    }
}
