// **************************************
// Class: Coordinate.java
// Code author: David Nguyen, Allen Luo
// Last modified: 05/10/2011
// **************************************

public class Coordinate 
{
	public Integer row;
	public Integer col;

	public Coordinate(int r, int c) {
		row = r;
		col = c;
	}

	public boolean equals(Object other) 
	{
		if (other instanceof Coordinate) {
			Coordinate otherCoordinate = (Coordinate) other;
			return (( this.row == otherCoordinate.row ||
					( this.row!= null && otherCoordinate.row!= null && this.row.equals(otherCoordinate.row))) &&
					( this.col == otherCoordinate.col || 
							( this.col != null && otherCoordinate.col != null && this.col.equals(otherCoordinate.col))));
		}
		return false;
	}
	
    public Integer getRow(){ return row; }
    public Integer getCol(){ return col; }
    public void setRow(Integer r){ this.row = r; }
    public void setCol(Integer c){ this.col = c; }	
	
	public String toString() {
		return "Move: (" + row + ", " + col + ")";
	}

}
