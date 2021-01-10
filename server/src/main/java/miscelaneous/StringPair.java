package miscelaneous;

import java.util.Objects;

public class StringPair{
    public final String first;
    public final String second;

    public StringPair(String first, String second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringPair that = (StringPair) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public String toString() {
        return "StringPair{" +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
