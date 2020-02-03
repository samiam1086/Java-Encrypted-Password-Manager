
public class items {
	
	private String t_DATA;
	private String u_DATA;
	private String p_DATA;
	private String n_DATA;
	
	public items(String u, String p, String n, String t) {
		u_DATA = u;
		p_DATA = p;
		n_DATA = n;
		t_DATA = t;
	}

	public String getT_DATA() {
		return t_DATA;
	}

	public void setT_DATA(String t_DATA) {
		this.t_DATA = t_DATA;
	}

	public String getU_DATA() {
		return u_DATA;
	}

	public void setU_DATA(String s) {
		this.u_DATA = s;
	}

	public String getP_DATA() {
		return p_DATA;
	}

	public void setP_DATA(String s) {
		this.p_DATA = s;
	}

	public String getN_DATA() {
		return n_DATA;
	}

	public void setN_DATA(String s) {
		this.n_DATA = s;
	}
	
	public String toString() {
		return t_DATA;
	}
}
