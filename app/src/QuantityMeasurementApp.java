import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
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
    public double convertToBaseUnit(double v) {
        return v * factor;
    }
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
    public double convertToBaseUnit(double v) {
        return v * factor;
    }

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

    public double convertToBaseUnit(double v) {
        return v * factor;
    }

    public double convertFromBaseUnit(double b) {
        return b / factor;
    }
}

enum ArithmeticOperation {
    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> a / b);

    private final DoubleBinaryOperator operator;

    ArithmeticOperation(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    public double compute(double a, double b) {
        return operator.applyAsDouble(a, b);
    }
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    private void validateArithmeticOperands(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException();
        }
        if (Double.isNaN(this.value) || Double.isNaN(other.value)) {
            throw new IllegalArgumentException();
        }
        if (Double.isInfinite(this.value) || Double.isInfinite(other.value)) {
            throw new IllegalArgumentException();
        }
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        validateArithmeticOperands(other);
        double a = this.unit.convertToBaseUnit(this.value);
        double b = other.unit.convertToBaseUnit(other.value);
        return operation.compute(a, b);
    }

    public Quantity<U> add(Quantity<U> other) {
        double result = performBaseArithmetic(other, ArithmeticOperation.ADD);
        return new Quantity<>(unit.convertFromBaseUnit(result), unit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        return new Quantity<>(unit.convertFromBaseUnit(result), unit);
    }

    public double divide(Quantity<U> other) {
        double result = performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity<?>)) return false;
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
