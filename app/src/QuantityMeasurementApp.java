import java.util.Objects; 
enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);
    private final double conversionFactor;
    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
    public double getConversionFactor() {
        return conversionFactor;
    }
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
}
class Length {
    private final double value;
    private final LengthUnit unit;
    public Length(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number.");
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit cannot be null.");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length that = (Length) o;
        return Math.abs(this.unit.convertToBaseUnit(this.value) - 
                        that.unit.convertToBaseUnit(that.value)) < 0.01;
    }
    public Length convertTo(LengthUnit targetUnit) {
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double targetValue = targetUnit.convertFromBaseUnit(baseValue);
        return new Length(round(targetValue), targetUnit);
    }
    public Length add(Length thatLength) {
        return add(thatLength, this.unit);
    }
    public Length add(Length thatLength, LengthUnit targetUnit) {
        return addAndConvert(this, thatLength, targetUnit);
    }
    private Length addAndConvert(Length l1, Length l2, LengthUnit target) {
        double firstInBase = l1.unit.convertToBaseUnit(l1.value);
        double secondInBase = l2.unit.convertToBaseUnit(l2.value);
        double totalInBase = firstInBase + secondInBase;
        double resultValue = target.convertFromBaseUnit(totalInBase);
        return new Length(round(resultValue), target);
    }
    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
    @Override
    public String toString() {
        return String.format("Quantity(%.1f, %s)", value, unit);
    }
}
public class QuantityMeasurementApp { 
    public static void main(String[] args) {
        System.out.println("=== UC8: Standalone Unit Enum & Simplified Quantity Logic ===\n");
        Length ft1 = new Length(1.0, LengthUnit.FEET);
        System.out.println("Input: 1.0 FEET converted to INCHES");
        System.out.println("Output: " + ft1.convertTo(LengthUnit.INCHES));
        Length yd1 = new Length(1.0, LengthUnit.YARDS);
        Length in36 = new Length(36.0, LengthUnit.INCHES);
        System.out.println("\nInput: 1.0 YARDS equals 36.0 INCHES");
        System.out.println("Output: " + yd1.equals(in36));
        System.out.println("\nInput: add(1.0 FEET, 12.0 INCHES, YARDS)");
        System.out.println("Output: " + ft1.add(new Length(12.0, LengthUnit.INCHES), LengthUnit.YARDS));
        System.out.println("\nDirect Unit Conversion Responsibility Check:");
        System.out.println("12.0 INCHES to base (FEET context): " + LengthUnit.INCHES.convertToBaseUnit(12.0) + " inches total");
        System.out.println("Base unit value 1.0 converted to FEET: " + LengthUnit.FEET.convertFromBaseUnit(1.0));
    }
}