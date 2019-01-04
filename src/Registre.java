// Josep Suy abril 2007

public class Registre  {

	String text;
	int type;
	int line;
	int pos;
	int intval;


public Registre() {
	text = "";
	type = -1;
	line = 0;
	pos = 0;
}

public Registre(String te) {
	text = te;
	type = -1;
	line = 0;
	pos = 0;
}

public Registre(String te, int t) {
	text = te;
	type = t;
	line = 0;
	pos = 0;
}

public Registre(String te, int t, int l) {
	text = te;
	type = t;
	line = l;
	pos = 0;
}

public Registre(String te, int t, int l, int p) {
	text = te;
	type = t;
	line = l;
	pos = p;
}

public Registre(String te, int t, int l, int p, int i) {
	text = te;
	type = t;
	line = l;
	pos = p;
	intval = i;
}


public String getText() {
	return (text);
	}
public int getType() {
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
public void putType(char t) {
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

}
