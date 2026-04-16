public class QuantityMeasurementApp {
    public static class Feet {
        private final double value;
        public Feet(double value) {
            this.value = value;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet that = (Feet) obj;
            return Double.compare(that.value, this.value) == 0;
        }
    }
    public static void main(String[] args) {
        Feet firstValue = new Feet(1.0);
        Feet secondValue = new Feet(1.0);
        System.out.println("Input: 1.0 ft and 1.0 ft");
        if (firstValue.equals(secondValue)) {
            System.out.println("Output: Equal (true)");
        } else {
            System.out.println("Output: Not Equal (false)");
        }
    }
}