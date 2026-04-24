import java.util.Objects;
class Length {
    private final double value;
    private final LengthUnit unit;
    public enum LengthUnit {
        INCHES(1.0),
        FEET(12.0),
        YARDS(36.0),
        CENTIMETERS(0.3937);
        private final double factor;
        LengthUnit(double factor) { this.factor = factor; }
        public double toBase(double val) { return val * this.factor; }
        public double fromBase(double baseVal) { return baseVal / this.factor; }
    }
    public Length(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = Objects.requireNonNull(unit);
    }
    public Length add(Length that) {
        return add(that, this.unit); 
    }
    public Length add(Length that, LengthUnit targetUnit) {
        Objects.requireNonNull(that, "Operand length cannot be null");
        Objects.requireNonNull(targetUnit, "Target unit cannot be null");
        return addAndConvert(this, that, targetUnit);
    }
    private Length addAndConvert(Length l1, Length l2, LengthUnit target) {
        double sumInBase = l1.unit.toBase(l1.value) + l2.unit.toBase(l2.value);
        double rawResult = target.fromBase(sumInBase);
        double rounded = Math.round(rawResult * 100.0) / 100.0;
        return new Length(rounded, target);
    }
    @Override
    public String toString() {
        return String.format("Quantity(%s, %s)", value, unit);
    }
}
public class QuanlityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== UC7: Addition with Target Unit Specification ===\n");
        Length oneFoot = new Length(1.0, Length.LengthUnit.FEET);
        Length twelveInches = new Length(12.0, Length.LengthUnit.INCHES);
        System.out.println("Target FEET:   " + oneFoot.add(twelveInches, Length.LengthUnit.FEET));
        System.out.println("Target INCHES: " + oneFoot.add(twelveInches, Length.LengthUnit.INCHES));
        System.out.println("Target YARDS:  " + oneFoot.add(twelveInches, Length.LengthUnit.YARDS));
        Length yard1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length feet3 = new Length(3.0, Length.LengthUnit.FEET);
        System.out.println("\nCommutativity Check (Target FEET):");
        System.out.println("A + B: " + yard1.add(feet3, Length.LengthUnit.FEET));
        System.out.println("B + A: " + feet3.add(yard1, Length.LengthUnit.FEET));
    }
}