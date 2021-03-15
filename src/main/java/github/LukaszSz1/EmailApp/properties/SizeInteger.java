package github.LukaszSz1.EmailApp.properties;

public class SizeInteger implements Comparable<SizeInteger> {

    private final int size;

    public SizeInteger(final int size) {
        this.size = size;
    }

    @Override
    public int compareTo(SizeInteger s) {
        return Integer.compare(size, s.size);
    }

    @Override
    public String toString() {
        if (size <= 0) {
            return "0";
        } else if (size < 1024) {
            return size + " B";
        } else if (size < 1048576) {
            return size / 1024 + " KB";
        } else {
            return size / 1048576 + " MB";
        }
    }
}
