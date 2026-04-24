import java.util.Objects;
enum LengthUnit {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);
    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    public double toBase(double val) { return val * factor; }
    public double fromBase(double baseVal) { return baseVal / factor; }
}
enum WeightUnit {
    GRAM(1.0), KILOGRAM(1000.0), POUND(453.592), MILLIGRAM(0.001), TONNE(1000000.0);
    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }
    public double toBase(double val) { return val * factor; }
    public double fromBase(double baseVal) { return baseVal / factor; }
}
class QuantityLength {
    private final double value;
    private final LengthUnit unit;
    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = Objects.requireNonNull(unit);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;
        return Math.abs(this.unit.toBase(this.value) - that.unit.toBase(that.value)) < 0.01;
    }
    public QuantityLength add(QuantityLength that, LengthUnit target) {
        double sum = this.unit.toBase(this.value) + that.unit.toBase(that.value);
        return new QuantityLength(Math.round(target.fromBase(sum) * 100.0) / 100.0, target);
    }
    @Override
    public String toString() { return value + " " + unit; }
}
class QuantityWeight {
    private final double value;
    private final WeightUnit unit;
    public QuantityWeight(double value, WeightUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = Objects.requireNonNull(unit);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityWeight that = (QuantityWeight) o;
        return Math.abs(this.unit.toBase(this.value) - that.unit.toBase(that.value)) < 0.01;
    }
    public QuantityWeight add(QuantityWeight that, WeightUnit target) {
        double sum = this.unit.toBase(this.value) + that.unit.toBase(that.value);
        return new QuantityWeight(Math.round(target.fromBase(sum) * 100.0) / 100.0, target);
    }
    public QuantityWeight convertTo(WeightUnit target) {
        double base = this.unit.toBase(this.value);
        return new QuantityWeight(Math.round(target.fromBase(base) * 100.0) / 100.0, target);
    }
    @Override
    public String toString() { return value + " " + unit; }
}
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityWeight kg1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight g1000 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight lb2_2 = new QuantityWeight(2.20462, WeightUnit.POUND);
        System.out.println("Equality: " + kg1.equals(g1000));
        System.out.println("Approx Equality: " + kg1.equals(lb2_2));
        QuantityWeight sum = kg1.add(new QuantityWeight(453.592, WeightUnit.GRAM), WeightUnit.POUND);
        System.out.println("Sum in Pounds: " + sum);
        QuantityLength ft1 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("Incompatibility Check: " + kg1.equals(ft1));
        QuantityWeight lb1 = new QuantityWeight(1.0, WeightUnit.POUND);
        System.out.println("Conversion: " + lb1.convertTo(WeightUnit.GRAM));
    }
}