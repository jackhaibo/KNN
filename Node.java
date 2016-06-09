package KNN;

public class Node {
	private double data[];
	private Node left;//左子树
	private Node right;//右子树
	private double distance;
	
	
	
	public Node(double [] data){
		this.data=data;
	}
	public double getIndex(int index){
		return data[index];
	}
	
	public double[] getData() {
		return data;
	}
	public void setData(double[] data) {
		this.data = data;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
