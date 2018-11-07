
package smarttesting.utils.uuid;

import org.omg.CORBA.portable.IDLEntity;
import org.springframework.util.StopWatch;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class UUID implements Comparable<UUID>, Externalizable, Cloneable, IDLEntity {

    public static String MIN = "00000000000000000000000000000000";
    public static String MAX = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";

    static final long serialVersionUID = 7435962790062944603L;

    public long time;

    public long clockSeqAndNode;

    public UUID() {
        this(UUIDGen.newTime(), UUIDGen.getClockSeqAndNode());
    }

    public UUID(long time, long clockSeqAndNode) {
        this.time = time;
        this.clockSeqAndNode = clockSeqAndNode;
    }

    public UUID(UUID u) {
        this(u.time, u.clockSeqAndNode);
    }

    public UUID(CharSequence s) {
        this(Hex.parseLong(s.subSequence(0, 18)), Hex.parseLong(s.subSequence(19, 36)));
    }

    public int compareTo(UUID t) {
        if (this == t) {
            return 0;
        }
        if (time > t.time) {
            return 1;
        }
        if (time < t.time) {
            return -1;
        }
        if (clockSeqAndNode > t.clockSeqAndNode) {
            return 1;
        }
        if (clockSeqAndNode < t.clockSeqAndNode) {
            return -1;
        }
        return 0;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(time);
        out.writeLong(clockSeqAndNode);
    }

    public void readExternal(ObjectInput in) throws IOException {
        time = in.readLong();
        clockSeqAndNode = in.readLong();
    }

    @Override
    public final String toString() {
        return toAppendable(null).toString();
    }

    public final String toString32() {
        return toAppendableWithoutSeparator(null).toString();
    }

    public StringBuffer toStringBuffer(StringBuffer in) {
        StringBuffer out = in;
        if (out == null) {
            out = new StringBuffer(36);
        } else {
            out.ensureCapacity(out.length() + 36);
        }
        return (StringBuffer) toAppendable(out);
    }

    public Appendable toAppendable(Appendable a) {
        Appendable out = a;
        if (out == null) {
            out = new StringBuilder(36);
        }
        try {
            Hex.append(out, (int) (time >> 32)).append('-');
            Hex.append(out, (short) (time >> 16)).append('-');
            Hex.append(out, (short) time).append('-');
            Hex.append(out, (short) (clockSeqAndNode >> 48)).append('-');
            Hex.append(out, clockSeqAndNode, 12);
        } catch (IOException ex) {

        }
        return out;
    }

    public Appendable toAppendableWithoutSeparator(Appendable a) {
        Appendable out = a;
        if (out == null) {
            out = new StringBuilder(36);
        }
        try {
            Hex.append(out, (int) (time >> 32));
            Hex.append(out, (short) (time >> 16));
            Hex.append(out, (short) time);
            Hex.append(out, (short) (clockSeqAndNode >> 48));
            Hex.append(out, clockSeqAndNode, 12);
        } catch (Exception ex) {

        }
        return out;
    }

    @Override
    public int hashCode() {
        return (int) ((time >> 32) ^ time ^ (clockSeqAndNode >> 32) ^ clockSeqAndNode);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public final long getTime() {
        return time;
    }

    public final long getClockSeqAndNode() {
        return clockSeqAndNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) {
            return false;
        }
        return compareTo((UUID) obj) == 0;
    }

    public static UUID nilUUID() {
        return new UUID(0, 0);
    }

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 10000000; i++) {
            new UUID();
        }
        watch.stop();
        System.out.println(watch.prettyPrint());
    }
}
