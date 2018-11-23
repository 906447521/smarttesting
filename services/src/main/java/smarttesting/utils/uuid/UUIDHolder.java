package smarttesting.utils.uuid;

public final class UUIDHolder implements org.omg.CORBA.portable.Streamable {
    public UUID value = null;

    public UUIDHolder() {
    }

    public UUIDHolder(UUID initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = UUIDHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        UUIDHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return UUIDHelper.type();
    }

}
