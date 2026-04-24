class Length {
    private final double value;
    private final LengthUnit unit;
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);
        private final double conversionFactor;
        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }
        public double getConversionFactor() {
            return this.conversionFactor;
        }
    }
    public Length(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }
    private double convertToBaseUnit() {
        double inInches = this.value * this.unit.getConversionFactor();
        return Math.round(inInches * 10000.0) / 10000.0;
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
}
public class QuantityMeasurementApp{
    public static void main(String[] args) {
        System.out.println("--- Exécution UC4 : Vérification des Mesures ---");
        Length yard = new Length(1.0, Length.LengthUnit.YARDS);
        Length feet = new Length(3.0, Length.LengthUnit.FEET);
        System.out.println("1 Yard == 3 Feet ? " + yard.equals(feet));
        Length yard2 = new Length(1.0, Length.LengthUnit.YARDS);
        Length inches = new Length(36.0, Length.LengthUnit.INCHES);
        System.out.println("1 Yard == 36 Inches ? " + yard2.equals(inches));
        Length cm = new Length(1.0, Length.LengthUnit.CENTIMETERS);
        Length inchRef = new Length(0.393701, Length.LengthUnit.INCHES);
        System.out.println("1 CM == 0.393701 Inches ? " + cm.equals(inchRef));
    }
}