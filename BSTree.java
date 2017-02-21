package com.myjava.datastruct.bst;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import com.myjava.datastruct.bst.BSTreeNode;

public class BSTree <T extends Comparable<T>>{
			final private  BSTreeNode<T> root;
			// 节点数
			AtomicLong size=new AtomicLong(0);
			
			// 覆盖模式时,所有节点没有相同的值
			// 非覆盖模式时,节点可以有相同值,建议不要使用非覆盖模式
			private volatile boolean overrideMode=true;
			
			public BSTree(){
				this.root=new BSTreeNode<T>();
			}
			
			public BSTree(boolean overrideMode){
				this();
				this.overrideMode=overrideMode;
			}
			
			public boolean isOverrideMode(){
				return overrideMode;
			}
			
			public void setOverrideMode(boolean overrideMode){
				this.overrideMode=overrideMode;
			}
			
			/**
			 * 树的节点数
			 * @return
			 */
			public long getSize(){
				return size.get();
			}
			
			/**
			 * 获取根节点
			 * @return
			 */
			private BSTreeNode<T> getRoot(){
				return root.getLeft();
			}
			
			/**
			 * 增加一个新的节点
			 * 如果值存，则返回存在值；否则返回空。
			 * 如果处于覆盖模式且值已经存在树中
			 * 将要覆盖旧值
			 * 
			 * @param value
			 * @return
			 */
			public T addNode(T value){
				BSTreeNode<T> t=new BSTreeNode<T>(value);
				return addNode(t);
			}
			
			/**
			 * 给定键值查找节点(包含key,key用来查找,
			 *其他变量不使用,@see compare method).如果不存在，返回空
			 * @param key
			 * @return
			 */
			public BSTreeNode<T> find(T key){
				BSTreeNode<T> dataRoot=new BSTreeNode<T>();
				dataRoot=getRoot();
				while(dataRoot!=null){
					int cmp=dataRoot.getValue().compareTo(key);
					if(cmp==0){
						return dataRoot;
					}else if(cmp<0){
						dataRoot=dataRoot.getLeft();
					}else {
						dataRoot=dataRoot.getRight();
					}
				}
				return null;
			}
			
			private T addNode(BSTreeNode<T> node){
				node.setLeft(null);
				node.setRight(null);
				setParent(node,null);
				if(root.getLeft()==null){
					root.setLeft(node);
					size.incrementAndGet();
				}
				else{
			           BSTreeNode<T> x = findParentNode(node); 
			            int cmp = x.getValue().compareTo(node.getValue()); 
			 
			            if(this.overrideMode && cmp==0){ 
			                T v = x.getValue(); 
			                x.setValue(node.getValue()); 
			                return v; 
			            }else if(cmp==0){ 
			                //已经有该值，则忽略
			                return x.getValue(); 
			            } 
			 
			            setParent(node,x); 
			 
			            if(cmp>0){ 
			                x.setLeft(node); 
			            }else{ 
			                x.setRight(node); 
			            } 
			 
			            size.incrementAndGet(); 
				}
				return null;
			}
		    /** 
		     * 找到节点x的父节点
		     * @param x 
		     * @return 
		     */ 
		    private BSTreeNode<T> findParentNode(BSTreeNode<T> x){ 
		        BSTreeNode<T> dataRoot = getRoot(); 
		        BSTreeNode<T> child = dataRoot; 
		 
		        while(child!=null){ 
		            int cmp = child.getValue().compareTo(x.getValue()); 
		            if(cmp==0){ 
		                return child; 
		            } 
		            if(cmp>0){ 
		                dataRoot = child; 
		                child = child.getLeft(); 
		            }else if(cmp<0){ 
		                dataRoot = child; 
		                child = child.getRight(); 
		            } 
		        } 
		        return dataRoot; 
		    } 
		    
			private void setParent(BSTreeNode<T> node,BSTreeNode<T> parent){
				if(node!=null){
					node.setParent(parent);
					if(node==root){
						node.setParent(null);
					}
				}
			}
			
