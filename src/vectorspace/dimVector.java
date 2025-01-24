package vectorspace;

import classic.*;


public class dimVector implements linearSpace<dimVector>{
	//column
	private int dim;
	private double[] a;
	public dimVector(int dim,double[] a){
		this.dim = dim;
		this.a = a;
	}
	
	//get set
	public static boolean if0(dimVector v){
		boolean f =  true;
		for(int i = 0;i<v.getdim();i++)
			f &= linearEqus.if0(v.geta()[i]);
		return f;
	}
	public double[] geta(){
		return a;
	}
	public void show(){
		for(int i = 0;i<dim;i++)
			System.out.print(a[i]+" ");
		System.out.println();
	}

	//linearSpace
	public dimVector timesk(double k){
		for(int i = 0;i<dim;i++)
			a[i] *= k;
		return this;
	}
	public dimVector plus(dimVector v){
		for(int i = 0;i<dim;i++)
			a[i] += v.geta()[i];
		return this;
	}
	public dimVector O(){
		return new dimVector(dim,new double[dim]);
	}
	public dimVector N(){
		double[] a_ = new double[dim];
		for(int i = 0;i<dim;i++) a_[i] = -1*a[i];
		return new dimVector(dim,a_); 
	}
	public boolean ifinfinite(){
		return false;
	}
	public int getdim(){
		return dim;
	}
	public dimVector[] defaultBase(){
		dimVector[] r = new dimVector[dim];
		for(int i = 0;i<dim;i++){
			r[i] = O();
			r[i].a[i] = 1;
		}
		return r;
	}
	public dimVector coordinate(dimVector[] base){
		return new dimVector(dim,Equize(dim,base.length,base,this).getjie());
	}
	public dimVector element(dimVector[] base,dimVector v){
		dimVector r = O();
		for(int i = 0;i<base.length;i++)
			 r.plus(base[i].timesk(v.geta()[i]));
		return r;
	}
	
	//static
	//for the definitions
	/*
	1 1    - -
	1 1 -> - -
	turn columnVectors to linearEqus
	*/
	public static linearEqus Equize(int dim,int n,dimVector[] as,dimVector b){
		linearEqu[] l = new linearEqu[dim];
		double[] t = new double[n];
		for(int i = 0;i<dim;i++){
			for(int j = 0;j<n;j++)
				t[j] = as[j].geta()[i];
			l[i] = new linearEqu(n,t.clone(),b.geta()[i]);
		}
		return new linearEqus(dim,l);
	}
	public static boolean linearExpress(int dim,int n,dimVector[] as,dimVector b){
		return Equize(dim,n,as,b).check();
	}
	public static boolean linearIndepence(int dim,int n,dimVector[] as){
		linearEqus l = Equize(dim,n,as,new dimVector(dim,new double[dim]));
		l.check();
		return l.getf();
	}
	public static boolean linearEquals(
		int dim,int n1,int n2,dimVector[] a1s,dimVector[] a2s
	){
		boolean f = true;
		for(int j = 0;j<n2;j++)
			f &= linearExpress(dim,n1,a1s,a2s[j]);
		for(int j = 0;j<n1;j++)
			f &= linearExpress(dim,n2,a2s,a1s[j]);
		return f;
	}
	public static int rank(int dim,int n,dimVector[] as){
		linearEqus l = Equize(dim,n,as,new dimVector(dim,new double[dim]));
		l.check();
		return l.lines();
	}
	/*
	public static dimVector[] fakeBase(int dim,int n,dimVector[] as){
		int j = 0;
		while( if0(as[j]) ) j++;
		dimVector[] r = new dimVector[rank(dim,n,as)];
		int jr = 0;
	    r[jr++] = as[j++];
		for(;j<n;j++)
			if( !linearExpress(dim,jr,Arrays.copyOfRange(r,0,jr),as[j]) )
				r[jr++] = as[j];
		return r;
	}
	*/

}
