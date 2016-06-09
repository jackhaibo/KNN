package KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class KdKnn {
	
	private Node buildKDTree(List<Node> nodeList,int dimen){
		
		if(nodeList.size()==0)
			return null;
		quicksort(nodeList,0,nodeList.size()-1,dimen);
		int median=nodeList.size()/2;
		Node root=nodeList.get(median);
		List<Node> leftRange=new ArrayList<Node>();
		List<Node> rightRange=new ArrayList<Node>();
		for(Node node: nodeList){
			if(node!=root){
				if(node.getIndex(dimen)<root.getIndex(dimen)){
					leftRange.add(node);
				}else {
					rightRange.add(node);
				}
					
			}
		}
		int newDimen=(++dimen)%2;
		
		root.setLeft(buildKDTree(leftRange,newDimen));
	    root.setRight(buildKDTree(rightRange, newDimen));
	    
	    	
		return root;
	}
	
	private void quicksort(List<Node> nodeList,int left,int right,int dimen){
		
		if(left<right){
			int q=partition(nodeList, left, right, dimen);
			quicksort(nodeList, left, q-1, dimen);
			quicksort(nodeList, q+1, right, dimen);
		}
		
		
	}
	
	private int partition(List<Node> nodeList, int left, int right,int dimen) {
		
		double x=nodeList.get(right).getIndex(dimen);
		int i=left-1;
		for(int j=left;j<right;j++){
			if(nodeList.get(j).getIndex(dimen)<x){
				i++;
				//交换i与j位置的节点
				Collections.swap(nodeList, i, j);
				
				
			}
		}
		Collections.swap(nodeList, i+1, right);
		
		return i+1;
	}

	//基本思想：从根节点开始搜索，搜索过程中顺便把搜索路径经过节点的“反向节点”加入先序队列中。搜索到达叶节点时候，这个叶节点暂时是
	
	//距离target节点最近的节点，计算distance。随后调整堆，按照distance的大小，小的在堆顶，只需要调整一次堆即可，即top1。
	
	//与堆顶元素进行比较，如果叶节点distance大于堆顶节点，则最近的节点便是堆顶元素，否则亦然。
	//总之：1、只计算叶节点          2、只检查其中一些叶节点
	//July说要把root节点放入先序队列。。。为什么要放进去？
	private Node searchKNN(Node root,Node target,int dimen){
		
		double Max_dist=0;

		Node nearest=null;
		
		List<Node> pirorList=new ArrayList<Node>();
	    					
			Node Kd_point=root;
			int max_steps=0;
			while(max_steps<200){
				int d=(dimen++)%2;				
				if(target.getIndex(d)<Kd_point.getIndex(d)){     //进入左子树

					if(Kd_point.getRight()!=null){      //将右子树存入先序队列
						
						Kd_point.getRight().setDistance(distance(target,Kd_point.getRight()));
						pirorList.add(Kd_point.getRight());
					}
					    
					Kd_point=Kd_point.getLeft();
					
				}else{                                          

					if(Kd_point.getLeft()!=null){                //将左子树加入先序队列
						Kd_point.getLeft().setDistance(distance(target,Kd_point.getLeft()));
						pirorList.add(Kd_point.getLeft());
						
					}
					     
					Kd_point=Kd_point.getRight();                //进入右子树
				}
				
				
				max_steps++;
				
				if(Kd_point.getRight()==null&&Kd_point.getLeft()==null){       //扫描到了叶节点
					Max_dist=distance(Kd_point,target);
					Kd_point.setDistance(Max_dist);
					nearest=Kd_point;
					break;
				}				
			}
			
			maintainHeap(pirorList);     //只调整一次堆就可以了
			
			if(pirorList.get(0).getDistance()<Max_dist)
				nearest=pirorList.get(0);
		
		return nearest;
	}
	
	private void maintainHeap(List<Node> pirorList) {
		
		for(int i=pirorList.size()/2-1;i>-1;i--){
		     fixHeap(pirorList,i);
		}
		
	}

	private void fixHeap(List<Node> pirorList, int root) {		
		int left=2*root+1;
		int right=2*root+2;
		int min=root;
		if(left<pirorList.size()&&pirorList.get(min).getDistance()>pirorList.get(left).getDistance())
			min=left;
		if(right<pirorList.size()&&pirorList.get(min).getDistance()>pirorList.get(right).getDistance())
			min=right;
		Collections.swap(pirorList, min, root);
		if(root!=min){
			fixHeap( pirorList,  min);
		}
		
	}

	private double distance(Node a,Node b){
		double dist=0;
		double [] A=a.getData();
		double [] B=b.getData();
		for(int i=0;i<A.length;i++)
			dist+=Math.pow(A[i]-B[i], 2);
		return Math.sqrt(dist);
	}


	

	public static void main(String[] args) {
		List<Node> nodeList=new ArrayList<Node>();
		nodeList.add(new Node(new double[]{2,3}));
		nodeList.add(new Node(new double[]{5,4}));
	    nodeList.add(new Node(new double[]{9,6}));
	    nodeList.add(new Node(new double[]{4,7}));
	    nodeList.add(new Node(new double[]{8,1}));
	    nodeList.add(new Node(new double[]{7,2}));
	    KdKnn nd=new KdKnn();
	    Node root=nd.buildKDTree(nodeList, 0);
	    Node target=new Node(new double[]{2.1,3.1});
	    double [] nea=nd.searchKNN(root, target,0).getData();
	    for(int i=0;i<nea.length;i++)
	           System.out.println(nea[i]);	    
	    System.out.println(nd.searchKNN(root, target,0).getDistance());
	

	}

}
