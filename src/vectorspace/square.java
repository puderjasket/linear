package vectorspace;

import java.util.ArrayList;

import classic.Primary;
import classic.linearEqus;

public class square extends matrix {
	private int n;
	public square(int n,double[][] A){
		super(n,n,A);
		this.n = n;
	}
	//E
	private square(int n){
		this(n,E(n));
	}
	public square(matrix A){
		this(A.getn(),A.geta());
	}
	
	//static give standard matrix
	private static double[][] E(int n){
		double[][] r = new double[n][n];		
		for(int i = 0;i<n;i++)
			for(int j = 0;j<n;j++)
				if(i==j)
					r[i][j] = 1;
		return r;
	}
	public static matrix sp1(int n,int i,int j){
		double[][] r = E(n);
		r[i][i] = 0;
		r[j][j] = 0;
		r[i][j] = 1;
		r[j][i] = 1;
		return new matrix(n,n,r);
	}
	public static matrix sp2(int n,int i,double c){
		double[][] r = E(n);
		r[i][i] = c;
		return new matrix(n,n,r);
	}
	//i j -> i j+k*i
	public static matrix sp3(int n,int i,int j,double k){
		double[][] r = E(n);
		r[j][i] = k;
		return new matrix(n,n,r);
	}
	public static matrix D(int n,int rank){
		double[][] r = new double[rank][rank];		
		for(int i = 0;i<rank;i++)
			for(int j = 0;j<rank;j++)
				if(i==j)
					r[i][j] = 1;
		return new matrix(n,n,r);		
	}
	
	public boolean reversible(){
		return super.rank()==n;
	}
	public square inverse(){
		int[] index = new int[n];
		double[] b = new double[n];
		for(int j = 0;j<n;j++)
			index[j] = j;
		linearEqus l = dimVector.Equize(n,n,getColumnVector(index),new dimVector(n,b));
		ArrayList<? extends Primary> p = l.getp();
		square r = new square(n); 
		for(int i = 0;i<p.size();i++){
			Primary t = p.get(i);
			matrix s;
			if(t.readt()==1){
				s = square.sp1(n,t.readi(),t.readj());
			}else if(t.readt()==2){
				s = square.sp2(n,t.readi(),t.readr());
			}else{
				s = square.sp3(n,t.readi(),t.readj(),t.readr());
			}
			r = new square(super.times(s,r));			
		}
		return r;
	}
}