			/**
			 * 给定一个键值，移除节点；如果该值不在树中，则返回空
			 * @param value 包含键值
			 * @return the value 移除节点中键值
			 */
			public  T removeNode  (T value)
			{
				BSTreeNode<T> dataRoot=getRoot();
				BSTreeNode<T> parent=root;
				while(dataRoot!=null)
				{
					int cmp=dataRoot.getValue().compareTo(value);
					if(cmp>0)
					{
						parent=dataRoot;
						dataRoot=dataRoot.getLeft();
					}
					else if(cmp<0)
					{
						parent=dataRoot;
						dataRoot=dataRoot.getRight();
					}
					else // 找到删除节点
					{
						if( dataRoot.getRight()!=null){ //右子节点不为空
							BSTreeNode<T> min=removeMinNode(dataRoot.getRight());
							min.setLeft(dataRoot.getLeft()); 
							setParent(dataRoot.getLeft(),min); 
							if(parent.getLeft()==dataRoot){ 
								parent.setLeft(min); 
							}else{ 
								parent.setRight(min); 
							} 
							setParent(min,parent); 
							if(min!=dataRoot.getRight()){ 
								min.setRight(dataRoot.getRight()); 
								setParent(dataRoot.getRight(),min);
							}
						}else{  // 右子节点为空，特殊情况左子节点为空包含在内
							setParent(dataRoot.getLeft(),parent);
							if(parent.getLeft()==dataRoot){
								parent.setLeft(dataRoot.getLeft());
							}else
							{
								parent.setRight(dataRoot.getLeft());
							}
						}
						setParent(dataRoot,null);
						dataRoot.setLeft(null);
						dataRoot.setRight(null);
						if(getRoot()!=null){
							setParent(getRoot(),null);
						}
		                size.decrementAndGet(); 
		                return dataRoot.getValue(); 
					}
				}
				return null;
			}
			
			/**
			 *找到后继节点
			 * @param node 待删除节点的右子节点
			 * @return
			 */
			private BSTreeNode<T> removeMinNode(BSTreeNode<T>node){
				// 找到右子节点中最小的节点
				BSTreeNode<T> parent=node;
				while(node!=null && node.getLeft()!=null){
					parent=node;
					node=node.getLeft();
				}
		        //remove min node 
		        if(parent==node){ 
		        	return node;
		        } 
		         parent.setLeft(node.getRight());
		         setParent(node.getRight(),parent);
		        return node; 
			}
			
			/**
			 * debug mode,该方法用来打印给定的节点以及其所有子节点
			 * 每一层输出在同一行
			 * @param nroot
			 */
			public void printTree(BSTreeNode<T> nroot){
				LinkedList<BSTreeNode<T>> queue=new LinkedList<BSTreeNode<T>>();
				LinkedList<BSTreeNode<T>> queue2=new LinkedList<BSTreeNode<T>>();
				if(nroot==null){
					return ;
				}
				queue.add(nroot);
				boolean firstQueue =true;
				
				while(!queue.isEmpty() || !queue2.isEmpty()){
					LinkedList<BSTreeNode<T>> q=firstQueue?queue:queue2;
					BSTreeNode<T> n=q.poll();
					if(n!=null){
						String pos=n.getParent()==null ? "": (n==n.getParent().getLeft()?"LE":"RI");
						String pstr=n.getParent()==null?"":n.getParent().toString();
						System.out.print(n+"("+pstr+" "+(pos)+")"+"\t");
						if(n.getLeft()!=null){
							(firstQueue?queue2:queue).add(n.getLeft());
						}
						if(n.getRight()!=null){
							(firstQueue?queue2:queue).add(n.getRight());
						}
					}else{
						System.out.println();
						firstQueue=!firstQueue;
					}
				}
				System.out.println("\n");
			}
			public static void main(String[] args){
				BSTree<String> bst=new BSTree<String>();
				bst.addNode("k");
				bst.addNode("e");
				bst.addNode("c");
				bst.addNode("d");
				bst.addNode("b");
				bst.addNode("f");
				bst.printTree(bst.getRoot());
				
				bst.addNode("a");
				bst.addNode("e");
				bst.printTree(bst.getRoot());
				
				bst.addNode("g");
				bst.addNode("h");
				bst.printTree(bst.getRoot());
				
				bst.removeNode("d");
				bst.printTree(bst.getRoot());
			}
}
















