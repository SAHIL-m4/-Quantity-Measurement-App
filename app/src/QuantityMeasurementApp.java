import java.util.Objects;
interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
} 
enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CM(0.4);
    private final double factor;
    LengthUnit(double factor) {
        this.factor = factor;
    } 
    @Override
    public double convertToBaseUnit(double v) {
        return v * factor;
    }
    @Override
    public double convertFromBaseUnit(double b) {
        return b / factor;
    }
}
enum WeightUnit implements IMeasurable {
    GRAM(1.0), KILOGRAM(1000.0), POUND(453.6);
    private final double factor;
    WeightUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double v) {
        return v * factor;
    }
    @Override
    public double convertFromBaseUnit(double b) {
        return b / factor;
    }
}
enum VolumeUnit implements IMeasurable {
    LITRE(1.0), MILLILITRE(0.001), GALLON(3.785);
    private final double factor;
    VolumeUnit(double factor) {
        this.factor = factor;
    }
    @Override
    public double convertToBaseUnit(double v) {
        return v * factor;
    }
    @Override
    public double convertFromBaseUnit(double b) {
        return b / factor;
    }
} 
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double totalInBase = this.unit.convertToBaseUnit(this.value) +
                             other.unit.convertToBaseUnit(other.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(totalInBase), targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quantity<?> that = (Quantity<?>) o;

        if (this.unit.getClass() != that.unit.getClass()) return false;

        double v1 = this.unit.convertToBaseUnit(this.value);
        double v2 = ((IMeasurable) that.unit).convertToBaseUnit(that.value);

        return Math.abs(v1 - v2) < 0.01;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litres = new Quantity<>(3.785, VolumeUnit.LITRE);
        System.out.println("1 Gallon == 3.785 Litres: " + gallon.equals(litres));
        Quantity<VolumeUnit> ml1000 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> oneLitre = new Quantity<>(1.0, VolumeUnit.LITRE);
        System.out.println("1L + 1000mL in Litres: " + oneLitre.add(ml1000, VolumeUnit.LITRE));
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        System.out.println("Litre equals Feet? " + oneLitre.equals(feet));
    }
}
