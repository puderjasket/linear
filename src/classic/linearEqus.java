package classic;

import java.util.ArrayList;

import vectorspace.dimVector;

public class linearEqus {
	//m represents the number of rows
	//zero represents the accuracy of this system
	//In other world,it can't deal with 'uncommon' Equations
	//Unless we succeed in field building
	//apparently
	public final static double zero = 0.00001; 
	private int m,n;
	private linearEqu[] l;
	//solving variables
	private double[] jie;
	private boolean[] stand;
	private int[] whichLine;
	//columnFullRank <=> f = true
	private boolean f;
	//save all transformations
	private ArrayList<PrimaryChange> p;
	//constructor(m represents the number of the equations)
	public linearEqus(int m,linearEqu[] l){
		this.m = m;
		this.l = l;
		//just for pointed input
		n = l[0].getn();
		jie = null;
		stand = new boolean[n];
		whichLine = new int[n];
		for(int j = 0;j<n;j++)
			whichLine[j] = -1;
		f = true;
		p = new ArrayList<PrimaryChange>();
	}
	
	//get set
	public double getaij(int i,int j){
		return l[i].getaj(j);
	}
	
	public double getbi(int i){
		return l[i].getb();
	}
	public boolean[] gets(){
		return stand;
	}
	public double[] getjie(){
		return jie;
	}
	public boolean getf(){
		return f;
	}
	public void caij(int i,int j,int a) {
		l[i].caj(j,a);
	}
	public void cbi(int i,int b) {
		l[i].cb(b);
	}
	public void show(){
		if(!check()){
			System.out.println(false);
		}else{
			System.out.println(true);
			for(int j = 0;j<n;j++){
				System.out.println(jie[j]+" "+stand[j]);
			}
		}
	}
	//get p
	public ArrayList<PrimaryChange> getp(){
		p = new ArrayList<PrimaryChange>();
		check();
		return p;
	}
	//the columnRank
	public int lines(){
		int r = 0;
		for(int j = 0;j<n;j++)
			if(stand[j])
				r++;
		return r;
	}
	
	public static boolean if0(double small) {
		return (Math.abs(small)<zero)?true:false;
	}
	
	public void p1(int i,int j){
		linearEqu t = l[i].get();
		l[i] = l[j].get();
		l[j] = t;
		p.add(new PrimaryChange(i,j));
	}
	public void p2(int i,double c){
		l[i].timesk(c);
		p.add(new PrimaryChange(i,c));
	}
	// i j -> i j+k*i
	public void p3(int i,int j,double k){
		linearEqu t = l[i].get();
		t.timesk(k);
		l[j].plus(t);
		p.add(new PrimaryChange(i,j,k));
	}
	private class PrimaryChange implements Primary{
		/*
		it reflects the type of transformation
		r=1 p1(int i,int j);
		r=2 p2(int i,double c);
		r=3 p3(int i,int j,double k);
		*/
		private int t;
		//data
		private int i;
		private int j;
		private double r;
		public PrimaryChange(int i,int j){
			t = 1;
			this.i = i;
			this.j = j;
		}
		public PrimaryChange(int i,double c){
			t = 2;
			this.i = i;
			this.r = c;
		}
		public PrimaryChange(int i,int j,double k){
			t = 3;
			this.i = i;
			this.j = j;
			this.r = k;
		}
		public int readt() {
			// TODO Auto-generated method stub
			return t;
		}
		public int readi() {
			// TODO Auto-generated method stub
			return i;
		}
		public int readj() {
			// TODO Auto-generated method stub
			return j;
		}
		public double readr() {
			// TODO Auto-generated method stub
			return r;
		}

	}
	
	/*
	how to use p1
	begin from m_next,n_next
	t finds the first line whose first element isn't zero
	-1 means all zero
	it carries out p1 when t becomes i and exchanges the line of m_next and t
	so the effect of sortof0 is putting the full line up
	and it returns the column
	*/ 
	public int sortof0(int m_next,int n_next){
		int t = -1;
		int j = n_next;
		while( ((t==-1)&&(j<n)) ){
			for(int i = m_next;i<m;i++)
				if(!if0( l[i].getaj(j) )){
					t = i;
					break;
				}
			if(t==-1) j++;
		}
		if(t!=-1) {
			p1(m_next,t);
			return j;
		}
		return t;
	}
	/*
	how to use p3
	use sortof0 to get the column
	then it carries out p3 to turn the first element of all later lines to zero
	stand means the first element isn't zero
	whichLine means the corresponding line
	the next loop aims to simplify the equation mostly using p3
	and it returns the number of the first empty line
	*/
	public int simplify(){
		int t;
		int i = 0;
		int j = 0;
		while( ((i<m)&&(j<n)) ){
			t = sortof0(i,j);
			if(t==-1) break;
			j = t;
			for(int u = i+1;u<m;u++){
				p3(i,u, (-1*l[u].getaj(j)/l[i].getaj(j)) );
			}
			stand[j] = true;
			whichLine[j] = i;
			i++;
			j++;
		}
		
		for(j = n-1;j>=0;j--)
			for(i = 0;i<whichLine[j];i++)
				p3( whichLine[j], i, (-1*l[i].getaj(j)/l[whichLine[j]].getaj(j)) );
		
		return i;
	}
	/*
	how to use p2
	exist = true
	the right side has more number -> false
	true ->
		calculate f
		the value of stand[j]
		true ->
			use p2 to simplify
			calculate it
		false ->
			choose the free variables to 0!
	*/
	public boolean check(){
		int m_zero = simplify();
		boolean exist = true;
		for(int i = m_zero;i<m;i++)
			if( !if0(l[i].getb()) )
				exist = false;
		if(exist){
			jie = new double[n];
			for(int j = 0;j<n;j++)
				f &= stand[j];
			for(int j = n-1;j>=0;j--)
				if(stand[j]){
					p2(whichLine[j],1/l[whichLine[j]].getaj(j));
					jie[j] = linearEqu.result(l[whichLine[j]],j,jie);
				}else{
					jie[j] = 0;
				}
		}
		return exist;
	}
	
	/*
	solutionSpace
	t- - - 
	r  j
	 - - - jKer 
	 - - -
	
	0100000
	0010000
	0000100
	0000010
	TFFTFFT
	*/
	public double[][] result(){
		int dimKer = n - lines();
		double[] t = new double[n];
		double[][] r = new double[dimKer][n];
		int jKer = 0;
		if(!f){
			for(int j = 0;j<n;j++){
				if(!stand[j]){
					t[j] = 1;
					for(int u = n-1;u>=0;u--)
						if(stand[u])
							t[u] = linearEqu.result(l[whichLine[u]],u,t);
				}
				r[jKer++] = t;
			}
		}else{
			r = null;
		}
		return r;
	}
	public static dimVector[] solutionSpace(int dim,int n,dimVector[] as,dimVector b){
		linearEqus l = dimVector.Equize(dim,n,as,b);
		boolean exist = l.check();
		boolean f = l.getf();
		int dimKer = n-l.lines();
		double[][] t = l.result();
		dimVector[] r = null;
		if(exist)
			if(!f){
				r = new dimVector[dimKer];
				for(int j = 0;j<dimKer;j++)
					r[j] = new dimVector(n,t[j]);			
			}
		return r;
	}
	public static dimVector solutionVector(int dim,int n,dimVector[] as,dimVector b){
		linearEqus l = dimVector.Equize(dim,n,as,b);
		boolean exist = l.check();
		dimVector r = null;
		if(exist)
			r = new dimVector(n,l.getjie());
		return r;
	}
}

