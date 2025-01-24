package classic;

import vectorspace.dimVector;
import vectorspace.linearSpace;

public class linearEqu implements linearSpace<linearEqu>{
	
	//n represents the number of columns(or a.length) 
	private int n; 
	//a represents each number of left side
	private double[] a;
	//b represents the number of right side
	private double b;
	//constructor(how to create it)
	public linearEqu(int n,double[] a,double b){
		this.n = n;
		this.a = a;
		this.b = b;
	}
	
	//get set
	public double getaj(int j){
		return a[j];
	}
	public linearEqu get(){
		//notice that double[] a must be cloned
		return new linearEqu(n,a.clone(),b);
	}
	public double getb(){
		return b;
	}
	public int getn(){
		return n;
	}
	public int getdim(){
		return n+1;
	}
	public void caj(int j,double a){
		this.a[j] = a;
	}
	public void cb(int b) {
		this.b = b;
	}
	
	//linearSpace
	public linearEqu timesk(double k){
		for(int i = 0;i<n;i++)a[i] *= k;
		b *= k;
		return this;
 	}
	public linearEqu plus(linearEqu l){
		for(int i = 0;i<n;i++)a[i] += l.getaj(i);
		b += l.getb();
		return this;
	}
	public linearEqu O(){
		return new linearEqu(n,new double[n],0);
	}
	public linearEqu N() {
		return this.get().timesk(-1);
	}
	public boolean ifinfinite(){
		return false;
	}
	public linearEqu[] defaultBase(){
		linearEqu[] r = new linearEqu[n+1];
		for(int i = 0;i<n;i++){
			r[i] = O();
			r[i].a[i] = 1;
		}
		r[n] = O();
		r[n].b = 1;
		return r;
	}
	public dimVector coordinate(linearEqu[] base){
		dimVector t = new dimVector(n+1,vectorize(n,a,b));
		dimVector[] base_ = new dimVector[base.length];
		for(int i = 0;i<base_.length;i++)
			base_[i] = new dimVector(n+1,vectorize(n,base[i].a,base[i].b));
		return t.coordinate(base_);
	}
	public linearEqu element(linearEqu[] base,dimVector v){
		linearEqu r = O();
		for(int i = 0;i<base.length;i++)
			 r.plus(base[i].timesk(v.geta()[i]));
		return r;
	}
	
	//static
	/*
	-- - -> ---
	*/
	private static double[] vectorize(int n,double[] a,double b){
		double[] d = new double[n+1];
		for(int i = 0;i<n;i++)
			d[i] = a[i];
		d[n] = b;
		return d;
	}
	public static double result(linearEqu l,int j_,double[] x){
		double r = l.b;
		for(int j = 0;j<l.n;j++)
			if(j==j_){
				continue;
			}else{
				r -= l.a[j]*x[j];
			}
		r /= l.a[j_];
		return r;
	}
}
