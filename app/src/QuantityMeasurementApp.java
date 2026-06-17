
import java.util.Objects;
interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
}

enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0);
    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    @Override public double convertToBaseUnit(double v) { return v * factor; }
    @Override public double convertFromBaseUnit(double b) { return b / factor; }
}

enum WeightUnit implements IMeasurable {
    GRAM(1.0), KILOGRAM(1000.0), POUND(453.6);
    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }
    @Override public double convertToBaseUnit(double v) { return v * factor; }
    @Override public double convertFromBaseUnit(double b) { return b / factor; }
}
enum VolumeUnit implements IMeasurable {
    LITRE(1.0), MILLILITRE(0.001), GALLON(3.785);
    private final double factor; 
    VolumeUnit(double factor) { this.factor = factor; }
    @Override public double convertToBaseUnit(double v) { return v * factor; }
    @Override public double convertFromBaseUnit(double b) { return b / factor; }
}
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateCategory(other);
        double sum = getBaseValue() + other.getBaseValue();
        return new Quantity<>(targetUnit.convertFromBaseUnit(sum), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateCategory(other);
        double diff = getBaseValue() - other.getBaseValue();
        return new Quantity<>(targetUnit.convertFromBaseUnit(diff), targetUnit);
    }
    public double divide(Quantity<U> other) {
        validateCategory(other);
        double divisor = other.getBaseValue();
        if (divisor == 0) throw new ArithmeticException("Cannot divide by zero quantity");
        return getBaseValue() / divisor;
    }
    private double getBaseValue() { return unit.convertToBaseUnit(value); }
    private void validateCategory(Quantity<?> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category arithmetic is not allowed");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        if (this.unit.getClass() != that.unit.getClass()) return false;
        return Math.abs(this.getBaseValue() - ((Quantity<IMeasurable>)that).getBaseValue()) < 0.01;
    }

    @Override public int hashCode() { return Objects.hash(value, unit); }
    @Override public String toString() { return String.format("%.2f %s", value, unit); }

    public void subtract(Object other, LengthUnit feet) {
        throw new UnsupportedOperationException("Unimplemented method 'subtract'");
    }
}
public class QuantityMeasurementApp {
    public static void main(String[] args) {

        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        System.out.println("1 Gallon + 1 Litre in Litres: " + gallon.add(litre, VolumeUnit.LITRE));
        Quantity<LengthUnit> tenFeet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> twoFeet = new Quantity<>(2.0, LengthUnit.FEET);
        System.out.println("10ft - 2ft: " + tenFeet.subtract(twoFeet, LengthUnit.FEET));
        System.out.println("10ft / 2ft (Ratio): " + tenFeet.divide(twoFeet));
        try { tenFeet.subtract(new Quantity<>(1.0, WeightUnit.KILOGRAM), LengthUnit.FEET); } 
        catch (Exception e) { System.out.println("Error caught: " + e.getMessage()); }
    }
} 
