import java.util.Objects;
class Length { 
    private final double value;
    private final LengthUnit unit;
    public enum LengthUnit {
        INCHES(1.0),
        FEET(12.0),
        YARDS(36.0),
        CENTIMETERS(0.3937); 
        private final double conversionFactor;
        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }
        public double toBase(double value) {
            return value * this.conversionFactor;
        }
        public double fromBase(double baseValue) {
            return baseValue / this.conversionFactor;
        }
    }
    public Length(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit cannot be null");
    }
    public Length add(Length thatLength) {
        Objects.requireNonNull(thatLength, "Operand cannot be null");
        double totalInBase = this.unit.toBase(this.value) + 
                             thatLength.unit.toBase(thatLength.value);
        double convertedResult = this.unit.fromBase(totalInBase);
        double roundedValue = Math.round(convertedResult * 100.0) / 100.0;
        return new Length(roundedValue, this.unit);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length that = (Length) o;
        return Math.abs(this.unit.toBase(this.value) - that.unit.toBase(that.value)) < 0.01;
    }
    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}
public class QuanlityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== UC6 Length Addition Demonstration ===\n");
        Length ft1 = new Length(1.0, Length.LengthUnit.FEET);
        Length ft2 = new Length(2.0, Length.LengthUnit.FEET);
        printAddition(ft1, ft2);
        Length in12 = new Length(12.0, Length.LengthUnit.INCHES);
        printAddition(ft1, in12);
        printAddition(in12, ft1);
        Length yd1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length ft3 = new Length(3.0, Length.LengthUnit.FEET);
        printAddition(yd1, ft3);
        Length cmBase = new Length(2.54, Length.LengthUnit.CENTIMETERS);
        Length in1 = new Length(1.0, Length.LengthUnit.INCHES);
        printAddition(cmBase, in1);
    }
    private static void printAddition(Length l1, Length l2) {
        Length result = l1.add(l2);
        System.out.printf("Input:  %s + %s%n", l1, l2);
        System.out.printf("Output: %s%n%n", result);
    }
}