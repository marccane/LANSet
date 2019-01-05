// Josep Suy abril 2007
/*
public class Registre  {

	static final String INVALID_TYPE = "Null";
	static final String CHARACTER_TYPE = "car";
	static final String INTEGER_TYPE = "enter";
	static final String BOOLEAN_TYPE = "boolea";
	static final String FLOAT_TYPE = "real";


	String text;
	String type;
	int line;


	public Registre() {
		text = "";
		type = INVALID_TYPE;
		line = -1;
	}


	public Registre(String l) {
		text = l;
		type = INVALID_TYPE;
		line = -1;
	}
	public Registre(String l, String t) {
		text = l;
		type = t;
		line = -1;
	}
	public Registre(String l, String t, int a) {
		text =l;
		type =t;
		line =a;
	}


	public String getText() {
		return (text);
	}
	public String getType() {
		return (type);
	}
	public Integer getLine() {
		return (line);
	}

	public void putText(String l) {
		text =l;
	}
	public void putType(String t) {
		type =t;
	}
	public void putLine(int a) {
		line =a;
	}

}
*/


public class Registre  {

	static final String INVALID_TYPE = "Null";
	static final String CHARACTER_TYPE = "car";
	static final String INTEGER_TYPE = "enter";
	static final String BOOLEAN_TYPE = "boolea";
	static final String FLOAT_TYPE = "real";
	static final String TUPLE_TYPE = "tupla";
	static final String VECTOR_TYPE = "vector";


	String text;
	String type;
	String subtype;
	int line;
	int pos;
	int intval;


public Registre() {
	text = "";
	type = INVALID_TYPE;
	line = -1;
	pos = -1;
}

public Registre(String te) {
	text = te;
	type = INVALID_TYPE;
	line = -1;
	pos = -1;
}

public Registre(String te, String t) {
	text = te;
	type = t;
	line = -1;
	pos = -1;
}

public Registre(String te, String t, int l) {
	text = te;
	type = t;
	line = l;
	pos = -1;
}

public Registre(String te, String t, String st, int l) {
	text = te;
	type = t;
	subtype = st;
	line = l;
	pos = -1;
}

public Registre(String te, String t, int l, int p) {
	text = te;
	type = t;
	line = l;
	pos = p;
}

public Registre(String te, String t, int l, int p, int i) {
	text = te;
	type = t;
	line = l;
	pos = p;
	intval = i;
}


public String getText() {
	return (text);
	}
public String getType() {
	return (type);
	}
public Integer getLine() {
	return (line);
	}
public Integer getPos() {
	return (pos);
}
public Integer getIntval() {
		return (intval);
	}

public void putText(String te) {
	text = te;
	}
public void putType(String t) {
	type = t;
	}
public void putLine(int l) {
	line = l;
	}
public void putPos(int p) {
	pos = p;
}
public void putIntval(int i) {
	intval = i;
}
public void putSubtype(String st) {
	subtype = st;
}

}
