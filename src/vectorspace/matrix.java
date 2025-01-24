package vectorspace;

public class matrix implements linearSpace<matrix>{
	//you need to understand its meaning
	private double[][] A;
	//row
	private int m;
	//column
	private int n;
	public matrix(int m,int n,double[][] A){
		this.m = m;
		this.n = n;
		this.A = A;
	}
	//rowVector!!!
	public matrix(dimVector[] vs){
		this.m = vs.length;
		this.n = vs[0].getdim();
		A = new double[m][n];
		for(int i = 0;i<m;i++)
			A[i] = vs[i].geta();
	}
	
	//get set
	public double getaij(int i,int j){
		return A[i][j];
	}
	public double[][] geta(){
		return A.clone();
	}
	public int getm(){
		return m;
	}
	public int getn(){
		return n;
	}
	public void show(){
		for(int i = 0;i<m;i++){
			for(int j = 0;j<n;j++)
				System.out.print(A[i][j]+" ");			
			System.out.println();
		}

	}
	public int rank(){
		int r = -1;
		if(rowRank()==columnRank())
			r = rowRank();
		return r;
	}
	public dimVector[] getRowVector(int[] index){
		dimVector[] r = new dimVector[index.length];
		for(int i = 0;i<index.length;i++)
			r[i] = new dimVector(n,A[index[i]]);
		return r;
	}
	public dimVector[] getColumnVector(int[] index){
		transpose();
		dimVector[] r = getRowVector(index);
		transpose();
		return r;
	}
	private int rowRank(){
		int[] index = new int[m];
		for(int i = 0;i<m;i++)
			index[i] = i;
		return dimVector.rank(n,m,getRowVector(index));
	}
	private int columnRank(){
		transpose();
		int r = rowRank();
		transpose();
		return r;
	}
	
	//transformations
	public void p1(int i,int j){
		double[] t = A[i].clone();
		A[i] = A[j].clone();
		A[j] = t;
	}
	public void p2(int i,double c){
		for(int j = 0;j<n;j++)
			A[i][j] *= c;
	}
	// i j -> i j+k*i
	public void p3(int i,int j,double k){
		for(int u = 0;u<n;u++)
			A[j][u] += k*A[i][u];
	}
	public void q1(int i,int j){
		transpose();
		p1(i,j);
		transpose();
	}
	public void q2(int i,double c){
		transpose();
		p2(i,c);
		transpose();
	}
	// i j -> i j+k*i
	public void q3(int i,int j,double k){
		transpose();
		p3(i,j,k);
		transpose();
	}
	
	//linearSpace
	public matrix timesk(double k){
		for(int i = 0;i<m;i++)
			for(int j = 0;j<n;j++)
				A[i][j] *= k;
		return this;
	}
	public matrix plus(matrix B){
		for(int i = 0;i<m;i++)
			for(int j = 0;j<n;j++)
				A[i][j] += B.getaij(i,j);
		return this;
	}
	public matrix O(){
		return new matrix(m,n,new double[m][n]);
	}
	public matrix N(){
		double[][] A_ = new double[m][n];
		for(int i = 0;i<m;i++)
			for(int j = 0;j<n;j++)
				A_[i][j] = this.getaij(i,j)*-1;
		return new matrix(m,n,A_); 
	}
	public boolean ifinfinite(){
		return false;
	}
	public int getdim(){
		return m*n;
	}
	public matrix[] defaultBase(){
		matrix[] r = new matrix[m*n];
		for(int i = 0;i<m*n;i++){
			r[i] = O();
			r[i].A[i/n][i%n-1] = 1;
		}
		return r;
	}
	public dimVector coordinate(matrix[] base){		
		dimVector t = new dimVector(m*n,vectorize(m,n,A));
		dimVector[] base_ = new dimVector[base.length];
		for(int i = 0;i<base_.length;i++)
			base_[i] = new dimVector(m*n,vectorize(m,n,base[i].A));
		return t.coordinate(base_);
	}
	public matrix element(matrix[] base,dimVector v){
		matrix r = O();
		for(int i = 0;i<base.length;i++)
			 r.plus(base[i].timesk(v.geta()[i]));
		return r;
	}
	
	//static
	/*
	* *    1
	* * -> 1
	       1
	   	   1
	*/
	private static double[] vectorize(int m,int n,double[][] A){
		double[] d = new double[m*n];
		for(int i = 0;i<m;i++)
			for(int j = 0;j<n;j++)
				d[i*n+j] = A[i][j];
		return d;
	}
	public static matrix times(matrix A,matrix B){
		double[][] r = new double[A.m][B.n];
		for(int i = 0;i<A.m;i++)
			for(int j = 0;j<B.n;j++)
				for(int u = 0;u<A.n;u++)
					r[i][j] += A.getaij(i,u)*B.getaij(u,j);
		return new matrix(A.m,B.n,r);
	}
	public void transpose(){
		double[][] AT = new double[n][m];
		for(int i = 0;i<m;i++)
			for(int j = 0;j<n;j++)
				AT[j][i] = getaij(i,j);
		A = AT;
		int t = m;
		m = n;
		n = t;
		//A = new matrix(n,m,AT);
	}
	public static boolean exist(matrix A,matrix A_){
		return A.rank()==A_.rank();
	}
}
