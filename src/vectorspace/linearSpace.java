package vectorspace;

//T means the type of the element in the set(linearSpace)
public interface linearSpace<T> {
	//operations
	public abstract T timesk(double k);
	public abstract T plus(T t);
	//zero and negative vector
	public abstract T O();
	public abstract T N();
	//dimension
	public abstract boolean ifinfinite();
	public abstract int getdim();
	//give a group of elements that have nice situations
	//if its dimension is infinite it should return null(haven't written it)
	public abstract T[] defaultBase();
	
	//transformation(so the definition of dimVector must be prior to others)
	//base + element -> coordinate
	public abstract dimVector coordinate(T[] base);
	//base + coordinate -> element
	public abstract T element(T[] base,dimVector v);
}
