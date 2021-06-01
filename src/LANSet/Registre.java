package LANSet;

//TODO this could just be a java "struct"
class Registre {

    private C_SUPERTYPE supertype;
    private C_TYPE type;
    private Long index = Long.MAX_VALUE; //local variable index
    private int vecSize = -1;
    private Registre parent;

    private String text;
    private int line = -1;
    private int pos = -1;

    Registre(String te, C_SUPERTYPE st, C_TYPE t, int l) {
        text = te;
        supertype = st;
        type = t;
        line = l;
    }

    Registre(String te, C_SUPERTYPE st, C_TYPE t, int l, int p) {
        text = te;
        supertype = st;
        type = t;
        line = l;
        pos = p;
    }

    int getVecSize() {
        return vecSize;
    }

    void setVecSize(int vecSize) {
        this.vecSize = vecSize;
    }

    Registre getParent() {
        return parent;
    }

    void setParent(Registre parent) {
        this.parent = parent;
    }

    String getText() {
        return text;
    }

	C_SUPERTYPE getSupertype() {
        return supertype;
    }

    C_TYPE getType() {
        return type;
    }

    Integer getLine() {
        return line;
    }

    Integer getPos() {
        return pos;
    }

    Long getIndex() {
        if(index == Long.MAX_VALUE) //not pretty but should ease development
            //TODO errorSemantic = true;
            System.err.println("Get of uninitialized direction");
        return index;
    }

    void setIndex(Long index) {
        this.index = index;
    }
}
