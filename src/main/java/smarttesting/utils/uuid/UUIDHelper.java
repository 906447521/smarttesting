package smarttesting.utils.uuid;

abstract public class UUIDHelper {
    private static String _id = "IDL:com/eaio/uuid/UUID:1.0";

    public static void insert(org.omg.CORBA.Any a, UUID that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static UUID extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;
    private static boolean __active = false;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[2];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                    _members0[0] = new org.omg.CORBA.StructMember("time", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong);
                    _members0[1] = new org.omg.CORBA.StructMember("clockSeqAndNode", _tcOf_members0, null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(UUIDHelper.id(), "UUID", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static UUID read(org.omg.CORBA.portable.InputStream istream) {
        UUID value = new UUID();
        value.time = istream.read_longlong();
        value.clockSeqAndNode = istream.read_longlong();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, UUID value) {
        ostream.write_longlong(value.time);
        ostream.write_longlong(value.clockSeqAndNode);
    }

}
