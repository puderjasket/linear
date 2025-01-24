package apply;

import classic.*;
import vectorspace.*;

public class test {
	public static void main(String[] args) {
		double[][] data = {
			{1,0,1},
			{-1,1,1},
			{2,-1,1}
		};
		dimVector[] d = new dimVector[3];
		d[0] = new dimVector(3,data[0]);
		d[1] = new dimVector(3,data[1]);
		d[2] = new dimVector(3,data[2]);
		matrix m = new matrix(d);
		square s = new square(m);
		s.inverse().show();
	}
}
