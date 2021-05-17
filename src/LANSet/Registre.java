package LANSet;

//TODO this could just be a java "struct"
class Registre {

    private String text;  //isn't this completely useless? It's always the key of the dictionary
    private C_SUPERTYPE supertype;
    private C_TYPE type;

    private int line;
    private int pos;
    private int intval;
    private Long dir = Long.MAX_VALUE;
    int vecSize; //i'm not making more getter/setters
    Registre parent;

    Registre() {
        text = "";
        supertype = C_SUPERTYPE.INVALID_SUPERTYPE;
        type = C_TYPE.INVALID_TYPE;
        line = -1;
        pos = -1;
    }

    Registre(String te) {
        text = te;
		supertype = C_SUPERTYPE.INVALID_SUPERTYPE;
		type = C_TYPE.INVALID_TYPE;
        line = -1;
        pos = -1;
    }

    Registre(String te, C_SUPERTYPE st) {
        text = te;
        supertype = st;
		type = C_TYPE.INVALID_TYPE;
        line = -1;
        pos = -1;
    }

    Registre(String te, C_SUPERTYPE st, C_TYPE t) {
        text = te;
        supertype = st;
        type = t;
        line = -1;
        pos = -1;
    }

    Registre(String te, C_SUPERTYPE st, C_TYPE t, int l) {
        text = te;
        supertype = st;
        type = t;
        line = l;
        pos = -1;
    }

    Registre(String te, C_SUPERTYPE st, C_TYPE t, int l, int p) {
        text = te;
        supertype = st;
        type = t;
        line = l;
        pos = p;
    }

    Registre(String te, C_SUPERTYPE st, C_TYPE t, int l, int p, int vecSize) {
        text = te;
        supertype = st;
        type = t;
        line = l;
        pos = p;
        this.vecSize = vecSize;
    }

    String getText() {
        return (text);
    }

	C_SUPERTYPE getSupertype() {
        return (supertype);
    }

    C_TYPE getType() {
        return (type);
    }

    Integer getLine() {
        return (line);
    }

    Integer getPos() {
        return (pos);
    }

    Integer getIntval() {
        return (intval);
    }

    Long getDir() {
        if(dir == Long.MAX_VALUE) //not pretty but should ease development
            //TODO errorSemantic = true;
            System.err.println("Get of uninitialized direction");
        return dir;
    }

    void putText(String te) {
        text = te;
    }

    void putType(C_TYPE t) {
        type = t;
    }

    void putLine(int l) {
        line = l;
    }

    void putPos(int p) {
        pos = p;
    }

    void putIntval(int i) {
        intval = i;
    }

    void putSupertype(C_SUPERTYPE st) {
        supertype = st;
    }

    void putDir(Long d) {
        dir = d;
    }
}
